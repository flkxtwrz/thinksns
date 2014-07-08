package com.hudson.thinksns.lock;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hudson.thinksns.LoginActivity;
import com.hudson.thinksns.R;
import com.hudson.thinksns.lock.LocusPassWordView.OnCompleteListener;
import com.hudson.thinksns.lock.util.StringUtil;
/**
 * 
 * @author 石仲才
 * 
 */
public class SetPasswordActivity extends Activity {
	private LocusPassWordView lpwv;
	private TextView time;
	private String password;
	private boolean needverify = true;
	private Toast toast;
	private int count = 0;
	private String prepassword;// 上一次输入密码
	private int errorcount = 0;// 记录出错次数
	private Timer timer;
	private TimerTask task = null;
	private int timecount;// 每次连续出错5次时间间隔加30秒
	private final int timegap = 30;
	private Handler mHandler;
	private int try_count = 0;// 记录连续出5次错的次数

	private void showToast(CharSequence message) {
		if (null == toast) {
			toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
			// toast.setGravity(Gravity.CENTER, 0, 0);
		} else {
			toast.setText(message);
		}

		toast.show();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setpassword);
		time = (TextView) findViewById(R.id.time);
		lpwv = (LocusPassWordView) this.findViewById(R.id.mLocusPassWordView);
		lpwv.setOnCompleteListener(new OnCompleteListener() {
			@Override
			public void onComplete(String mPassword) {
				password = mPassword;
				if (needverify) {
					if (lpwv.verifyPassword(mPassword)) {
						showToast("密码输入正确,请输入新密码!");
						lpwv.clearPassword();
						needverify = false;
					} else {
						errorcount++;
						if (errorcount > 4) {
							time.setVisibility(View.VISIBLE);
							try_count++;
							timecount = timegap;
							timecount *= try_count;
							showToast("连续输入5次错误密码");
							lpwv.clearPassword();
							lpwv.disableTouch();
							lpwv.postInvalidate();
							timer = new Timer();
							task = new TimerTask() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									Message msg = mHandler.obtainMessage();
									msg.what = 1;
									mHandler.sendMessage(msg);

								}
							};
							timer.schedule(task, 0, 1000);
						} else {
							Log.e("errorcount", errorcount + " ");
							showToast("错误的密码,请重新输入!");
							lpwv.clearPassword();
							password = "";
						}
					}
				} else {
					Log.e("password", password);
					if (StringUtil.isNotEmpty(password)) {
						if (count == 0) {
							prepassword = password;// 记住上次输入的密码
							count++;
							showToast("请再次确认密码.");
							lpwv.clearPassword();

						} else {
							if (password.equals(prepassword)) {
								lpwv.resetPassWord(password);
								lpwv.clearPassword();
								showToast("密码设置成功,请记住密码.");
								startActivity(new Intent(
										SetPasswordActivity.this,
										LoginActivity.class));
								finish();
							} else {
								count = 0;
								lpwv.clearPassword();
								showToast("两次输入密码不一致，请重新开始输入新密码");
							}
						}

					} else {
						lpwv.clearPassword();
						showToast("密码至少5点,请重新输入!");
					}
				}
			}
		});
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub

				switch (msg.what) {
				case 1:
					if (timecount > 0) {
						String str=timecount + "秒后可再次输入密码";
						SpannableString span=new SpannableString(str);
						span.setSpan(new ForegroundColorSpan(Color.RED),  0, str.length()-9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						time.setText(span);
						timecount--;
						time.postInvalidate();
						Log.e("count", timecount + "miao");
					} else {
						errorcount = 0;
						time.setVisibility(View.GONE);
						timer.cancel();
						lpwv.enableTouch();
						lpwv.postInvalidate();
					}

					break;

				default:
					break;

				}
			}

		};

/*		OnClickListener mOnClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.tvSave:
					Log.e("password", password);
					if (StringUtil.isNotEmpty(password)) {
						lpwv.resetPassWord(password);
						lpwv.clearPassword();
						showToast("密码修改成功,请记住密码.");
						startActivity(new Intent(SetPasswordActivity.this,
								LoginActivity.class));
						finish();
					} else {
						lpwv.clearPassword();
						showToast("密码至少5点,请重新输入!");
					}
					break;
				case R.id.tvReset:
					lpwv.clearPassword();
					break;
				}
			}
		};*/
		/*Button buttonSave = (Button) this.findViewById(R.id.tvSave);
		buttonSave.setOnClickListener(mOnClickListener);
		Button tvReset = (Button) this.findViewById(R.id.tvReset);
		tvReset.setOnClickListener(mOnClickListener);*/
		// 如果密码为空,直接输入密码
		if (lpwv.isPasswordEmpty()) {
			this.needverify = false;
			showToast("请输入密码");
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

}
