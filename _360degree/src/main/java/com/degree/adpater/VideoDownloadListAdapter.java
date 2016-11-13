/**
 * 
 */
package com.degree.adpater;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.creativeinnovations.mea.AnimatedActivity;
import com.creativeinnovations.mea.DownloadTask;
import com.creativeinnovations.mea.R;
import com.creativeinnovations.mea.VideoDetailDownloadActivity;
import com.creativeinnovations.mea.VideoPlayerViewDownload;
import com.degree.Twitter_code.Twitt_Sharing;
import com.degree.database.DataBaseHandler;
import com.degree.hitaishin.connection.ImageLoader;
import com.degree.hitaishin.connection.WebServiceNaming;
import com.degree.hitaishin.model.VideoList;
import com.duowan.mobile.netroid.Listener;
import com.duowan.mobile.netroid.NetroidError;
import com.duowan.mobile.netroid.NetroidLog;
import com.duowan.mobile.netroid.toolbox.FileDownloader;
//import com.facebook.android.DialogError;
//import com.facebook.android.Facebook;
//import com.facebook.android.Facebook.DialogListener;
//import com.facebook.android.FacebookError;
import com.squareup.picasso.Picasso;

/**
 * @author sachin
 * 
 */
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class VideoDownloadListAdapter extends BaseAdapter {
	ArrayList<DownloadTask> videoList;
	Activity context;
	String filename;
	VideoList videos;
	// Progress Dialog
	@SuppressWarnings("unused")
	private ProgressDialog pDialog;
	private static final String APP_ID = "893530320657596";
	@SuppressWarnings("unused")
	private static final String[] PERMISSIONS = new String[] { "publish_stream" };
//	private Facebook facebook;

	private static final String TOKEN = "access_token";
	private static final String EXPIRES = "expires_in";
	private static final String KEY = "facebook-credentials";

	public static final int progress_bar_type = 0;
	String textFacebook;
	ImageLoader imgLoader;
	ProgressBar bar;

	@SuppressLint("SdCardPath")
	private static final String mSaveDirPath = "/sdcard/360/";
	public final static DecimalFormat DECIMAL_POINT = new DecimalFormat("0.0");

	static DataBaseHandler db;
	FileDownloader mDownloder;

	/**
	 * 
	 */
	@SuppressWarnings("deprecation")
	public VideoDownloadListAdapter(Activity context,
			ArrayList<DownloadTask> videoList, FileDownloader mDownloder) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.videoList = videoList;
//		facebook = new Facebook(APP_ID);
//		restoreCredentials(facebook);
		imgLoader = new ImageLoader(context);

		db = new DataBaseHandler(context);
		this.mDownloder = mDownloder;

	}

	@SuppressWarnings("unused")
	private void showToast(String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

//	@SuppressWarnings("deprecation")
//	public boolean saveCredentials(Facebook facebook) {
//		Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
//				.edit();
//		editor.putString(TOKEN, facebook.getAccessToken());
//		editor.putLong(EXPIRES, facebook.getAccessExpires());
//		return editor.commit();
//	}
//
//	@SuppressWarnings("deprecation")
//	public boolean restoreCredentials(Facebook facebook) {
//		SharedPreferences sharedPreferences = context.getSharedPreferences(KEY,
//				Context.MODE_PRIVATE);
//		facebook.setAccessToken(sharedPreferences.getString(TOKEN, null));
//		facebook.setAccessExpires(sharedPreferences.getLong(EXPIRES, 0));
//		return facebook.isSessionValid();
//	}

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

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		videos = videoList.get(position).videoList;
		final DownloadTask task = getItem(position);
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.video_row, null);

		}

		task.textCountry = (TextView) convertView
				.findViewById(R.id.textCountry);

		task.textCategory = (TextView) convertView
				.findViewById(R.id.textCategory);

		task.textDetail = (TextView) convertView.findViewById(R.id.textDetail);
		task.playImage = (ImageButton) convertView
				.findViewById(R.id.playImage);
		task.textTitle = (TextView) convertView
				.findViewById(R.id.textVideoTitle);
		task.linearLayout = (LinearLayout) convertView
				.findViewById(R.id.LinearLayout);
		task.videoImage = (ImageView) convertView.findViewById(R.id.videoImage);
		task.twiterImage = (ImageButton) convertView.findViewById(R.id.twiter);
		task.downloadIMage = (ImageButton) convertView
				.findViewById(R.id.download);

		task.facebookImage = (ImageButton) convertView
				.findViewById(R.id.facebook);
		task.download_progress = (RelativeLayout) convertView
				.findViewById(R.id.download_progress);

		task.bar = (ProgressBar) convertView.findViewById(R.id.bar);

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
								+ Formatter.formatFileSize(context,
										new File(mSaveDirPath
												+ task.storeFileName).length()));
						task.invalidate();
					}

					@Override
					public void onProgressChange(long fileSize,
							long downloadedSize) {
						task.onProgressChange(fileSize, downloadedSize);
						 task.btnStatus.setText(DECIMAL_POINT.format(task.downloadedSize
						 * 1.0f / task.fileSize * 100) + '%');

						// NetroidLog.e("---- fileSize : " + fileSize +
						// " downloadedSize : " + downloadedSize);
					}
				});
		task.btnStatus = (TextView) convertView
				.findViewById(R.id.tv_progress_circle);

		convertView.setTag(task);

		@SuppressWarnings("unused")
		int loader = R.drawable.blank_thumb;
//		imgLoader.DisplayImage(videos.getVideo_thumbnail(), task.videoImage);
		Picasso.with(context).load(videos.getVideo_thumbnail()).into(task.videoImage);
		task.textCategory.setText(videos.getCategory_name());
		task.textCountry.setText(videos.getVideo_city());
		task.bar.setVisibility(View.VISIBLE);
		task.textDetail.setText(videos.getVideo_description());
		task.download_progress.setVisibility(View.VISIBLE);
		String title = videos.getVideo_title();

		char c = (char) 0x00B0;
		title = title.replaceAll("0x00B0", c + "");
		task.textTitle.setText(videos.getVideo_id() + title);

		task.twiterImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Twitt_Sharing twitt = new Twitt_Sharing(context,
						"xwWUX7uRyXhZXPjQn4pJC4USf",
						"clzs0pJjv4wPhb7tov2yNhSKjR1BGW5LTgR8lQs7PVoldN4LM4");
				String string_img_url = "http://3.bp.blogspot.com/_Y8u09A7q7DU/S-o0pf4EqwI/AAAAAAAAFHI/PdRKv8iaq70/s1600/id-do-anything-logo.jpg";
				String string_msg = "https://chintankhetiya.wordpress.com/";
				twitt.shareToTwitter(string_msg, new File(string_img_url));
			}
		});
		task.facebookImage.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				List<Intent> targetedShareIntents = new ArrayList<Intent>();

				Intent facebookIntent = getShareIntent("com.facebook.katana",
						"subject",
						"http://www.360mea.com/demo/video_detail.php?id="
								+ videos.getVideo_id());

				if (facebookIntent != null) {
					targetedShareIntents.add(facebookIntent);

					Intent chooser = Intent.createChooser(
							targetedShareIntents.remove(0), "Delen");

					chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS,
							targetedShareIntents.toArray(new Parcelable[] {}));
					chooser.putExtra(Intent.EXTRA_TEXT,	"http://www.360mea.com/demo/video_detail.php?id="
									+ videos.getVideo_id());
					context.startActivity(chooser);
				} else {

					Bundle parameters = new Bundle();
					parameters.putString("link",
							"http://www.360mea.com/demo/video_detail.php?id="
									+ videos.getVideo_id());

				/*	facebook.dialog(context, "feed", parameters,
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

				AnimatedActivity parentActivity = (AnimatedActivity) context;
				parentActivity.startChildActivity("detailss", videoIntent);

			}
		});
		task.playImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent videoIntent = new Intent(context, VideoPlayerViewDownload.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable(WebServiceNaming.TAG_VIDEO_LINK,
						videoList.get(position).videoList);
				videoIntent.putExtras(bundle);

				context.startActivity(videoIntent);
			}
		});
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				@SuppressWarnings("unused")
				VideoList newVideoList = videoList.get(position).videoList;
				// StringTokenizer st = new StringTokenizer(
				// newVideoList.getVideo_link(), "||");
				// File file = new File("");
				// String prod_id;
				// String links = null;
				// try {
				// prod_id = st.nextToken();
				//
				// String progress = st.nextToken();
				// links = st.nextToken();
				// file = new File(links);
				// } catch (Exception e) {
				//
				// }
				// if (file.exists()) {
				//
				// Intent videoIntent = new Intent(context,
				// VideoPlayerViewDownload.class);
				// Bundle bundle = new Bundle();
				//
				// newVideoList.setVideo_link(links);
				//
				// bundle.putSerializable(WebServiceNaming.TAG_VIDEO_LINK,
				// newVideoList);
				// videoIntent.putExtras(bundle);
				//
				// context.startActivity(videoIntent);
				// } else {
				// AlertDialog.Builder builder = new AlertDialog.Builder(
				// context.getParent());
				// builder.setMessage("File not available!")
				// .setCancelable(false)
				// .setPositiveButton("OK",
				// new DialogInterface.OnClickListener() {
				// public void onClick(
				// DialogInterface dialog, int id) {
				// // do things
				// dialog.dismiss();
				// }
				// });
				// AlertDialog alert = builder.create();
				// alert.show();
				// }
				
				Intent videoIntent = new Intent(context, VideoPlayerViewDownload.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable(WebServiceNaming.TAG_VIDEO_LINK,
						videoList.get(position).videoList);
				videoIntent.putExtras(bundle);

				context.startActivity(videoIntent);

			}
		});

		@SuppressWarnings("unused")
		DataBaseHandler db = new DataBaseHandler(context);

		
		notifyDataSetChanged();
		return convertView;
	}

	@SuppressLint("DefaultLocale")
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

	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private String checkVideoProgres(int reason, int status, RelativeLayout bar) {
		// TODO Auto-generated method stub
		bar.setVisibility(View.VISIBLE);
		String statusText = "";
		String reasonText = "";
		switch (status) {
		case DownloadManager.STATUS_FAILED:
			statusText = "STATUS_FAILED";
			switch (reason) {

			case DownloadManager.ERROR_CANNOT_RESUME:
				reasonText = "ERROR";

				break;
			case DownloadManager.ERROR_DEVICE_NOT_FOUND:
				reasonText = "ERROR";

				break;
			case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
				reasonText = "ERROR";

				break;
			case DownloadManager.ERROR_FILE_ERROR:
				reasonText = "ERROR";

				break;
			case DownloadManager.ERROR_HTTP_DATA_ERROR:
				reasonText = "ERROR";

				break;
			case DownloadManager.ERROR_INSUFFICIENT_SPACE:
				reasonText = "INSUFFICIENT SPACE";

				break;
			case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
				reasonText = "ERROR";

				break;
			case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
				reasonText = "ERROR";

				break;
			case DownloadManager.ERROR_UNKNOWN:
				reasonText = "ERROR";

				break;
			}
			break;
		case DownloadManager.STATUS_PAUSED:
			statusText = "PAUSED";
			switch (reason) {
			case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
				reasonText = "QUEUED";

				break;
			case DownloadManager.PAUSED_UNKNOWN:
				reasonText = "PAUSED";

				break;
			case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
				reasonText = "PAUSED";

				break;
			case DownloadManager.PAUSED_WAITING_TO_RETRY:
				reasonText = "PAUSED";

				break;
			}
			break;
		case DownloadManager.STATUS_PENDING:
			statusText = "PENDING";

			break;
		case DownloadManager.STATUS_RUNNING:
			statusText = "RUNNING";

			break;
		case DownloadManager.STATUS_SUCCESSFUL:
			bar.setVisibility(View.INVISIBLE);
			statusText = "STATUS_SUCCESSFUL";
			reasonText = "Filename:\n" + filename;

			break;
		}
		return statusText;
	}

	
}
