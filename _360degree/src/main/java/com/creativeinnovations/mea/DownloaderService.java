/**
 * 
 */
package com.creativeinnovations.mea;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.degree.database.DataBaseHandler;
import com.degree.hitaishin.model.VideoList;

/**
 * @author sachin
 * 
 */
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class DownloaderService extends Service {

	VideoList videoList;
	boolean flag = true;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TBD
		if (intent != null) {
			videoList = (VideoList) intent.getExtras().get("videoList");

			new DownloadFileFromURL().execute();
		}
		return Service.START_FLAG_REDELIVERY;

	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	class DownloadFileFromURL extends AsyncTask<String, String, String> {
		ProgressDialog pDialog;
		DataBaseHandler db = new DataBaseHandler(DownloaderService.this);

		/**
		 * Before starting background thread Show Progress Bar Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			videoList.setVideo_link("Progress");
			db.addVIDEO(videoList);

		}

		@Override
		protected String doInBackground(String... f_url) {

			try {
				String urlToDownload = videoList.getVideo_link();
				// videoList.setVideo_link("progess||0");

				URL url = new URL(urlToDownload);
				URLConnection connection = url.openConnection();
				connection.connect();
				// this will be useful so that you can show a typical 0-100%
				// progress bar
				int fileLength = connection.getContentLength();
				File direct = new File(Environment
						.getExternalStorageDirectory().toString()
						+ "/360degree/");

				if (!direct.exists()) {
					direct.mkdirs();
				}
				// download the file
				InputStream input = new BufferedInputStream(
						connection.getInputStream());
				OutputStream output = new FileOutputStream(Environment
						.getExternalStorageDirectory().toString()
						+ "/360degree/" + videoList.getVideo_id() + ".mp4");

				byte data[] = new byte[1024];
				long total = 0;
				int count;
				while ((count = input.read(data)) != -1) {
					total += count;
					// publishing the progress....

					Log.e("star", (int) (total * 100 / fileLength) + "");

					String URI = Environment.getExternalStorageDirectory()
							.toString()
							+ "/360degree/"
							+ videoList.getVideo_id() + ".mp4";

					videoList.setVideo_link((int) (total * 100 / fileLength)
							+ "||" + URI);
					db.updateVideoRecord(videoList);

					output.write(data, 0, count);
				}

				output.flush();
				output.close();
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
				flag = false;
			}

			return null;
		}

		/**
		 * Updating progress bar
		 * */
		protected void onProgressUpdate(String... progress) {
			// setting progress percentage

		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		@Override
		protected void onPostExecute(String file_url) {
			if (flag) {
				String URI = Environment.getExternalStorageDirectory()
						.toString()
						+ "/360degree/"
						+ videoList.getVideo_id()
						+ ".mp4";
				Toast.makeText(DownloaderService.this, "Downloading Completed",
						Toast.LENGTH_LONG).show();
				DataBaseHandler db = new DataBaseHandler(DownloaderService.this);
				videoList.setVideo_link("100" + URI);
				db.updateVideoRecord(videoList);

			} else {
				String URI = Environment.getExternalStorageDirectory()
						.toString()
						+ "/360degree/"
						+ videoList.getVideo_id()
						+ ".mp4";
				Toast.makeText(DownloaderService.this, "Downloading Completed",
						Toast.LENGTH_LONG).show();
				DataBaseHandler db = new DataBaseHandler(DownloaderService.this);
				videoList.setVideo_link("failed" + URI);
				db.updateVideoRecord(videoList);
			}
		}
	}
}