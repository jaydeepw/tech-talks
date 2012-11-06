package com.codelab.moviefindercheatsheet;

import java.util.HashMap;

import com.codelab.restapi.ServerConnectionManager;

import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MovieSearch extends Activity {
	
	private static final String TAG = "MovieSearch";
	
	private Button mSearchButton;
	private EditText mSearchEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_search);
        
        mSearchEditText = (EditText) findViewById(R.id.search_box);
        initSearchButton();
    }

	private void initSearchButton() {
		mSearchButton = (Button) findViewById(R.id.search_button);

		mSearchButton.setOnClickListener( new View.OnClickListener() {
			
			public void onClick(View v) {
				String movieName = mSearchEditText.getText().toString();
				searchMovie(movieName);
			}
		});
	}
	
	private void searchMovie(final String movieName) {
		
		Thread movieFinderThread = new Thread(new Runnable() {
			
			public void run() {
				ServerConnectionManager servConnMgr = new ServerConnectionManager();
				HashMap<String, String> outPut = new HashMap<String, String>();
				
				servConnMgr.searchMovie(movieName, outPut);
				
				if( outPut.containsKey("title") ) Log.i(TAG, "#searchMovie title: " + outPut.get("title") );
				if( outPut.containsKey("year") ) Log.i(TAG, "#searchMovie year: " + outPut.get("year") );
				if( outPut.containsKey("runtime") ) Log.i(TAG, "#searchMovie runtime: " + outPut.get("runtime") );
				if( outPut.containsKey("synopsis") ) Log.i(TAG, "#searchMovie synopsis: " + outPut.get("synopsis") );
			}
		});
		
		movieFinderThread.start();

	}
}