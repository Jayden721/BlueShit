package com.blueshit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blueshit.R;
import com.blueshit.application.Application;
import com.blueshit.db.MySqlHelper;
import com.easemob.EMCallBack;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText etUsename, etPassword;
    private Button btnRegister;
    private String username, password;
    private MySqlHelper sqlHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        bindView();
        init();
    }

    private void initView() {
        etUsename = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnRegister = (Button) findViewById(R.id.btn_login);
    }

    private void bindView() {
        btnRegister.setOnClickListener(this);
    }

    private void init() {
        sqlHelper = new MySqlHelper(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                register();
                break;
        }
    }

    private void register() {
        username = etUsename.getText().toString();
        password = etPassword.getText().toString();
        sqlHelper.insertItem(username,password);

        // 注册IM
        new Thread(new Runnable() {
            public void run() {
                try {
                    // 调用sdk注册方法
                    EMChatManager.getInstance().createAccountOnServer(username, password);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            // 保存用户名
                            Application.getInstance().setUserName(username);
                            finish();
                        }
                    });
                } catch (final EaseMobException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            int errorCode=e.getErrorCode();
                            if(errorCode== EMError.NONETWORK_ERROR){
                                Toast.makeText(getApplicationContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
                            }else if(errorCode==EMError.USER_ALREADY_EXISTS){
                                Toast.makeText(getApplicationContext(), "用户已存在", Toast.LENGTH_SHORT).show();
                            }else if(errorCode==EMError.UNAUTHORIZED){
                                Toast.makeText(getApplicationContext(), "不知名", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(), "其他错误" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }).start();

    }

}
