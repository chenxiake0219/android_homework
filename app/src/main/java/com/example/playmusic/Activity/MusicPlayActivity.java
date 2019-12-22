package com.example.playmusic.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.playmusic.R;
import com.example.playmusic.SQLite.musicTable;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MusicPlayActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    private musicTable dbHelper = new musicTable(this, "Music.db",null,7);

    private List<String> musicList = new ArrayList<>();
    private List<Integer> musicId = new ArrayList<>();
    int songIndex;
    SeekBar musicSeekBar;
    TextView currentPlayMusicName;
    ImageView musicCover;
    ImageView playAndPause;
    ImageView musicNext;
    ImageView musicPrevious;
    TextView title_text;

//    Binder
private MusicPlayService.MusicPlayBinder musicPlayBinder;

    private ServiceConnection connectionMusicPlayBinder = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicPlayBinder = (MusicPlayService.MusicPlayBinder) service;
        }

    };

    private MusicListService.musicListBinder musicListBinder;

    private ServiceConnection connectionMusicListBinder = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicListBinder = (MusicListService.musicListBinder) service;
        }

    };

    private void MusicThread(){
        new Thread(){
            public void run(){
//                该进程一直运行，每个一秒改变seekbar的进度条
                while(true){
                    try{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("test", "run: "+musicPlayBinder.currentPosition());
                                musicSeekBar.setProgress(musicPlayBinder.currentPosition());
                            }
                        });
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }

            }
        }.start();
    }

//    本地广播
    private BroadcastReceiver  bReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            //put here whaterver you want your activity to do with the intent received
            Log.d("test", "onReceive: "+intent.getIntExtra("wholeMusicLength",0));
            if(intent.getIntExtra("wholeMusicLength",0) != 0){
//              设置seekbar的长度为歌曲长度
                musicSeekBar.setMax(intent.getIntExtra("wholeMusicLength",0));
//              seekbar当前进度条实时改变
                MusicThread();
//              seekbar移动的时候歌曲也播放到那里
                musicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if( fromUser){
                            musicPlayBinder.moveSeekBar(progress);
                            playAndPause.setImageResource(R.drawable.icon_pause);
                        }
                    }
                });
            }

//            如果当前的歌播放完了，更新页面信息，歌曲播放在MusicListService给
            if(intent.getBooleanExtra("MusicFinished",false) == true){
                //            换成播放键
                playAndPause.setImageResource(R.drawable.icon_pause);
                if(mediaPlayer != null){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
                if(songIndex+1 <= musicId.size()-1){
                    songIndex++;
                }else{
                    songIndex = 0;
                }
//        更换歌曲封面
                musicCover.setImageResource(getResources().getIdentifier(musicList.get(songIndex), "drawable",  getPackageName()));
//        更换歌曲名
                currentPlayMusicName.setText(musicList.get(songIndex));
            }

        }
    };

    protected void onResume(){
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(bReceiver, new IntentFilter("message"));
    }

    protected void onPause (){
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(bReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);

        musicSeekBar = findViewById(R.id.musicSeekBar);
        currentPlayMusicName = findViewById(R.id.currentPlayMusicName);
        musicCover = findViewById(R.id.musicCover);
        playAndPause = findViewById(R.id.playAndPause);
        musicNext = findViewById(R.id.musicNext);
        musicPrevious = findViewById(R.id.musicPrevious);


//        对歌单列表进行初始化
        int i=0;
        String str = "music"+i;
        Intent intent = getIntent();
        String currentMusicName = intent.getStringExtra("currentMusicName");
        title_text = findViewById(R.id.title_text);
        title_text.setText(currentMusicName);
        while(intent.getStringExtra(str) != null){
            String musicName = intent.getStringExtra(str);
            if(currentMusicName.equals(musicName)){
                songIndex = i;
            }
            musicList.add(musicName);
            i++;
            str = "music"+i;
        }

        if(currentMusicName != null){

//        更换歌曲封面
            musicCover.setImageResource(getResources().getIdentifier(currentMusicName, "drawable",  getPackageName()));
//        更换歌曲名
            currentPlayMusicName.setText(currentMusicName);
//
//        初始化歌单列表
            initMusic();

//        启动服务绑定音乐播放服务，便于后台播放
            Intent MusicPlayintent = new Intent(this, MusicPlayService.class);
            startService(MusicPlayintent); // 启动服务
            bindService(MusicPlayintent, connectionMusicPlayBinder, BIND_AUTO_CREATE); // 绑定服务


//            歌单列表服务
            Intent MusicListIntent = new Intent(this,MusicListService.class);
            for(int j=0;j<musicId.size();j++){
                String string = "music"+j;
                int musicid = musicId.get(j);
                MusicListIntent.putExtra(string,musicid);
            }
            MusicListIntent.putExtra("songIndex",songIndex);
            startService(MusicListIntent);
            bindService(MusicListIntent, connectionMusicListBinder, BIND_AUTO_CREATE); // 绑定服务

        }
    }

    protected void onDestroy(){
        super.onDestroy();
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    //    对歌单列表初始化
    private void initMusic(){
//        初始化为暂停键
        playAndPause.setImageResource(R.drawable.icon_pause);

        int currentMusicFile;
        String musicName;
        for(int i=0;i<musicList.size();i++){
            musicName = musicList.get(i);
            currentMusicFile = getResources().getIdentifier(musicName, "raw",  getPackageName());
            musicId.add(currentMusicFile);
        }
    }



    public void playAndPause(View v){
//        如果当前是暂停键
        if(musicPlayBinder.isPlaying()){
//            换成播放键
            playAndPause.setImageResource(R.drawable.icon_play);
        }else{
//            换成暂停键
            playAndPause.setImageResource(R.drawable.icon_pause);
        }
        musicPlayBinder.playOrPauseMusic();
    }

    public void musicPrevious(View v){
//            换成播放键
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        playAndPause.setImageResource(R.drawable.icon_pause);
        if(songIndex-1 >= 0){
            songIndex--;
        }else{
            songIndex = musicId.size() - 1;
        }

//        更换歌曲封面
        musicCover.setImageResource(getResources().getIdentifier(musicList.get(songIndex), "drawable",  getPackageName()));
//        更换歌曲名
        currentPlayMusicName.setText(musicList.get(songIndex));

        musicListBinder.PreviousMusic();
    }

    public void musicNext(View v){
//            换成播放键
        playAndPause.setImageResource(R.drawable.icon_pause);
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        if(songIndex+1 <= musicId.size()-1){
            songIndex++;
        }else{
            songIndex = 0;
        }
//        更换歌曲封面
        musicCover.setImageResource(getResources().getIdentifier(musicList.get(songIndex), "drawable",  getPackageName()));
//        更换歌曲名
        currentPlayMusicName.setText(musicList.get(songIndex));

        musicListBinder.NextMusic();
    }

}
