package com.example.playmusic.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.playmusic.Adapter.Music;
import com.example.playmusic.Adapter.MusicAdapter;
import com.example.playmusic.R;
import com.example.playmusic.SQLite.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class MusicListActivity extends AppCompatActivity {
    TextView title_text;
    ListView listView;
    private MyDatabaseHelper dbHelper = new MyDatabaseHelper(this, "Music.db",null,5);

    private List<Music> musicList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);

        title_text = findViewById(R.id.title_text);
        title_text.setText("不喜欢就拉倒");

        initMusic();

        MusicAdapter adapter = new MusicAdapter(MusicListActivity.this,R.layout.all_music_item,musicList);
        listView = findViewById(R.id.music_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                改变喜欢不喜欢的图片
                Music music = musicList.get(i);
                if(music.getImageId()==R.drawable.like){
                    Log.d("test", "1"+music.getImageId());
                    music.setImageId(R.drawable.dislike);
                }else if(music.getImageId()==R.drawable.dislike){
                    Log.d("test", "2"+music.getImageId());
                    music.setImageId(R.drawable.like);
                }
                //更新喜欢不喜欢的图片
                ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();

            }
        });
    }

    public void initMusic(){
        if(musicList.size() == 0){
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Music currentMusic;
            int currentMusicFile;
            Cursor cursor = db.rawQuery("select * from Music",null);
            if(cursor.moveToFirst()){
                do{
//                    音乐名
                    String musicName = cursor.getString(cursor.getColumnIndex("musicName"));
//                    歌曲地址
                    currentMusicFile = getResources().getIdentifier(musicName, "raw",  getPackageName());
//                    如果state是1，图片就是喜欢，否则就是不喜欢
                    if(cursor.getInt(cursor.getColumnIndex("state")) == 1){
                        currentMusic = new Music(musicName,currentMusicFile,R.drawable.like);
                    }else{
                        currentMusic = new Music(musicName,currentMusicFile,R.drawable.dislike);
                    }

                    musicList.add(currentMusic);

                }while(cursor.moveToNext());
            }
            cursor.close();
        }
    }

    public void gotoMusicPlay(View v){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        更新数据库中各个歌曲的state状态
        for(int i=0;i<musicList.size();i++){
            String musicName = musicList.get(i).getName();
            int like;
            if(musicList.get(i).getImageId() == R.drawable.like){
                like = 1;
            }else{
                like = 0;
            }
            db.execSQL("update Music set state = "+ like +" where musicName = ?",new String[]{musicName});
        }

//        跳转页面
        Intent intent = new Intent(MusicListActivity.this, MusicActivity.class);
        startActivity(intent);
    }
}
