package com.nyulink.adapter;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import com.nyu.cloudcomputing.nyulink.R;
import com.nyulink.info.UserInfo;
import com.nyulink.model.Model;
import com.nyulink.net.MyGet;
import com.nyulink.utils.LoadImg;
import com.nyulink.utils.MyJson;
import com.nyulink.utils.LoadImg.ImageDownloadCallBack;
import com.appkefu.lib.interfaces.KFIMInterfaces;
import com.appkefu.lib.ui.entity.KFConversationEntity;
import com.appkefu.lib.utils.KFSLog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ConversationAdapter extends BaseAdapter {

	private static final String TAG = ConversationAdapter.class.getSimpleName();

	private MyJson myJson = new MyJson();
	private Context context;
	private List<KFConversationEntity> list;
	private LoadImg loadImgHeadImg;
	UserInfo info = new UserInfo();

	public ConversationAdapter(Context context, List<KFConversationEntity> list) {
		this.context = context;
		this.list = list;
		loadImgHeadImg = new LoadImg(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void remove(KFConversationEntity entity) {
		list.remove(entity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.xiaozhitiao_listvile,
					null);
			holder.icon = (ImageView) convertView
					.findViewById(R.id.recent_userhead);
			holder.name = (TextView) convertView.findViewById(R.id.recent_name);
			holder.date = (TextView) convertView.findViewById(R.id.recent_time);
			holder.msg = (TextView) convertView.findViewById(R.id.recent_msg);
			holder.count = (TextView) convertView
					.findViewById(R.id.recent_new_num);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final KFConversationEntity entity = list.get(position);

		KFSLog.d("conv.id " + entity.getId() + " conv.name:" + entity.getName());

		if (entity.getName().equals("admin")) {
			holder.name.setText("KidBuddies service");
		} else if (entity.getName().equals("")) {
			holder.name.setText("System inform");
		} else if (entity.getName().equals(
				"appkefu_subscribe_notification_message")) {
			holder.name.setText("Verification message");
		} else {
			// 首先获取昵称，如果昵称未设置，则显示对方用户名
			String nickname = KFIMInterfaces.getOtherNickname(entity.getName());
			if (nickname == null) {
				nickname = entity.getName().replace(Model.APPKEY, "");
			}
			holder.name.setText(nickname);
		}

		holder.icon.setImageResource(R.drawable.my_photo_for_drawer);

		holder.name.setTextColor(Color.BLACK);
		holder.date.setText(entity.getTime());
		holder.msg.setText(entity.getMsg());

		if (entity.getCount() > 0) {
			holder.count.setText(entity.getCount() + "");
			holder.count.setTextColor(Color.BLACK);
			holder.count.setVisibility(View.VISIBLE);
		} else {
			holder.count.setVisibility(View.INVISIBLE);
		}

		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (entity.getType().equals("subscribe")) {
					// Intent intent = new Intent(context,
					// KFSubscribeNotificationActivity.class);
					// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					// context.startActivity(intent);
				} else// type == chat
				{
					String jid = entity.getName() + "@appkefu.com";
					Log.d(TAG, "chatWith:" + jid);
					entity.getName().replace(Model.APPKEY, "");
					KFIMInterfaces.startChatWithUser(context,// 上下文Context
							entity.getName(),// 对方用户名
							entity.getName().replace(Model.APPKEY, ""));// 自定义会话窗口标题
				}
			}
		});

		convertView
				.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

					@Override
					public void onCreateContextMenu(ContextMenu menu, View v,
							ContextMenuInfo menuInfo) {
						Log.d(TAG, "setOnCreateContextMenuListener");
					}
				});

		return convertView;
	}

	static class ViewHolder {
		public ImageView icon;
		public TextView name;
		public TextView date;
		public TextView msg;
		public TextView count;
	}

}
