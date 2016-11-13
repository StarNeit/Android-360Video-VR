/**
 * 
 */
package com.creativeinnovations.mea;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
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
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.creativeinnovations.mea.network.CommonUtility;
import com.degree.database.DataBaseHandler;
import com.degree.hitaishin.UI.MeterView;
import com.degree.hitaishin.connection.WebServiceNaming;
import com.degree.hitaishin.model.VideoList;
import com.panframe.android.lib.PFAsset;
import com.panframe.android.lib.PFAssetObserver;
import com.panframe.android.lib.PFAssetStatus;
import com.panframe.android.lib.PFNavigationMode;
import com.panframe.android.lib.PFObjectFactory;
import com.panframe.android.lib.PFView;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint({ "NewApi", "ClickableViewAccessibility" })
public class VideoPlayerView extends FragmentActivity implements
		PFAssetObserver, OnSeekBarChangeListener, OnTouchListener {

	String URI;
	ImageView bar;
	PFView _pfview;
	SeekBar _scrubber;
	VideoList videoList;
	PFAsset _pfasset;
	public ProgressBar progressbar;
	ScaleGestureDetector scaleGestureDetector;
	PFNavigationMode _currentNavigationMode = PFNavigationMode.MOTION;
	boolean check_mode = true;
	boolean _updateThumb = true;;
	Timer _scrubberMonitorTimer;
	Boolean boolFlag = true;
	RelativeLayout controller_layout, controller_layout_second;
	ViewGroup _frameContainer;
	Runnable mRunnable;
	ImageButton _playButton;
	ImageButton _touchButton;
	ImageButton _downloadButton;
	ImageButton _backButton;
	ImageButton _modeButton;
	int mode_flag = 0;
	String videoLink;
	Handler mHandler = new Handler();
	private ProgressDialog progressDialog;
	private TextView _sticker2;
	public int progressBarStatus = 0;
	public Handler handlerProgressBar;
	private Thread currentThread;
	public TextView tv_buffering;

	private CountDownTimer countDownTimer;
	private ObjectAnimator anim;
	protected boolean _Flag = true;
	Float _overture = 110f;
	private CountDownTimer countDownTimer2;

	/**
	 * Creation and initalization of the Activitiy. Initializes variables,
	 * listeners, and starts request of a movie list.
	 * 
	 * @param savedInstanceState
	 *            a saved instance of the Bundle
	 */
	@SuppressLint("WrongViewCast")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.video_play);

		_frameContainer = (ViewGroup) findViewById(R.id.framecontainer);
		_frameContainer.setBackgroundColor(0xFF000000);

		handlerProgressBar = new Handler();
		_sticker2 = (TextView) findViewById(R.id.sticker2);
		_playButton = (ImageButton) findViewById(R.id.playbutton);
		progressbar = (ProgressBar) findViewById(R.id.circular_progress_bar);
		tv_buffering = (TextView) findViewById(R.id.textView1);
		_touchButton = (ImageButton) findViewById(R.id.mobile_button);
		bar = (ImageView) findViewById(R.id.bar);
		_playButton.setOnClickListener(playListener);
		_scrubber = (SeekBar) findViewById(R.id.scrubber);
		_downloadButton = (ImageButton) findViewById(R.id.download_button);
		_backButton = (ImageButton) findViewById(R.id.back_button);
		_modeButton = (ImageButton) findViewById(R.id.mode_button);
		controller_layout = (RelativeLayout) findViewById(R.id.controller_layout);
		controller_layout_second = (RelativeLayout) findViewById(R.id.controller_layout_second);

		_backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
				;
			}
		});

		_downloadButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DataBaseHandler db = new DataBaseHandler(VideoPlayerView.this);
				ArrayList<VideoList> vlist = db.getAllVideo();
				boolean flag = true;
				for (int i = 0; i < vlist.size(); i++) {
					if (vlist.get(i).getVideo_id()
							.equals(videoList.getVideo_id())) {
						flag = false;
					}
				}
				if (flag) {
					DownloadActivity.flag++;
					// videos.setVideo_link(URI);
					db.addVIDEO(videoList);
					AlertDialog.Builder builder = new AlertDialog.Builder(
							VideoPlayerView.this);
					builder.setMessage("DOWNLOAD STARTED")
							.setCancelable(false)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											// do things
											dialog.dismiss();

										}
									});
					AlertDialog alert = builder.create();
					alert.show();

				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							VideoPlayerView.this);
					builder.setMessage("VIDEO ALREADY DOWNLOADED")
							.setCancelable(false)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											// do things
											dialog.dismiss();
										}
									});
					AlertDialog alert = builder.create();
					alert.show();
				}

			}
		});

		_modeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (mode_flag == 0) {
					_pfview.setMode(2, 16 / 9);
					Log.e("mode", "double");
					_sticker2.setVisibility(View.VISIBLE);
					_pfview.setFieldOfView(85f);
					mode_flag = 1;
				} else {
					_pfview.setMode(0, 16 / 9);
					Log.e("mode", "single");
					_sticker2.setVisibility(View.GONE);
					_pfview.setFieldOfView(_overture);
					mode_flag = 0;
				}

				// if (mode_flag == 0) {
				// _pfview.setMode(0, 16 / 9);
				// Log.e("mode", "double");
				// mode_flag = 1;
				// } else if (mode_flag == 1) {
				// _pfview.setMode(1, 16 / 9);
				// Log.e("mode", "double");
				// mode_flag = 2;
				// } else if (mode_flag == 2) {
				// _pfview.setMode(2, 16 / 9);
				// Log.e("mode", "double");
				// mode_flag = 0;
				// }

			}
		});

		_scrubber.setEnabled(false);

		// set app linking code

		if (CommonUtility.getGlobalString(VideoPlayerView.this,
				"APP_LINKING_URL").length() > 0) {
			String url = CommonUtility.getGlobalString(VideoPlayerView.this,
					"APP_LINKING_URL");
			System.out.println("===================>" + url);
			videoLink = url;
			CommonUtility.setGlobalString(VideoPlayerView.this,
					"APP_LINKING_URL", "");
		} else {
			videoList = (VideoList) getIntent().getExtras().getSerializable(
					WebServiceNaming.TAG_VIDEO_LINK);
			videoLink = videoList.getVideo_link();
		}

		loadVideo(videoLink);
		ImageButton touchButton = (ImageButton) findViewById(R.id.mobile_button);
		touchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (_currentNavigationMode == PFNavigationMode.TOUCH) {
					_currentNavigationMode = PFNavigationMode.MOTION;
					// _pfview.setViewRotationOffsetX(90);
				} else {

					_currentNavigationMode = PFNavigationMode.TOUCH;
					// _pfview.setViewRotationOffsetX(180);
				}
				_pfview.setFieldOfView(_overture);
				_pfview.setNavigationMode(_currentNavigationMode);

			}
		});

		_pfview.setNavigationMode(_currentNavigationMode);

		mRunnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				controller_layout_second.setVisibility(View.GONE); // This
				// if (videoList.getCategory_name()!=null &&
				// videoList.getCategory_name().length()>0) {
				// textCategory.setText(videoList.getCategory_name());
				// }

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
		_scrubber.setVisibility(visibility);

		if (_pfview != null) {
			if (!_pfview.supportsNavigationMode(PFNavigationMode.MOTION))
				// _touchButton.setVisibility(View.GONE);
				Log.d("SimplePlayer", "Not supported nav");
		}

	}

	/**
	 * Start the video with a local file path
	 * 
	 * @param filename
	 *            The file path on device storage
	 */

	public class MyOnScaleGestureListener extends SimpleOnScaleGestureListener {

		@Override
		public boolean onScale(ScaleGestureDetector detector) {

			float scaleFactor = detector.getScaleFactor();

			if (scaleFactor > 1) {

				if (_overture > 40) {
					_overture--;// zoom out
				}
				// _pfview.setFieldOfView(5);

			} else {
				if (_overture < 100) {
					_overture++;// zoom in
				}

			}

			_pfview.setFieldOfView(_overture);
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

	public void loadVideo(String filename) {

		_pfview = PFObjectFactory.view(this);
		// Show progress dialog while video is downloading
		/*
		 * CountDownTimmer Which finish after Buffering progressBar 20 sec
		 * Finishes & in pause the boolean variable handled to stop play in
		 * mulitasking mode
		 */
		showProgress();

		scaleGestureDetector = new ScaleGestureDetector(this,
				new MyOnScaleGestureListener());

		// _pfasset = PFObjectFactory.assetFromUri(this, Uri.parse(filename),
		// this);
		_pfasset = PFObjectFactory.assetFromUri(VideoPlayerView.this,
				Uri.parse(filename), VideoPlayerView.this);
		// _pfview.setMode((mode_flag >= 2) ? 2 : mode_flag, 16 / 9); // nikhil
		_pfview.setMode(mode_flag, 16 / 9);
		_pfview.displayAsset(_pfasset);
		_pfview.handleOrientationChange();
		_pfview.setFieldOfView(_overture);

		if (_pfview != null) {
			if (_pfview.supportsNavigationMode(PFNavigationMode.MOTION)) {
				// _touchButton.setVisibility(View.GONE);
				_currentNavigationMode = PFNavigationMode.MOTION;

			} else {
				_currentNavigationMode = PFNavigationMode.TOUCH;
				Toast.makeText(
						VideoPlayerView.this,
						"Opps! Gyroscope is missing on your device. Please use Touch to navigate the video.",
						Toast.LENGTH_SHORT).show();
			}
		}

		_pfview.setNavigationMode(_currentNavigationMode);

		_frameContainer.addView(_pfview.getView(), 0);

	}

	/* Called From LoadVideo Function */
	public void showProgress() {

		startProgress(10);
		tv_buffering.setVisibility(View.VISIBLE);
	}

	/* Called From showProgress Function */
	private void startProgress(int i) {
		anim = ObjectAnimator.ofInt(progressbar, "progress", 0, 100);
		anim.setDuration(600000); // 10 min
		anim.setInterpolator(new DecelerateInterpolator());
		anim.start();
		startTimer(i);
	}

	/*
	 * CountDownTimmer Which finish after Buffering progressBar 20 sec Finishes
	 * & in pause the boolean variable handled to stop play in mulitasking mode
	 */
	/* Called From startProgress Function */
	private void startTimer(final int minuti) {
		countDownTimer = new CountDownTimer(60 * minuti * 1000, 1000) { // 10
																		// min
			// 500 means, onTick function will be called at every 500
			// milliseconds

			@Override
			public void onTick(long leftTimeInMilliseconds) {
				// long seconds = leftTimeInMilliseconds / 1000;
				// mprogressBar.setProgress((int)seconds);
				// tv.setText(String.format("%02d", seconds/60) + ":" +
				// String.format("%02d", seconds%60));
				// progress_tv.setText(String.format("%02d", seconds%60));
				// progressbar
				if (_Flag == true
						&& _pfasset.getStatus() == PFAssetStatus.PLAYING) {
					// progressbar.setProgress(100);
					_pfasset.pause();
					anim.setDuration(5000);

					_Flag = false;
					startTimer2(1);
				}

			}

			@Override
			public void onFinish() {

				if (boolFlag) {
					_pfasset.play();
				}

				progressbar.setVisibility(View.GONE);
				tv_buffering.setVisibility(View.GONE);
			}
		};
		countDownTimer.start();

	}

	/* Called From startProgress Function */
	private void startTimer2(final int minuti) {
		countDownTimer2 = new CountDownTimer(4 * minuti * 1000, 1000) { // 5sec
			// 500 means, onTick function will be called at every 500
			// milliseconds

			@Override
			public void onTick(long leftTimeInMilliseconds) {
				// long seconds = leftTimeInMilliseconds / 1000;
				// mprogressBar.setProgress((int)seconds);
				// tv.setText(String.format("%02d", seconds/60) + ":" +
				// String.format("%02d", seconds%60));
				// progress_tv.setText(String.format("%02d", seconds%60));
				// progressbar

			}

			@Override
			public void onFinish() {
				// progressbar.setProgress(100);
				countDownTimer.onFinish();
			}
		};
		countDownTimer2.start();

	}

	/**
	 * Status callback from the PFAsset instance. Based on the status this
	 * function selects the appropriate action.
	 * 
	 * @param asset
	 *            The asset who is calling the function
	 * @param status
	 *            The current status of the asset.
	 * 
	 **/

	public void onStatusMessage(final PFAsset asset, PFAssetStatus status) {

		switch (status) {
		case LOADED:
			Log.d("SimplePlayer", "Loaded");
			_pfasset.play();
			break;
		case DOWNLOADING:
			// progressbar.setMax(100);
			//
			if (progressDialog != null) {
				if (progressDialog.isShowing()) {
					progressDialog.setProgress(_pfasset.getDownloadProgress());
				}
			}
			// progressbar.setValue(_pfasset.getDownloadProgress(), 100);
			Log.d("SimplePlayer",
					"Downloading 360 movie: " + _pfasset.getDownloadProgress()
							+ " percent complete");
			// if (progressbar.getValue() == 100) {
			// _scrubberMonitorTimer = null;
			// _pfasset.play();
			// }
			break;
		case DOWNLOADED:
			// Log.d("SimplePlayer", "Downloaded to " + asset.getUrl());
			// _pfasset.play();

			break;
		case DOWNLOADCANCELLED:
			Log.d("SimplePlayer", "Download cancelled");
			break;
		case PLAYING:
			if (progressDialog != null) {
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
			}
			tv_buffering.setVisibility(View.GONE);
			Log.d("SimplePlayer", "Playing");

			getWindow()
					.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			_scrubber.setEnabled(true);
			_scrubber.setMax((int) asset.getDuration());
			_playButton.setImageResource(R.drawable.pause_selector);
			_scrubberMonitorTimer = new Timer();
			final TimerTask task = new TimerTask() {
				public void run() {
					if (_updateThumb && _pfview != null && _pfasset != null)
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
	protected int progressStatus;
	protected Handler handler;

	/**
	 * Called when pausing the app. This function pauses the playback of the
	 * asset when it is playing.
	 * 
	 */
	public void onPause() {
		super.onPause();
		if (_pfasset != null) {
			// if (_pfasset.getStatus() == PFAssetStatus.PLAYING) {
			// _pfasset.pause();
			// }
			_pfasset.pause();
			boolFlag = false;
			// startProgress(0);
			// if (countDownTimer!=null) {
			// countDownTimer.onFinish();
			// }
			if (countDownTimer2 != null) {
				countDownTimer2.onFinish();
			}

			// VideoPlayerView.this.finish();

			// }
		}

	}

	@Override
	protected void onResume() {

		// if (videoLink!=null && videoLink.length()>0) {
		// loadVideo(videoLink);
		// }
		if (_Flag == true && _pfasset.getStatus() == PFAssetStatus.PAUSED) {
			// progressbar.setProgress(100);
			_pfasset.play();
		}
		super.onResume();
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
		if (_pfasset != null) {
			_pfasset.setPLaybackTime(seekbar.getProgress());

			_updateThumb = true;
		}
	}

	public void onStartCommand(Intent intent, int flags, int startId) {
		if (_pfasset != null) {
			_pfasset.play();
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if (_pfasset != null) {
			_pfasset.stop();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (_pfasset != null) {
			_pfasset.release();
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_POINTER_2_DOWN:

			controller_layout.setVisibility(View.VISIBLE);
			mHandler.removeCallbacks(mRunnable);
			mHandler.postDelayed(mRunnable, 2 * 1000);

		default:
			return super.onTouchEvent(event);
		}

	}
}