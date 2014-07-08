package com.hudson.thinksns.statusparse;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
/**
 * 
 * @author  Ø÷Ÿ≤≈
 * 
 */
public class WeiboTagSpan extends WeiboClickSpan {

	public WeiboTagSpan(String content,Context mContext) {
		super(content,mContext);
		// TODO Auto-generated constructor stub
	}

	@Override
	void onClick(View widget, String content) {
		// TODO Auto-generated method stub
		Log.e("content",content);
		Toast.makeText(WeiboTagSpan.this.mContext, "######", Toast.LENGTH_LONG).show();
	}

}
