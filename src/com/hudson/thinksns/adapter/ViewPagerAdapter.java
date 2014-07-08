package com.hudson.thinksns.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.hudson.thinksns.LoginActivity;
import com.hudson.thinksns.R;
import com.hudson.thinksns.lock.LockLoginActivity;


/**
 * 
 * @{# ViewPagerAdapter.java Create on 2013-5-2 ����11:03:39
 * 
 *     class desc: ����ҳ��������
 * 
 *     <p>
 *     Copyright: Copyright(c) 2013
 *     </p>
 * @Version 1.0
 * @author ʯ�ٲ�
 * 
 * 
 */
public class ViewPagerAdapter extends PagerAdapter {

    // �����б�
    private List<View> views;
    private Activity activity;

    private static final String SHAREDPREFERENCES_NAME = "first_pref";

    public ViewPagerAdapter(List<View> views, Activity activity) {
        this.views = views;
        this.activity = activity;
    }

    // ����arg1λ�õĽ���
    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView(views.get(arg1));
    }

    @Override
    public void finishUpdate(View arg0) {
    }

    // ��õ�ǰ������
    @Override
    public int getCount() {
        if (views != null) {
            return views.size();
        }
        return 0;
    }

    // ��ʼ��arg1λ�õĽ���
    @Override
    public Object instantiateItem(View v, int pos) {
        ((ViewPager) v).addView(views.get(pos), 0);
        if (pos == views.size()-1 ) {
            ImageView mStartWeiboImageButton = (ImageView) v
                    .findViewById(R.id.iv_start_weibo);
            mStartWeiboImageButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // �����Ѿ�����
                    setGuided();
                    goHome();

                }

            });
        }
        return views.get(pos);
    }

    private void goHome() {
        // ��ת
        Intent intent = new Intent(activity, LockLoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    /**
     * 
     * method desc�������Ѿ��������ˣ��´����������ٴ�����
     */
    private void setGuided() {
        SharedPreferences preferences = activity.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        // ��������
        editor.putBoolean("isFirstIn", false);
        // �ύ�޸�
        editor.commit();
    }

    // �ж��Ƿ��ɶ������ɽ���
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return (arg0 == arg1);
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(View arg0) {
    }

}
