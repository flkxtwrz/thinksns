package com.hudson.thinksns;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.hudson.thinksns.application.ThinkSNSApplication;
import com.hudson.thinksns.chating.ChatFriends;
import com.hudson.thinksns.netuti.MHttpClient;
import com.hudson.thinksns.netuti.MyNameValuePair;
/**
 * @file RegisterActivity.java
 * @author ������
 * @description ע���˺ŵĽ���
 */
public class RegisterActivity extends Activity {
private EditText ed_nickname,ed_psw,ed_email,ed_psw_two;
private RadioGroup rg_sex;
private Button reg_btn,reg_cancel;
String res = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_registration);
		findViews();
		setListeners();
	}

	String tryregister()
	{
		registerThread register = new registerThread();
		register.start();
		try 
		{
			register.join();
		}
		catch(InterruptedException e) 
		{
			e.printStackTrace();
		}
		return res;
	}
	private void setListeners() 
	{
		// TODO Auto-generated method stub
		reg_btn.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				Pattern p = Pattern.compile("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\\.([a-zA-Z0-9_-])+)+$");
				Matcher m = p.matcher(ed_email.getEditableText().toString());
				boolean b = m.matches();
				if(!b||ed_nickname.getEditableText().toString().equals("")||ed_email.getEditableText().toString().equals("")||ed_psw.getText().toString().equals("")||ed_psw_two.getText().toString().equals(""))
				{
					Toast.makeText(RegisterActivity.this, "�����ʽ����ȷ���п�ֵ",Toast.LENGTH_SHORT).show();
				}
				else if(ed_psw.getText().toString().length() < 6)
				{
					Toast.makeText(RegisterActivity.this, "���볤�ȱ������6λ",Toast.LENGTH_SHORT).show();
				}
				else
				{
					if(ed_psw.getText().toString().equals(ed_psw_two.getText().toString()))
					{
						if(tryregister().equals("1"))
						{
							Toast.makeText(RegisterActivity.this, "ע��ɹ����뷵�ص�½",Toast.LENGTH_SHORT).show();
							finish();
						}
						else
						{
							Toast.makeText(RegisterActivity.this, "�ǳƻ������Ѿ���ʹ��",Toast.LENGTH_SHORT).show();
						}
					}
					else
					{
						Toast.makeText(RegisterActivity.this, "�����������벻һ��",Toast.LENGTH_SHORT).show();
					}
				}
				
			}
		});
		reg_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
				//startActivity(i);
				finish();
			}
		});
	}

	private void findViews() {
		// TODO Auto-generated method stub
		ed_email=(EditText) findViewById(R.id.registration_username_edit);
		ed_psw=(EditText) findViewById(R.id.registration_password_edit);
		ed_psw_two =(EditText) findViewById(R.id.registration_password_edit2);
		ed_nickname=(EditText) findViewById(R.id.registration_name_edit);
		rg_sex=(RadioGroup) findViewById(R.id.registration_sex_check);
		reg_btn=(Button) findViewById(R.id.registration_button);
		reg_cancel=(Button)findViewById(R.id.registration_button_cancel);
	}
private class registerThread extends Thread{
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		MyNameValuePair NameValuePair1 = new MyNameValuePair("app", "api");
		MyNameValuePair NameValuePair2 = new MyNameValuePair("mod", "Oauth");
		MyNameValuePair NameValuePair3 = new MyNameValuePair("act",
				"register");
		  
		MyNameValuePair NameValuePair4 = new MyNameValuePair("uname", ed_nickname.getEditableText().toString());
		MyNameValuePair NameValuePair5 = new MyNameValuePair("password",
				ed_psw.getEditableText().toString());//˵��api�ܿӵ�������ӦΪpassword����passwd
		MyNameValuePair NameValuePair6 = new MyNameValuePair("email",
				ed_email.getEditableText().toString());
		String sex=rg_sex.getCheckedRadioButtonId()==R.id.registration_male?"��":"Ů";
		MyNameValuePair NameValuePair7 = new MyNameValuePair("sex",
				sex);
		String result = MHttpClient.get(ThinkSNSApplication.baseUrl,
				NameValuePair1, NameValuePair2, NameValuePair3,
				NameValuePair4, NameValuePair5,NameValuePair6,NameValuePair7);
		/*
		 * ���ݽ���ж�
		 */
		System.out.println("result" + result);
		
		String pan_title = "\"status\":(.*?),\"msg\":";
		Pattern pp_title =Pattern.compile(pan_title);
		Matcher mm_title = pp_title.matcher(result);
		//ArrayList<String> title = new ArrayList<String>();
		while(mm_title.find())
		{
			res = mm_title.group(1);
		}
		System.out.println("res" + res);
	}

}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
