package com.example.playmusic.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.playmusic.R;
import com.example.playmusic.Adapter.Software;
import com.example.playmusic.Adapter.SoftwareAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class  SecondActivity extends AppCompatActivity {
//    private String[] software = {"taobao","alipay","google","facebook","instagram","twttier","youtube"};
    private List<Software> softwareList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_layout);

        initSoftware();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        SoftwareAdapter adapter = new SoftwareAdapter(softwareList);
        recyclerView.setAdapter(adapter);
    }

    private void initSoftware(){
        for(int i=0;i<200;i++){
            Software taobao = new Software(getRadomLengthName("taobao"),R.drawable.taobao);
            softwareList.add(taobao);
            Software alipay = new Software(getRadomLengthName("alipay"),R.drawable.alipay);
            softwareList.add(alipay);
            Software google = new Software(getRadomLengthName("google"),R.drawable.google_img);
            softwareList.add(google);
            Software facebook = new Software(getRadomLengthName("facebook"),R.drawable.facebook);
            softwareList.add(facebook);
            Software instagram = new Software(getRadomLengthName("instagram"),R.drawable.instagram);
            softwareList.add(instagram);
            Software twttier = new Software(getRadomLengthName("twttier"),R.drawable.twttier);
            softwareList.add(twttier);
            Software youtube = new Software(getRadomLengthName("youtube"),R.drawable.youtube);
            softwareList.add(youtube);
        }
    }

    private String getRadomLengthName(String name){
        Random random = new Random();
        int length = random.nextInt(20) + 1;
        StringBuilder builder = new StringBuilder();
        for(int i=0;i<length;i++){
            builder.append(name);
        }
        return builder.toString();
    }
}
