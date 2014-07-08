package com.hudson.thinksns;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * @file GeneralSetting.java
 * @author ʯ�ٲ�
 * @description ��ҳ�Ҳ����ò˵���ͨ�����ý��棬����һЩ����������
 */
public class GeneralSetting extends Activity {

	private ImageView back, update = null;
	private ImageView voice, info, other, vibra, picdownload = null;
	private TextView cacheclean = null;
	// �洢���õ�״̬���Կ������������� 1Ϊ�� 0Ϊ��
	private int vo_state, in_state, ot_state, vi_state = 0;
	private int picdownload_state = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.setting_generalsetting);

		back = (ImageView) findViewById(R.id.setting_voice_back);
//		voice = (ImageView) findViewById(R.id.setting_voice_voice_onoff);
//		info = (ImageView) findViewById(R.id.setting_voice_infovoice_onoff);
//		other = (ImageView) findViewById(R.id.setting_voice_others_onoff);
//		vibra = (ImageView) findViewById(R.id.setting_voice_vabiration_onoff);
		cacheclean = (TextView) findViewById(R.id.setting_sys_cacheclean);
		picdownload = (ImageView) findViewById(R.id.setting_sys_picdownload_onoff);
		update = (ImageView) findViewById(R.id.setting_sys_update);

		//Initswitch(vo_state,voice);
		//Initswitch(in_state,info);
		//Initswitch(ot_state,other);
		//Initswitch(vi_state,vibra);
		Initswitch(picdownload_state,picdownload);
		
		OnClickListener ocl = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.setting_voice_back:// ���ذ�ť
					// TODO
					Navigate(MainActivity.class);
					break;
//				case R.id.setting_voice_voice_onoff:// ��������
//					if (vo_state == 0) {
//						voice.setBackgroundResource(R.drawable.setting_on_btn);
//						// TODO ��������д���������ĳ���
//
//						vo_state = 1;
//					} else {
//						voice.setBackgroundResource(R.drawable.setting_off_btn);
//						// TODO ��������д�ر������ĳ���
//
//						vo_state = 0;
//					}
//					break;
//				case R.id.setting_voice_infovoice_onoff:// ��Ϣ֪ͨ����
//					if (in_state == 0) {
//						info.setBackgroundResource(R.drawable.setting_on_btn);
//						// TODO ��������д���������ĳ���
//
//						in_state = 1;
//					} else {
//						info.setBackgroundResource(R.drawable.setting_off_btn);
//						// TODO ��������д�ر������ĳ���
//
//						in_state = 0;
//					}
//					break;
//				case R.id.setting_voice_others_onoff:// ������������
//					if (ot_state == 0) {
//						other.setBackgroundResource(R.drawable.setting_on_btn);
//						// TODO ��������д���������ĳ���
//
//						ot_state = 1;
//					} else {
//						other.setBackgroundResource(R.drawable.setting_off_btn);
//						// TODO ��������д�ر������ĳ���
//
//						ot_state = 0;
//					}
//					break;
//				case R.id.setting_voice_vabiration_onoff:// �񶯿���
//					if (vi_state == 0) {
//						vibra.setBackgroundResource(R.drawable.setting_on_btn);
//						// TODO ��������д���������ĳ���
//
//						vi_state = 1;
//					} else {
//						vibra.setBackgroundResource(R.drawable.setting_off_btn);
//						// TODO ��������д�ر������ĳ���
//
//						vi_state = 0;
//					}
//					break;
				case R.id.setting_sys_picdownload_onoff:// 2g/3g����
					if (picdownload_state == 0) {
						picdownload
								.setBackgroundResource(R.drawable.setting_on_btn);
						// TODO ��������д����2g3g�ĳ���

						picdownload_state = 1;
					} else {
						picdownload
								.setBackgroundResource(R.drawable.setting_off_btn);
						// TODO ��������д�ر�2g3g�ĳ���

						picdownload_state = 0;
					}
					break;
				case R.id.setting_sys_cacheclean:// ������
					// TODO

					break;
				case R.id.setting_sys_update:// �������
					// TODO

					break;
				}
			}
		};
		back.setOnClickListener(ocl);
//		voice.setOnClickListener(ocl);
//		info.setOnClickListener(ocl);
//		other.setOnClickListener(ocl);
//		vibra.setOnClickListener(ocl);
		cacheclean.setOnClickListener(ocl);
		picdownload.setOnClickListener(ocl);
		update.setOnClickListener(ocl);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.voice_reminder, menu);
		return true;
	}

	//��ʼ������
	private void Initswitch(int Switch, ImageView img){
		if(Switch == 0){
			img.setBackgroundResource(R.drawable.setting_off_btn);
		}else{
			img.setBackgroundResource(R.drawable.setting_on_btn);
		}		
	}
	// ������ת
	private void Navigate(Class<?> t) {
		Intent i = new Intent();
		i.setClass(GeneralSetting.this, t);
		startActivity(i);
		GeneralSetting.this.finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Navigate(MainActivity.class);
		}
		return super.onKeyDown(keyCode, event);
	}

}
