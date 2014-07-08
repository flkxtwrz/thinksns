package com.hudson.thinksns.statusparse;

import android.content.Context;
import android.text.style.ClickableSpan;
import android.view.View;
/**
 * 
 * @author ʯ�ٲ�
 * 
 */
public abstract class WeiboClickSpan extends ClickableSpan {
	// 点击的内�?
	String mContent;
	Context mContext;
	public WeiboClickSpan(String content,Context context){
		mContent = content;
		mContext=context;
		}
	@Override
	public void onClick(View widget) {
		// TODO Auto-generated method stub
		onClick(widget, mContent);
	}
	abstract void onClick(View widget, String content);

}
