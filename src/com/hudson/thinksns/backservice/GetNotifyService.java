package com.hudson.thinksns.backservice;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hudson.thinksns.AtMeActivity;
import com.hudson.thinksns.MainActivity;
import com.hudson.thinksns.R;
import com.hudson.thinksns.otherActivity;
import com.hudson.thinksns.application.ThinkSNSApplication;
import com.hudson.thinksns.dbutils.DBManager;
import com.hudson.thinksns.model.Account;
import com.hudson.thinksns.model.Notify;
import com.hudson.thinksns.model.Statuses;
import com.hudson.thinksns.model.User;
import com.hudson.thinksns.netuti.MHttpClient;
import com.hudson.thinksns.netuti.MyNameValuePair;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
/**
 *@file  GetNotifyService.java
 *@author 石仲才
 *@description 得到Notify的后台服务，用于接收别人的评论通知
 */
public class GetNotifyService extends Service {
	private Timer mTimer;
	private TimerTask mTimeTask;
	private Handler mHandler;
	private Long timegap = 10 * 1000L;// 10秒
	private Account account;
	private final int getNofify = 0x33;
	private final int startgetNofify = 0x23;
	private DBManager dbm;
	private Context mContext;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		mContext = this;

		dbm = new DBManager(mContext);
		account = dbm.getAccountonline();
		mTimer = new Timer();
		mTimeTask = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message msg = mHandler.obtainMessage();
				msg.what = startgetNofify;
				mHandler.sendMessage(msg);

			}
		};
		mTimer.schedule(mTimeTask, 6000, timegap);// 6s
		initHandler();
	}

	private void initHandler() {
		// TODO Auto-generated method stub
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case startgetNofify:

					new GetNotifyThread().start();
					break;
				case getNofify:
					ArrayList<Notify> tNotifys = format(msg.obj.toString());
					if (tNotifys.size() != 0) {
						addNotificaction(tNotifys);
					}
					break;
				default:
					break;
				}
			}

		};
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		dbm.closeDB();
		super.onDestroy();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		dbm.closeDB();
		return super.onUnbind(intent);
	}

	private class GetNotifyThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			MyNameValuePair NameValuePair1 = new MyNameValuePair("app", "api");
			MyNameValuePair NameValuePair2 = new MyNameValuePair("mod",
					"Notifytion");
			MyNameValuePair NameValuePair3 = new MyNameValuePair("act",
					"get_notify_by_count");
			MyNameValuePair NameValuePair4 = new MyNameValuePair("oauth_token",
					account.getOauth_token());
			MyNameValuePair NameValuePair5 = new MyNameValuePair(
					"oauth_token_secret", account.getOauth_token_secret());

			String result = MHttpClient.post(ThinkSNSApplication.baseUrl,
					NameValuePair1, NameValuePair2, NameValuePair3,
					NameValuePair4, NameValuePair5);
			Log.e("re", result);

			Message msg = new Message();
			msg.what = getNofify;
			msg.obj = result;
			mHandler.sendMessage(msg);
		}

	}

	private ArrayList<Notify> format(String jsonstr) {

		ArrayList<Notify> notifys = new ArrayList<Notify>();

		try {
			JSONArray notify_jsonarray = new JSONArray(jsonstr);
			for (int i = 0; i < notify_jsonarray.length(); i++) {
				JSONObject notify_jsonobject = notify_jsonarray
						.getJSONObject(i);
				String type = notify_jsonobject.optString("type");
				String name = notify_jsonobject.optString("name");
				int count = Integer.parseInt(notify_jsonobject
						.optString("count"));
				Notify notify = new Notify();
				notify.setType(type);
				notify.setName(name);
				notify.setCount(count);
				notifys.add(notify);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return notifys;

	}

	/**
	 * 添加一个notification
	 */


	private void addNotificaction(ArrayList<Notify> tNotifys) {
		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

		for (Notify notify : tNotifys) {
			int count = notify.getCount();
			if (count != 0) {
				if (notify.getType().equalsIgnoreCase("notify")) {


				} else if (notify.getType().equalsIgnoreCase("atme")) {

					Intent resultIntent = new Intent(this, AtMeActivity.class);
					showNotification(mContext, R.drawable.add, "未读@信息提醒",
							"未读@信息：", "你有" + count + "条@信息", resultIntent, 222);
					

				} else if (ThinkSNSApplication.comment_notify_enable
						&& notify.getType().equalsIgnoreCase("comment")) {
					Intent resultIntent = new Intent(this, otherActivity.class);
				
					showNotification(mContext, R.drawable.add, "未读评论提醒",
							"未读评论提醒：", "你有" + count + "条未读评论", resultIntent,
							333);
			
				} else if (notify.getType().equalsIgnoreCase("new_folower")) {


				} else if (notify.getType().equalsIgnoreCase("unread_message")) {
	

				}

			}

			this.stopSelf();
		}
	}

	private void showNotification(Context context, int icon, String tickerText,
			String title, String content, Intent intent, int id) {
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		long when = System.currentTimeMillis();
		Notification notification = new Notification(icon, tickerText, when);
		PendingIntent pendIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(context, title, content, pendIntent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(id, notification);
		// notificationManager.cancel(id);
	}

}
