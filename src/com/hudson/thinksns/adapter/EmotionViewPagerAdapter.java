package com.hudson.thinksns.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
/**
 * @file EmotionViewPagerAdapter.java
 * @author ������
 * @description ���鹦�ܵ�adapter�����gridview
 */
public class EmotionViewPagerAdapter extends PagerAdapter {

	// �����б�
	private List<View> views;
	private Context activity;

	public EmotionViewPagerAdapter(List<View> views, Context activity) {
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

		return views.get(pos);
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
