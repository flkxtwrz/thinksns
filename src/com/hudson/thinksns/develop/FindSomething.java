package com.hudson.thinksns.develop;

import com.hudson.thinksns.*;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 
 * @author 贾焱超
 * 
 */
@SuppressLint("NewApi")
public class FindSomething extends Fragment
{
	private RelativeLayout people, blog, app = null;
	private TextView hot, tp2, tp3 = null;
	private Intent i = null;
	View view;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		view = inflater.inflate(R.layout.activity_finding, container,false);
		
		return view;
	}
	void initView()
	{
		people = (RelativeLayout)view.findViewById(R.id.find_people);
		blog = (RelativeLayout)view.findViewById(R.id.find_hotblog);
		app = (RelativeLayout)view.findViewById(R.id.find_app);
		hot = (TextView)view.findViewById(R.id.find_hottopic_txt);
		tp2 = (TextView)view.findViewById(R.id.find_hot_topic2);
		tp3 = (TextView)view.findViewById(R.id.find_hot_topic3);
		
		people.setOnClickListener(ocl);
		blog.setOnClickListener(ocl);
		app.setOnClickListener(ocl);
		hot.setOnClickListener(ocl);
		tp2.setOnClickListener(ocl);
		tp3.setOnClickListener(ocl);
	}
	
	OnClickListener ocl = new OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			// TODO Auto-generated method stub
			switch (v.getId()) 
			{
			case R.id.find_people:
				// TODO click things
				i.putExtra("title", "找人");
				//i.setClass(getActivity().this, BlogDetail.class);
				getActivity().startActivity(i);
				//Finding.this.finish();
				break;
			case R.id.find_hotblog:
				// TODO click things
				i.putExtra("title", "热门微博");
				// i.setClass(getActivity().this, BlogDetail.class);
				getActivity().startActivity(i);
				//Finding.this.finish();
				break;
			case R.id.find_app:
				// TODO click things
				i.putExtra("title", "应用");
				// i.setClass(getActivity().this, BlogDetail.class);
				getActivity().startActivity(i);
				//Finding.this.finish();
				break;
			case R.id.find_hottopic_txt:
				// TODO click things

				break;
			case R.id.find_hot_topic2:
				// TODO click things

				break;
			case R.id.find_hot_topic3:
				// TODO click things

				break;
			case R.id.finding_audio:
				// TODO click things

				break;
			}
		}
	};
}
