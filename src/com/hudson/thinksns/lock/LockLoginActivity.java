package com.hudson.thinksns.lock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.hudson.thinksns.LoginActivity;
import com.hudson.thinksns.MainActivity;
import com.hudson.thinksns.R;
import com.hudson.thinksns.lock.LocusPassWordView.OnCompleteListener;

/**
 * 
 * ������������ü��ж�
 * 
 * @author ʯ�ٲ�
 * 
 */
public class LockLoginActivity extends Activity {
	private LocusPassWordView lpwv;
	private Toast toast;

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
		setContentView(R.layout.login_lock);
		lpwv = (LocusPassWordView) this.findViewById(R.id.mLocusPassWordView);
		lpwv.setOnCompleteListener(new OnCompleteListener() {
			@Override
			public void onComplete(String mPassword) {
				// ���������ȷ,�������ҳ�档
				if (lpwv.verifyPassword(mPassword)) {
					showToast("��½�ɹ���");
					// �е�¼���˻��Ľ�����ҳ
					Intent intent = new Intent(LockLoginActivity.this,
							LoginActivity.class);
					// ���µ�Activity
					startActivity(intent);
					finish();
				} else {
					showToast("�����������,����������");
					lpwv.clearPassword();
				}
			}
		});

	}

	@Override
	protected void onStart() {
		super.onStart();
		// �������Ϊ��,�������������Ľ���
		View noSetPassword = (View) this.findViewById(R.id.tvNoSetPassword);
		TextView toastTv = (TextView) findViewById(R.id.login_toast);
		if (lpwv.isPasswordEmpty()) {
			lpwv.setVisibility(View.GONE);
			noSetPassword.setVisibility(View.VISIBLE);
			toastTv.setText("���Ȼ�����������");
			noSetPassword.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(LockLoginActivity.this,
							SetPasswordActivity.class);
					// ���µ�Activity
					startActivity(intent);
					finish();
				}

			});
		} else {
			toastTv.setText("��������������");
			lpwv.setVisibility(View.VISIBLE);
			noSetPassword.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

}
