/**
 * 
 */
package com.creativeinnovations.mea;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.creativeinnovations.mea.network.SoapServicePath;
import com.degree.hitaishin.connection.WebServiceConnection;
import com.degree.hitaishin.connection.WebServiceNaming;
import com.degree.hitaishin.model.Catgory;
import com.degree.hitaishin.model.Cities;

/**
 * @author sachin
 * 
 */
public class SearchActivity extends Activity {
	NoDefaultSpinner spinnerCategory, spinnerCity, spinnerDetail;
	EditText editKeyword;
	Button btnSearch;
	List<Catgory> catList = new ArrayList<Catgory>();
	List<Cities> cityList = new ArrayList<Cities>();
	List<String> catStringlist = new ArrayList<String>();
	List<String> sub_catStringlist = new ArrayList<String>();
	ImageButton btnBack;
	List<String> citylist = new ArrayList<String>();
	List<String> subcatlist_id = new ArrayList<String>();
	int cat_ids[] = { 1, 2, 3, 4, 14, 18 };

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	
		View viewToLoad = LayoutInflater.from(this.getParent()).inflate(
				R.layout.search_actvity, null);
		this.setContentView(viewToLoad);
		
		spinnerCategory = (NoDefaultSpinner) viewToLoad.findViewById(R.id.spinnerCategory);
		spinnerCity = (NoDefaultSpinner) viewToLoad.findViewById(R.id.spinnerCity);
		spinnerDetail = (NoDefaultSpinner) viewToLoad.findViewById(R.id.spinnerDetail);
		
		editKeyword = (EditText) viewToLoad.findViewById(R.id.editKeyword);
		editKeyword.setImeOptions(EditorInfo.IME_ACTION_DONE);
		btnSearch = (Button) viewToLoad.findViewById(R.id.btnSearch);

	
		btnBack = (ImageButton) findViewById(R.id.btn_Back);
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
//				hideKeyboard();

			}
		});
	
		new SearchTaskRunner().execute();
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
				SearchActivity.this.getParent(), R.layout.simple_spinner_item,
				sub_catStringlist);
		dataAdapter.setDropDownViewResource(R.layout.simple_spinner_new);

		spinnerDetail.setAdapter(dataAdapter);
		spinnerCategory.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Catgory cat = catList.get(position);

				try {
					subcatlist_id.clear();
					sub_catStringlist.clear();
					Log.e("Eroor", cat.getSubcategory());
					if (cat.getSubcategory().trim().equals("null") || cat.getSubcategory().equals(null)) {
						
						if (sub_catStringlist.size() == 0) {
							sub_catStringlist.add("No Detail Found");
							subcatlist_id.add("0");
							ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
									SearchActivity.this.getParent(),
									R.layout.simple_spinner_item,
									sub_catStringlist);
							dataAdapter
									.setDropDownViewResource(R.layout.simple_spinner_new);

							spinnerDetail.setAdapter(dataAdapter);
							spinnerDetail.setPrompt("Select Detail");
							spinnerDetail.setSelected(false);

						}
						
					} else {
						
						JSONArray json = new JSONArray(cat.getSubcategory());
						
						for (int i = 0; i < json.length(); i++) {
							JSONObject subcategoryObject = json.getJSONObject(i);
							
							String sub_cat_name = subcategoryObject.getString(WebServiceNaming.TAG_SUB_CATEGORY_NAME);
							String subcatid = subcategoryObject.getString(WebServiceNaming.TAG_SUB_CATEGORY_ID);
							
							sub_catStringlist.add(sub_cat_name);
							subcatlist_id.add(subcatid);
						}

						if (sub_catStringlist.size() != 0) {
							ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
									SearchActivity.this.getParent(),
									R.layout.simple_spinner_item,
									sub_catStringlist );
							dataAdapter
									.setDropDownViewResource(R.layout.simple_spinner_new);

							spinnerDetail.setAdapter(dataAdapter);
						}
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(
						editKeyword.getApplicationWindowToken(), 0);

				String cat_id;
				if (spinnerCategory.getSelectedItemPosition() == -1) {
					cat_id = "0";
				} else {
					cat_id = catList.get(spinnerCategory.getSelectedItemPosition()).getCat_id();
				}

				String city_id;
				if (spinnerCity.getSelectedItemPosition() == -1) {
					city_id = "0";
				} else {
					city_id = cityList.get(spinnerCity.getSelectedItemPosition()).getCity();

				}

				String sub_Cat_id;
				if (spinnerDetail.getSelectedItemPosition() == -1) {
					sub_Cat_id = "0";
				} else {
					sub_Cat_id = subcatlist_id.get(spinnerDetail.getSelectedItemPosition());
				}

				Intent frequentMessages = new Intent( SearchActivity.this , SearchVideoListActivity.class );
				frequentMessages.putExtra(WebServiceNaming.TAG_CATEGORY_ID,cat_id);
				frequentMessages.putExtra(WebServiceNaming.TAG_CITY, city_id);
				frequentMessages.putExtra(WebServiceNaming.TAG_SUB_CATEGORY_ID,sub_Cat_id);
				frequentMessages.putExtra(WebServiceNaming.TAG_KEYWORD,editKeyword.getText().toString());

				AnimatedActivity parentActivity = (AnimatedActivity) SearchActivity.this.getParent();
				parentActivity.startChildActivity("SerchActivity",frequentMessages);

			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		AnimatedActivity pActivity = (AnimatedActivity) getParent();

		pActivity.finishFromChild(SearchActivity.this);

	}



	class SearchTaskRunner extends AsyncTask<String, String, String> {

		JSONObject jsonResult = null;
		ProgressDialog pDialog;

		@Override
		protected String doInBackground(String... params) {

			try {

				jsonResult = WebServiceConnection.performPost("",SoapServicePath.CATEGORY_SEARCH ); //TAG_URL_FOR_Commo
				
			/*	jsonResult = WebServiceConnection.performPost("",
						WebServiceNaming.TAG_URL
						+ WebServiceNaming.TAG_CATEGORY_LIST);*/
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				if (jsonResult != null) {
					catStringlist.clear();
					catList.clear();
					cityList.clear();
					citylist.clear();

					JSONObject jsonResultList = jsonResult.getJSONObject(WebServiceNaming.TAG_RESULT);
					JSONArray objectCatgory = jsonResultList.getJSONArray(WebServiceNaming.TAG_CATEGORY);
					JSONArray cities = jsonResultList.getJSONArray(WebServiceNaming.TAG_CITIES);
					
					for (int i = 0; i < objectCatgory.length(); i++) {
						JSONObject jsonCategory = objectCatgory.getJSONObject(i);

						String CategoryName = jsonCategory.getString(WebServiceNaming.TAG_CATEGORY_NAME);
						String category_id = jsonCategory.getString(WebServiceNaming.TAG_CATEGORY_ID);
						String sucategory = jsonCategory.getString(WebServiceNaming.TAG_SUB_CATEGORY);
						Catgory catgory = new Catgory(category_id,CategoryName, sucategory);
						catList.add(catgory);

						catStringlist.add(CategoryName);
					}
					for (int j = 0; j < cities.length(); j++) {
						JSONObject jsonCities = cities.getJSONObject(j);
						String city = jsonCities
								.getString(WebServiceNaming.TAG_CITY);
						String city_id = jsonCities
								.getString(WebServiceNaming.TAG_CITY_ID);

						Cities citiesObject = new Cities(city, city_id);
						cityList.add(citiesObject);
						citylist.add(city);
					}

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (pDialog.isShowing()) {
				pDialog.dismiss();
			}
			
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
			(SearchActivity.this.getParent(),R.layout.simple_spinner_item, catStringlist);
			dataAdapter.setDropDownViewResource(R.layout.simple_spinner_new);
			spinnerCategory.setAdapter(dataAdapter);
			
			ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>
			(SearchActivity.this.getParent(),R.layout.simple_spinner_item, citylist);
			dataAdapter2.setDropDownViewResource(R.layout.simple_spinner_new);
			spinnerCity.setAdapter(dataAdapter2);

		}

		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(SearchActivity.this.getParent());
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			if (pDialog != null) {
				pDialog.show();
			}

		}

		@Override
		protected void onProgressUpdate(String... text) {

		}
	}

}
