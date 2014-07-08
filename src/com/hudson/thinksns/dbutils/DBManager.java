package com.hudson.thinksns.dbutils;

import java.util.ArrayList;

import com.hudson.thinksns.model.Account;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
/** 
* 数据库管理
* @author 贾焱超
*/  
public class DBManager {
	private DBHelper helper;
	private SQLiteDatabase db;
	private static final String TAG = "DBManager_Log";
	public static final String TABLE_NAME = "User_tb";

	public DBManager(Context context) {
		Log.d(TAG, "DBManager --> Constructor");
		helper = new DBHelper(context);
		// 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
		// mFactory);
		// 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
		db = helper.getWritableDatabase();
	}

	public void add(Account u) {
		if (findUserByUid(u.getUid()).equals(u)) {
			update(u);
		} else {
			ContentValues cv = new ContentValues();
			cv.put("uid", u.getUid());
			cv.put("oauth_token", u.getOauth_token());
			cv.put("oauth_token_secret", u.getOauth_token_secret());
			cv.put("state", u.getState());
			cv.put("uname", u.getUname());
			cv.put("headicon", u.getHeadicon());
			db.insertOrThrow(TABLE_NAME, null, cv);
            
		}
	}

	public Account findUserByUid(int uid) {
		Cursor c = db.query(TABLE_NAME, null, " uid=? ",
				new String[] { Integer.toString(uid) }, null, null, null);
		Account u = new Account();
		while (c.moveToNext()) {
			u.setUid(uid);
			u.setOauth_token(c.getString(2));
			u.setOauth_token_secret(c.getString(3));
			u.setState(c.getInt(4));
			u.setUname(c.getString(5));
			u.setHeadicon(c.getString(6));
		}
		c.close();
		
		return u;
	}
   public Account getAccountonline(){
		Cursor c = db.query(TABLE_NAME, null, " state=? ",
				new String[] { Integer.toString(1) }, null, null, null);
		Account u = new Account();
		while (c.moveToNext()) {
			u.setUid(c.getInt(1));
			u.setOauth_token(c.getString(2));
			u.setOauth_token_secret(c.getString(3));
			u.setState(c.getInt(4));
			u.setUname(c.getString(5));
			u.setHeadicon(c.getString(6));
		}
		c.close();
		
		return u;
   }
   
	public void update(Account u) {
		ContentValues cv = new ContentValues();
		cv.put("uid", u.getUid());
		cv.put("oauth_token", u.getOauth_token());
		cv.put("oauth_token_secret", u.getOauth_token_secret());
		cv.put("state", u.getState());
		cv.put("uname", u.getUname());
		cv.put("headicon", u.getHeadicon());
		db.update(TABLE_NAME, cv, " uid=? ",
				new String[] { Integer.toString(u.getUid()) });
		
	}

	public void deleteByUid(int uid) {
		db.delete(TABLE_NAME, " uid=? ", new String[] { Integer.toString(uid) });
		
	}

	public ArrayList<Account> findAllUser() {
		Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
		ArrayList<Account> UserList = new ArrayList<Account>();
		while (c.moveToNext()) {
			Account u = new Account();
			u.setUid(c.getInt(1));
			u.setOauth_token(c.getString(2));
			u.setOauth_token_secret(c.getString(3));
			u.setState(c.getInt(4));
			u.setUname(c.getString(5));
			u.setHeadicon(c.getString(6));
			UserList.add(u);
		}
		c.close();
		
		return UserList;

	}
	/**
     * close database
     */
    public void closeDB()
    {
        Log.d(TAG, "DBManager --> closeDB");
        // 释放数据库资源
        db.close();
    }

}
