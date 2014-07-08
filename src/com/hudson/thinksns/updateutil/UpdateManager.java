
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
 * @author ʯ�ٲ�
 * 
 * ʵ��������µĹ�����
 */
public class UpdateManager {
	
	//������...
	private static final int DOWNLOAD = 1;
	//�������
	private static final int DOWNLOAD_FINISH = 2;
	/* �����ļ�ʧ�� */
    private static final int DOWNLOAD_ERROR = 3;
	//���������XML��Ϣ
	HashMap<String , String> mHashMap;
	//���ر���·��
	private String mSavePath;
	//��¼����������
	private int progress;
	//�Ƿ�ȡ������
	private boolean cancelUpdate = false;
	//�����Ķ���
	private Context mContext;
	//������
	private ProgressBar mProgressBar;
	//���½������ĶԻ���
	private Dialog mDownloadDialog;
	private AppVersion appVersion;
	 private UpdateListener updateListener = null; 
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			//�����С�����
			case DOWNLOAD:
				//���½�����
				System.out.println(progress);
				mProgressBar.setProgress(progress);
				mDownloadDialog.setTitle("���ڸ���"+"-"+progress+"%");
				break;
			//�������
			case DOWNLOAD_FINISH:
				// ��װ�ļ�
				installApk();
				break;
            case DOWNLOAD_ERROR:
            	Log.e("xiaz","error");
            	Toast.makeText(mContext, "�����ļ�ʧ�ܣ�", 1000).show();
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
	 * ����������
	 */
	public boolean checkUpdate() {
		if (isUpdate()) {
			//��ʾ��ʾ�Ի���
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
		//����Ի���
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle(R.string.soft_update_title);
		builder.setMessage(appVersion.getAppVersion_UpgradeTips());
		//����
		builder.setPositiveButton(R.string.soft_update_updatebtn, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				// ��ʾ���ضԻ���
				showDownloadDialog();
				if(updateListener != null)
                	updateListener.onStartUpdate();
			}
		});
		if(appVersion.getAppVersion_UpgradeMust()==1){
			//ǿ�Ƹ��£�û���Ժ����
		}else{
			// �Ժ����
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
		// ����������ضԻ���
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle(R.string.soft_updating);
		// �����ضԻ������ӽ�����
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.softupdate_progress, null);
		mProgressBar = (ProgressBar) view.findViewById(R.id.update_progress);
		builder.setView(view);
		if(appVersion.getAppVersion_UpgradeMust()==1){
			//ǿ�Ƹ��£�����ȡ������
		}else{
			// �Ժ����
			builder.setNegativeButton(R.string.soft_update_cancel, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					// ����ȡ��״̬
					cancelUpdate = true;
					   if(updateListener != null)
		                	updateListener.onCancelUpdate();
		            
				}
			});
		}
		builder.setCancelable(false);
		mDownloadDialog = builder.create();
		mDownloadDialog.show();
		//�����ļ�
		downloadApk();
	}
	
	/**
	 * ����APK�ļ�
	 */
	private void downloadApk() {
		// TODO Auto-generated method stub
		// �������߳��������
		new DownloadApkThread().start();
	}


	/**
	 * �������Ƿ��и��°汾
	 * @return
	 */
	public boolean isUpdate() {
	// ��ȡ��ǰ����汾
		int versionCode = getVersionCode(mContext);//������ʹ�õ�app��versionCode
	    Log.e("version", ""+versionCode);
		if(versionCode<this.appVersion.getAppVersionCode()){
			return true;
		}
		return false;
		
	}

	/**
	 * ��ȡ����汾��
	 * @param context
	 * @return
	 */
	private int getVersionCode(Context context) {
		// TODO Auto-generated method stub
		int versionCode = 0;

		// ��ȡ����汾�ţ���ӦAndroidManifest.xml��android:versionCode
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
	 * �����ļ��߳�
	 * @author Administrator
	 *
	 */
	private class DownloadApkThread extends Thread {
		@Override
		public void run() {
			try
			{
				//�ж�SD���Ƿ���ڣ������Ƿ���ж�дȨ��
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				{
					// ��ô洢����·��
                    String sdpath = Environment.getExternalStorageDirectory() + "/ThinkSNS/";
                    mSavePath = sdpath + "Download";
					URL url = new URL(appVersion.getAppVersion_DownloadUrl());
					// ��������
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					// ��ȡ�ļ���С
					int length = conn.getContentLength();
					// ����������
					InputStream is = conn.getInputStream();

					File file = new File(mSavePath);
					// ����ļ������ڣ��½�Ŀ¼
					if (!file.exists())
					{
						file.mkdir();
					}
					File apkFile = new File(mSavePath, "ThinkSNS.apk");
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// ����
					byte buf[] = new byte[1024];
					// д�뵽�ļ���
					do
					{
						int numread = is.read(buf);
						count += numread;
						// �����������λ��
						progress = (int) (((float) count / length) * 100);
						// ���½���
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0)
						{
							
	                        	mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
	                            break;
                        	
						}
						// д���ļ�
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);//���ȡ����ֹͣ����
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
			// ȡ�����ضԻ�����ʾ
			mDownloadDialog.dismiss();
		}
	}
	
	/**
	 * ��װAPK�ļ�
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