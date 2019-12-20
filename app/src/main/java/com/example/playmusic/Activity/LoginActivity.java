package com.example.playmusic.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.playmusic.R;

public class LoginActivity extends AppCompatActivity {

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

//        intentFilter = new IntentFilter();
//        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
//        networkChangeReceiver = new NetworkChangeRecevier();
//        registerReceiver(networkChangeReceiver,intentFilter);

        log_in.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                switch (v.getId()){
                    case R.id.log_in:
                        String account = login_account.getText().toString();
                        String password = login_password.getText().toString();
                        if(account.equals("admin") && password.equals("123")){
                            editor = pref.edit();
                            if(rememberPass.isChecked()){
                                editor.putBoolean("remember_password",true);
                                editor.putString("account",account);
                                editor.putString("password",password);
                            }else{
                                editor.clear();
                            }
                            editor.apply();
                            Intent intent = new Intent(LoginActivity.this,SQLiteActivity.class);
                            startActivity(intent);
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

    public  void music(View v){
        Intent intent = new Intent(LoginActivity.this,MusicActivity.class);
        startActivity(intent);
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
}
