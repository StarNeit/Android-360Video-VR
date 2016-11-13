/**
 * 
 */
package com.degree.adpater;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore.Images;
import android.util.Log;
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
import com.creativeinnovations.mea.R;
import com.creativeinnovations.mea.VideoDetailActivity;
import com.creativeinnovations.mea.SimpleStreamPlayerActivity;
import com.creativeinnovations.mea.VideoPlayerView;
import com.creativeinnovations.mea.WebViewActivity;
import com.creativeinnovations.mea.network.CommonUtility;
import com.degree.Twitter_code.Twitt_Sharing;
import com.degree.hitaishin.connection.ImageLoader;
import com.degree.hitaishin.connection.WebServiceNaming;
import com.degree.hitaishin.model.VideoList;
//import com.facebook.android.DialogError;
//import com.facebook.android.Facebook;
//import com.facebook.android.Facebook.DialogListener;
//import com.facebook.android.FacebookError;
import com.squareup.picasso.Picasso;

@SuppressWarnings("deprecation")
@SuppressLint({ "NewApi", "InflateParams", "DefaultLocale" })
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class VideoListAdapter extends BaseAdapter {
	ArrayList<VideoList> videoList;
	Activity context;
	String filename;
	VideoList videos;
	String URI;
	// Progress Dialog

//	private static final String APP_ID = "893530320657596";

//	private Facebook facebook;

	public static final int progress_bar_type = 0;
	String textFacebook;
	int pos;
	ViewHolder holders;
	ImageLoader imgLoader;
	int progress = 0;

	/**
	 * 
	 */
	public VideoListAdapter(Activity context, ArrayList<VideoList> videoList,
			int pos) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.videoList = videoList;
//		facebook = new Facebook(APP_ID);
		imgLoader = new ImageLoader(context);
		this.pos = pos;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return videoList.size();
	}

	@Override
	public VideoList getItem(int position) {
		// TODO Auto-generated method stub
		return videoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final VideoList video = videoList.get(position);
		videos = video;

		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.video_row, null);
			holders = new ViewHolder();
			holders.textCountry = (TextView) convertView
					.findViewById(R.id.textCountry);

			holders.textCategory = (TextView) convertView
					.findViewById(R.id.textCategory);

			holders.textDetail = (TextView) convertView
					.findViewById(R.id.textDetail);

			holders.textTitle = (TextView) convertView
					.findViewById(R.id.textVideoTitle);
			
			holders.textTitl1 = (TextView) convertView
					.findViewById(R.id.textVideoTitlea);

			holders.videoImage = (ImageView) convertView
					.findViewById(R.id.videoImage);
			holders.twiterImage = (ImageButton) convertView
					.findViewById(R.id.twiter);
			holders.downloadIMage = (ImageButton) convertView
					.findViewById(R.id.download);
			holders.facebookImage = (ImageButton) convertView
					.findViewById(R.id.facebook);
			holders.playImage = (ImageButton) convertView
					.findViewById(R.id.playImage);
			holders.bar = (ProgressBar) convertView.findViewById(R.id.bar);
			holders.linearLayout = (LinearLayout) convertView
					.findViewById(R.id.LinearLayout);
			holders.download_progress = (RelativeLayout) convertView
					.findViewById(R.id.download_progress);
			holders.tv_progress_circle = (TextView) convertView
					.findViewById(R.id.tv_progress_circle);

			convertView.setTag(holders);

		} else {
			holders = (ViewHolder) convertView.getTag();

		}

//		imgLoader.DisplayImage(video.getVideo_thumbnail(), holders.videoImage);
//		Picasso.with(context)
//		  .load(video.getVideo_thumbnail())
//		  .resize(100, 100)
//		  .centerCrop()
//		  .into(holders.videoImage);
		Picasso.with(context).load(video.getVideo_thumbnail()).into(holders.videoImage);
		holders.textCategory.setText(video.getCategory_name());
		holders.textCountry.setText(video.getVideo_city());

		holders.linearLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent videoIntent = new Intent( context , VideoDetailActivity.class );
				Bundle bundle = new Bundle();
				bundle.putSerializable( WebServiceNaming.TAG_VIDEO_LINK,
						videoList.get(position) );
				videoIntent.putExtra( "position", pos );
				videoIntent.putExtras( bundle );

				AnimatedActivity parentActivity = (AnimatedActivity) context;
				parentActivity.startChildActivity("detail", videoIntent);

			}
		});

		holders.textDetail.setText(video.getVideo_description());

		String title = video.getVideo_title();
		char c = (char) 0x00B0;
		title = title.replaceAll("0x00B0", c + "");

		holders.textTitle.setText(title);
		holders.textTitl1.setText(title);
		holders.playImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
	
				if(videoList.get(position).getVideoType().equals("html"))
				{
					CommonUtility.setGlobalString(context, "HTML", videoList.get(position).getVideo_link());
					Intent videoIntent = new Intent(context, WebViewActivity.class);
					context.startActivity(videoIntent);	
					
				}
				else
				{
					Intent videoIntent = new Intent( context, VideoPlayerView.class );
					Bundle bundle = new Bundle();
					bundle.putSerializable(WebServiceNaming.TAG_VIDEO_LINK,
							videoList.get(position));
					videoIntent.putExtras(bundle);
					context.startActivity(videoIntent);					
				}
			}
		});

		holders.twiterImage.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("unused")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				List<Intent> targetedShareIntents = new ArrayList<Intent>();

				Intent facebookIntent = getShareIntent(
						"com.twitter.android",
						"View this amazing 360 Degrees Video.",
						"http://www.360mea.com/video-detail?id="
								+ video.getVideo_id());

				if (facebookIntent != null) {

					holders.videoImage.buildDrawingCache(); 
					Bitmap bm = holders.videoImage.getDrawingCache();
					OutputStream fOut = null;
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
						Uri.fromFile(sdImageMainDirectory);
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
							video.getVideo_title()
									+ "\n\n"
									+ "http://www.360mea.com/video-detail?id="
									+ video.getVideo_id());

				} else {
					
				/*	ConfigurationBuilder builder = new ConfigurationBuilder();
					builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
					builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
					
					// Access Token 
					String access_token = mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, "");
					// Access Token Secret
					String access_token_secret = mSharedPreferences.getString(PREF_KEY_OAUTH_SECRET, "");
					
					AccessToken accessToken = new AccessToken(access_token, access_token_secret);
					Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);
					
					// Update status
					twitter4j.Status response = twitter.updateStatus(status);*/

					Twitt_Sharing twitt = new Twitt_Sharing(context,
							"xwWUX7uRyXhZXPjQn4pJC4USf",
							"clzs0pJjv4wPhb7tov2yNhSKjR1BGW5LTgR8lQs7PVoldN4LM4");

					twitt.shareToTwitter("View this amazing 360 Degrees Video."+"http://www.360mea.com/video-detail?id="
							+ video.getVideo_id(), new File(
							"file:///android_asset/back.png"));
				}
			}
		});
		
		holders.facebookImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				List<Intent> targetedShareIntents = new ArrayList<Intent>();

				Intent facebookIntent = getShareIntent("com.facebook.katana",
						"subject","http://www.360mea.com/video-detail?id="+ video.getVideo_id());

				if (facebookIntent != null) {
					targetedShareIntents.add(facebookIntent);

					Intent chooser = Intent.createChooser(
							targetedShareIntents.remove(0), "Delen");

					chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS,
							targetedShareIntents.toArray(new Parcelable[] {}));
					chooser.putExtra(Intent.EXTRA_TEXT,
							"http://www.360mea.com/video-detail?id="
									+ video.getVideo_id());
					context.startActivity(chooser);
				} else {

					textFacebook = video.getMeta_data();
					Bundle parameters = new Bundle();
					parameters.putString("text", "Test 360");
					parameters.putString("link",
							"http://www.360mea.com/video-detail?id="
									+ video.getVideo_id());

			/*		facebook.dialog(context, "feed", parameters,
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
				// share();
			}
		});
		
		holders.downloadIMage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				 if(appInstalledOrNot("com.whatsapp"))
				 {
					 Intent sendIntent = new Intent();
					    sendIntent.setAction(Intent.ACTION_SEND);
					    sendIntent.putExtra(Intent.EXTRA_TEXT, "View this amazing 360 Degrees Video http://www.360mea.com/video-detail?id="+video.getVideo_id());
					    sendIntent.setType("text/plain");
					sendIntent.setPackage("com.whatsapp");
					context.startActivity(sendIntent);
				 }
				 else
				 {
					 Toast.makeText(context, "Whats app not found.", Toast.LENGTH_LONG).show();
				 }
				/*
				// TODO Auto-generated method stub

				DataBaseHandler db = new DataBaseHandler(context);
				ArrayList<VideoList> vlist = db.getAllVideo();

				boolean flag = true;
				for (int i = 0; i < vlist.size(); i++) {
					if (vlist.get(i).getVideo_id().equals(video.getVideo_id())) {
						flag = false;
					}
				}
				if (flag) {

					DownloadActivity.flag++;

					// videos.setVideo_link(URI);
					db.addVIDEO(videoList.get(position));

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

				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							context.getParent());
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
			*/
				 }
		});
		
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// holders.bar.setVisibility(View.VISIBLE);
				Log.i("nik", videoList.get(position).getVideoType());
				if(videoList.get(position).getVideoType().equals("html"))
				{
					CommonUtility.setGlobalString(context, "HTML", videoList.get(position).getVideo_link());
					Intent videoIntent = new Intent(context, WebViewActivity.class);
					context.startActivity(videoIntent);	
					
				}
				else
				{
					Intent videoIntent = new Intent(context, VideoPlayerView.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable(WebServiceNaming.TAG_VIDEO_LINK,
							videoList.get(position));
					videoIntent.putExtras(bundle);
					context.startActivity(videoIntent);					
				}

			}
		});

		return convertView;
	}

	private boolean appInstalledOrNot(String uri) {
	        PackageManager pm = context.getPackageManager();
	        boolean app_installed;
	        try {
	            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
	            app_installed = true;
	        }
	        catch (PackageManager.NameNotFoundException e) {
	            app_installed = false;
	        }
	        return app_installed;
	    }
	
	public Uri getImageUri(Context inContext, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = Images.Media.insertImage(inContext.getContentResolver(),
				inImage, "Title", null);
		return Uri.parse(path);
	}

	public static class ViewHolder {
		TextView textTitle,textTitl1, textCategory, textCountry, textDownload,
				textDetail, tv_progress_circle;
		ImageView videoImage;
		ImageButton facebookImage, twiterImage, downloadIMage, playImage;
		ProgressBar bar;
		LinearLayout linearLayout;
		RelativeLayout download_progress;

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
				if (info.activityInfo.packageName.toLowerCase().equals(type)
						|| info.activityInfo.name.toLowerCase().equals(type)) {

					share.putExtra(Intent.EXTRA_SUBJECT, subject);
					share.putExtra(Intent.EXTRA_TEXT, text);
					share.putExtra(
							"com.facebook.platform.extra.APPLICATION_ID",
							"893530320657596");
					share.putExtra("com.facebook.platform.extra.ACCESS_TOKEN",
							"d32597ddbb97a79414dc98483f3d6156");

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

	public String download(String url) {
		try {
			File externalStorageDirectory = Environment
					.getExternalStorageDirectory();
			URL urlTmp = new URL(url);
			String filename = urlTmp.getFile();
			filename = externalStorageDirectory + "file"
					+ filename.substring(filename.lastIndexOf("/") + 1);
			Bitmap bitmap = BitmapFactory.decodeStream(urlTmp.openStream());
			@SuppressWarnings("resource")
			FileOutputStream fileOutputStream = new FileOutputStream(filename);
			if (bitmap != null) {
				bitmap.compress(getFormat(filename), 50, fileOutputStream);
				// Log.d(TAG, "Saved image " + filename);
				return filename;
			}
		} catch (MalformedURLException e) {
			// Log.w(TAG, "Could not save image with url: " + url, e);
		} catch (IOException e) {
			// Log.w(TAG, "Could not save image with url: " + url, e);
		}
		// /Log.d(TAG, "Failed to save image " + filename);
		return null;
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
