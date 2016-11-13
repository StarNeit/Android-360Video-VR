/**
 * 
 */
package com.degree.hitaishin.connection;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.apache.http.conn.util.InetAddressUtils;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

/**
 * @author sachin
 * 
 */
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class WebServiceConnection {// static InputStream is = null;
	static JSONObject jObj = null;
	static String json = "";
	static HttpURLConnection urlc = null;

	static DataOutputStream dataout = null;
	static BufferedReader in = null;

	public static JSONObject performPost(String encodedData, String path)
			throws Exception {
		Log.e("Url", path);
		Log.e("post", encodedData);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		URL url = new URL(path);

		urlc = (HttpURLConnection) url.openConnection();
		urlc.setReadTimeout(20000);
		urlc.setConnectTimeout(2*60*60*1000);

		urlc.setRequestMethod("POST");
		urlc.setDoOutput(true);
		urlc.setDoInput(true);
		urlc.setUseCaches(false);
		urlc.setAllowUserInteraction(false);

		urlc.setRequestProperty("Content-Type", "application");
		dataout = new DataOutputStream(urlc.getOutputStream());

		dataout.writeBytes(encodedData);
		@SuppressWarnings("unused")
		int responseCode = urlc.getResponseCode();
		in = new BufferedReader(new InputStreamReader(urlc.getInputStream()),
				8096);

		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = in.readLine()) != null) {
			sb.append(line + "n");
		}

		json = sb.toString();
		Log.e("response", json.toString());

		jObj = new JSONObject(json);

		return jObj;

	}

	public static String getLocalIpv4Address() {
		try {
			String ipv4;
			List<NetworkInterface> nilist = Collections.list(NetworkInterface
					.getNetworkInterfaces());
			if (nilist.size() > 0) {
				for (NetworkInterface ni : nilist) {
					List<InetAddress> ialist = Collections.list(ni
							.getInetAddresses());
					if (ialist.size() > 0) {
						for (InetAddress address : ialist) {
							if (!address.isLoopbackAddress()
									&& InetAddressUtils
											.isIPv4Address(ipv4 = address
													.getHostAddress())) {
								return ipv4;
							}
						}
					}

				}
			}

		} catch (SocketException ex) {

		}
		return "";
	}

}
