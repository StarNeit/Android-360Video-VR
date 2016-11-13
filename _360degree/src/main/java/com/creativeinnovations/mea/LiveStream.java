/**
 * 
 */
package com.creativeinnovations.mea;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.creativeinnovations.mea.network.CommonUtility;
import com.degree.hitaishin.connection.WebServiceConnection;
import com.degree.hitaishin.connection.WebServiceNaming;
import com.degree.hitaishin.model.VideoList;

/**
 * @author sachin
 * 
 */
public class LiveStream extends Activity {
	ImageButton btn_Back, btnSearch, btnPlay;
	Button btnEmail;
	EditText editEmail;
	ImageView imageLogo;
	TextView status;
	String url;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.live_stream_layout);
		btn_Back = (ImageButton) findViewById(R.id.btn_Back);
		editEmail = (EditText) findViewById(R.id.editEmail);
		imageLogo = (ImageView) findViewById(R.id.imageLogo);
		btnEmail = (Button) findViewById(R.id.Submit);
		btnSearch = (ImageButton) findViewById(R.id.btnSearch);
		status = (TextView) findViewById(R.id.status);
		btnPlay = (ImageButton) findViewById(R.id.videoPlay);
			new InfoTaskRunner().execute();
//		editEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				if (!hasFocus) {
////					hideKeyboard();
//				}
//			}
//		});
		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent videoIntent = new Intent(LiveStream.this,
						SearchActivity.class);

				AnimatedActivity parentActivity = (AnimatedActivity) LiveStream.this
						.getParent();
				parentActivity.startChildActivity("LiveStreamsearch", videoIntent);
			}
		});
		btn_Back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				hideKeyboard();
				AnimatedActivity pActivity = (AnimatedActivity) LiveStream.this
						.getParent();

				pActivity.finishFromChild(LiveStream.this);

			}
		});
		btnPlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent videoIntent = new Intent(LiveStream.this,
						VideoPlayerLiveStream.class);
				Bundle bundle = new Bundle();

				bundle.putSerializable(WebServiceNaming.TAG_VIDEO_LINK,
						new VideoList("", "Live Stream", "", "", "", "", url,
								url, "", "", "",""));

				videoIntent.putExtras(bundle);

				startActivity(videoIntent);
			}
		});

		btnEmail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub

				if(editEmail.getText().toString().trim().equals(""))
				{
					CommonUtility.showAlert(getParent(), "Error", "Email cannot be left blank.");
					return;
				}
				else
				{
					if(!android.util.Patterns.EMAIL_ADDRESS.matcher(editEmail.getText().toString().trim()).matches())
					{
						CommonUtility.showAlert(getParent(), "Error", "Email should be in proper format.");
						return;
					}
				}
				
				Intent emailIntent = new Intent(Intent.ACTION_SEND);
				emailIntent.putExtra(Intent.EXTRA_EMAIL,
						new String[] { "info@360mea.com" });
				emailIntent.putExtra(Intent.EXTRA_SUBJECT,
						"Live Broadcast Notification.");
				emailIntent.putExtra(Intent.EXTRA_TEXT,
						" My email is "+editEmail.getText().toString()+". Please keep me notified of the next live broadcast.");

				emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Live Broadcast Notification");

				// need this to prompts email client only
				emailIntent.setType("message/rfc822");

				startActivity(Intent.createChooser(emailIntent,
						"Select an Email Client:"));
			}
		});
	}


	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
		AnimatedActivity pActivity = (AnimatedActivity) LiveStream.this
				.getParent();

		pActivity.finishFromChild(LiveStream.this);
	}


	@SuppressWarnings("unused")
	private Context getDialogContext() {
		Context context;
		if (getParent() != null)
			context = getParent();
		else
			context = this;
		return context;
	}

	private class InfoTaskRunner extends AsyncTask<String, String, String> {

		JSONObject jsonResult = null;
		ProgressDialog pDialog;

		@Override
		protected String doInBackground(String... params) {

			try {

				jsonResult = WebServiceConnection.performPost("",
						WebServiceNaming.TAG_URL
								+ WebServiceNaming.TAG_LIVESTREAM);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (pDialog.isShowing()) {
				pDialog.dismiss();
			}

			try {
				if (jsonResult != null) {

					JSONObject jsonResultList = jsonResult
							.getJSONObject(WebServiceNaming.TAG_RESULT);

					String success = jsonResultList.getString("success");
					if (success.equals("true")) {

						status.setVisibility(View.GONE);
						btnPlay.setVisibility(View.VISIBLE);
						url = jsonResultList.getString("livestreaming_url");
						Log.e("url", url);

					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(LiveStream.this.getParent());
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			if (pDialog != null) {
				pDialog.show();
			}

		}

		@Override
		protected void onProgressUpdate(String... text) {

		}
	}

}
