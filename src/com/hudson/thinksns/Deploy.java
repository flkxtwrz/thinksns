package com.hudson.thinksns;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
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
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.hudson.thinksns.adapter.EmotionViewPagerAdapter;
import com.hudson.thinksns.application.ThinkSNSApplication;
import com.hudson.thinksns.dbutils.DBManager;
import com.hudson.thinksns.imageloader.ImageLoader;
import com.hudson.thinksns.model.Account;
import com.hudson.thinksns.model.Statuses;
import com.hudson.thinksns.model.User;
import com.hudson.thinksns.netuti.MHttpClient;
import com.hudson.thinksns.netuti.MyNameValuePair;
import com.hudson.thinksns.statusparse.EmotionMap;
/**
 *@file  Deploy.java
 *@author 石仲才
 *@description 发布、转发、评论、意见反馈的 页面。主要用于发布微博状态，评论状态等
 */
public class Deploy extends Activity {
	private DBManager dbm;
	private Account ac;
	/*
	 * 转发微博时显示的微博页面
	 */
	RelativeLayout repost_layout;
	ImageView head_iv;
	TextView username;
	TextView content;
	private int b1, b2, b3, b4, b5 = 0;
	private ImageView Add = null;
	private ImageView emotionbutton;
	private ImageView atbutton;
	private ImageView camerabutton, preview;
	private boolean or = true;
	private EditText ed = null;
	private List<View> views;
	private View menuView;
	private GridView menuGrid;
	private RelativeLayout re;
	private Button send, cancel;
	int[] emotion_image_array = EmotionMap.imgemotion;
	String[] menu_name_array = EmotionMap.wenziemotion;
	// private View mViewPager;
	// 底部小点图片
	private ImageView[] dots;
	private AlertDialog dialog;
	// 记录当前选中位置
	private int currentIndex;
	private Context mContext;
	private String type;
	private int crop = 300;
	private File tempFile;
	private File sdcardTempFile;
	private static final String CHARSET = HTTP.UTF_8;
	private Handler mHandler;
	private LayoutInflater inflater;
	private ArrayList<User> friends = new ArrayList<User>();
	private Intent mIntent;
	private String way;// 标志是发微博还是转发或者评论。
	private Statuses status;
	private ImageLoader imageLoader;
	private final int createcomment = 0x11;
	private final int repoststatus = 0x22;
	private final int deploy = 0x33;
	private final int send_fail = 0x44;
	private TextView title = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_deploy);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.deploy_top_menu);
		// 获取当前登录账户
		dbm = new DBManager(this);
		ac = dbm.getAccountonline();
		inflater = LayoutInflater.from(this);
		mContext = this;

		mIntent = getIntent();
		title = (TextView) findViewById(R.id.faweibo);
		if (getIntent().getStringExtra("title") != null) {
			title.setText(getIntent().getStringExtra("title"));
		}
		findViews();
		status = (Statuses) mIntent.getSerializableExtra("weibo");
		if (status == null) {
			way = "deploy";
			camerabutton.setVisibility(View.VISIBLE);
			if (mIntent.getIntExtra("advice", 0) == 1) {
				ed.setText("#Android意见反馈# ");
			}
		} else {
			// status = (Statuses) mIntent.getSerializableExtra("weibo");
			camerabutton.setVisibility(View.GONE);
			if (mIntent.getStringExtra("type").equals("repost")) {
				way = "repost";
				imageLoader = new ImageLoader(mContext);
			}
			if (mIntent.getStringExtra("type").equals("comment")) {
				way = "comment";
			}
		}
		initViews();
		setListeners();
		initFile();
		initHandler();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		dbm.closeDB();
		super.onDestroy();
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

	// 使用系统当前日期加以调整作为照片的名称
	private String getPhotoFileName(String poststr) {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("'" + poststr
				+ "_IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}

	private void initHandler() {
		// TODO Auto-generated method stub
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case createcomment:
					String s = msg.obj.toString();
					if (s.equals("1")) {
						/*
						 * 评论成功
						 */
						Toast.makeText(mContext, "评论成功", Toast.LENGTH_SHORT)
								.show();
						;
						// GetRepostStatusThread
					}
					break;
				case repoststatus:
					String ss = msg.obj.toString();
					Log.e("zf", ss);
					if (!ss.equals("0")) {
						/*
						 * 转发成功
						 */
						Toast.makeText(mContext, "转发成功", Toast.LENGTH_SHORT)
								.show();
						;
					}
					break;
				case deploy:
					//if (ed.getText() != null) {
						Toast.makeText(mContext, "发布成功", Toast.LENGTH_SHORT)
								.show();
					//} else {
						
					//}
					;
					break;
				case send_fail:
					Toast.makeText(mContext, "发布失败", Toast.LENGTH_SHORT).show();
					;
					break;
				default:
					break;
				}

			}

		};
	}

	private void setListeners() {
		// TODO Auto-generated method stub
		re.setFocusable(true);
		re.setFocusableInTouchMode(true);
		ed.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					re.removeAllViews();
				}
			}
		});
		emotionbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if (im.isActive()) {

					im.hideSoftInputFromWindow(getCurrentFocus()
							.getApplicationWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
				}
				re.requestFocus();
				if (or == true || b5 == 1 || b3 == 1) {
					/*
					 * 初始化表情
					 */
					views.clear();
					View mViewPager = inflater.inflate(
							R.layout.emotion_viewpager, null);
					ViewPager vp = (ViewPager) mViewPager
							.findViewById(R.id.viewpager);
					for (int i = 0; i < 6; i++) {
						ArrayList<Integer> temp_image_array = new ArrayList<Integer>();
						ArrayList<String> temp_name_array = new ArrayList<String>();
						for (int k = 20 * i; k < 20 * (i + 1); k++) {
							if (k >= emotion_image_array.length)
								break;
							temp_image_array.add(emotion_image_array[k]);
							temp_name_array.add(menu_name_array[k]);
						}
						Log.e("", temp_name_array.toString());
						menuView = View.inflate(mContext,
								R.layout.emotion_gridview, null);
						menuGrid = (GridView) menuView
								.findViewById(R.id.emotion_gridview);
						menuGrid.setAdapter(getMenuAdapter(temp_name_array,
								temp_image_array));
						menuGrid.setOnItemClickListener(new EmotionItemClickListener(
								vp));
						views.add(menuView);
					}

					EmotionViewPagerAdapter vpAdapter = new EmotionViewPagerAdapter(
							views, mContext);

					initDots(mViewPager);

					vp.setAdapter(vpAdapter);
					// 绑定回调
					vp.setOnPageChangeListener(emotionPageChangeListener);
					re.removeAllViews();
					re.addView(mViewPager);
					or = false;
					b4 = 1;
					b5 = 0;
					b3 = 0;
				} else {
					re.removeAllViews();
					or = true;
				}
			}
		});
		atbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				// TODO Auto-generated method stub

				InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if (im.isActive()) {

					im.hideSoftInputFromWindow(getCurrentFocus()
							.getApplicationWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
				}
				re.requestFocus();
				if (or == true || b4 == 1 || b5 == 1) {
					friends.clear();
					GetFriendsThread getFriends = new GetFriendsThread();
					getFriends.start();
					try {
						getFriends.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					/*
					 * 初始化表情
					 */
					views.clear();
					View mViewPager = inflater.inflate(
							R.layout.emotion_viewpager, null);
					ViewPager vp = (ViewPager) mViewPager
							.findViewById(R.id.viewpager);
					ImageLoader imageLoader = new ImageLoader(mContext);
					for (int i = 0; i < friends.size(); i++) {

						View convertView = LayoutInflater.from(mContext)
								.inflate(R.layout.emotion_item, null);
						TextView text = (TextView) convertView
								.findViewById(R.id.emotion_text);
						text.setText(friends.get(i).getUname());
						Log.e("xxxx", friends.get(i).getUname() + " ");
						ImageView img = (ImageView) convertView
								.findViewById(R.id.emotion_image);
						imageLoader.DisplayImage(friends.get(i).getHeadicon(),
								img);
						final User u = friends.get(i);
						convertView.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub

								ed.append("@" + u.getUname() + " ");
							}
						});
						views.add(convertView);
					}

					EmotionViewPagerAdapter vpAdapter = new EmotionViewPagerAdapter(
							views, mContext);

					initDots(mViewPager);

					vpAdapter = new EmotionViewPagerAdapter(views, mContext);
					vp.setAdapter(vpAdapter);
					// vp.setOnClickListener(l)
					// 绑定回调
					vp.setOnPageChangeListener(emotionPageChangeListener);
					re.removeAllViews();
					re.addView(mViewPager);
					or = false;
					b4 = 0;
					b3 = 1;
					b5 = 0;
				} else {
					re.removeAllViews();
					or = true;
				}

			}
		});
		camerabutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (dialog == null) {
					dialog = new AlertDialog.Builder(mContext).setItems(
							new String[] { "相机", "相册" },
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									if (which == 0) {
										// 选择拍照
										type = "pz";
										Intent cameraintent = new Intent(
												MediaStore.ACTION_IMAGE_CAPTURE);
										// 指定调用相机拍照后照片的储存路径
										cameraintent.putExtra(
												MediaStore.EXTRA_OUTPUT,
												Uri.fromFile(tempFile));
										startActivityForResult(cameraintent,
												101);

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
										intent.putExtra("aspectX", 1);// 裁剪框比例
										intent.putExtra("aspectY", 1);
										intent.putExtra("outputX", crop);// 输出图片大小
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
		});
		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent o = new Intent();
				if (way.equals("deploy")) {
					//					
					if (ed.getText() != null) {
						new SendPicThread(type).start();
						o.setClass(Deploy.this, MainActivity.class);
						startActivity(o);
						Deploy.this.finish();
					}else{
						Toast.makeText(mContext, "请输入内容", Toast.LENGTH_SHORT)
						.show();
					}
				}
				if (way.equals("repost")) {
					//
					new RepostStatusThread().start();
					o.setClass(Deploy.this, MainActivity.class);
					startActivity(o);
					Deploy.this.finish();
				}
				if (way.equals("comment")) {
					//
					new CreatCommentThread().start();
					// Intent newcomment = getIntent();
					o.putExtra("m_position",
							getIntent().getIntExtra("m_position", 0));
					o.putExtra("weibo", status);
					o.putExtra("type", "comment");
					// newcomment
					//
					// try {
					// o.putExtra("weibo", o.getIntent("weibo"));
					// o.putExtra("m_position", o.getIntent("m_position"));
					// } catch (URISyntaxException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }
					//
					o.setClass(Deploy.this, DoComment.class);
					startActivity(o);
					Deploy.this.finish();
				}
				send.setClickable(false);

			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent();
				mIntent.getIntExtra("m_position", 0);
				i.putExtra("m_position", mIntent.getIntExtra("m_position", 0));
				i.setClass(Deploy.this, MainActivity.class);
				startActivity(i);
				Deploy.this.finish();
			}
		});

		Add.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if (im.isActive()) {
					// or = true;
					im.hideSoftInputFromWindow(getCurrentFocus()
							.getApplicationWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
				}
				re.requestFocus();
				if (or == true || b4 == 1 || b3 == 1) {

					RelativeLayout layout = (RelativeLayout) inflater.inflate(
							R.layout.menu_item, null).findViewById(R.id.rl);
					re.removeAllViews();
					re.addView(layout);
					or = false;
					b5 = 1;
					b4 = 0;
					b3 = 0;
				} else {
					re.removeAllViews();
					or = true;
				}
			}
		});
	}

	private void initViews() {
		// TODO Auto-generated method stub
		views = new ArrayList<View>();
		Log.e("way---", way);
		if (way.equals("repost")) {
			Log.e("status---", status.toString());
			repost_layout.setVisibility(View.VISIBLE);
			imageLoader.DisplayImage(status.getUser().getHeadicon(), head_iv);
			username.setText(status.getUser().getUname());
			content.setText(status.getFeed_content());

		}

	}

	private void findViews() {
		// TODO Auto-generated method stub

		repost_layout = (RelativeLayout) findViewById(R.id.resend_relayout);
		head_iv = (ImageView) findViewById(R.id.resend_pic);
		username = (TextView) findViewById(R.id.resend_username);
		content = (TextView) findViewById(R.id.resend_contents);

		Add = (ImageView) findViewById(R.id.add);
		emotionbutton = (ImageView) findViewById(R.id.emotionbut);
		atbutton = (ImageView) findViewById(R.id.btn_at);
		camerabutton = (ImageView) findViewById(R.id.btn_camera);
		preview = (ImageView) findViewById(R.id.preview);
		ed = (EditText) findViewById(R.id.blog_contents);
		send = (Button) findViewById(R.id.send);
		re = (RelativeLayout) findViewById(R.id.item_choose_layout);
		cancel = (Button) findViewById(R.id.cancel);
	}

	private SimpleAdapter getMenuAdapter(ArrayList<String> menuNameArray,
			ArrayList<Integer> imageResourceArray) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < imageResourceArray.size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemImage", imageResourceArray.get(i));
			map.put("itemText", menuNameArray.get(i));
			data.add(map);
		}
		SimpleAdapter simperAdapter = new SimpleAdapter(this, data,
				R.layout.emotion_item,
				new String[] { "itemImage", "itemText" }, new int[] {
						R.id.emotion_image, R.id.emotion_text });
		return simperAdapter;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.deploy, menu);
		return true;
	}

	private OnPageChangeListener emotionPageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			Log.e("xianz", arg0 + " ");
			setCurrentDot(arg0);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}
	};

	private class EmotionItemClickListener implements OnItemClickListener {
		private ViewPager viewPager;

		public EmotionItemClickListener(ViewPager viewPager) {

			this.viewPager = viewPager;
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View item, int position,
				long arg3) {
			// TODO Auto-generated method stub

			int itmeid = emotion_image_array[20 * viewPager.getCurrentItem()
					+ position];// 表情资源的id

			String emotion_text = EmotionMap.getEmMapI().get(itmeid);// 表情对应得文字
			Log.e("text-->" + viewPager.getCurrentItem(), emotion_text);
			SpannableString spannableString = new SpannableString(emotion_text);
			Drawable drawable2 = Deploy.this.getResources().getDrawable(itmeid);
			drawable2.setBounds(0, 0, drawable2.getIntrinsicWidth() * 2,
					drawable2.getIntrinsicHeight() * 2);
			ImageSpan imageSpan = new ImageSpan(drawable2,
					ImageSpan.ALIGN_BASELINE);
			spannableString.setSpan(imageSpan, 0, emotion_text.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			ed.append(spannableString);

		}
	}

	// private class AtOnClickListener implements OnItemClickListener{
	// private ViewPager viewPager;
	//
	//
	// public AtOnClickListener(ViewPager viewPager) {
	//
	// this.viewPager = viewPager;
	// }
	//
	//
	// @Override
	// public void onItemClick(AdapterView<?> arg0, View item, int position,
	// long arg3) {
	// // TODO Auto-generated method stub
	// User u=friends.get(viewPager.getCurrentItem());
	// ed.append("@"+u.getUname()+" ");
	//
	//
	// }
	// }
	private void initDots(View v) {
		LinearLayout ll = (LinearLayout) v.findViewById(R.id.ll);
		ll.removeAllViews();
		dots = new ImageView[views.size()];
		Log.e("xxxx", views.size() + " ");
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);

		// 循环取得小点图片
		for (int i = 0; i < views.size(); i++) {

			dots[i] = new ImageView(mContext);
			dots[i].setImageResource(R.drawable.dot_selector);
			dots[i].setClickable(true);
			dots[i].setPadding(18, 0, 0, 0);
			dots[i].setLayoutParams(layoutParams);
			dots[i].setEnabled(true);// 都设为灰色
			ll.addView(dots[i]);

		}

		currentIndex = 0;
		dots[currentIndex].setEnabled(false);// 设置为白色，即选中状态
	}

	private void setCurrentDot(int position) {
		if (position < 0 || position > views.size() - 1
				|| currentIndex == position) {
			return;
		}

		dots[position].setEnabled(false);
		dots[currentIndex].setEnabled(true);

		currentIndex = position;
	}

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
			// 选择拍照
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
		// crop为true是设置在开启的intent中设置显示的view可以剪裁
		intent.putExtra("crop", "true");

		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);

		// outputX,outputY 是剪裁图片的宽高
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);
		intent.putExtra("noFaceDetection", true);
		startActivityForResult(intent, 102);

	}

	// 将进行剪裁后的图片传递到下一个界面上
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

	private class GetFriendsThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			MyNameValuePair NameValuePair1 = new MyNameValuePair("app", "api");
			MyNameValuePair NameValuePair2 = new MyNameValuePair("mod", "User");
			MyNameValuePair NameValuePair3 = new MyNameValuePair("act",
					"user_following");
			MyNameValuePair NameValuePair4 = new MyNameValuePair("oauth_token",
					ac.getOauth_token());
			MyNameValuePair NameValuePair5 = new MyNameValuePair(
					"oauth_token_secret", ac.getOauth_token_secret());

			String result = MHttpClient.get(ThinkSNSApplication.baseUrl,
					NameValuePair1, NameValuePair2, NameValuePair3,
					NameValuePair4, NameValuePair5);

			try {
				JSONArray jsonArray_result = new JSONArray(result);
				for (int i = 0; i < jsonArray_result.length(); i++) {
					JSONObject json_result = jsonArray_result.getJSONObject(i);
					String uname = json_result.optString("uname");
					String headicon = json_result.optString("avatar_small");
					User user = new User();
					user.setUname(uname);
					user.setHeadicon(headicon);
					friends.add(user);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.e("re", result);
		}

	}

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
				response = postStatus(ThinkSNSApplication.baseUrl, ed.getText()
						.toString(), file);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.e("re", response.toString());
			Message msg = Message.obtain();
			msg.what = deploy;
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
		sb7 = new StringBody("2");// android版
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
			throw new RuntimeException("请求失败");
		}
		HttpEntity resEntity = response.getEntity();
		return (resEntity == null) ? null : EntityUtils.toString(resEntity,
				CHARSET);

	}

	/*
	 * 转发微博
	 */
	private class RepostStatusThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			MyNameValuePair NameValuePair1 = new MyNameValuePair("app", "api");
			MyNameValuePair NameValuePair2 = new MyNameValuePair("mod",
					"WeiboStatuses");
			MyNameValuePair NameValuePair3 = new MyNameValuePair("act",
					"repost");
			MyNameValuePair NameValuePair4 = new MyNameValuePair("oauth_token",
					ac.getOauth_token());
			MyNameValuePair NameValuePair5 = new MyNameValuePair(
					"oauth_token_secret", ac.getOauth_token_secret());
			MyNameValuePair NameValuePair6 = new MyNameValuePair("id",
					String.valueOf(status.getFeed_id()));
			MyNameValuePair NameValuePair7 = new MyNameValuePair("content", ed
					.getEditableText().toString());

			String result = MHttpClient.get(ThinkSNSApplication.baseUrl,
					NameValuePair1, NameValuePair2, NameValuePair3,
					NameValuePair4, NameValuePair5, NameValuePair6,
					NameValuePair7);

			Message msg = new Message();
			msg.what = repoststatus;
			msg.obj = result;
			mHandler.sendMessage(msg);
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) { // TODO
															// Auto-generated
															// method stub

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent();
			mIntent.getIntExtra("m_position", 0);
			i.putExtra("m_position", mIntent.getIntExtra("m_position", 0));
			i.setClass(Deploy.this, MainActivity.class);
			startActivity(i);
			Deploy.this.finish();
		}
		return true;
	}

	/*
	 * 进行评论
	 */
	private class CreatCommentThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			MyNameValuePair NameValuePair1 = new MyNameValuePair("app", "api");
			MyNameValuePair NameValuePair2 = new MyNameValuePair("mod",
					"WeiboStatuses");
			MyNameValuePair NameValuePair3 = new MyNameValuePair("act",
					"comment");
			MyNameValuePair NameValuePair4 = new MyNameValuePair("oauth_token",
					ac.getOauth_token());
			MyNameValuePair NameValuePair5 = new MyNameValuePair(
					"oauth_token_secret", ac.getOauth_token_secret());
			MyNameValuePair NameValuePair6 = new MyNameValuePair("row_id",
					String.valueOf(status.getFeed_id()));
			MyNameValuePair NameValuePair7 = new MyNameValuePair("content", ed
					.getEditableText().toString());

			String result = MHttpClient.get(ThinkSNSApplication.baseUrl,
					NameValuePair1, NameValuePair2, NameValuePair3,
					NameValuePair4, NameValuePair5, NameValuePair6,
					NameValuePair7);

			Message msg = new Message();
			msg.what = createcomment;
			msg.obj = result;
			mHandler.sendMessage(msg);
		}

	}
}
