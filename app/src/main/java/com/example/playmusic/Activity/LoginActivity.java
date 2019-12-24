package com.example.playmusic.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.playmusic.R;
import com.example.playmusic.SQLite.musicTable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


public class LoginActivity extends Activity {
    private EditText username;
    private EditText password;
    private ImageView btnLogin;
    private SQLiteDatabase sql;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CheckBox rememberPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_mian);
        initView();

        initEvents();

    }

    private void initEvents() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    private void initView() {
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        btnLogin = (ImageView) findViewById(R.id.btn_login);
        rememberPass = (CheckBox) findViewById(R.id.remember_pass);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember = pref.getBoolean("remember_password",false);
        if(isRemember){
            String usernamestr = pref.getString("username","");
            String passwordstr = pref.getString("password","");
            username.setText(usernamestr);
            password.setText(passwordstr);
            rememberPass.setChecked(true);
        }
        musicTable helper = new musicTable(this, "Music.db",null,7);
        sql = helper.getWritableDatabase();
    }

    private void submit() {
        // validate
        String usernameString = username.getText().toString().trim();
        if (TextUtils.isEmpty(usernameString)) {
            Toast.makeText(this, "用户名未输入完整", Toast.LENGTH_SHORT).show();
            return;
        }

        String passwordString = password.getText().toString().trim();
        if (TextUtils.isEmpty(passwordString)) {
            Toast.makeText(this, "密码未输入完整", Toast.LENGTH_SHORT).show();
            return;
        }

        String encryptionPasswordString = md5(passwordString);
        Cursor cursor = sql.rawQuery("select * from User where username = ? and password = ?", new String[]{usernameString, encryptionPasswordString});
        if(cursor.moveToFirst()) {
            editor = pref.edit();
            if(rememberPass.isChecked()){
                editor.putBoolean("remember_password",true);
                editor.putString("username",usernameString);
                editor.putString("password",passwordString);
            }else{
                editor.clear();
            }
            editor.apply();
            int userPrivate = cursor.getInt(cursor.getColumnIndex("private"));
            if(userPrivate == 1){
                Intent intent = new Intent(LoginActivity.this,SQLiteActivity.class);
                startActivity(intent);
            }else if(userPrivate == 0){
                Intent intent = new Intent(LoginActivity.this,MusicListActivity.class);
                startActivity(intent);
            }
        }else{
//                            对话框AlertDialog
            AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
            dialog.setTitle("Warning");
            dialog.setMessage("账号或者密码错误！");
            dialog.setCancelable(false);
            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            dialog.show();
        }
        cursor.close();

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

    public void goToRegiste(View view){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }
}
