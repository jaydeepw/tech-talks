package com.codelab.restapi;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.codelab.moviefindercheatsheet.Constants;

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

}
