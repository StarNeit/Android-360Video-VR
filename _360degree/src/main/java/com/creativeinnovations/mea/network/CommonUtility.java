/*
 * Used as class for common utility functions
 */

package com.creativeinnovations.mea.network;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * @author gwb
 *
 */
/**
 * @author gwb
 *
 */
@SuppressLint({ "SimpleDateFormat", "DefaultLocale" })
public class CommonUtility {

	// returns status of user login
	public static final boolean isLoggedIn(Context activity)
	{
		String authcode = getGlobalString(activity, "AUTHCODE");
		if (authcode.equals("")) 
		{
			return false;
		} 
		else
		{
			return true;
		}
	}
	// shows alert message
	public static void showAlert(final Activity context, final String title,
			final String message) {
		context.runOnUiThread(new Runnable() {

			@Override
			public void run()
			{
				new AlertDialog.Builder(context).setTitle(title).setMessage(message).setPositiveButton("Ok",new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog,int which)
					{
										// context.finish();
					}
									}).show();
				}
											});
	}
	
	/*public static  void customToast(Activity _act, String _message)
	{
		  LayoutInflater inflater = _act.getLayoutInflater();
          // Inflate the Layout
          View layout = inflater.inflate(R.layout.custom_toast,(ViewGroup) _act.findViewById(R.id.custom_toast_layout));
          TextView text = (TextView) layout.findViewById(R.id.textToShow);
          // Set the Text to show in TextView
          text.setText(_message);

          Toast toast = new Toast(_act);
          toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
          toast.setDuration(Toast.LENGTH_SHORT);
          toast.setView(layout);
          toast.show();

	}*/
/* SHOW ALERT AND GO BACK 
 * 
 */
	public static void showAlertAndGoBack(final Activity context, final String title,
			final String message) {
		context.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				new AlertDialog.Builder(context)
						.setTitle(title)
						.setMessage(message)
						.setCancelable(false)
						.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        context.finish();
                                    }
                                }).show();
			}
		});
	}

	// shows error message when internet connection doesn't work
	public static void connectivityError(final Activity context) {
		CommonUtility.showAlert(context, "Connectivity Error",
                "Internet connection is not available or may be too slow.");
	}



	// set app level string global value
	public static final boolean setGlobalString(Context context,
			final String key, final String value) {
		SharedPreferences sharedPref = context.getSharedPreferences("360DEGREE", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(key, value);
		editor.commit();

		return true;
	}

	// get app level string global value
	public static final String getGlobalString(Activity activity,
			final String key) {
		SharedPreferences sharedPref = activity.getSharedPreferences("360DEGREE", Context.MODE_PRIVATE);
		return sharedPref.getString(key, "");
	}

	// get app level string global value
	public static final String getGlobalString(Context activity,
			final String key) {
		SharedPreferences sharedPref = activity.getSharedPreferences("360DEGREE", Context.MODE_PRIVATE);
		return sharedPref.getString(key, "");
	}
	
	//Check the Location is update
//		public static final String setLocation(Activity activity) {
//			CommonUtility.setGlobalString(activity, "isLocation", "true");
//		}

	// returns userid of loggedin user
	public static final String getUserId(Activity activity) {
		return CommonUtility.getGlobalString(activity, "USER_ID");
	}

	// returns authcode of loggedin user
	public static final String getAuthCode(Activity activity) {
		return CommonUtility.getGlobalString(activity, "AUTHCODE");
	}

	// returns userid of loggedin user
	public static final String getUserId(Context activity) {
		return CommonUtility.getGlobalString(activity, "USER_ID");
	}
	// returns Android Device ID
	public static final String getAndroidDeviceID(Context activity) {
		return CommonUtility.getGlobalString(activity, "DEVICE_ID");
	}
	

	// returns authcode of loggedin user
	public static final String getAuthCode(Context activity) {
		return CommonUtility.getGlobalString(activity, "AUTHCODE");
	}

	// to check whether network is available or not
	public static boolean isNetworkAvailable(Context context) 
	{
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetworkInfo != null && activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnectedOrConnecting()) 
		{
			return true;
		}
		return false;
	}

	// ****** returns webservice base url ******
	public static String getWebserviceUrl(Context ctx, String action) 
	{
		 //return "http://192.168.2.150:3000/api/" + action;
		 return "http://192.168.2.2:8088/360_wp/api/api.php" + action;
	}
	
	public static String getWebserviceUrlforApp(Context ctx, String action) 
	{
		//return "http://192.168.2.150:3000/api/" + action;
		return "http://192.168.2.2:8088/360_wp/api/api.php" + action;
	}
	
	// clears authentication parameters from system for logout
	public static void logout(Activity activity) {
		// Common logout
		CommonUtility.setGlobalString(activity, "AUTHCODE", "");
		CommonUtility.setGlobalString(activity, "USER_ID", "");
	}

	// removes html tags from string
	public static String stripHtml(String html) {
		return Html.fromHtml(html).toString().trim();
	}

	// returns md5 encrypted string of parameter string
	public static final String md5(final String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = MessageDigest
					.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				String h = Integer.toHexString(0xFF & messageDigest[i]);
				while (h.length() < 2)
					h = "0" + h;
				hexString.append(h);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	// converts date string from its current source format to required target
	// format
	// "yyyy-MM-dd HH:mm:ss" to "MM/dd/yyyy hh:mm a"
	// "MM/dd/yy HH:mm:ss" to "MM/dd/yyyy hh:mm a"
	public static String convertDateFormat(String dateStr, String sourceFormat,
			String targetFormat) {

		if (dateStr.equals("")) {
			return "";
		}
		Log.d("date", dateStr + "---" + sourceFormat + "---" + targetFormat);
		// "yyyy-MM-dd'T'HH:mm:ss.SSS"
		SimpleDateFormat form = new SimpleDateFormat(sourceFormat);
		Date date = null;
		try {
			date = form.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		SimpleDateFormat postFormater = new SimpleDateFormat(targetFormat);
		String newDateStr = postFormater.format(date);
		Log.d("Lead Response", newDateStr);
		return newDateStr;
	}

	// removes object at index from jsonarray
	public static JSONArray remove(final int idx, final JSONArray from) {
		final List<JSONObject> objs = asList(from);
		objs.remove(idx);

		final JSONArray ja = new JSONArray();
		for (final JSONObject obj : objs) {
			ja.put(obj);
		}

		return ja;
	}

	// removes objects from jsonarray for given range
	// idx is initial index
	// range is number of objects to be removed from index (idx)
	public static JSONArray removeInRange(final int idx, final int range,
			final JSONArray from) {
		final List<JSONObject> objs = asList(from);

		for (int i = 0; i < range; i++) {
			if (idx < objs.size()) {
				objs.remove(idx);
			}
		}

		final JSONArray ja = new JSONArray();
		for (final JSONObject obj : objs) {
			ja.put(obj);
		}

		return ja;
	}

	// creates new list object from objects in jsonarray
	public static List<JSONObject> asList(final JSONArray ja) {
		final int len = ja.length();
		final ArrayList<JSONObject> result = new ArrayList<JSONObject>(len);
		for (int i = 0; i < len; i++) {
			final JSONObject obj = ja.optJSONObject(i);
			if (obj != null) {
				result.add(obj);
			}
		}
		return result;
	}

	// checks whether running is actual android device
	// returns true for android device
	// returns false for emulator
	
	
	public static boolean isActualDevice() {
		boolean inEmulator = "generic".equals(Build.BRAND.toLowerCase());
		return !inEmulator;
	}
	
	// checks whether string has null or blank value and returns false for null or blank
	public static boolean isStringExists(String str) {
		if (str == null) {
			return false;
		}
		if (!(str instanceof String)) {
			return false;
		}
		if (str.equalsIgnoreCase("null")) {
			return false;
		}
		if (str.equalsIgnoreCase("<null>")) {
			return false;
		}
		if (str.equalsIgnoreCase("(null)")) {
			return false;
		}
		str = str.trim();
		if (str.equals("")) {
			return false;
		}
		return true;
	}

	// removes unwanted characters and null values
	public static String getStringValue(String str) {
		if (CommonUtility.isStringExists(str)) {
			str = str.replace("\t", "");
			str = str.trim();

			if (str.equals("{}")) {
				str = "";
			}

			if (str.equals("()")) {
				str = "";
			}

			return str;
		}
		return "";
	}

	/*public static void changedateformatusingslash(String oldDateString)
	{
		Date d = null;
		final String OLD_FORMAT = "dd/MM/yyyy";
		final String NEW_FORMAT = "MM/dd/yyyy";

		// August 12, 2010
	//	String oldDateString = "12/08/2010";
		String newDateString;

		SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
		
		try 
		{
			d = sdf.parse(oldDateString);
		} 
		catch (ParseException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sdf.applyPattern(NEW_FORMAT);
		newDateString = sdf.format(d);
	}*/
	
	// decode html encoded characters to its original html characters
	public static String htmlDecode(String html) {
		html = html.replace("&lt;", "<");
		html = html.replace("&gt;", ">");
		html = html.replace("&quot;", "\"");
		html = html.replace("&#39;", "'");
		return html;
	}

	/*// takes object for default id of title, sets title and font of title
	public static void setViewTitle(Activity activity, String title) {
		Log.d("ActivityClass",activity.getClass().toString());
		TextView textTitleBar = (TextView) activity
				.findViewById(R.id.textTitleBar);
		textTitleBar.setText(title);
		Constant.setViewTitleFont(activity, textTitleBar);
	}*/

	// sets compulsory parameters of webview
	@SuppressWarnings("deprecation")
	public static void setupWebView(WebView webView, String htmlString) {
		// htmlString=CommomnUtility.getStringValue(htmlString);
		webView.setBackgroundColor(0);
		webView.clearView();
		webView.measure(10, 10);
		webView.setVisibility(View.GONE);
		webView.setVisibility(View.VISIBLE);
		webView.loadDataWithBaseURL("", htmlString, "text/html", "UTF-8", null);
	}

	// select spinner row by comparing spinner array's string value
	public static void setSpinnerValue(Spinner spinner, final String value,
			String[] array) {

		for (int i = 0; i < array.length; i++) {
			if (value==null) {
				return;
			}
			if (value.equals(array[i])) {
				spinner.setSelection(i, true);
			}
		}
	}

	// returns desired key value of selected index of spinner from jsonarray
	public static String getSpinnerActualValue(Spinner spinner,
			JSONArray jsonArray, final String paramKey) {
		String returnStr = "";

		int selectedIndex = spinner.getSelectedItemPosition() - 1;
		if (selectedIndex < 0) {
			selectedIndex = 0;
		}
		try {
			JSONObject jsonObject = jsonArray.getJSONObject(selectedIndex);
			if (jsonObject.has(paramKey)) {
				return jsonObject.getString(paramKey);// /////////// Change
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return returnStr;
	}

	// returns desired key value of selected index of spinner from ArrayList
	public static String getSpinnerActualValue1(Spinner spinner,
			ArrayList<HashMap<String, String>> arrayListCustom, final String paramKey) {
		String returnStr = "";

		int selectedIndex = spinner.getSelectedItemPosition();
		if (selectedIndex < 0) {
			selectedIndex = 0;
		}
		try {
			returnStr = arrayListCustom.get(selectedIndex).get(paramKey);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return returnStr;
	}

	// creates array of string using key from arraylist
	public static String[] getStringArray1(ArrayList<HashMap<String, String>> arrayListCustom,
			String displayKey, boolean includeBlank) {
		String[] strArray;
	
			strArray = new String[arrayListCustom.size()];

		try {
			for (int i = 0; i <arrayListCustom.size(); i++) {
				String str=arrayListCustom.get(i).get(displayKey);
				// / Check the display key 
						strArray[i] = str;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strArray;
	}
	
	// creates array of string using key from JSONArray
	public static String[] getStringArray(JSONArray jsonArray,
			String displayKey, boolean includeBlank) {
		String[] strArray;

		if (includeBlank) {
			Log.d("Json Array", jsonArray.toString());
			strArray = new String[jsonArray.length() + 1];
			strArray[0] = "";
		} else {
			strArray = new String[jsonArray.length()];
		}

		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				// / Check the display key 
				if (jsonArray.get(i) instanceof String) {
					String str = (String) jsonArray.get(i);
					if (includeBlank) {
						strArray[i + 1] = str;
					} else {
						strArray[i] = str;
					}
				} else {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					if (includeBlank) {
						strArray[i + 1] = jsonObject.getString(displayKey);
					} else {
						strArray[i] = jsonObject.getString(displayKey);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strArray;
	}
	
	// returns current date using format "yyyy-MM-dd HH:mm:ss"
	public static String getCurrentDateTime(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(new Date());
	}
	
	// Check the device is tablet or not
	public static boolean isTablet(Context context) {
		boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
		boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
		return (xlarge || large);
	}

	// Check the start date and end date validation
	public static boolean isDateAfter(String startDate,String endDate)
    {
        try
        {
            String myFormatString = "MM/dd/yyyy"; // for example
            SimpleDateFormat df = new SimpleDateFormat(myFormatString);
            Date date1 = df.parse(endDate);
            Date startingDate = df.parse(startDate);

            if (date1.after(startingDate))
                return true;
            else
                return false;
        }
        catch (Exception e) 
        {

            return false;
        }
    }	
	// compare edit text value
		public static boolean isEqual(EditText edittext1, EditText edittext2) {
			boolean check = false;
			if (edittext1.getText().toString().trim()
					.equals(edittext2.getText().toString().trim())) {
				check = true;
				return check;
			}
			return check;
		}
	
}
