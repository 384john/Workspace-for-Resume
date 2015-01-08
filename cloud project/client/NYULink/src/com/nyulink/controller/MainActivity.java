package com.nyulink.controller;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nyu.cloudcomputing.nyulink.R;
import com.nyulink.fragment.CrossFragment;
import com.nyulink.fragment.EventFragment;
import com.nyulink.fragment.FriendFragment;
import com.nyulink.fragment.VoiceFragment;
import com.nyulink.fragment.NearFragment;
import com.nyulink.fragment.NiceFragment;
import com.nyulink.fragment.NotesFragment;
import com.nyulink.fragment.PictureFragment;
import com.nyulink.fragment.CrossFragment.CrossFragmentCallBack;
import com.nyulink.fragment.EventFragment.EventFragmentCallback;
import com.nyulink.fragment.FriendFragment.FriendFragmentCallback;
import com.nyulink.fragment.VoiceFragment.HotFragmentCallBack;
import com.nyulink.fragment.NearFragment.NearFragmentCallBack;
import com.nyulink.fragment.NiceFragment.NiceFragmentCallBack;
import com.nyulink.fragment.NotesFragment.NotesFragmentCallBack;
import com.nyulink.fragment.PictureFragment.PictureFragmentCallBack;
import com.nyulink.info.UserInfo;
import com.nyulink.model.Model;
import com.nyulink.utils.LoadImg;
import com.nyulink.utils.MyJson;
import com.nyulink.utils.LoadImg.ImageDownloadCallBack;
import com.nyulink.utils.TypefaceHelper;
import com.nyulink.utils.TypefaceInitialize;
import com.appkefu.lib.interfaces.KFIMInterfaces;
import com.appkefu.lib.service.KFSettingsManager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity implements
		OnClickListener {
	private KFSettingsManager mSettingsMgr;
	private View mLeftView;
	// 第三方的抽屉菜单管理工具类
	private SlidingMenu mSlidingMenu;
	// 热门的碎片
	private VoiceFragment mHotFragment;
	// 精华的碎片
	private NiceFragment mNiceFragment;
	// 有图有真相的碎片
	private PictureFragment mPictureFragment;
	// 穿越的碎片
	private CrossFragment mCrossFragment;
	// local events
	private EventFragment mEventFragment;
	// friend fragment
	private FriendFragment mFriendFragment;
	// 小纸条的碎片
	private NotesFragment mNotesFragment;
	// 附近的碎片
	private NearFragment mNearFragment;
	// 定义fragment管理器:向界面增加修改删除fragment
	private FragmentManager mFragmentManager;
	// 获取fragment栈
	private FragmentTransaction mFragmentTransaction;
	private List<Fragment> myFragmentList = new ArrayList<Fragment>();
	// leftView里面的控件
	private RelativeLayout mMenuUserInfo;
	private TextView mMenuUserName, mMenuUserEmail;
	private ImageView mMenuUserImage;
	
	private ImageView mSettingBtn, mUserMore;// 设置按钮
	// leftview中下面的按钮
	private RelativeLayout mLeftHot, mLeftNice, mLeftPicture, mLeftCross, mLeftContact, mLeftEvent,
			mLeftScrip, mLeftNear, mLeftCheck; // leftview中下面的按钮
	
	private TextView mMenuVoiceText, mMenuContactText, mMenuGossipText, mMenuMapText;
	
	private int fragmentFlag = 0;
	private MyJson myJson = new MyJson();
	private LoadImg loadImgHeadImg;
	private String curUserHeadName = "";//当前头像照片名称，用于头像图片更新
	
	private TypefaceInitialize tfInit;
	private UserInfo info = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//检查是否登陆，若没登陆，就跳转到登陆界面
		if (Model.MYUSERINFO == null) {
			Intent intent = new Intent(MainActivity.this, LoginActivity.class);
			startActivity(intent);
			MainActivity.this.finish();
			return;
		}
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		//
		mSettingsMgr = KFSettingsManager.getSettingsManager(this);
		
//		Intent intent = getIntent();
//		Bundle bund = intent.getBundleExtra("value");
//		info = (UserInfo) bund.getSerializable("UserInfo");

		// 设置开发者调试模式，默认为true，如要关闭开发者模式，请设置为false
		KFIMInterfaces.enableDebugMode(this, true);
		initView();
		//在登陆页面已经登陆
		//login();
		
//		SharedPreferences sp = MainActivity.this.getSharedPreferences(
//				"UserInfo", MODE_PRIVATE);
//		String result = sp.getString("UserInfoJson", "none");
//		Log.e("SharedPreferencesOld", result);
//		if (!result.equals("none")) {
//			List<UserInfo> newList = myJson.getNearUserList(result);
//			if (newList != null) {
//				Model.MYUSERINFO = newList.get(0);
//				myUserName.setText(Model.MYUSERINFO.getUname());
//			}
//		} 
	}

	private void initView() {
		mLeftView = View.inflate(MainActivity.this, R.layout.leftview, null);
		
		//mMenuUserInfo = (RelativeLayout) mLeftView.findViewById(R.id.MenuUserInfo);
		
//		mSettingBtn = (ImageView) mLeftView.findViewById(R.id.SettingBtn);
		mLeftHot = (RelativeLayout) mLeftView.findViewById(R.id.LeftHot);
//		mLeftNice = (RelativeLayout) mLeftView.findViewById(R.id.LeftNice);
//		mLeftPicture = (RelativeLayout) mLeftView
//				.findViewById(R.id.LeftPicture);
		mLeftScrip = (RelativeLayout) mLeftView.findViewById(R.id.LeftScrip);
		mLeftNear = (RelativeLayout) mLeftView.findViewById(R.id.LeftNear);
		mLeftEvent = (RelativeLayout) mLeftView.findViewById(R.id.LeftEvent);
		mLeftContact = (RelativeLayout) mLeftView.findViewById(R.id.LeftContact);
//		mLeftCross = (RelativeLayout) mLeftView.findViewById(R.id.LeftCross);
//		mLeftCheck = (RelativeLayout) mLeftView.findViewById(R.id.LeftCheck);
		
		mMenuUserImage = (ImageView) mLeftView.findViewById(R.id.MenuUserImage);
		mMenuUserName = (TextView) mLeftView.findViewById(R.id.MenuUserName);
		mMenuUserEmail = (TextView) mLeftView.findViewById(R.id.MenuUserEmail);
		
		mMenuVoiceText = (TextView) mLeftView.findViewById(R.id.MenuVoiceText);
		mMenuContactText = (TextView) mLeftView.findViewById(R.id.MenuContactText);
		mMenuGossipText = (TextView) mLeftView.findViewById(R.id.MenuGossipText);
		mMenuMapText = (TextView) mLeftView.findViewById(R.id.MenuMapText);
		
		mUserMore = (ImageView) mLeftView.findViewById(R.id.img_UserMore);
		
		tfInit = new TypefaceInitialize(this);
		TypefaceHelper.typeface(mMenuVoiceText, tfInit.LightSlab());
		TypefaceHelper.typeface(mMenuContactText, tfInit.LightSlab());
		TypefaceHelper.typeface(mMenuGossipText, tfInit.LightSlab());
		TypefaceHelper.typeface(mMenuMapText, tfInit.LightSlab());
		
		TypefaceHelper.typeface(mMenuUserName, tfInit.Medium());
		TypefaceHelper.typeface(mMenuUserEmail, tfInit.Thin());
		
		try {
			//设置用户名称
			mMenuUserName.setText(Model.MYUSERINFO.getUname());
			mMenuUserEmail.setText(Model.MYUSERINFO.getUemail());
			
			//设置头像
			if (Model.MYUSERINFO.getUhead() != null && !Model.MYUSERINFO.getUhead().equals("null")) {
				curUserHeadName = Model.MYUSERINFO.getUhead();
				loadImgHeadImg = new LoadImg(MainActivity.this);
				Bitmap bit = loadImgHeadImg.loadImage(mMenuUserImage,
						Model.USERHEADURL + Model.MYUSERINFO.getUhead(),
						new ImageDownloadCallBack() {
							public void onImageDownload(ImageView imageView,
									Bitmap bitmap) {
								mMenuUserImage.setImageBitmap(bitmap);
							}
						});
				if (bit != null) {
					mMenuUserImage.setImageBitmap(bit);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		//mMenuUserInfo.setOnClickListener(MainActivity.this);
		mMenuUserImage.setOnClickListener(MainActivity.this);
		mUserMore.setOnClickListener(MainActivity.this);
		
//		mSettingBtn.setOnClickListener(MainActivity.this);
		mLeftHot.setOnClickListener(MainActivity.this);
//		mLeftNice.setOnClickListener(MainActivity.this);
//		mLeftPicture.setOnClickListener(MainActivity.this);
		mLeftScrip.setOnClickListener(MainActivity.this);
		mLeftNear.setOnClickListener(MainActivity.this);
		mLeftEvent.setOnClickListener(MainActivity.this);
		mLeftContact.setOnClickListener(MainActivity.this);
//		mLeftCross.setOnClickListener(MainActivity.this);
//		mLeftCheck.setOnClickListener(MainActivity.this);
//		mLeftHot.setBackgroundResource(R.drawable.side_menu_background_active);
		TypefaceHelper.typeface(mMenuVoiceText, tfInit.BoldSlab());
		mHotFragment = new VoiceFragment();
		myFragmentList.add(mHotFragment);
		mNiceFragment = new NiceFragment();
		myFragmentList.add(mNiceFragment);
		mPictureFragment = new PictureFragment();
		myFragmentList.add(mPictureFragment);
		mCrossFragment = new CrossFragment();
		myFragmentList.add(mCrossFragment);
		mNotesFragment = new NotesFragment();
		myFragmentList.add(mNotesFragment);
		mNearFragment = new NearFragment();
		myFragmentList.add(mNearFragment);
		mFriendFragment = new FriendFragment();
		myFragmentList.add(mFriendFragment);
		mEventFragment = new EventFragment();
		myFragmentList.add(mEventFragment);
		mSlidingMenu = this.getSlidingMenu();
		this.setBehindContentView(mLeftView);
		mSlidingMenu.setShadowDrawable(R.drawable.drawer_shadow);
		mSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		mSlidingMenu.setFadeDegree(0.35f);
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		mFragmentManager = MainActivity.this.getSupportFragmentManager();
		mFragmentTransaction = mFragmentManager.beginTransaction();
		//createFargment(5);
		createFargment(1);
		FragmentTransaction mFragmentTransaction = mFragmentManager
				.beginTransaction();
		mFragmentTransaction.replace(R.id.main, mHotFragment);
		mFragmentTransaction.commit();
		
	}

	@Override
	public void onClick(View v) {
		int mID = v.getId();
		// MainActivity.this.toggle();
		switch (mID) {
		case R.id.MenuUserImage:
			if (Model.MYUSERINFO != null) {
				Intent intent = new Intent(MainActivity.this,
						UserInfoActivity.class);
				Bundle bund = new Bundle();
				bund.putSerializable("UserInfo", Model.MYUSERINFO);
				intent.putExtra("value", bund);
				startActivity(intent);
			} else {
				Intent intent = new Intent(MainActivity.this,
						LoginActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.LeftHot:
			createleftviewbg();
//			mLeftHot.setBackgroundResource(R.drawable.side_menu_background_active);
			TypefaceHelper.typeface(mMenuVoiceText, tfInit.BoldSlab());
			createFargment(1);
			break;
		case R.id.LeftScrip:
			createleftviewbg();
//			mLeftScrip
//					.setBackgroundResource(R.drawable.side_menu_background_active);
			TypefaceHelper.typeface(mMenuGossipText, tfInit.BoldSlab());
			
			createFargment(5);
			break;
		case R.id.LeftNear:
/*			createleftviewbg();
			mLeftNear
					.setBackgroundResource(R.drawable.side_menu_background_active);
			createFargment(6);*/
			
			createleftviewbg();
			TypefaceHelper.typeface(mMenuMapText, tfInit.BoldSlab());
			
		    Intent intentmap = new Intent(MainActivity.this,MapMainActivity.class);
			startActivity(intentmap);
			
			break;
		case R.id.LeftContact:
			createleftviewbg();
//			mLeftContact
//					.setBackgroundResource(R.drawable.side_menu_background_active);
			TypefaceHelper.typeface(mMenuContactText, tfInit.BoldSlab());
			
			createFargment(7);
			break;
		case R.id.LeftEvent:
			createleftviewbg();
			mLeftEvent
					.setBackgroundResource(R.drawable.side_menu_background_active);
			createFargment(8);
			break;
		case R.id.img_UserMore:
			logout();
			break;
		default:
			break;
		}
	}

//    // 退出登录
//	private void logout() {
//		if (Model.MYUSERINFO != null) {
//			if (info.getUname().equals(Model.MYUSERINFO.getUname())) {
//				new AlertDialog.Builder(this)
//						.setMessage("Do you wanna logout current account?")
//						.setPositiveButton("Yes",
//								new DialogInterface.OnClickListener() {
//									@Override
//									public void onClick(DialogInterface dialog,
//											int which) {
//										Model.MYUSERINFO = null;
//										SharedPreferences sp = getSharedPreferences(
//												"UserInfo", MODE_PRIVATE);
//										Editor editor = sp.edit();
//										editor.clear();
//										editor.commit();
//										// 退出登录
//										KFIMInterfaces
//												.Logout(MainActivity.this);
//										Intent intent = new Intent(
//												MainActivity.this,
//												LoginActivity.class);
//										startActivity(intent);
//										finish();
//									}
//								}).setNegativeButton("No", null).create()
//						.show();
//			}
//		}
//	}

	// 设置右边的fragment加载的控件
	private void createFargment(int flag) {
		// 如果正在加载的fragment是传过来的 那么就不操作否则就去加载
		MainActivity.this.toggle();
		if (fragmentFlag != flag) {
			switch (flag) {
			case 1:// Activity
				hotFragmentCallBack();
				break;
			case 2:
				niceFragmentCallBack();
				break;
			case 3:
				pictureFragmentCallBack();
				break;
			case 4:
				crossFragmentCallBack();
				break;
			case 5:
				notesfragmentCallBack();
				break;
			case 6:
				nearfragmentCallBack();
				break;
			case 7:
				friendFragmentCallback();
				break;
			case 8:
				eventFragmentCallback();
				break;
			}
			if (fragmentFlag != 0) {
				mFragmentTransaction.remove(myFragmentList
						.get(fragmentFlag - 1));
			}
			mFragmentTransaction = mFragmentManager.beginTransaction();
			mFragmentTransaction.replace(R.id.main,
					myFragmentList.get(flag - 1));
			// 提交保存杠杆替换或者添加的fragment
			mFragmentTransaction.commit();
			fragmentFlag = flag;
		}
	}

	// 从notesfragment里面回调回来的事件监听设置方法
	private void notesfragmentCallBack() {
		mNotesFragment.setCallBack(new MyNotesFragmentCallBack());
	}

	// 从picturefragment里面回调回来的事件监听设置方法
	private void crossFragmentCallBack() {
		mCrossFragment.setCallBack(new MyCrossFragmentCallBack());
	}

	// 从picturefragment里面回调回来的事件监听设置方法
	private void pictureFragmentCallBack() {
		mPictureFragment.setCallBack(new MyPictureFragmentCallBack());
	}

	// 从nicefragment里面回调回来的事件监听设置方法
	private void niceFragmentCallBack() {
		mNiceFragment.setCallBack(new MyNiceFragmentCallBack());
	}

	// 从hotfragment里面回调回来的事件监听设置方法
	private void hotFragmentCallBack() {
		mHotFragment.setCallBack(new MyHotFragmentCallBack());
	}

	// 从nearfragment里面回调回来的事件监听设置方法
	private void nearfragmentCallBack() {
		mNearFragment.setCallBack(new MyNearFragmentCallBack());
	}
	
	private void friendFragmentCallback() {
		mFriendFragment.setCallBack(new MyFriendFragmentCallBack());
	}
	
	private void eventFragmentCallback() {
		mEventFragment.setCallBack(new MyEventFragmentCallBack());
	}

	private class MyNotesFragmentCallBack implements NotesFragmentCallBack {
		@Override
		public void callback(int flag) {
			switch (flag) {
			case R.id.Menu:
				MainActivity.this.toggle();
				break;
			default:
				break;
			}
		}
	}

	private class MyHotFragmentCallBack implements HotFragmentCallBack {
		@Override
		public void callback(int flag) {
			switch (flag) {
			case R.id.Menu:
				MainActivity.this.toggle();
				break;
			case R.id.SendAshamed:
				if (Model.MYUSERINFO != null) {
					Intent intent = new Intent(MainActivity.this,
							CreateVoiceActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(MainActivity.this,
							LoginActivity.class);
					startActivity(intent);
				}
				break;
			default:
				break;
			}
		}
	}

	// 实现nicefragment里面接口的子类
	private class MyCrossFragmentCallBack implements CrossFragmentCallBack {
		public void callback(int flag) {
			switch (flag) {
			case R.id.Menu:// 用户点击hotfragment上面的导航按钮
				// 设置开启或关闭抽屉
				MainActivity.this.toggle();
				break;
			case R.id.SendAshamed:
				if (Model.MYUSERINFO != null) {
					Intent intent = new Intent(MainActivity.this,
							CreateVoiceActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(MainActivity.this,
							LoginActivity.class);
					startActivity(intent);
				}
				break;
			default:
				break;
			}
		}
	}

	// 实现nicefragment里面接口的子类
	private class MyPictureFragmentCallBack implements PictureFragmentCallBack {
		public void callback(int flag) {
			switch (flag) {
			case R.id.Menu:// 用户点击hotfragment上面的导航按钮
				// 设置开启或关闭抽屉
				MainActivity.this.toggle();
				break;
			case R.id.SendAshamed:
				if (Model.MYUSERINFO != null) {
					Intent intent = new Intent(MainActivity.this,
							CreateVoiceActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(MainActivity.this,
							LoginActivity.class);
					startActivity(intent);
				}
				break;
			default:
				break;
			}
		}
	}

	// 实现nicefragment里面接口的子类
	private class MyNiceFragmentCallBack implements NiceFragmentCallBack {
		public void callback(int flag) {
			switch (flag) {
			case R.id.Menu:// 用户点击hotfragment上面的导航按钮
				// 设置开启或关闭抽屉
				MainActivity.this.toggle();
				break;
			case R.id.SendAshamed:
				if (Model.MYUSERINFO != null) {
					Intent intent = new Intent(MainActivity.this,
							CreateVoiceActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(MainActivity.this,
							LoginActivity.class);
					startActivity(intent);
				}
				break;
			default:
				break;
			}
		}
	}

	private class MyNearFragmentCallBack implements NearFragmentCallBack {
		@Override
		public void callback(int flag) {
			switch (flag) {
			case R.id.Near_Back:
				MainActivity.this.toggle();
				break;
			case R.id.Near_Seting:
				break;
			case R.id.Near_More:
				break;
			default:
				break;
			}
		}
	}
	
	private class MyFriendFragmentCallBack implements FriendFragmentCallback {
		@Override
		public void callback(int flag) {
			switch (flag) {
			case R.id.friend_back:
				MainActivity.this.toggle();
				break;
			default:
				break;
			}
		}
	}
	
	private class MyEventFragmentCallBack implements EventFragmentCallback {
		@Override
		public void callback(int flag) {
			switch (flag) {
			case R.id.event_back:
				MainActivity.this.toggle();
				break;
			default:
				break;
			}
		}
	}

	// 设置leftview控件的默认背景色
	private void createleftviewbg() {
//		mLeftHot.setBackgroundResource(R.drawable.leftview_list_bg);
////		mLeftNice.setBackgroundResource(R.drawable.leftview_list_bg);
////		mLeftPicture.setBackgroundResource(R.drawable.leftview_list_bg);
//		mLeftScrip.setBackgroundResource(R.drawable.leftview_list_bg);
//		mLeftNear.setBackgroundResource(R.drawable.leftview_list_bg);
//		mLeftContact.setBackgroundResource(R.drawable.leftview_list_bg);
//		mLeftEvent.setBackgroundResource(R.drawable.leftview_list_bg);
////		mLeftCross.setBackgroundResource(R.drawable.leftview_list_bg);
////		mLeftCheck.setBackgroundResource(R.drawable.leftview_list_bg);
		
		TypefaceHelper.typeface(mMenuVoiceText, tfInit.LightSlab());
		TypefaceHelper.typeface(mMenuContactText, tfInit.LightSlab());
		TypefaceHelper.typeface(mMenuGossipText, tfInit.LightSlab());
		TypefaceHelper.typeface(mMenuMapText, tfInit.LightSlab());
		
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if (Model.MYUSERINFO != null) {
			mMenuUserName.setText(Model.MYUSERINFO.getUname());
			mMenuUserEmail.setText(Model.MYUSERINFO.getUemail());
		} else {
			mMenuUserName.setText("Login please...");
			mMenuUserEmail.setText("example@example.com");
		}
		// createFargment(fragmentFlag);
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (Model.MYUSERINFO != null) {
			mMenuUserName.setText(Model.MYUSERINFO.getUname());
			mMenuUserEmail.setText(Model.MYUSERINFO.getUemail());
			
			//与当前头像名称比较，判断头像是否变更，若有变更，则更新
			if (Model.MYUSERINFO.getUhead() != null 
					&& !Model.MYUSERINFO.getUhead().equals("null")
					&& !Model.MYUSERINFO.getUhead().equals(curUserHeadName)) 
			{
				loadImgHeadImg = new LoadImg(MainActivity.this);
				Bitmap bit = loadImgHeadImg.loadImage(mMenuUserImage,
						Model.USERHEADURL + Model.MYUSERINFO.getUhead(),
						new ImageDownloadCallBack() {
							public void onImageDownload(ImageView imageView,
									Bitmap bitmap) {
								mMenuUserImage.setImageBitmap(bitmap);
							}
						});
				if (bit != null) {
					mMenuUserImage.setImageBitmap(bit);
				}
			}
			
//			KFIMInterfaces.setVCardField(MainActivity.this, "NICKNAME",
//					Model.MYUSERINFO.getUname());
		} else {
			//每次返回主界面，都要检查是否登陆，若没登陆，就跳转到登陆界面
			Intent intent = new Intent(MainActivity.this, LoginActivity.class);
			startActivity(intent);
			MainActivity.this.finish();
		}
	}

	private void login() {
		// 检查 用户名/密码 是否已经设置,如果已经设置，则登录
		if (!"".equals(mSettingsMgr.getUsername())
				&& !"".equals(mSettingsMgr.getPassword()))
			KFIMInterfaces.login(this, mSettingsMgr.getUsername(),
					mSettingsMgr.getPassword());
		// 设置个人资料
		KFIMInterfaces.setVCardField(MainActivity.this, "NICKNAME",
		Model.MYUSERINFO.getUname());
	}
	
	// 退出登录
		private void logout() {
			if (Model.MYUSERINFO != null) {
				new AlertDialog.Builder(this)
				.setMessage("Log out?")
				.setPositiveButton("Confirm",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Model.MYUSERINFO = null;
								SharedPreferences sp = getSharedPreferences(
										"UserInfo", MODE_PRIVATE);
								Editor editor = sp.edit();
								editor.clear();
								editor.commit();
								// 退出登录
								KFIMInterfaces
										.Logout(MainActivity.this);
								Intent intent = new Intent(
										MainActivity.this,
										LoginActivity.class);
								startActivity(intent);
								finish();
							}
						}).setNegativeButton("Cancel", null).create()
				.show();
			}
		}

}
