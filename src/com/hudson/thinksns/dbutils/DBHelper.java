package com.hudson.thinksns.dbutils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/** 
* 数据库操作类
* @author 王代银
*/  
@SuppressLint("NewApi")
public class DBHelper extends SQLiteOpenHelper {
	// 数据库版本号
    private static final int DATABASE_VERSION = 1;
    // 数据库名
    private static final String DATABASE_NAME = "ThinkSNS_DB.db";

    // 数据表名，一个数据库中可以有多个表（虽然本例中只建立了一个表）
    public static final String TABLE_NAME = "User_tb";
    private static final String TAG="DB_Log";
    // 构造函数，调用父类SQLiteOpenHelper的构造函数
    public DBHelper(Context context, String name, CursorFactory factory,
            int version, DatabaseErrorHandler errorHandler)
    {
        super(context, name, factory, version, errorHandler);

    }

    public DBHelper(Context context, String name, CursorFactory factory,
            int version)
    {
        super(context, name, factory, version);
        // SQLiteOpenHelper的构造函数参数：
        // context：上下文环境
        // name：数据库名字
        // factory：游标工厂（可选）
        // version：数据库模型版本号
    }

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        // 数据库实际被创建是在getWritableDatabase()或getReadableDatabase()方法调用时
        Log.d(TAG, "DatabaseHelper Constructor");
        // CursorFactory设置为null,使用系统默认的工厂类
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
	        // 每次打开数据库之后首先被执行

	        Log.d(TAG, "DatabaseHelper onOpen");
	    }

	

}
