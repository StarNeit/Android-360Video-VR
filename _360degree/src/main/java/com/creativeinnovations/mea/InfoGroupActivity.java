package com.creativeinnovations.mea;

import java.util.List;

import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class InfoGroupActivity extends AnimatedActivity {
	public static InfoGroupActivity HomeGroupStack;
	List<RunningTaskInfo> taskInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		@SuppressWarnings("unused")
		Intent serviceIntent = new Intent();
		//serviceIntent.setAction("com.google.android.youtube.api.service.START");
		//startService(serviceIntent);
		HomeGroupStack = InfoGroupActivity.this;

		startChildActivity("InfoGroupActivity", new Intent(this,
				InfoActivity.class));


	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		new AlertDialog.Builder(this)
				.setIcon(R.drawable.ic_launcher)
				.setTitle("Close App")
				.setMessage("Are you sure you want to close app?")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								finishActivityFromChild(getParent(), 1);
								finishActivity(1);
								finish();

							}

						}).setNegativeButton("No", null).show();

	}

}