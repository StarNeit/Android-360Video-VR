package com.creativeinnovations.mea;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.os.StrictMode;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.degree.Twitter_code.Twitt_Sharing;
import com.degree.database.DataBaseHandler;
import com.degree.hitaishin.connection.ImageLoader;
import com.degree.hitaishin.connection.WebServiceNaming;
import com.degree.hitaishin.model.VideoList;
import com.duowan.mobile.example.netroid.netroid.Netroid;
import com.duowan.mobile.netroid.Listener;
import com.duowan.mobile.netroid.NetroidError;
import com.duowan.mobile.netroid.NetroidLog;
import com.duowan.mobile.netroid.RequestQueue;
import com.duowan.mobile.netroid.request.FileDownloadRequest;
import com.duowan.mobile.netroid.toolbox.FileDownloader;
//import com.facebook.android.DialogError;
//import com.facebook.android.Facebook;
//import com.facebook.android.Facebook.DialogListener;
//import com.facebook.android.FacebookError;
import com.squareup.picasso.Picasso;

@SuppressWarnings("deprecation")
public class DownloadActivity extends Activity implements OnClickListener{
	
	public static ArrayList<VideoList> videoList;
	ImageButton btnBack, btnSearch;
	TextView btnTitle;
	ImageView imageTitle;
	ImageView slider_Image;
	static ListView videoListView;
	Button btnDownload;
	static ArrayList<DownloadTask> taskList;
	static DataBaseHandler db;
	// ProgressBar pbar;
	private static final String mSaveDirPath = "/sdcard/360/";
	static VideoDownloadListAdapter videoListAdapter;
	Activity activity;
	EditText editDownload;
	private static FileDownloader mDownloder;
	RequestQueue queue;
	static Activity context;
	public static int flag = 0;
	TextView tvwhatsnew,tvpopular,tvlive;
	LinearLayout llllive;
	ImageView ivlive;
	AnimatedActivity parentActivity;
	Intent _intent;
	public static final String UPDATE_LIST = "com.gokicksales.crm.LeadModule.LeadListActivity";
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.download_activity);
		
		db = new DataBaseHandler(getApplicationContext());
		File downloadDir = new File(mSaveDirPath);
		if (!downloadDir.exists())
			downloadDir.mkdir();

		queue = Netroid.newRequestQueue(DownloadActivity.this, null);
		mDownloder = new FileDownloader(queue, 1) {
			@Override
			public FileDownloadRequest buildRequest(String storeFilePath,
					String url) {
				return new FileDownloadRequest(storeFilePath, url) {
					@Override
					public void prepare() {
						addHeader("Accept-Encoding", "identity");
						super.prepare();
					}
				};
			}
		};
		context = this.getParent();
		videoList = db.getAllVideo();
		Log.i("Anish", videoList.toString());
		taskList = new ArrayList<DownloadActivity.DownloadTask>();
		
		for (int i = 0; i < videoList.size(); i++) {
			final DownloadTask task = new DownloadTask(videoList.get(i)
					.getVideo_id() + ".mp4", videoList.get(i).getVideo_link(),
					videoList.get(i));
			taskList.add(task);
			task.controller = mDownloder.add(mSaveDirPath + task.storeFileName,
					task.url, new Listener<Void>() {
						@Override
						public void onPreExecute() {
							task.invalidate();
						}

						@Override
						public void onSuccess(Void response) {
							// showToast(task.storeFileName + " Success!");
						}

						@Override
						public void onError(NetroidError error) {
							NetroidLog.e(error.getMessage());
						}

						@Override
						public void onFinish() {
							NetroidLog.e("onFinish size : "
									+ Formatter.formatFileSize(
											DownloadActivity.this,
											new File(mSaveDirPath
													+ task.storeFileName)
													.length()));
							task.invalidate();
						}

						@Override
						public void onProgressChange(long fileSize,
								long downloadedSize) {
							task.onProgressChange(fileSize, downloadedSize);
							// task.btnStatus.setText(task.downloadedSize
							// * 1.0f / task.fileSize * 100 + '%'+"");

							// NetroidLog.e("---- fileSize : " + fileSize +
							// " downloadedSize : " + downloadedSize);
						}
					});
		}

		init();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		registerReceiver(receiver, new IntentFilter(DownloadActivity.UPDATE_LIST));
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		editDownload.setText("");
	}

	
	public BroadcastReceiver receiver = new BroadcastReceiver(){
	    @Override
	    public void onReceive(Context context, Intent intent) 
	    {
		      Bundle bundle = intent.getExtras();
		      if (bundle != null) 
		      {
			        int resultCode = bundle.getInt("RESULT");
			        if (resultCode == 1) 
			        {
			        	dothings();
			        }
			        unregisterReceiver(receiver);
		        	registerReceiver(receiver, new IntentFilter(DownloadActivity.UPDATE_LIST));
		      }
	    }
	 };
	
	
	public void dothings()
	{
		@SuppressWarnings("unused")
		int size = videoList.size();
		DataBaseHandler db = new DataBaseHandler(getApplicationContext());
		videoList = db.getAllVideo();

		if (flag != 0) {
			queue = Netroid.
					newRequestQueue(DownloadActivity.this, null);
		/*for (int i = 0; i < taskList.size(); i++) {
			if(taskList.get(i).videoList.getVideo_id().equals(videoList.get(videoList.size()-1).getVideo_id()))
				return;
		}*/
			
			flag = 1;
			for (int i = videoList.size() - flag; i < videoList.size(); i++) {
				final DownloadTask task = new DownloadTask(videoList.get(i)
						.getVideo_id() + ".mp4", videoList.get(i)
						.getVideo_link(), videoList.get(i));
				taskList.add(task);
				
				task.controller = mDownloder.add(mSaveDirPath
						+ task.storeFileName, task.url, new Listener<Void>() {
					@Override
					public void onPreExecute() {
						task.invalidate();
					}

					@Override
					public void onSuccess(Void response) {
						// showToast(task.storeFileName + " Success!");
					}

					@Override
					public void onError(NetroidError error) {
						NetroidLog.e(error.getMessage());
					}

					@Override
					public void onFinish() {
						NetroidLog.e("onFinish size : "
								+ Formatter.formatFileSize(
										DownloadActivity.this, new File(
												mSaveDirPath
														+ task.storeFileName)
												.length()));
						task.invalidate();
					}

					@Override
					public void onProgressChange(long fileSize,
							long downloadedSize) {
						task.onProgressChange(fileSize, downloadedSize);
						// task.btnStatus.setText(task.downloadedSize
						// * 1.0f / task.fileSize * 100 + '%'+"");

						// NetroidLog.e("---- fileSize : " + fileSize +
						// " downloadedSize : " + downloadedSize);
					}
				});
			}
		
			
		
			flag = 0;
			videoListAdapter = new VideoDownloadListAdapter(this, taskList,mDownloder);
			videoListView.setAdapter(videoListAdapter);
			videoListAdapter.notifyDataSetChanged();

		}

	}
	
	private void init() {
		// TODO Auto-generated method stub
	/*	btnSearch = (ImageButton) findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent videoIntent = new Intent(DownloadActivity.this,
						SearchActivity.class);

				AnimatedActivity parentActivity = (AnimatedActivity) DownloadActivity.this
						.getParent();
				parentActivity.startChildActivity("LiveStream", videoIntent);
			}
		});*/
		btnTitle = (TextView) findViewById(R.id.page_Title);
		imageTitle = (ImageView) findViewById(R.id.category_icon);

		videoListView = (ListView) findViewById(R.id.videoListView);

		activity = this.getParent();
		videoListAdapter = new VideoDownloadListAdapter(activity, taskList,mDownloder);
		videoListView.setAdapter(videoListAdapter);
		tvwhatsnew = (TextView) findViewById(R.id.tvwhatsnew);
		tvwhatsnew.setOnClickListener(this);
		tvpopular = (TextView) findViewById(R.id.tvpopular);
		tvpopular.setOnClickListener(this);
		tvlive = (TextView) findViewById(R.id.tvlive);
		tvlive.setOnClickListener(this);
		llllive = (LinearLayout) findViewById(R.id.llllive);
		llllive.setOnClickListener(this);
		ivlive =  (ImageView) findViewById(R.id.ivlive);
		ivlive.setOnClickListener(this);
		btnSearch = (ImageButton) findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(this);

		btnDownload = (Button) findViewById(R.id.btnDownload);
		editDownload = (EditText) findViewById(R.id.editDownload);
		editDownload.setImeOptions(EditorInfo.IME_ACTION_DONE);
		// editDownload.setOnFocusChangeListener(new
		// View.OnFocusChangeListener() {
		// @Override
		// public void onFocusChange(View v, boolean hasFocus) {
		// if (!hasFocus) {
		//
		// }
		// }
		// });

		// videoListView.addHeaderView(headerView);
		btnDownload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {/*
				// TODO Auto-generated method stub
				boolean check = Patterns.WEB_URL
						.matcher(editDownload.getText()).matches();
				if (check) {
					db.addVIDEO(new VideoList("Default", "Default", "Default",
							"Default", "Default", editDownload.getText()
									.toString(), "Default", "Default",
							"Default", "Default", "Default","Default"));
					videoList.add(new VideoList("Default", "Default",
							"Default", "Default", "Default", editDownload
									.getText().toString(), "Default",
							"Default", "Default", "Default", "Default","Default"));
					
					DownloadActivity.flag++;
					//onResume();
					dothings();
					
					videoListAdapter.notifyDataSetChanged();
					AlertDialog.Builder builder = new AlertDialog.Builder(
							context.getParent());
					builder.setMessage("DOWNLOAD STARTED")
							.setCancelable(false)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											// do things
											dialog.dismiss();

											TabActivity tabs = (TabActivity) context
													.getParent();
											tabs.getTabHost().setCurrentTab(1);

										}
									});
					AlertDialog alert = builder.create();
					alert.show();
					editDownload.setText("");
				} else {
					new AlertDialog.Builder(DownloadActivity.this.getParent())
							.setIcon(R.drawable.ic_launcher)
							.setTitle("Video Download")
							.setMessage("Url is not valid")
							.setPositiveButton("ok",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {

											dialog.dismiss();

										}

									}).show();
				}

			*/
				
			DataBaseHandler _dbHandler = new DataBaseHandler(getApplicationContext());
			videoList = 	_dbHandler.getAllVideoBySearch(editDownload.getText().toString());
			taskList = new ArrayList<DownloadActivity.DownloadTask>();
			//dothings();
			for (int i = 0; i < videoList.size(); i++) {
				final DownloadTask task = new DownloadTask(videoList.get(i)
						.getVideo_id() + ".mp4", videoList.get(i).getVideo_link(),
						videoList.get(i));
				taskList.add(task);
				task.controller = mDownloder.add(mSaveDirPath + task.storeFileName,
						task.url, new Listener<Void>() {
							@Override
							public void onPreExecute() {
								task.invalidate();
							}

							@Override
							public void onSuccess(Void response) {
								// showToast(task.storeFileName + " Success!");
							}

							@Override
							public void onError(NetroidError error) {
								NetroidLog.e(error.getMessage());
							}

							@Override
							public void onFinish() {
								NetroidLog.e("onFinish size : "
										+ Formatter.formatFileSize(
												DownloadActivity.this,
												new File(mSaveDirPath
														+ task.storeFileName)
														.length()));
								task.invalidate();
							}

							@Override
							public void onProgressChange(long fileSize,
									long downloadedSize) {
								task.onProgressChange(fileSize, downloadedSize);
								// task.btnStatus.setText(task.downloadedSize
								// * 1.0f / task.fileSize * 100 + '%'+"");

								// NetroidLog.e("---- fileSize : " + fileSize +
								// " downloadedSize : " + downloadedSize);
							}
						});
			}
			//Log.i("Anish", videoList.toString());
			//Log.i("Anish", taskList.toString());
			videoListAdapter = new VideoDownloadListAdapter(getParent(), taskList,mDownloder);
			videoListView.setAdapter(videoListAdapter);
			videoListAdapter.notifyDataSetChanged();
			}
		});

	}

	private static final String CHAR_LIST = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	private static final int RANDOM_STRING_LENGTH = 6;

	/**
	 * This method generates random string
	 * 
	 * @return
	 */

	public String generateRandomString() {

		StringBuffer randStr = new StringBuffer();
		for (int i = 0; i < RANDOM_STRING_LENGTH; i++) {
			int number = getRandomNumber();
			char ch = CHAR_LIST.charAt(number);
			randStr.append(ch);
		}
		return randStr.toString();
	}

	private int getRandomNumber() {
		int randomInt = 0;
		Random randomGenerator = new Random();
		randomInt = randomGenerator.nextInt(CHAR_LIST.length());
		if (randomInt - 1 == -1) {
			return randomInt;
		} else {
			return randomInt - 1;
		}
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	class DownloadFileFromURL extends AsyncTask<String, String, String> {
		ProgressDialog pDialog;
		String Error;

		/**
		 * Before starting background thread Show Progress Bar Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(DownloadActivity.this.getParent());
			pDialog.setMessage("Downloading file. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setMax(100);
			pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... f_url) {
			int count;
			try {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
						.permitAll().build();
				StrictMode.setThreadPolicy(policy);
				URL url = new URL(f_url[0]);
				URLConnection conection = url.openConnection();
				conection.connect();

				// this will be useful so that you can show a tipical 0-100%
				// progress bar
				int lenghtOfFile = conection.getContentLength();

				// download the file
				InputStream input = new BufferedInputStream(url.openStream(),
						8192);

				// Output stream
				File direct = new File(Environment
						.getExternalStorageDirectory().toString()
						+ "/360degree/");

				if (!direct.exists()) {
					direct.mkdirs();
				}
				OutputStream output = new FileOutputStream(Environment
						.getExternalStorageDirectory().toString()
						+ "/360degree/" + generateRandomString() + ".mp4");
				Log.e("download", "start");
				byte data[] = new byte[1024];

				long total = 0;

				while ((count = input.read(data)) != -1) {
					total += count;
					publishProgress("" + (int) ((total * 100) / lenghtOfFile));

					// writing data to file
					output.write(data, 0, count);
				}

				// flushing output
				output.flush();

				// closing streams
				output.close();
				input.close();

			} catch (Exception e) {
				Error = e.toString();
				Log.e("Error: ", e.getMessage());
			}

			return null;
		}

		/**
		 * Updating progress bar
		 * */
		protected void onProgressUpdate(String... progress) {
			// setting progress percentage
			pDialog.setProgress(Integer.parseInt(progress[0]));
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		@Override
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after the file was downloaded
			pDialog.dismiss();

			if (Error != null)
				Toast.makeText(DownloadActivity.this, Error, Toast.LENGTH_LONG)
						.show();

		}

	}

	class VideoDownloadListAdapter extends BaseAdapter {
		ArrayList<DownloadTask> videoList;
		Activity context;
		String filename;

		VideoList videos;
		// Progress Dialog
		@SuppressWarnings("unused")
		private ProgressDialog pDialog;
		private static final String APP_ID = "893530320657596";

//		private Facebook facebook;

		private static final String TOKEN = "access_token";
		private static final String EXPIRES = "expires_in";
		private static final String KEY = "facebook-credentials";

		public static final int progress_bar_type = 0;
		String textFacebook;
		ImageLoader imgLoader;
		ProgressBar bar;

		@SuppressWarnings("unused")
		private static final String mSaveDirPath = "/sdcard/360/";

		FileDownloader mDownloder;

		/**
		 * 
		 */
		@SuppressWarnings({ "unused" })
		public VideoDownloadListAdapter(Activity context,
				ArrayList<DownloadTask> videoList, FileDownloader mDownloder) {
			// TODO Auto-generated constructor stub
			this.context = context;
			this.videoList = videoList;
//			facebook = new Facebook(APP_ID);
			final DecimalFormat DECIMAL_POINT = new DecimalFormat("0.0");
//			restoreCredentials(facebook);
			imgLoader = new ImageLoader(context);
			this.mDownloder = mDownloder;

		}

 
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return videoList.size();
		}

		@Override
		public DownloadTask getItem(int position) {
			// TODO Auto-generated method stub
			return videoList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@SuppressWarnings("unused")
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			videos = videoList.get(position).videoList;
			final DownloadTask task = getItem(position);
			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.video_row, null);

			}
			task.textTitl1 = (TextView) convertView
					.findViewById(R.id.textVideoTitlea);
			task.textCountry = (TextView) convertView
					.findViewById(R.id.textCountry);
			task.playImage = (ImageButton) convertView
					.findViewById(R.id.playImage);
			task.textCategory = (TextView) convertView
					.findViewById(R.id.textCategory);

			task.textDetail = (TextView) convertView
					.findViewById(R.id.textDetail);

			task.textTitle = (TextView) convertView
					.findViewById(R.id.textVideoTitle);
			task.linearLayout = (LinearLayout) convertView
					.findViewById(R.id.LinearLayout);
			task.videoImage = (ImageView) convertView
					.findViewById(R.id.videoImage);
			task.twiterImage = (ImageButton) convertView
					.findViewById(R.id.twiter);
			task.downloadIMage = (ImageButton) convertView
					.findViewById(R.id.download);

			task.facebookImage = (ImageButton) convertView
					.findViewById(R.id.facebook);
			task.download_progress = (RelativeLayout) convertView
					.findViewById(R.id.download_progress);

			task.bar = (ProgressBar) convertView.findViewById(R.id.bar);

			TextView txvTaskName = (TextView) convertView
					.findViewById(R.id.textVideoTitle);
			txvTaskName.setText(task.storeFileName);
			task.btnStatus = (TextView) convertView
					.findViewById(R.id.tv_progress_circle);

			task.btnStatus.setTag(task.storeFileName);

			int loader = R.drawable.blank_thumb;
//			imgLoader.DisplayImage(videos.getVideo_thumbnail(), task.videoImage);
			Picasso.with(context).load(videos.getVideo_thumbnail()).into(task.videoImage);
			task.textCategory.setText(videos.getCategory_name());
			task.textCountry.setText(videos.getVideo_city());

			task.textDetail.setText(videos.getVideo_description());
			task.download_progress.setVisibility(View.VISIBLE);
			String title = videos.getVideo_title();
			
			File file = new File(videos.getUpload_date());
			if (file.exists()) {
				task.download_progress.setVisibility(View.INVISIBLE);
			} else {
				task.download_progress.setVisibility(View.VISIBLE);
			}

			char c = (char) 0x00B0;
			title = title.replaceAll("0x00B0", c + "");
			task.textTitle.setText(title);
			task.textTitl1.setText(title);

			task.twiterImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					List<Intent> targetedShareIntents = new ArrayList<Intent>();

					Intent facebookIntent = getShareIntent(
							"com.facebook.katana", "subject",
							"http://www.360mea.com/video-detail?id="
									+ videos.getVideo_id());

					if (facebookIntent != null) {

						task.videoImage.buildDrawingCache();
						Bitmap bm = task.videoImage.getDrawingCache();
						OutputStream fOut = null;
						Uri outputFileUri;
						File root = null;
						File sdImageMainDirectory = null;
						try {
							root = new File(Environment
									.getExternalStorageDirectory()
									+ File.separator
									+ "folder_name"
									+ File.separator);
							if (root.exists()) {
								root.delete();
							}
							root.mkdirs();

							sdImageMainDirectory = new File(root,
									"myPicName.jpg");
							outputFileUri = Uri.fromFile(sdImageMainDirectory);
							fOut = new FileOutputStream(sdImageMainDirectory);
						} catch (Exception e) {

						}

						try {
							bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
							fOut.flush();
							fOut.close();
						} catch (Exception e) {
						}

						share("twitter",
								sdImageMainDirectory.toString(),
								videos.getVideo_title()
										+ "\n\n"
										+ "Click on the link to view an amazing 360 video http://www.360mea.com/video-detail?id="
										+ videos.getVideo_id());

					} else {
						Twitt_Sharing twitt = new Twitt_Sharing(context,
								"xwWUX7uRyXhZXPjQn4pJC4USf",
								"clzs0pJjv4wPhb7tov2yNhSKjR1BGW5LTgR8lQs7PVoldN4LM4");
						String string_img_url = "http://3.bp.blogspot.com/_Y8u09A7q7DU/S-o0pf4EqwI/AAAAAAAAFHI/PdRKv8iaq70/s1600/id-do-anything-logo.jpg";
						//String string_msg = "https://chintankhetiya.wordpress.com/";
						
						String string_msg = "http://www.360mea.com/video-detail?id="+ videos.getVideo_id();
						twitt.shareToTwitter(string_msg, new File(
								string_img_url));
					}
				}
			});
			task.facebookImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					List<Intent> targetedShareIntents = new ArrayList<Intent>();

					Intent facebookIntent = getShareIntent(
							"com.facebook.katana", "subject",
							"http://www.360mea.com/video-detail?id="
									+ videos.getVideo_id());

					if (facebookIntent != null) {
						targetedShareIntents.add(facebookIntent);

						Intent chooser = Intent.createChooser(
								targetedShareIntents.remove(0), "Delen");

						chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS,
								targetedShareIntents
										.toArray(new Parcelable[] {}));
						chooser.putExtra(Intent.EXTRA_TEXT,
								"http://www.360mea.com/video-detail?id="
										+ videos.getVideo_id());
						context.startActivity(chooser);
					} else {

						Bundle parameters = new Bundle();
						parameters.putString("link",
								"http://www.360mea.com/video-detail?id="
										+ videos.getVideo_id());

					/*	facebook.dialog(context, "feed", parameters,
								new DialogListener() {

									@Override
									public void onFacebookError(
											FacebookError arg0) {
										// Display your message for facebook
										// error

									}

									@Override
									public void onError(DialogError arg0) {
										// Display your message for error

									}

									@Override
									public void onComplete(Bundle arg0) {
										// Display your message on share
										// scuccessful

									}

									@Override
									public void onCancel() {
										// Display your message on dialog cancel

									}
								});
					*/
					}
				}
			});
			
			task.downloadIMage.setImageResource(R.drawable.delete_one);
			task.downloadIMage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					DataBaseHandler db = new DataBaseHandler(context);
					db.deleteVIDEO(videoList.get(position).videoList);

					videoList.remove(position);
					notifyDataSetChanged();
				}
			});
			
			task.linearLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					Intent videoIntent = new Intent(context,
							VideoDetailDownloadActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable(WebServiceNaming.TAG_VIDEO_LINK,
							videoList.get(position).videoList);
					videoIntent.putExtra("position", position);
					videoIntent.putExtras(bundle);

					AnimatedActivity parentActivity = (AnimatedActivity) DownloadActivity.this.getParent();;
					parentActivity.startChildActivity("detailss", videoIntent);

				}
			});
			
			task.playImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					File file = new File(videoList.get(position).videoList.getUpload_date());

					if (file.exists()) {

						Intent videoIntent = new Intent(context,VideoPlayerViewDownload.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable(WebServiceNaming.TAG_VIDEO_LINK,videoList.get(position).videoList);
						videoIntent.putExtras(bundle);

						context.startActivity(videoIntent);
					} else {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								context.getParent());
						builder.setMessage("File not available!")
								.setCancelable(false)
								.setPositiveButton("OK",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												// do things
												dialog.dismiss();
											}
										});
						AlertDialog alert = builder.create();
						alert.show();
					}

				}
			});

			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					File file = new File(videoList.get(position).videoList
							.getUpload_date());

					if (file.exists()) {

						Intent videoIntent = new Intent(context,
								VideoPlayerViewDownload.class);
						Bundle bundle = new Bundle();

						bundle.putSerializable(WebServiceNaming.TAG_VIDEO_LINK,
								videoList.get(position).videoList);
						videoIntent.putExtras(bundle);

						context.startActivity(videoIntent);
					} else {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								context.getParent());
						builder.setMessage("File not available!")
								.setCancelable(false)
								.setPositiveButton("OK",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												// do things
												dialog.dismiss();
											}
										});
						AlertDialog alert = builder.create();
						alert.show();
					}

				}
			});

			DataBaseHandler db = new DataBaseHandler(context);
			notifyDataSetChanged();
			task.invalidate();
			return convertView;
		}

		private Intent getShareIntent(String type, String subject, String text) {
			boolean found = false;
			Intent share = new Intent(android.content.Intent.ACTION_SEND);
			share.setType("text/plain");

			// gets the list of intents that can be loaded.
			List<ResolveInfo> resInfo = context.getPackageManager()
					.queryIntentActivities(share, 0);
			System.out.println("resinfo: " + resInfo);
			if (!resInfo.isEmpty()) {
				for (ResolveInfo info : resInfo) {
					if (info.activityInfo.packageName.toLowerCase().contains(
							type)
							|| info.activityInfo.name.toLowerCase().contains(
									type)) {
						share.putExtra(Intent.EXTRA_SUBJECT, subject);
						share.putExtra(Intent.EXTRA_TEXT, text);
						share.setPackage(info.activityInfo.packageName);
						found = true;
						break;
					}
				}
				if (!found)
					return null;

				return share;
			}
			return null;
		}

		/**
		 * 
		 */

	}

	public static class DownloadTask {

		private static final String mSaveDirPath = "/sdcard/360/";
		private final static DecimalFormat DECIMAL_POINT = new DecimalFormat(
				"0");

		public TextView textTitle,textTitl1;
		public TextView textCategory;
		public TextView textCountry;
		public TextView textDownload;
		public TextView textDetail;
		public TextView tv_progress_circle;
		public ImageView videoImage;
		public ImageButton facebookImage;
		public ImageButton twiterImage, playImage;;
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

		private void onProgressChange(long fileSize, long downloadedSize) {
			this.fileSize = fileSize;
			this.downloadedSize = downloadedSize;
			invalidate();
		}

		private void invalidate() {
			if (btnStatus == null)
				return;
			if (!TextUtils.equals((CharSequence) btnStatus.getTag(),
					storeFileName))
				return;

			switch (controller.getStatus()) {
			case FileDownloader.DownloadController.STATUS_DOWNLOADING:
				if (fileSize > 0 && downloadedSize > 0) {
					btnStatus.setText(DECIMAL_POINT.format(downloadedSize
							* 1.0f / fileSize * 100) + '%');
				} else {
					btnStatus.setText("0%");
				}
				break;
			case FileDownloader.DownloadController.STATUS_WAITING:
				btnStatus.setText("waiting");
				break;
			case FileDownloader.DownloadController.STATUS_PAUSE:
				btnStatus.setText("0%");
				break;
			case FileDownloader.DownloadController.STATUS_SUCCESS:
				btnStatus.setText("done");
				videoList.setUpload_date(mSaveDirPath + videoList.getVideo_id()
						+ ".mp4");
				db.updateVideoRecord(videoList);
				break;
			}

		}

		public DownloadTask(String storeFileName, String url, VideoList video) {
			this.storeFileName = storeFileName;
			this.url = url;
			this.videoList = video;
		}

	}

	public void share(String nameApp, String imagePath, String message) {
		try {
			List<Intent> targetedShareIntents = new ArrayList<Intent>();
			Intent share = new Intent(android.content.Intent.ACTION_SEND);
			share.setType("image/jpeg");
			List<ResolveInfo> resInfo = context.getPackageManager()
					.queryIntentActivities(share, 0);
			if (!resInfo.isEmpty()) {
				for (ResolveInfo info : resInfo) {
					Intent targetedShare = new Intent(
							android.content.Intent.ACTION_SEND);
					targetedShare.setType("image/jpeg"); // put here your mime
															// type
					if (info.activityInfo.packageName.toLowerCase().contains(
							nameApp)
							|| info.activityInfo.name.toLowerCase().contains(
									nameApp)) {
						targetedShare.putExtra(Intent.EXTRA_SUBJECT,
								"Sample Photo");
						targetedShare.putExtra(Intent.EXTRA_TEXT, message);
						targetedShare.putExtra(Intent.EXTRA_STREAM,
								Uri.fromFile(new File(imagePath)));
						targetedShare.setPackage(info.activityInfo.packageName);
						targetedShareIntents.add(targetedShare);
					}
				}
				Intent chooserIntent = Intent.createChooser(
						targetedShareIntents.remove(0), "Select app to share");
				chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
						targetedShareIntents.toArray(new Parcelable[] {}));
				context.startActivity(chooserIntent);
			}
		} catch (Exception e) {
			Log.v("VM",
					"Exception while sending image on" + nameApp + " "
							+ e.getMessage());
		}

	}

	@SuppressWarnings("unused")
	private CompressFormat getFormat(String filename) {
		int periodIndex = filename.lastIndexOf(".");
		if (periodIndex != -1) {
			String imageformat = filename.substring(periodIndex);
			// Log.d(TAG, "Image file format: " + fileformat);
			if (imageformat.equals(".png"))
				return CompressFormat.PNG;
		}
		return CompressFormat.JPEG;
	}

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tvwhatsnew:
			_intent = new Intent(DownloadActivity.this,
					VideoListActivity.class);
			_intent.putExtra(WebServiceNaming.TAG_WHATS_NEW,"1");
			_intent.putExtra(
					WebServiceNaming.TAG_CATEGORY_NAME,"");
			_intent.putExtra(WebServiceNaming.TAG_IMAGE,1);
			_intent.putExtra(WebServiceNaming.TAG_CITY, "");
			_intent.putExtra(
					WebServiceNaming.TAG_SUB_CATEGORY_ID, "");
			_intent.putExtra(WebServiceNaming.TAG_KEYWORD, "");

			parentActivity = (AnimatedActivity) DownloadActivity.this
					.getParent();
			parentActivity.startChildActivity(
					"FrequentMessageActivity", _intent);
			break;

	 case R.id.tvpopular:
		 _intent = new Intent(DownloadActivity.this,
					VideoListActivity.class);
			_intent.putExtra(
					WebServiceNaming.TAG_CATEGORY_NAME,"");
			_intent.putExtra(WebServiceNaming.POPULAR,"1");
			_intent.putExtra(WebServiceNaming.TAG_IMAGE,1);
			_intent.putExtra(WebServiceNaming.TAG_CITY, "");
			_intent.putExtra(
					WebServiceNaming.TAG_SUB_CATEGORY_ID, "");
			_intent.putExtra(WebServiceNaming.TAG_KEYWORD, "");

			parentActivity = (AnimatedActivity) DownloadActivity.this
					.getParent();
			parentActivity.startChildActivity(
					"FrequentMessageActivity", _intent);
			break;
	 case R.id.tvlive:
		 _intent = new Intent(DownloadActivity.this,
					LiveStream.class);

			 parentActivity = (AnimatedActivity) DownloadActivity.this
					.getParent();
			parentActivity
					.startChildActivity("LiveStream", _intent);
		break;
	 case R.id.llllive:
		 _intent = new Intent(DownloadActivity.this,
					LiveStream.class);

			 parentActivity = (AnimatedActivity) DownloadActivity.this
					.getParent();
			parentActivity
					.startChildActivity("LiveStream", _intent);
		break;
	 case R.id.ivlive:
		 _intent = new Intent(DownloadActivity.this,
					LiveStream.class);

			 parentActivity = (AnimatedActivity) DownloadActivity.this
					.getParent();
			parentActivity
					.startChildActivity("LiveStream", _intent);
		break;
	 case R.id.btnSearch:
			Intent videoIntent = new Intent(DownloadActivity.this,
					SearchActivity.class);

			AnimatedActivity parentActivity = (AnimatedActivity) DownloadActivity.this
					.getParent();
			parentActivity.startChildActivity("LiveStream", videoIntent);
		break;

		}
	}
	
}