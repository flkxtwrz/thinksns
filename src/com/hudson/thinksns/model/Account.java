package com.hudson.thinksns.model;

import java.io.Serializable;
/**
 * 
 * @author 王代银
 * 
 */
public class Account implements Serializable {
/**
 * 登录账户的实例类模型
 * @author hudson
 *
 * 2014年3月31日15:23:45
 */
	private static final long serialVersionUID = -4507554211068532759L;

	private int uid;
	private String uname;
	private String headicon;
	private String oauth_token;
	private String oauth_token_secret;
	private int state;
	public Account( int uid, String oauth_token, String oauth_token_secret,int state,String uname,String headicon) {
		super();
		
		this.uid = uid;
		this.oauth_token = oauth_token;
		this.oauth_token_secret = oauth_token_secret;
		this.state=state;
		this.uname=uname;
		this.headicon=headicon;
	}
	
	public Account() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getHeadicon() {
		return headicon;
	}

	public void setHeadicon(String headicon) {
		this.headicon = headicon;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getOauth_token() {
		return oauth_token;
	}
	public void setOauth_token(String oauth_token) {
		this.oauth_token = oauth_token;
	}
	public String getOauth_token_secret() {
		return oauth_token_secret;
	}
	public void setOauth_token_secret(String oauth_token_secret) {
		this.oauth_token_secret = oauth_token_secret;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		Account u=(Account) o;
		return this.getUid()==u.getUid()? true :false;
		//return super.equals(o);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Account["+"uid="+this.getUid()+","+"uname="+this.getUname()+","+"oauth_token="+this.getOauth_token()+","
				+"oauth_token_secret="+this.getOauth_token_secret()+","+"headicon="+this.getHeadicon()+","+"state="+this.getState()+"]"
				;
	}
	
}
