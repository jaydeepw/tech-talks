package com.codelab.restapi;

import java.net.URLEncoder;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.codelab.moviefindercheatsheet.Constants;

import android.util.Base64;
import android.util.Log;

public class ServerConnectionManager {
	
	private static final String TAG = "ServerConnectionManager";
	
	public boolean hitApi(){
		try {
		    HttpClient client = new DefaultHttpClient();  
		    //String getURL = "http://www.google.com";
		    String getURL = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey=" + Constants.MOVIE_FINDER_API_KEY + "&q=Toy+Story+3&page_limit=1";
		    HttpGet get = new HttpGet( getURL );
		    HttpResponse responseGet = client.execute(get);  
		    HttpEntity resEntityGet = responseGet.getEntity();  
		    if (resEntityGet != null) {  
		        // do something with the response
		        String response = EntityUtils.toString(resEntityGet);
		        Log.v(TAG, response);
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		    Log.e(TAG, "#hitApi Exception msg: " + e.getMessage() );
		    return false;
		}
		
		return true;
	}
	
	public boolean searchMovie(String movieName, HashMap<String, String> outPut){
		
		String response = null;
		
		try {
		    HttpClient client = new DefaultHttpClient();
		    String getURL = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey=" + Constants.MOVIE_FINDER_API_KEY + "&q=" + URLEncoder.encode(movieName) + "&page_limit=1";
		    HttpGet get = new HttpGet( getURL );
		    HttpResponse responseGet = client.execute(get);  
		    HttpEntity resEntityGet = responseGet.getEntity();  
		    if (resEntityGet != null) {  
		        // do something with the response
		         response = EntityUtils.toString(resEntityGet);
		        Log.v(TAG, response);
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		    Log.e(TAG, "#searchMovie Exception msg: " + e.getMessage() );
		    return false;
		}
		
		// parse the response
		JSONObject respJSONObj = null;
		JSONArray movieArr = null;
		try {
			 respJSONObj = new JSONObject(response);
			 String error = respJSONObj.has("error") ? respJSONObj.getString("error") : null;
			 
			if( error != null ) {outPut.put("error", error); return false;};
		} catch (JSONException e) {
			e.printStackTrace();
			Log.e(TAG, "#searchMovie JSONException while parsing response from the server " + e.getMessage() );
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "#searchMovie Exception while parsing response from the server " + e.getMessage() );
		}
		
		try {
			movieArr = respJSONObj.has("movies") ? respJSONObj.getJSONArray("movies") : null;
			
			// consider only the first movie result that has been returned.
			// Skip others.
			if(movieArr != null && movieArr.length() > 0){
				JSONObject movieInfo = (JSONObject) movieArr.get(0);
				// Using JSON keys this way is real bad.
				String title = movieInfo.has("title") ? movieInfo.getString("title") : null;
				String year = movieInfo.has("year") ? movieInfo.getString("year") : null;
				String runtime = movieInfo.has("runtime") ? movieInfo.getString("runtime") : null;
				String synopsis = movieInfo.has("synopsis") ? movieInfo.getString("synopsis") : null;

				JSONObject posters = movieInfo.has("posters") ? movieInfo.getJSONObject("posters") : null;
				String imageUrl = posters.has("thumbnail") ? posters.getString("thumbnail") : null;
				
				if( title != null ) outPut.put("title", title);
				if( year != null ) outPut.put("year", year);
				if( runtime != null ) outPut.put("runtime", runtime);
				if( synopsis != null ) outPut.put("synopsis", synopsis);
				if( imageUrl != null ) outPut.put("image_url", imageUrl);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return true;
	}

}