package com.example.playmusic.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.playmusic.R;

import java.io.File;

public class ThirdActivity extends AppCompatActivity implements View.OnClickListener{
    private MediaPlayer mediaPlayer = new MediaPlayer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.third_layout);
        Button play = (Button)findViewById(R.id.playMusic);
        Button pause = (Button)findViewById(R.id.pauseMusic);
        Button stop = (Button)findViewById(R.id.stopMusic);
        Button replay = (Button)findViewById(R.id.replayMusic);
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        stop.setOnClickListener(this);
        replay.setOnClickListener(this);
        if(ContextCompat.checkSelfPermission(ThirdActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ThirdActivity.this,new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            },1);
        }else{
            initMediaPlayer();
        }
    }

    private void initMediaPlayer(){
        try{
            mediaPlayer = MediaPlayer.create(this,R.raw.qianqianquege);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch (requestCode){
            case 1:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    initMediaPlayer();
                }else{
                    Toast.makeText(this,"Your permission request is denied",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;

            default:
        }
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.playMusic:
                if(!mediaPlayer.isPlaying()){
                    mediaPlayer.start();
                    Log.d("test", "onClick: ");
                }
                break;
            case R.id.pauseMusic:
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }
                break;
            case R.id.stopMusic:
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.reset();
                    initMediaPlayer();
                }
                break;
            case R.id.replayMusic:
                if(!mediaPlayer.isPlaying()){
                    mediaPlayer.seekTo(0);
                    mediaPlayer.start();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
