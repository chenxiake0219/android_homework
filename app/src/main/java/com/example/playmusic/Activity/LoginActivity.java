package com.example.playmusic.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.playmusic.R;
import com.example.playmusic.SQLite.musicTable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {
    private musicTable musicTable = new musicTable(this, "Music.db",null,7);
    private EditText login_account;
    private EditText login_password;
    private ImageView imageView;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CheckBox rememberPass;

    private IntentFilter intentFilter;
    private NetworkChangeRecevier networkChangeReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

//        按钮
        Button log_in = (Button)findViewById(R.id.log_in);
//        输入框
        login_account = (EditText) findViewById(R.id.login_account);
        login_password = (EditText) findViewById(R.id.login_password);
//        图片
        imageView = (ImageView)findViewById(R.id.image_view);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        rememberPass = (CheckBox) findViewById(R.id.remember_pass);
        boolean isRemember = pref.getBoolean("remember_password",false);
        if(isRemember){
            String account = pref.getString("account","");
            String password = pref.getString("password","");
            login_account.setText(account);
            login_password.setText(password);
            rememberPass.setChecked(true);
        }

        log_in.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                switch (v.getId()){
                    case R.id.log_in:
                        String account = login_account.getText().toString();
                        String password = login_password.getText().toString();
                        String md5_password = md5(password);
                        SQLiteDatabase db = musicTable.getWritableDatabase();
                        Cursor cursor = db.rawQuery("select * from User where account = ? and password = ?",new String[]{account, md5_password});
                        if(cursor.moveToFirst()) {
                            editor = pref.edit();
                            if(rememberPass.isChecked()){
                                editor.putBoolean("remember_password",true);
                                editor.putString("account",account);
                                editor.putString("password",password);
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
                        break;
                        default:
                            break;
                }
            }
        });

    }

    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
    }

    class NetworkChangeRecevier extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent){
            Toast.makeText(context,"network changes",Toast.LENGTH_SHORT).show();
        }
    }

    public void noAccount(View view){
        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
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
