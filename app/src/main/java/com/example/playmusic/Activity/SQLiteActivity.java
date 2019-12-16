package com.example.playmusic.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.playmusic.Adapter.Music;
import com.example.playmusic.R;
import com.example.playmusic.SQLite.MyDatabaseHelper;

public class SQLiteActivity extends AppCompatActivity {
    private MyDatabaseHelper dbHelper = new MyDatabaseHelper(this, "Music.db",null,5);
    TextView title_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sqlite_layout);

        title_text = findViewById(R.id.title_text);
        title_text.setText("在后面搞来搞去");

        Button createDatabase = findViewById(R.id.create);
        createDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.getWritableDatabase();
            }
        });

    }

//    如果数据库中没有这首歌,添加歌曲
    public void addMusic(View v){
        int haveMusic = 0;
        EditText music_name = findViewById(R.id.music_name);
        String addMusicName = music_name.getText().toString();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from Music",null);
        if(cursor.moveToFirst()){
            do{
                String musicName = cursor.getString(cursor.getColumnIndex("musicName"));
//                Log.d("test",  musicName);
//                Log.d("test",  addMusicName);
//                Log.d("test",  "" + (musicName.equals(addMusicName)));
                if(musicName.equals(addMusicName)){
                    haveMusic = 1;
                    break;
                }
            }while(cursor.moveToNext());
        }
        cursor.close();

        if(haveMusic == 0){
            db.execSQL("insert into Music(musicName) values(?)",new String[]{addMusicName});
            Toast.makeText(SQLiteActivity.this,addMusicName+"歌曲添加成功",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(SQLiteActivity.this,"数据库中已有"+addMusicName+"歌曲,歌曲添加成失败",Toast.LENGTH_LONG).show();
        }
    }

//    输入歌曲名删除歌曲
    public void deleteMusic(View v){
        EditText music_name = findViewById(R.id.music_name);
        String deleteMusicName = music_name.getText().toString();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.d("test","1" + deleteMusicName + "1" );
        db.execSQL("delete from Music where musicName = ?",new String[]{deleteMusicName});
        Toast.makeText(SQLiteActivity.this,deleteMusicName+"歌曲删除成功",Toast.LENGTH_LONG).show();
    }

//    转向歌单列表
    public void gotoMusicList(View v){
        Intent intent = new Intent(SQLiteActivity.this, MusicListActivity.class);
        startActivity(intent);
    }

}
