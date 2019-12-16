package com.example.playmusic.Activity;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.SeekBar;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.playmusic.R;

public class MusicPlayService extends Service {

    MediaPlayer mediaPlayer;
    int lastMusicId;

    //    本地广播
    private BroadcastReceiver bReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d("test", "onReceive: "+intent.getIntExtra("newMusicId",0));
            int musicId = intent.getIntExtra("newMusicId",-1);
            if(musicId != -1 && lastMusicId != musicId){
                if(mediaPlayer != null){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
                lastMusicId = musicId;
                mediaPlayer = MediaPlayer.create(getApplicationContext(),musicId);
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new  MediaPlayer.OnCompletionListener(){
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        sendBroadcastMusicFinished();
                    }
                });
                sendBroadcastWholeMusicLength(mediaPlayer.getDuration()/ 1000);
            }else if(lastMusicId == musicId){
                Log.d("test", "lastMusicId == currentMusicId");
                sendBroadcastWholeMusicLength(mediaPlayer.getDuration()/ 1000);
            }
        }
    };

    public MusicPlayService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LocalBroadcastManager.getInstance(this).registerReceiver(bReceiver, new IntentFilter("message"));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        第一次绑定的时候
        int currentMusicId = intent.getIntExtra("currentMusicId",-1);
        if(currentMusicId != -1 ){
//            记录当前的musicid
            lastMusicId = currentMusicId;

            if(mediaPlayer != null){
                mediaPlayer.stop();
                mediaPlayer.release();
            }
            mediaPlayer = MediaPlayer.create(getApplicationContext(),currentMusicId);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new  MediaPlayer.OnCompletionListener(){
                @Override
                public void onCompletion(MediaPlayer mp) {
                    sendBroadcastMusicFinished();
                }
            });
            sendBroadcastWholeMusicLength(mediaPlayer.getDuration()/ 1000);
        }else if(lastMusicId == currentMusicId){
            Log.d("test", "lastMusicId == currentMusicId");
            sendBroadcastWholeMusicLength(mediaPlayer.getDuration()/ 1000);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(bReceiver);
    }



    private MusicPlayBinder musicPlayBinder = new MusicPlayBinder();

    public IBinder onBind(Intent intent){
        return musicPlayBinder;
    }

    class MusicPlayBinder extends Binder {

        public void playOrPauseMusic(){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.pause();
            }else{
                mediaPlayer.start();
            }
        }

        public Boolean isPlaying(){
            if(mediaPlayer.isPlaying()){
                return true;
            }else{
                return false;
            }
        }

        public int currentPosition(){
            return mediaPlayer.getCurrentPosition() / 1000;
        }

        public void moveSeekBar(int progress){
            mediaPlayer.seekTo(progress * 1000);
            if(!mediaPlayer.isPlaying()){
                mediaPlayer.start();
            }
        }
    }

    private void sendBroadcastWholeMusicLength (int wholeMusicLength){
        Intent intent = new Intent ("message");
        intent.putExtra("wholeMusicLength", wholeMusicLength);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendBroadcastMusicFinished (){
        Intent intent = new Intent ("message");
        intent.putExtra("MusicFinished", true);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
