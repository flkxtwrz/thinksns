package com.hudson.thinksns;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
/**
 *@file  AccountSafe.java
 *@author ʯ�ٲ�
 *@description ���á����˻���ȫ�����ý���
 */
public class AccountSafe extends Activity {

	private Button back, menu = null;
	// ���������༭��ť:�༭���롢�༭�Ź��񡢱༭��
	private Button pedit, ledit, bedit = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.setting_safe_accounts);

		back = (Button) findViewById(R.id.setting_accounts_btn_backup);
		//menu = (Button) findViewById(R.id.setting_accounts_btn_tomenu);
		pedit = (Button) findViewById(R.id.safe_accounts_btn_pedit);
		ledit = (Button) findViewById(R.id.safe_accounts_btn_ledit);
		bedit = (Button) findViewById(R.id.safe_accounts_btn_bedit);

		OnClickListener ocl = new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.setting_accounts_btn_backup:
					Navigate(PrivateSafeSetting.class);
					break;
//				case R.id.setting_accounts_btn_tomenu:
//					// Navigate(MainUiActivity.class);
//					break;
				case R.id.safe_accounts_btn_pedit:
					// Navigate(EditPassword.class);
					break;
				case R.id.safe_accounts_btn_ledit:
					// ����Ź����趨�����޸�
					break;
				case R.id.safe_accounts_btn_bedit:
					// �����������߲鿴���༭����
					break;
				}
			}

		};
		back.setOnClickListener(ocl);
		//menu.setOnClickListener(ocl);
		pedit.setOnClickListener(ocl);
		ledit.setOnClickListener(ocl);
		bedit.setOnClickListener(ocl);

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
		i.setClass(AccountSafe.this, t);
		startActivity(i);
		AccountSafe.this.finish();
	}
}
