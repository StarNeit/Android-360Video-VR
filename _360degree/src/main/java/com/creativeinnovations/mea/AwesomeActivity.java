package com.creativeinnovations.mea;

import android.annotation.TargetApi;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

/**
 * @author Adil Soomro
 * 
 */
@TargetApi(Build.VERSION_CODES.DONUT)
@SuppressWarnings("deprecation")
public class AwesomeActivity extends TabActivity {
	TabHost tabHost;
	Bundle _extra;
	/** Called when the activity is first created. */

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_extra =  this.getIntent().getExtras();
		setContentView(R.layout.main);
		
		tabHost = getTabHost();
		final String mimeType = "text/html";
		final String encoding = "UTF-8";
		setTabs();
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
//				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//				imm.hideSoftInputFromWindow(
//						tabHost.getApplicationWindowToken(), 0);


				if (!tabId.equals("tabInfo")) {
					if (InfoActivity.youTubePlayerView != null) {

						InfoActivity.youTubePlayerView.loadDataWithBaseURL("",
								"", mimeType, encoding, "");
					}
				} else {
					if (InfoActivity.youTubePlayerView != null) {

						String html = getHTML("pg2m6EkIt84");
						InfoActivity.youTubePlayerView.loadDataWithBaseURL("",
								html, mimeType, encoding, "");
					}
				}
			}

		});

	}

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

	private void setTabs() {
		addTab("Home", R.drawable.tabbar_selection, HomeGroupActivity.class);
		addTab("Downloads", R.drawable.download_new_assets,
				DownloadGroupActivity.class);

	/*	addTab("Contact", R.drawable.contact_tab_new, ContactGroupActivity.class);
		addTab("Info", R.drawable.info_tab_new, InfoGroupActivity.class);*/
		
		addTab("Contact", R.drawable.share_new_assets, InfoGroupActivity.class);
		addTab("Info", R.drawable.contact_new_assets, ContactGroupActivity.class);

	}

	private void addTab(String labelId, int drawableId, Class<?> c) {
		Intent intent = new Intent(this, c);
		TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);
		View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		title.setText(labelId);
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		icon.setImageResource(drawableId);
		spec.setIndicator(tabIndicator);
		spec.setContent(intent);
		tabHost.addTab(spec);
		if(_extra != null)
		{
			tabHost.setCurrentTab(1);
		}
		
	}
	
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// TODO Auto-generated method stub
	super.onActivityResult(requestCode, resultCode, data);
//	 ContactActivity.uiHelper.onActivityResult(requestCode, resultCode, data, null);
  }

}