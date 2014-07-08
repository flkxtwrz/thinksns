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
public class WeiboAtSpan extends WeiboClickSpan {

	public WeiboAtSpan(String content,Context mContext) {
		super(content,mContext);
		// TODO Auto-generated constructor stub
	}

	@Override
	void onClick(View widget, String content) {
		// TODO Auto-generated method stub
	   Log.e("content",content);
       Toast.makeText(WeiboAtSpan.this.mContext, "@@@@", Toast.LENGTH_LONG).show();
	}

}
