package com.nyulink.controller;

import com.nyu.cloudcomputing.nyulink.R;
import com.nyulink.controller.PhotoAct.IMGCallBack1;
import com.nyulink.model.Model;
import com.nyulink.net.ThreadPoolUtils;
import com.nyulink.thread.HttpPostThread;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CreateChildInfoActivity extends Activity {
	private ImageView mClose, mCreate, mChildCamera;
	private EditText mName, mAge, mHobbies, mDetail;
	private TextView mGender;

	private String data = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_createchildinfo);
		initView();
	}

	private void initView() {
		mClose = (ImageView) findViewById(R.id.close_create);
		mCreate = (ImageView) findViewById(R.id.create_activity);
		mChildCamera = (ImageView) findViewById(R.id.create_activity_childcamera);

		mGender = (TextView) findViewById(R.id.create_activity_gender);
		mName = (EditText) findViewById(R.id.create_activity_name);
		mAge = (EditText) findViewById(R.id.create_activity_age);
		mHobbies = (EditText) findViewById(R.id.create_activity_hobbies);
		mDetail = (EditText) findViewById(R.id.create_activity_detail);

		MyOnClick my = new MyOnClick();
		mClose.setOnClickListener(my);
		mCreate.setOnClickListener(my);
		mChildCamera.setOnClickListener(my);
		mGender.setOnClickListener(my);

		PhotoAct.setIMGcallback(new IMGCallBack1() {

			@Override
			public void callback(String data) {
				CreateChildInfoActivity.this.data = data;
				byte[] bitmapArray = Base64.decode(
						CreateChildInfoActivity.this.data, Base64.DEFAULT);
				Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
						bitmapArray.length);
				try {
					mChildCamera.setImageBitmap(bitmap);
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		});
	}

	class MyOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int mId = v.getId();
			switch (mId) {
			case R.id.close_create:
				CreateChildInfoActivity.this.finish();
				break;
			case R.id.create_activity:
				if (Model.MYUSERINFO != null) {
					saveMeth();
				} else {
					Intent intent = new Intent(CreateChildInfoActivity.this,
							LoginActivity.class);
					startActivity(intent);
				}
				break;
			case R.id.create_activity_childcamera:
				Intent intent2 = new Intent(CreateChildInfoActivity.this,
						PhotoAct.class);
				startActivity(intent2);
				break;
			case R.id.create_activity_gender:
				Builder builder = new Builder(CreateChildInfoActivity.this);
//				builder.setTitle("please select gender");
				final String[] items = new String[] { "FEMALE", "MALE" };
				DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg1, int arg0) {
						// TODO Auto-generated method stub
						if (arg0 == DialogInterface.BUTTON_POSITIVE) {
							arg1.cancel();
						}
						switch (arg0) {
						case 0:
							mGender.setText("FEMALE");
							break;
						case 1:
							mGender.setText("MALE");
							break;
						}
					}
				};
				builder.setItems(items, dialog);
				builder.setPositiveButton("cancel", dialog);
				AlertDialog alertDialog = builder.create();
				alertDialog.show();
				break;
			case R.id.save_button:
				saveMeth();
				break;
			default:
				break;
			}
		}

	}

	private void saveMeth() {

		String uid = Model.MYUSERINFO.getUserid();
		String kimg = "";// Activity picture
		if (!data.equalsIgnoreCase("")) {
			kimg = System.currentTimeMillis() + ".png";// Activity picture
		}

		String gender = mGender.getText().toString().equals("male") ? "1" : "0";
		String name = mName.getText().toString();
		String age = mAge.getText().toString();
		String hobbies = mHobbies.getText().toString();
		String detail = mDetail.getText().toString();

		String Json = "{\"uid\":\"" + uid + "\"," + "\"kimg\":\"" + kimg
				+ "\"," + "\"gender\":\"" + gender + "\"," + "\"name\":\""
				+ name + "\"," + "\"age\":\"" + age + "\"," + "\"hobbies\":\""
				+ hobbies + "\"," + "\"detail\":\"" + detail + "\"}";
		ThreadPoolUtils.execute(new HttpPostThread(hand, Model.ADDCHILD, Json,
				data));
	}

	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 200) {
				String result = (String) msg.obj;
				if (result != null && result.equals("ok")) {
					Toast.makeText(CreateChildInfoActivity.this,
							"Successfully created!", 1).show();
				}

				Intent intent = new Intent();
				setResult(3, intent);
				CreateChildInfoActivity.this.finish();
			}
		}
	};

}

// Handler hand1 = new Handler() {
// public void handleMessage(android.os.Message msg) {
// super.handleMessage(msg);
// if (msg.what == 200) {
// String result = (String) msg.obj;
// if (result != null && result.equals("ok")) {
// Toast.makeText(CreateChildInfoActivity.this, "Successfully created!",
// 1).show();
// CreateChildInfoActivity.this.finish();
// }
// }
// };
// };

