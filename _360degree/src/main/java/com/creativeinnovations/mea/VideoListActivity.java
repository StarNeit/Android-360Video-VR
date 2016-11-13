package com.creativeinnovations.mea;

/*
 * @author Nikhil
 */

import java.util.ArrayList;

import org.json.JSONArray;
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
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.creativeinnovations.mea.network.CommonUtility;
import com.creativeinnovations.mea.network.SoapServicePath;
import com.degree.adpater.VideoListAdapter;
import com.degree.hitaishin.connection.WebServiceConnection;
import com.degree.hitaishin.connection.WebServiceNaming;
import com.degree.hitaishin.model.VideoList;

/**
 * @author Nikhil
 * 
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
public class VideoListActivity extends Activity implements OnClickListener {
	ImageButton btnBack;
	TextView btnTitle;
	ImageView imageTitle;
	ImageView slider_Image;
	ListView videoListView;
	public static final int progress_bar_type = 0;
	// ProgressBar pbar;
	ArrayList<VideoList> videoList;
	VideoListAdapter videoListAdapter;
	Activity activity;
	int cat_ids[] = { 0, 1, 2, 4, 14, 18 };
	int image;
	String subcat_id;
	String city;
	String keyword,whatsNew,popular;
	TextView status;
	public static int _dataIncrement = 0;
	TextView tvwhatsnew,tvpopular,tvlive;
	LinearLayout llllive;
	ImageView ivlive;
	ImageButton btnSearch;
	AnimatedActivity parentActivity;
	Intent _intent;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	 
		setContentView(R.layout.video_list_activtiy);
		
		ImageView btnSearch = (ImageView) findViewById(R.id.btnSearch);
		status = (TextView) findViewById(R.id.status);
		tvwhatsnew = (TextView) findViewById(R.id.tvwhatsnew);
		tvwhatsnew.setOnClickListener(this);
		tvpopular = (TextView) findViewById(R.id.tvpopular);
		tvpopular.setOnClickListener(this);
		tvlive = (TextView) findViewById(R.id.tvlive);
		tvlive.setOnClickListener(this);
		llllive = (LinearLayout) findViewById(R.id.llllive);
		llllive.setOnClickListener(this);
		ivlive =  (ImageView) findViewById(R.id.ivlive);
		ivlive.setOnClickListener(this);
		
		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent videoIntent = new Intent(VideoListActivity.this,
						SearchActivity.class);

				parentActivity = (AnimatedActivity) VideoListActivity.this.getParent();
				parentActivity.startChildActivity("LiveStream", videoIntent);
				
			}
		});

		init();

	}

	
	/**
	 * 
	 */
	private void init() {
		// TODO Auto-generated method stub
		btnBack = (ImageButton) findViewById(R.id.btn_Back);
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		btnTitle = (TextView) findViewById(R.id.page_Title);
		imageTitle = (ImageView) findViewById(R.id.category_icon);

		videoListView = (ListView) findViewById(R.id.videoListView);
		// pbar = (ProgressBar) findViewById(R.id.pbar);
		image = getIntent().getExtras().getInt(WebServiceNaming.TAG_IMAGE);
		
		String type = getIntent().getExtras().getString( WebServiceNaming.TAG_CATEGORY_NAME);
		city = getIntent().getExtras().getString(WebServiceNaming.TAG_CITY);
		keyword = getIntent().getExtras().getString(WebServiceNaming.TAG_KEYWORD);
		subcat_id = getIntent().getExtras().getString(WebServiceNaming.TAG_SUB_CATEGORY_ID);
		if(getIntent().getExtras().getString(WebServiceNaming.TAG_WHATS_NEW) != null)
		{
			whatsNew = getIntent().getExtras().getString(WebServiceNaming.TAG_WHATS_NEW);
		}
		
		if(getIntent().getExtras().getString(WebServiceNaming.POPULAR) != null)
		{
			popular = getIntent().getExtras().getString(WebServiceNaming.POPULAR);
		}
//		btnTitle.setText(type);


		activity = this.getParent();

		new VideoListTaskRunner().execute();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
		case R.id.tvwhatsnew:
			_dataIncrement  = _dataIncrement + 1;
			_intent = new Intent(VideoListActivity.this,
					VideoListActivity.class);
			_intent.putExtra(WebServiceNaming.TAG_WHATS_NEW,"1");
			_intent.putExtra(
					WebServiceNaming.TAG_CATEGORY_NAME,"");
			_intent.putExtra(WebServiceNaming.TAG_IMAGE,1);
			_intent.putExtra(WebServiceNaming.TAG_CITY, "");
			_intent.putExtra(
					WebServiceNaming.TAG_SUB_CATEGORY_ID, "");
			_intent.putExtra(WebServiceNaming.TAG_KEYWORD, "");

			parentActivity = (AnimatedActivity) VideoListActivity.this.getParent();
			parentActivity.startChildActivity( "FrequentMessageActivitynew"+String.valueOf(_dataIncrement), _intent );
			break;

	 case R.id.tvpopular:
		 _dataIncrement  = _dataIncrement + 1;
			_intent = new Intent(VideoListActivity.this , VideoListActivity.class);
			_intent.putExtra(
					WebServiceNaming.TAG_CATEGORY_NAME,"");
			_intent.putExtra(WebServiceNaming.POPULAR,"1");
			_intent.putExtra(WebServiceNaming.TAG_IMAGE,1);
			_intent.putExtra(WebServiceNaming.TAG_CITY, "");
			_intent.putExtra(
					WebServiceNaming.TAG_SUB_CATEGORY_ID, "");
			_intent.putExtra(WebServiceNaming.TAG_KEYWORD, "");

			parentActivity = (AnimatedActivity) VideoListActivity.this
					.getParent();
			parentActivity.startChildActivity( "FrequentMessageActivitynew" +String.valueOf(_dataIncrement) , _intent );
			
			break;
	 case R.id.tvlive:
		 _intent = new Intent(VideoListActivity.this , LiveStream.class);

			 parentActivity = (AnimatedActivity) VideoListActivity.this
					.getParent();
			parentActivity
					.startChildActivity("LiveStream" , _intent);
		    break;
	 case R.id.llllive:
		 
		 _intent = new Intent(VideoListActivity.this , LiveStream.class);
		 parentActivity = (AnimatedActivity) VideoListActivity.this.getParent();
		 parentActivity.startChildActivity("LiveStream", _intent);
		 
		     break;
	 case R.id.ivlive:
		 
		 _intent = new Intent(VideoListActivity.this , LiveStream.class);
		 parentActivity = (AnimatedActivity) VideoListActivity.this.getParent();
		 parentActivity.startChildActivity("LiveStream", _intent);
		 
		     break;
		default:
			break;
		}
	}

	private class VideoListTaskRunner extends AsyncTask<String, String, String> {

		JSONObject jsonResult = null;
		ProgressDialog pdialog;

		@Override
		protected String doInBackground(String... params) {

			try {
				JSONObject jsonPost = new JSONObject();
				
				jsonPost.put(WebServiceNaming.TAG_SUB_CATEGORY_ID, subcat_id);
				jsonPost.put(WebServiceNaming.TAG_CITYS, city);
				jsonPost.put(WebServiceNaming.TAG_KEYWORD, keyword);
				jsonPost.put(WebServiceNaming.TAG_DEVICE_MODEL, "Android");
				jsonPost.put("action", "video_list");
				 String _userID = CommonUtility.getUserId(VideoListActivity.this);
				jsonPost.put("id", _userID);
				if(whatsNew != null)
				{
					jsonPost.put(WebServiceNaming.TAG_WHATS_NEW, whatsNew);
				}
				else
				{
					jsonPost.put(WebServiceNaming.TAG_CATEGORY_ID, image
							+ "");
				}
				
				if(popular != null)
				{
					jsonPost.put(WebServiceNaming.POPULAR, popular);
				}
				else
				{
					jsonPost.put(WebServiceNaming.TAG_CATEGORY_ID, image
							+ "");
				}
				
				@SuppressWarnings("unused")
				String reg_id = getRegistrationId(VideoListActivity.this);
				jsonPost.put(WebServiceNaming.TAG_NOTIFICATION_ID,HomeActivity.regId);
				Log.e("USER_ID", jsonPost.toString());
				jsonResult = WebServiceConnection.performPost(
						jsonPost.toString(), SoapServicePath.DOMAIN_NAME);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				videoList = new ArrayList<VideoList>();
				if (jsonResult != null) {
					JSONObject jsonResultList = jsonResult
							.getJSONObject(WebServiceNaming.TAG_RESULT);
					JSONArray jsonArrayList = jsonResultList
							.getJSONArray("videoslist");
					for (int i = 0; i < jsonArrayList.length(); i++) {
						JSONObject jsonDetail = jsonArrayList.getJSONObject(i);
						String video_id = jsonDetail
								.getString(WebServiceNaming.TAG_VIDEO_ID);
						String categoryName = jsonDetail
								.getString(WebServiceNaming.TAG_CATEGORY_NAME);
						String video_city = jsonDetail
								.getString(WebServiceNaming.TAG_VIDEO_CITY);
						String video_title = jsonDetail
								.getString(WebServiceNaming.TAG_VIDEO_TITLE);
						String video_description = jsonDetail
								.getString(WebServiceNaming.TAG_VIDEO_DESCRIPTION);
						String video_link = jsonDetail
								.getString(WebServiceNaming.TAG_VIDEO_LINK);
						String video_uploaded_date = jsonDetail
								.getString(WebServiceNaming.TAG_UPLOAD_DATE);
						String subcategory_name = jsonDetail
								.getString(WebServiceNaming.TAG_SUB_CATEGORY_NAME);
						String meta_data = jsonDetail
								.getString(WebServiceNaming.TAG_META_DATA);

						String video_thumbnail = jsonDetail
								.getString(WebServiceNaming.TAG_VIDEO_TUBENIL);
						String websiteLink = jsonDetail.getString(WebServiceNaming.TAG_WEBSITE_LINK);

						videoList.add(new VideoList(video_id, categoryName,
								video_city, video_title, video_description,
								video_link, video_uploaded_date,
								video_thumbnail, subcategory_name, meta_data,
								websiteLink,jsonDetail.getString("videotype")));

					}

					// pbar.setVisibility(View.INVISIBLE);

					videoListAdapter = new VideoListAdapter(activity,videoList, image);
					videoListView.setAdapter(videoListAdapter);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (videoList.size() == 0) {
				status.setVisibility(View.VISIBLE);
			}
			if (pdialog.isShowing())
				pdialog.dismiss();

		}

		@Override
		protected void onPreExecute() {
			// pbar.setVisibility(View.VISIBLE);
			pdialog = new ProgressDialog(VideoListActivity.this.getParent());
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

	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getSharedPreferences(
				VideoListActivity.class.getSimpleName(), Context.MODE_PRIVATE);
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

	/**
	 * @param context
	 * @return
	 */
	private int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {

			throw new RuntimeException(e);
		}
	}

}
