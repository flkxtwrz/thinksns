package com.hudson.thinksns;
import java.util.ArrayList;
import java.util.List;

import com.hudson.thinksns.adapter.ViewPagerAdapter;
import com.hudson.thinksns.lock.LockLoginActivity;
import com.hudson.thinksns.lock.SetPasswordActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 
 * @{# GuideActivity.java Create on 2013-5-2 下午10:59:08
 * 
 *     class desc: 第一次进入应用的引导界面
 * 
 *     <p>
 *     Copyright: Copyright(c) 2013
 *     </p>
 * @Version 1.0
 * @author 贾焱超
 * 
 * 
 */
public class GuideActivity extends Activity implements OnPageChangeListener {

    private ViewPager vp;
    private ViewPagerAdapter vpAdapter;
    private List<View> views;
    private static final String SHAREDPREFERENCES_NAME = "first_pref";
    private static final int GUIDE_END = 1001;
    private static final int TIME_GAP = 3000;
    // 底部小点图片
    private ImageView[] dots;

    // 记录当前选中位置
    private int currentIndex;
    /**
     * Handler:跳转到不同界面
     */
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case GUIDE_END:
            	setGuided();
                goHome();
                break;
            
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*set it to be no title*/ 
        requestWindowFeature(Window.FEATURE_NO_TITLE);   
         
        /*set it to be full screen*/ 
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,    
        WindowManager.LayoutParams.FLAG_FULLSCREEN); 
        setContentView(R.layout.guide);

        // 初始化页面
        initViews();

        // 初始化底部小点
        initDots();
    }

    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(this);

        views = new ArrayList<View>();
        // 初始化引导图片列表
       ImageView iv1= new ImageView(this);
       iv1.setBackgroundResource(R.drawable.start);
       ImageView iv2= new ImageView(this);
       iv2.setBackgroundResource(R.drawable.start1);
       ImageView iv3= new ImageView(this);
       iv3.setBackgroundResource(R.drawable.start2);
        views.add(iv1);
        views.add(iv2);
        views.add(iv3);
        views.add(inflater.inflate(R.layout.guide_end, null));
        // 初始化Adapter
        vpAdapter = new ViewPagerAdapter(views, this);

        vp = (ViewPager) findViewById(R.id.viewpager);
        vp.setAdapter(vpAdapter);
        // 绑定回调
        vp.setOnPageChangeListener(this);
    }

    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

        dots = new ImageView[views.size()];

        // 循环取得小点图片
        for (int i = 0; i < views.size(); i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(true);// 都设为灰色
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

    // 当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    // 当当前页面被滑动时调用
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    	if(arg0==views.size() - 1){
    		// mHandler.sendEmptyMessageDelayed(GUIDE_END,TIME_GAP);
    		
    	}
    }
    /**
     * 
     * method desc：设置已经引导过了，下次启动不用再次引导
     */
    private void setGuided() {
        SharedPreferences preferences = this.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        // 存入数据
        editor.putBoolean("isFirstIn", false);
        // 提交修改
        editor.commit();
    }

    // 当新的页面被选中时调用
    @Override
    public void onPageSelected(int arg0) {
        // 设置底部小点选中状态
        setCurrentDot(arg0);
    }
    
    //跳转解锁登陆界面。
    private void goHome() {
        // 跳转
        Intent intent = new Intent(this, LockLoginActivity.class);
        this.startActivity(intent);
        this.finish();
    }
}
