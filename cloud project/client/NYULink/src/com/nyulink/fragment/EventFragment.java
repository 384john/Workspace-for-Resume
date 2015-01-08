package com.nyulink.fragment;

import java.util.ArrayList;
import java.util.List;

import com.nyu.cloudcomputing.nyulink.R;
import com.nyulink.adapter.NearAdapter;
import com.nyulink.controller.UserInfoActivity;
import com.nyulink.info.UserInfo;
import com.nyulink.model.Model;
import com.nyulink.myview.MyListView;
import com.nyulink.myview.MyListView.OnRefreshListener;
import com.nyulink.net.ThreadPoolUtils;
import com.nyulink.thread.HttpGetThread;
import com.nyulink.thread.HttpPostThread;
import com.nyulink.tools.EventsListActivity;
import com.nyulink.utils.MyJson;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class EventFragment extends Fragment implements OnClickListener {

	private EventFragmentCallback mEventFragmentCallback;
	private View view;
	private Context ctx;
	private EditText mEventCity, mEventDateStart, mEventDateEnd, mEventKeyword;
	private LinearLayout mLinearLayout, mEventBack;
	private TextView HomeNoValue;
	private Button mSearchEvent;
	
	private String city, dateStart, dateEnd, keyword;
	
	private MyJson myJson = new MyJson();
	private List<UserInfo> list = new ArrayList<UserInfo>();
	private Button ListBottem = null;
	private int mStart = 0;
	private int mEnd = 5;
	private String url = null;
	private boolean flag = true;
	private boolean loadflag = false;
	private boolean listBottemFlag = true;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.activity_event, null);
		ctx = view.getContext();
		initView();
		
		return view;
	}

	private void initView() {
		mLinearLayout = (LinearLayout) view.findViewById(R.id.HomeGroup);
		mEventBack = (LinearLayout) view.findViewById(R.id.event_back);
		HomeNoValue = (TextView) view.findViewById(R.id.HomeNoValue);
		
		mEventCity = (EditText) view.findViewById(R.id.event_city);
		mEventDateStart = (EditText) view.findViewById(R.id.event_date_start);
		mEventDateEnd = (EditText) view.findViewById(R.id.event_date_end);
		mEventKeyword = (EditText) view.findViewById(R.id.event_keyword);
		mSearchEvent = (Button) view.findViewById(R.id.btn_search_event);
		
		mEventBack.setOnClickListener(this);
		mSearchEvent.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.event_back:
			mEventFragmentCallback.callback(R.id.event_back);
			break;
		case R.id.btn_search_event:
			city = mEventCity.getText().toString().trim();
			dateStart = mEventDateStart.getText().toString().trim();
			dateEnd = mEventDateEnd.getText().toString().trim();
			keyword = mEventKeyword.getText().toString().trim();
			
			if (city.equals("") && dateStart.equals("") && dateEnd.equals("") && keyword.equals("")) {
				Toast.makeText(ctx, "No input!", Toast.LENGTH_LONG).show();
				return;
			}
			
			Intent i = new Intent(ctx, EventsListActivity.class);
			i.putExtra("city", city);
			i.putExtra("dateStart", dateStart);
			i.putExtra("dateEnd", dateEnd);
			i.putExtra("keyword", keyword);
			startActivity(i);
			
			break;
		default:
			break;
		}
	}

	public void setCallBack(EventFragmentCallback mEventFragmentCallback) {
		this.mEventFragmentCallback = mEventFragmentCallback;
	}

	public interface EventFragmentCallback {
		public void callback(int flag);
	}

	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				Toast.makeText(ctx, "Address not found", 1).show();
				listBottemFlag = true;
			} else if (msg.what == 100) {
				Toast.makeText(ctx, "Communication failed", 1).show();
				listBottemFlag = true;
			} else if (msg.what == 200) {
				String result = (String) msg.obj;
				if (result != null) {
					List<UserInfo> newList = myJson.getNearUserList(result);
					if (newList != null) {
						Log.e("KidBuddies", "newList=" + newList.size()
								+ "  list=" + list.size());
						if (newList.size() == 5) {
							ListBottem.setVisibility(View.VISIBLE);
							mStart += 5;
							mEnd += 5;
						} else if (newList.size() == 0) {
							if (list.size() == 0)
								HomeNoValue.setVisibility(View.VISIBLE);
							ListBottem.setVisibility(View.GONE);
							Toast.makeText(ctx, "There is no more...", 1).show();
						} else {
							ListBottem.setVisibility(View.GONE);
						}
						if (!loadflag) {
							list.removeAll(list);
						}
						for (UserInfo info : newList) {
							list.add(info);
						}
						listBottemFlag = true;
					} else {
						if (list.size() == 0)
							HomeNoValue.setVisibility(View.VISIBLE);
					}
				}
				mLinearLayout.setVisibility(View.VISIBLE);
				loadflag = true;
			}
		};
	};
	
	Handler hand_event = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			
			if (msg.what == 404) {
				Toast.makeText(ctx, "Server IP not found.", Toast.LENGTH_SHORT).show();
			} else if (msg.what == 100) {
				Toast.makeText(ctx, "Communication failed.", Toast.LENGTH_SHORT).show();
			} else if (msg.what == 200) {
				String result = (String) msg.obj;
				
				if (result != null && result.equals("ok")) {
					
					Toast.makeText(ctx, "Successfully", Toast.LENGTH_SHORT).show();
				}
			}
			
		};
	};
}
