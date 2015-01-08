package com.nyulink.adapter;

import java.util.List;

import com.nyu.cloudcomputing.nyulink.R;
import com.nyulink.controller.VoiceDetailActivity;
import com.nyulink.controller.LoginActivity;
import com.nyulink.info.CommentInfo;
import com.nyulink.model.Model;
import com.appkefu.lib.interfaces.KFIMInterfaces;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DetailListAdapter extends BaseAdapter {

	private List<CommentInfo> list;
	private Context ctx;

	public DetailListAdapter(Context ctx, List<CommentInfo> list) {
		this.ctx = ctx;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		Holder hold;
		if (arg1 == null) {
			hold = new Holder();
			arg1 = View.inflate(ctx, R.layout.detail_list_item, null);
			hold.UserName = (TextView) arg1
					.findViewById(R.id.Detail_Item_UserName);
			hold.Num = (TextView) arg1.findViewById(R.id.Detail_Item_Num);
			hold.Value = (TextView) arg1.findViewById(R.id.Detail_Item_Value);
			arg1.setTag(hold);
		} else {
			hold = (Holder) arg1.getTag();
		}
		hold.UserName.setText(list.get(arg0).getUname());
		hold.Num.setText("" + (arg0 + 1));
		hold.Value.setText(list.get(arg0).getCvalue());
		hold.UserName.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
//				Toast.makeText(ctx, list.get(arg0).getUname(), 1).show();
				if (Model.MYUSERINFO != null) {
					KFIMInterfaces.startChatWithUser(ctx,// 上下文Context
							Model.APPKEY + list.get(arg0).getUname(),// 对方用户名
							list.get(arg0).getUname());// 自定义会话窗口标题
				} else {
					Toast.makeText(ctx, "Please login first!", 1).show();
				}
			}
		});
		return arg1;
	}

	static class Holder {
		TextView UserName;
		TextView Num;
		TextView Value;
	}

}
