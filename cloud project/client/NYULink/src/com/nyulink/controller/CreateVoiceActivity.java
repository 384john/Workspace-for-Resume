package com.nyulink.controller;

import com.nyu.cloudcomputing.nyulink.R;
import com.nyulink.model.Model;
import com.nyulink.net.ThreadPoolUtils;
import com.nyulink.thread.HttpPostThread;
import com.nyulink.utils.TypefaceHelper;
import com.nyulink.utils.TypefaceInitialize;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CreateVoiceActivity extends Activity {
	private ImageView mClose, mCreate;
	private EditText mTitle, mStartDate, mStartTime, mEndDate, mEndTime, mSpot, mMaxNum, mDetail;
	
	private TypefaceInitialize tfInit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_new_voice);
		initView();
	}

	private void initView() {
		MyOnclickListener mOnclickListener = new MyOnclickListener();
		
		mClose = (ImageView) findViewById(R.id.cancel_new_voice);
		mCreate = (ImageView) findViewById(R.id.submit_new_voice);
		
//		mTitle = (EditText) findViewById(R.id.create_activity_title);
//		mStartDate = (EditText) findViewById(R.id.create_activity_startdate);
//		mStartTime = (EditText) findViewById(R.id.create_activity_starttime);
//		mEndDate = (EditText) findViewById(R.id.create_activity_enddate);
//		mEndTime = (EditText) findViewById(R.id.create_activity_endtime);
//		mSpot = (EditText) findViewById(R.id.create_activity_spot);
//		mMaxNum = (EditText) findViewById(R.id.create_activity_maxnum);
		mDetail = (EditText) findViewById(R.id.voice_detail);
		
		tfInit = new TypefaceInitialize(this);
		TypefaceHelper.typeface(mDetail, tfInit.Regular());
		
		mClose.setOnClickListener(mOnclickListener);
		mCreate.setOnClickListener(mOnclickListener);
	}

	private class MyOnclickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.cancel_new_voice:
					CreateVoiceActivity.this.finish();
					break;
				case R.id.submit_new_voice:
					if (Model.MYUSERINFO != null) {
						checkForm();
					} else {
						Intent intent = new Intent(CreateVoiceActivity.this, LoginActivity.class);
						startActivity(intent);
					}
					break;
			}
		}
	}

	private void checkForm() {
//		if (mTitle.getText().toString().equals("")) {
//			Toast.makeText(CreateVoiceActivity.this, "Invalid title", 1).show();
//			return;
//		}
//		if (mStartDate.getText().toString().equals("")) {
//			Toast.makeText(CreateVoiceActivity.this, "Invalid start date", 1).show();
//			return;
//		}
//		if (mStartTime.getText().toString().equals("")) {
//			Toast.makeText(CreateVoiceActivity.this, "Invalid start time", 1).show();
//			return;
//		}
//		if (mEndDate.getText().toString().equals("")) {
//			Toast.makeText(CreateVoiceActivity.this, "Invalid end date", 1).show();
//			return;
//		}
//		if (mEndTime.getText().toString().equals("")) {
//			Toast.makeText(CreateVoiceActivity.this, "Invalid end time", 1).show();
//			return;
//		}
//		if (mSpot.getText().toString().equals("")) {
//			Toast.makeText(CreateVoiceActivity.this, "Invalid spot", 1).show();
//			return;
//		}
//		if (mMaxNum.getText().toString().equals("")) {
//			Toast.makeText(CreateVoiceActivity.this, "Invalid max participants", 1).show();
//			return;
//		}
		if (mDetail.getText().toString().equals("")) {
			Toast.makeText(CreateVoiceActivity.this, "Invalid detail description", 1).show();
			return;
		}
		
		String typeId = "1";   // default value is 1
		String uid = Model.MYUSERINFO.getUserid();
//		String title = mTitle.getText().toString();
//		String startdate = mStartDate.getText().toString();
//		String starttime = mStartTime.getText().toString();
//		String enddate = mEndDate.getText().toString();
//		String endtime = mEndTime.getText().toString();
//		String spot = mSpot.getText().toString();
//		String maxnum = mMaxNum.getText().toString();
		String title = "";
		String startdate = "";
		String starttime = "";
		String enddate = "";
		String endtime = "";
		String spot = "";
		String maxnum = "";
		String detail = mDetail.getText().toString();
		
		// removed qlike and qunlike
		String form = "{\"uid\":\"" + uid + "\"," + "\"tid\":\"" + typeId + "\","
						+ "\"title\":\"" + title + "\"," + "\"startdate\":\"" + startdate + "\","
						+ "\"starttime\":\"" + starttime + "\"," + "\"enddate\":\"" + enddate + "\","
						+ "\"endtime\":\"" + endtime + "\"," + "\"spot\":\"" + spot + "\","
						+ "\"maxnum\":\"" + maxnum + "\"," + "\"detail\":\"" + detail + "\"}";
		ThreadPoolUtils.execute(new HttpPostThread(hand, Model.ADDVALUE, form));   // removed photo data
		CreateVoiceActivity.this.finish();
	}

	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 200) {
				String result = (String) msg.obj;
				if (result != null && result.equals("ok")) {
					Toast.makeText(CreateVoiceActivity.this, "Successfully created!", 1).show();
					CreateVoiceActivity.this.finish();
				}
			}
		};
	};

}
