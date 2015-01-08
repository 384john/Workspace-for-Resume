package com.nyulink.controller;

import java.util.List;

import com.nyu.cloudcomputing.nyulink.R;
import com.nyulink.info.UserInfo;
import com.nyulink.model.Model;
import com.nyulink.net.ThreadPoolUtils;
import com.nyulink.thread.HttpPostThread;
import com.nyulink.utils.MyJson;
import com.nyulink.utils.TypefaceHelper;
import com.nyulink.utils.TypefaceInitialize;
import com.appkefu.lib.interfaces.KFIMInterfaces;
import com.appkefu.lib.service.KFMainService;
import com.appkefu.lib.service.KFSettingsManager;
import com.appkefu.lib.service.KFXmppManager;
import com.appkefu.lib.utils.KFSLog;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private ImageView mClose;
	private RelativeLayout mLogin;
	private EditText mName, mPassword;
	private RelativeLayout mRepeatsendemail;
	private TextView mRegister, mLoginTitle, mForgotPsd;
	private String NameValue = null;
	private String PasswordValue = null;
	private String url = null;
	private String value = null;
	private MyJson myJson = new MyJson();
	
	private TypefaceInitialize tfInit;

	// private KFSettingsManager mSettingsMgr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		// mSettingsMgr = KFSettingsManager.getSettingsManager(this);
		initView();
	}

	private void initView() {
//		mClose = (ImageView) findViewById(R.id.loginClose);
		mLogin = (RelativeLayout) findViewById(R.id.login);
		mName = (EditText) findViewById(R.id.Ledit_name);
		mPassword = (EditText) findViewById(R.id.Ledit_password);
		mRepeatsendemail = (RelativeLayout) findViewById(R.id.repeatsendemail);
		
		mRegister = (TextView) findViewById(R.id.register);
		mLoginTitle = (TextView) findViewById(R.id.text_login_title);
		mForgotPsd = (TextView) findViewById(R.id.text_forgot_password);
		
		MyOnClickListener my = new MyOnClickListener();
//		mClose.setOnClickListener(my);
		mRepeatsendemail.setOnClickListener(my);
		mLogin.setOnClickListener(my);
		mRegister.setOnClickListener(my);

		tfInit = new TypefaceInitialize(this);
		TypefaceHelper.typeface(mName, tfInit.Regular());
		TypefaceHelper.typeface(mPassword, tfInit.Regular());
		TypefaceHelper.typeface(mForgotPsd, tfInit.Regular());
		TypefaceHelper.typeface(mLogin, tfInit.Bold());
		TypefaceHelper.typeface(mRegister, tfInit.Medium());
		TypefaceHelper.typeface(mLoginTitle, tfInit.BoldSlab());
	}

	class MyOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			int mId = v.getId();
			switch (mId) {
//			case R.id.loginClose:
//				finish();
//				break;
			case R.id.login:
				NameValue = mName.getText().toString();
				PasswordValue = mPassword.getText().toString();
				Log.e("KidBuddies", "NameValue" + NameValue + "  PasswordValue"
						+ PasswordValue);
				if (NameValue.equalsIgnoreCase(null)
						|| PasswordValue.equalsIgnoreCase(null)
						|| NameValue.equals("") || PasswordValue.equals("")) {
					Toast.makeText(LoginActivity.this, "Please input password", 1).show();
				} else {
					//登录NYULink
					login();
				}
				break;
			case R.id.repeatsendemail:
				NameValue = mName.getText().toString();
				PasswordValue = mPassword.getText().toString();
				repeatSendEmail();
				break;
			case R.id.register:
				Intent intent = new Intent(LoginActivity.this,
						RegisterActivity.class);
				startActivityForResult(intent, 1);

			}

		}

	}

	private void login() {
		url = Model.LOGIN;
		value = "{\"uname\":\"" + NameValue + "\",\"upassword\":\""
				+ PasswordValue + "\"}";
		Log.e("KidBuddies", value);
		ThreadPoolUtils.execute(new HttpPostThread(hand_login, url, value));
	}

	Handler hand_login = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				Toast.makeText(LoginActivity.this, "Server error! Request failed!", 1).show();
			} else if (msg.what == 100) {
				Toast.makeText(LoginActivity.this, "Server no response", 1).show();
			} else if (msg.what == 200) {
				String result = (String) msg.obj;
				if(result == null)
				{
					Toast.makeText(LoginActivity.this, "Server no response, please check network", 1).show();
					return;
				}
				
				Log.e("Result after login", result);
				if (result.equalsIgnoreCase("NOUSER")) {
					mName.setText("");
					mPassword.setText("");
					Toast.makeText(LoginActivity.this, "User doesn't exist", 1).show();
					return;
				} else if (result.equalsIgnoreCase("NOPASS")) {
					mPassword.setText("");
					Toast.makeText(LoginActivity.this, "Wrong password", 1).show();
					return;
				} else if (result.equalsIgnoreCase("NOACTIVE")) {
					mRepeatsendemail.setVisibility(View.VISIBLE);
					Toast.makeText(LoginActivity.this, "您的账户处于未激活状态，请先登录注册邮箱点击激活链接进行激活操作。如果您想重新接受激活邮件，请点击\"Repeat Send Email\"", 1).show();
					return;
				} else if (result != null) {
					Toast.makeText(LoginActivity.this, "Successfully signed in", 1).show();
					List<UserInfo> newList = myJson.getNearUserList(result);
					if (newList != null) {
						Model.MYUSERINFO = newList.get(0);
					}
					
//					Intent intent = new Intent(LoginActivity.this,
//							UserInfoActivity.class);
//					Bundle bund = new Bundle();
//					bund.putSerializable("UserInfo", Model.MYUSERINFO);
//					intent.putExtra("value", bund);
					
					// 调用SharedPreferences保存本程序软件相关参数
					SharedPreferences sp = LoginActivity.this
							.getSharedPreferences("UserInfo", MODE_PRIVATE);
					Log.e("SharedPreferencesOld",
							sp.getString("UserInfoJson", "none"));
					SharedPreferences.Editor mSettinsEd = sp.edit();
					mSettinsEd.putString("UserInfoJson", result);
					// 提交保存
					mSettinsEd.commit();

					// 登录KFIM
					 KFIMInterfaces.login(LoginActivity.this, Model.APPKEY
					 + NameValue, PasswordValue);
					 
					// 设置KFIM个人资料
					KFIMInterfaces.setVCardField(LoginActivity.this,
							"NICKNAME", Model.MYUSERINFO.getUname());
					
					//跳转到首页
					Intent intent = new Intent(LoginActivity.this,
							MainActivity.class);
					startActivity(intent);
					finish();
				}
			}
		};
	};
	
	private void repeatSendEmail() {
		url = Model.REGISTER;
		value = "{\"command\":\"" + "repeatSendEmail" 
				+ "\",\"uname\":\"" + NameValue
				+ "\",\"upassword\":\"" + PasswordValue
				+ "\"}";
		ThreadPoolUtils.execute(new HttpPostThread(hand_repeatSendEmail, url, value));
	}
	
	Handler hand_repeatSendEmail = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				Toast.makeText(LoginActivity.this, "Server error! Request failed!", 1).show();
			} else if (msg.what == 100) {
				Toast.makeText(LoginActivity.this, "Server no response", 1).show();
			} else if (msg.what == 200) {
				String result = (String) msg.obj;
				if(result == null)
				{
					Toast.makeText(LoginActivity.this, "Server no response, please check network", 1).show();
					return;
				}
				if (result.equalsIgnoreCase("username and password are error")) {
					mName.setText("");
					mPassword.setText("");
					Toast.makeText(LoginActivity.this, "username and password are error", 1).show();
				} else if (result.equalsIgnoreCase("actived")) {
					Toast.makeText(LoginActivity.this, "您之前已经激活过，无需重新发送激活邮件，请直接登录", 1).show();
				} else if (result.equalsIgnoreCase("Message sent")) {
					Toast.makeText(LoginActivity.this, "激活邮件已发送，请在24小时登录邮箱并激活", 1).show();
				} else{
					Toast.makeText(LoginActivity.this, "邮件发送失败，请检查网络", 1).show();
				}
			}
		};
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1 && resultCode == 2 && data != null) {
			NameValue = data.getStringExtra("NameValue");
			mName.setText(NameValue);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		KFSLog.d("onStart");

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(KFMainService.ACTION_XMPP_CONNECTION_CHANGED);
		registerReceiver(mXmppreceiver, intentFilter);

	}

	@Override
	protected void onStop() {
		super.onStop();

		KFSLog.d("onStop");
		unregisterReceiver(mXmppreceiver);
	}

	private BroadcastReceiver mXmppreceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(KFMainService.ACTION_XMPP_CONNECTION_CHANGED)) {
				updateStatus(intent.getIntExtra("new_state", 0));
			}

		}
	};

	private void updateStatus(int status) {
		switch (status) {
		case KFXmppManager.CONNECTED:
			KFSLog.d("Successfully signed in");
			break;
		case KFXmppManager.DISCONNECTED:
			KFSLog.d("not signed in");
			break;
		case KFXmppManager.CONNECTING:
			KFSLog.d("sign in...");
			break;
		case KFXmppManager.DISCONNECTING:
			KFSLog.d("log out...");
			break;
		case KFXmppManager.WAITING_TO_CONNECT:
		case KFXmppManager.WAITING_FOR_NETWORK:
			KFSLog.d("waiting to connect");
			break;
		default:
			throw new IllegalStateException();
		}
	}

}