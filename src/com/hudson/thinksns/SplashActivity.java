package com.hudson.thinksns;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import com.hudson.thinksns.lock.LockLoginActivity;
import com.hudson.thinksns.model.AppVersion;
import com.hudson.thinksns.netuti.MHttpClient;
import com.hudson.thinksns.updateutil.UpdateManager;
import com.hudson.thinksns.updateutil.UpdateManager.UpdateListener;

/**
 * 
 * @{# SplashActivity.java Create on 2013-5-2 下午9:10:01
 * 
 *     class desc: 启动画面 (1)判断是否是首次加载应用--采取读取SharedPreferences的方法
 *     (2)是，则进入GuideActivity；否，则进入LockLoginActivity【九宫格格界面】 (3)3s后执行(2)操作
 * 
 *     <p>
 *     Copyright: Copyright(c) 2013
 *     </p>
 * @Version 1.0
 * @author 石仲才
 * 
 * 
 */
public class SplashActivity extends Activity {
	boolean isFirstIn = false;
	private Context mContext;
	// 常量标志
	private static final int GO_HOME = 1000;
	private static final int GO_GUIDE = 1001;
	private static final int CHECK_UPDATE_SUCC = 1002;
	private static final int CHECK_UPDATE_FAIL = 1003;
	// 延迟3秒
	private static final long SPLASH_DELAY_MILLIS = 1800;

	private static final String SHAREDPREFERENCES_NAME = "first_pref";

	/**
	 * Handler:跳转到不同界面
	 */
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GO_HOME:
				goHome();
				break;
			case GO_GUIDE:
				goGuide();
				break;
			case CHECK_UPDATE_SUCC:
				AppVersion appVersion = (AppVersion) msg.obj;
				UpdateManager um = new UpdateManager(mContext, appVersion);
				if (!um.checkUpdate()) {
					init();
					break;
				}
				um.setUpdateListener(updateListener);
				break;
			case CHECK_UPDATE_FAIL:
				Log.e("", "获取更新失败");
				init();
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* set it to be no title */
		requestWindowFeature(Window.FEATURE_NO_TITLE);		
		setContentView(R.layout.splash);
		mContext = this;
		new Thread(getUpdateThread).start();
		// init();
	}

	//初始化，1.读取sharedpreference的值，用来跳转不同界面。
	private void init() {
		// 读取SharedPreferences中需要的数据
		// 使用SharedPreferences来记录程序的使用次数
		SharedPreferences preferences = getSharedPreferences(
				SHAREDPREFERENCES_NAME, MODE_PRIVATE);

		// 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
		isFirstIn = preferences.getBoolean("isFirstIn", true);

		// 判断程序与第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
		// / if(UpdateManager.)
		if (!isFirstIn) {
			// 使用Handler的postDelayed方法，3秒后执行跳转到MainActivity
			mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
		} else {
			mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
		}

	}

	//从网络获取更新包的线程
	private Runnable getUpdateThread = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			//内网测试http://192.168.21.19/TS3/AndroidUpgrade.php
			String result = MHttpClient
					.get("http://192.168.21.19/TS3/AndroidUpgrade.php");
			Message msg = mHandler.obtainMessage();
			try {
				JSONObject json_result = new JSONObject(result);
				int version_code = json_result.optInt("version_code");
				String version_name = json_result.optString("version_name");
				String tips = json_result.optString("upgrade_tips");
				String download_url = json_result.optString("download_url");
				int must_upgrade = json_result.optInt("must_upgrade");
				AppVersion version = new AppVersion();
				version.setAppVersionCode(version_code);
				version.setAppVersionName(version_name);
				version.setAppVersion_DownloadUrl(download_url);
				version.setAppVersion_UpgradeTips(tips);
				version.setAppVersion_UpgradeMust(must_upgrade);

				msg.what = CHECK_UPDATE_SUCC;
				msg.obj = version;
				mHandler.sendMessage(msg);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				msg.what = CHECK_UPDATE_FAIL;

				mHandler.sendMessage(msg);
				e.printStackTrace();
			}
		}
	};

	// 跳转到解锁界面
	private void goHome() {
		Intent intent = new Intent(SplashActivity.this, LockLoginActivity.class);
		SplashActivity.this.startActivity(intent);
		SplashActivity.this.finish();
	}

	// 跳转到向导界面
	private void goGuide() {
		Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
		SplashActivity.this.startActivity(intent);
		SplashActivity.this.finish();
	}

	// 版本更新监听
	private UpdateListener updateListener = new UpdateListener() {

		@Override
		public void onStartUpdate() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onCancelUpdate() {
			// TODO Auto-generated method stub
			// startMainActivity();
			init();
		}

		@Override
		public void onFinishUpdate() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onDownloadError() {
			init();
			// TODO Auto-generated method stub
			// Toast.makeText(mContext, "下载更新包失败！", 1000).show();
			// new Handler().postDelayed(new Runnable(){
			// @Override
			// public void run() {
			// startMainActivity();
			// }
			// }, SPLASH_DISPLAY_LENGHT);
		}
	};
}
