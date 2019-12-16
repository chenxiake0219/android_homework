package com.example.playmusic.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.playmusic.Activity.FourthActivity;
import com.example.playmusic.R;

public class TitleLayout extends LinearLayout {
    public TitleLayout(Context context, AttributeSet attrs){
        super(context,attrs);
        LayoutInflater.from(context).inflate(R.layout.title,this);
        Button titleback = (Button) findViewById(R.id.title_back);
        titleback.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                ((Activity)getContext()).finish();

            }
        });

    }
}
