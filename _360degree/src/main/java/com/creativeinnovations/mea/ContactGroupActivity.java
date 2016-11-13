package com.creativeinnovations.mea;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class ContactGroupActivity extends AnimatedActivity {
	public static ContactGroupActivity HomeGroupStack;
	List<RunningTaskInfo> taskInfo;
	public static Activity act;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		HomeGroupStack = ContactGroupActivity.this;

		startChildActivity("CotactGroupActivity", new Intent(this,
				ContactActivity.class));

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
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
//		 ContactActivity.uiHelper.onActivityResult(requestCode, resultCode, data, null);
	}

}