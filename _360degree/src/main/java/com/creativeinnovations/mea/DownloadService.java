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

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.ResultReceiver;
import android.util.Log;

import com.degree.database.DataBaseHandler;
import com.degree.hitaishin.model.VideoList;

public class DownloadService extends IntentService {
	public static final int UPDATE_PROGRESS = 8344;
	VideoList videoList;

	public DownloadService() {
		super("DownloadService");
	
	}

	@SuppressWarnings("unused")
	@Override
	protected void onHandleIntent(Intent intent) {
		DataBaseHandler db = new DataBaseHandler(DownloadService.this);

		if (intent != null) {
			videoList = (VideoList) intent.getExtras().get("videoList");
			String urlToDownload = videoList.getVideo_link();
			// videoList.setVideo_link("progess||0");
			db.addVIDEO(videoList);

			ResultReceiver receiver = (ResultReceiver) intent
					.getParcelableExtra("receiver");
			try {
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
					Bundle resultData = new Bundle();

					Log.e("star", (int) (total * 100 / fileLength) + "");

					String URI = Environment.getExternalStorageDirectory()
							.toString()
							+ "/360degree/"
							+ videoList.getVideo_id() + ".mp4";

					videoList.setVideo_link((int) (total * 100 / fileLength)
							+ "||" + URI);
					db.updateVideoRecord(videoList);
					DownloadActivity.videoList = db.getAllVideo();
					// try {
					// resultData.putSerializable(
					// WebServiceNaming.TAG_VIDEO_LINK, videoList);
					// resultData.putInt("progress",
					// (int) (total * 100 / fileLength));
					// receiver.send(UPDATE_PROGRESS, resultData);
					//
					//
					// } catch (Exception e) {
					//
					// }

					output.write(data, 0, count);
				}

				output.flush();
				output.close();
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (videoList != null) {
				try {
					String URI = Environment.getExternalStorageDirectory()
							.toString()
							+ "/360degree/"
							+ videoList.getVideo_id() + ".mp4";

					videoList.setVideo_link(100 + "||" + URI);
					db.updateVideoRecord(videoList);
					// Bundle resultData = new Bundle();
					// resultData.putInt("progress", 100);
					// resultData.putSerializable(WebServiceNaming.TAG_VIDEO_LINK,
					// videoList);
					// receiver.send(UPDATE_PROGRESS, resultData);
				} catch (Exception e) {

				}
				stopSelf();
			}
		}
	}
}