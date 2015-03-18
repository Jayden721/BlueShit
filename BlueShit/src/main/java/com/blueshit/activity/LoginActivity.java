package com.blueshit.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blueshit.R;
import com.blueshit.application.Application;
import com.blueshit.db.MyDatabaseHelper;
import com.blueshit.db.MySqlHelper;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText etUsename, etPassword;
    private Button btnLogin;
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
        btnLogin = (Button) findViewById(R.id.btn_login);
    }

    private void bindView() {
        btnLogin.setOnClickListener(this);
    }

    private void init() {
        sqlHelper = new MySqlHelper(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();
                break;
        }
    }

    private void login() {
        username = etUsename.getText().toString();
        password = etPassword.getText().toString();
//        sqlHelper.insertItem(username,password);

        // 调用sdk登陆方法登陆聊天服务器
        EMChatManager.getInstance().login(username, password, new EMCallBack() {

            @Override
            public void onSuccess() {
                // 登陆成功，保存用户名密码
                Application.getInstance().setUserName("blueshit");
                Application.getInstance().setPassword("qqqqqq");
                runOnUiThread(new Runnable() {
                    public void run() {
                    }
                });
                try {
                    // ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
                    // ** manually load all local groups and
                    // conversations in case we are auto login
                    EMGroupManager.getInstance().loadAllGroups();
                    EMChatManager.getInstance().loadAllConversations();
                    //处理好友和群组
//                            processContactsAndGroups();
                } catch (Exception e) {
                    e.printStackTrace();
                    //取好友或者群聊失败，不让进入主页面
                    runOnUiThread(new Runnable() {
                        public void run() {
//                                    pd.dismiss();
                            Application.getInstance().logout(null);
                            Toast.makeText(getApplicationContext(), R.string.login_failure_failed, Toast.LENGTH_LONG).show();
                        }
                    });
                    return;
                }
                //更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
                boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(Application.currentUserNick.trim());
                if (!updatenick) {
                    Log.e("LoginActivity", "update current user nick fail");
                }
                // 进入聊天页面
                startActivity(new Intent(LoginActivity.this, ChatActivity.class));
                finish();
            }


            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(final int code, final String message) {
            }
        });

    }

}
