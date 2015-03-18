package com.blueshit.activity;

import android.support.v4.app.Fragment;
import android.widget.Toast;

public class BaseFragment extends Fragment {
	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
													// 之前调用,因为 onPause 中会保存信息
	}

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
		Toast.makeText(this.getActivity(), msg, duration).show();
	}
}
