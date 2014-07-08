package com.hudson.thinksns;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.hudson.thinksns.model.AppVersion;
import com.hudson.thinksns.netuti.MHttpClient;
import com.hudson.thinksns.updateutil.UpdateManager;
import com.hudson.thinksns.updateutil.UpdateManager.UpdateListener;
/**
 *@file  Aboutsoft.java
 *@author ���ͳ�
 *@description ����Ӧ�õ�ҳ�棬���������ť����������°�װ
 */
public class Aboutsoft extends Activity {
	
	private final int CHECK_UPDATE = 1001;//��־��ȡ������Ϣ�ɹ�
	private final int CHECK_FAIL = 1002;//��־��ȡ������Ϣʧ��
	
	private Button back, menu, check = null;
	private Context mContext;
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.setting_aboutsoft);
		mContext = this;
		back = (Button) findViewById(R.id.setting_aboutsoft_btn_backup);
		//menu = (Button) findViewById(R.id.setting_aboutsoft_btn_tomenu);
		check = (Button) findViewById(R.id.app_checkupdate);
		initHandler();
		OnClickListener ocl = new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.setting_aboutsoft_btn_backup:
					Navigate(MainActivity.class);
					break;
//				case R.id.setting_aboutsoft_btn_tomenu:
//					Navigate(MainActivity.class);
//					break;
				case R.id.app_checkupdate:
					new Thread(getUpdateThread).start();
					break;
				}
			}

		};
		back.setOnClickListener(ocl);
		//menu.setOnClickListener(ocl);
		check.setOnClickListener(ocl);

	}

	private void initHandler() {
		// TODO Auto-generated method stub
		mHandler=new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case CHECK_UPDATE:
					AppVersion appVersion = (AppVersion) msg.obj;
					UpdateManager um = new UpdateManager(mContext, appVersion);
					if (!um.checkUpdate()) {
						Toast.makeText(mContext, "�������°汾", Toast.LENGTH_LONG)
						.show();
						break;
					}
					um.setUpdateListener(updateListener);
					break;
				case CHECK_FAIL:
					Toast.makeText(mContext, "���汾ʧ��", Toast.LENGTH_LONG)
					.show();
					break;
				default:
					break;
				}
			}

		};
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.private_setting, menu);
		return true;
	}

	// ������ת
	private void Navigate(Class<?> t) {
		Intent i = new Intent();
		i.setClass(Aboutsoft.this, t);
		startActivity(i);
		Aboutsoft.this.finish();
	}

	private UpdateListener updateListener = new UpdateListener() {

		@Override
		public void onStartUpdate() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onCancelUpdate() {
			// TODO Auto-generated method stub
			// startMainActivity();
			Toast.makeText(mContext, "ȡ������", Toast.LENGTH_LONG)
			.show();
		}

		@Override
		public void onFinishUpdate() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onDownloadError() {
			Toast.makeText(mContext, "���س���", Toast.LENGTH_LONG)
			.show();
		}
	};
	/**
	 * @author Hudson
	 * @description  �����̻߳�ȡ������µİ汾��Ϣ������Ŀ���ҵĵ��Եĵ�ַΪD:\Apache24\htdocs\TS3\AndroidUpgrade.php��
	 *  ���Ҫ�ر�ע��û���ùٷ���api��������Ϊ��ȡ������Ϣ�����Բ���Ҫ�˻���֤��Ϣ����û���ùٷ���api��
	 * http://192.168.21.19/TS3/AndroidUpgrade.php---�ɸ���ʵ��������ġ�
	 */
	private Runnable getUpdateThread = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String result = MHttpClient
					.get("http://192.168.21.19/TS3/AndroidUpgrade.php");
			Log.e("re", result);
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
				
				msg.what = CHECK_UPDATE;
				msg.obj = version;
				mHandler.sendMessage(msg);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				msg.what = CHECK_FAIL;
				
				mHandler.sendMessage(msg);
				e.printStackTrace();
			}
		}
	};
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Navigate(MainActivity.class);
		}
		return super.onKeyDown(keyCode, event);
	}
}
