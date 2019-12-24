package com.example.playmusic.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class musicTable extends SQLiteOpenHelper {

    public static final String CREATE_MUSIC = "create table Music( id integer primary key autoincrement, musicName text, state integer DEFAULT 0)";
    public static final String CREATE_USER = "create table User( id integer primary key autoincrement, username char(20), password char(40),private integer DEFAULT 0)";
    private Context mContext;

    public musicTable(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context,name,factory,version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER);
        db.execSQL("insert into User(username,password,private) values(?,?,1)",new String[]{"admin","e10adc3949ba59abbe56e057f20f883e"});
        db.execSQL(CREATE_MUSIC);
        Toast.makeText(mContext,"Create succeeded",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Music");
        db.execSQL("DROP TABLE IF EXISTS User");
        onCreate(db);
    }
}
