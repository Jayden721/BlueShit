package com.blueshit.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.blueshit.R;
import com.blueshit.utils.CommonUtils;
import com.easemob.chat.EMMessage;
import com.easemob.util.EasyUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseActivity extends Activity {
    private static final int notifiId = 11;
    protected NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	public static final String NAME_COMMON = "COMMON";

	/**
	 * 显示toast提示信息
	 * 
	 * @param msg
	 *            提示信息
	 */
	public void showToastMsg(String msg) {
		showToastMsg(msg, Toast.LENGTH_SHORT);
	}

	/**
	 * 显示toast提示信息
	 * 
	 * @param msg
	 *            提示信息
	 * @param duration
	 *            显示时间长短
	 */
	protected void showToastMsg(String msg, int duration) {
		Toast.makeText(this, msg, duration).show();
	}

	/**
	 * get current time
	 * 
	 * @return yyyy-mm-dd
	 */
	public static String getCurrentTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sTime = format.format(new Date());
		System.out.println("CurrentTime:" + sTime);
		return sTime;
		// return String.format("%04d-%02d-%02d ", date.getYear() + 1900,
		// date.getMonth() + 1, date.getDate());

	}

	/**
	 * get current time
	 * 
	 * @return yyyy-mm-dd
	 */
	public static String getNameByTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		String sTime = format.format(new Date());
		System.out.println("CurrentTime:" + sTime);
		return sTime;
		// return String.format("%04d-%02d-%02d ", date.getYear() + 1900,
		// date.getMonth() + 1, date.getDate());

	}

	// *********************** 数据存储方法 ****************************
	/**
	 * SharedPreferences工具方法,用来读取一个值 如果没有读取到，会返回""
	 * 
	 * @param key
	 * @return
	 */
	public String readPreference(String key) {
		SharedPreferences sharedPreferences = getSharedPreferences(NAME_COMMON,
				MODE_PRIVATE);
		String value = sharedPreferences.getString(key, "");
		return value;
	}

	/**
	 * SharedPreferences工具方法,用来写入一个值
	 * 
	 * @param key
	 * @param value
	 */
	public void writePreference(String key, String value) {
		SharedPreferences sharedPreferences = getSharedPreferences(NAME_COMMON,
				MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * 删除 SharedPreferences
	 * 
	 * @param key
	 */
	public void deletePreference(String key) {
		SharedPreferences sharedPreferences = getSharedPreferences(NAME_COMMON,
				MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.remove(key);
		editor.commit();
	}

	/**
	 * SharedPreferences工具方法,用来读取一个值 如果没有读取到，会返回""
	 * 
	 * @param key
	 * @return
	 */
	public String readPreference(String xmlname, String key) {
		SharedPreferences sharedPreferences = getSharedPreferences(xmlname,
				MODE_PRIVATE);
		String value = sharedPreferences.getString(key, "");
		return value;
	}

	/**
	 * SharedPreferences工具方法,用来写入一个值进自定义文件名
	 * 
	 * @param key
	 * @param value
	 * @param xmlname
	 *            自定义xml的名字
	 */
	public void writePreference(String xmlname, String key, String value) {
		SharedPreferences sharedPreferences = getSharedPreferences(xmlname,
				MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * 删除信息
	 * 
	 * @param xmlname
	 *            指定的信息文件
	 * @param key
	 *            特定的key值
	 */
	public void deletePreference(String xmlname, String key) {
		SharedPreferences sharedPreferences = getSharedPreferences(xmlname,
				MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.remove(key);
		editor.commit();
	}

	/**
	 * 删除特定信息
	 * 
	 * @param xmlname
	 *            指定的信息文件
	 */
	public void deleteAllPreference(String xmlname) {
		SharedPreferences sharedPreferences = getSharedPreferences(xmlname,
				MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.clear();
		editor.commit();
	}

    /**
     * 当应用在前台时，如果当前消息不是属于当前会话，在状态栏提示一下
     * 如果不需要，注释掉即可
     * @param message
     */
    protected void notifyNewMessage(EMMessage message) {
        //如果是设置了不提醒只显示数目的群组(这个是app里保存这个数据的，demo里不做判断)
        //以及设置了setShowNotificationInbackgroup:false(设为false后，后台时sdk也发送广播)
        if(!EasyUtils.isAppRunningForeground(this)){
            return;
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(getApplicationInfo().icon)
                .setWhen(System.currentTimeMillis()).setAutoCancel(true);

        String ticker = CommonUtils.getMessageDigest(message, this);
        String st = getResources().getString(R.string.expression);
        if(message.getType() == EMMessage.Type.TXT)
            ticker = ticker.replaceAll("\\[.{2,3}\\]", st);
        //设置状态栏提示
        mBuilder.setTicker(message.getFrom()+": " + ticker);

        //必须设置pendingintent，否则在2.3的机器上会有bug
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, notifiId, intent, PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(pendingIntent);

        Notification notification = mBuilder.build();
        notificationManager.notify(notifiId, notification);
        notificationManager.cancel(notifiId);
    }
}
