
package com.creativeinnovations.mea;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.degree.hitaishin.connection.WebServiceNaming;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
//import com.facebook.android.DialogError;
//import com.facebook.android.Facebook;
//import com.facebook.android.Facebook.DialogListener;
//import com.facebook.android.FacebookError;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

public class InfoActivity extends Activity implements OnClickListener{

	public static final String API_KEY = "AIzaSyBlKo1SYAFsg4d-OuEy_DkFx7hz0Nacp_4";
	public static final String VIDEO_ID = "pg2m6EkIt84";
//	private Facebook facebook;
	TextView textInfo, textTitle;
	 public static WebView youTubePlayerView;
	ImageButton btnSearch;
	
	TextView tvwhatsnew,tvpopular,tvlive;
	LinearLayout llllive;
	ImageView ivlive;
	
	AnimatedActivity parentActivity;
	Intent _intent;
	
	ImageView btnrate,btnwhatsapp,btnfblogin;
	private ShareDialog shareDialog;
	private CallbackManager callbackManager;
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	public static final int USER_MOBILE = 0;
	public static final int USER_DESKTOP = 1;
	private static final String APP_ID = "893530320657596"; // 893530320657596

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.info_page);
//		facebook = new Facebook(APP_ID);
		btnSearch = (ImageButton) findViewById(R.id.btnSearch);
		textInfo = (TextView) findViewById(R.id.textInfo);
		textTitle = (TextView) findViewById(R.id.textTitle);
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
		btnSearch = (ImageButton) findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(this);
		btnrate = (ImageView) findViewById(R.id.btnrate);
		btnrate.setOnClickListener(this);
		btnwhatsapp = (ImageView) findViewById(R.id.btnwhatsapp);
		btnwhatsapp.setOnClickListener(this);
		btnfblogin = (ImageView) findViewById(R.id.btnfblogin);
		btnfblogin.setOnClickListener(this);
		
		FacebookSdk.sdkInitialize(getApplicationContext());
		callbackManager = CallbackManager.Factory.create();
		shareDialog = new ShareDialog(this);
		
	/*	new InfoTaskRunner().execute();
		youTubePlayerView = (WebView) findViewById(R.id.youtube_player);
		youTubePlayerView.getSettings().setJavaScriptEnabled(true);
		youTubePlayerView.getSettings().setPluginState(
				WebSettings.PluginState.ON);
		// youTubePlayerView.getSettings().setUserAgent(USER_MOBILE);
		youTubePlayerView.setWebChromeClient(new WebChromeClient() {
		});
		
		youTubePlayerView.setVerticalScrollBarEnabled(false);
		youTubePlayerView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		youTubePlayerView.setScrollContainer(false);
		// youtube video url
		// //http://www.youtube.com/watch?v=WM5HccvYYQg\\
		
		youTubePlayerView.setOnTouchListener(new View.OnTouchListener() {
		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
		      return (event.getAction() == MotionEvent.ACTION_MOVE);
		    }
		  });

		final String mimeType = "text/html";
		final String encoding = "UTF-8";
		String html = getHTML("pg2m6EkIt84");
		youTubePlayerView.loadDataWithBaseURL("", html, mimeType, encoding, "");
*/	}

	public String getHTML(String videoId) {

		String html = "<hmtl><head><body bgcolor=\"#000000\"><iframe class=\"youtube-player\" "
				+ "style=\"border: 0; width: 100%; height: 100%;"
				+ "padding:0px; margin:0px\" "
				+ "id=\"ytplayer\" type=\"text/html\" "
				+ "src=\"http://www.youtube.com/embed/"
				+ videoId
				+ "?fs=0\" frameborder=\"0\" "
				+ "allowfullscreen autobuffer "
				+ "controls onclick=\"this.play()\">\n"
				+ "</iframe>\n</body></head></html>";

		/**
		 * <iframe id="ytplayer" type="text/html" width="640" height="360"
		 * src="https://www.youtube.com/embed/WM5HccvYYQg" frameborder="0"
		 * allowfullscreen>
		 **/

		return html;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.android.youtube.player.YouTubeBaseActivity#onResume()
	 */

	@Override
	protected void onDestroy() {
		super.onDestroy();
		final WebView webview = (WebView) findViewById(R.id.youtube_player);
		// Calling .clearView does not stop the flash player must load new data
		webview.destroy();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		if (youTubePlayerView != null) {
			// Remove the WebView from the old placeholder
			youTubePlayerView.destroy();
		}

		super.onConfigurationChanged(newConfig);

	;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Save the state of the WebView
		//youTubePlayerView.saveState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		// Restore the state of the WebView
		youTubePlayerView.restoreState(savedInstanceState);
	}


	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
		case R.id.btnSearch:
			_intent = new Intent(InfoActivity.this,SearchActivity.class);
			parentActivity = (AnimatedActivity) InfoActivity.this.getParent();
			parentActivity.startChildActivity("LiveStream", _intent);
			break;
		case R.id.tvwhatsnew:
			_intent = new Intent(InfoActivity.this,VideoListActivity.class);
			_intent.putExtra(WebServiceNaming.TAG_WHATS_NEW,"1");
			_intent.putExtra(WebServiceNaming.TAG_CATEGORY_NAME,"");
			_intent.putExtra(WebServiceNaming.TAG_IMAGE,1);
			_intent.putExtra(WebServiceNaming.TAG_CITY, "");
			_intent.putExtra(WebServiceNaming.TAG_SUB_CATEGORY_ID, "");
			_intent.putExtra(WebServiceNaming.TAG_KEYWORD, "");
			parentActivity = (AnimatedActivity) InfoActivity.this.getParent();
			parentActivity.startChildActivity("FrequentMessageActivity", _intent);
			break;

	 case R.id.tvpopular:
		 _intent = new Intent(InfoActivity.this,VideoListActivity.class);
			_intent.putExtra(WebServiceNaming.TAG_CATEGORY_NAME,"");
			_intent.putExtra(WebServiceNaming.POPULAR,"1");
			_intent.putExtra(WebServiceNaming.TAG_IMAGE,1);
			_intent.putExtra(WebServiceNaming.TAG_CITY, "");
			_intent.putExtra(WebServiceNaming.TAG_SUB_CATEGORY_ID, "");
			_intent.putExtra(WebServiceNaming.TAG_KEYWORD, "");
			parentActivity = (AnimatedActivity) InfoActivity.this.getParent();
			parentActivity.startChildActivity("FrequentMessageActivity", _intent);
			break;
	 case R.id.tvlive:
		 _intent = new Intent(InfoActivity.this,LiveStream.class);
		 parentActivity = (AnimatedActivity) InfoActivity.this.getParent();
		 parentActivity.startChildActivity("LiveStream", _intent);
		break;
	 case R.id.llllive:
		 _intent = new Intent(InfoActivity.this,LiveStream.class);
		 parentActivity = (AnimatedActivity) InfoActivity.this.getParent();
		 parentActivity.startChildActivity("LiveStream", _intent);
		break;
	 case R.id.ivlive:
		 _intent = new Intent(InfoActivity.this,LiveStream.class);
		 parentActivity = (AnimatedActivity) InfoActivity.this.getParent();
		parentActivity.startChildActivity("LiveStream", _intent);
		break;
		
	 case R.id.btnrate:
		 final AlertDialog alertDialog = new AlertDialog.Builder(
					getParent()).create();
					alertDialog.setTitle("Rate Our App");
					alertDialog.setMessage("If you like our app rate us on app store!");
					alertDialog.setIcon(R.drawable.ic_launcher);
				
					alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					alertDialog.dismiss();
					Intent _intent = new Intent(
					Intent.ACTION_VIEW,
					Uri.parse("https://play.google.com/store/apps/details?id=com.creativeinnovations.mea"));
					startActivity(_intent);
					}
					});
					alertDialog.setButton2("Cancel",
					new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					alertDialog.dismiss();
					}
					});
				
					alertDialog.show();
		break;

	 case R.id.btnwhatsapp:
		 if(appInstalledOrNot("com.whatsapp"))
		 {
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_TEXT, "Install this free app, view and control exclusive amazing 360 degrees videos. www.360mea.com");
			sendIntent.setType("text/plain");
			sendIntent.setPackage("com.whatsapp");
			startActivity(sendIntent);
		 }
		 else
		 {
			 Toast.makeText(InfoActivity.this, "Whats app not found.", Toast.LENGTH_LONG).show();
		 }
		break;

		
	 case R.id.btnfblogin:

			// TODO Auto-generated method stub


			// TODO Auto-generated method stub

			List<Intent> targetedShareIntents = new ArrayList<Intent>();

			Intent facebookIntent = getShareIntent("com.facebook.katana",
					"subject",
					"Install this app, it have an awesome experience of 360 degree ."+"https://play.google.com/store/apps/details?id=com.creativeinnovations.mea");

			if (facebookIntent != null) {
				targetedShareIntents.add(facebookIntent);

				Intent chooser = Intent.createChooser(
						targetedShareIntents.remove(0), "Delen");

				chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS,
						targetedShareIntents.toArray(new Parcelable[] {}));
				chooser.putExtra(Intent.EXTRA_TEXT,
						"Install this app, it have an awesome experience of 360 degree ."+"https://play.google.com/store/apps/details?id=com.creativeinnovations.mea");
				startActivity(chooser);
			} else {

				//textFacebook = video.getMeta_data();
				Bundle parameters = new Bundle();
				parameters.putString("fields", "Install this free app, view and control exclusive amazing 360 degrees videos.");
				parameters.putString("link",
						"https://play.google.com/store/apps/details?id=com.creativeinnovations.mea");
				
//				ShareLinkContent content = new ShareLinkContent.Builder()
//		        .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.creativeinnovations.mea"))
//		        .build();
				
				if (ShareDialog.canShow(ShareLinkContent.class))
				{
					ShareLinkContent linkContent = new ShareLinkContent.Builder()
					        .setContentTitle("360 MEA")
					        .setContentDescription(
					                 "Install this free app, view and control exclusive amazing 360 degrees videos.")
					        .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.creativeinnovations.mea"))
					        .build();

					shareDialog.show(linkContent);
				}
				

			/*	facebook.dialog(getParent(), "feed", parameters,
						new DialogListener() {

							@Override
							public void onFacebookError(FacebookError arg0) {
								// Display your message for facebook error

							}

							@Override
							public void onError(DialogError arg0) {
								// Display your message for error

							}

							@Override
							public void onComplete(Bundle arg0) {
								// Display your message on share scuccessful

							}

							@Override
							public void onCancel() {
								// Display your message on dialog cancel

							}
						});
			*/
			}
			// share();
		
		
		break;

		
		
		}
	}
	
	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    callbackManager.onActivityResult(requestCode, resultCode, data);
	}
	
	private Intent getShareIntent(String type, String subject, String text) {

		boolean found = false;
		Intent share = new Intent(android.content.Intent.ACTION_SEND);

		share.setType("text/plain");

		// gets the list of intents that can be loaded.
		List<ResolveInfo> resInfo = getPackageManager()
				.queryIntentActivities(share, 0);
		System.out.println("resinfo: " + resInfo);
		if (!resInfo.isEmpty()) {
			for (ResolveInfo info : resInfo) {
				if (info.activityInfo.packageName.toLowerCase().equals(type)
						|| info.activityInfo.name.toLowerCase().equals(type)) {

					share.putExtra(Intent.EXTRA_SUBJECT, subject);
					share.putExtra(Intent.EXTRA_TEXT, text);
					share.putExtra(
							"com.facebook.platform.extra.APPLICATION_ID",
							"893530320657596");
					share.putExtra("com.facebook.platform.extra.ACCESS_TOKEN",
							"d32597ddbb97a79414dc98483f3d6156");

					share.setPackage(info.activityInfo.packageName);
					found = true;
					break;
				}
			}
			if (!found)
				return null;

			return share;
		}
		return null;
	}
	
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