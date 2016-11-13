package com.creativeinnovations.mea;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.creativeinnovations.mea.network.SoapServicePath;
import com.degree.hitaishin.UI.ImageAdapter;
import com.degree.hitaishin.connection.WebServiceConnection;
import com.degree.hitaishin.connection.WebServiceNaming;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.hitaishin.notification.Config;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class HomeActivity extends Activity implements OnClickListener {
	private ListView filterGridView;
	ArrayList<String> headingList;
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	SharedPreferences shared;
	public static String regId;
	GoogleCloudMessaging gcm;
	ImageAdapter adapter;
	Intent _intent;

	TextView tvwhatsnew, tvpopular, tvlive;
	LinearLayout llllive;
	ImageView ivlive;
	ImageButton btnSearch;
	AnimatedActivity parentActivity;
	ImageView ivcarrace, ivrealstate;
	private ArrayList<HashMap<String, String>> categoriesList;
	
	
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home_activity);
		filterGridView = (ListView) findViewById(R.id.filterGridView);
		tvwhatsnew = (TextView) findViewById(R.id.tvwhatsnew);
		tvwhatsnew.setOnClickListener(this);
		tvpopular = (TextView) findViewById(R.id.tvpopular);
		tvpopular.setOnClickListener(this);
		tvlive = (TextView) findViewById(R.id.tvlive);
		tvlive.setOnClickListener(this);
		llllive = (LinearLayout) findViewById(R.id.llllive);
		llllive.setOnClickListener(this);
		ivlive = (ImageView) findViewById(R.id.ivlive);
		ivlive.setOnClickListener(this);
		btnSearch = (ImageButton) findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(this);
		ivcarrace = (ImageView) findViewById(R.id.ivcarrace);
		ivcarrace.setOnClickListener(this);
		ivrealstate = (ImageView) findViewById(R.id.ivrealstate);
		ivrealstate.setOnClickListener(this);
		headingList = new ArrayList<String>();
		headingList.add("Real Estate");
		headingList.add("Event");
						
 
		View headerView = getLayoutInflater().inflate( R.layout.gridview_header , null );
		shared = this.getSharedPreferences("whatlite", MODE_WORLD_WRITEABLE);

		categoriesList = new ArrayList<HashMap<String, String>>();
		adapter = new ImageAdapter(HomeActivity.this, categoriesList);
		checkPlayServices();
		
		new VideoCategoryListTask().execute();

		regId = registerGCM();
		Log.e("regsd", regId);
		ImageButton bthSearch = (ImageButton) headerView
				.findViewById(R.id.btnSearch);
		bthSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent videoIntent = new Intent(HomeActivity.this , SearchActivity.class);
				AnimatedActivity parentActivity = (AnimatedActivity) HomeActivity.this.getParent();
				videoIntent.putExtra("categoriesList", categoriesList);
				parentActivity.startChildActivity("LiveStream", videoIntent);
			}
		});

		// filterGridView.addHeaderView(headerView, "Potato", false);
		filterGridView.setAdapter(adapter);
		filterGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				/*
				 * if (position >5) { Intent videoIntent = new
				 * Intent(HomeActivity.this, LiveStream.class);
				 * 
				 * AnimatedActivity parentActivity = (AnimatedActivity)
				 * HomeActivity.this .getParent(); parentActivity
				 * .startChildActivity("LiveStream", videoIntent); } else {
				 */
//				if (position == 0) {
					Intent frequentMessages = new Intent(HomeActivity.this,
							VideoListActivity.class);
					frequentMessages.putExtra(
							WebServiceNaming.TAG_CATEGORY_NAME,
							categoriesList.get(position).get("category_name"));
					Log.e("nik", "id"+categoriesList.get(position).get("category_id")+"Cat NAme "+categoriesList.get(position).get("category_name"));
					
					frequentMessages.putExtra(WebServiceNaming.TAG_IMAGE, Integer.valueOf(categoriesList.get(position).get("category_id")));
					frequentMessages.putExtra(WebServiceNaming.TAG_CITY, "");
					frequentMessages.putExtra(
							WebServiceNaming.TAG_SUB_CATEGORY_ID, "");
					frequentMessages.putExtra(WebServiceNaming.TAG_KEYWORD, "");

					AnimatedActivity parentActivity = (AnimatedActivity) HomeActivity.this
							.getParent();
					frequentMessages.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
					parentActivity.startChildActivity(
							"FrequentMessageActivity", frequentMessages);
//				} else {
//					Intent frequentMessages = new Intent(HomeActivity.this,
//							VideoListActivity.class);
//					frequentMessages.putExtra(
//							WebServiceNaming.TAG_CATEGORY_NAME,
//							headingList.get(0));
//					frequentMessages.putExtra(WebServiceNaming.TAG_IMAGE, 1);
//					frequentMessages.putExtra(WebServiceNaming.TAG_CITY, "");
//					frequentMessages.putExtra(
//							WebServiceNaming.TAG_SUB_CATEGORY_ID, "");
//					frequentMessages.putExtra(WebServiceNaming.TAG_KEYWORD, "");
//
//					AnimatedActivity parentActivity = (AnimatedActivity) HomeActivity.this
//							.getParent();
//					parentActivity.startChildActivity(
//							"FrequentMessageActivity", frequentMessages);
//				}

				// }

			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// hideKeyboard();
		// filterGridView.requestFocus();

	}

	public String registerGCM() {

		gcm = GoogleCloudMessaging.getInstance(this);
		regId = getRegistrationId(HomeActivity.this);

		if (TextUtils.isEmpty(regId)) {

			registerInBackground();

			Log.e("RegisterActivity",
					"registerGCM - successfully registered with GCM server - regId: "
							+ regId);
		} else {
			getRegistrationId(getApplicationContext());
		}
		return regId;
	}

	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getSharedPreferences(
				SplashScreen.class.getSimpleName(), Context.MODE_PRIVATE);
		String registrationId = prefs.getString(
				WebServiceNaming.TAG_NOTIFICATION_ID, "");
		if (registrationId.isEmpty()) {
			Log.i("msg", "Registration not found.");
			return "";
		}
		int registeredVersion = prefs.getInt(WebServiceNaming.TAG_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i("msh", "App version changed.");
			return "";
		}
		return registrationId;
	}

	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
				Log.e("sucess", "This device  supported.");
			} else {
				Log.e("erroe", "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}

	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging
								.getInstance(HomeActivity.this);
					}
					regId = gcm.register(Config.GOOGLE_PROJECT_ID);
					Log.e("RegisterActivity", "registerInBackground - regId: "
							+ regId);
					msg = "Device registered, registration ID=" + regId;

					storeRegistrationId(HomeActivity.this, regId);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					Log.e("RegisterActivity", "Error: " + msg);
				}
				Log.e("RegisterActivity", "AsyncTask completed: " + msg);
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {

			}
		}.execute(null, null, null);
	}

	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getSharedPreferences(
				SplashScreen.class.getSimpleName(), Context.MODE_PRIVATE);
		int appVersion = getAppVersion(context);
		Log.e("Save", "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(WebServiceNaming.TAG_NOTIFICATION_ID, regId);
		editor.putInt(WebServiceNaming.TAG_APP_VERSION, appVersion);
		editor.commit();
	}

	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			Log.e("RegisterActivity",
					"I never expected this! Going down, going down!" + e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tvwhatsnew:
			_intent = new Intent(HomeActivity.this, VideoListActivity.class);
			_intent.putExtra(WebServiceNaming.TAG_WHATS_NEW, "1");
			_intent.putExtra(WebServiceNaming.TAG_CATEGORY_NAME, "");
			_intent.putExtra(WebServiceNaming.TAG_IMAGE, 1);
			_intent.putExtra(WebServiceNaming.TAG_CITY, "");
			_intent.putExtra(WebServiceNaming.TAG_SUB_CATEGORY_ID, "");
			_intent.putExtra(WebServiceNaming.TAG_KEYWORD, "");

			parentActivity = (AnimatedActivity) HomeActivity.this.getParent();
			parentActivity.startChildActivity("FrequentMessageActivity",
					_intent);
			break;

		case R.id.tvpopular:
			_intent = new Intent(HomeActivity.this, VideoListActivity.class);
			_intent.putExtra(WebServiceNaming.TAG_CATEGORY_NAME, "");
			_intent.putExtra(WebServiceNaming.POPULAR, "1");
			_intent.putExtra(WebServiceNaming.TAG_IMAGE, 1);
			_intent.putExtra(WebServiceNaming.TAG_CITY, "");
			_intent.putExtra(WebServiceNaming.TAG_SUB_CATEGORY_ID, "");
			_intent.putExtra(WebServiceNaming.TAG_KEYWORD, "");

			parentActivity = (AnimatedActivity) HomeActivity.this.getParent();
			parentActivity.startChildActivity("FrequentMessageActivity",
					_intent);
			break;
		case R.id.tvlive:
			_intent = new Intent(HomeActivity.this, LiveStream.class);

			parentActivity = (AnimatedActivity) HomeActivity.this.getParent();
			parentActivity.startChildActivity("LiveStream", _intent);
			break;
		case R.id.llllive:
			_intent = new Intent(HomeActivity.this, LiveStream.class);

			parentActivity = (AnimatedActivity) HomeActivity.this.getParent();
			parentActivity.startChildActivity("LiveStream", _intent);
			break;
		case R.id.ivlive:
			_intent = new Intent(HomeActivity.this, LiveStream.class);

			parentActivity = (AnimatedActivity) HomeActivity.this.getParent();
			parentActivity.startChildActivity("LiveStream", _intent);
			break;
		case R.id.btnSearch:
			Intent videoIntent = new Intent(HomeActivity.this,
					SearchActivity.class);

			parentActivity = (AnimatedActivity) HomeActivity.this.getParent();
			parentActivity.startChildActivity("LiveStream", videoIntent);
			break;
		case R.id.ivcarrace:
			Intent frequentMessages = new Intent(HomeActivity.this,
					VideoListActivity.class);
			frequentMessages.putExtra(WebServiceNaming.TAG_CATEGORY_NAME,
					headingList.get(1));
			frequentMessages.putExtra(WebServiceNaming.TAG_IMAGE, 2);
			frequentMessages.putExtra(WebServiceNaming.TAG_CITY, "");
			frequentMessages.putExtra(WebServiceNaming.TAG_SUB_CATEGORY_ID, "");
			frequentMessages.putExtra(WebServiceNaming.TAG_KEYWORD, "");

			AnimatedActivity parentActivity = (AnimatedActivity) HomeActivity.this
					.getParent();
			parentActivity.startChildActivity("FrequentMessageActivity",
					frequentMessages);
			break;
		case R.id.ivrealstate:
			_intent = new Intent(HomeActivity.this, VideoListActivity.class);
			_intent.putExtra(WebServiceNaming.TAG_CATEGORY_NAME,
					headingList.get(0));
			_intent.putExtra(WebServiceNaming.TAG_IMAGE, 1);
			_intent.putExtra(WebServiceNaming.TAG_CITY, "");
			_intent.putExtra(WebServiceNaming.TAG_SUB_CATEGORY_ID, "");
			_intent.putExtra(WebServiceNaming.TAG_KEYWORD, "");

			parentActivity = (AnimatedActivity) HomeActivity.this.getParent();
			parentActivity.startChildActivity("FrequentMessageActivity",
					_intent);
			break;

		}
	}

	/*
	 * To Load all categories from API below task is to be executed
	 */
	private class VideoCategoryListTask extends
			AsyncTask<String, String, String> {

		JSONObject jsonResult = null;
		ProgressDialog pdialog;
		private HashMap<String, String> _data;


		@Override
		protected String doInBackground(String... params) {

			try {
			 
				jsonResult = WebServiceConnection.performPost("" // null data in param
						,SoapServicePath.TAG_URL_GET_CATEGORY);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			categoriesList = new ArrayList<HashMap<String, String>>();
			
			try {
				if (jsonResult != null) {
					JSONObject jsonResultList;
					

					jsonResultList = jsonResult
							.getJSONObject(WebServiceNaming.TAG_RESULT);
					if (jsonResultList.getString("success").equalsIgnoreCase("true")) {
						
					
							for (int i = 0; i < jsonResultList.getJSONArray(
									WebServiceNaming.TAG_CATEGORY).length(); i++) {
		
								_data = new HashMap<String, String>();
								
								JSONObject _obj = jsonResultList.getJSONArray(
										WebServiceNaming.TAG_CATEGORY).getJSONObject(i);
		
								Iterator iter = _obj.keys();
								while (iter.hasNext()) {
									String key = (String) iter.next();
									String value;
		
									value = _obj.getString(key);
									_data.put(key, value);
		
								}
								categoriesList.add(_data);
							}
						
						/*
						 * To refresh the list
						 */
							Log.e("nik", categoriesList.toString());
						adapter.notifyDataSetChanged();
						adapter = new ImageAdapter(HomeActivity.this, categoriesList);
						filterGridView.setAdapter(adapter);
						
					}else
					{
						Toast.makeText(HomeActivity.this, jsonResultList.getString("error") , Toast.LENGTH_LONG).show();
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// JSONObject jsonResultList = jsonResult
			// .getJSONObject(WebServiceNaming.TAG_RESULT);
			// JSONArray jsonArrayList = jsonResultList
			// .getJSONArray("videoslist");
			// for (int i = 0; i < jsonArrayList.length(); i++) {
			// JSONObject jsonDetail = jsonArrayList.getJSONObject(i);
			// String video_id = jsonDetail
			// .getString(WebServiceNaming.TAG_VIDEO_ID);
			// String categoryName = jsonDetail
			// .getString(WebServiceNaming.TAG_CATEGORY_NAME);
			// String video_city = jsonDetail
			// .getString(WebServiceNaming.TAG_VIDEO_CITY);
			// String video_title = jsonDetail
			// .getString(WebServiceNaming.TAG_VIDEO_TITLE);
			// String video_description = jsonDetail
			// .getString(WebServiceNaming.TAG_VIDEO_DESCRIPTION);
			// String video_link = jsonDetail
			// .getString(WebServiceNaming.TAG_VIDEO_LINK);
			// String video_uploaded_date = jsonDetail
			// .getString(WebServiceNaming.TAG_UPLOAD_DATE);
			// String subcategory_name = jsonDetail
			// .getString(WebServiceNaming.TAG_SUB_CATEGORY_NAME);
			// String meta_data = jsonDetail
			// .getString(WebServiceNaming.TAG_META_DATA);
			//
			// String video_thumbnail = jsonDetail
			// .getString(WebServiceNaming.TAG_VIDEO_TUBENIL);
			// String websiteLink =
			// jsonDetail.getString(WebServiceNaming.TAG_WEBSITE_LINK);
			//
			// videoList.add(new VideoList(video_id, categoryName,
			// video_city, video_title, video_description,
			// video_link, video_uploaded_date,
			// video_thumbnail, subcategory_name, meta_data,
			// websiteLink,jsonDetail.getString("videotype")));

			// }

			// pbar.setVisibility(View.INVISIBLE);

			// videoListAdapter = new VideoListAdapter(activity,videoList,
			// image);
			// videoListView.setAdapter(videoListAdapter);
			// }
			// } catch (JSONException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }

			// if (videoList.size() == 0) {
			// // status.setVisibility(View.VISIBLE);
			// }
			 if (pdialog.isShowing()){
				 pdialog.dismiss();
			 }

		}

		@Override
		protected void onPreExecute() {
			// pbar.setVisibility(View.VISIBLE);
			pdialog = new ProgressDialog(HomeActivity.this.getParent());
			pdialog.setMessage("Loading...");
			pdialog.setCancelable(false);
			if (pdialog != null) {
				pdialog.show();
			}
		}

		@Override
		protected void onProgressUpdate(String... text) {

		}
	}


}