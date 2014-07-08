package com.hudson.thinksns.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hudson.thinksns.R;
import com.hudson.thinksns.adapter.CommentToMeListAdapter;
import com.hudson.thinksns.application.ThinkSNSApplication;
import com.hudson.thinksns.diyview.xlistview.XListView;
import com.hudson.thinksns.diyview.xlistview.XListView.IXListViewListener;
import com.hudson.thinksns.imageloader.ImageLoader;
import com.hudson.thinksns.model.Account;
import com.hudson.thinksns.model.CommentOfStatus;
import com.hudson.thinksns.model.User;
import com.hudson.thinksns.netuti.MHttpClient;
import com.hudson.thinksns.netuti.MyNameValuePair;
/**
 * 
 * @author 王代银
 * 
 */
public class CommentToMeFragment extends Fragment implements  IXListViewListener{
	private XListView mListView;
	private Handler mHandler;
	private String commentstr="";
	private Context mContext;
	private CommentToMeListAdapter mWeiboListAdapter;
	private ArrayList<CommentOfStatus> mComments=new ArrayList<CommentOfStatus>() ;
    private Account account;
    private final int getcomment=0x11;
    private final int num=5;
    private int page=1;
    private final int LoadMore=0x33;
    private int state=0;
    
	public Context getContext() {
		return mContext;
	}

	public void setContext(Context mContext) {
		this.mContext = mContext;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.weibostatues, container,false);
		mListView=(XListView) v.findViewById(R.id.weibo_list);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
		mComments=new ArrayList<CommentOfStatus>() ;
		page=1;
		state=0;
		initHandler();
		GetCommentToMeThread getCommentthread=new GetCommentToMeThread();
		getCommentthread.start();
		
		return v;
	}
	private class GetCommentToMeThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			MyNameValuePair NameValuePair1 = new MyNameValuePair("app", "api");
			MyNameValuePair NameValuePair2 = new MyNameValuePair("mod",
					"WeiboStatuses");
			MyNameValuePair NameValuePair3 = new MyNameValuePair("act",
					"comments_to_me");
			MyNameValuePair NameValuePair4 = new MyNameValuePair("oauth_token",
					account.getOauth_token());
			MyNameValuePair NameValuePair5 = new MyNameValuePair(
					"oauth_token_secret", account.getOauth_token_secret());
			MyNameValuePair NameValuePair6 = new MyNameValuePair(
					"user_id", String.valueOf(account.getUid()));
			MyNameValuePair NameValuePair7 = new MyNameValuePair(
					"count", String.valueOf(num));
			MyNameValuePair NameValuePair8= new MyNameValuePair(
					"page", String.valueOf((page)));
			String result = MHttpClient.get(ThinkSNSApplication.baseUrl,
					NameValuePair1, NameValuePair2, NameValuePair3,
					NameValuePair4, NameValuePair5,NameValuePair6,NameValuePair7,NameValuePair8);
			Log.e("re", result);
			page++;
			Message msg = new Message();
			msg.what = getcomment;
			msg.obj = result;
			mHandler.sendMessage(msg);
		}
		
	}
	private ArrayList<CommentOfStatus> format() {
		 Log.e("ssss", commentstr);
		   ArrayList<CommentOfStatus> comments=new ArrayList<CommentOfStatus>() ;
		   try {
			JSONArray comments_jsonarray=new JSONArray(commentstr);
			 for(int i=0;i<comments_jsonarray.length();i++){
				 JSONObject comment_jsonobject=comments_jsonarray.getJSONObject(i);
				 int comment_id=Integer.parseInt(comment_jsonobject.optString("comment_id","-1"));
				 String apptype=comment_jsonobject.optString("app");
				 int source_id=Integer.parseInt(comment_jsonobject.optString("Row_id","-1"));
				 String content=comment_jsonobject.optString("content");
				 String Ctime=comment_jsonobject.optString("ctime");
				 JSONObject user_jsonobject=comment_jsonobject.optJSONObject("user_info");
				 int uid=Integer.parseInt(user_jsonobject.optString("uid","-1"));
				 String uname=user_jsonobject.optString("uname");
				 String headicon=user_jsonobject.optString("avatar_small");
				 User user=new User();
				 user.setUid(uid);
				 user.setUname(uname);
				 user.setHeadicon(headicon);
				 CommentOfStatus comment=new CommentOfStatus();
				 comment.setComment_id(comment_id);
				 comment.setApptype(apptype);
				 comment.setContent(content);
				 comment.setCtime(Ctime);
				 comment.setSource_id(source_id);
				 comment.setComment_user(user);
				 comments.add(comment);
			 }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return comments;
		}
	private void initHandler() {
		// TODO Auto-generated method stub
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch(msg.what){
				case getcomment:
					commentstr=msg.obj.toString();
					mListView.setPullLoadEnable(true);
					if(commentstr.equals("[]")){
						mListView.setPullLoadEnable(false);
						break;
					}
					ArrayList<CommentOfStatus> tcomments= format();
					Log.e("states", tcomments.toString());
					if(tcomments.size()!=0){
						for(CommentOfStatus s:tcomments){
							mComments.add(s);
						}
					}
					if(state==LoadMore){
						mWeiboListAdapter.notifyDataSetChanged();
						break;
					}
        mWeiboListAdapter=new CommentToMeListAdapter(mComments, new ImageLoader(mContext),mContext);
					
					//mstatuses=format();//解析微博
					//Log.e("stat", mstatuses.toString());
					mListView.setAdapter(mWeiboListAdapter);
					break;
				}
			}
			
		};
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		state=0;
		page=1;
		mComments.clear();
		// getResources().getDrawable(id)
		GetCommentToMeThread getcommentsthread=new GetCommentToMeThread();
		getcommentsthread.start();
		onLoad();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		state=LoadMore;
		GetCommentToMeThread getcommentsthread=new GetCommentToMeThread();
		getcommentsthread.start();
		onLoad();
	}
	 private void onLoad() {
			mListView.stopRefresh();
			mListView.stopLoadMore();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			mListView.setRefreshTime(sdf.format(new Date()));
		}
}
