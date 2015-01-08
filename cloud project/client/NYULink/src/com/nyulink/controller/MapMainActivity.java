package com.nyulink.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nyu.cloudcomputing.nyulink.R;
import com.nyulink.adapter.RadiusAdapter;
import com.nyulink.fragment.NearFragment.NearFragmentCallBack;
import com.nyulink.info.NearByKid;
import com.nyulink.info.UserInfo;
import com.nyulink.model.Model;
import com.nyulink.net.ThreadPoolUtils;
import com.nyulink.thread.HttpPostThread;
import com.nyulink.utils.AlertDialogManager;
import com.nyulink.utils.LoadImg;
import com.nyulink.utils.MyJson;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nyulink.utils.LoadImg.ImageDownloadCallBack;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

public class MapMainActivity extends FragmentActivity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, LocationListener, OnClickListener {

	private static final int GPS_ERRORDIALOG_REQUEST = 9001;
	private static final String DEFAULT = "N/A";
	private LinearLayout mapBack;
	GoogleMap mMap;

	@SuppressWarnings("unused")
	private static final double JALGAON_LAT = 40.694131,
			JALGAON_LNG = -73.986927;
	private List<Marker> markerList = new ArrayList<Marker>();
	private List<NearByKid> nearKidList = new ArrayList<NearByKid>();
	private List<UserInfo> nearUserList = new ArrayList<UserInfo>();
	private Hashtable<String,UserInfo> nearUserHt = new Hashtable<String,UserInfo>();
	
	private static final float DEFAULTZOOM = 15;
	LocationClient mLocationClient;
	Marker marker;
	private MyJson myJson = new MyJson();
	// Circle shape;
	private String locality;
    Spinner spinnerRadius;
//	private ArrayAdapter<String> adapter = null;
//	private static final String[] searchRadius = {"1", "2", "3"};
	private double radius = 1.00d;
	LoadImg loadImgHeadImg;

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (servicesOK()) {
			setContentView(R.layout.activity_mapmain);
			spinnerRadius = (Spinner)findViewById(R.id.spinnerradius);
			mapBack = (LinearLayout) findViewById(R.id.Map_Back);
			mapBack.setOnClickListener(new OnClickListener() 
	         {

	            @Override
	            public void onClick(View v)
	            {
//					Intent intenttoMain = new Intent(MapMainActivity.this,MainActivity.class);
//					startActivity(intenttoMain);
	            	finish();

	            }
	        });

//			adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,searchRadius);
			
//			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			
//			spinnerRadius.setAdapter(adapter);
//			spinnerRadius.setVisibility(View.VISIBLE);
//			spinnerRadius.setOnItemSelectedListener(new OnItemSelectedListener(){



		/*		@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
				//	result.setText("Select search radius: "+((TextView)arg1).getText());
					radius = Double.parseDouble(arg0.getItemAtPosition(arg2).toString());
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
			});*/

			final List<String> radiusList = new ArrayList<String>();
			radiusList.add("1");
			radiusList.add("5");
			radiusList.add("10");
			radiusList.add("Radius (miles)");// set hint here .
			
			spinnerRadius.setAdapter(new RadiusAdapter(getApplicationContext(), radiusList));
			spinnerRadius.setVisibility(View.VISIBLE);
			spinnerRadius.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					if(arg0.getItemAtPosition(arg2).toString().length()<=2){
						radius = Double.parseDouble(arg0.getItemAtPosition(arg2).toString());
					}else{
						radius = 1.0;
					}
					
						
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					
					
				}
			});
			
			spinnerRadius.setSelection(radiusList.size()-1);
			
			
			
			if (initMap()) {
				Toast.makeText(this, "Ready to map!", Toast.LENGTH_SHORT)
						.show();
				gotoLocation(JALGAON_LAT, JALGAON_LNG, DEFAULTZOOM);
				mMap.setMyLocationEnabled(true);
				mLocationClient = new LocationClient(this, this, this);
				mLocationClient.connect();
			} else {
				Toast.makeText(this, "Map not available!", Toast.LENGTH_SHORT)
						.show();
			}
		} else {
			setContentView(R.layout.activity_nomapservice);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mapmain, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mapTypeNone:
			mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
			break;
		case R.id.mapTypeNormal:
			mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			break;
		case R.id.mapTypeSatellite:
			mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			break;
		case R.id.mapTypeTerrain:
			mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			break;
		case R.id.mapTypeHybrid:
			mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			break;
		case R.id.gotoCurrentLocation:
			gotoCurrentLocation();
			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	public boolean servicesOK() {
		int isAvailables = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (isAvailables == ConnectionResult.SUCCESS) {
			return true;
		} else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailables)) {
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailables,
					this, GPS_ERRORDIALOG_REQUEST);
			dialog.show();
		} else {
			Toast.makeText(this, "Google service not available",
					Toast.LENGTH_SHORT).show();
		}
		return false;
	}

	private boolean initMap() {
		if (mMap == null) {
			SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map);
			mMap = mapFrag.getMap();

			if (mMap != null) {
				mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

					@Override
					public View getInfoWindow(Marker arg0) {
						return null;
					}

					@Override
					public View getInfoContents(Marker marker) {
						View v = getLayoutInflater().inflate(
								R.layout.mapinfo_window, null);
						ImageView mUserCamera = (ImageView) v.findViewById(R.id.imageView1);
						TextView tvLocality = (TextView) v.findViewById(R.id.tv_locality);
						TextView tvLat = (TextView) v.findViewById(R.id.tv_lat);
						TextView tvLng = (TextView) v.findViewById(R.id.tv_lng);
						TextView tvSnippet = (TextView) v.findViewById(R.id.tv_snippet);

						LatLng ll = marker.getPosition();
						
						String title = marker.getTitle();
						//设置地址（地点标记）或者人名（附近的人）
						tvLocality.setText(title);
						
						String snippet = marker.getSnippet();
						//若为人,将地址给snippet
						if(snippet != null && !snippet.equals("")){
							tvSnippet.setText(snippet);
							tvSnippet.setVisibility(View.VISIBLE);
							
						}else{
							//若为地点标记，则隐藏头像
							mUserCamera.setVisibility(View.GONE);
						}
						
						tvLat.setText("latitude: " + ll.latitude);
						tvLng.setText("longitude: " + ll.longitude);
						
						return v;
					}
				});

				mMap.setOnMapLongClickListener(new OnMapLongClickListener() {

					@Override
					public void onMapLongClick(LatLng ll) {
						Geocoder gc = new Geocoder(MapMainActivity.this);
						List<Address> list = null;

						try {
							list = gc.getFromLocation(ll.latitude,
									ll.longitude, 1);
						} catch (IOException e) {
							e.printStackTrace();
							return;
						}
						
						Address add = list.get(0);
						String localityStr = (add.getAddressLine(0) != null ? add.getAddressLine(0) : " ")
								+ (add.getAddressLine(1) != null ? add.getAddressLine(1) : " ")
								+ (add.getAddressLine(2) != null ? add.getAddressLine(2) : " ").trim();

						
						MapMainActivity.this.setMarker(localityStr, ll.latitude, ll.longitude);

					}
				});

				//mMap.setOnMarkerClickListener(new OnMarkerClickListener() {

//					@Override
//					public boolean onMarkerClick(Marker marker) {
//						String msg = marker.getTitle() + " ("
//								+ marker.getPosition().latitude + ","
//								+ marker.getPosition().longitude + ")";
//						Toast.makeText(MapMainActivity.this, msg,
//								Toast.LENGTH_LONG).show();
//						return false;
//					}
//				});
				
				mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			        @Override
			        public void onInfoWindowClick(final Marker marker) {
			        	//地点标记时，直接返回
			        	if(marker.getSnippet() == null || marker.getSnippet().equals("")){
			        		return;
			        	}
			        	
			        	//自己不可以添加自己
			        	if(marker.getTitle().equals(Model.MYUSERINFO.getUname())){
			        		Toast.makeText(MapMainActivity.this, "can't add yourself!", 1).show();
			        		return;
			        	}
			        	
			        	AlertDialog.Builder builder = new AlertDialog.Builder(
								MapMainActivity.this);
						builder.setTitle("Add friend?");
						builder.setPositiveButton("Confirm",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										// 这里添加点击确定后的逻辑
										addFriend(marker.getTitle());
										dialog.cancel();
										
									}
								});
						builder.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										// 这里添加点击取消后的逻辑
							
										dialog.cancel();
									}
								});
						builder.create().show();
			        }
			    });
//
//				Handler hand = new Handler() {
//					public void handleMessage(android.os.Message msg) {
//						super.handleMessage(msg);
//						
//					};
//				};				

	        	
				mMap.setOnMarkerDragListener(new OnMarkerDragListener() {

					@Override
					public void onMarkerDragStart(Marker arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onMarkerDragEnd(Marker marker) {
						Geocoder gc = new Geocoder(MapMainActivity.this);
						List<Address> list = null;
						LatLng ll = marker.getPosition();
						try {
							list = gc.getFromLocation(ll.latitude,
									ll.longitude, 1);
						} catch (IOException e) {
							e.printStackTrace();
							return;
						}

						Address add = list.get(0);
						marker.setTitle(add.getLocality());
						marker.setSnippet(add.getCountryName());
						marker.showInfoWindow();
					}

					@Override
					public void onMarkerDrag(Marker arg0) {
						// TODO Auto-generated method stub

					}
				});

			}
		}
		return (mMap != null);
	}

	private void gotoLocation(double lat, double lng) {
		LatLng ll = new LatLng(lat, lng);
		CameraUpdate update = CameraUpdateFactory.newLatLng(ll);
		mMap.moveCamera(update);
	}

	private void gotoLocation(double lat, double lng, float zoom) {
		LatLng ll = new LatLng(lat, lng);
		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
		mMap.moveCamera(update);
	}

	public void geoLocate(View v) {
		try {
			hideSoftKeyboard(v);

			EditText et = (EditText) findViewById(R.id.editText1);
			String location = et.getText().toString().trim();
			double lat = 0;
			double lng = 0;
			Geocoder gc = new Geocoder(this);
			List<Address> list = null;
			locality = "";
			if(location.equals("")){
				Location currentLocation = mLocationClient.getLastLocation();
				if (currentLocation == null) {
					Toast.makeText(this, "Current location isn't available",
							Toast.LENGTH_SHORT).show();
				} else {
					lat = currentLocation.getLatitude();
					lng = currentLocation.getLongitude();
					list = gc.getFromLocation(lat, lng, 1);
					if(list == null && list.size() == 0){
						Toast.makeText(this, "please relocate", Toast.LENGTH_LONG).show();
						return;
					}
					Address add = list.get(0);
					//locality = add.getLocality();
					
					locality = (add.getAddressLine(0) != null ? add.getAddressLine(0) : " ")
							+ (add.getAddressLine(1) != null ? add.getAddressLine(1) : " ")
							+ (add.getAddressLine(2) != null ? add.getAddressLine(2) : " ").trim();
					if(!locality.equals("")){
						Toast.makeText(this, locality, Toast.LENGTH_LONG).show();
					}
				}
			}else{
				list = gc.getFromLocationName(location, 1);
				if(list == null && list.size() == 0){
					Toast.makeText(this, "error location, please input again", Toast.LENGTH_LONG).show();
					return;
				}
				Address add = list.get(0);
				//locality = add.getLocality();
				
				locality = (add.getAddressLine(0) != null ? add.getAddressLine(0) : " ")
						+ (add.getAddressLine(1) != null ? add.getAddressLine(1) : " ")
						+ (add.getAddressLine(2) != null ? add.getAddressLine(2) : " ").trim();
				if(!locality.equals("")){
					Toast.makeText(this, locality, Toast.LENGTH_LONG).show();
				}

				lat = add.getLatitude();
				lng = add.getLongitude();
			}
			gotoLocation(lat, lng, DEFAULTZOOM);
			if (marker != null) {
				marker.remove();
			}
			
			//mark on map
			
			//这个标记是个地点
			MarkerOptions options = new MarkerOptions().title(locality).position(
					new LatLng(lat, lng));
			marker = mMap.addMarker(options);

			//以上面那个地点为中心，查找附近的人
			retriveNearbyList(String.valueOf(lat), String.valueOf(lng),
					String.valueOf(radius));
			
		} catch (Exception e) {
			Toast.makeText(this, "error location, please check network!", Toast.LENGTH_LONG).show();
		}
	}

	private void hideSoftKeyboard(View v) {
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}

	@Override
	protected void onStop() {
		super.onStop();
		MapStateManager mgr = new MapStateManager(this);
		mgr.saveMapState(mMap);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MapStateManager mgr = new MapStateManager(this);
		CameraPosition position = mgr.getSavedCameraPosition();
		if (position != null) {
			CameraUpdate update = CameraUpdateFactory
					.newCameraPosition(position);
			mMap.moveCamera(update);
			// This is part of the answer to the code challenge
			mMap.setMapType(mgr.getSavedMapType());
		}
	}

	protected void gotoCurrentLocation() {
		Location currentLocation = mLocationClient.getLastLocation();
		if (currentLocation == null) {
			Toast.makeText(this, "Current location isn't available",
					Toast.LENGTH_SHORT).show();
		} else {
			LatLng ll = new LatLng(currentLocation.getLatitude(),
					currentLocation.getLongitude());
			CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll,
					DEFAULTZOOM);
			mMap.animateCamera(update);
		}
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		Toast.makeText(this, "Connected to location service",
				Toast.LENGTH_SHORT).show();
		LocationRequest request = LocationRequest.create();
		request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		request.setInterval(5000);
		request.getFastestInterval();
		mLocationClient.requestLocationUpdates(request, this);
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub

	}

	private double distance(double lat, double lng, double Latitude,
			double Longitude) {

		double earthRadius = 3958.75; // in miles, change to 6371 for kilometer
										// output

		double dLat = Math.toRadians(Latitude - lat);
		double dLng = Math.toRadians(Longitude - lng);

		double sindLat = Math.sin(dLat / 2);
		double sindLng = Math.sin(dLng / 2);

		double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
				* Math.cos(Math.toRadians(lat))
				* Math.cos(Math.toRadians(Latitude));

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		double dist = earthRadius * c;

		return dist; // output distance, in MILES
	}
	
	
	private void setMarker(String locality, double lat,
			double lng) {
		LatLng ll = new LatLng(lat, lng);

		MarkerOptions options = new MarkerOptions()
				.title(locality)
				.position(new LatLng(lat, lng))
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_mapmarker))
				// .icon(BitmapDescriptorFactory.defaultMarker())
				.anchor(.5f, .5f).draggable(true);

		if (marker != null) {
			removeEverything();
		}

		marker = mMap.addMarker(options);
	}
	

	private void removeEverything() {
		marker.remove();
		marker = null;
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
	}

	//look for near people
	private void retriveNearbyList(String latitude, String longitude,
			String radius) {
		// latitude longitude function(address);
		String Json = "{\"latitude\":\"" + latitude + "\","
				+ "\"longitude\":\"" + longitude + "\"," + "\"radius\":\""
				+ radius + "\"}";
		ThreadPoolUtils.execute(new HttpPostThread(hand,
				Model.RETRIEVE_NEARBY_LIST, Json));
	}

	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				Toast.makeText(MapMainActivity.this,
						"Server error! Request failed!", 1).show();
			} else if (msg.what == 100) {
				Toast.makeText(MapMainActivity.this, "Server no response", 1)
						.show();
			} else if (msg.what == 200) {
				String result = (String) msg.obj;
				if (result == null) {
					Toast.makeText(MapMainActivity.this,
							"Server no response, please check network", 1)
							.show();
					return;
				}
				
				nearUserList = myJson.getNearUserList(result);
				if (nearUserList != null && nearUserList.size() > 0){
					for (Marker oldMarker : markerList) {
						oldMarker.remove();
					}
					markerList.clear();
					
					nearUserHt.clear();
					String localityStr = "";
					Geocoder gc = new Geocoder(MapMainActivity.this);
					for (UserInfo info : nearUserList) {
						nearUserHt.put(info.getUname(), info);
						List<Address> list;
						try {
							list = gc.getFromLocation(info.getUlat(), info.getUlng(), 1);
							if(list == null && list.size() == 0){
								
							}else{
								Address add = list.get(0);
								localityStr = (add.getAddressLine(0) != null ? add.getAddressLine(0) : " ")
										+ (add.getAddressLine(1) != null ? add.getAddressLine(1) : " ")
										+ (add.getAddressLine(2) != null ? add.getAddressLine(2) : " ").trim();
							}
							
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
						
						MarkerOptions options = new MarkerOptions()
								.title(info.getUname())
								.snippet(localityStr)
								.position(new LatLng(info.getUlat(), info.getUlng()))
								.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));

						Marker markerNew = mMap.addMarker(options);
						markerNew.showInfoWindow();
						markerList.add(markerNew);
					}
				}
				
				nearKidList = myJson.getNearKidList(result);
			}
		};
	};
	
	public void addFriend(String receiveusername){
		if(receiveusername.equals("")){
			AlertDialogManager.showAlert(this, "ALERT", "please enter username!", "OK");
			return;
		}
		String form = "{\"command\":\"" + "send invitation" + "\"," 
				+ "\"senduid\":\"" + Model.MYUSERINFO.getUserid() + "\","
				+ "\"receiveusername\":\"" + receiveusername + "\"}";
		ThreadPoolUtils.execute(new HttpPostThread(hand_addFriend, Model.FRIEND, form)); 
	}

	Handler hand_addFriend = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			
			if (msg.what == 200) {
				String result = (String) msg.obj;
				
				if (result != null && result.equals("ok")) {
					Toast.makeText(MapMainActivity.this, "Successfully send invitation!", 1).show();
				}else if(result != null && result.equals("not exist username")){
					Toast.makeText(MapMainActivity.this, "username doesn't exists!", 1).show();
				}else if(result != null && result.equals("exist friend")){
					Toast.makeText(MapMainActivity.this, "friend exists!", 1).show();
				}else if(result != null && result.equals("exist invitation")){
					Toast.makeText(MapMainActivity.this, "invitation exists!", 1).show();
				}
			}
		}
	};



	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}