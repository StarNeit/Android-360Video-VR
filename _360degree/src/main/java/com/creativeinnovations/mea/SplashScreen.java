/**
 * 
 */
package com.creativeinnovations.mea;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.widget.ImageView;

import com.creativeinnovations.mea.network.CommonUtility;
import com.creativeinnovations.mea.splashsupport.GifAnimationDrawable;

@TargetApi(Build.VERSION_CODES.FROYO)
public class SplashScreen extends Activity {
	ImageView splash;
	private GifAnimationDrawable  big;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash_actvity);
		splash = (ImageView) findViewById(R.id.splash);
		GetHasKey();
		try{
			
			big = new GifAnimationDrawable(getResources().openRawResource(R.raw.splash));
		//	big.setOneShot(true);
		
			if(splash.getDrawable() == null) splash.setImageDrawable(big);
			big.setVisible(true, true);
		}catch(IOException ioe){
			
		}
		
		SimpleDateFormat ss = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		String currentdate = ss.format(date);
		Log.e("date", currentdate);
 
			final Handler handler = new Handler();
			Runnable runnable = new Runnable() {
				@SuppressWarnings("unused")
				int i = 0;

				public void run() {
				
					handler.postDelayed(this, 180); // for interval...
				}
			};
			handler.postDelayed(runnable, 0); // for initial delay..
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					// This method will be executed once the timer is over
					// Start your app Next activity
					/*try {
						PackageInfo info = getPackageManager().getPackageInfo(
								"com.creativeinnovations.mea",
								PackageManager.GET_SIGNATURES);
						for (Signature signature : info.signatures) {
							MessageDigest md = MessageDigest.getInstance("SHA");
							md.update(signature.toByteArray());
							Log.e("KeyHash:", Base64.encodeToString(
									md.digest(), Base64.DEFAULT));
						}
					} catch (NameNotFoundException e) {

					} catch (NoSuchAlgorithmException e) {

					}*/
					Log.e("KeyHash:", "sadfs");
					/*Intent in = new Intent(SplashScreen.this,
							AwesomeActivity.class);
					startActivity(in);
					finish();*/
					if(CommonUtility.getUserId(SplashScreen.this).equals(""))
					{
						Intent in = new Intent(SplashScreen.this,
								LoginActivity.class);
						startActivity(in);
						finish();	
					}
					else
					{
						Intent in = new Intent(SplashScreen.this,
								AwesomeActivity.class);
						startActivity(in);
						finish();
					}
				}
			}, 2700);


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void GetHasKey() {

	    PackageInfo info;
	    try
	    {
	        info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
	        for (Signature signature : info.signatures)
	        {
	            MessageDigest md;
	            md = MessageDigest.getInstance("SHA");
	            md.update(signature.toByteArray());
	            String keyhash = new String(Base64.encode(md.digest(), 0));
	            // String something = new String(Base64.encodeBytes(md.digest()));
	            Log.e("keyhash", "keyhash= " + keyhash);
	            System.out.println("keyhash= " + keyhash);
	        }
	    }
	    catch (NameNotFoundException e1)
	    {
	        Log.e("name not found", e1.toString());
	    }
	    catch (NoSuchAlgorithmException e)
	    {
	        Log.e("no such an algorithm", e.toString());
	    }
	    catch (Exception e)
	    {
	        Log.e("exception", e.toString());
	    }

	}
}
