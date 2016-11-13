package com.creativeinnovations.mea;

import java.util.ArrayList;
import java.util.HashMap;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.creativeinnovations.mea.network.CommonUtility;
import com.creativeinnovations.mea.network.SoapServicePath;
import com.creativeinnovations.mea.network.WebAPICallingClass;
import com.degree.adpater.VideoListAdapter;
import com.degree.hitaishin.connection.WebServiceConnection;
import com.degree.hitaishin.connection.WebServiceNaming;
import com.degree.hitaishin.model.VideoList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class VideoPlayerViewDirect extends Activity {
	
	public static final String KEY_CD = "cd";

    private Uri uriData;
    private TextView dataTextView;
    HashMap<String, String> _dataToPost;
    String videoId;
    WebAPICallingClass _webserviceCall;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_player_view_dierect);

        dataTextView = (TextView) findViewById(R.id.textView1);
        System.out.println("==============GizmosActivity========>>>>>>>>");
        String url = "market://details?id=com.creativeinnovations.mea&another_key=46";
        videoId = url.substring(url.indexOf("y")+2, url.length()); 
        _webserviceCall =  new WebAPICallingClass();
        
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();

        if(intent != null) {
            uriData = intent.getData();

            if (uriData != null) {

                //To get scheme.
                String scheme = uriData.getScheme();

                //To get server name.
                String host = uriData.getHost();

                //To get parameter value from the URI.
                String parameterValue = uriData.getQueryParameter(KEY_CD);

                StringBuilder builder = new StringBuilder();
                builder.append("Data Received From URI:\n"+uriData.toString()+"\n");
                
                videoId = uriData.toString().substring(uriData.toString().indexOf("y")+2, uriData.toString().length()); 

                // Set the retrieved value to dataTextView.
//                dataTextView.setText(builder.toString());
                dataTextView.setText("Please Wait...");
                new SingleDataWebServiceCall().execute();
            }
        }
    }

    /**
     * To get the latest intent object.
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

	
    
    
    public class SingleDataWebServiceCall extends AsyncTask<String, String, String> 
	{
    	JSONObject jsonResult = null;
		ProgressDialog pdialog;

		@Override
		protected void onPreExecute() {
			// pbar.setVisibility(View.VISIBLE);
			pdialog = new ProgressDialog(VideoPlayerViewDirect.this);
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
				jsonPost.put("video_id", videoId);
				jsonResult = WebServiceConnection.performPost(jsonPost.toString(),"http://www.360mea.com/app/app_single_video");

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			try {
				if (jsonResult != null) {
					JSONObject jsonResultList = jsonResult.getJSONObject(WebServiceNaming.TAG_RESULT);
					JSONArray jsonArrayList = jsonResultList.getJSONArray("videos");
					String v_url = jsonArrayList.getJSONObject(0).getString("video_link");					// pbar.setVisibility(View.INVISIBLE);
//					CommonUtility.setGlobalString(VideoPlayerViewDirect.this, "APP_LINKING_URL", v_url);
//					String url = CommonUtility.getGlobalString(VideoPlayerViewDirect.this, "APP_LINKING_URL");
//					System.out.println("===================>"+url);
					
					
					String video_id = jsonArrayList.getJSONObject(0)
							.getString(WebServiceNaming.TAG_VIDEO_ID);
					String categoryName = jsonArrayList.getJSONObject(0)
							.getString(WebServiceNaming.TAG_CATEGORY_NAME);
					String video_city = jsonArrayList.getJSONObject(0)
							.getString(WebServiceNaming.TAG_VIDEO_CITY);
					String video_title = jsonArrayList.getJSONObject(0)
							.getString(WebServiceNaming.TAG_VIDEO_TITLE);
					String video_description = jsonArrayList.getJSONObject(0)
							.getString(WebServiceNaming.TAG_VIDEO_DESCRIPTION);
					String video_link = jsonArrayList.getJSONObject(0)
							.getString(WebServiceNaming.TAG_VIDEO_LINK);
					String video_uploaded_date = jsonArrayList.getJSONObject(0)
							.getString(WebServiceNaming.TAG_UPLOAD_DATE);
					String subcategory_name = jsonArrayList.getJSONObject(0)
							.getString(WebServiceNaming.TAG_SUB_CATEGORY_NAME);
					String meta_data = jsonArrayList.getJSONObject(0)
							.getString(WebServiceNaming.TAG_META_DATA);

					String video_thumbnail = jsonArrayList.getJSONObject(0)
							.getString(WebServiceNaming.TAG_VIDEO_TUBENIL);
					String websiteLink = jsonArrayList.getJSONObject(0).getString(WebServiceNaming.TAG_WEBSITE_LINK);

					VideoList _list = new VideoList(video_id, categoryName,
							video_city, video_title, video_description,
							video_link, video_uploaded_date,
							video_thumbnail, subcategory_name, meta_data,
							websiteLink,("videotype"));
					
					
					Intent videoIntent = new Intent(getApplicationContext(), VideoPlayerView.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable(WebServiceNaming.TAG_VIDEO_LINK,_list);
					videoIntent.putExtras(bundle);
					startActivity(videoIntent);	
					VideoPlayerViewDirect.this.finish();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (pdialog.isShowing())
				pdialog.dismiss();

		}
	}
}
