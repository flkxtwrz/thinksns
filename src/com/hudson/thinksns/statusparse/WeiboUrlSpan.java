package com.hudson.thinksns.statusparse;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
/**
 * 
 * @author ʯ�ٲ�
 * 
 */
public class WeiboUrlSpan extends WeiboClickSpan {

	public WeiboUrlSpan(String content,Context mContext) {
		super(content,mContext);
		// TODO Auto-generated constructor stub
	}
	public void updateDrawState(TextPaint ds) {
	    ds.setColor(ds.linkColor);
	    ds.setUnderlineText(false); //去掉下划�?
	}
	@Override
	void onClick(View widget, String content) {
		// TODO Auto-generated method stub
		 // processHyperLinkClick(text);
		widget.setBackgroundColor(Color.RED);
		Log.e("content",content);
		Toast.makeText(WeiboUrlSpan.this.mContext, "URL", Toast.LENGTH_LONG).show();
	}

}
