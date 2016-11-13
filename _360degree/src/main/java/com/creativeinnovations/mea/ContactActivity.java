package com.creativeinnovations.mea;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.degree.Twitter_code.Twitter_Handler;
import com.degree.hitaishin.connection.WebServiceConnection;
import com.degree.hitaishin.connection.WebServiceNaming;
//import com.facebook.UiLifecycleHelper;
//import com.facebook.widget.LikeView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
public class ContactActivity extends Activity implements OnClickListener {
	
	static String TWITTER_CONSUMER_KEY = "LsCQaPOwd8k7WkyRFRZF4Q";
	static String TWITTER_CONSUMER_SECRET = "KJbJu5IQrlwxW7Cwnax3mMzAc4j3n6Wd2dG125srgk";
	// Preference Constants
	static String PREFERENCE_NAME = "twitter_oauth";
	static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";

	static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";
	LinearLayout likeView;
//	LikeView otherView;
//	public static UiLifecycleHelper uiHelper;
	String stringTwitter;
	// Twitter oauth urls
	static final String URL_TWITTER_AUTH = "auth_url";
	static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
	static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";

	ImageView slider_Image;
	TextView textTitle, textWebsite, textEmail, textPhone,tvwhatshot;
	ImageButton btnTwitter, btnSearch, btn_like;
	Twitter_Handler mTwitter;

	TextView tvwhatsnew,tvpopular,tvlive;
	LinearLayout llllive;
	ImageView ivlive;
	AnimatedActivity parentActivity;
	Intent _intent;
	
	ImageView btnfollowusfb,btnfollowustw,btnemailus;
	
	
	@SuppressWarnings("unused")
	private static SharedPreferences mSharedPreferences;
	Activity activity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 
		setContentView(R.layout.contact_activity);
		btnSearch = (ImageButton) findViewById(R.id.btnSearch);
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
		btnSearch.setOnClickListener(this);
		btnfollowusfb =  (ImageView) findViewById(R.id.btnfollowusfb);
		btnfollowusfb.setOnClickListener(this);
		btnfollowustw =  (ImageView) findViewById(R.id.btnfollowustw);
		btnfollowustw.setOnClickListener(this);
		btnemailus =  (ImageView) findViewById(R.id.btnemailus);
		btnemailus.setOnClickListener(this);
		/*tvwhatshot = (TextView) findViewById(R.id.tvwhatsnew);
		tvwhatshot.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent videoIntent = new Intent(ContactActivity.this,VideoListActivity.class);
				videoIntent.putExtra(
						WebServiceNaming.TAG_CATEGORY_NAME,
						"Real Estate");
				videoIntent.putExtra(WebServiceNaming.TAG_IMAGE,1);
				videoIntent.putExtra(WebServiceNaming.TAG_WHATS_NEW,1);
				videoIntent.putExtra(WebServiceNaming.TAG_CITY, "");
				videoIntent.putExtra(
						WebServiceNaming.TAG_SUB_CATEGORY_ID, "");
				videoIntent.putExtra(WebServiceNaming.TAG_KEYWORD, "");
				AnimatedActivity parentActivity = (AnimatedActivity) ContactActivity.this.getParent();
				parentActivity.startChildActivity("FrequentMessageActivity", videoIntent);
			}
		});
		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent videoIntent = new Intent(ContactActivity.this,
						SearchActivity.class);

				AnimatedActivity parentActivity = (AnimatedActivity) ContactActivity.this
						.getParent();
				parentActivity.startChildActivity("LiveStream", videoIntent);
			}
		});*/
		mSharedPreferences = this.getSharedPreferences("MyPref", 0);

		activity = ContactActivity.this.getParent();
		slider_Image = (ImageView) findViewById(R.id.slider_Image);

		textWebsite = (TextView) findViewById(R.id.textWebSite);

		textEmail = (TextView) findViewById(R.id.textEmail);
		textPhone = (TextView) findViewById(R.id.textPhone);
		textTitle = (TextView) findViewById(R.id.textTitle);
//		uiHelper = new UiLifecycleHelper(this, null);

		likeView = (LinearLayout) findViewById(R.id.like_view);
//		otherView = new LikeView(activity);
//		otherView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
//				LayoutParams.WRAP_CONTENT));
//		otherView.setObjectId("https://www.facebook.com/360MEA");// it can be

		btn_like = (ImageButton) findViewById(R.id.like_button);
		// any link

		// otherView.setLikeViewStyle(LikeView.Style.STANDARD);
		// otherView
		// .setAuxiliaryViewPosition(LikeView.AuxiliaryViewPosition.INLINE);
		// otherView.setHorizontalAlignment(LikeView.HorizontalAlignment.LEFT);
		// likeView.addView(otherView);

		btnTwitter = (ImageButton) findViewById(R.id.btnTwitter);

		textWebsite.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				Intent viewIntent = new Intent("android.intent.action.VIEW",
						Uri.parse("http://" + textWebsite.getText().toString()));
				startActivity(viewIntent);
				return false;
			}
		});

		textPhone.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_CALL);

				intent.setData(Uri.parse("tel:"
						+ textPhone.getText().toString()));
				startActivity(intent);
				return false;
			}
		});
		// textFacebook.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// // TODO Auto-generated method stub
		// Intent viewIntent = new Intent(
		// "android.intent.action.VIEW",
		// Uri.parse("http://" + textFacebook.getText().toString()));
		// startActivity(viewIntent);
		// return false;
		// }
		// });

		// textTwitter.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// // TODO Auto-generated method stub
		// Intent viewIntent = new Intent("android.intent.action.VIEW",
		// Uri.parse("http://" + textTwitter.getText().toString()));
		// startActivity(viewIntent);
		//
		//
		//
		// return false;
		// }
		// });
		textEmail.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				Intent emailIntent = new Intent(Intent.ACTION_SEND);
				emailIntent.putExtra(Intent.EXTRA_EMAIL,
						new String[] { textEmail.getText().toString() });
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, "RE:360MEA");
				emailIntent.putExtra(Intent.EXTRA_TEXT, "info");
				// / use below 2 commented lines if need to use BCC an CC
				// feature in email
				// emailIntent.putExtra(Intent.EXTRA_CC, new String[]{ to});
				// emailIntent.putExtra(Intent.EXTRA_BCC, new String[]{to});
				// //use below 3 commented lines if need to send attachment

				emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");

				// need this to prompts email client only
				emailIntent.setType("message/rfc822");

				startActivity(Intent.createChooser(emailIntent,
						"Select an Email Client:"));
				return false;
			}
		});
		btn_like.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String facebookUrl = "https://www.facebook.com/360MEA";
				try {
					int versionCode = getPackageManager().getPackageInfo(
							"com.facebook.katana", 0).versionCode;
					if (versionCode >= 3002850) {
						Uri uri = Uri.parse("fb://facewebmodal/f?href="
								+ facebookUrl);
						startActivity(new Intent(Intent.ACTION_VIEW, uri));
						;
					} else {
						// open the Facebook app using the old method
						// (fb://profile/id or fb://page/id)
						startActivity(new Intent(Intent.ACTION_VIEW, Uri
								.parse("fb://page/336227679757310")));
					}
				} catch (PackageManager.NameNotFoundException e) {
					// Facebook is not installed. Open the browser
					startActivity(new Intent(Intent.ACTION_VIEW, Uri
							.parse(facebookUrl)));
				}
			}
		});
		btnTwitter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent viewIntent = new Intent("android.intent.action.VIEW",
						Uri.parse("http://" + stringTwitter));
				startActivity(viewIntent);

			}
		});
		Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
				R.drawable.other_slide);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;

		slider_Image.setImageBitmap(getResizedBitmap(largeIcon, width));
		//new ContactTaskRunner().execute();
	}

	@SuppressWarnings("unused")
	private class ContactTaskRunner extends AsyncTask<String, String, String> {

		JSONObject jsonResult = null;
		ProgressDialog pDialog;

		@Override
		protected String doInBackground(String... params) {

			try {

				jsonResult = WebServiceConnection.performPost("",
						WebServiceNaming.TAG_URL
								+ WebServiceNaming.TAG_CONTACT_DETAIL);
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
					JSONObject jsonResultList = jsonResult
							.getJSONObject(WebServiceNaming.TAG_RESULT);

					String title = jsonResultList.getString("title");
					String facebook = jsonResultList
							.getString(WebServiceNaming.TAG_CONTACT_FACEBOOK);
					String twitter = jsonResultList
							.getString(WebServiceNaming.TAG_CONTACT_TWITTER);
					String EMAIL = jsonResultList
							.getString(WebServiceNaming.TAG_CONTACT_EMAIL);

					String WEBSITE = jsonResultList
							.getString(WebServiceNaming.TAG_CONTACT_WEBSITE);
					String PHONE = jsonResultList
							.getString(WebServiceNaming.TAG_CONTACT_PHONE);

					textEmail.setText(EMAIL);

					textWebsite.setText(WEBSITE);
					textPhone.setText(PHONE);
					stringTwitter = twitter;
					textTitle.setText(title);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (pDialog.isShowing()) {
				if (pDialog != null) {
				pDialog.dismiss();}
			}

		}

		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(ContactActivity.this.getParent());
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

	public Intent getOpenFacebookIntent(String pId) {

		try {
			activity.getPackageManager().getPackageInfo("com.facebook.katana",
					0);
			return new Intent(Intent.ACTION_VIEW,
					Uri.parse("facebook://link/421901904605718"));
		} catch (Exception e) {
			return new Intent(Intent.ACTION_VIEW,
					Uri.parse("https://www.facebook.com/"));
		}
	}

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
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tvwhatsnew:
			_intent = new Intent(ContactActivity.this,
					VideoListActivity.class);
			_intent.putExtra(WebServiceNaming.TAG_WHATS_NEW,"1");
			_intent.putExtra(
					WebServiceNaming.TAG_CATEGORY_NAME,"");
			_intent.putExtra(WebServiceNaming.TAG_IMAGE,1);
			_intent.putExtra(WebServiceNaming.TAG_CITY, "");
			_intent.putExtra(
					WebServiceNaming.TAG_SUB_CATEGORY_ID, "");
			_intent.putExtra(WebServiceNaming.TAG_KEYWORD, "");

			parentActivity = (AnimatedActivity) ContactActivity.this
					.getParent();
			parentActivity.startChildActivity(
					"FrequentMessageActivity", _intent);
			break;

	 case R.id.tvpopular:
		 _intent = new Intent(ContactActivity.this,
					VideoListActivity.class);
			_intent.putExtra(
					WebServiceNaming.TAG_CATEGORY_NAME,"");
			_intent.putExtra(WebServiceNaming.POPULAR,"1");
			_intent.putExtra(WebServiceNaming.TAG_IMAGE,1);
			_intent.putExtra(WebServiceNaming.TAG_CITY, "");
			_intent.putExtra(
					WebServiceNaming.TAG_SUB_CATEGORY_ID, "");
			_intent.putExtra(WebServiceNaming.TAG_KEYWORD, "");

			parentActivity = (AnimatedActivity) ContactActivity.this
					.getParent();
			parentActivity.startChildActivity(
					"FrequentMessageActivity", _intent);
			break;
	 case R.id.tvlive:
		 _intent = new Intent(ContactActivity.this,
					LiveStream.class);

			 parentActivity = (AnimatedActivity) ContactActivity.this
					.getParent();
			parentActivity
					.startChildActivity("LiveStream", _intent);
		break;
	 case R.id.llllive:
		 _intent = new Intent(ContactActivity.this,
					LiveStream.class);

			 parentActivity = (AnimatedActivity) ContactActivity.this
					.getParent();
			parentActivity
					.startChildActivity("LiveStream", _intent);
		break;
	 case R.id.ivlive:
		 _intent = new Intent(ContactActivity.this,
					LiveStream.class);

			 parentActivity = (AnimatedActivity) ContactActivity.this
					.getParent();
			parentActivity
					.startChildActivity("LiveStream", _intent);
		break;
	 case R.id.btnSearch:
			Intent videoIntent = new Intent(ContactActivity.this,
					SearchActivity.class);

			AnimatedActivity parentActivity = (AnimatedActivity) ContactActivity.this
					.getParent();
			parentActivity.startChildActivity("LiveStream", videoIntent);
		break;
		
	 case R.id.btnfollowusfb:
		 String facebookUrl = "https://www.facebook.com/360MEA";
			try {
				int versionCode = getPackageManager().getPackageInfo(
						"com.facebook.katana", 0).versionCode;
				if (versionCode >= 3002850) {
					Uri uri = Uri.parse("fb://facewebmodal/f?href="
							+ facebookUrl);
					startActivity(new Intent(Intent.ACTION_VIEW, uri));
					;
				} else {
					// open the Facebook app using the old method
					// (fb://profile/id or fb://page/id)
					startActivity(new Intent(Intent.ACTION_VIEW, Uri
							.parse("fb://page/336227679757310")));
				}
			} catch (PackageManager.NameNotFoundException e) {
				// Facebook is not installed. Open the browser
				startActivity(new Intent(Intent.ACTION_VIEW, Uri
						.parse(facebookUrl)));
			}
		 break;
		 
	 case R.id.btnfollowustw:
		 stringTwitter = "twitter.com/360MEA";
			Intent viewIntent = new Intent("android.intent.action.VIEW",
					Uri.parse("http://" + stringTwitter));
			startActivity(viewIntent);
		 /*if(appInstalledOrNot("com.whatsapp"))
		 {
			 Intent sendIntent = new Intent();
			    sendIntent.setAction(Intent.ACTION_SEND);
			    sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
			    sendIntent.setType("text/plain");
			sendIntent.setPackage("com.whatsapp");
			    startActivity(sendIntent);
		 }
		 else
		 {
			 Toast.makeText(ContactActivity.this, "Whats app not found.", Toast.LENGTH_LONG).show();
		 }*/
		 break;
		 
	 case R.id.btnemailus:
		  Intent emailIntent = new Intent(Intent.ACTION_SEND);
			emailIntent.putExtra(Intent.EXTRA_EMAIL,
					new String[] { "info@360mea.com"});
			emailIntent.putExtra(Intent.EXTRA_SUBJECT,
					"Please contact me about 360 MEA - 360° VideoVR Player");
/*			emailIntent.putExtra(Intent.EXTRA_TEXT,
					" Please keep me notified of the next live broadcast.");
*/
			//emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");

			// need this to prompts email client only
			emailIntent.setType("message/rfc822");

			startActivity(Intent.createChooser(emailIntent,
					"Select an Email Client:"));
		  
		 break;

		}
	}
	  @SuppressWarnings("unused")
	private boolean appInstalledOrNot(String uri) {
	        PackageManager pm = getPackageManager();
	        boolean app_installed;
	        try {
	            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
	            app_installed = true;
	        }
	        catch (PackageManager.NameNotFoundException e) {
	            app_installed = false;
	        }
	        return app_installed;
	    }

}