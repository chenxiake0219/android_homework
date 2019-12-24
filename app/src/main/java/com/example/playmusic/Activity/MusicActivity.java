package com.example.playmusic.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.playmusic.Adapter.Music;
import com.example.playmusic.Adapter.MusicAdapter;
import com.example.playmusic.R;
import com.example.playmusic.SQLite.musicTable;

import java.util.ArrayList;
import java.util.List;

public class MusicActivity extends AppCompatActivity {
    private musicTable musicTable = new musicTable(this, "Music.db",null,7);

    private List<Music> musicList;
    TextView title_text;
    int index;
    ListView listView;
    MusicAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_layout);

        title_text = findViewById(R.id.title_text);
        title_text.setText("我喜欢的音乐");
    }

    @Override
    protected void onStart() {
        super.onStart();
        //        初始化歌单
        initMusic();

        adapter = new MusicAdapter(MusicActivity.this,R.layout.music_item,musicList);
        listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String currentMusicName = musicList.get(i).getName();
                Intent intent = new Intent(MusicActivity.this,MusicPlayActivity.class);
//                将当前选的歌曲放入intent中
                intent.putExtra("currentMusicName",currentMusicName);
//                将歌单中所有的歌曲名都放入intent中
                for(int j=0;j<musicList.size();j++){
                    String str = "music"+j;
                    String musicName = musicList.get(j).getName();
                    intent.putExtra(str,musicName);
                }
                startActivity(intent);

            }
        });
    }

    //    对歌单列表初始化
    private void initMusic(){
        SQLiteDatabase db = musicTable.getWritableDatabase();
        Music currentMusic;
        int currentMusicFile;
        musicList = new ArrayList<>();


        Cursor cursor = db.rawQuery("select * from Music",null);
        if(cursor.moveToFirst()){
            index = 1;
            do{
//                    如果state属性是1的话显示歌曲
                if(cursor.getInt(cursor.getColumnIndex("state")) == 1){
//                    音乐名
                    String musicName = cursor.getString(cursor.getColumnIndex("musicName"));
//                    歌曲地址
                    currentMusicFile = getResources().getIdentifier(musicName, "raw",  getPackageName());

                    currentMusic = new Music(index, musicName,currentMusicFile,R.drawable.play);
                    index++;
                    musicList.add(currentMusic);
                }
            }while(cursor.moveToNext());
        }
        cursor.close();
    }
}
