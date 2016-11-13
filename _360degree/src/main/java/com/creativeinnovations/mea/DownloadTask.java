package com.creativeinnovations.mea;

import java.text.DecimalFormat;

import android.text.TextUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.degree.hitaishin.model.VideoList;
import com.duowan.mobile.netroid.toolbox.FileDownloader;

public abstract class DownloadTask {

	private static final String mSaveDirPath = "/sdcard/360/";
	public final static DecimalFormat DECIMAL_POINT = new DecimalFormat("0.0");

	public TextView textTitle;
	public TextView textCategory;
	public TextView textCountry;
	public TextView textDownload;
	public TextView textDetail;
	public TextView tv_progress_circle;
	public ImageView videoImage;
	public ImageButton facebookImage;
	public ImageButton twiterImage,playImage;
	public ImageButton downloadIMage;
	public ProgressBar bar;
	public LinearLayout linearLayout;
	public RelativeLayout download_progress;

	public FileDownloader.DownloadController controller;
	public String storeFileName;
	public String url;
	public VideoList videoList;
	public TextView btnStatus;
	public TextView txvFileSize;
	public TextView txvDownloadedSize, txvTaskName;

	public long fileSize;
	public long downloadedSize;

	public void onProgressChange(long fileSize, long downloadedSize) {
		this.fileSize = fileSize;
		this.downloadedSize = downloadedSize;

		invalidate();
	}

	public void invalidate() {
		if (btnStatus == null)
			return;
		if (!TextUtils.equals((CharSequence) btnStatus.getTag(), storeFileName))
			return;

		switch (controller.getStatus()) {
		case FileDownloader.DownloadController.STATUS_DOWNLOADING:
			if (fileSize > 0 && downloadedSize > 0) {
				btnStatus.setText(DECIMAL_POINT.format(downloadedSize * 1.0f
						/ fileSize * 100) + '%');
			} else {
				btnStatus.setText("0%");
			}
			break;
		case FileDownloader.DownloadController.STATUS_WAITING:
			btnStatus.setText("waiting");
			break;
		case FileDownloader.DownloadController.STATUS_PAUSE:
			btnStatus.setText("paused");
			break;
		case FileDownloader.DownloadController.STATUS_SUCCESS:

			videoList.setVideo_link(mSaveDirPath + videoList.getVideo_id()
					+ ".mp4");

			// db.updateVideoRecord(videoList);
			btnStatus.setText("done");
			break;
		}

		txvDownloadedSize.setText(downloadedSize + "");
		txvFileSize.setText(fileSize + "");
	}

	public  DownloadTask(String storeFileName, String url, VideoList video) {
		this.storeFileName = storeFileName;
		this.url = url;
		this.videoList = video;
	}



}