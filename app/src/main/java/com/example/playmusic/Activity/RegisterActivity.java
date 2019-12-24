package com.example.playmusic.Activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.playmusic.R;
import com.example.playmusic.SQLite.musicTable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


public class RegisterActivity extends Activity implements View.OnClickListener {
    private EditText username;
    private EditText password;
    private EditText passwordAgain;
    private TextView registerBtn;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_main);
        initView();
    }

    private void initView() {
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        passwordAgain = (EditText) findViewById(R.id.password_again);
        registerBtn = (TextView) findViewById(R.id.register_btn);

        registerBtn.setOnClickListener(this);

        musicTable dbHelper = new musicTable(this, "Music.db",null,7);
        db = dbHelper.getWritableDatabase();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_btn:
                submit();
                break;
        }
    }

    private void submit() {
        // validate
        String usernameString = username.getText().toString().trim();
        if (TextUtils.isEmpty(usernameString) || usernameString.length() < 6) {
            Toast.makeText(this, "用户名长度不可少于6位", Toast.LENGTH_SHORT).show();
            return;
        }

        String passwordString = password.getText().toString().trim();
        if (TextUtils.isEmpty(passwordString) || passwordString.length() < 6) {
            Toast.makeText(this, "密码长度不可少于6位", Toast.LENGTH_SHORT).show();
            return;
        }

        String again = passwordAgain.getText().toString().trim();
        if (TextUtils.isEmpty(again) || !passwordString.equals(again)) {
            Toast.makeText(this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();

        Cursor cursor = db.rawQuery("select * from user where username = ? ", new String[]{usernameString});
        if (cursor.getCount() == 1) {
            Toast.makeText(this, "账号已被注册", Toast.LENGTH_SHORT).show();
            cursor.close();
            return;
        }
        cursor.close();

        values.put("username", usernameString);
        passwordString = md5(passwordString);
        System.out.println("加密后：" + passwordString);
        values.put("password", passwordString);
        if (db.insert("user", null, values) == -1) {
            Toast.makeText(this, "账号已被注册", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
            startActivity(intent);
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

    public String base64Decrypt(String str) {
        //Base64 解密
        byte[] decoded = new byte[0];
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            decoded = Base64.getDecoder().decode(str);
        }
        return new String(decoded);
    }

    public void GoToLogin(View view){
        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(intent);
    }
}
