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
 * @{# GuideActivity.java Create on 2013-5-2 ����10:59:08
 * 
 *     class desc: ��һ�ν���Ӧ�õ���������
 * 
 *     <p>
 *     Copyright: Copyright(c) 2013
 *     </p>
 * @Version 1.0
 * @author ���ͳ�
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
    // �ײ�С��ͼƬ
    private ImageView[] dots;

    // ��¼��ǰѡ��λ��
    private int currentIndex;
    /**
     * Handler:��ת����ͬ����
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

        // ��ʼ��ҳ��
        initViews();

        // ��ʼ���ײ�С��
        initDots();
    }

    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(this);

        views = new ArrayList<View>();
        // ��ʼ������ͼƬ�б�
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
        // ��ʼ��Adapter
        vpAdapter = new ViewPagerAdapter(views, this);

        vp = (ViewPager) findViewById(R.id.viewpager);
        vp.setAdapter(vpAdapter);
        // �󶨻ص�
        vp.setOnPageChangeListener(this);
    }

    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

        dots = new ImageView[views.size()];

        // ѭ��ȡ��С��ͼƬ
        for (int i = 0; i < views.size(); i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(true);// ����Ϊ��ɫ
        }

        currentIndex = 0;
        dots[currentIndex].setEnabled(false);// ����Ϊ��ɫ����ѡ��״̬
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

    // ������״̬�ı�ʱ����
    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    // ����ǰҳ�汻����ʱ����
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    	if(arg0==views.size() - 1){
    		// mHandler.sendEmptyMessageDelayed(GUIDE_END,TIME_GAP);
    		
    	}
    }
    /**
     * 
     * method desc�������Ѿ��������ˣ��´����������ٴ�����
     */
    private void setGuided() {
        SharedPreferences preferences = this.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        // ��������
        editor.putBoolean("isFirstIn", false);
        // �ύ�޸�
        editor.commit();
    }

    // ���µ�ҳ�汻ѡ��ʱ����
    @Override
    public void onPageSelected(int arg0) {
        // ���õײ�С��ѡ��״̬
        setCurrentDot(arg0);
    }
    
    //��ת������½���档
    private void goHome() {
        // ��ת
        Intent intent = new Intent(this, LockLoginActivity.class);
        this.startActivity(intent);
        this.finish();
    }
}
