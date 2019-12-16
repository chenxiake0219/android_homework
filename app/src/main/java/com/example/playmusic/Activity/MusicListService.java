package com.example.playmusic.Activity;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;

public class MusicListService extends Service {

    private List<Integer> musicId = new ArrayList<>();
    int songIndex;

    private MusicListService.musicListBinder musicListBinder = new musicListBinder();

    public IBinder onBind(Intent intent){
        return musicListBinder;
    }

    class musicListBinder extends Binder{
        public void PreviousMusic(){
            if(songIndex-1 >= 0){
                songIndex--;
            }else{
                songIndex = musicId.size() - 1;
            }
            sendBroadcastNewMusicId(musicId.get(songIndex));
        }

        public void NextMusic(){
            if(songIndex+1 <= musicId.size()-1){
                songIndex++;
            }else{
                songIndex = 0;
            }
            sendBroadcastNewMusicId(musicId.get(songIndex));
        }
    }

    //    本地广播
    private BroadcastReceiver bReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getBooleanExtra("MusicFinished",false) == true){
                if(songIndex+1 <= musicId.size()-1){
                    songIndex++;
                }else{
                    songIndex = 0;
                }
                sendBroadcastNewMusicId(musicId.get(songIndex));
            }
        }
    };

    public MusicListService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //        对歌单列表进行初始化
        int i=0;
        String str = "music"+i;
        songIndex = intent.getIntExtra("songIndex",-1);

        while(intent.getIntExtra(str,0) != 0){
            int musicid = intent.getIntExtra(str,0);
            musicId.add(musicid);
            Log.d("test", "onStartCommand: "+musicid);
            i++;
            str = "music"+i;
        }
        sendBroadcastNewMusicId(musicId.get(songIndex));
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LocalBroadcastManager.getInstance(this).registerReceiver(bReceiver, new IntentFilter("message"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(bReceiver);
    }

    private void sendBroadcastNewMusicId (int newMusicId){
        Intent intent = new Intent ("message");
        intent.putExtra("newMusicId", newMusicId);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
