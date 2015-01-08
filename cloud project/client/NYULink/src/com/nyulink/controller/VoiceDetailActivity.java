package com.nyulink.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.nyu.cloudcomputing.nyulink.R;
import com.nyulink.adapter.ActivityUsersAdapter;
import com.nyulink.adapter.DetailListAdapter;
import com.nyulink.adapter.FriendAdapter;
import com.nyulink.info.CommentInfo;
import com.nyulink.info.FriendInfo;
import com.nyulink.info.UserInfo;
import com.nyulink.info.VoiceInfo;
import com.nyulink.model.Model;
import com.nyulink.myview.MyDetailsListView;
import com.nyulink.myview.MyListView;
import com.nyulink.myview.MyListView.OnRefreshListener;
import com.nyulink.net.ThreadPoolUtils;
import com.nyulink.thread.HttpGetThread;
import com.nyulink.thread.HttpPostThread;
import com.nyulink.utils.LoadImg;
import com.nyulink.utils.MyJson;
import com.nyulink.utils.LoadImg.ImageDownloadCallBack;
import com.appkefu.lib.interfaces.KFIMInterfaces;

public class VoiceDetailActivity extends Activity {

	private VoiceInfo info = null;
	private LoadImg loadImg;
	Bitmap bit = null;
	private MyJson myJson = new MyJson();
	private ImageView Detail_Back, Detail_SenComment;
	private ImageView Detail_UserHead;
	private TextView Detail_UserName;
	
	private TextView Detail_Title, Detail_Startdate, Detail_Starttime, Detail_Enddate, Detail_Endtime, Detail_Spot,
			Detail_Maxnum, Detail_Desc, Detail_joined_num;
	
	private MyDetailsListView Detail_List;
	private LinearLayout Detail__progressBar;
	private TextView Detail_CommentsNum,Detail_CommentCount;
	private List<CommentInfo> list = new ArrayList<CommentInfo>();
	private DetailListAdapter mAdapter = null;
	private ActivityUsersAdapter mAdapter_activityUsers = null;
	private List<UserInfo> list_activityUsers = new ArrayList<UserInfo>();
	
	private Button ListBottem = null;
	private int mStart = 0;
	private int mEnd = 5;
	private String url = null;
	private boolean flag = true;
	private boolean listBottemFlag = true;
	private boolean loadflag = false;
	private boolean isJoin = false;
	
	private MyDetailsListView myListView;
	private LinearLayout mLinearLayout;
	private Button Detail_join_activity;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_voice_detail);
		myListView = new MyDetailsListView(this);
		
		Intent intent = getIntent();
		Bundle bund = intent.getBundleExtra("value");
		info = (VoiceInfo) bund.getSerializable("AshamedInfo");
		loadImg = new LoadImg(VoiceDetailActivity.this);
		initView();
		addInformation();
		addImg();
		
		myListView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		myListView.setDivider(null);
		mLinearLayout.addView(myListView);
		mAdapter_activityUsers = new ActivityUsersAdapter(VoiceDetailActivity.this, list_activityUsers);
		myListView.setAdapter(mAdapter_activityUsers);
		myListView.setOnItemClickListener(new MainListOnItemClickListener());
		
		
		mAdapter = new DetailListAdapter(VoiceDetailActivity.this, list);
		ListBottem = new Button(VoiceDetailActivity.this);
		ListBottem.setText("Click to load");
		ListBottem.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (flag && listBottemFlag) {
					url = Model.COMMENTS + "qid=" + info.getQid() + "&start="
							+ mStart + "&end=" + mEnd;
					ThreadPoolUtils.execute(new HttpGetThread(hand, url));
					listBottemFlag = false;
				} else if (!listBottemFlag)
					Toast.makeText(VoiceDetailActivity.this, "Loading...", 1)
							.show();
			}
		});
		Detail_List.addFooterView(ListBottem, null, false);
		ListBottem.setVisibility(View.GONE);
		Detail_List.setAdapter(mAdapter);
		String endParames = Model.COMMENTS + "qid=" + info.getQid() + "&start="
				+ mStart + "&end=" + mEnd;
		ThreadPoolUtils.execute(new HttpGetThread(hand, endParames));
		
//		String form = "{\"command\":\"" + "all friend" + "\"," 
//				+ "\"uid\":\"" + Model.MYUSERINFO.getUserid() + "\"}";
//				
//		ThreadPoolUtils.execute(new HttpPostThread(hand_activityUsers, Model.NEAR, form));
		
	}

	private void initView() {
		MyOnClickListner myOnclick = new MyOnClickListner();
		Detail_Back = (ImageView) findViewById(R.id.Detail_Back);
		Detail_SenComment = (ImageView) findViewById(R.id.Detail_SenComment);   // top-right comment button
		Detail_UserHead = (ImageView) findViewById(R.id.Detail_UserHead);
		Detail_UserName = (TextView) findViewById(R.id.Detail_UserName);
		Detail_Title = (TextView) findViewById(R.id.detail_activity_title);
		Detail_Startdate = (TextView) findViewById(R.id.detail_activity_startdate);
		Detail_Starttime = (TextView) findViewById(R.id.detail_activity_starttime);
		Detail_Enddate = (TextView) findViewById(R.id.detail_activity_enddate);
		Detail_Endtime = (TextView) findViewById(R.id.detail_activity_endtime);
		Detail_Spot = (TextView) findViewById(R.id.detail_activity_spot);
		Detail_Maxnum = (TextView) findViewById(R.id.detail_activity_maxnum);
		Detail_Desc = (TextView) findViewById(R.id.detail_activity_desc);
		
		Detail_CommentCount = (TextView) findViewById(R.id.btn_comment_num);
		
		Detail_List = (MyDetailsListView) findViewById(R.id.Detail_List);
		Detail__progressBar = (LinearLayout) findViewById(R.id.Detail__progressBar);
		Detail_CommentsNum = (TextView) findViewById(R.id.Detail_CommentsNum);
		
		mLinearLayout = (LinearLayout) findViewById(R.id.HomeGroup);
		Detail_join_activity = (Button) findViewById(R.id.btn_join_activity);
		Detail_joined_num = (TextView) findViewById(R.id.btn_joined_num);
		
		
		
		
		Detail_Back.setOnClickListener(myOnclick);
		Detail_SenComment.setOnClickListener(myOnclick);
		
		Detail_UserHead.setOnClickListener(myOnclick);
		Detail_join_activity.setOnClickListener(myOnclick);
		
		
//		Detail_MainImg.setOnClickListener(myOnclick);
//		Detail_Up.setOnClickListener(myOnclick);
//		Detail_Down.setOnClickListener(myOnclick);
//		Detail_Share.setOnClickListener(myOnclick);
		
		Detail_CommentCount.setVisibility(View.GONE);
	}

	private class MyOnClickListner implements View.OnClickListener {
		public void onClick(View arg0) {
			String form = "";
			int mID = arg0.getId();
			switch (mID) {
			case R.id.Detail_Back:
				VoiceDetailActivity.this.finish();
				break;
			case R.id.Detail_SenComment:
				if (Model.MYUSERINFO != null) {
					Intent intent = new Intent(VoiceDetailActivity.this,
							SendCommentActivity.class);
					Bundle bund = new Bundle();
					bund.putSerializable("AshamedInfo", info);
					intent.putExtra("value", bund);
					startActivity(intent);
				} else {
					Intent intent = new Intent(VoiceDetailActivity.this,
							LoginActivity.class);
					startActivity(intent);
				}

				break;
			case R.id.Detail_UserHead:
				form = "{\"command\":\"" + "isFriendAndGetUserInfoById" + "\"," 
						+ "\"uid\":\"" + Model.MYUSERINFO.getUserid() + "\","
						+ "\"fid\":\"" + info.getUid() + "\"}";
				ThreadPoolUtils.execute(new HttpPostThread(hand_friend, Model.INFOMANAGER, form));
				break;
			case R.id.btn_join_activity:
				if(isJoin){
					form = "{\"command\":\"" + "quitActivity" + "\"," 
							+ "\"uid\":\"" + Model.MYUSERINFO.getUserid() + "\","
							+ "\"aid\":\"" + info.getQid() + "\"}";
					
				}else{
					form = "{\"command\":\"" + "joinActivity" + "\"," 
							+ "\"uid\":\"" + Model.MYUSERINFO.getUserid() + "\","
							+ "\"aid\":\"" + info.getQid() + "\"}";
				}
				ThreadPoolUtils.execute(new HttpPostThread(hand_joinOrQuit, Model.ACTIVITYUSERS, form));
				break;
			default:
				break;
			}
		}
	}

	

	private void addImg() {
		if (!info.getUhead().equals("")) {
			Detail_UserHead.setTag(Model.USERHEADURL + info.getUhead());
			bit = loadImg.loadImage(Detail_UserHead,
					Model.USERHEADURL + info.getUhead(),
					new ImageDownloadCallBack() {
						public void onImageDownload(ImageView imageView,
								Bitmap bitmap) {
							Detail_UserHead.setImageBitmap(bitmap);
						}
					});
			if (bit != null) {
				Detail_UserHead.setImageBitmap(bit);
			}
		} else {
			Detail_UserHead.setImageResource(R.drawable.default_users_avatar);
		}
	}

	private void addInformation() {
		Detail_UserName.setText(info.getUname());
		
		Detail_Title.setText(info.getTitle());
		Detail_Startdate.setText(info.getStartdate());
		Detail_Starttime.setText(info.getStarttime());
		Detail_Enddate.setText(info.getEnddate());
		Detail_Endtime.setText(info.getEndtime());
		Detail_Spot.setText(info.getSpot());
		Detail_Maxnum.setText(info.getMaxnum());
		Detail_Desc.setText(info.getDetail());
		Detail_CommentCount.setText(info.getCommentcount());
		
		setJoinActivityUsersInfo();
	}
	
	
	private void setJoinActivityUsersInfo() {
		String form = "{\"command\":\"" + "getJoinActivityUsersInfo" + "\"," 
				+ "\"uid\":\"" + Model.MYUSERINFO.getUserid() + "\","
				+ "\"aid\":\"" + info.getQid() + "\"}";
		ThreadPoolUtils.execute(new HttpPostThread(hand_JoinActivityUsersInfo, Model.ACTIVITYUSERS, form));
	}
	
	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				Toast.makeText(VoiceDetailActivity.this, "Server error! Request failed!", 1)
						.show();
				listBottemFlag = true;
			} else if (msg.what == 100) {
				Toast.makeText(VoiceDetailActivity.this, "Server no response", 1).show();
				listBottemFlag = true;
			} else if (msg.what == 200) {
				String result = (String) msg.obj;
				if (result != null) {
					List<CommentInfo> newList = myJson
							.getAhamedCommentsList(result);
					if (newList != null) {
						if (newList.size() == 5) {
							Detail_List.setVisibility(View.VISIBLE);
							ListBottem.setVisibility(View.VISIBLE);
							mStart += 5;
							mEnd += 5;
						} else if (newList.size() == 0) {
							if (list.size() == 0)
								Detail_CommentsNum.setVisibility(View.VISIBLE);
							ListBottem.setVisibility(View.GONE);
						} else {
							Detail_List.setVisibility(View.VISIBLE);
							ListBottem.setVisibility(View.GONE);
						}
						
						if (!loadflag) {
							list.removeAll(list);
						}
						for (CommentInfo info : newList) {
							list.add(info);
						}
						listBottemFlag = true;
					} else {
						Detail_CommentsNum.setVisibility(View.VISIBLE);
					}
				}
				Detail__progressBar.setVisibility(View.GONE);
				mAdapter.notifyDataSetChanged();
				loadflag = true;
			}
		};

	};
	
	Handler hand_friend = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			
			if (msg.what == 404) {
				Toast.makeText(VoiceDetailActivity.this, "Server IP not found.", Toast.LENGTH_SHORT).show();
			} else if (msg.what == 100) {
				Toast.makeText(VoiceDetailActivity.this, "Communication failed.", Toast.LENGTH_SHORT).show();
			} else if (msg.what == 200) {
				String result = (String) msg.obj;
				if (result != null && !result.trim().equals("")) {
					String isFriend = result.substring(0,1);
					String userInfoStr = result.substring(1,result.length());
					List<UserInfo> newList = myJson.getNearUserList(userInfoStr);
					if(newList != null && newList.size() != 0 ){
						Intent intent = new Intent(VoiceDetailActivity.this, UserInfoActivity.class);
						Bundle bund = new Bundle();
						bund.putSerializable("UserInfo", newList.get(0));
						intent.putExtra("value", bund);
						intent.putExtra("isFriend", isFriend.equalsIgnoreCase("y") ? true : false);
						startActivity(intent);
					}
				}
			}
			
		};
	};
	
	Handler hand_joinOrQuit = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			
			if (msg.what == 404) {
				Toast.makeText(VoiceDetailActivity.this, "Server IP not found.", Toast.LENGTH_SHORT).show();
			} else if (msg.what == 100) {
				
			} else if (msg.what == 200) {
				String result = (String) msg.obj;
				if (result != null && !result.trim().equals("")) {
					String isjoinStr = result.trim().substring(0,1);
					String userInfoStr = result.trim().substring(1,result.trim().length());
					if(isjoinStr.equals("y")){
						isJoin = true;
						Detail_join_activity.setText("QUIT");
						Toast.makeText(VoiceDetailActivity.this, "Successfull Joined!", Toast.LENGTH_SHORT).show();
					}else{
						isJoin = false;
						Detail_join_activity.setText("JOIN");
						Toast.makeText(VoiceDetailActivity.this, "Successfull QUIT!", Toast.LENGTH_SHORT).show();
					}
					
					List<UserInfo> newList = myJson.getNearUserList(userInfoStr);
					list_activityUsers.removeAll(list_activityUsers);
					for (UserInfo info : newList) {
						list_activityUsers.add(info);
					}
						
					mAdapter_activityUsers.notifyDataSetChanged();
					Detail_joined_num.setText(String.valueOf(list_activityUsers.size()));
					if(list_activityUsers.size() > 0){
						mLinearLayout.setVisibility(View.VISIBLE);
					}else{
						mLinearLayout.setVisibility(View.GONE);
					}
				}else{
					mLinearLayout.setVisibility(View.GONE);
				}
				
			}
			
		};
	};
	
	
	Handler hand_JoinActivityUsersInfo = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 200) {
				String result = (String) msg.obj;
				if (result != null && !result.trim().equals("")) {
					String isjoinStr = result.trim().substring(0,1);
					String userInfoStr = result.trim().substring(1,result.trim().length());
					if(isjoinStr.equals("y")){
						isJoin = true;
						Detail_join_activity.setText("QUIT");
					}else{
						isJoin = false;
						Detail_join_activity.setText("JOIN");
					}
					
					List<UserInfo> newList = myJson.getNearUserList(userInfoStr);
					list_activityUsers.removeAll(list_activityUsers);
					for (UserInfo info : newList) {
						list_activityUsers.add(info);
					}
						
					mAdapter_activityUsers.notifyDataSetChanged();
					Detail_joined_num.setText(String.valueOf(list_activityUsers.size()));
					if(list_activityUsers.size() > 0){
						mLinearLayout.setVisibility(View.VISIBLE);
					}else{
						mLinearLayout.setVisibility(View.GONE);
					}
					
				}else{
					mLinearLayout.setVisibility(View.GONE);
				}
			}
			
		};
	};
	
	
	
	

	@Override
	protected void onRestart() {
		mStart = 0;
		mEnd = 5;
		loadflag = false;
		String endParames = Model.COMMENTS + "qid=" + info.getQid() + "&start="
				+ mStart + "&end=" + mEnd;
		ThreadPoolUtils.execute(new HttpGetThread(hand, endParames));
		super.onRestart();
	}
	
	private class MainListOnItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			Intent intent = new Intent(VoiceDetailActivity.this, UserInfoActivity.class);
			Bundle bund = new Bundle();
			bund.putSerializable("UserInfo", list_activityUsers.get(arg2));
			intent.putExtra("value", bund);
			intent.putExtra("isFriend", true);
			startActivity(intent);
		}
	}
	
	
	
	
	
	
	

}
