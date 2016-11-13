package com.degree.database;

import java.io.File;
import java.util.ArrayList;
import java.util.StringTokenizer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.degree.hitaishin.model.VideoList;

public class DataBaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "360degree";

	public static final String TABLE_VIDEO_TABLE = "degreetable";

	public static final String TAG_VIDEO_ID = "video_id";
	public static final String TAG_CATEGORY_NAME = "category_name";
	public static final String TAG_VIDEO_CITY = "video_city";
	public static final String TAG_VIDEO_TITLE = "video_title";
	public static final String TAG_VIDEO_DESCRIPTION = "video_description";
	public static final String TAG_VIDEO_LINK = "video_link";
	public static final String TAG_UPLOAD_DATE = "upload_date";
	public static final String TAG_VIDEO_THUMBNAIL = "video_icon";
	public static final String TAG_SUB_CATEGORY = "subcategory_namess";
	public static final String TAG_META_DATA = "meta_data";
	public static final String TAG_WEBSITE_LINK = "website_link";

	public DataBaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {

		String CREATE_360 = "CREATE TABLE " + TABLE_VIDEO_TABLE + "("
				+ TAG_VIDEO_ID + " TEXT," + TAG_CATEGORY_NAME + " TEXT,"
				+ TAG_VIDEO_CITY + " TEXT," + TAG_VIDEO_TITLE + " TEXT,"
				+ TAG_VIDEO_DESCRIPTION + " TEXT," + TAG_VIDEO_LINK + " TEXT,"
				+ TAG_UPLOAD_DATE + " TEXT," + TAG_VIDEO_THUMBNAIL + " TEXT,"
				+ TAG_SUB_CATEGORY + " TEXT," + TAG_META_DATA + " TEXT,"
				+ TAG_WEBSITE_LINK + " TEXT" + ")";

		db.execSQL(CREATE_360);

	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEO_TABLE);

		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	public void addVIDEO(VideoList e) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TAG_VIDEO_ID, e.getVideo_id());
		values.put(TAG_CATEGORY_NAME, e.getCategory_name());
		values.put(TAG_VIDEO_CITY, e.getVideo_city());
		values.put(TAG_VIDEO_TITLE, e.getVideo_title());
		values.put(TAG_VIDEO_DESCRIPTION, e.getVideo_description());
		values.put(TAG_VIDEO_LINK, e.getVideo_link());
		values.put(TAG_UPLOAD_DATE, e.getUpload_date());
		values.put(TAG_VIDEO_THUMBNAIL, e.getVideo_thumbnail());
		values.put(TAG_SUB_CATEGORY, e.getSubcategory_name());
		values.put(TAG_META_DATA, e.getMeta_data());
		values.put(TAG_WEBSITE_LINK, e.getWebsiteLink());
		db.insert(TABLE_VIDEO_TABLE, null, values);
		db.close();

	}

	public void deleteVIDEO(VideoList e) {
		SQLiteDatabase db = this.getWritableDatabase();
		StringTokenizer st = new StringTokenizer(e.getVideo_link(), "||");

		if (st.hasMoreTokens()) {
			st.nextToken();
			if (st.hasMoreTokens()) {
				st.nextToken();
				if (st.hasMoreTokens()) {
					File f = new File(st.nextToken());
					if (f.exists()) {
						f.delete();
					}
				}
			}
		}

		db.delete(TABLE_VIDEO_TABLE, TAG_VIDEO_ID + " = ?",
				new String[] { String.valueOf(e.getVideo_id()) });
		db.close();
	}

	public void updateVideoRecord(VideoList e) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TAG_VIDEO_ID, e.getVideo_id());
		values.put(TAG_CATEGORY_NAME, e.getCategory_name());
		values.put(TAG_VIDEO_CITY, e.getVideo_city());
		values.put(TAG_VIDEO_TITLE, e.getVideo_title());
		values.put(TAG_VIDEO_DESCRIPTION, e.getVideo_description());
		values.put(TAG_VIDEO_LINK, e.getVideo_link());
		values.put(TAG_UPLOAD_DATE, e.getUpload_date());
		values.put(TAG_VIDEO_THUMBNAIL, e.getVideo_thumbnail());
		values.put(TAG_SUB_CATEGORY, e.getSubcategory_name());
		values.put(TAG_META_DATA, e.getMeta_data());
		values.put(TAG_WEBSITE_LINK, e.getWebsiteLink());

		db.update(TABLE_VIDEO_TABLE, values, TAG_VIDEO_ID + " =?",
				new String[] { String.valueOf(e.getVideo_id()) });
		db.close();
	}

	public ArrayList<VideoList> getAllVideo() {
		ArrayList<VideoList> ItemList = new ArrayList<VideoList>();
		try {

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_VIDEO_TABLE;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do 
				{
					VideoList ele = new VideoList();
					ele.setVideo_id(cursor.getString(0));
					ele.setCategory_name(cursor.getString(1));
					ele.setVideo_city(cursor.getString(2));
					ele.setVideo_title(cursor.getString(3));
					ele.setVideo_description(cursor.getString(4));
					ele.setVideo_link(cursor.getString(5));
					ele.setUpload_date(cursor.getString(6));
					ele.setVideo_thumbnail(cursor.getString(7));
					ele.setSubcategory_name(cursor.getString(8));
					ele.setMeta_data(cursor.getString(9));
					ele.setWebsiteLink(cursor.getString(10));
					ItemList.add(ele);
				}
				while (cursor.moveToNext());
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return ItemList;

	}
	
	public ArrayList<VideoList> getAllVideoBySearch(String _data) {
		ArrayList<VideoList> ItemList = new ArrayList<VideoList>();
		try {

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_VIDEO_TABLE + " WHERE " + TAG_VIDEO_TITLE + " LIKE '%"+_data+"%'";

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do 
				{
					VideoList ele = new VideoList();
					ele.setVideo_id(cursor.getString(0));
					ele.setCategory_name(cursor.getString(1));
					ele.setVideo_city(cursor.getString(2));
					ele.setVideo_title(cursor.getString(3));
					ele.setVideo_description(cursor.getString(4));
					ele.setVideo_link(cursor.getString(5));
					ele.setUpload_date(cursor.getString(6));
					ele.setVideo_thumbnail(cursor.getString(7));
					ele.setSubcategory_name(cursor.getString(8));
					ele.setMeta_data(cursor.getString(9));
					ele.setWebsiteLink(cursor.getString(10));
					ItemList.add(ele);
				}
				while (cursor.moveToNext());
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return ItemList;

	}

}
