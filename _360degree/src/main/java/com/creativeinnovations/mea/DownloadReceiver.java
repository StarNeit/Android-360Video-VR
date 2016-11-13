/**
 * 
 */
package com.creativeinnovations.mea;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class DownloadReceiver extends BroadcastReceiver {
	public DownloadReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();

		if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
			long downloadId = intent.getLongExtra(
					DownloadManager.EXTRA_DOWNLOAD_ID, 0);
			DownloadManager.Query query = new DownloadManager.Query();
			query.setFilterById(downloadId);
			DownloadManager mDownloadManager = (DownloadManager) context
					.getSystemService(Context.DOWNLOAD_SERVICE);
			Cursor c = mDownloadManager.query(query);
			if (c.moveToFirst()) {
				int columnIndex = c
						.getColumnIndex(DownloadManager.COLUMN_STATUS);

				int status = c.getInt(columnIndex);
				// column for reason code if the download failed or paused
				int columnReason = c
						.getColumnIndex(DownloadManager.COLUMN_REASON);
				int reason = c.getInt(columnReason);
				// get the download filename
				int filenameIndex = c
						.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
				String filename = c.getString(filenameIndex);

				String statusText = "";
				String reasonText = "";

				switch (status) {
				case DownloadManager.STATUS_FAILED:
					statusText = "STATUS_FAILED";
					switch (reason) {
					case DownloadManager.ERROR_CANNOT_RESUME:
						reasonText = "ERROR_CANNOT_RESUME";
						break;
					case DownloadManager.ERROR_DEVICE_NOT_FOUND:
						reasonText = "ERROR_DEVICE_NOT_FOUND";
						break;
					case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
						reasonText = "ERROR_FILE_ALREADY_EXISTS";
						break;
					case DownloadManager.ERROR_FILE_ERROR:
						reasonText = "ERROR_FILE_ERROR";
						break;
					case DownloadManager.ERROR_HTTP_DATA_ERROR:
						reasonText = "ERROR_HTTP_DATA_ERROR";
						break;
					case DownloadManager.ERROR_INSUFFICIENT_SPACE:
						reasonText = "ERROR_INSUFFICIENT_SPACE";
						break;
					case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
						reasonText = "ERROR_TOO_MANY_REDIRECTS";
						break;
					case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
						reasonText = "ERROR_UNHANDLED_HTTP_CODE";
						break;
					case DownloadManager.ERROR_UNKNOWN:
						reasonText = "ERROR_UNKNOWN";
						break;
					}
					break;
				case DownloadManager.STATUS_PAUSED:
					statusText = "STATUS_PAUSED";
					switch (reason) {
					case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
						reasonText = "PAUSED_QUEUED_FOR_WIFI";
						break;
					case DownloadManager.PAUSED_UNKNOWN:
						reasonText = "PAUSED_UNKNOWN";
						break;
					case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
						reasonText = "PAUSED_WAITING_FOR_NETWORK";
						break;
					case DownloadManager.PAUSED_WAITING_TO_RETRY:
						reasonText = "PAUSED_WAITING_TO_RETRY";
						break;
					}
					break;
				case DownloadManager.STATUS_PENDING:
					statusText = "STATUS_PENDING";
					break;
				case DownloadManager.STATUS_RUNNING:
					statusText = "STATUS_RUNNING";
					break;
				case DownloadManager.STATUS_SUCCESSFUL:
					statusText = "STATUS_SUCCESSFUL";
					reasonText = "Filename:\n" + filename;
					break;
				}

				Toast.makeText(context, statusText + "\n" + reasonText,
						Toast.LENGTH_LONG).show();
				;

			}
		}
	}
}