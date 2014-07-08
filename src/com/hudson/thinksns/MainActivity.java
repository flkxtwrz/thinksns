package com.hudson.thinksns;

import java.util.ArrayList;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hudson.thinksns.InfoList.InfoList_Act;
import com.hudson.thinksns.application.ThinkSNSApplication;
import com.hudson.thinksns.backservice.GetNotifyService;
import com.hudson.thinksns.channel.ListAllChannel;
import com.hudson.thinksns.chating.ChatActivity;
import com.hudson.thinksns.dbutils.DBManager;
import com.hudson.thinksns.fragment.CommentToMeFragment;
import com.hudson.thinksns.fragment.WeiboStatuesFragment;
import com.hudson.thinksns.imageloader.ImageLoader;
import com.hudson.thinksns.model.Account;
import com.hudson.thinksns.netuti.MHttpClient;
import com.hudson.thinksns.netuti.MyNameValuePair;
import com.hudson.thinksns.sign.SignUp;
import com.hudson.thinksns.userinfo.SelfHome;
import com.hudson.thinksns.view.TabHorizontalScrollView;
/**
 * @file MainActivity.java
 * @author 贾焱超
 * @description 应用的主界面，包括左侧功能菜单和右侧设置菜单，以及中心内容的“最新微博”“好友微博”“我的微博”“收藏微博”“最新评论”。
 */
public class MainActivity extends FragmentActivity {
	private static final String STATE_MENUDRAWER = "net.simonvt.menudrawer.samples.WindowSample.menuDrawer";
	private static final String STATE_ACTIVE_VIEW_ID = "net.simonvt.menudrawer.samples.WindowSample.activeViewId";
	private MenuDrawer mLeftMenuDrawer, mRightMenuDrawer;
	private long mExitTime;
	private ThinkSNSApplication mApplication;

	/*
	 * 右侧菜单
	 */
	private ArrayList<Account> userlist;
	private RelativeLayout account, inform, safe, general, advice,
			about = null;
	private Button exit = null;
	/*
	 * 左侧菜单
	 */
	private RelativeLayout blog, myhome, chat, find, friend, channel, webar,
			sign, apprecom = null;
	private ImageView blog_pic, myhome_pic, chat_pic, find_pic, friend_pic,
			channel_pic, webar_pic, sign_pic, apprecom_pic = null;
	private Button msg_inform = null;

	private Button top_title;// 顶部titleButton
	private ImageView setting, write;// 设置，发布按钮
	protected static final String TAG = "MainActivity";
	private ViewPager vp;
	private ImageView iv_nav_right, iv_nav_left;// iv_indicator,
	private RadioGroup rg;
	private static String[] tabTitle = { "最新微博", "最新评论", "好友微博",  "我的微博" ,"收藏微博"}; // 标题
	private int currentNavItemWidth, currentIndicatorLeft = 0;

	private int cardinality; // 将屏幕按宽分成的份数
	private RelativeLayout rl;
	private int mActiveViewId;
	private DBManager dbm;
	private int m_position = 0;
	private final int getweibo = 0x11;
	private Account ac;
	private Context mContext;
	private Intent serviceIntent;
	private TabHorizontalScrollView tsv;

	//@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			mActiveViewId = savedInstanceState.getInt(STATE_ACTIVE_VIEW_ID);
		}
		Intent i = getIntent();

		if (i != null) {
			m_position = i.getIntExtra("m_position", 0);
		}
		Log.e("hhhhhh----", m_position + " gdfh");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		WindowManager manage = getWindowManager();
		Display display = manage.getDefaultDisplay();
		int iWidth = display.getWidth();
		
		/*
		 * 右侧边栏
		 */
		mRightMenuDrawer = MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_WINDOW,
				Position.RIGHT);
		mRightMenuDrawer.setContentView(R.layout.main);
		mRightMenuDrawer.setMenuView(R.layout.homeblog_setting);
		mRightMenuDrawer.setTouchBezelSize(50);
		// mRightMenuDrawer.setTouchBezelSize(iWidth);
		/*
		 * 左侧边栏
		 */
		mLeftMenuDrawer = MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_WINDOW,
				Position.LEFT);
		mLeftMenuDrawer.setContentView(R.layout.main);
		mLeftMenuDrawer.setMenuView(R.layout.left_menu);
		//mLeftMenuDrawer.setMenuSize(340);
		//MenuDrawer.
		mApplication = (ThinkSNSApplication) getApplication();
		mApplication.addActivity(this);

		mContext = this;
		dbm = new DBManager(this);
		new ImageLoader(mContext);
		Intent intent = getIntent();
		ac = (Account) intent.getSerializableExtra("ac");
		if (ac == null) {
			ac = dbm.getAccountonline();
		}
		ac.setState(1);
		findView();
		GetUserInfoThread userinfothread = new GetUserInfoThread();
		userinfothread.start();
		try {
			userinfothread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Account ac_on = dbm.getAccountonline();
		if (ac_on.getUid() != 0 && ac_on.getUid() != ac.getUid()) {
			ac_on.setState(0);
			dbm.update(ac_on);
		}
		dbm.add(ac);// 将登录的用户信息加入到数据库
		initHandler();

		serviceIntent = new Intent(this, GetNotifyService.class);
		startService(serviceIntent);

		initView();

		setListener();
	}

	// public void refresh() throws InterruptedException {
	// ac = dbm.getAccountonline();
	// Log.e("re_ac", ac.toString());
	// top_title.setText(ac.getUname());
	//
	// vp.getAdapter().notifyDataSetChanged();
	// }

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
			MyNameValuePair NameValuePair6 = new MyNameValuePair("user_id",
					String.valueOf(ac.getUid()));

			String result = MHttpClient.get(ThinkSNSApplication.baseUrl,
					NameValuePair1, NameValuePair2, NameValuePair3,
					NameValuePair4, NameValuePair5, NameValuePair6);
			Log.e("re", result);
			try {
				JSONObject json_result = new JSONObject(result);
				String uname = json_result.optString("uname");
				String headicon = json_result.optString("avatar_small");
				ac.setUname(uname);
				ac.setHeadicon(headicon);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void initHandler() {
		new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case getweibo:

					break;
				}
			}

		};
	}

	OnClickListener ocl = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.homeblog_setting_accoutmanage:
				// TODO click things
				Navigate(AccountManageActivity.class);
				break;
			case R.id.homeblog_setting_inform:
				// TODO click things
				Navigate(InformSetting.class);
				break;
			case R.id.homeblog_setting_safe:
				// TODO click things
				Navigate(PrivateSafeSetting.class);
				break;
			case R.id.homeblog_setting_general:
				// TODO click things
				Navigate(GeneralSetting.class);
				break;
			case R.id.homeblog_setting_advice:
				// TODO click things
				// 跳到发送微博页面发表意见反馈
				Intent advice = new Intent(MainActivity.this,Deploy.class);
				advice.putExtra("advice", 1);
				advice.putExtra("title", "意见反馈");
				startActivity(advice);
				MainActivity.this.finish();
				break;
			case R.id.homeblog_setting_about:
				// TODO click things
				Navigate(Aboutsoft.class);
				break;
			case R.id.homeblog_setting_exit:
				// TODO click things
				// 退出当前账号，返回用户登陆页面；
				Intent mlogin = new Intent();
				//Account u = userlist.get(position);
				mlogin.putExtra("relogin", true);				
				mlogin.setClass(MainActivity.this, LoginActivity.class);
				startActivity(mlogin);
				MainActivity.this.finish();
				break;
			/*
			 * 左侧监听
			 */

//			case R.id.selfmenu_blog:
//			case R.id.selfmenu_blog_pic:
//				// TODO click things
//				// Navigate( AFDA.class);
//				break;
			case R.id.selfmenu_mainpage:
			case R.id.selfmenu_mainpage_pic:
				// TODO click things
				Intent i = new Intent();
				i.setClass(MainActivity.this, SelfHome.class);
				i.putExtra("uid", String.valueOf(ac.getUid()));
				startActivity(i);
				MainActivity.this.finish();
				break;
			case R.id.selfmenu_chat:
			case R.id.selfmenu_chat_pic:
				// TODO click things
				Navigate(ChatActivity.class);

				break;
			case R.id.selfmenu_find:
			case R.id.selfmenu_find_pic:
				// TODO click things
				Navigate(Finding.class);
				break;
			case R.id.selfmenu_friend:
			case R.id.selfmenu_friend_pic:
				// TODO click things
				Navigate(Fans.class);
				break;
			case R.id.selfmenu_channel:
			case R.id.selfmenu_channel_pic:
				// TODO click things
				Navigate(ListAllChannel.class);
				
				break;
				///签到~！！！！！！！！！！
			case R.id.menu_microbar:
			case R.id.menu_microbar_pic:
				// TODO click things
				Navigate(SignUp.class);
				//Navigate(FriendHome.class);
				break;
//			case R.id.menu_sign:
//			case R.id.menu_sign_pic:
//				// TODO click things
//
//				break;
//			case R.id.menu_applicationmore:
//			case R.id.menu_application_more_pic:
//				// TODO click things
//
//				// 测试好友主页的跳转
//				
//
//				break;
			case R.id.selfmenu_msg_inform:
				// TODO click things
				Intent notify = new Intent(MainActivity.this,InfoList_Act.class);
				notify.putExtra("uid", String.valueOf(ac.getUid()));
				startActivity(notify);
				MainActivity.this.finish();
				break;

				
			case R.id.title:
				Intent i1 = new Intent();
				i1.setClass(MainActivity.this, SelfHome.class);
				i1.putExtra("uid", String.valueOf(ac.getUid()));
				startActivity(i1);
				MainActivity.this.finish();
				break;
			}
		}
	};

	// 界面跳转
	private void Navigate(Class<?> t) {
		Intent i = new Intent();
		i.setClass(MainActivity.this, t);
		startActivity(i);
		MainActivity.this.finish();
	}

	private void setListener() {
		// 右侧绑定监听
		account.setOnClickListener(ocl);
		inform.setOnClickListener(ocl);
		safe.setOnClickListener(ocl);
		general.setOnClickListener(ocl);
		advice.setOnClickListener(ocl);
		about.setOnClickListener(ocl);
		exit.setOnClickListener(ocl);
		// 左侧绑定监听
		//blog.setOnClickListener(ocl);
		myhome.setOnClickListener(ocl);
		chat.setOnClickListener(ocl);
		find.setOnClickListener(ocl);
		friend.setOnClickListener(ocl);
		channel.setOnClickListener(ocl);
		webar.setOnClickListener(ocl);
		//sign.setOnClickListener(ocl);
		//apprecom.setOnClickListener(ocl);
		//blog_pic.setOnClickListener(ocl);
		myhome_pic.setOnClickListener(ocl);
		chat_pic.setOnClickListener(ocl);
		find_pic.setOnClickListener(ocl);
		friend_pic.setOnClickListener(ocl);
		channel_pic.setOnClickListener(ocl);
		webar_pic.setOnClickListener(ocl);
		//sign_pic.setOnClickListener(ocl);
		//apprecom_pic.setOnClickListener(ocl);
		msg_inform.setOnClickListener(ocl);
		top_title.setOnClickListener(ocl);
		setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (mLeftMenuDrawer.isShown()) {
					mLeftMenuDrawer.closeMenu();
				} else {
					mLeftMenuDrawer.toggleMenu();
				}

			}
		});
		write.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 跳转到发布微博页面
				Intent i = new Intent(mContext, Deploy.class);
				mContext.startActivity(i);
			}
		});

		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				Log.e(TAG, "checkedId:" + checkedId);
				if (checkedId >= tabTitle.length - 4) {
					iv_nav_right.setVisibility(View.GONE);
				}
				// ((RadioButton)
				rg.check(checkedId);
				TranslateAnimation animation = new TranslateAnimation(
						currentIndicatorLeft, rg.getChildAt(checkedId)
								.getLeft(), 0f, 0f);
				animation.setInterpolator(new LinearInterpolator());
				animation.setDuration(300);
				animation.setFillAfter(true);

				// iv_indicator.startAnimation(animation);// 执行位移动画

				currentIndicatorLeft = rg.getChildAt(checkedId).getLeft();// 记录当前
																			// 下标的距最左侧的
																			// 距离

				vp.setCurrentItem(checkedId);// ViewPager 跟随一起 切换
				int x = (checkedId > 0 ? ((RadioButton) rg
						.getChildAt(checkedId)).getLeft() : 0)
						- rg.getChildAt(1).getLeft();
				tsv.smoothScrollTo(x, 0);

			}
		});

		vp.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {

				Log.i(TAG, "position: " + position);
				// if(position>=tabTitle.length-4 ){
				// iv_nav_right.setVisibility(View.GONE);
				// }

				if (rg != null) {
					m_position = position;
					rg.getChildAt(position).performClick();

				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	private void findView() {
		top_title = (Button) findViewById(R.id.title);
		setting = (ImageView) findViewById(R.id.settings);
		write = (ImageView) findViewById(R.id.write);
		rl = (RelativeLayout) findViewById(R.id.rl_nav);

		rg = (RadioGroup) findViewById(R.id.rg_nav);
		tsv = (TabHorizontalScrollView) findViewById(R.id.sv_nav);
		// iv_indicator = (ImageView) findViewById(R.id.iv_nav_indicator);
		iv_nav_left = (ImageView) findViewById(R.id.iv_nav_left);
		iv_nav_right = (ImageView) findViewById(R.id.iv_nav_right);
		vp = (ViewPager) findViewById(R.id.vp);

		/*
		 * 右侧
		 */
		account = (RelativeLayout) findViewById(R.id.homeblog_setting_accoutmanage);
		inform = (RelativeLayout) findViewById(R.id.homeblog_setting_inform);
		safe = (RelativeLayout) findViewById(R.id.homeblog_setting_safe);
		general = (RelativeLayout) findViewById(R.id.homeblog_setting_general);
		advice = (RelativeLayout) findViewById(R.id.homeblog_setting_advice);
		about = (RelativeLayout) findViewById(R.id.homeblog_setting_about);
		exit = (Button) findViewById(R.id.homeblog_setting_exit);
		/*
		 * 左侧
		 */
	//	blog = (RelativeLayout) findViewById(R.id.selfmenu_blog);
		myhome = (RelativeLayout) findViewById(R.id.selfmenu_mainpage);
		chat = (RelativeLayout) findViewById(R.id.selfmenu_chat);
		find = (RelativeLayout) findViewById(R.id.selfmenu_find);
		friend = (RelativeLayout) findViewById(R.id.selfmenu_friend);
		channel = (RelativeLayout) findViewById(R.id.selfmenu_channel);
		webar = (RelativeLayout) findViewById(R.id.menu_microbar);
		//sign = (RelativeLayout) findViewById(R.id.menu_sign);
		//apprecom = (RelativeLayout) findViewById(R.id.menu_applicationmore);
		msg_inform = (Button) findViewById(R.id.selfmenu_msg_inform);

	//	blog_pic = (ImageView) findViewById(R.id.selfmenu_blog_pic);
		myhome_pic = (ImageView) findViewById(R.id.selfmenu_mainpage_pic);
		chat_pic = (ImageView) findViewById(R.id.selfmenu_chat_pic);
		find_pic = (ImageView) findViewById(R.id.selfmenu_find_pic);
		friend_pic = (ImageView) findViewById(R.id.selfmenu_friend_pic);
		channel_pic = (ImageView) findViewById(R.id.selfmenu_channel_pic);
		webar_pic = (ImageView) findViewById(R.id.menu_microbar_pic);
		//sign_pic = (ImageView) findViewById(R.id.menu_sign_pic);
	//	apprecom_pic = (ImageView) findViewById(R.id.menu_application_more_pic);
		msg_inform = (Button) findViewById(R.id.selfmenu_msg_inform);

	}

	private void initviewpager() {
		TabFragmentPagerAdapter pagerAdapter = new TabFragmentPagerAdapter(
				getSupportFragmentManager());
		vp.setAdapter(pagerAdapter);
		Log.e("initviewpager----", m_position + " gdfh");
		// rg.getChildAt(m_position).performClick();
		rg.check(m_position);
		TranslateAnimation animation = new TranslateAnimation(
				currentIndicatorLeft, rg.getChildAt(m_position).getLeft(), 0f,
				0f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(300);
		animation.setFillAfter(true);

		// iv_indicator.startAnimation(animation);// 执行位移动画

		currentIndicatorLeft = rg.getChildAt(m_position).getLeft();// 记录当前
																	// 下标的距最左侧的
																	// 距离
		int x = (m_position > 0 ? ((RadioButton) rg.getChildAt(m_position))
				.getLeft() : 0) - rg.getChildAt(1).getLeft();
		tsv.smoothScrollTo(x, 0);
		vp.setCurrentItem(m_position);
		// rg.performClick();
	}

	private void initView() {
		Log.e("hhhhhhintview----", m_position + " gdfh");
		top_title.setText(ac.getUname());
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		cardinality = 4;
		if (tabTitle.length < 4) {
			cardinality = tabTitle.length;
		}

		currentNavItemWidth = dm.widthPixels / cardinality;

		// indicator
		// LayoutParams param = iv_indicator.getLayoutParams();
		// param.width = currentNavItemWidth;// 初始化滑动下标的宽
		// iv_indicator.setLayoutParams(param);

		// RadioGroup
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		rg.removeAllViews();
		for (int i = 0; i < tabTitle.length; i++) {

			RadioButton rb = (RadioButton) inflater.inflate(
					R.layout.nav_rg_item, null);
			rb.setLayoutParams(new LinearLayout.LayoutParams(
					currentNavItemWidth, LayoutParams.MATCH_PARENT));
			rb.setId(i);
			
			rb.setText(tabTitle[i]);
			rb.setTextSize(14);
			rg.addView(rb);
		}

		tsv.setParams(rl, iv_nav_left, iv_nav_right, this);
		iv_nav_left.setVisibility(View.GONE); // 初始化时默认选中第一个，所以将向左的箭头隐藏
		if (tabTitle.length <= 4) {
			iv_nav_right.setVisibility(View.GONE); // 向右的箭头隐藏
		}
		initviewpager();
		/*
		 * 初始化右侧菜单
		 */

		/*
		 * 初始化左侧菜单
		 */
	}

	class TabFragmentPagerAdapter extends FragmentPagerAdapter {

		public TabFragmentPagerAdapter(FragmentManager fm) {
			super(fm);

			// TODO Auto-generated constructor stub
		}

		// }
		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public Fragment getItem(int position) {
			// return new Fragment();
			Fragment fg = null;

			switch (position) {
			// case 0:
			// fg = new WeiboStatuesFragment();
			//
			// break;
			case 1:
				fg = new CommentToMeFragment();
				break;
			default:
				// fg = new CommonFragment();
				fg = new WeiboStatuesFragment();

				break;
			}

			return fg;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub

			switch (position) {
			// case 0:
			// WeiboStatuesFragment fg = (WeiboStatuesFragment) super
			// .instantiateItem(container, position);
			//
			// fg.setContext(mContext);
			// fg.setAccount(ac);
			// return fg;
			case 1:
				CommentToMeFragment fg3 = (CommentToMeFragment) super
						.instantiateItem(container, position);

				fg3.setContext(mContext);
				fg3.setAccount(ac);
				return fg3;
				// default:
				// CommonFragment fg2 = (CommonFragment) super.instantiateItem(
				// container, position);
				// fg2.setTitle(tabTitle[position]);
				// // fg.setArguments(args2);
				// return fg2;
			default:
				WeiboStatuesFragment fg = (WeiboStatuesFragment) super
						.instantiateItem(container, position);
				Log.e("hhhhhh----", m_position + " gdfh");
				fg.setContext(mContext);
				fg.setAccount(ac);
				fg.setM_position(position);
				if (position == 0) {
					fg.setModel("public_timeline");
				}
				if (position == 2) {
					fg.setModel("friends_timeline");
				}
				if (position == 4) {
					fg.setModel("favorite_feed");
				}
				if (position == 3) {
					fg.setModel("user_timeline");
				}
				return fg;
			}

			// return super.instantiateItem(container, position);
		}

		@Override
		public int getCount() {

			if (tabTitle != null) {
				return tabTitle.length;
			}

			return 0;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if ((System.currentTimeMillis() - mExitTime) > 2000) {

				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();

				mExitTime = System.currentTimeMillis();

			} else {
				Log.e("mList", mApplication.getmList().toString());
				mApplication.exit();
			}

			return true;

		}

		return super.onKeyDown(keyCode, event);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onRestoreInstanceState(Bundle inState) {
		super.onRestoreInstanceState(inState);
		mLeftMenuDrawer.restoreState(inState.getParcelable(STATE_MENUDRAWER));
		mRightMenuDrawer.restoreState(inState.getParcelable(STATE_MENUDRAWER));
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(STATE_MENUDRAWER, mLeftMenuDrawer.saveState());
		outState.putParcelable(STATE_MENUDRAWER, mRightMenuDrawer.saveState());
		outState.putInt(STATE_ACTIVE_VIEW_ID, mActiveViewId);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		dbm.closeDB();
		mApplication.deleteActivity(this);
		stopService(serviceIntent);
		Log.e("mList", mApplication.getmList().toString());
		super.onDestroy();
	}

}
