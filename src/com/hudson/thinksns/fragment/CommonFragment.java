package com.hudson.thinksns.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hudson.thinksns.R;
import com.hudson.thinksns.netuti.NetUtils;
/**
 * 
 * @author Íõ´úÒø
 * 
 */
public class CommonFragment extends Fragment {
	private String title;
	private Handler mHandler;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.common_frag, container,false);
		
		final TextView tv = (TextView) view.findViewById(R.id.tv);
	//	Bundle arg = getArguments();
	
		if(title.contains("http")){
			new Thread(new getOauth(title)).start();
		}
		else
		tv.setText(title);
     mHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			tv.setText(msg.obj.toString());
		}
    	 
     };
		return view;
		
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	private class getOauth implements Runnable{
      private String url;
      
		public getOauth(String url) {
		super();
		this.url = url;
	}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			String s= NetUtils.getInstance().sendGet(url, "");
			Message msg=new Message();
			msg.obj=s;
			mHandler.sendMessage(msg);
			
		}
		
	}


	
}
