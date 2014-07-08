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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hudson.thinksns.application.ThinkSNSApplication;
import com.hudson.thinksns.dbutils.DBManager;
import com.hudson.thinksns.des3md5.DesBase64Tool;
import com.hudson.thinksns.model.Account;
import com.hudson.thinksns.netuti.MHttpClient;
import com.hudson.thinksns.netuti.MyNameValuePair;

/**
 * 登陆界面
 * @author 石仲才
 * @author 
 * 
 */
public class LoginActivity extends Activity {
	private EditText et_uname, et_upass;
	private Button bt_login, bt_regist;
	private Handler mHandler;
	private Context mContext;
	private final int getkey = 1000;
	private final int oauthsucc = 1001;
	private final int oauthfail = 1002;
	private String RequestKey = "";
	private DBManager dbm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		/* set it to be no title */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_ts);
		mContext = this;
		dbm = new DBManager(mContext);
		Account ac_on = dbm.getAccountonline();
		Intent i = getIntent();
		System.out.println(i.getBooleanExtra("relogin", false));
		if (i.getBooleanExtra("relogin", false) == false && ac_on.getUid() != 0
				&& getIntent().getStringExtra("add") == null) {
			Intent intent = new Intent(mContext, MainActivity.class);
			intent.putExtra("ac", ac_on);
			mContext.startActivity(intent);
			this.finish();
		}
		initHandler();
		initViews();
		setListener();
	}

	private void initHandler() {
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case getkey:
					/*
					 * 获取加密的key ["key"]
					 */

					RequestKey = msg.obj.toString().substring(2,
							msg.obj.toString().length() - 2);
					new OauthThread().start();
					break;
				case oauthfail:
					/*
					 * 登录认证失败 提示错误信息
					 */
					Toast.makeText(mContext, "密码或账户错误", Toast.LENGTH_SHORT)
							.show();

					break;
				case oauthsucc:
					/*
					 * 登录认证成功跳转到首页
					 */
					String uid = "";
					String oauth_token = "";
					String oauth_token_secret = "";
					try {
						JSONObject json_res = new JSONObject(msg.obj.toString());
						uid = json_res.optString("uid");
						oauth_token = json_res.optString("oauth_token");
						oauth_token_secret = json_res
								.optString("oauth_token_secret");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					Account ac = new Account();
					ac.setUid(Integer.parseInt(uid));
					ac.setOauth_token(oauth_token);
					ac.setOauth_token_secret(oauth_token_secret);
					Intent intent = new Intent(mContext, MainActivity.class);
					Log.e("ac", ac.toString());
					intent.putExtra("ac", ac);
					mContext.startActivity(intent);
					((Activity) mContext).finish();
					break;
				default:
					break;

				}
			}

		};
	}

	private void initViews() {
		et_uname = (EditText) findViewById(R.id.username_edit);
		et_upass = (EditText) findViewById(R.id.password_edit);
		bt_login = (Button) findViewById(R.id.signin_button);
		bt_regist = (Button) findViewById(R.id.registration_button);
	}

	private void setListener() {
		bt_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// new OauthThread().start();
				new Thread(new GetKeyThrad()).start();
			}
		});
		bt_regist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(LoginActivity.this,
						RegisterActivity.class);
				startActivity(i);
				//finish();
			}
		});
	}

	private class GetKeyThrad implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			MyNameValuePair NameValuePair1 = new MyNameValuePair("app", "api");
			MyNameValuePair NameValuePair2 = new MyNameValuePair("mod", "Oauth");
			MyNameValuePair NameValuePair3 = new MyNameValuePair("act",
					"request_key");
			String result = MHttpClient.get(ThinkSNSApplication.baseUrl,
					NameValuePair1, NameValuePair2, NameValuePair3);
			Log.e("re", result);
			Message msg = new Message();
			msg.what = getkey;
			msg.obj = result;
			mHandler.sendMessage(msg);
		}

	}

	private class OauthThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			MyNameValuePair NameValuePair1 = new MyNameValuePair("app", "api");
			MyNameValuePair NameValuePair2 = new MyNameValuePair("mod", "Oauth");
			MyNameValuePair NameValuePair3 = new MyNameValuePair("act",
					"authorize");
			Log.e("key", RequestKey);

			String uid = DesBase64Tool.desEncrypt(et_uname.getEditableText()
					.toString(), DesBase64Tool.paddingkey(RequestKey));
			Log.e("uid", uid);
			Log.e("uid",
					DesBase64Tool.desDecrypt(uid,
							DesBase64Tool.paddingkey(RequestKey)));
			String passwd = DesBase64Tool.desEncrypt(
					DesBase64Tool.md5(et_upass.getEditableText().toString()),
					DesBase64Tool.paddingkey(RequestKey));
			Log.e("passwd", passwd);
			MyNameValuePair NameValuePair4 = new MyNameValuePair("uid", uid);
			MyNameValuePair NameValuePair5 = new MyNameValuePair("passwd",
					passwd);
			String result = MHttpClient.get(ThinkSNSApplication.baseUrl,
					NameValuePair1, NameValuePair2, NameValuePair3,
					NameValuePair4, NameValuePair5);
			Log.e("re", result);
			Message msg = new Message();
			JSONObject json_result = null;
			try {
				json_result = new JSONObject(result);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (!json_result.has("uid")) {
				msg.what = oauthfail;
				msg.obj = result;
			} else {
				msg.what = oauthsucc;
				msg.obj = result;
			}

			mHandler.sendMessage(msg);
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		dbm.closeDB();
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

}
