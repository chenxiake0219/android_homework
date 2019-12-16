package com.example.playmusic.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.playmusic.R;

import java.io.File;

public class FourthActivity extends AppCompatActivity implements View.OnClickListener{
    private VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fourth_activity);

        videoView = (VideoView) findViewById(R.id.video_view);
        Button videoPlay = (Button) findViewById(R.id.video_play);
        Button videoPause = (Button) findViewById(R.id.video_pause);
        Button videoReplay = (Button) findViewById(R.id.video_replay);
        videoPlay.setOnClickListener(this);
        videoPause.setOnClickListener(this);
        videoReplay.setOnClickListener(this);
        initVideoPath();
        if(ContextCompat.checkSelfPermission(FourthActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(FourthActivity.this,new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            },1);
        }else{
            initVideoPath();
        }
    }

    private void initVideoPath(){
        //String uri = "android.resource://" + getPackageName() + "/" + R.raw.gaojidongwu;
        //videoView.setVideoPath(uri);
        Log.d("test",Environment.getExternalStorageDirectory().toString() );
        File file=new File(Environment.getExternalStorageDirectory(),"我也曾想过一了百了.mp4");
        Log.d("test", file.getPath());
        videoView.setVideoPath(file.getPath());
    }

    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch (requestCode){
            case 1:
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    initVideoPath();
                }else{
                    Toast.makeText(this,"Your permission request is denied",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;

            default:
        }
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.video_play:
                if(!videoView.isPlaying()){
                    videoView.start();
                }
                break;
            case R.id.video_pause:
                if(videoView.isPlaying()){
                    videoView.pause();
                }
                break;
            case R.id.video_replay:
                if(videoView.isPlaying()){
                    videoView.resume();
                }
                break;
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(videoView != null){
            videoView.suspend();
        }
    }
}
