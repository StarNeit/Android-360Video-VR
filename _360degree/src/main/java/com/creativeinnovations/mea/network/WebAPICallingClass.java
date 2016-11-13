package com.creativeinnovations.mea.network;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;



public class WebAPICallingClass {

	Context _ctx;
	String _url;
	
	public WebAPICallingClass() {
		// TODO Auto-generated constructor stub
	}
	
	public WebAPICallingClass(Context _context) {
		// TODO Auto-generated constructor stub
	}


    public JSONObject performPostCallDirect(HashMap<String, String> postDataParams) {
        _url = SoapServicePath.DOMAIN_NAME;
        URL url;
        String response = "";
        JSONObject _results =  null;

        try {
            url = new URL(SoapServicePath.DOMAIN_NAME);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }

                if(response.trim().length() > 0)
                {
                    _results = new JSONObject(response);
                }
            }
            else {
                _results = null;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(_results != null)
        {
            Log.i("Anish",_results.toString());
            return  _results;
        }
        else
        {              
            return _results;
        }
    }


    public JSONObject performPostCallDirect(HashMap<String, String> postDataParams,String _common) {
        _url = SoapServicePath.DOMAIN_NAME_Common;
        URL url;
        String response = "";
        JSONObject _results =  null;

        try {
            url = new URL(SoapServicePath.DOMAIN_NAME_Common);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }

                if(response.trim().length() > 0)
                {
                    _results = new JSONObject(response);
                }
            }
            else {
                _results = null;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(_results != null)
        {
            Log.i("Anish",_results.toString());
            return  _results;
        }
        else
        {              
            return _results;
        }
    }

    
	
	private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        Log.i("Anish", _url+"?"+result.toString());
        return result.toString();
    }
}