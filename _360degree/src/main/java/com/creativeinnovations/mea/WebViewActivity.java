package com.creativeinnovations.mea;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.ImageView;

import com.creativeinnovations.mea.network.CommonUtility;

public class WebViewActivity extends Activity implements OnClickListener{

	WebView wb;
	ImageView ivback;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		wb = (WebView) findViewById(R.id.wb);
		  WebSettings settings = wb.getSettings();
	        settings.setPluginState(PluginState.ON);
	        ivback = (ImageView) findViewById(R.id.ivback);
	        ivback.setOnClickListener(this);
	        wb.getSettings().setJavaScriptEnabled(true);
	        wb.getSettings().setDomStorageEnabled(true);
	        wb.getSettings().setBuiltInZoomControls(true);
	        wb.setInitialScale(100);
	        wb.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
	        CommonUtility.getGlobalString(this, "HTML");
		wb.loadUrl(CommonUtility.getGlobalString(this, "HTML"));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
		case R.id.ivback:
			finish();
			break;
		}
		
	}

}
