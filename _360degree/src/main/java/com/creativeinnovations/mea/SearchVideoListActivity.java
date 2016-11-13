/**
 * 
 */
package com.creativeinnovations.mea;

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
 * @author sachin
 * 
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
public class SearchVideoListActivity extends Activity implements
		OnClickListener {
	ImageButton btnBack;

	ListView videoListView;
	// ProgressBar pbar;
	ArrayList<VideoList> videoList;
	VideoListAdapter videoListAdapter;
	Activity activity;
	int[] sliderImageList = { R.drawable.realestate_slide,
			R.drawable.events_slide, R.drawable.wedding_slide,
			R.drawable.other_slide, R.drawable.donations_silde,
			R.drawable.livebroadcast };
	int[] headingImakgeList = { R.drawable.realestate_icon, R.drawable.events,
			R.drawable.weddings, R.drawable.other, R.drawable.donations,
			R.drawable.livebroadcast };
	String subcat_id;
	String city;
	String keyword;
	TextView status;
	String cat_id;
	Intent _intent;
	
	TextView tvwhatsnew,tvpopular,tvlive;
	LinearLayout llllive;
	ImageView ivlive;
	ImageButton btnSearch;
	AnimatedActivity parentActivity;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_list);
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
		ImageView btnSearch = (ImageView) findViewById(R.id.btnSearch);
		btnSearch.setVisibility(View.INVISIBLE);
		findViewById(R.id.page_Title).requestFocus();
		status = (TextView) findViewById(R.id.status);

		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent videoIntent = new Intent(SearchVideoListActivity.this,
						SearchActivity.class);

				AnimatedActivity parentActivity = (AnimatedActivity) SearchVideoListActivity.this
						.getParent();
				parentActivity
						.startChildActivity("LiveStreamoter", videoIntent);
			}
		});
		btnBack = (ImageButton) findViewById(R.id.btn_Back);
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});

		init();

	}

	/**
	 * 
	 */
	private void init() {
		// TODO Auto-generated method stub

		videoListView = (ListView) findViewById(R.id.videoListView);
		// pbar = (ProgressBar) findViewById(R.id.pbar);

		city = getIntent().getExtras().getString(WebServiceNaming.TAG_CITY);
		keyword = getIntent().getExtras().getString(
				WebServiceNaming.TAG_KEYWORD);
		subcat_id = getIntent().getExtras().getString(
				WebServiceNaming.TAG_SUB_CATEGORY_ID);
		cat_id = getIntent().getExtras().getString(
				WebServiceNaming.TAG_CATEGORY_ID);

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
		case R.id.btn_Back:
			// AnimatedActivity pActivity = (AnimatedActivity) this.getParent();
			//
			// pActivity.finishFromChild(SearchVideoListActivity.this);

			break;
		case R.id.tvwhatsnew:
			_intent = new Intent(SearchVideoListActivity.this,
					VideoListActivity.class);
			_intent.putExtra(WebServiceNaming.TAG_WHATS_NEW,"1");
			_intent.putExtra(
					WebServiceNaming.TAG_CATEGORY_NAME,"");
			_intent.putExtra(WebServiceNaming.TAG_IMAGE,1);
			_intent.putExtra(WebServiceNaming.TAG_CITY, "");
			_intent.putExtra(
					WebServiceNaming.TAG_SUB_CATEGORY_ID, "");
			_intent.putExtra(WebServiceNaming.TAG_KEYWORD, "");

			parentActivity = (AnimatedActivity) SearchVideoListActivity.this
					.getParent();
			parentActivity.startChildActivity(
					"FrequentMessageActivity", _intent);
			break;

	 case R.id.tvpopular:
		 _intent = new Intent(SearchVideoListActivity.this,
					VideoListActivity.class);
			_intent.putExtra(
					WebServiceNaming.TAG_CATEGORY_NAME,"");
			_intent.putExtra(WebServiceNaming.POPULAR,"1");
			_intent.putExtra(WebServiceNaming.TAG_IMAGE,1);
			_intent.putExtra(WebServiceNaming.TAG_CITY, "");
			_intent.putExtra(
					WebServiceNaming.TAG_SUB_CATEGORY_ID, "");
			_intent.putExtra(WebServiceNaming.TAG_KEYWORD, "");

			parentActivity = (AnimatedActivity) SearchVideoListActivity.this
					.getParent();
			parentActivity.startChildActivity(
					"FrequentMessageActivity", _intent);
			break;
	 case R.id.tvlive:
		 _intent = new Intent(SearchVideoListActivity.this,
					LiveStream.class);

			 parentActivity = (AnimatedActivity) SearchVideoListActivity.this
					.getParent();
			parentActivity
					.startChildActivity("LiveStream", _intent);
		break;
	 case R.id.llllive:
		 _intent = new Intent(SearchVideoListActivity.this,
					LiveStream.class);

			 parentActivity = (AnimatedActivity) SearchVideoListActivity.this
					.getParent();
			parentActivity
					.startChildActivity("LiveStream", _intent);
		break;
	 case R.id.ivlive:
		 _intent = new Intent(SearchVideoListActivity.this,
					LiveStream.class);

			 parentActivity = (AnimatedActivity) SearchVideoListActivity.this
					.getParent();
			parentActivity
					.startChildActivity("LiveStream", _intent);
		break;
	 case R.id.btnSearch:
			Intent videoIntent = new Intent(SearchVideoListActivity.this,
					SearchActivity.class);

			AnimatedActivity parentActivity = (AnimatedActivity) SearchVideoListActivity.this
					.getParent();
			parentActivity.startChildActivity("LiveStream", videoIntent);
		break;


		default:
			break;
		}
	}

	private class VideoListTaskRunner extends AsyncTask<String, String, String> {

		JSONObject jsonResult = null;
		ProgressDialog pdialog;

		@Override
		protected void onPreExecute() {
			// pbar.setVisibility(View.VISIBLE);
			pdialog = new ProgressDialog(
					SearchVideoListActivity.this.getParent());
			pdialog.setMessage("Loading...");
			pdialog.setCancelable(false);
			if (pdialog != null) {
				pdialog.show();
			}
		}
		
		@Override
		protected String doInBackground(String... params) {

			try {
				JSONObject jsonPost = new JSONObject();
				jsonPost.put(WebServiceNaming.TAG_CATEGORY_ID, cat_id + "");
				jsonPost.put(WebServiceNaming.TAG_SUB_CATEGORY_ID, subcat_id);
				jsonPost.put(WebServiceNaming.TAG_CITYS, city);
				jsonPost.put(WebServiceNaming.TAG_KEYWORD, keyword);
				jsonPost.put(WebServiceNaming.TAG_DEVICE_MODEL, "Android");
				jsonPost.put("action", "video_list");
				 
				String _userID = CommonUtility.getUserId(SearchVideoListActivity.this);
				
				jsonPost.put("id", _userID);
				String reg_id = getRegistrationId(SearchVideoListActivity.this);
				jsonPost.put(WebServiceNaming.TAG_NOTIFICATION_ID, reg_id);
				/*jsonResult = WebServiceConnection.performPost(
						jsonPost.toString(), WebServiceNaming.TAG_URL
								+ WebServiceNaming.TAG_VIDEO_LIST);*/
				
				jsonResult = WebServiceConnection.performPost(jsonPost.toString(), SoapServicePath.DOMAIN_NAME);

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
					JSONObject jsonResultList = jsonResult.getJSONObject(WebServiceNaming.TAG_RESULT);
					JSONArray jsonArrayList = jsonResultList.getJSONArray(WebServiceNaming.TAG_VIDEO);
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
						String websiteLink = jsonDetail
								.getString(WebServiceNaming.TAG_WEBSITE_LINK);

						videoList.add(new VideoList(video_id, categoryName,
								video_city, video_title, video_description,
								video_link, video_uploaded_date,
								video_thumbnail, subcategory_name, meta_data,
								websiteLink,jsonDetail.getString("videotype")));

					}

					// pbar.setVisibility(View.INVISIBLE);

					videoListAdapter = new VideoListAdapter(activity,
							videoList, -1);
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
		protected void onProgressUpdate(String... text) {

		}
	}

	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getSharedPreferences(
				SearchVideoListActivity.class.getSimpleName(),
				Context.MODE_PRIVATE);
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

	@SuppressWarnings("unused")
	private Bitmap getResizedBitmap(Bitmap bm, int newWidth) {

		int width = bm.getWidth();

		int height = bm.getHeight();

		float aspect = (float) width / height;

		float scaleWidth = newWidth;

		float scaleHeight = scaleWidth / aspect; // yeah!

		// create a matrix for the manipulation

		Matrix matrix = new Matrix();

		// resize the bit map

		matrix.postScale(scaleWidth / width, scaleHeight / height);

		// recreate the new Bitmap

		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
				matrix, true);

		bm.recycle();

		return resizedBitmap;
	}

}
