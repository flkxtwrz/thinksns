package com.hudson.thinksns.sign;

import java.util.Calendar;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hudson.thinksns.MainActivity;
import com.hudson.thinksns.R;
import com.hudson.thinksns.application.ThinkSNSApplication;
import com.hudson.thinksns.dbutils.DBManager;
import com.hudson.thinksns.model.Account;
import com.hudson.thinksns.netuti.MHttpClient;
import com.hudson.thinksns.netuti.MyNameValuePair;
/**
 * 
 * @author 王代银
 * 
 */
public class SignUp extends Activity {

	private ImageView back;
	private TextView con_signed, sum_signed, date, week;
	private Button sign;
	private Account account;
	private DBManager dbm;
	private Handler mHandler;
	private boolean ischeck;
	private String con_num, total_num;
	private Context mContext;
	private static String mYear;
	private static String mDay;
	private static String mMonth;
	private static String mWay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sign_up);
		init();
		listener();
		initHandler();
		new GetSignInfoThread().start();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Navigate(MainActivity.class);
		}
		return super.onKeyDown(keyCode, event);
	}

	// initial views
	private void init() {
		mContext = this;
		dbm = new DBManager(this);
		account = dbm.getAccountonline();
		back = (ImageView) findViewById(R.id.sign_up_back);
		con_signed = (TextView) findViewById(R.id.sign_up_con_num);
		sum_signed = (TextView) findViewById(R.id.sign_up_total_num);
		sign = (Button) findViewById(R.id.sign_up_btn_backup);
		date = (TextView) findViewById(R.id.sign_up_date);
		week = (TextView) findViewById(R.id.sign_up_week);
		date.setText(GetDate());
		week.setText(GetWeek());
	}

	// initial listeners
	private void listener() {
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Navigate(MainActivity.class);
			}
		});
		sign.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new DoSignThread().start();
			}
		});
	}

	// 界面跳转
	private void Navigate(Class<?> t) {
		Intent i = new Intent();
		i.setClass(SignUp.this, t);
		startActivity(i);
		SignUp.this.finish();
	}

	// 获取签到信息
	private class GetSignInfoThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			MyNameValuePair NameValuePair1 = new MyNameValuePair("app", "api");
			MyNameValuePair NameValuePair2 = new MyNameValuePair("mod",
					"Checkin");
			MyNameValuePair NameValuePair3 = new MyNameValuePair("act",
					"get_check_info");
			MyNameValuePair NameValuePair4 = new MyNameValuePair("oauth_token",
					account.getOauth_token());
			MyNameValuePair NameValuePair5 = new MyNameValuePair(
					"oauth_token_secret", account.getOauth_token_secret());

			String result = MHttpClient.get(ThinkSNSApplication.baseUrl,
					NameValuePair1, NameValuePair2, NameValuePair3,
					NameValuePair4, NameValuePair5);

			Message msg = new Message();
			msg.what = 0x01;
			msg.obj = result;
			mHandler.sendMessage(msg);
		}
	}

	// 获取签到信息
	private class DoSignThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			MyNameValuePair NameValuePair1 = new MyNameValuePair("app", "api");
			MyNameValuePair NameValuePair2 = new MyNameValuePair("mod",
					"Checkin");
			MyNameValuePair NameValuePair3 = new MyNameValuePair("act",
					"checkin");
			MyNameValuePair NameValuePair4 = new MyNameValuePair("oauth_token",
					account.getOauth_token());
			MyNameValuePair NameValuePair5 = new MyNameValuePair(
					"oauth_token_secret", account.getOauth_token_secret());

			String result = MHttpClient.get(ThinkSNSApplication.baseUrl,
					NameValuePair1, NameValuePair2, NameValuePair3,
					NameValuePair4, NameValuePair5);

			Message msg = new Message();
			msg.what = 0x02;
			msg.obj = result;
			mHandler.sendMessage(msg);
		}
	}

	// 获取当前时间
	public static String GetDate() {
		final Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
		mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
		mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
		
		return mYear + "." + mMonth + "." + mDay;
	}
	
	public static String GetWeek(){
		final Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
		if ("1".equals(mWay)) {
			mWay = "天";
		} else if ("2".equals(mWay)) {
			mWay = "一";
		} else if ("3".equals(mWay)) {
			mWay = "二";
		} else if ("4".equals(mWay)) {
			mWay = "三";
		} else if ("5".equals(mWay)) {
			mWay = "四";
		} else if ("6".equals(mWay)) {
			mWay = "五";
		} else if ("7".equals(mWay)) {
			mWay = "六";
		}
		return "星期"+mWay;
	}

	private void initHandler() {
		// mWeiboListAdapter=new WeiboListAdapter(mstatuses, new
		// ImageLoader(mContext),mContext);
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case 0x01:
					System.out.println("jiao shou :::" + msg.obj.toString());
					try {
						if (msg.obj.toString() != null) {
							JSONObject getsign_msg = new JSONObject(
									msg.obj.toString());
							ischeck = getsign_msg.optBoolean("ischeck");
							con_num = getsign_msg.optString("con_num");
							total_num = getsign_msg.optString("total_num");
							con_signed.setText(con_num);
							sum_signed.setText(total_num);
							if (ischeck == true) {
								// sign.set();
								sign.setClickable(false);
								sign.setBackgroundColor(Color
										.parseColor("#cdcdcd"));
								sign.setText("已签到！");
								sign.setTextColor(Color.parseColor("#eeeeee"));
							} else {
								sign.setClickable(true);
								sign.setBackgroundColor(Color
										.parseColor("#00ffff"));
								sign.setTextColor(Color.parseColor("#000000"));
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case 0x02:
					System.out.println("jiao shou2 :::" + msg.obj.toString());
					try {
						if (msg.obj.toString() != null) {
							JSONObject getsign_msg = new JSONObject(
									msg.obj.toString());
							ischeck = getsign_msg.optBoolean("ischeck");
							con_num = getsign_msg.optString("con_num");
							total_num = getsign_msg.optString("total_num");
							if (ischeck == true) {
								// sign.set();
								Toast.makeText(mContext, "签到成功!",
										Toast.LENGTH_SHORT).show();
								sign.setClickable(false);
								sign.setBackgroundColor(Color
										.parseColor("#cdcdcd"));
								sign.setText("已签到！");
								sign.setTextColor(Color.parseColor("#eeeeee"));
								con_signed.setText(con_num);
								sum_signed.setText(total_num);
							} else {
								Toast.makeText(mContext, "签到失败!",
										Toast.LENGTH_SHORT).show();
								sign.setClickable(true);
								sign.setBackgroundColor(Color
										.parseColor("#00ffff"));
								sign.setTextColor(Color.parseColor("#000000"));
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		};
	}
}
