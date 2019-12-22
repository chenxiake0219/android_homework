package com.example.playmusic.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.playmusic.R;

import java.util.List;

public class MusicAllAdapter extends ArrayAdapter<MusicAll> {

    private int resourceId;

    public MusicAllAdapter(Context context, int textViewResourceId, List<MusicAll>objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        MusicAll musicAll = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView musicname = (TextView) view.findViewById(R.id.music_name);
        ImageView play = view.findViewById(R.id.play);
        play.setImageResource(musicAll.getImageId());
        musicname.setText(musicAll.getName());
        return view;
    }

}
