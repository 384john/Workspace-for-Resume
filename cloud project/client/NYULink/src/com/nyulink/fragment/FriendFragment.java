package com.nyulink.fragment;

import java.util.ArrayList;
import java.util.List;

import com.nyu.cloudcomputing.nyulink.R;
import com.nyulink.adapter.FriendAdapter;
import com.nyulink.adapter.InvitationAdapter;
import com.nyulink.controller.VoiceDetailActivity;
import com.nyulink.controller.SearchPeopleActivity;
import com.nyulink.controller.UserInfoActivity;
import com.nyulink.controller.UserInfoEditActivity;
import com.nyulink.info.FriendInfo;
import com.nyulink.info.InvitationInfo;
import com.nyulink.info.UserInfo;
import com.nyulink.model.Model;
import com.nyulink.myview.MyListView;
import com.nyulink.myview.MyListView.OnRefreshListener;
import com.nyulink.net.ThreadPoolUtils;
import com.nyulink.thread.HttpGetThread;
import com.nyulink.thread.HttpPostThread;
import com.nyulink.utils.MyJson;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FriendFragment extends Fragment implements OnClickListener,OnItemLongClickListener {

	//private String friendUrl = Model.FRIEND;
	private FriendFragmentCallback mFriendFragmentCallback;
	private View view;
	private Context ctx;
	private LinearLayout mBack, mLinearLayout, load_progressBar;
	private LinearLayout mLinearLayout_invitation, load_progressBar_invitation;
	private ImageView mTopMenuOne, mTopMenuTwo;
	private TextView HomeNoValue;
	private TextView HomeNoValue_invitation;
	private ImageView mOpenAdd;
	private MyListView myListView;
	private MyListView myListView_invitation;
	private FriendAdapter mAdapter = null;
	private InvitationAdapter mAdapter_invitation = null;
	private MyJson myJson = new MyJson();
	private List<FriendInfo> friendList = new ArrayList<FriendInfo>();
	private List<InvitationInfo> invitationList = new ArrayList<InvitationInfo>();
	private Button ListBottem = null;
	private Button ListBottem_invitation = null;
	
	private int topMeunFlag = 1;
	private int mStart = 0;
	private int mEnd = 5;
	private boolean flag = true;
	private boolean loadflag = false;
	private boolean listBottemFlag = true;
	
	private int mStart_invitation = 0;
	private int mEnd_invitation = 5;
	private boolean flag_invitation = true;
	private boolean loadflag_invitation = false;
	private boolean listBottemFlag_invitation = true;
	private int selectRow=0;
	

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.activity_friend, null);
		ctx = view.getContext();
		myListView = new MyListView(ctx);
		myListView_invitation = new MyListView(ctx);
		initView();
		return view;
	}

	private void initView() {
		mBack = (LinearLayout) view.findViewById(R.id.friend_back);
		mTopMenuOne = (ImageView) view.findViewById(R.id.TopMenuOne);
		mTopMenuTwo = (ImageView) view.findViewById(R.id.TopMenuTwo);
		mOpenAdd = (ImageView) view.findViewById(R.id.btn_open_add);
		
		load_progressBar = (LinearLayout) view.findViewById(R.id.load_progressBar);
		mLinearLayout = (LinearLayout) view.findViewById(R.id.HomeGroup);
		HomeNoValue = (TextView) view.findViewById(R.id.HomeNoValue);
		
		load_progressBar_invitation = (LinearLayout) view.findViewById(R.id.load_progressBar_invitation);
		mLinearLayout_invitation = (LinearLayout) view.findViewById(R.id.HomeGroup_invitation);
		HomeNoValue_invitation = (TextView) view.findViewById(R.id.HomeNoValue_invitation);
		
		
		myListView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		myListView.setDivider(null);
		mLinearLayout.addView(myListView);
		
		mBack.setOnClickListener(this);
		
		
		
		myListView_invitation.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		myListView_invitation.setDivider(null);
		mLinearLayout_invitation.addView(myListView_invitation);
		


		mTopMenuOne.setOnClickListener(this);
		mTopMenuTwo.setOnClickListener(this);
		mOpenAdd.setOnClickListener(this);
		
		
		
		mAdapter = new FriendAdapter(ctx, friendList);
		ListBottem = new Button(ctx);
		
		ListBottem.setText("Click to load more");
		ListBottem.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (flag && listBottemFlag) {
					String form = "{\"command\":\"" + "all friend" + "\"," 
									+ "\"uid\":\"" + Model.MYUSERINFO.getUserid() + "\","
									+ "\"start\":\"" + mStart + "\","
									+ "\"end\":\"" + mEnd + "\"}";
					ThreadPoolUtils.execute(new HttpPostThread(hand, Model.FRIEND, form));
					
					listBottemFlag = false;
				} else if (!listBottemFlag)
					Toast.makeText(ctx, "Loading...", Toast.LENGTH_SHORT).show();
			}
		});
		
		myListView.addFooterView(ListBottem, null, false);
		ListBottem.setVisibility(View.GONE);
		myListView.setAdapter(mAdapter);
		myListView.setOnItemClickListener(new MainListOnItemClickListener());
		
		myListView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				if (loadflag == true) {
					loadflag = false;
					mStart = 0;
					mEnd = 5;
					ListBottem.setVisibility(View.GONE);
					
					String form = "{\"command\":\"" + "all friend" + "\"," 
							+ "\"uid\":\"" + Model.MYUSERINFO.getUserid() + "\","
							+ "\"start\":\"" + mStart + "\","
							+ "\"end\":\"" + mEnd + "\"}";
					ThreadPoolUtils.execute(new HttpPostThread(hand, Model.FRIEND, form));
					
					
				} else {
					Toast.makeText(ctx, "Loading...Please do not refresh.", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		
		mAdapter_invitation = new InvitationAdapter(ctx, invitationList);
		ListBottem_invitation = new Button(ctx);
		
		ListBottem_invitation.setText("Click to load more");
		ListBottem_invitation.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (flag_invitation && listBottemFlag_invitation) {
					String form = "{\"command\":\"" + "all invitation" + "\"," 
							+ "\"receiveuid\":\"" + Model.MYUSERINFO.getUserid() + "\","
							+ "\"start\":\"" + mStart_invitation + "\","
							+ "\"end\":\"" + mEnd_invitation + "\"}";
					ThreadPoolUtils.execute(new HttpPostThread(hand_invitation, Model.FRIEND, form));
					
					listBottemFlag_invitation = false;
				} else if (!listBottemFlag_invitation)
					Toast.makeText(ctx, "Loading...", Toast.LENGTH_SHORT).show();
			}
		});
		
		myListView_invitation.addFooterView(ListBottem_invitation, null, false);
		ListBottem_invitation.setVisibility(View.GONE);
		myListView_invitation.setAdapter(mAdapter_invitation);
		myListView_invitation.setOnItemClickListener(new MainListOnItemClickListener_invitation());
		
		myListView_invitation.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				if (loadflag_invitation == true) {
					mStart_invitation = 0;
					mEnd_invitation = 5;
					ListBottem_invitation.setVisibility(View.GONE);
					
					String form = "{\"command\":\"" + "all invitation" + "\"," 
							+ "\"receiveuid\":\"" + Model.MYUSERINFO.getUserid() + "\","
							+ "\"start\":\"" + mStart_invitation + "\","
							+ "\"end\":\"" + mEnd_invitation + "\"}";
					ThreadPoolUtils.execute(new HttpPostThread(hand_invitation, Model.FRIEND, form));
					
					loadflag_invitation = false;
				} else {
					Toast.makeText(ctx, "Loading...Please do not refresh.", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
	
		
		myListView.setOnItemLongClickListener(new OnItemLongClickListener() {  
			@Override
	        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {  
	            System.out.println("Item LONG clicked. Position:" + position); 
	            selectRow = position - 1;
	            AlertDialog.Builder builder = new AlertDialog.Builder(
	            		ctx);
				// builder.setIcon(R.drawable.icon);
				builder.setTitle("Are you delete the friend?");
				builder.setPositiveButton("Sure",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// 这里添加点击确定后的逻辑
								//showDialog("你选择了确定");
								
								String form = "{\"command\":\"" + "del friend" + "\"," 
										+ "\"uid\":\"" + Model.MYUSERINFO.getUserid() + "\","
										+ "\"fid\":\"" + friendList.get(selectRow).getUserid() + "\"}";
								ThreadPoolUtils.execute(new HttpPostThread(hand_delFriend, Model.FRIEND, form)); 
								
							}
						});
				builder.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// 这里添加点击确定后的逻辑
								//showDialog("你选择了取消");
							}
						});
				builder.create().show();
	            
	            return true;  
	        }
	    });  
		
		
		createTextColor();
		switch (topMeunFlag) {
			case 1:
//				mTopMenuOne.setTextColor(Color.WHITE);
//				mTopMenuOne.setBackgroundResource(R.drawable.top_tab_active);
				mTopMenuOne.setEnabled(false);
				mTopMenuTwo.setEnabled(true);
				break;
			case 2:
//				mTopMenuTwo.setTextColor(Color.WHITE);
//				mTopMenuTwo.setBackgroundResource(R.drawable.top_tab_active);
				mTopMenuTwo.setEnabled(false);
				mTopMenuOne.setEnabled(true);
				break;
		}
		
		createListModel();
	 
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.friend_back:
				mFriendFragmentCallback.callback(R.id.friend_back);
				break;
			case R.id.TopMenuOne:
				createTextColor();
//				mTopMenuOne.setTextColor(Color.WHITE);
//				mTopMenuOne.setBackgroundResource(R.drawable.top_tab_active);
				mTopMenuOne.setEnabled(false);
				mTopMenuTwo.setEnabled(true);
				
				if (topMeunFlag != 1) {
					topMeunFlag = 1;
					createListModel();
				}
				break;
			case R.id.TopMenuTwo:
				createTextColor();
//				mTopMenuTwo.setTextColor(Color.WHITE);
//				mTopMenuTwo.setBackgroundResource(R.drawable.top_tab_active);
				mTopMenuTwo.setEnabled(false);
				mTopMenuOne.setEnabled(true);
				
				if (topMeunFlag != 2) {
					topMeunFlag = 2;
					createListModel();
				}
				break;
			case R.id.btn_open_add:
				Intent intent = new Intent(ctx, SearchPeopleActivity.class);
				startActivity(intent);
				
				break;
			default:
				break;
		}

	}
	
	@SuppressWarnings("deprecation")
	private void createTextColor() {
		Drawable background = new BitmapDrawable();
//		mTopMenuOne.setTextColor(Color.parseColor("#815F3D"));
//		mTopMenuTwo.setTextColor(Color.parseColor("#815F3D"));
		mTopMenuOne.setBackgroundDrawable(background);
		mTopMenuTwo.setBackgroundDrawable(background);
		HomeNoValue.setVisibility(View.GONE);
		HomeNoValue_invitation.setVisibility(View.GONE);
	}
	
	
	private void createListModel() {
		String form = "";
		
		if(topMeunFlag == 1){
			
			mLinearLayout_invitation.setVisibility(View.GONE);
			HomeNoValue_invitation.setVisibility(View.GONE);
			load_progressBar_invitation.setVisibility(View.GONE);
			
			ListBottem.setVisibility(View.GONE);
			mLinearLayout.setVisibility(View.GONE);
			load_progressBar.setVisibility(View.VISIBLE);
			loadflag = false;
			mStart = 0;
			mEnd = 5;
			
			form = "{\"command\":\"" + "all friend" + "\"," 
					+ "\"uid\":\"" + Model.MYUSERINFO.getUserid() + "\","
					+ "\"start\":\"" + mStart + "\","
					+ "\"end\":\"" + mEnd + "\"}";
			ThreadPoolUtils.execute(new HttpPostThread(hand, Model.FRIEND, form)); 
		}else {
			mLinearLayout.setVisibility(View.GONE);
			HomeNoValue.setVisibility(View.GONE);
			load_progressBar.setVisibility(View.GONE);
			
			ListBottem_invitation.setVisibility(View.GONE);
			mLinearLayout_invitation.setVisibility(View.GONE);
			load_progressBar_invitation.setVisibility(View.VISIBLE);
			loadflag_invitation = false;
			mStart_invitation = 0;
			mEnd_invitation = 5;
			form = "{\"command\":\"" + "all invitation" + "\"," 
					+ "\"receiveuid\":\"" + Model.MYUSERINFO.getUserid() + "\","
					+ "\"start\":\"" + mStart_invitation + "\","
					+ "\"end\":\"" + mEnd_invitation + "\"}";
			ThreadPoolUtils.execute(new HttpPostThread(hand_invitation, Model.FRIEND, form)); 
		}
		
	}

	private class MainListOnItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			Intent intent = new Intent(ctx, UserInfoActivity.class);
			Bundle bund = new Bundle();
			bund.putSerializable("UserInfo", myJson.getUserInfoFromFriendInfo(friendList.get(arg2 - 1)));
			intent.putExtra("value", bund);
			intent.putExtra("isFriend", true);
			startActivity(intent);
		}
	}
	
//	private class MainListOnLongItemClickListener implements OnItemLonClickListener {
//		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//			Intent intent = new Intent(ctx, UserInfoActivity.class);
//			Bundle bund = new Bundle();
//			bund.putSerializable("UserInfo", myJson.getUserInfoFromFriendInfo(friendList.get(arg2 - 1)));
//			intent.putExtra("value", bund);
//			intent.putExtra("isFriend", true);
//			startActivity(intent);
//		}
//	}
//	

		  
	 
	private class MainListOnItemClickListener_invitation implements OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			Intent intent = new Intent(ctx, UserInfoActivity.class);
			Bundle bund = new Bundle();
			bund.putSerializable("UserInfo", myJson.getUserInfoFromInvitationInfo(invitationList.get(arg2 - 1)));
			intent.putExtra("value", bund);
			intent.putExtra("isFriend", false);
			startActivity(intent);
		}
	}
	

	public void setCallBack(FriendFragmentCallback mFriendFragmentCallback) {
		this.mFriendFragmentCallback = mFriendFragmentCallback;
	}

	public interface FriendFragmentCallback {
		public void callback(int flag);
	}

	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			
			if (msg.what == 404) {
				Toast.makeText(ctx, "Server IP not found.", Toast.LENGTH_SHORT).show();
				listBottemFlag = true;
			} else if (msg.what == 100) {
				Toast.makeText(ctx, "Communication failed.", Toast.LENGTH_SHORT).show();
				listBottemFlag = true;
			} else if (msg.what == 200) {
				String result = (String) msg.obj;
				
				if (result != null) {
					List<FriendInfo> newList = myJson.getFriendList(result);
					
					if (newList != null) {
						Log.e("KidBuddies", "newList=" + newList.size() + " list=" + friendList.size());
						
						if (newList.size() == 5) {
							ListBottem.setVisibility(View.VISIBLE);
							mStart += 5;
							mEnd += 5;
						} else if (newList.size() == 0) {
							if (friendList.size() == 0) {
								HomeNoValue.setVisibility(View.VISIBLE);
							}
							
							ListBottem.setVisibility(View.GONE);
							Toast.makeText(ctx, "There is no more...", Toast.LENGTH_LONG).show();
						} else {
							ListBottem.setVisibility(View.GONE);
						}
						
						if (!loadflag) {
							friendList.removeAll(friendList);
						}
						
						for (FriendInfo info : newList) {
							friendList.add(info);
						}
						
						listBottemFlag = true;
					} else {
						if (friendList.size() == 0) {
							HomeNoValue.setVisibility(View.VISIBLE);
						}
					}
				}
				
				mLinearLayout.setVisibility(View.VISIBLE);
				load_progressBar.setVisibility(View.GONE);
				myListView.onRefreshComplete();
				mAdapter.notifyDataSetChanged();
				loadflag = true;
			}
			
			mAdapter.notifyDataSetChanged();
		};
	};
	
	
	
	Handler hand_invitation = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			
			if (msg.what == 404) {
				Toast.makeText(ctx, "Server IP not found.", Toast.LENGTH_SHORT).show();
				listBottemFlag_invitation = true;
			} else if (msg.what == 100) {
				Toast.makeText(ctx, "Communication failed.", Toast.LENGTH_SHORT).show();
				listBottemFlag_invitation = true;
			} else if (msg.what == 200) {
				String result = (String) msg.obj;
				
				if (result != null) {
					//List<UserInfo> newList = myJson.getNearUserList(result);
					List<InvitationInfo> newList = myJson.getInvitationList(result);
					if (newList != null) {
						Log.e("KidBuddies", "newList=" + newList.size() + " list=" + invitationList.size());
						
						if (newList.size() == 5) {
							ListBottem_invitation.setVisibility(View.VISIBLE);
							mStart_invitation += 5;
							mEnd_invitation += 5;
						} else if (newList.size() == 0) {
							if (invitationList.size() == 0) {
								HomeNoValue_invitation.setVisibility(View.VISIBLE);
							}
							
							ListBottem_invitation.setVisibility(View.GONE);
							Toast.makeText(ctx, "There is no more...", Toast.LENGTH_LONG).show();
						} else {
							ListBottem_invitation.setVisibility(View.GONE);
						}
						
						if (!loadflag_invitation) {
							invitationList.removeAll(invitationList);
						}
						
						for (InvitationInfo info : newList) {
							invitationList.add(info);
						}
						
						listBottemFlag_invitation = true;
					} else {
						if (invitationList.size() == 0) {
							HomeNoValue_invitation.setVisibility(View.VISIBLE);
						}
					}
				}
				
				mLinearLayout_invitation.setVisibility(View.VISIBLE);
				load_progressBar_invitation.setVisibility(View.GONE);
				myListView_invitation.onRefreshComplete();
				mAdapter_invitation.notifyDataSetChanged();
				loadflag_invitation = true;
			}
			
			mAdapter_invitation.notifyDataSetChanged();
		};
	};
	
	
	Handler hand_delFriend = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			
			if (msg.what == 404) {
				Toast.makeText(ctx, "Server IP not found.", Toast.LENGTH_SHORT).show();
			} else if (msg.what == 100) {
				Toast.makeText(ctx, "Communication failed.", Toast.LENGTH_SHORT).show();
			} else if (msg.what == 200) {
				String result = (String) msg.obj;
				
				if (result != null && result.equals("ok")) {
					friendList.remove(selectRow);
					mAdapter.notifyDataSetChanged();
					Toast.makeText(ctx, "Successfully deled", Toast.LENGTH_SHORT).show();
				}
			}
			
		};
	};


	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
}
