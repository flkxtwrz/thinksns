package com.hudson.thinksns.userinfo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.hudson.thinksns.Fans;
import com.hudson.thinksns.MainActivity;
import com.hudson.thinksns.R;

import com.hudson.thinksns.adapter.WeiboListAdapter;
import com.hudson.thinksns.application.ThinkSNSApplication;
import com.hudson.thinksns.dbutils.DBManager;
import com.hudson.thinksns.diyview.xlistview.XListView;
import com.hudson.thinksns.diyview.xlistview.XListView.IXListViewListener;

import com.hudson.thinksns.imageloader.ImageLoader;
import com.hudson.thinksns.model.Account;
import com.hudson.thinksns.model.Statuses;
import com.hudson.thinksns.model.User;
import com.hudson.thinksns.model.UserPhoto;
import com.hudson.thinksns.netuti.MHttpClient;
import com.hudson.thinksns.netuti.MyNameValuePair;
import com.hudson.thinksns.timeutil.TimeUtil;
/** 
* @author 贾焱超
*/
public class SelfHome extends Activity implements IXListViewListener {
	private DBManager dbm;
	private Account ac;
	private Context mContext;
	private String uid;
	private ImageLoader imageLoader;
	private Handler mHandler;
	private TextView username_top, name, location, brief, user_tag, blog, fan,
			care;
	private ImageView photo, gender;
	private TextView photo_num;
	private LinearLayout m_pic, m_colle, m_good, m_card, m_blog, m_fan, m_care,
			below_ll = null;
	private TextView myprofile = null;
	private Button back, more = null;
	private XListView mListView = null;
	private int i = 0;
	private final int getweibo = 0x11;
	private final int getuserinfo = 0x12;
	private final int getuserphoto = 0x13;
	private final int num = 5;
	private int page = 1;
	private final int LoadMore = 0x33;
	private int state = 0;
	private String weibostr = "";
	private WeiboListAdapter mWeiboListAdapter;
	private ArrayList<Statuses> mstatuses = new ArrayList<Statuses>();
	private ArrayList<UserPhoto> photos = new ArrayList<UserPhoto>();

	private static final String CHARSET = HTTP.UTF_8;
	private static final String IMAGE_FILE_NAME = "faceImage.jpg";
	private String[] items = new String[] { "选择本地图片", "拍照" };
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	private static final int CAMERA_Next = 3;
	private File tempFile, sdcardTempFile;
	private String type;
	private String s_username, s_gender, s_location, s_tag, s_profile;

	// public SelfHome(String s_username, String s_gender, String s_location,
	// String s_tag, String s_profile) {
	// super();
	// this.s_username = s_username;
	// this.s_gender = s_gender;
	// this.s_location = s_location;
	// this.s_tag = s_tag;
	// this.s_profile = s_profile;
	// }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		/* set it to be full screen */
		setContentView(R.layout.self_home);
		findviews();
		mContext = this;
		dbm = new DBManager(this);
		ac = dbm.getAccountonline();
		uid = getIntent().getStringExtra("uid");
		imageLoader = new ImageLoader(mContext);
		initHander();
		new GetUserInfoThread().start();
		new GetUserphotosThread().start();
		OnClickListener ocl = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.selfhome_back:
					// TODO click things
					Navigate(MainActivity.class);
					break;
				// case R.id.selfhome_setting:
				// // TODO click things
				// // Navigate(MainUiActivity.class);
				// break;
				case R.id.selfhome_profile:
					// TODO click things
					Intent senddata = new Intent(SelfHome.this,
							SelfHomeProfile.class);
					senddata.putExtra("username", s_username);
					senddata.putExtra("gender", s_gender);
					senddata.putExtra("location", s_location);
					senddata.putExtra("tag", s_tag);
					senddata.putExtra("profile", s_profile);
					senddata.putExtra("uid", uid);
					startActivity(senddata);
					SelfHome.this.finish();
					// Navigate(SelfHomeProfile.class);
					// Navigate(MainUiActivity.class);
					break;
				case R.id.selfhome_blog:
					// TODO click things
					if (i == 0) {
						below_ll.setVisibility(View.GONE);
						mListView.setVisibility(View.VISIBLE);
						// inflate();
						GetUserStatuesThread getstatuesthread = new GetUserStatuesThread();
						getstatuesthread.start();
						i = 1;
					} else {
						below_ll.setVisibility(View.VISIBLE);
						mListView.setVisibility(View.GONE);
						i = 0;
					}
					// Navigate(MainUiActivity.class);
					break;
				case R.id.selfhome_myphoto_linear:
					// TODO click things
					int photos_count = Integer.parseInt(photo_num.getText()
							.toString());
					if (photos_count != 0) {
						Intent i = new Intent();
						i.putExtra("photos", photos);
						i.setClass(SelfHome.this, SelfHomePhoto.class);
						i.putExtra("uid", uid);
						startActivity(i);
						SelfHome.this.finish();
					}
					// Navigate(SelfHomePhoto.class);
					// Navigate(MainUiActivity.class);
					break;
				case R.id.selfhome_mylove_linear:
					// TODO click things
					Intent i = new Intent();
					// i.putExtra("uid", uid);
					i.setClass(SelfHome.this, SelfHomeCollect.class);
					i.putExtra("uid", uid);
					startActivity(i);
					SelfHome.this.finish();
					// Navigate(MainUiActivity.class);
					break;
				case R.id.selfhome_good_linear:
					// TODO click things
					// Navigate(SelfHomeGood.class);
					// Navigate(MainUiActivity.class);
					break;
				case R.id.self_mycard_linearlayout:
					// TODO click things
					// Navigate(SelfHomeCard.class);
					// Navigate(MainUiActivity.class);
					break;
				case R.id.selfhome_photo:
					showDialog();
					break;
				case R.id.selfhome_fan:
					// Navigate(Fans.class);
					Intent ifan = new Intent(SelfHome.this, Fans.class);
					ifan.putExtra("checkwhat", 1);
					ifan.putExtra("uid", uid);
					// ifan.putExtra("title", "粉丝");
					ifan.putExtra("mainorhome", 1);
					startActivity(ifan);
					SelfHome.this.finish();
					break;
				case R.id.selfhome_care:
					Intent ifan2 = new Intent(SelfHome.this, Fans.class);
					ifan2.putExtra("checkwhat", 0);
					ifan2.putExtra("uid", uid);
					ifan2.putExtra("mainorhome", 1);
					startActivity(ifan2);
					SelfHome.this.finish();
					break;
				}
			}
		};
		photo.setOnClickListener(ocl);
		back.setOnClickListener(ocl);
		// more.setOnClickListener(ocl);
		myprofile.setOnClickListener(ocl);
		m_blog.setOnClickListener(ocl);
		m_card.setOnClickListener(ocl);
		m_colle.setOnClickListener(ocl);
		m_good.setOnClickListener(ocl);
		m_pic.setOnClickListener(ocl);
		m_care.setOnClickListener(ocl);
		m_fan.setOnClickListener(ocl);
	}

	/**
	 * 显示选择对话框
	 */
	private void showDialog() {

		new AlertDialog.Builder(this)
				.setTitle("设置头像")
				.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							type = "pick";
							initScardTempFile();
							// Intent intentFromGallery = new Intent();
							// intentFromGallery.setType("image/*"); // 设置文件类型
							// intentFromGallery
							// .setAction(Intent.ACTION_GET_CONTENT);
							// startActivityForResult(intentFromGallery,
							// IMAGE_REQUEST_CODE);
							Intent intentFromGallery = new Intent(
									"android.intent.action.PICK");
							intentFromGallery
									.setDataAndType(
											MediaStore.Images.Media.INTERNAL_CONTENT_URI,
											"image/*");
							intentFromGallery.putExtra("output",
									Uri.fromFile(sdcardTempFile));
							intentFromGallery.putExtra("crop", "true");
							intentFromGallery.putExtra("aspectX", 1);// 裁剪框比例
							intentFromGallery.putExtra("aspectY", 1);
							intentFromGallery.putExtra("outputX", 320);// 输出图片大小
							intentFromGallery.putExtra("outputY", 320);
							startActivityForResult(intentFromGallery,
									IMAGE_REQUEST_CODE);
							break;
						case 1:
							type = "pz";
							initTempFile();
							Intent intentFromCapture = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,
									Uri.fromFile(tempFile));
							// 判断存储卡是否可以用，可用进行存储
							// if (hasSdcard()) {
							//
							// intentFromCapture.putExtra(
							// MediaStore.EXTRA_OUTPUT,
							// Uri.fromFile(new File(Environment
							// .getExternalStorageDirectory(),
							// IMAGE_FILE_NAME)));
							// }

							startActivityForResult(intentFromCapture,
									CAMERA_REQUEST_CODE);
							break;
						}
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();

	}

	private void initScardTempFile() {
		File cacheDir;
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			cacheDir = new File(
					android.os.Environment.getExternalStorageDirectory()
							+ "/ThinkSNS", "User_Face");
		else
			cacheDir = ThinkSNSApplication.getInstance().getCacheDir();
		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}
		sdcardTempFile = new File(cacheDir, "TS_PICK_"
				+ SystemClock.currentThreadTimeMillis() + ".jpg");
	}

	private void initTempFile() {
		File cacheDir;
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			cacheDir = new File(
					android.os.Environment.getExternalStorageDirectory()
							+ "/ThinkSNS", "User_Face");
		else
			cacheDir = ThinkSNSApplication.getInstance().getCacheDir();
		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}

		tempFile = new File(cacheDir, getPhotoFileName("User_Face"));

		Log.e("tempFile", tempFile.toString());
	}

	private String getPhotoFileName(String poststr) {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("'" + poststr
				+ "_IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}

	private void initHander() {
		// TODO Auto-generated method stub
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {

				// TODO Auto-generated method stub
				switch (msg.what) {
				case getweibo:
					weibostr = msg.obj.toString();
					mListView.setPullLoadEnable(true);
					if (weibostr.equals("[]")) {
						mListView.setPullLoadEnable(false);
						break;
					}
					ArrayList<Statuses> tstatuses = format();
					Log.e("微博个数：", "" + tstatuses.size());
					// Log.e("states", tstatuses.toString());
					if (tstatuses.size() != 0) {
						for (Statuses s : tstatuses) {
							mstatuses.add(s);
						}
					}
					Log.e("hhhh->>>>", mstatuses.size() + "");
					if (state == LoadMore) {
						mWeiboListAdapter.notifyDataSetChanged();
						break;
					}

					mWeiboListAdapter = new WeiboListAdapter(mstatuses,
							new ImageLoader(mContext), mContext, ac);

					mListView.setAdapter(mWeiboListAdapter);

					break;
				case getuserinfo:
					User user = (User) msg.obj;
					//username_top.setText(user.getUname());
					name.setText(user.getUname());
					location.setText(user.getLocation());
					brief.setText("简介：" + user.getIntro());
					user_tag.setText("标签：" + user.getTag());
					blog.setText(user.getFeedcount() + "");
					fan.setText(user.getFollower() + "");
					care.setText(user.getFollowing() + "");
					imageLoader.DisplayImage(user.getHeadicon(), photo);
					if (user.getSex().equals("\\u5973")) {
						gender.setImageResource(R.drawable.fans_sex_boy);
					} else {
						gender.setImageResource(R.drawable.fans_sex_girl);
					}
					setText(user.getUname(), user.getSex(), user.getLocation(),
							user.getTag(), user.getIntro());
					break;
				case getuserphoto:
					// ArrayList<UserPhoto> photos=(ArrayList<UserPhoto>)
					// msg.obj;
					photo_num.setText(String.valueOf(photos.size()));
					break;
				// case 0x26:
				//
				// Toast.makeText(SelfHome.this, "aaaa找到存储卡，无法存储照片！",
				// Toast.LENGTH_LONG).show();
				// break;
				default:
					break;
				}

			}

		};

	}

	protected void setText(String uname, String sex, String location,
			String tag, String intro) {
		// TODO Auto-generated method stub
		this.s_username = uname;
		this.s_gender = sex;
		this.s_location = location;
		this.s_tag = tag;
		this.s_profile = intro;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// if(requestCode ==IMAGE_REQUEST_CODE
		// ||requestCode==CAMERA_REQUEST_CODE||requestCode==RESULT_REQUEST_CODE){
		switch (requestCode) {
		case IMAGE_REQUEST_CODE:
			// startPhotoZoom(data.getData());
			Bitmap bmp = BitmapFactory.decodeFile(sdcardTempFile
					.getAbsolutePath());
			new upLoadFaceThread().start();
			Log.e("afsf", "ghh");
			photo.setImageBitmap(bmp);
			break;
		case CAMERA_REQUEST_CODE:
			startPhotoZoom(Uri.fromFile(tempFile));
			/*
			 * if (resultCode == Activity.RESULT_OK) { if (hasSdcard()) { File
			 * tempFile = new File( Environment.getExternalStorageDirectory() +
			 * "/" + IMAGE_FILE_NAME); startPhotoZoom(Uri.fromFile(tempFile)); }
			 * else { Toast.makeText(SelfHome.this, "未找到存储卡，无法存储照片！",
			 * Toast.LENGTH_LONG).show(); } }
			 */
			break;
		/*
		 * case RESULT_REQUEST_CODE: if (data != null) { getImageToView(data);
		 * 
		 * } break;
		 */
		case CAMERA_Next:
			if (data != null)
				// setPicToView(data);
				sentPicToNext(data);
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
		// }
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, CAMERA_Next);
	}

	// 将进行剪裁后的图片传递到下一个界面上
	private void sentPicToNext(Intent picdata) {
		Bundle bundle = picdata.getExtras();
		if (bundle != null) {
			Bitmap face = bundle.getParcelable("data");
			if (face == null) {
				Toast.makeText(mContext, "上传失败！", Toast.LENGTH_SHORT).show();
			} else {
				new upLoadFaceThread().start();
				// 应该根据返回结果判断是否显示新的用户图片
				Log.e("afsf", "ghh");
				photo.setImageBitmap(face);
				Toast.makeText(mContext, "上传成功！", Toast.LENGTH_SHORT).show();
				// if (!preview.isShown()) {
				// preview.setVisibility(View.VISIBLE);
				// }
				// preview.setImageBitmap(photo);
			}

			ByteArrayOutputStream baos = null;
			try {
				baos = new ByteArrayOutputStream();
				face.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				byte[] photodata = baos.toByteArray();
				System.out.println(photodata.toString());

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

	// 上传用户头像线程
	private class upLoadFaceThread extends Thread {

		@Override
		public void run() {
			Log.e("上传头像xianc ", "线程启动");
			File f = (type.equals("pz")) ? tempFile : sdcardTempFile;
			try {
				String result = upload_face(ThinkSNSApplication.baseUrl, f);
				/*
				 * 由于时间创促，请解析result【判断成功与否，图片大小限制1MB】
				 */
				Log.e("上传头像result", result);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private String upload_face(String url, File f) throws ParseException,
			IOException {
		Log.e("上传头像", "上传头像中1");
		// TODO Auto-generated method stub
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = null;
		HttpPost httpPost = new HttpPost(url);
		MultipartEntity reqEntity = new MultipartEntity();

		StringBody sb1, sb2, sb3, sb4, sb5;

		sb1 = new StringBody("api");
		sb2 = new StringBody("User");

		sb3 = new StringBody("upload_face");
		sb4 = new StringBody(ac.getOauth_token());
		sb5 = new StringBody(ac.getOauth_token_secret());
		// File f;
		// f = (type.equals("pz")) ? tempFile : sdcardTempFile;
		FileBody photofile = new FileBody(f);
		reqEntity.addPart("Filedata", photofile);
		reqEntity.addPart("app", sb1);
		reqEntity.addPart("mod", sb2);
		reqEntity.addPart("act", sb3);
		reqEntity.addPart("oauth_token", sb4);
		reqEntity.addPart("oauth_token_secret", sb5);
		Log.e("上传头像", "上传头像中21");
		httpPost.setEntity(reqEntity);
		response = client.execute(httpPost);
		Log.e("上传头像", "上传头像中2");
		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
			Log.e("上传头像", "上传头像中3");
			throw new RuntimeException("请求失败");
		}
		HttpEntity resEntity = response.getEntity();
		Log.e("shangc", resEntity.toString());
		return (resEntity == null) ? null : EntityUtils.toString(resEntity,
				CHARSET);

	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		// if (extras != null) {
		Bitmap photos = (Bitmap) extras.get("data");
		// Drawable drawable = new BitmapDrawable(photos);
		// photo.setImageDrawable(drawable);
		photo.setImageBitmap(photos);
		// new SendPicThread(type).start();
		// }
	}

	// private class SendPicThread extends Thread {
	// private String type;
	//
	// public SendPicThread(String type) {
	//
	// this.type = type;
	// }
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	//
	// String response = null; // ;
	// File file = null;
	// if (photo.isShown()) {
	// file = (type.equals("pz")) ? tempFile : sdcardTempFile;
	// }
	// try {
	// response = postStatus(ThinkSNSApplication.baseUrl, IMAGE_FILE_NAME,
	// file);
	// } catch (ClientProtocolException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// Log.e("re", response.toString());
	// Message msg = Message.obtain();
	// msg.what = 0x26;
	// msg.obj = response.toString();
	// mHandler.sendMessage(msg);
	// }
	//
	// }
	// private void initFile() {
	// // TODO Auto-generated method stub
	// File cacheDir;
	// if (android.os.Environment.getExternalStorageState().equals(
	// android.os.Environment.MEDIA_MOUNTED))
	// cacheDir = new File(
	// android.os.Environment.getExternalStorageDirectory()
	// + "/Sdd", "Announce");
	// else
	// cacheDir = ThinkSNSApplication.getInstance().getCacheDir();
	// if (!cacheDir.exists()) {
	// cacheDir.mkdirs();
	// }
	//
	// tempFile = new File(cacheDir, getPhotoFileName("TS_PZ"));
	//
	// Log.e("tempFile", tempFile.toString());
	// sdcardTempFile = new File(cacheDir, "TS_PICK_"
	// + SystemClock.currentThreadTimeMillis() + ".jpg");
	// Log.e("sdcardTempFile", sdcardTempFile.toString());
	// }
	//
	// private String getPhotoFileName(String poststr) {
	// Date date = new Date(System.currentTimeMillis());
	// SimpleDateFormat dateFormat = new SimpleDateFormat("'" + poststr
	// + "_IMG'_yyyyMMdd_HHmmss");
	// return dateFormat.format(date) + ".jpg";
	// }
	//
	// private String postStatus(String url, String content, File f)
	// throws IOException {
	// HttpClient client = new DefaultHttpClient();
	// HttpResponse response = null;
	// HttpPost httpPost = new HttpPost(url);
	// MultipartEntity reqEntity = new MultipartEntity();
	//
	// StringBody sb1, sb2, sb3, sb4, sb5, sb6, sb7;
	//
	// sb1 = new StringBody("api");
	// sb2 = new StringBody("WeiboStatuses");
	// if (f != null) {
	//
	// FileBody file = new FileBody(f);
	// reqEntity.addPart("File", file);
	// sb3 = new StringBody("upload");
	// } else {
	// sb3 = new StringBody("update");
	// }
	//
	// sb4 = new StringBody(ac.getOauth_token());
	// sb5 = new StringBody(ac.getOauth_token_secret());
	// sb6 = new StringBody(content);
	// sb7 = new StringBody("2");// android版
	// reqEntity.addPart("app", sb1);
	// reqEntity.addPart("mod", sb2);
	// reqEntity.addPart("act", sb3);
	// reqEntity.addPart("oauth_token", sb4);
	// reqEntity.addPart("oauth_token_secret", sb5);
	// reqEntity.addPart("content", sb6);
	// reqEntity.addPart("from", sb7);
	//
	// httpPost.setEntity(reqEntity);
	//
	// response = client.execute(httpPost);
	// if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
	// throw new RuntimeException("请求失败");
	// }
	// HttpEntity resEntity = response.getEntity();
	// return (resEntity == null) ? null : EntityUtils.toString(resEntity,
	// CHARSET);
	//
	// }

	private class GetUserphotosThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			MyNameValuePair NameValuePair1 = new MyNameValuePair("app", "api");
			MyNameValuePair NameValuePair2 = new MyNameValuePair("mod",
					"WeiboStatuses");
			MyNameValuePair NameValuePair3 = new MyNameValuePair("act",
					"weibo_photo");
			MyNameValuePair NameValuePair4 = new MyNameValuePair("oauth_token",
					ac.getOauth_token());
			MyNameValuePair NameValuePair5 = new MyNameValuePair(
					"oauth_token_secret", ac.getOauth_token_secret());
			MyNameValuePair NameValuePair6 = new MyNameValuePair("user_id", uid);
			String result = MHttpClient.get(ThinkSNSApplication.baseUrl,
					NameValuePair1, NameValuePair2, NameValuePair3,
					NameValuePair4, NameValuePair5, NameValuePair6);
			// ArrayList<UserPhoto> userphotos=new ArrayList<UserPhoto>();
			if (!result.equals("false")) {
				try {
					JSONArray json_result = new JSONArray(result);
					for (int i = 0; i < json_result.length(); i++) {
						JSONObject temp_obj = json_result.getJSONObject(i);
						String publish_time = temp_obj
								.optString("publish_time");
						String publish_content = temp_obj.optString("body");
						String photo_url = temp_obj.optString("savepath");
						UserPhoto userphoto = new UserPhoto();
						userphoto.setPhoto_content(publish_content);
						userphoto.setPhoto_time(publish_time);
						userphoto.setPhoto_url(photo_url);
						photos.add(userphoto);
						Log.e("userphoto-->", userphoto.toString());
					}

					// Log.e("tempuser", tempuser.toString());

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Message msg = mHandler.obtainMessage();
			msg.what = getuserphoto;
			// msg.obj=photos;
			mHandler.sendMessage(msg);
		}

	}

	private class GetUserInfoThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			MyNameValuePair NameValuePair1 = new MyNameValuePair("app", "api");
			MyNameValuePair NameValuePair2 = new MyNameValuePair("mod", "User");
			MyNameValuePair NameValuePair3 = new MyNameValuePair("act", "show");
			MyNameValuePair NameValuePair4 = new MyNameValuePair("oauth_token",
					ac.getOauth_token());
			MyNameValuePair NameValuePair5 = new MyNameValuePair(
					"oauth_token_secret", ac.getOauth_token_secret());
			MyNameValuePair NameValuePair6 = new MyNameValuePair("user_id", uid);
			String result = MHttpClient.get(ThinkSNSApplication.baseUrl,
					NameValuePair1, NameValuePair2, NameValuePair3,
					NameValuePair4, NameValuePair5, NameValuePair6);
			Log.e("re", result);
			try {
				JSONObject json_result = new JSONObject(result);
				String uname = json_result.optString("uname");
				String sex = json_result.optString("sex");
				String location = json_result.optString("location");
				String headicon = json_result.optString("avatar_small");
				String intro = json_result.optString("intro");
				String tag = json_result.optString("user_tag");
				int following = json_result.optJSONObject("count_info").optInt(
						"following_count");
				int follower = json_result.optJSONObject("count_info").optInt(
						"follower_count");
				int feedcount = json_result.optJSONObject("count_info").optInt(
						"feed_count");
				int checkcount = json_result.optJSONObject("count_info")
						.optInt("check_totalnum");
				User tempuser = new User();
				tempuser.setUname(uname);
				tempuser.setUid(Integer.parseInt(uid));
				tempuser.setSex(sex);
				tempuser.setLocation(location);
				tempuser.setIntro(intro);
				tempuser.setTag(tag);
				tempuser.setHeadicon(headicon);
				tempuser.setFollowing(following);
				tempuser.setFollower(follower);
				tempuser.setFeedcount(feedcount);
				tempuser.setCheckcount(checkcount);
				Message msg = mHandler.obtainMessage();
				msg.what = getuserinfo;
				msg.obj = tempuser;
				mHandler.sendMessage(msg);
				Log.e("tempuser", tempuser.toString());

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private class GetUserStatuesThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			MyNameValuePair NameValuePair1 = new MyNameValuePair("app", "api");
			MyNameValuePair NameValuePair2 = new MyNameValuePair("mod",
					"WeiboStatuses");
			MyNameValuePair NameValuePair3 = new MyNameValuePair("act",
					"user_timeline");
			MyNameValuePair NameValuePair4 = new MyNameValuePair("oauth_token",
					ac.getOauth_token());
			MyNameValuePair NameValuePair5 = new MyNameValuePair(
					"oauth_token_secret", ac.getOauth_token_secret());
			MyNameValuePair NameValuePair6 = new MyNameValuePair("user_id",
					String.valueOf(uid));
			MyNameValuePair NameValuePair7 = new MyNameValuePair("count",
					String.valueOf(num));
			MyNameValuePair NameValuePair8 = new MyNameValuePair("page",
					String.valueOf((page)));
			String result = MHttpClient.get(ThinkSNSApplication.baseUrl,
					NameValuePair1, NameValuePair2, NameValuePair3,
					NameValuePair4, NameValuePair5, NameValuePair6,
					NameValuePair7, NameValuePair8);

			page++;
			Message msg = new Message();
			msg.what = getweibo;
			msg.obj = result;
			mHandler.sendMessage(msg);
		}

	}

	private void findviews() {
		// TODO Auto-generated method stub
		username_top = (TextView) findViewById(R.id.selfhome_username);
		name = (TextView) findViewById(R.id.selfhome_name);
		location = (TextView) findViewById(R.id.selfhome_country);
		brief = (TextView) findViewById(R.id.selfhome_brief);
		user_tag = (TextView) findViewById(R.id.selfhome_tag);
		blog = (TextView) findViewById(R.id.selfhome_blog_num);
		fan = (TextView) findViewById(R.id.selfhome_fan_num);
		care = (TextView) findViewById(R.id.selfhome_care_num);
		photo = (ImageView) findViewById(R.id.selfhome_photo);
		gender = (ImageView) findViewById(R.id.selfhome_gender);

		back = (Button) findViewById(R.id.selfhome_back);
		// more = (Button) findViewById(R.id.selfhome_setting);
		myprofile = (TextView) findViewById(R.id.selfhome_profile);
		m_card = (LinearLayout) findViewById(R.id.self_mycard_linearlayout);
		m_pic = (LinearLayout) findViewById(R.id.selfhome_myphoto_linear);
		photo_num = (TextView) findViewById(R.id.selfhome_myphoto_num);
		m_colle = (LinearLayout) findViewById(R.id.selfhome_mylove_linear);
		m_good = (LinearLayout) findViewById(R.id.selfhome_good_linear);
		m_blog = (LinearLayout) findViewById(R.id.selfhome_blog);
		below_ll = (LinearLayout) findViewById(R.id.self_home_fun);
		m_care = (LinearLayout) findViewById(R.id.selfhome_care);
		m_fan = (LinearLayout) findViewById(R.id.selfhome_fan);

		mListView = (XListView) findViewById(R.id.self_home_blog_list);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
		// mstatuses=new ArrayList<Statuses>() ;
		below_ll.setVisibility(View.VISIBLE);
		mListView.setVisibility(View.GONE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.self_home, menu);
		return true;
	}

	// 界面跳转
	private void Navigate(Class<?> t) {
		Intent i = new Intent();
		i.setClass(SelfHome.this, t);
		startActivity(i);
		SelfHome.this.finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent(SelfHome.this, MainActivity.class);
			startActivity(i);
			SelfHome.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private ArrayList<Statuses> format() {

		ArrayList<Statuses> statuses = new ArrayList<Statuses>();
		JSONArray statuses_jsonarray;
		try {
			statuses_jsonarray = new JSONArray(weibostr);
			Log.e("json微博个数：", "" + statuses_jsonarray.length());
			for (int i = 0; i < statuses_jsonarray.length(); i++) {
				JSONObject status_jsonobject = statuses_jsonarray
						.getJSONObject(i);
				int feed_id = Integer.parseInt(status_jsonobject
						.optString("feed_id"));
				String publish_time = status_jsonobject
						.optString("publish_time");
				String from = status_jsonobject.optString("from");
				String feed_type = status_jsonobject.optString("type");
				int comment_count = Integer.parseInt(status_jsonobject
						.optString("comment_count", "0"));
				int repost_count = Integer.parseInt(status_jsonobject
						.optString("repost_count", "0"));
				int digg_count = Integer.parseInt(status_jsonobject.optString(
						"digg_count", "0"));
				String feed_content = status_jsonobject
						.optString("feed_content");
				int uid = Integer.parseInt(status_jsonobject.optString("uid"));
				String uname = status_jsonobject.optString("uname");
				String headicon = status_jsonobject.optString("avatar_small");
				Statuses status = new Statuses();
				status.setFeed_type(feed_type);
				if (feed_type.equals("postimage")) {
					// 上传图片微博
					String bigpic_url = status_jsonobject
							.getJSONArray("attach").getJSONObject(0)
							.optString("attach_url");
					String middle_pic_url = status_jsonobject
							.getJSONArray("attach").getJSONObject(0)
							.optString("attach_middle");
					String small_pic_url = status_jsonobject
							.getJSONArray("attach").getJSONObject(0)
							.optString("attach_small");
					status.setBig_postimage_url(bigpic_url);
					status.setMiddle_postimage_url(middle_pic_url);
					status.setSmall_postimage_url(small_pic_url);
				}
				Statuses source_status = new Statuses();
				if (status_jsonobject.optJSONObject("api_source") != null) {
					JSONObject source_status_jsonobject = status_jsonobject
							.optJSONObject("api_source");
					int source_feed_id = Integer
							.parseInt(source_status_jsonobject
									.optString("feed_id"));
					String source_publish_time = source_status_jsonobject
							.optString("publish_time");
					String source_from = source_status_jsonobject
							.optString("from");
					String source_feed_type = status_jsonobject
							.optString("type");
					int source_comment_count = Integer
							.parseInt(source_status_jsonobject.optString(
									"comment_count", "0"));
					int source_repost_count = Integer
							.parseInt(source_status_jsonobject.optString(
									"repost_count", "0"));
					int source_digg_count = Integer
							.parseInt(source_status_jsonobject.optString(
									"digg_count", "0"));
					String source_feed_content = source_status_jsonobject
							.optString("feed_content");
					int source_uid = Integer.parseInt(source_status_jsonobject
							.optString("uid", "-1"));
					String source_uname = source_status_jsonobject
							.optString("uname");
					String source_headicon = source_status_jsonobject
							.optString("avatar_small");
					if (source_feed_type.equals("postimage")) {
						// 上传图片微博
						String bigpic_url = source_status_jsonobject
								.getJSONArray("attach").getJSONObject(0)
								.optString("attach_url");
						String middle_pic_url = source_status_jsonobject
								.getJSONArray("attach").getJSONObject(0)
								.optString("attach_middle");
						String small_pic_url = source_status_jsonobject
								.getJSONArray("attach").getJSONObject(0)
								.optString("attach_small");
						source_status.setBig_postimage_url(bigpic_url);
						source_status.setMiddle_postimage_url(middle_pic_url);
						source_status.setSmall_postimage_url(small_pic_url);
					}
					User source_user = new User();
					source_user.setUid(source_uid);
					source_user.setUname(source_uname);
					source_user.setHeadicon(source_headicon);
					source_status.setFeed_id(source_feed_id);
					source_status.setPublish_time(source_publish_time);
					source_status.setFrom(source_from);
					source_status.setComment_count(source_comment_count);
					source_status.setRepost_count(source_repost_count);
					source_status.setDigg_count(source_digg_count);
					source_status.setFeed_content(source_feed_content);
					source_status.setFeed_type(source_feed_type);
					source_status.setUser(source_user);
				}
				User user = new User();
				user.setUid(uid);
				user.setUname(uname);
				user.setHeadicon(headicon);

				status.setFeed_id(feed_id);
				status.setPublish_time(publish_time);
				status.setFrom(from);
				status.setComment_count(comment_count);
				status.setRepost_count(repost_count);
				status.setDigg_count(digg_count);
				status.setFeed_content(feed_content);
				status.setUser(user);
				status.setSource_status(source_status);
				statuses.add(status);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.e("huode微博个数：", "" + statuses.size());
		return statuses;

	}

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		mListView.setRefreshTime(sdf.format(new Date()));
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		state = 0;
		page = 1;
		mstatuses.clear();

		GetUserStatuesThread getstatuesthread = new GetUserStatuesThread();
		getstatuesthread.start();
		onLoad();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		state = LoadMore;
		GetUserStatuesThread getstatuesthread = new GetUserStatuesThread();
		getstatuesthread.start();
		onLoad();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		dbm.closeDB();
	}

	public boolean hasSdcard() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

}
