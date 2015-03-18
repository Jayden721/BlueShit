package com.blueshit.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blueshit.R;
import com.blueshit.application.Application;
import com.blueshit.utils.StringUtil;
import com.easemob.EMCallBack;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;


public class MainActivity extends BaseFragmentActivity implements View.OnClickListener {

    public static boolean isForeground = false;
    private LinearLayout layoutFirst;
    private LinearLayout layoutMeet;
    private LinearLayout layoutMessage;
    private LinearLayout layoutMine;
    private TextView tvFirst, tvMeet, tvMessage, tvMine;
    private ViewPager viewPager;
    private FragmentPagerAdapter pagerAdapter;
    private List<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        bindView();
        init();
        registerMessageReceiver();
    }

    private void initView() {
        layoutFirst = (LinearLayout) findViewById(R.id.layout_first);
        layoutMeet = (LinearLayout) findViewById(R.id.layout_meet);
        layoutMessage = (LinearLayout) findViewById(R.id.layout_message);
        layoutMine = (LinearLayout) findViewById(R.id.layout_mine);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tvFirst = (TextView) findViewById(R.id.tv_first);
        tvMeet = (TextView) findViewById(R.id.tv_meet);
        tvMessage = (TextView) findViewById(R.id.tv_message);
        tvMine = (TextView) findViewById(R.id.tv_mine);
    }

    private void bindView() {
        layoutFirst.setOnClickListener(this);
        layoutMeet.setOnClickListener(this);
        layoutMessage.setOnClickListener(this);
        layoutMine.setOnClickListener(this);
    }

    private void init() {
        mFragments = new ArrayList<Fragment>();
        FirstFragment firstFragment = new FirstFragment();
        FirstFragment meetFragment = new FirstFragment();
        FirstFragment messageFragment = new FirstFragment();
        FirstFragment mineFragment = new FirstFragment();
        mFragments.add(0, firstFragment);
        mFragments.add(1, meetFragment);
        mFragments.add(2, messageFragment);
        mFragments.add(3, mineFragment);
        pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        tvFirst.setTextColor(getResources().getColor(R.color.red));
                        tvMeet.setTextColor(getResources().getColor(R.color.grey));
                        tvMessage.setTextColor(getResources().getColor(R.color.grey));
                        tvMine.setTextColor(getResources().getColor(R.color.grey));
                        break;
                    case 1:
                        tvFirst.setTextColor(getResources().getColor(R.color.grey));
                        tvMeet.setTextColor(getResources().getColor(R.color.red));
                        tvMessage.setTextColor(getResources().getColor(R.color.grey));
                        tvMine.setTextColor(getResources().getColor(R.color.grey));
                        break;
                    case 2:
                        tvFirst.setTextColor(getResources().getColor(R.color.grey));
                        tvMeet.setTextColor(getResources().getColor(R.color.grey));
                        tvMessage.setTextColor(getResources().getColor(R.color.red));
                        tvMine.setTextColor(getResources().getColor(R.color.grey));
                        break;
                    case 3:
                        tvFirst.setTextColor(getResources().getColor(R.color.grey));
                        tvMeet.setTextColor(getResources().getColor(R.color.grey));
                        tvMessage.setTextColor(getResources().getColor(R.color.grey));
                        tvMine.setTextColor(getResources().getColor(R.color.red));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_first:

                login();

//                Intent intent = new Intent(this,ChatActivity.class);
//                this.startActivity(intent);
//                viewPager.setCurrentItem(0);
                break;
            case R.id.layout_meet:
                register();
//                viewPager.setCurrentItem(1);
                break;
            case R.id.layout_message:
                viewPager.setCurrentItem(2);
                break;
            case R.id.layout_mine:
                viewPager.setCurrentItem(3);
                break;
        }
    }

    @Override
    public void onResume() {
        isForeground = true;
        JPushInterface.onResume(this);
        super.onResume();
    }


    @Override
    public void onPause() {
        isForeground = false;
        JPushInterface.onPause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.blueshit.jpush.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!StringUtil.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
                showToastMsg(showMsg.toString());
//                setCostomMsg(showMsg.toString());
            }
        }
    }

    private String username = "blueshit";
    private String password = "qqqqqq";

    private void login() {
        // 调用sdk登陆方法登陆聊天服务器

        EMChatManager.getInstance().login(username, password, new EMCallBack() {

            @Override
            public void onSuccess() {
                // 登陆成功，保存用户名密码
//                Application.getInstance().setUserName(username);
//                Application.getInstance().setPassword(password);
                writePreference("username",username);
//                writePreference("password",password);
                runOnUiThread(new Runnable() {
                    public void run() {
                    }
                });
                try {
                    // ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
                    // ** manually load all local groups and
                    // conversations in case we are auto login
//                    EMGroupManager.getInstance().loadAllGroups();
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
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra("userId", "miaotu");
                MainActivity.this.startActivity(intent);
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


    private void register() {
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
                            int errorCode = e.getErrorCode();
                            if (errorCode == EMError.NONETWORK_ERROR) {
                                Toast.makeText(getApplicationContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
                            } else if (errorCode == EMError.USER_ALREADY_EXISTS) {
                                Toast.makeText(getApplicationContext(), "用户已存在", Toast.LENGTH_SHORT).show();
                            } else if (errorCode == EMError.UNAUTHORIZED) {
                                Toast.makeText(getApplicationContext(), "不知名", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "其他错误" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }).start();
    }

}
