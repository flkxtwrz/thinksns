package com.hudson.thinksns.userinfo;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;

import com.hudson.thinksns.MainActivity;
import com.hudson.thinksns.R;
import com.hudson.thinksns.adapter.UserPhotoListAdapter;
import com.hudson.thinksns.imageloader.ImageLoader;
import com.hudson.thinksns.model.UserPhoto;
/** 
* @author º÷ÏÕ≥¨
*/
public class SelfHomePhoto extends Activity {
	private GridView photo_gridview;
	private ArrayList<UserPhoto> photos = new ArrayList<UserPhoto>();
	private ImageLoader imageloder;
	private Context mContext;
	private Button back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		/* set it to be full screen */

		setContentView(R.layout.self_home_photoset);
		back = (Button) findViewById(R.id.self_center_photoset_btn_backup);
		mContext = this;
		imageloder = new ImageLoader(mContext);
		photos = (ArrayList<UserPhoto>) getIntent().getSerializableExtra(
				"photos");
		findViews();
		initViews();

		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent();
				i.setClass(SelfHomePhoto.this, SelfHome.class);
				i.putExtra("uid", getIntent().getStringExtra("uid"));
				startActivity(i);
				SelfHomePhoto.this.finish();
			}
		});
	}

	private void initViews() {
		// TODO Auto-generated method stub
		photo_gridview.setAdapter(new UserPhotoListAdapter(photos, imageloder,
				mContext));
	}

	private void findViews() {
		// TODO Auto-generated method stub
		photo_gridview = (GridView) findViewById(R.id.self_photo_gridview);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.self_home_photo, menu);
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent(SelfHomePhoto.this, SelfHome.class);
			i.putExtra("uid", getIntent().getStringExtra("uid"));
			startActivity(i);
			SelfHomePhoto.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
