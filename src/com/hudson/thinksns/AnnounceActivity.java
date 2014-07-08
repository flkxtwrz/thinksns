package com.hudson.thinksns;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.hudson.thinksns.application.ThinkSNSApplication;
import com.hudson.thinksns.cacheutil.CleanCache;
import com.hudson.thinksns.dbutils.DBManager;
import com.hudson.thinksns.model.Account;
import com.hudson.thinksns.statusparse.EmotionMap;
/**
 *@file  AnnounceActivity.java
 *@author ���ͳ�
 *@description ����΢����������Խ��棨�����ã�
 */
public class AnnounceActivity extends Activity {
	private DBManager dbm;
	private Account ac;
	private ImageView home;// ��ҳ
	private ImageView write;// ��΢��
	private ImageView message;// ��Ϣ
	private ImageView refresh;// ˢ��
	private ImageView more;// ����
	private TextView info;// ��ʾ����
	private AutoCompleteTextView content;// ����΢����
	private ImageButton emotionbutton;// ����
	private ImageButton camerabutton;// ͼƬѡ��
	private ImageView preview;// ͼƬԤ��
	private Button annobutton;// ������ť
	private TextView head;// ����
	private AlertDialog dialog;
	private LinearLayout ann_linearlayout;
	private View menuView;
	private GridView menuGrid;
	private int flag = 0;// ��־
	private Context mContext;
	private String type;
	private int crop = 300;
	private File tempFile;
	private File sdcardTempFile;
	private static final String CHARSET = HTTP.UTF_8;
	public static final String baseUrl = "http://192.168.21.19/TS3/index.php";
	private String[] books;
	int[] emotion_image_array = EmotionMap.imgemotion;
	String[] menu_name_array = EmotionMap.wenziemotion;// {"�鿴������Ϣ","ˢ��΢��","����΢��","�鿴ʹ�ð���"};
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write_tencent);
		books = new String[] { "@rollen", "@rollenholt", "@rollenren", "@roll",
				"##" };
		dbm = new DBManager(this);
		ac = dbm.getAccountonline();
		findViews();
		initViews();
		setListeners();
		initFile();
		initHandler();
	}

	private void initFile() {
		// TODO Auto-generated method stub
		File cacheDir;
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			cacheDir = new File(
					android.os.Environment.getExternalStorageDirectory()
							+ "/Sdd", "Announce");
		else
			cacheDir = ThinkSNSApplication.getInstance().getCacheDir();
		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}

		tempFile = new File(cacheDir, getPhotoFileName("TS_PZ"));

		Log.e("tempFile", tempFile.toString());
		sdcardTempFile = new File(cacheDir, "TS_PICK_"
				+ SystemClock.currentThreadTimeMillis() + ".jpg");
		Log.e("sdcardTempFile", sdcardTempFile.toString());
	}

	private void setListeners() {
		// TODO Auto-generated method stub
		home.setOnClickListener(clicklistner);
		write.setOnClickListener(clicklistner);
		message.setOnClickListener(clicklistner);
		refresh.setOnClickListener(clicklistner);
		more.setOnClickListener(clicklistner);
		preview.setOnClickListener(prelistener);
		menuGrid.setOnItemClickListener(itemclicklistener);
		emotionbutton.setOnClickListener(emotionlistener);
		camerabutton.setOnClickListener(cameralistener);
		annobutton.setOnClickListener(listener);
		content.addTextChangedListener(mTextWatcher);
	}

	private void initViews() {
		// TODO Auto-generated method stub
		head.setText("@����΢��");
		mContext = this;
		menuGrid.setAdapter(getMenuAdapter(menu_name_array, emotion_image_array));
		info.setTextColor(Color.GREEN);
		ArrayAdapter<String> av = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, books);
		content.setAdapter(av);
		content.setThreshold(1);

	}

	private void findViews() {
		// TODO Auto-generated method stub
		head = (TextView) this.findViewById(R.id.write_label_tencent);
		home = (ImageView) this.findViewById(R.id.shouyetu);
		write = (ImageView) this.findViewById(R.id.faweibotu);
		message = (ImageView) this.findViewById(R.id.messagetu);
		refresh = (ImageView) this.findViewById(R.id.shuaxintu);
		more = (ImageView) this.findViewById(R.id.moretu);
		ann_linearlayout = (LinearLayout) this.findViewById(R.id.writelinear);
		preview = (ImageView) this.findViewById(R.id.tupian);
		menuView = View.inflate(this, R.layout.emotion_gridview, null);
		menuGrid = (GridView) menuView.findViewById(R.id.emotion_gridview);

		content = (AutoCompleteTextView) this.findViewById(R.id.statuscontent);
		info = (TextView) this.findViewById(R.id.tishi);
		annobutton = (Button) this.findViewById(R.id.fabu);
		emotionbutton = (ImageButton) this.findViewById(R.id.biaoqing);
		camerabutton = (ImageButton) this.findViewById(R.id.shangchuan);
	}

	private void initHandler() {
		// TODO Auto-generated method stub
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				Toast.makeText(mContext, msg.obj.toString(), Toast.LENGTH_SHORT)
						.show();
				// tv.setText(msg.obj.toString());
			}

		};
	}

	private OnClickListener clicklistner = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.shouyetu) {
				String strCache = android.os.Environment
						.getExternalStorageDirectory() + "/sdd/Announce";
				String strFileSize = CleanCache.cleanApplicationData(mContext,
						strCache);
				Toast.makeText(mContext, "�������" + strFileSize + "M", 1000)
						.show();
			}

			/*
			 * // TODO Auto-generated method stub switch (v.getId()) { case
			 * R.id.shouyetu: Intent intent = new Intent(AnnounceActivity.this,
			 * showWeibo.class); // intent.setClass(AnnounceActivity.this,
			 * showWeibo.class); // new showWeibo().onRestart();
			 * AnnounceActivity.this.startActivity(intent); break; case
			 * R.id.faweibotu: new AnnounceActivity().onRestart(); break; case
			 * R.id.shuaxintu: new AnnounceActivity().onRestart(); break; case
			 * R.id.write_label_tencent: Intent intent2 = new
			 * Intent(AnnounceActivity.this, showInfo.class);
			 * intent2.putExtra("name", ac.getUname()); // /
			 * intent2.putExtra("expire", ac.getExpires_in());
			 * AnnounceActivity.this.startActivity(intent2); break; case
			 * R.id.moretu: Intent intent3 = new Intent(AnnounceActivity.this,
			 * MoreMenu.class); AnnounceActivity.this.startActivity(intent3);
			 * break; }
			 */}

	};

	private SimpleAdapter getMenuAdapter(String[] menuNameArray,
			int[] imageResourceArray) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < imageResourceArray.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemImage", imageResourceArray[i]);
			map.put("itemText", menuNameArray[i]);
			data.add(map);
		}
		SimpleAdapter simperAdapter = new SimpleAdapter(this, data,
				R.layout.emotion_item,
				new String[] { "itemImage", "itemText" }, new int[] {
						R.id.emotion_image, R.id.emotion_text });
		return simperAdapter;
	}

	private OnClickListener prelistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			v.setVisibility(View.GONE);
		}
	};
	private OnClickListener cameralistener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub

			/*
			 * startActivityForResult(new Intent(AnnounceActivity.this,
			 * SelectPicPopupWindow.class), 1);
			 */

			if (dialog == null) {
				dialog = new AlertDialog.Builder(mContext).setItems(
						new String[] { "���", "���" },
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (which == 0) {
									// ѡ������
									type = "pz";
									Intent cameraintent = new Intent(
											MediaStore.ACTION_IMAGE_CAPTURE);
									// ָ������������պ���Ƭ�Ĵ���·��
									cameraintent.putExtra(
											MediaStore.EXTRA_OUTPUT,
											Uri.fromFile(tempFile));
									startActivityForResult(cameraintent, 101);

								} else {
									type = "pick";
									Intent intent = new Intent(
											"android.intent.action.PICK");
									intent.setDataAndType(
											MediaStore.Images.Media.INTERNAL_CONTENT_URI,
											"image/*");
									intent.putExtra("output",
											Uri.fromFile(sdcardTempFile));
									intent.putExtra("crop", "true");
									intent.putExtra("aspectX", 1);// �ü������
									intent.putExtra("aspectY", 1);
									intent.putExtra("outputX", crop);// ���ͼƬ��С
									intent.putExtra("outputY", crop);
									startActivityForResult(intent, 100);
								}
							}
						}).create();
			}
			if (!dialog.isShowing()) {
				dialog.show();
			}

		}
	};

	private OnClickListener emotionlistener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (flag % 2 == 0) {
				ann_linearlayout.addView(menuView);
			} else {
				ann_linearlayout.removeView(menuView);
			}
			flag++;
			// TODO Auto-generated method stub

		}
	};

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			new SendPicThread(type).start();
		}

	};

	private class SendPicThread extends Thread {
		private String type;

		public SendPicThread(String type) {

			this.type = type;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub

			String response = null; // ;
			File file = null;
			if (preview.isShown()) {
				file = (type.equals("pz")) ? tempFile : sdcardTempFile;
			}
			try {
				response = postStatus(baseUrl, content.getText().toString(),
						file);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.e("re", response.toString());
			Message msg = Message.obtain();
			msg.obj = response.toString();
			mHandler.sendMessage(msg);
		}

	}

	private String postStatus(String url, String content, File f)
			throws IOException {
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = null;
		HttpPost httpPost = new HttpPost(url);
		MultipartEntity reqEntity = new MultipartEntity();

		StringBody sb1, sb2, sb3, sb4, sb5, sb6, sb7;

		sb1 = new StringBody("api");
		sb2 = new StringBody("WeiboStatuses");
		if (f != null) {

			FileBody file = new FileBody(f);
			reqEntity.addPart("File", file);
			sb3 = new StringBody("upload");
		} else {
			sb3 = new StringBody("update");
		}

		sb4 = new StringBody(ac.getOauth_token());
		sb5 = new StringBody(ac.getOauth_token_secret());
		sb6 = new StringBody(content);
		sb7 = new StringBody("2");
		reqEntity.addPart("app", sb1);
		reqEntity.addPart("mod", sb2);
		reqEntity.addPart("act", sb3);
		reqEntity.addPart("oauth_token", sb4);
		reqEntity.addPart("oauth_token_secret", sb5);
		reqEntity.addPart("content", sb6);
		reqEntity.addPart("from", sb7);

		httpPost.setEntity(reqEntity);

		response = client.execute(httpPost);
		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
			throw new RuntimeException("����ʧ��");
		}
		HttpEntity resEntity = response.getEntity();
		return (resEntity == null) ? null : EntityUtils.toString(resEntity,
				CHARSET);

	}

	private OnItemClickListener itemclicklistener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View item, int position,
				long arg3) {
			// TODO Auto-generated method stub
			int itmeid = emotion_image_array[position];// ������Դ��id

			String emotion_text = EmotionMap.getEmMapI().get(itmeid);// �����Ӧ������
			Log.e("text-->", emotion_text);
			SpannableString spannableString = new SpannableString(emotion_text);
			Drawable drawable2 = AnnounceActivity.this.getResources()
					.getDrawable(itmeid);
			drawable2.setBounds(0, 0, drawable2.getIntrinsicWidth() * 2,
					drawable2.getIntrinsicHeight() * 2);
			ImageSpan imageSpan = new ImageSpan(drawable2,
					ImageSpan.ALIGN_BASELINE);
			spannableString.setSpan(imageSpan, 0, emotion_text.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			content.append(spannableString);

		}
	};
	TextWatcher mTextWatcher = new TextWatcher() {
		private CharSequence temp;
		private int editStart;
		private int editEnd;

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			temp = s;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			// mTextView.setText(s);//�����������ʵʱ��ʾ
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			editStart = content.getSelectionStart();
			editEnd = content.getSelectionEnd();
			int t = 140 - temp.length();
			if (t > 10) {
				info.setText("��������" + t + "����");
				info.setTextColor(Color.GREEN);
			} else {
				info.setText("��������" + t + "����");
				info.setTextColor(Color.RED);
			}
			if (temp.length() > 140) {
				Toast.makeText(AnnounceActivity.this, "������������Ѿ����������ƣ�",
						Toast.LENGTH_SHORT).show();
				s.delete(editStart - 1, editEnd);
				int tempSelection = editStart;
				content.setText(s);
				content.setSelection(tempSelection);
			}
		}

	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 100) {
			Bitmap bmp = BitmapFactory.decodeFile(sdcardTempFile
					.getAbsolutePath());
			if (!preview.isShown()) {
				preview.setVisibility(View.VISIBLE);
			}
			preview.setImageBitmap(bmp);
		} else if (requestCode == 101) {
			// ѡ������
			startPhotoZoom(Uri.fromFile(tempFile));
		} else if (requestCode == 102) {
			if (data != null)
				// setPicToView(data);
				sentPicToNext(data);

		}
	}

	private void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// cropΪtrue�������ڿ�����intent��������ʾ��view���Լ���
		intent.putExtra("crop", "true");

		// aspectX aspectY �ǿ�ߵı���
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);

		// outputX,outputY �Ǽ���ͼƬ�Ŀ��
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);
		intent.putExtra("noFaceDetection", true);
		startActivityForResult(intent, 102);

	}

	// �����м��ú��ͼƬ���ݵ���һ��������
	private void sentPicToNext(Intent picdata) {
		Bundle bundle = picdata.getExtras();
		if (bundle != null) {
			Bitmap photo = bundle.getParcelable("data");
			if (photo == null) {
			} else {
				if (!preview.isShown()) {
					preview.setVisibility(View.VISIBLE);
				}
				preview.setImageBitmap(photo);
			}

			ByteArrayOutputStream baos = null;
			try {
				baos = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				byte[] photodata = baos.toByteArray();
				System.out.println(photodata.toString());
				// Intent intent = new Intent();
				// intent.setClass(RegisterActivity.this, ShowActivity.class);
				// intent.putExtra("photo", photodata);
				// startActivity(intent);
				// finish();
			} catch (Exception e) {
				e.getStackTrace();
			} finally {
				if (baos != null) {
					try {
						baos.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		dbm.closeDB();
	}

	// ʹ��ϵͳ��ǰ���ڼ��Ե�����Ϊ��Ƭ������
	private String getPhotoFileName(String poststr) {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("'" + poststr
				+ "_IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}

}
