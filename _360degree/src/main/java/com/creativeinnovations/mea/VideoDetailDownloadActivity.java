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
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.degree.Twitter_code.Twitt_Sharing;
import com.degree.database.DataBaseHandler;
import com.degree.hitaishin.connection.WebServiceNaming;
import com.degree.hitaishin.model.VideoList;
//import com.facebook.android.DialogError;
//import com.facebook.android.Facebook;
//import com.facebook.android.Facebook.DialogListener;
//import com.facebook.android.FacebookError;

/**
 * @author sachin
 * 
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
public class VideoDetailDownloadActivity extends Activity {
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	VideoList videoList;
	ImageButton btn_Back;
	int position;

	int[] sliderImageList = { R.drawable.realestate_slide,
			R.drawable.events_slide, R.drawable.wedding_slide,
			R.drawable.other_slide, R.drawable.donations_silde,
			R.drawable.livebroadcast };
	int[] headingImakgeList = { R.drawable.realestate_icon, R.drawable.events,
			R.drawable.weddings, R.drawable.other, R.drawable.donations,
			R.drawable.livebroadcast };
	ImageView category_icon, slider_Image;
	TextView page_Title, textTitle, textCoutry, textCategory, textDetail,
			textLocation, textLocationURL;
	ArrayList<String> headingList;
	ImageButton btnFacebook, btnTwitter, btnDownload;
	Activity activity;
//	private Facebook facebook;
	private static final String APP_ID = "893530320657596";
	String URI;
	String filename;

	@SuppressWarnings("deprecation")
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
		category_icon = (ImageView) findViewById(R.id.category_icon);
		slider_Image = (ImageView) findViewById(R.id.slider_Image);
		category_icon.setImageResource(headingImakgeList[position]);
		slider_Image.setImageResource(sliderImageList[position]);
		page_Title = (TextView) findViewById(R.id.page_Title);
		page_Title.setText(headingList.get(position));

		textTitle = (TextView) findViewById(R.id.textTitle);
		textCoutry = (TextView) findViewById(R.id.textCoutry);
		textCategory = (TextView) findViewById(R.id.textCategory);
		textDetail = (TextView) findViewById(R.id.textDetail);

		textLocation = (TextView) findViewById(R.id.textLocation);
		textLocationURL = (TextView) findViewById(R.id.textLocationURL);
		btnTwitter = (ImageButton) findViewById(R.id.btnTwitter);
		btnFacebook = (ImageButton) findViewById(R.id.btnFacebook);
		btnDownload = (ImageButton) findViewById(R.id.btnDownload);
		Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
				R.drawable.other_slide);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		slider_Image.setImageBitmap(getResizedBitmap(largeIcon, width));
		String title = videoList.getVideo_title();
		char c = (char) 0x00B0;
		title = title.replaceAll("0x00B0", c + "");
		textTitle.setText(title);
		textCoutry.setText(videoList.getVideo_city());
		textCategory.setText(videoList.getCategory_name());
		textDetail.setText(videoList.getVideo_description());

		textLocation.setText(videoList.getVideo_city());
		textLocationURL.setText(videoList.getWebsiteLink());
		textLocationURL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent viewIntent = new Intent("android.intent.action.VIEW",
						Uri.parse("http://" + videoList.getWebsiteLink()));
				startActivity(viewIntent);
			}
		});
		slider_Image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent videoIntent = new Intent(getApplicationContext(),
						VideoPlayerViewDownload.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable(WebServiceNaming.TAG_VIDEO_LINK,
						videoList);
				videoIntent.putExtras(bundle);

				startActivity(videoIntent);
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
							videoList.getVideo_title()
									+ "\n\n"
									+ "http://www.360mea.com/video-detail?id="
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
		btnDownload.setImageResource(R.drawable.delete_one);
		btnDownload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DataBaseHandler db = new DataBaseHandler(
						getApplicationContext());
				db.deleteVIDEO(videoList);
				finishFromChild(getParent());

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
					parameters.putString("link","http://www.360mea.com/video-detail?id="+ videoList.getVideo_id());

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
		new ImageLoader(slider_Image).execute("");

		btn_Back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finishFromChild(getParent());
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
			pbar = new ProgressDialog(
					VideoDetailDownloadActivity.this.getParent());
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
			if (bitmap != null) {
				slider_Image.setImageBitmap(getResizedBitmap(bitmap, width));
			}
			if (pbar.isShowing()) {
				pbar.dismiss();
			}
		}

	}

	class DownloadFileFromURL extends AsyncTask<String, Integer, String> {

		/**
		 * Before starting background thread Show Progress Bar Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Toast.makeText(getApplicationContext(), "download start",
					Toast.LENGTH_LONG).show();
			filename = videoList.getVideo_id();
		}

		@Override
		protected String doInBackground(String... f_url) {
			int count;
			try {
				Log.e("progress", "run");
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
						192);

				// Output stream
				File direct = new File(Environment
						.getExternalStorageDirectory().toString()
						+ "/360degree/");

				if (!direct.exists()) {
					direct.mkdirs();
				}
				publishProgress(50);
				URI = Environment.getExternalStorageDirectory().toString()
						+ "/360degree/" + filename + ".mp4";

				OutputStream output = new FileOutputStream(Environment
						.getExternalStorageDirectory().toString()
						+ "/360degree/" + filename + ".mp4");
				Log.e("download", "start");
				byte data[] = new byte[1024];

				long total = 0;

				while ((count = input.read(data)) != -1) {
					total += count;
					// publishing the progress....
					// After this onProgressUpdate will be called
					publishProgress((int) ((total * 100) / lenghtOfFile));

					// writing data to file
					output.write(data, 0, count);
					Log.e("progress", count + "");
				}

				// flushing output
				output.flush();

				// closing streams
				output.close();
				input.close();

			} catch (Exception e) {
				Log.e("Error: ", e.getMessage());
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(final Integer... values) {

			// setting progress percentage
			// bar.setProgress(values[0]);
			Log.e("progress", values[0] + "");

		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		@Override
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after the file was downloaded

			Log.e("360MEA", "download completd");
			Toast.makeText(getApplicationContext(), "Downloading Completed",
					Toast.LENGTH_LONG).show();
			DataBaseHandler db = new DataBaseHandler(getApplicationContext());
			// videos.setVideo_link(URI);
			db.addVIDEO(videoList);

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
