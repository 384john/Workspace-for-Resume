package com.nyulink.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.nyu.cloudcomputing.nyulink.R;
import com.nyulink.model.Model;
import com.nyulink.net.ThreadPoolUtils;
import com.nyulink.thread.HttpPostThread;
import com.nyulink.utils.TypefaceHelper;
import com.nyulink.utils.TypefaceInitialize;
import com.appkefu.lib.interfaces.KFIMInterfaces;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	private ImageView mClose;
	private EditText mName, mPassword, mEmail;
	private RelativeLayout mNext;
	private TextView mStartlogin, mRegisterTitle, mSigninHint;
	private String url = null;
	private String value = null;
	private String username = null;
	private String password = null;
	private String email = null;
	private TypefaceInitialize tfInit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);
		initView();
	}

	private void initView() {
//		mClose = (ImageView) findViewById(R.id.registClose);
		mName = (EditText) findViewById(R.id.Redit_name);
		mPassword = (EditText) findViewById(R.id.Redit_password);
		mEmail = (EditText) findViewById(R.id.Redit_email);
		mNext = (RelativeLayout) findViewById(R.id.signup);
		mStartlogin = (TextView) findViewById(R.id.startlogin);
		mRegisterTitle = (TextView) findViewById(R.id.text_register_title);
		mSigninHint = (TextView) findViewById(R.id.text_signin_hint);
		
		MyOnClickListener my = new MyOnClickListener();
//		mClose.setOnClickListener(my);
		mNext.setOnClickListener(my);
		mStartlogin.setOnClickListener(my);

		tfInit = new TypefaceInitialize(this);
		TypefaceHelper.typeface(mName, tfInit.Regular());
		TypefaceHelper.typeface(mPassword, tfInit.Regular());
		TypefaceHelper.typeface(mEmail, tfInit.Regular());
		TypefaceHelper.typeface(mSigninHint, tfInit.Regular());
		TypefaceHelper.typeface(mNext, tfInit.Bold());
		TypefaceHelper.typeface(mStartlogin, tfInit.Medium());
		TypefaceHelper.typeface(mRegisterTitle, tfInit.BoldSlab());
	}

	private void checkRegisterInfo() {
		url = Model.REGISTER;
		value = "{\"command\":\"" + "checkUser" 
				+ "\",\"uname\":\"" + username
				+ "\",\"upassword\":\"" + password
				+ "\",\"uemail\":\"" + email
				+ "\"}";
		Log.e("KidBuddies", value);
		ThreadPoolUtils.execute(new HttpPostThread(hand_checkRegisterInfo, url, value));
	}
	
	private void registerNYULink() {
		url = Model.REGISTER;
		value = "{\"command\":\"" + "addUser" 
				+ "\",\"uname\":\"" + username
				+ "\",\"upassword\":\"" + password
				+ "\",\"uemail\":\"" + email
				+ "\"}";
		Log.e("KidBuddies", value);
		ThreadPoolUtils.execute(new HttpPostThread(hand_registerNYULink, url, value));
	}
	
	private String checkNYUEmail(String strEmail){
		String ret = "";
        String pat="[a-zA-Z0-9_\\-\\.]+@[a-zA-Z0-9]+(\\.(edu))";  
        Pattern p = Pattern.compile(pat);//实例化Pattern类  
        Matcher m = p.matcher(strEmail);//验证内容是否合法  
        if(m.matches()){
        	if((strEmail.split("@")[strEmail.split("@").length-1]).equalsIgnoreCase("nyu.edu")){
        		ret = "ok";
        	}else{
        		ret = "The email does not belong to NYU";
        	}
        }else{
        	ret = "The email invalid or does not belong to NYU";
        }
        return ret;
	}

	class MyOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			int mId = v.getId();
			switch (mId) {
//			case R.id.registClose:
//				finish();
//				break;
			case R.id.signup:
				username = mName.getText().toString().trim();
				password = mPassword.getText().toString();
				email = mEmail.getText().toString().trim();
				if (!username.equalsIgnoreCase(null)
						&& !password.equalsIgnoreCase(null)
						&& !email.equalsIgnoreCase(null)
						&& !username.equals("") && !password.equals("")&& !email.equals("")) {
					String checkEmailRet = checkNYUEmail(email);
					if (username.length() >= 6 && password.length() >= 6 && checkEmailRet.equals("ok")) {
						checkRegisterInfo();//检查注册信息，若录入合法，先注册微客服，再注册NYULink
						//registerThread();// 注册app客服,注册成功后再注册本系统
					} else if(!(username.length() >= 6 && password.length() >= 6)){
						Toast.makeText(RegisterActivity.this, "Username and password at least 6 digitals", 1)
								.show();
					}else if(!checkEmailRet.equals("ok")){
						Toast.makeText(RegisterActivity.this, checkEmailRet, 1)
						.show();
					}
				} else {
					Toast.makeText(RegisterActivity.this, "Please enter username password and email", 1)
							.show();
				}
				break;
			case R.id.startlogin:
				Intent intent = new Intent(RegisterActivity.this,
						LoginActivity.class);
				startActivity(intent);
				break;
			}

		}
	}
	
	
	
	Handler hand_checkRegisterInfo = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				Toast.makeText(RegisterActivity.this, "Server error! Request failed!", 1).show();
			} else if (msg.what == 100) {
				Toast.makeText(RegisterActivity.this, "Server no response", 1).show();
			} else if (msg.what == 200) {
				String result = (String) msg.obj;
				if (result.equals("ok")) {
					//录入信息合法，注册微客服
					//registerNYULink();
					registerKFIMThread();
					
				} else if (result.trim().equals("existUserAndStatusIs0") || result.trim().equals("existUserAndStatusIs1")) {
					Toast.makeText(RegisterActivity.this, "user exist", 1).show();
					return;
				} else if (result.trim().equals("existEmail")) {
					
					Toast.makeText(RegisterActivity.this, "email exist", 1).show();
					return;
				} else {
//					mName.setText("");
//					mPassword.setText("");
//					mEmail.setText("");
					Toast.makeText(RegisterActivity.this, "registration failed", 1).show();
					return;
				}

			}
		};
	};
	
	Handler hand_registerNYULink = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				Toast.makeText(RegisterActivity.this, "Server error! Request failed!", 1).show();
			} else if (msg.what == 100) {
				Toast.makeText(RegisterActivity.this, "Server no response", 1).show();
			} else if (msg.what == 200) {
				String result = (String) msg.obj;
				if (result.equals("Message sent")) {
					Toast.makeText(RegisterActivity.this, "Successfully signed in, 系统已发你注册邮箱一封激活邮件，请登录邮箱进行激活本账号。", 1).show();
					Intent intent = new Intent();
					intent.putExtra("NameValue", username);
					setResult(2, intent);
					finish();
				}  else {
					mName.setText("");
					mPassword.setText("");
					mEmail.setText("");
					Toast.makeText(RegisterActivity.this, "registration failed", 1).show();
					return;
				}

			}
		};
	};
	
	

	public void registerKFIMThread() {

		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				Integer result = (Integer) msg.obj;

				if (result == 1) {
					Log.d("注册成功", "注册成功");
					//KFIM注册成功，开始注册NYULink
					registerNYULink();
					
				} else if (result == -400001) {
					Log.d("注册失败", "用户名长度最少为6");
					mName.setText("");
					mPassword.setText("");
					Toast.makeText(RegisterActivity.this,
							"注册失败:用户名长度最少为6(错误码:-400001)", Toast.LENGTH_LONG)
							.show();
				} else if (result == -400002) {
					Log.d("注册失败", "密码长度最少为6");
					mName.setText("");
					mPassword.setText("");
					Toast.makeText(RegisterActivity.this,
							"注册失败:密码长度最少为6(错误码:-400002)", Toast.LENGTH_LONG)
							.show();
				} else if (result == -400003) {
					Log.d("注册失败", "此用户名已经被注册");
					mName.setText("");
					mPassword.setText("");
					Toast.makeText(RegisterActivity.this, "注册失败:此用户名已经被注册",
							Toast.LENGTH_LONG).show();
				} else if (result == -400004) {
					Log.d("注册失败", "用户名含有非法字符");
					mName.setText("");
					mPassword.setText("");
					Toast.makeText(RegisterActivity.this, "注册失败:用户名含有非法字符",
							Toast.LENGTH_LONG).show();
				} else if (result == 0) {
					Log.d("注册失败",
							"其他原因：有可能是短时间内重复注册，为防止恶意注册，服务器对同一个IP注册做了时间间隔限制，即10分钟内同一个IP只能注册一个账号");
					mName.setText("");
					mPassword.setText("");
					Toast.makeText(RegisterActivity.this, "注册失败:别太累了，等下再注册",
							Toast.LENGTH_LONG).show();
				}
			}
		};

		new Thread() {
			public void run() {
				Message msg = new Message();
				// 目前用户名为整个微客服唯一，建议开发者在程序内部将appkey做为用户名的后缀，
				// 这样可以保证用户名的唯一性
				// 注册接口，返回结果为int
				msg.obj = KFIMInterfaces.register(Model.APPKEY + username,
						password);
				handler.sendMessage(msg);
			}
		}.start();

	}

}
