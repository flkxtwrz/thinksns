package com.hudson.thinksns;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.hudson.thinksns.adapter.UserListAdapter;
import com.hudson.thinksns.dbutils.DBManager;
import com.hudson.thinksns.imageloader.ImageLoader;
/**
 *@file  AccountManageActivity.java
 *@author 王代银
 *@description 账户管理的页面，在这里面进行账户的添加和删除
 */
public class AccountManageActivity extends Activity {
	private Button managebutton;
	private ListView mlistview;
	private Context mContext;
	private ImageLoader imageLoader;
	private DBManager dbm;
	private int cflag = 0;
    private ImageView back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);		
		setContentView(R.layout.right);
		mContext = this;
		dbm = new DBManager(mContext);
		imageLoader = new ImageLoader(mContext);
		back = (ImageView) findViewById(R.id.setting_inform_back);
		findViews();
		initViews();
		setListeners();
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(AccountManageActivity.this,MainActivity.class);
				startActivity(i);
				AccountManageActivity.this.finish();
			}
		});
	}

	private void setListeners() {
		// TODO Auto-generated method stub
		final TranslateAnimation mShowAction = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.75f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		mShowAction.setDuration(500);
		final TranslateAnimation mHiddenAction = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.75f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		mHiddenAction.setDuration(500);
		managebutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// rg.check(1);
				if (cflag % 2 == 0) {
					int i = mlistview.getChildCount() - 1;
					if (mlistview.findViewWithTag("on") != null) {
						mlistview.findViewWithTag("on")
								.setVisibility(View.GONE);
					}
					for (int k = 0; k < i; k++) {
						mlistview.getChildAt(k).findViewById(R.id.butlf)
							.setBackgroundResource(R.drawable.del);

						mlistview.getChildAt(k).findViewById(R.id.butlf)
								.setVisibility(View.VISIBLE);
					}

				} else {
					int i = mlistview.getChildCount() - 1;
					if (mlistview.findViewWithTag("on") != null) {
						mlistview.findViewWithTag("on").setVisibility(
								View.VISIBLE);
					}
					for (int k = 0; k < i; k++) {
						// list.getChildAt(k).findViewById(R.id.butlf).setAnimation(mHiddenAction);
						mlistview.getChildAt(k).findViewById(R.id.butlf)
								.setVisibility(View.GONE);
						mlistview.getChildAt(k).findViewById(R.id.butrt)
								.setAnimation(mHiddenAction);
						mlistview.getChildAt(k).findViewById(R.id.butrt)
								.setVisibility(View.GONE);
					}

				}
				cflag++;
			}
		});
	}

	private void initViews() {
		// TODO Auto-generated method stub
		mlistview.setAdapter(new UserListAdapter(dbm.findAllUser(), this, dbm,
				imageLoader));
	}

	private void findViews() {
		// TODO Auto-generated method stub
		managebutton = (Button) findViewById(R.id.button1);
		mlistview = (ListView) findViewById(R.id.user_list);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
