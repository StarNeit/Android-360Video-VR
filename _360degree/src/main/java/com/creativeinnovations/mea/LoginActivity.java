package com.creativeinnovations.mea;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.creativeinnovations.mea.network.CommonUtility;
import com.creativeinnovations.mea.network.WebAPICallingClass;
import com.degree.hitaishin.connection.WebServiceNaming;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.hitaishin.notification.Config;

@SuppressLint("WorldWriteableFiles")
@SuppressWarnings("deprecation")
public class LoginActivity extends Activity implements OnClickListener {
	EditText Username, Password;
	Button Login, Login_Facebook;
	TextView Signup, Forgot_password;
	private static String APP_ID = "893530320657596";
	public static int flag = 0;
	public static String regId;
	GoogleCloudMessaging gcm;
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	public static int other_flag = 0;
	public CallbackManager callbackmanager;
	String FILENAME = "AndroidSSO_data";
	private SharedPreferences mPrefs;
	String facebook_name;
	String facebook_email = "";
	String facebook_id;
	String facebookjson;
	ProgressDialog dialog;
	SharedPreferences shared;
	ImageView btnfblogin;
	Button btnsignup, btnlogin;
	WebAPICallingClass _webserviceCall;
	HashMap<String, String> _dataToPost;

	String firstname = "", lastname = "", email = "", gender = "", fbid = "",
			regtype = "facebook";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		shared = this.getSharedPreferences("360Degree", MODE_WORLD_WRITEABLE);
		// String android_id = Secure.getString(getContentResolver(),
		// Secure.ANDROID_ID);

		_webserviceCall = new WebAPICallingClass();
		// try {
		// PackageInfo info = getPackageManager().getPackageInfo(
		// getPackageName(),
		// PackageManager.GET_SIGNATURES);
		// for (Signature signature : info.signatures) {
		// MessageDigest md = MessageDigest.getInstance("SHA");
		// md.update(signature.toByteArray());
		// Log.d("KeyHash:", Base64.encodeToString(md.digest(),
		// Base64.DEFAULT));
		// }
		// } catch (NameNotFoundException e) {
		//
		// } catch (NoSuchAlgorithmException e) {
		//
		// }

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();

		StrictMode.setThreadPolicy(policy);

		// Initialize SDK before setContentView(Layout ID)
		FacebookSdk.sdkInitialize(getApplicationContext());

		setContentView(R.layout.login);
		checkPlayServices();
		registerGCM();
		// Toast.makeText(this, registerGCM(), Toast.LENGTH_SHORT).show();
		initUI();
		if (shared.getString("username", null) != null) {
			Username.setText(shared.getString("username", null));
			Password.setText(shared.getString("password", null));
			Intent in = new Intent(LoginActivity.this, AwesomeActivity.class);
			in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(in);
			finish();

		}

		// Log.i("Anish", printKeyHash(this));
	}

	// Private method to handle Facebook login and callback
	private void onFblogin() {
		callbackmanager = CallbackManager.Factory.create();

		// Set permissions
		LoginManager.getInstance().logInWithReadPermissions(this,
				Arrays.asList("email", "user_photos", "public_profile"));

		LoginManager.getInstance().registerCallback(callbackmanager,
				new FacebookCallback<LoginResult>() {

					@Override
					public void onSuccess(final LoginResult loginResult) {

						System.out.println("Success");
						GraphRequest request = GraphRequest.newMeRequest(
								loginResult.getAccessToken(),
								new GraphRequest.GraphJSONObjectCallback() {
									@Override
									public void onCompleted(JSONObject json,
											GraphResponse response) {
										if (response.getError() != null) {
											// handle error
											System.out.println("ERROR");
										} else {
											System.out.println("Success");
											try {

												// firstname =
												// _Object.getString("first_name");
												// fbid =
												// _Object.getString("id");
												// lastname =
												// _Object.getString("last_name");
												// gender =
												// _Object.getString("gender");
												// if(_Object.has("email"))
												// {
												// email =
												// _Object.getString("email");
												// }
												// else
												// {
												// email =
												// firstname+"."+lastname+"@facebook.com";
												// }

												String jsonresult = String
														.valueOf(json);
												System.out
														.println("JSON Result"
																+ jsonresult);

												String str_email = json
														.getString("email");
												fbid = json.getString("id");
												// String str_firstname =
												// json.getString("first_name");
												// String str_lastname =
												// json.getString("last_name");
												gender = json
														.getString("gender");
												// String str_birthday =
												// json.getString("birthday");
												// String str_link =
												// json.getString("link");
												// String str_locale =
												// json.getString("locale");
												firstname = json
														.getString("name");
												// lastname =
												// json.getString("last_name");
												// String pic_url =
												// json.getJSONObject("picture").getJSONObject("data").getString("url");

												// name.setText(str_name);
												// email.setText(str_email);
												// gender.setText(str_gender);
												// id.setText(str_id);
												// //
												// birthday.setText(str_birthday);
												// // local.setText(str_locale);
												// link.setText(pic_url);

												// InputStream in =
												// (InputStream) new
												// URL(pic_url).getContent();
												// Bitmap bitmap =
												// BitmapFactory.decodeStream(in);
												// if(bitmap!=null){
												// fb_profile_pic.setImageBitmap(bitmap);
												// }
												// else {
												// //
												// Toast.makeText(,"null it is",Toast.LENGTH_SHORT).show();
												// }

												if (str_email == null
														&& str_email.isEmpty()) {
													email = firstname + "."
															+ lastname
															+ "@facebook.com";
												}

												new SignUPWebServiceCall()
														.execute();

												// Edit Preferences and update
												// facebook acess_token
												mPrefs = getPreferences(MODE_PRIVATE);
												SharedPreferences.Editor editor = mPrefs
														.edit();
												editor.putString(
														"access_token",
														String.valueOf(loginResult
																.getAccessToken()));
												editor.putString(
														"access_expires",
														String.valueOf(loginResult
																.getAccessToken()));
												editor.commit();

											} catch (JSONException e) {
												e.printStackTrace();
											}
										}
									}

								});
						Bundle parameters = new Bundle();
						parameters
								.putString("fields",
										"id,name,email,gender,birthday,picture.width(300)");
						request.setParameters(parameters);
						request.executeAsync();

					}

					@Override
					public void onCancel() {
						Log.v("nik", "On cancel");
					}

					@Override
					public void onError(FacebookException error) {
						Log.v("nik", error.toString());
					}
				});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (data != null && resultCode == RESULT_OK) {
			callbackmanager.onActivityResult(requestCode, resultCode, data);
		}
	}

	public static String printKeyHash(Activity context) {
		PackageInfo packageInfo;
		String key = null;
		try {
			// getting application package name, as defined in manifest
			String packageName = context.getApplicationContext()
					.getPackageName();

			// Retriving package info
			packageInfo = context.getPackageManager().getPackageInfo(
					packageName, PackageManager.GET_SIGNATURES);

			Log.e("Package Name=", context.getApplicationContext()
					.getPackageName());

			for (Signature signature : packageInfo.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				key = new String(Base64.encode(md.digest(), 0));

				// String key = new String(Base64.encodeBytes(md.digest()));
				Log.e("Key Hash=", key);
			}
		} catch (NameNotFoundException e1) {
			Log.e("Name not found", e1.toString());
		} catch (NoSuchAlgorithmException e) {
			Log.e("No such an algorithm", e.toString());
		} catch (Exception e) {
			Log.e("Exception", e.toString());
		}

		return key;
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

	// ////////

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
								.getInstance(LoginActivity.this);
					}
					regId = gcm.register(Config.GOOGLE_PROJECT_ID);
					Log.e("RegisterActivity", "registerInBackground - regId: "
							+ regId);
					msg = "Device registered, registration ID=" + regId;

					storeRegistrationId(LoginActivity.this, regId);
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

	private void storeRegistrationId( Context context, String regId ) {
		final SharedPreferences prefs = getSharedPreferences(
				SplashScreen.class.getSimpleName(), Context.MODE_PRIVATE);
		int appVersion = getAppVersion(context);
		Log.e("Save", "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(WebServiceNaming.TAG_NOTIFICATION_ID, regId);
		editor.putInt(WebServiceNaming.TAG_APP_VERSION, appVersion);
		editor.commit();
	}

	private void initUI() {
		// TODO Auto-generated method stub
		/*
		 * Username = (EditText) findViewById(R.id.username); Password =
		 * (EditText) findViewById(R.id.password);
		 * 
		 * Login = (Button) findViewById(R.id.Login); Signup = (TextView)
		 * findViewById(R.id.Signup); Login_Facebook = (Button)
		 * findViewById(R.id.Login_Facebook); Forgot_password = (TextView)
		 * findViewById(R.id.Forgot_password);
		 */

		btnfblogin = (ImageView) findViewById(R.id.btnfblogin);
		btnsignup = (Button) findViewById(R.id.btnsignup);
		btnlogin = (Button) findViewById(R.id.btnlogin);

		btnfblogin.setOnClickListener(this);
		btnsignup.setOnClickListener(this);
		btnlogin.setOnClickListener(this);

		/*
		 * Login.addTextChangedListener(new TextWatcher() {
		 * 
		 * @Override public void onTextChanged(CharSequence s, int start, int
		 * before, int count) { }
		 * 
		 * @Override public void beforeTextChanged(CharSequence s, int start,
		 * int count, int after) { }
		 * 
		 * @Override public void afterTextChanged(Editable s) { if
		 * (Login.getText().toString().length() <= 0)
		 * Login.setError("Please Enter Email Id /Phone No."); } });
		 */
		// mAsyncRunner = new AsyncFacebookRunner(facebook);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnsignup:
			Intent in = new Intent(LoginActivity.this, SignUpActivity.class);
			startActivity(in);
			// finish();
			// Login.setClickable(false);
			// ProgressDialog pr = new ProgressDialog(LoginActivity.this);
			// pr.setMessage("Loading..... ");
			// //Animation shake = AnimationUtils.loadAnimation(this,
			// R.anim.shake);

			// if (Username.getText().toString().trim().equals("")) {
			// // DialogCreate.messageDialog(LoginActivity.this, "Alert",
			// // "Please Enter User Name");
			// Username.setError("Please Enter Email Id /Phone No.");
			// findViewById(R.id.username).startAnimation(shake);
			// }
			//
			// else if (Vaildator.isValidEmail(Username.getText().toString()
			// .trim()) == false
			// && Vaildator.isValidMobile(Username.getText().toString()
			// .trim()) == false) {
			// // DialogCreate.messageDialog(LoginActivity.this, "Alert",
			// // "Enter Valid Email ID");
			// Username.setError("Enter Valid Email Id /Phone No.");
			//
			// findViewById(R.id.username).startAnimation(shake);
			//
			// } else if (Password.getText().toString().trim().equals("")) {
			// // DialogCreate.messageDialog(LoginActivity.this, "Alert",
			// // "Please Enter Password");
			// Password.setError("Please Enter Password");
			//
			// findViewById(R.id.password).startAnimation(shake);
			//
			// } else if (Password.getText().toString().length() <= 5) {
			// // DialogCreate.messageDialog(LoginActivity.this, "Alert",
			// // "Please Enter Password");
			// Password.setError("Please Enter Valid Password");
			//
			// findViewById(R.id.password).startAnimation(shake);
			//
			// } else {
			// new MyAsyncTask().execute(Username.getText().toString());
			// }

			break;
		case R.id.btnlogin:
			Intent _login = new Intent(LoginActivity.this, SigninActivity.class);
			startActivity(_login);
			// finish();

			break;
		case R.id.btnfblogin:
			if (CommonUtility.isNetworkAvailable(this)) {
				onFblogin();
				// loginToFacebook();
			} else {
				Toast.makeText(
						LoginActivity.this,
						"Internet connection is too slow or might be not working.",
						Toast.LENGTH_SHORT).show();
			}
			break;

		default:
			break;
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();

		finish();
	}

	public class SignUPWebServiceCall extends AsyncTask<Void, Void, JSONObject> {
		ProgressDialog _dialog;
		ArrayList<NameValuePair> nameValuePaire;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			_dialog = new ProgressDialog(LoginActivity.this);
			_dialog.setMessage("Please Wait...");
			_dialog.setCancelable(false);
			_dialog.show();
			_dataToPost = new HashMap<String, String>();
			_dataToPost.put("action", "signup");
			_dataToPost.put("deviceid", registerGCM());
			_dataToPost.put("email", email);
			_dataToPost.put("model", "android");
			_dataToPost.put("firstname", firstname);
			_dataToPost.put("lastname", lastname);
			_dataToPost.put("regtype", regtype);
			_dataToPost.put("gender", gender);
			_dataToPost.put("fbid", fbid);
		}

		@Override
		protected JSONObject doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return _webserviceCall.performPostCallDirect(_dataToPost, "");
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			_dialog.dismiss();
			if (result != null) {
				try {
					if (result.getInt("status") == 1) {
						CommonUtility.setGlobalString(LoginActivity.this,
								"USER_ID", result.getString("id"));
						Toast.makeText(LoginActivity.this,
								result.getString("message"), Toast.LENGTH_LONG)
								.show();
						Intent _intent = new Intent(LoginActivity.this,
								AwesomeActivity.class);
						_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_CLEAR_TASK);
						startActivity(_intent);
						LoginActivity.this.finish();
					} else {
						Toast.makeText(LoginActivity.this,
								result.getString("message"), Toast.LENGTH_LONG)
								.show();
					}
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				Toast.makeText(LoginActivity.this,
						"Some error occured please try again.",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}

}
