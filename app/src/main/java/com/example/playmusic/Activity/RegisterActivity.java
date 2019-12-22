package com.example.playmusic.Activity;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.playmusic.R;
import com.example.playmusic.SQLite.musicTable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegisterActivity extends AppCompatActivity {
    private musicTable musicTable = new musicTable(this, "Music.db",null,7);
    private EditText register_account;
    private EditText register_password;
    private EditText register_repeat_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        musicTable.getWritableDatabase();

        register_account = findViewById(R.id.register_account);
        register_password = findViewById(R.id.register_password);
        register_repeat_password = findViewById(R.id.register_repeat_password);
    }

    public void haveAccount(View view){
        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(intent);
    }

    public void registration(View view){
        String account = register_account.getText().toString();
        String password = register_password.getText().toString();
        String repeatPassword = register_repeat_password.getText().toString();

//        判断用户名密码是否可用
        if(account.length() == 0){
            Toast.makeText(RegisterActivity.this, "请输入账号！",Toast.LENGTH_SHORT).show();
        }else if(password.length() == 0){
            Toast.makeText(RegisterActivity.this, "请输入密码！",Toast.LENGTH_SHORT).show();
        }else if(repeatPassword.length() == 0){
            Toast.makeText(RegisterActivity.this, "请再次输入密码！",Toast.LENGTH_SHORT).show();
        }else if(!password.equals(repeatPassword)){
            Toast.makeText(RegisterActivity.this, "两次输入的密码不一致，请检查后重新输入！",Toast.LENGTH_SHORT).show();
            register_password.setText("");
            register_repeat_password.setText("");
        }else{
            password = md5(password);
            SQLiteDatabase db = musicTable.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from User where account = ?",new String[]{account});
            if(cursor.moveToFirst()){
                Toast.makeText(RegisterActivity.this,account + "用户已经存在，添加失败！",Toast.LENGTH_SHORT).show();
//            清空文本框
                register_account.setText("");
                register_password.setText("");
                register_repeat_password.setText("");
            }else{
                db.execSQL("insert into User(account,password) values(?,?)",new String[]{account,password});
                Toast.makeText(RegisterActivity.this,account + "用户添加成功！",Toast.LENGTH_SHORT).show();
//            注册成功以后跳转登录页面
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        }
    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
