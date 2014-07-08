package com.hudson.thinksns.dbutils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/** 
* ���ݿ������
* @author ������
*/  
@SuppressLint("NewApi")
public class DBHelper extends SQLiteOpenHelper {
	// ���ݿ�汾��
    private static final int DATABASE_VERSION = 1;
    // ���ݿ���
    private static final String DATABASE_NAME = "ThinkSNS_DB.db";

    // ���ݱ�����һ�����ݿ��п����ж������Ȼ������ֻ������һ����
    public static final String TABLE_NAME = "User_tb";
    private static final String TAG="DB_Log";
    // ���캯�������ø���SQLiteOpenHelper�Ĺ��캯��
    public DBHelper(Context context, String name, CursorFactory factory,
            int version, DatabaseErrorHandler errorHandler)
    {
        super(context, name, factory, version, errorHandler);

    }

    public DBHelper(Context context, String name, CursorFactory factory,
            int version)
    {
        super(context, name, factory, version);
        // SQLiteOpenHelper�Ĺ��캯��������
        // context�������Ļ���
        // name�����ݿ�����
        // factory���α깤������ѡ��
        // version�����ݿ�ģ�Ͱ汾��
    }

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        // ���ݿ�ʵ�ʱ���������getWritableDatabase()��getReadableDatabase()��������ʱ
        Log.d(TAG, "DatabaseHelper Constructor");
        // CursorFactory����Ϊnull,ʹ��ϵͳĬ�ϵĹ�����
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
      String sql="create table "+TABLE_NAME+" (tid INTEGER PRIMARY KEY AUTOINCREMENT, uid INTEGER, oauth_token VARCHA,oauth_token_secret VARCHA,state INTEGER,uname VARCHA,headicon VARCHA)";
      db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
		// TODO Auto-generated method stub

	}
	 @Override
	    public void onOpen(SQLiteDatabase db)
	    {
	        super.onOpen(db);
	        // ÿ�δ����ݿ�֮�����ȱ�ִ��

	        Log.d(TAG, "DatabaseHelper onOpen");
	    }

	

}
