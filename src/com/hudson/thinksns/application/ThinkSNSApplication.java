package com.hudson.thinksns.application;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
/**
 *@file  ThinkSNSApplication.java
 *@author 王代银
 *@description 配置一些关于应用的基本的信息，如网址
 */
public class ThinkSNSApplication extends Application {
	private List<Activity> mList = new LinkedList<Activity>();
	private static ThinkSNSApplication instance;
//	public static final String baseUrl = "http://192.168.21.19/TS3/index.php";
	public static final String baseUrl = "http://42.121.113.32/weibo/";// "http://42.121.113.32/weibo/   http://192.168.21.19/TS3/index.php";
	public static String strUserSession = null;
	public static boolean comment_notify_enable = true;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		instance = this;
		super.onCreate();
	}

	public List<Activity> getmList() {
		return mList;
	}

	public void setmList(List<Activity> mList) {
		this.mList = mList;
	}

	// add Activity
	public void addActivity(Activity activity) {
		mList.add(activity);
	}

	// delete Activity
	public void deleteActivity(Activity activity) {
		mList.remove(activity);
	}

	public void exit() {
		try {
			for (Activity activity : mList) {
				if (activity != null)
					activity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

	public void onLowMemory() {
		super.onLowMemory();
		System.gc();
	}

	public static ThinkSNSApplication getInstance() {
		return instance;
	}

	public static void updateSession(String strNewSession) {
		strUserSession = strNewSession;
	}

}
