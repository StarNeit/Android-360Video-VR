/**
 * 
 */
package com.creativeinnovations.mea;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.os.StrictMode;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.creativeinnovations.mea.network.CommonUtility;
import com.degree.Twitter_code.Twitt_Sharing;
import com.degree.database.DataBaseHandler;
import com.degree.hitaishin.connection.WebServiceNaming;
import com.degree.hitaishin.model.VideoList;

/**
 * @author sachin
 * 
 */
@SuppressWarnings("deprecation")
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
public class VideoDetailActivity extends Activity {
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	VideoList videoList;
	ImageButton btn_Back;
	int position;

	ImageView  slider_Image;
	TextView  textTitle, textCoutry, textCategory, textDetail,
			textLocation, textLocationURL;
	ArrayList<String> headingList;
	ImageButton btnFacebook, btnTwitter, btnDownload;
	Activity activity;
//	private Facebook facebook;
	private static final String APP_ID = "893530320657596";
	String URI;
	String filename;
	protected AnimatedActivity parentActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_page_detail);
		
		headingList = new ArrayList<String>();
		headingList.add("Real Estate");
		headingList.add("Event");
		headingList.add("Weddings");
		headingList.add("Other");
		headingList.add("Donations");
		headingList.add("Live Broadcast");
		activity = this.getParent();
//		facebook = new Facebook(APP_ID);
		position = getIntent().getExtras().getInt("position");
		videoList = (VideoList) getIntent().getExtras().getSerializable(
				WebServiceNaming.TAG_VIDEO_LINK);
		btn_Back = (ImageButton) findViewById(R.id.btn_Back);
		ImageView btnSearch = (ImageView) findViewById(R.id.btnSearch);
		slider_Image = (ImageView) findViewById(R.id.slider_Image);
	
		textTitle = (TextView) findViewById(R.id.textTitle);
		textCoutry = (TextView) findViewById(R.id.textCoutry);
		textCategory = (TextView) findViewById(R.id.textCategory);
		textDetail = (TextView) findViewById(R.id.textDetail);

		textLocation = (TextView) findViewById(R.id.textLocation);
		textLocationURL = (TextView) findViewById(R.id.textLocationURL);
		btnTwitter = (ImageButton) findViewById(R.id.btnTwitter);
		btnFacebook = (ImageButton) findViewById(R.id.btnFacebook);
		btnDownload = (ImageButton) findViewById(R.id.btnDownload);
		String title = videoList.getVideo_title();
		char c = (char) 0x00B0;
		title = title.replaceAll("0x00B0", c + "");
		textTitle.setText(title);
		textCoutry.setText(videoList.getVideo_city());
		textCategory.setText(videoList.getCategory_name());
		textDetail.setText(videoList.getVideo_description());
		Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
				R.drawable.other_slide);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;

		slider_Image.setImageBitmap(getResizedBitmap(largeIcon, width));

		textLocation.setText(videoList.getVideo_city());
		textLocationURL.setText(videoList.getWebsiteLink());
		textLocationURL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent viewIntent = new Intent("android.intent.action.VIEW",
						Uri.parse("" + videoList.getWebsiteLink()));
				startActivity(viewIntent);
			}
		});
		slider_Image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
/*			Intent videoIntent = new Intent(getApplicationContext(),
						VideoPlayerView.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable(WebServiceNaming.TAG_VIDEO_LINK,
						videoList);
				videoIntent.putExtras(bundle);

				startActivity(videoIntent);*/
				if(videoList.getVideoType().equals("html"))
				{
					CommonUtility.setGlobalString(getApplicationContext(), "HTML", videoList.getVideo_link());
					Intent videoIntent = new Intent(getApplicationContext(), WebViewActivity.class);
					startActivity(videoIntent);	
					
				}
				else
				{
					Intent videoIntent = new Intent(getApplicationContext(), VideoPlayerView.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable(WebServiceNaming.TAG_VIDEO_LINK,videoList);
					videoIntent.putExtras(bundle);
					startActivity(videoIntent);				
				}
				
			
			}
		});
		btnTwitter.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("unused")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				List<Intent> targetedShareIntents = new ArrayList<Intent>();

				Intent facebookIntent = getShareIntent("com.facebook.katana",
						"subject",
						"Click on the link to view an amazing 360 video http://www.360mea.com/video-detail?id="
								+ videoList.getVideo_id());

				if (facebookIntent != null) {

					slider_Image.buildDrawingCache();
					Bitmap bm = slider_Image.getDrawingCache();
					OutputStream fOut = null;
					Uri outputFileUri;
					File root = null;
					File sdImageMainDirectory = null;
					try {
						root = new File(Environment
								.getExternalStorageDirectory()
								+ File.separator
								+ "folder_name" + File.separator);
						if (root.exists()) {
							root.delete();
						}
						root.mkdirs();

						sdImageMainDirectory = new File(root, "myPicName.jpg");
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
							videoList.getVideo_title() + "\n\n"
									+"http://www.360mea.com/video-detail?id="
									+ videoList.getVideo_id());


				} else {

					Twitt_Sharing twitt = new Twitt_Sharing(activity,
							"xwWUX7uRyXhZXPjQn4pJC4USf",
							"clzs0pJjv4wPhb7tov2yNhSKjR1BGW5LTgR8lQs7PVoldN4LM4");

					twitt.shareToTwitter(videoList.getMeta_data()
							+ "http://www.360mea.com/video-detail?id="
							+ videoList.getVideo_id(), new File(
							"file:///android_asset/back.png"));
				}
			}
		});
		final Activity act = this.getParent();
		
		btnDownload.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(DownloadActivity.flag != 0)
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(act);
					builder.setMessage("Another Video Is Downloading")
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
					return;
				}
				else
				{
					DataBaseHandler db = new DataBaseHandler(
							VideoDetailActivity.this);
					ArrayList<VideoList> vlist = db.getAllVideo();
					DownloadActivity.flag++;
					boolean flag = true;
					for (int i = 0; i < vlist.size(); i++) {
						if (vlist.get(i).getVideo_id()
								.equals(videoList.getVideo_id())) {
							flag = false;
						}
					}
					if (flag) {
						db.addVIDEO(videoList);
						AlertDialog.Builder builder = new AlertDialog.Builder(act);
						builder.setMessage("DOWNLOAD STARTED")
								.setCancelable(false)
								.setPositiveButton("OK",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog, int id) {
												// do things
												dialog.dismiss();
												 Intent intent = new Intent(DownloadActivity.UPDATE_LIST);
								            	    intent.putExtra("RESULT", 1);
								            	    VideoDetailActivity.this.sendBroadcast(intent);
												TabActivity tabs = (TabActivity) activity
														.getParent();
												tabs.getTabHost().setCurrentTab(1);

											}
										});
						AlertDialog alert = builder.create();
						alert.show();
					} else {
						AlertDialog.Builder builder = new AlertDialog.Builder(act);
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
				
			}
		});
		btnFacebook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				List<Intent> targetedShareIntents = new ArrayList<Intent>();

				Intent facebookIntent = getShareIntent("com.facebook.katana",
						"subject",
						"http://www.360mea.com/video-detail?id="
								+ videoList.getVideo_id());

				if (facebookIntent != null) {
					targetedShareIntents.add(facebookIntent);

					Intent chooser = Intent.createChooser(
							targetedShareIntents.remove(0), "Delen");

					chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS,
							targetedShareIntents.toArray(new Parcelable[] {}));
					chooser.putExtra(Intent.EXTRA_TEXT,
							"http://www.360mea.com/video-detail?id="
									+ videoList.getVideo_id());
					startActivity(chooser);
				} else {
					videoList.getMeta_data();
					Bundle parameters = new Bundle();

					parameters.putString("link",
							"http://www.360mea.com/video-detail?id="
									+ videoList.getVideo_id());

				/*	facebook.dialog(activity, "feed", parameters,
							new DialogListener() {

								@Override
								public void onFacebookError(FacebookError arg0) {
									// Display your message for facebook error

								}

								@Override
								public void onError(DialogError arg0) {
									// Display your message for error

								}

								@Override
								public void onComplete(Bundle arg0) {
									// Display your message on share scuccessful

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
		
		
		if (videoList.getVideo_thumbnail()!=null &&  videoList.getVideo_thumbnail().length() > 0 ) {
			   new ImageLoader(slider_Image).execute("");
			  }

		btn_Back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finishFromChild(getParent());
			}
		});

		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent videoIntent = new Intent(VideoDetailActivity.this,
						SearchActivity.class);

				parentActivity = (AnimatedActivity) VideoDetailActivity.this
						.getParent();
				parentActivity.startChildActivity("LiveStream", videoIntent);
			}
		});
	}

	private Bitmap getResizedBitmap(Bitmap bm, int newWidth) {

		int width = bm.getWidth();

		int height = bm.getHeight();

		float aspect = (float) width / height;

		float scaleWidth = newWidth;

		float scaleHeight = scaleWidth / aspect; // yeah!

		// create a matrix for the manipulation

		Matrix matrix = new Matrix();

		// resize the bit map

		matrix.postScale(scaleWidth / width, scaleHeight / height);

		// recreate the new Bitmap

		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
				matrix, true);

		bm.recycle();

		return resizedBitmap;
	}




	public static Bitmap getBitmapFromURL(String src) {
		try {
			URL url = new URL(src);
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public class ImageLoader extends AsyncTask<String, Integer, Bitmap> {
		ProgressDialog pbar;
		@SuppressWarnings("unused")
		private final WeakReference<ImageView> viewReference;

		public ImageLoader(ImageView view) {
			viewReference = new WeakReference<ImageView>(view);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pbar = new ProgressDialog(VideoDetailActivity.this.getParent());
			pbar.setMessage("Loading.....");
			pbar.setCancelable(false);
			pbar.show();

		}

		@Override
		protected Bitmap doInBackground(String... params) {
			return getBitmapFromURL(videoList.getVideo_thumbnail());
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			// ImageView imageView = viewReference.get();
			// if( imageView != null ) {
			// imageView.setImageBitmap( bitmap );
			// }

			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			int width = size.x;
			// Next create a Bitmap object and download the image to bitmap

			slider_Image.setImageBitmap(getResizedBitmap(bitmap, width));
			if (pbar.isShowing()) {
				pbar.dismiss();
			}
		}

	}

	private Intent getShareIntent(String type, String subject, String text) {
		boolean found = false;
		Intent share = new Intent(android.content.Intent.ACTION_SEND);
		share.setType("text/plain");

		// gets the list of intents that can be loaded.
		List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(
				share, 0);
		System.out.println("resinfo: " + resInfo);
		if (!resInfo.isEmpty()) {
			for (ResolveInfo info : resInfo) {
				if (info.activityInfo.packageName.toLowerCase().contains(type)
						|| info.activityInfo.name.toLowerCase().contains(type)) {
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

	public void share(String nameApp, String imagePath, String message) {
		try {
			List<Intent> targetedShareIntents = new ArrayList<Intent>();
			Intent share = new Intent(android.content.Intent.ACTION_SEND);
			share.setType("image/jpeg");
			List<ResolveInfo> resInfo = getPackageManager()
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
				startActivity(chooserIntent);
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
}
