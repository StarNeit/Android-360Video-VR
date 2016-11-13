package com.creativeinnovations.mea;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.creativeinnovations.mea.network.CommonUtility;
import com.creativeinnovations.mea.network.WebAPICallingClass;
import com.degree.hitaishin.connection.WebServiceNaming;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.hitaishin.notification.Config;

public class SignUpActivity extends Activity implements OnClickListener,OnCheckedChangeListener{

	EditText etfirstname,etlastname,etemail,etpassword,etcfpassword;
	RadioButton rbmale,rbfemale;
	Button btnsignup;
	WebAPICallingClass _webserviceCall;
	HashMap<String, String> _dataToPost;
	String _gender = "";
	public static String regId;
	GoogleCloudMessaging gcm;
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initializeActivity();
		bindControls();
		checkPlayServices();
		//Toast.makeText(this, registerGCM(), Toast.LENGTH_SHORT).show();
	}

	private void initializeActivity() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_sign_up);
		etfirstname = (EditText) findViewById(R.id.etfirstname);
		etfirstname.addTextChangedListener(new MyTextWatcher(etfirstname));
		etlastname = (EditText) findViewById(R.id.etlastname);
		etlastname.addTextChangedListener(new MyTextWatcher(etlastname));
		etemail = (EditText) findViewById(R.id.etemail);
		etemail.addTextChangedListener(new MyTextWatcher(etemail));
		etpassword = (EditText) findViewById(R.id.etpassword);
		etpassword.addTextChangedListener(new MyTextWatcher(etpassword));
		etcfpassword = (EditText) findViewById(R.id.etcfpassword);
		etcfpassword.addTextChangedListener(new MyTextWatcher(etcfpassword));
		rbmale = (RadioButton) findViewById(R.id.rbmale);
		rbmale.setOnClickListener(this);
		rbfemale = (RadioButton) findViewById(R.id.rbfemale);
		rbfemale.setOnClickListener(this);
		btnsignup = (Button) findViewById(R.id.btnsignup);
		btnsignup.setOnClickListener(this);
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
	
	public String registerGCM() {

		gcm = GoogleCloudMessaging.getInstance(this);
		regId = getRegistrationId(this);

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

	
	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging
								.getInstance(SignUpActivity.this);
					}
					regId = gcm.register(Config.GOOGLE_PROJECT_ID);
					Log.e("RegisterActivity", "registerInBackground - regId: "
							+ regId);
					msg = "Device registered, registration ID=" + regId;

					storeRegistrationId(SignUpActivity.this, regId);
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
	
	private void bindControls() {
		// TODO Auto-generated method stub
		_webserviceCall =  new WebAPICallingClass();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		@SuppressWarnings("unused")
		int id = item.getItemId();
		/*if (id == R.id.action_settings) {
			return true;
		}*/
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnsignup:
			if(etfirstname.getText().toString().trim().equals(""))
			{
				etfirstname.setError("First Name cannot be left blank.");
				return;
			}
			
			if(etlastname.getText().toString().trim().equals(""))
			{
				etlastname.setError("Last Name cannot be left blank.");
				return;
			}
			
			if(etemail.getText().toString().trim().equals(""))
			{
				etemail.setError("Email cannot be left blank.");
				return;
			}
			else
			{
				if(!android.util.Patterns.EMAIL_ADDRESS.matcher(etemail.getText().toString().trim()).matches())
				{
					etemail.setError("Email should be in proper format.");
					return;
				}
			}
			
			if(etpassword.getText().toString().trim().equals(""))
			{
				etpassword.setError("Password cannot be left blank.");
				return;
			}
			
			if(etcfpassword.getText().toString().trim().equals(""))
			{
				etcfpassword.setError("Confirm Password cannot be left blank.");
				return;
			}
			
			if(!etpassword.getText().toString().trim().equals(etcfpassword.getText().toString().trim()))
			{
				//etcfpassword.setError("Password and confirm password are not matched");
				Toast.makeText(SignUpActivity.this, "Password and confirm password are not matched", Toast.LENGTH_LONG).show();
				return;
			}
			
			if(CommonUtility.isNetworkAvailable(SignUpActivity.this))
			{
				new SignUPWebServiceCall().execute();
		
			 }
			else
			{
				Toast.makeText(SignUpActivity.this, "Internet connection is too slow or might be not working.", Toast.LENGTH_LONG).show();
				return;
			}

			
			break;
		case R.id.rbfemale:
			
				rbfemale.setChecked(true);
				rbmale.setChecked(false);
			
			break;
		case R.id.rbmale:
				rbfemale.setChecked(false);
				rbmale.setChecked(true);
			
			break;

		}
	}
	
	 private class MyTextWatcher implements TextWatcher {

	        private View view;

	        private MyTextWatcher(View view) {
	            this.view = view;
	        }

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				switch (view.getId()) {
                case R.id.etfirstname:
                	etfirstname.setError(null);
                    break;
                    
                case R.id.etlastname:
                	etlastname.setError(null);
                	break;
                	
                case R.id.etemail:
                	etemail.setError(null);
                	break;
                	
                case R.id.etcfpassword:
                	etcfpassword.setError(null);
                	break;
                	
                case R.id.etpassword:
                	etpassword.setError(null);
                	break;
            }
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}

	      
	    }
	 
	 public class SignUPWebServiceCall extends AsyncTask<Void, Void, JSONObject>
		{
			ProgressDialog _dialog;
			ArrayList<NameValuePair> nameValuePaire;
			
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				_dialog =  new ProgressDialog(SignUpActivity.this);
				_dialog.setMessage("Please Wait...");
				_dialog.setCancelable(false);
				_dialog.show();
				_dataToPost =  new HashMap<String, String>();
				_dataToPost.put("action", "signup");
				 _dataToPost.put("deviceid", registerGCM());
				 _dataToPost.put("model", "android");
				 _dataToPost.put("email", etemail.getText().toString());
				 _dataToPost.put("firstname", etfirstname.getText().toString());
				 _dataToPost.put("lastname", etlastname.getText().toString());
				 _dataToPost.put("regtype", "normal");
				 if(rbmale.isChecked())
				 {
					 _dataToPost.put("gender", "male"); 
				 }
				 else
				 {
					 _dataToPost.put("gender", "female");
				 }
				_dataToPost.put("password", etpassword.getText().toString());
			}

			@Override
			protected JSONObject doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return _webserviceCall.performPostCallDirect(_dataToPost,"");
			}
		
			
			@Override
			protected void onPostExecute(JSONObject result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				_dialog.dismiss();
				if(result != null)
				{
					try 
					{
						if(result.getInt("status") == 1)
						{
							CommonUtility.setGlobalString(SignUpActivity.this, "USER_ID", result.getString("id"));
							Toast.makeText(SignUpActivity.this, result.getString("message"), Toast.LENGTH_LONG).show();
							Intent _intent = new Intent(SignUpActivity.this, AwesomeActivity.class);
							_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
							startActivity(_intent);
							SignUpActivity.this.finish();
						}
						else
						{
							Toast.makeText(SignUpActivity.this, result.getString("message"), Toast.LENGTH_LONG).show();
						}
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
				{
					Toast.makeText(SignUpActivity.this, "Some error occured please try again.", Toast.LENGTH_LONG).show();
				}
			}
		}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if(buttonView == rbfemale)
		{
			rbfemale.setSelected(true);
			rbmale.setSelected(false);
		}
		
		if(buttonView == rbmale)
		{
			rbfemale.setSelected(false);
			rbmale.setSelected(true);
		}
	}
}
