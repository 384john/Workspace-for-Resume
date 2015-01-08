package com.nyulink.adapter;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nyu.cloudcomputing.nyulink.R;
import com.nyulink.info.FriendInfo;
import com.nyulink.info.UserInfo;
import com.nyulink.model.Model;
import com.nyulink.net.ThreadPoolUtils;
import com.nyulink.thread.HttpPostThread;
import com.nyulink.utils.LoadImg;
import com.nyulink.utils.LoadImg.ImageDownloadCallBack;
import com.appkefu.lib.interfaces.KFIMInterfaces;

public class ActivityUsersAdapter extends BaseAdapter {
	
	private List<UserInfo> userInfoList;
	private Context context;
	private LoadImg headImg;
	private int selectRow = 0;

	public ActivityUsersAdapter(Context context, List<UserInfo> userInfoList) {
		this.context = context;
		this.userInfoList = userInfoList;
		headImg = new LoadImg(context);
	}

	@Override
	public int getCount() {
		return userInfoList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return userInfoList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		final Holder holder;
		
		if (arg1 == null) {
			holder = new Holder();
			
			arg1 = View.inflate(context, R.layout.item_activityusers, null);
			holder.friend_username = (TextView) arg1.findViewById(R.id.friend_username);
			holder.friend_gender = (TextView) arg1.findViewById(R.id.friend_gender);
			//holder.friend_status = (TextView) arg1.findViewById(R.id.friend_status);
			holder.friend_img = (ImageView) arg1.findViewById(R.id.friend_img);
			//holder.friend_operation = (Button) arg1.findViewById(R.id.friend_operation);
			
			arg1.setTag(holder);
		} else {
			holder = (Holder) arg1.getTag();
		}
		
		holder.friend_username.setText(userInfoList.get(arg0).getUname());
		holder.friend_gender.setBackgroundResource(R.drawable.nearby_gender_male);
		holder.friend_gender.setText("0");
		holder.friend_gender.setVisibility(View.VISIBLE);
		//holder.friend_status.setVisibility(View.VISIBLE);
		holder.friend_img.setImageResource(R.drawable.default_users_avatar);
//		holder.friend_operation
//		.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				KFIMInterfaces.startChatWithUser(context,// 上下文Context
//						Model.APPKEY + userInfoList.get(arg0).getUname(),// 对方用户名
//						userInfoList.get(arg0).getUname());// 自定义会话窗口标题
//			}
//		});
		
		if (!userInfoList.get(arg0).getUage().equalsIgnoreCase("null")) {
			holder.friend_gender.setText(userInfoList.get(arg0).getUage());
			
			if (userInfoList.get(arg0).getUsex().equals("0")) {
				holder.friend_gender.setBackgroundResource(R.drawable.nearby_gender_female);
			} else {
				holder.friend_gender.setBackgroundResource(R.drawable.nearby_gender_male);
			}
		} else {
			holder.friend_gender.setVisibility(View.GONE);
		}
		
//		if (!userInfoList.get(arg0).getUexplain().equalsIgnoreCase("null")) {
//			holder.friend_status.setText(userInfoList.get(arg0).getUexplain());
//		} else {
//			holder.friend_status.setVisibility(View.GONE);
//		}
		
		if (userInfoList.get(arg0).getUhead().equalsIgnoreCase("")) {
			holder.friend_img.setImageResource(R.drawable.default_users_avatar);
		} else {
			holder.friend_img.setTag(Model.USERHEADURL + userInfoList.get(arg0).getUhead());
			
			Bitmap bitHead = headImg.loadImage(holder.friend_img, Model.USERHEADURL + userInfoList.get(arg0).getUhead(),
				new ImageDownloadCallBack() {
					@Override
					public void onImageDownload(ImageView imageView, Bitmap bitmap) {
						if (holder.friend_img.getTag().equals(Model.USERHEADURL + userInfoList.get(arg0).getUhead())) {
							holder.friend_img.setImageBitmap(bitmap);
						}
					}
				});
			
			if (bitHead != null) {
				holder.friend_img.setImageBitmap(bitHead);
			}
		}

		return arg1;
	}

	static class Holder {
		TextView friend_username;
		TextView friend_gender;
		//TextView friend_status;
		ImageView friend_img;
		//Button friend_operation;
	}
}
