/**
 * 
 */
package com.degree.hitaishin.UI;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.creativeinnovations.mea.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

/**
 * @author Nikhil
 * 
 */
public class ImageAdapter extends BaseAdapter {
	
	ArrayList<HashMap<String, String>> videoList;
	Activity context;
	ViewHolder holders;
	private int lastPosition = -1;
	private ImageLoader imageLoader;

	/**
	 * 
	 */
	public ImageAdapter(Activity context, ArrayList<HashMap<String, String>> videoList) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.videoList = videoList;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return videoList.size();
	}

	@Override
	public HashMap<String, String> getItem(int position) {
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

		final HashMap<String, String> _catMapData = videoList.get(position);

		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate( R.layout.grid_row , null);
			holders = new ViewHolder();
			holders.videoImage = (CircleImageView) convertView
					.findViewById(R.id.image);
		
			convertView.setTag(holders);

		} else {
			holders = (ViewHolder) convertView.getTag();

		}

		if (_catMapData.get("image")!=null && _catMapData.get("image").length()>0) {
			Picasso.with(context).load(_catMapData.get("image")).into(holders.videoImage);	
 
		}
		
			
//		Animation animation = AnimationUtils.loadAnimation(this.context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
//		convertView.startAnimation(animation);
//		lastPosition  = position;	
	
		return convertView;
	}


	

	public static class ViewHolder {
		
		CircleImageView videoImage;
		

	}




}
