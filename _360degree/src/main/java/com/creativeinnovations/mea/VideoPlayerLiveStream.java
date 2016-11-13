package com.creativeinnovations.mea;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.degree.hitaishin.connection.WebServiceNaming;
import com.degree.hitaishin.model.VideoList;
import com.panframe.android.lib.PFAsset;
import com.panframe.android.lib.PFAssetObserver;
import com.panframe.android.lib.PFAssetStatus;
import com.panframe.android.lib.PFNavigationMode;
import com.panframe.android.lib.PFObjectFactory;
import com.panframe.android.lib.PFView;

/**
 * @author sachin
 * 
 */
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
public class VideoPlayerLiveStream extends FragmentActivity implements
		PFAssetObserver, OnSeekBarChangeListener {
	String URI;
	ImageView bar;
	PFView _pfview;
	SeekBar _scrubber;
	VideoList videoList;
	PFAsset _pfasset;

	PFNavigationMode _currentNavigationMode = PFNavigationMode.MOTION;
	boolean check_mode = true;
	boolean _updateThumb = true;;
	Timer _scrubberMonitorTimer;

	RelativeLayout controller_layout, controller_layout_second;
	ViewGroup _frameContainer;
	Runnable mRunnable;
	ImageButton _playButton;
	ImageButton _touchButton;
	ImageButton _downloadButton;
	ImageButton _backButton;
	ImageButton _modeButton;
	ProgressBar progressBar1;
	ScaleGestureDetector scaleGestureDetector;
	int mode_flag = 0;
	String videoLink;
	Handler mHandler = new Handler();

	TextView textCategory, textCategory1;

	/**
	 * Creation and initalization of the Activitiy. Initializes variables,
	 * listeners, and starts request of a movie list.
	 * 
	 * @param savedInstanceState
	 *            a saved instance of the Bundle
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.player_livestream);

		_frameContainer = (ViewGroup) findViewById(R.id.framecontainer);
		_frameContainer.setBackgroundColor(0xFF000000);

		_playButton = (ImageButton) findViewById(R.id.playbutton);

		_touchButton = (ImageButton) findViewById(R.id.mobile_button);
		bar = (ImageView) findViewById(R.id.bar);
		_playButton.setOnClickListener(playListener);
		_scrubber = (SeekBar) findViewById(R.id.scrubber);
		textCategory1 = (TextView) findViewById(R.id.category1);
		_downloadButton = (ImageButton) findViewById(R.id.download_button);
		_backButton = (ImageButton) findViewById(R.id.back_button);
		_modeButton = (ImageButton) findViewById(R.id.mode_button);
		controller_layout = (RelativeLayout) findViewById(R.id.controller_layout);
		controller_layout_second = (RelativeLayout) findViewById(R.id.controller_layout_second);
		textCategory = (TextView) findViewById(R.id.category);
		progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
		 scaleGestureDetector =
	                new ScaleGestureDetector(this,
	                        new MyOnScaleGestureListener());
		_backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();

			}
		});
		_downloadButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// AlertDialog.Builder builder = new AlertDialog.Builder(
				// VideoPlayerViewDownload.this);
				// builder.setMessage("ALREADY DOWNLOADED")
				// .setCancelable(false)
				// .setPositiveButton("OK",
				// new DialogInterface.OnClickListener() {
				// public void onClick(DialogInterface dialog,
				// int id) {
				// // do things
				// dialog.dismiss();
				// }
				// });
				// builder.show();

			}
		});

		_modeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (mode_flag == 0) {
					_pfview.setMode(2, 16 / 9);
					Log.e("mode", "double");

					textCategory1.setVisibility(View.VISIBLE);
					mode_flag = 1;
				} else {
					_pfview.setMode(0, 16 / 9);
					Log.e("mode", "single");
					textCategory1.setVisibility(View.GONE);
					mode_flag = 0;
				}

			}
		});

		_scrubber.setEnabled(false);

		videoList = (VideoList) getIntent().getExtras().getSerializable(
				WebServiceNaming.TAG_VIDEO_LINK);
		videoLink = videoList.getUpload_date();
		loadVideo(videoLink);
		textCategory.setText(videoList.getCategory_name());
		textCategory1.setText(videoList.getCategory_name());

		showControls(true);
		_pfasset.play();

		videoLink = getIntent().getExtras().getString(
				WebServiceNaming.TAG_VIDEO_LINK);

		ImageButton touchButton = (ImageButton) findViewById(R.id.mobile_button);
		touchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (_currentNavigationMode == PFNavigationMode.TOUCH) {
					_currentNavigationMode = PFNavigationMode.MOTION;

					_pfview.setViewRotationOffsetX(90);
				} else {

					_currentNavigationMode = PFNavigationMode.TOUCH;

					_pfview.setViewRotationOffsetX(180);

				}
				_pfview.setNavigationMode(_currentNavigationMode);

			}
		});

		_pfview.setNavigationMode(_currentNavigationMode);

		mRunnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				controller_layout_second.setVisibility(View.GONE); // This
				textCategory.setText(videoList.getCategory_name());

			}
		};

		controller_layout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				int action = event.getActionMasked();

				switch (action) {

				case MotionEvent.ACTION_DOWN:

					controller_layout_second.setVisibility(View.VISIBLE);

					controller_layout_second.invalidate();
					mHandler.removeCallbacks(mRunnable);
					mHandler.postDelayed(mRunnable, 2 * 1000);

					break;

				}

				return false;

			}
		});
		mHandler.postDelayed(mRunnable, 3 * 1000);

	}

	/**
	 * Show/Hide the playback controls
	 * 
	 * @param bShow
	 *            Show or hide the controls. Pass either true or false.
	 */
	public void showControls(boolean bShow) {
		int visibility = View.GONE;

		if (bShow)
			visibility = View.VISIBLE;

		_playButton.setVisibility(visibility);
		_touchButton.setVisibility(visibility);

		if (_pfview != null) {
			if (!_pfview.supportsNavigationMode(PFNavigationMode.MOTION))
				// _touchButton.setVisibility(View.GONE);
				Log.d("SimplePlayer", "Not supported nav");
		}
		_pfview.setViewRotationOffsetX(90);

	}

	/**
	 * Start the video with a local file path
	 * 
	 * @param filename
	 *            The file path on device storage
	 */
	public void loadVideo(String filename) {

		_pfview = PFObjectFactory.view(this);
	
		
		// _pfasset = PFObjectFactory.assetFromUri(this, Uri.parse(filename),
		// this);
		_pfasset = PFObjectFactory
				.assetFromUri(this, Uri.parse(filename), this);
		_pfview.displayAsset(_pfasset);

		_pfview.setNavigationMode(_currentNavigationMode);

		_frameContainer.addView(_pfview.getView(), 0);

	}

	/**
	 * Status callback from the PFAsset instance. Based on the status this
	 * function selects the appropriate action.
	 * 
	 * @param asset
	 *            The asset who is calling the function
	 * @param status
	 *            The current status of the asset.
	 */
	
	public class MyOnScaleGestureListener extends
    SimpleOnScaleGestureListener {

@Override
public boolean onScale(ScaleGestureDetector detector) {

    float scaleFactor = detector.getScaleFactor();

    if (scaleFactor > 1) {
    	
    	//_pfview.getView().s
        
    } else {
        
    }
    return true;
}

@Override
public boolean onScaleBegin(ScaleGestureDetector detector) {
    return true;
}

@Override
public void onScaleEnd(ScaleGestureDetector detector) {

}
};
	public void onStatusMessage(final PFAsset asset, PFAssetStatus status) {

		switch (status) {
		case LOADED:
			Log.d("SimplePlayer", "Loaded");
			break;
		case DOWNLOADING:

			Log.d("SimplePlayer",
					"Downloading 360 movie: " + _pfasset.getDownloadProgress()
							+ " percent complete");
			break;
		case DOWNLOADED:
			Log.d("SimplePlayer", "Downloaded to " + asset.getUrl());

			showDownloadProgress(false);
			break;
		case DOWNLOADCANCELLED:
			Log.d("SimplePlayer", "Download cancelled");
			break;
		case PLAYING:
			Log.d("SimplePlayer", "Playing");
			progressBar1.setVisibility(View.INVISIBLE);
			getWindow()
					.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

			_playButton.setImageResource(R.drawable.pause_selector);
			_scrubberMonitorTimer = new Timer();
			final TimerTask task = new TimerTask() {
				public void run() {
					if (_updateThumb)
						_scrubber.setProgress((int) asset.getPlaybackTime());
				}
			};

			_scrubberMonitorTimer.schedule(task, 0, 33);

			Log.d("SimplePlayer", "Playing");

			break;
		case PAUSED:
			Log.d("SimplePlayer", "Paused");
			_playButton.setImageResource(R.drawable.play_selector);

			break;
		case STOPPED:
			try {
				Log.d("SimplePlayer", "Stopped");
				_playButton.setImageResource(R.drawable.play_selector);

				_scrubberMonitorTimer.cancel();
				_scrubberMonitorTimer = null;
				_scrubber.setProgress(0);
				_scrubber.setEnabled(false);

			} catch (NullPointerException e) {
				// TODO: handle exception
			}

			getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			break;
		case COMPLETE:
			try {
				Log.d("SimplePlayer", "Complete");
				_playButton.setImageResource(R.drawable.play_selector);
				_playButton.setImageResource(R.drawable.play_selector);
				_scrubberMonitorTimer.cancel();
				_scrubberMonitorTimer = null;

			} catch (NullPointerException e) {
				// TODO: handle exception
			}
			getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			break;
		case ERROR:
			Log.d("SimplePlayer", "Error");
			break;
		}
	}

	/**
	 * Click listener for the play/pause button
	 * 
	 */
	private OnClickListener playListener = new OnClickListener() {
		public void onClick(View v) {
			if (_pfasset.getStatus() == PFAssetStatus.PLAYING) {
				_pfasset.pause();

			} else {
				_pfasset.play();

			}

		}
	};

	/**
	 * Click listener for the stop/back button
	 * 
	 */
	@SuppressWarnings("unused")
	private OnClickListener stopListener = new OnClickListener() {
		public void onClick(View v) {
			_pfasset.stop();

		}
	};

	public void onPause() {
		super.onPause();
		if (_pfasset != null) {
			if (_pfasset.getStatus() == PFAssetStatus.PLAYING)
				_pfasset.pause();
		}

	}

	/**
	 * Called when a previously created loader is being reset, and thus making
	 * its data unavailable.
	 * 
	 * @param seekbar
	 *            The SeekBar whose progress has changed
	 * @param progress
	 *            The current progress level.
	 * @param fromUser
	 *            True if the progress change was initiated by the user.
	 * 
	 */
	public void onProgressChanged(SeekBar seekbar, int progress,
			boolean fromUser) {
	}

	/**
	 * Notification that the user has started a touch gesture. In this function
	 * we signal the timer not to update the playback thumb while we are
	 * adjusting it.
	 * 
	 * @param seekbar
	 *            The SeekBar in which the touch gesture began
	 * 
	 */
	public void onStartTrackingTouch(SeekBar seekbar) {
		_updateThumb = false;

	}

	/**
	 * Notification that the user has finished a touch gesture. In this function
	 * we request the asset to seek until a specific time and signal the timer
	 * to resume the update of the playback thumb based on playback.
	 * 
	 * @param seekbar
	 *            The SeekBar in which the touch gesture began
	 * 
	 */
	public void onStopTrackingTouch(SeekBar seekbar) {
		_pfasset.setPLaybackTime(seekbar.getProgress());

		_updateThumb = true;

	}

	public void onStartCommand(Intent intent, int flags, int startId) {
		_pfasset.play();

	}

	public void showDownloadProgress(boolean bShow) {
		@SuppressWarnings("unused")
		int visibility = View.GONE;

		if (bShow)
			visibility = View.VISIBLE;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		_pfasset.pause();

		_pfasset.stop();

		finish();

	}

}