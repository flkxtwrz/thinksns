package com.hudson.thinksns.userinfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.hudson.thinksns.R;
/** 
* @author º÷ÏÕ≥¨
*/
public class SelfHomeProfile extends Activity {

	private Button back = null;
	private TextView username, gender, location, tag, profile = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.self_home_details);
		// findviewbyid
		findViews();
		inflateText(getIntent().getStringExtra("username"), getIntent()
				.getStringExtra("gender"),
				getIntent().getStringExtra("location"), getIntent()
						.getStringExtra("tag"),
				getIntent().getStringExtra("profile"));
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(SelfHomeProfile.this, SelfHome.class);
				i.putExtra("uid", getIntent().getStringExtra("uid"));
				startActivity(i);
				SelfHomeProfile.this.finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.self_home_profile, menu);
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent(SelfHomeProfile.this, SelfHome.class);
			i.putExtra("uid", getIntent().getStringExtra("uid"));
			startActivity(i);
			SelfHomeProfile.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void findViews() {
		back = (Button) findViewById(R.id.self_center_details_btn_backup);
		username = (TextView) findViewById(R.id.self_center_details_username);
		gender = (TextView) findViewById(R.id.self_center_details_sex);
		location = (TextView) findViewById(R.id.self_center_details_area);
		tag = (TextView) findViewById(R.id.self_center_details_label);
		profile = (TextView) findViewById(R.id.self_center_details_introduction);
	}

	private void inflateText(String tname, String tgender, String tlocation,
			String ttag, String tprofile) {
		username.setText(tname);
		gender.setText(tgender);
		location.setText(tlocation);
		tag.setText(ttag);
		profile.setText(tprofile);
	}

}
