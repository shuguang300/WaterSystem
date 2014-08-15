package com.env.watersystem.activity;
import com.env.watersystem.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SystemMainActivity extends Activity {
	
	private Button btn_Device,btn_Patrol;
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_activity_main);
		iniView();
	}
	
	private void iniActionBar(){
		actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	private void iniView(){
		iniActionBar();
		btn_Device = (Button)findViewById(R.id.system_activity_main_btn_device);
		btn_Patrol = (Button)findViewById(R.id.system_activity_main_btn_patrol);
		btn_Device.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent("com.env.dcwater.activity.StartupActivity"));
			}
		});
		btn_Patrol.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent("com.env.easypatrol.SplashActivity"));
			}
		});
	}
}
