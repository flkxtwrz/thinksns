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
 * @{# SplashActivity.java Create on 2013-5-2 ����9:10:01
 * 
 *     class desc: �������� (1)�ж��Ƿ����״μ���Ӧ��--��ȡ��ȡSharedPreferences�ķ���
 *     (2)�ǣ������GuideActivity���������LockLoginActivity���Ź������桿 (3)3s��ִ��(2)����
 * 
 *     <p>
 *     Copyright: Copyright(c) 2013
 *     </p>
 * @Version 1.0
 * @author ʯ�ٲ�
 * 
 * 
 */
public class SplashActivity extends Activity {
	boolean isFirstIn = false;
	private Context mContext;
	// ������־
	private static final int GO_HOME = 1000;
	private static final int GO_GUIDE = 1001;
	private static final int CHECK_UPDATE_SUCC = 1002;
	private static final int CHECK_UPDATE_FAIL = 1003;
	// �ӳ�3��
	private static final long SPLASH_DELAY_MILLIS = 1800;

	private static final String SHAREDPREFERENCES_NAME = "first_pref";

	/**
	 * Handler:��ת����ͬ����
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
				Log.e("", "��ȡ����ʧ��");
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

	//��ʼ����1.��ȡsharedpreference��ֵ��������ת��ͬ���档
	private void init() {
		// ��ȡSharedPreferences����Ҫ������
		// ʹ��SharedPreferences����¼�����ʹ�ô���
		SharedPreferences preferences = getSharedPreferences(
				SHAREDPREFERENCES_NAME, MODE_PRIVATE);

		// ȡ����Ӧ��ֵ�����û�и�ֵ��˵����δд�룬��true��ΪĬ��ֵ
		isFirstIn = preferences.getBoolean("isFirstIn", true);

		// �жϳ�����ڼ������У�����ǵ�һ����������ת���������棬������ת��������
		// / if(UpdateManager.)
		if (!isFirstIn) {
			// ʹ��Handler��postDelayed������3���ִ����ת��MainActivity
			mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
		} else {
			mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
		}

	}

	//�������ȡ���°����߳�
	private Runnable getUpdateThread = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			//��������http://192.168.21.19/TS3/AndroidUpgrade.php
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

	// ��ת����������
	private void goHome() {
		Intent intent = new Intent(SplashActivity.this, LockLoginActivity.class);
		SplashActivity.this.startActivity(intent);
		SplashActivity.this.finish();
	}

	// ��ת���򵼽���
	private void goGuide() {
		Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
		SplashActivity.this.startActivity(intent);
		SplashActivity.this.finish();
	}

	// �汾���¼���
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
			// Toast.makeText(mContext, "���ظ��°�ʧ�ܣ�", 1000).show();
			// new Handler().postDelayed(new Runnable(){
			// @Override
			// public void run() {
			// startMainActivity();
			// }
			// }, SPLASH_DISPLAY_LENGHT);
		}
	};
}
