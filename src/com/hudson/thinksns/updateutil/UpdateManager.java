
package com.hudson.thinksns.updateutil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hudson.thinksns.R;
import com.hudson.thinksns.model.AppVersion;

/**
 * 
 * @author 石仲才
 * 
 * 实现软件更新的管理类
 */
public class UpdateManager {
	
	//下载中...
	private static final int DOWNLOAD = 1;
	//下载完成
	private static final int DOWNLOAD_FINISH = 2;
	/* 下载文件失败 */
    private static final int DOWNLOAD_ERROR = 3;
	//保存解析的XML信息
	HashMap<String , String> mHashMap;
	//下载保存路径
	private String mSavePath;
	//记录进度条数量
	private int progress;
	//是否取消更新
	private boolean cancelUpdate = false;
	//上下文对象
	private Context mContext;
	//进度条
	private ProgressBar mProgressBar;
	//更新进度条的对话框
	private Dialog mDownloadDialog;
	private AppVersion appVersion;
	 private UpdateListener updateListener = null; 
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			//下载中。。。
			case DOWNLOAD:
				//更新进度条
				System.out.println(progress);
				mProgressBar.setProgress(progress);
				mDownloadDialog.setTitle("正在更新"+"-"+progress+"%");
				break;
			//下载完成
			case DOWNLOAD_FINISH:
				// 安装文件
				installApk();
				break;
            case DOWNLOAD_ERROR:
            	Log.e("xiaz","error");
            	Toast.makeText(mContext, "下载文件失败！", 1000).show();
            	if(updateListener != null)
                	updateListener.onCancelUpdate();
            	break;
            default:
                break;
			}
		};
	};


	public UpdateManager(Context context,AppVersion mVersion) {
		super();
		this.mContext = context;
		this.appVersion=mVersion;
	}
	
	
	/**
	 * 检测软件更新
	 */
	public boolean checkUpdate() {
		if (isUpdate()) {
			//显示提示对话框
			showNoticeDialog();
			 return true;
		} 
		/*else {
			Toast.makeText(mContext, R.string.soft_update_no, Toast.LENGTH_SHORT).show();
		}*/
		 return false;
	}
	 public void setUpdateListener(UpdateListener updateListener)
	    {
	    	this.updateListener = updateListener;
	    }
	
	private void showNoticeDialog() {
		// TODO Auto-generated method stub
		//构造对话框
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle(R.string.soft_update_title);
		builder.setMessage(appVersion.getAppVersion_UpgradeTips());
		//更新
		builder.setPositiveButton(R.string.soft_update_updatebtn, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				// 显示下载对话框
				showDownloadDialog();
				if(updateListener != null)
                	updateListener.onStartUpdate();
			}
		});
		if(appVersion.getAppVersion_UpgradeMust()==1){
			//强制更新，没有稍后更新
		}else{
			// 稍后更新
			builder.setNegativeButton(R.string.soft_update_later, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					 if(updateListener != null)
		                	updateListener.onCancelUpdate();
				}
			});
		}
        builder.setOnCancelListener(new OnCancelListener(){

			@Override
			public void onCancel(DialogInterface arg0) {
				// TODO Auto-generated method stub
				 if(updateListener != null)
	                updateListener.onCancelUpdate();
			}
        	
        });
		Dialog noticeDialog = builder.create();
		noticeDialog.show();
	}
	
	private void showDownloadDialog() {
		// 构造软件下载对话框
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle(R.string.soft_updating);
		// 给下载对话框增加进度条
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.softupdate_progress, null);
		mProgressBar = (ProgressBar) view.findViewById(R.id.update_progress);
		builder.setView(view);
		if(appVersion.getAppVersion_UpgradeMust()==1){
			//强制更新，不能取消下载
		}else{
			// 稍后更新
			builder.setNegativeButton(R.string.soft_update_cancel, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					// 设置取消状态
					cancelUpdate = true;
					   if(updateListener != null)
		                	updateListener.onCancelUpdate();
		            
				}
			});
		}
		builder.setCancelable(false);
		mDownloadDialog = builder.create();
		mDownloadDialog.show();
		//下载文件
		downloadApk();
	}
	
	/**
	 * 下载APK文件
	 */
	private void downloadApk() {
		// TODO Auto-generated method stub
		// 启动新线程下载软件
		new DownloadApkThread().start();
	}


	/**
	 * 检查软件是否有更新版本
	 * @return
	 */
	public boolean isUpdate() {
	// 获取当前软件版本
		int versionCode = getVersionCode(mContext);//该正在使用的app的versionCode
	    Log.e("version", ""+versionCode);
		if(versionCode<this.appVersion.getAppVersionCode()){
			return true;
		}
		return false;
		
	}

	/**
	 * 获取软件版本号
	 * @param context
	 * @return
	 */
	private int getVersionCode(Context context) {
		// TODO Auto-generated method stub
		int versionCode = 0;

		// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
		try {
			versionCode = context.getPackageManager().getPackageInfo(
					"com.hudson.thinksns", 0).versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return versionCode;
	}
	
	/**
	 * 下载文件线程
	 * @author Administrator
	 *
	 */
	private class DownloadApkThread extends Thread {
		@Override
		public void run() {
			try
			{
				//判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				{
					// 获得存储卡的路径
                    String sdpath = Environment.getExternalStorageDirectory() + "/ThinkSNS/";
                    mSavePath = sdpath + "Download";
					URL url = new URL(appVersion.getAppVersion_DownloadUrl());
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					// 获取文件大小
					int length = conn.getContentLength();
					// 创建输入流
					InputStream is = conn.getInputStream();

					File file = new File(mSavePath);
					// 如果文件不存在，新建目录
					if (!file.exists())
					{
						file.mkdir();
					}
					File apkFile = new File(mSavePath, "ThinkSNS.apk");
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					do
					{
						int numread = is.read(buf);
						count += numread;
						// 计算进度条的位置
						progress = (int) (((float) count / length) * 100);
						// 更新进度
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0)
						{
							
	                        	mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
	                            break;
                        	
						}
						// 写入文件
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);//点击取消就停止下载
					fos.close();
					is.close();
				}
			} catch (MalformedURLException e)
			{
				if(updateListener != null)
            		updateListener.onDownloadError();
				e.printStackTrace();
			} catch (IOException e)
			{if(updateListener != null)
        		updateListener.onDownloadError();
				e.printStackTrace();
			}
			// 取消下载对话框显示
			mDownloadDialog.dismiss();
		}
	}
	
	/**
	 * 安装APK文件
	 */
	private void installApk()
	{
		File apkfile = new File(mSavePath, "ThinkSNS.apk");
		if (!apkfile.exists())
		{
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		 i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		mContext.startActivity(i);
	}
    public interface UpdateListener{
    	void onStartUpdate();
    	void onCancelUpdate();
    	void onFinishUpdate();
    	void onDownloadError();
    }
}