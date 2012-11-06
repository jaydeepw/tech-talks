package com.codelab.moviefindercheatsheet;

import java.util.HashMap;

import com.codelab.restapi.ServerConnectionManager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MovieSearch extends Activity {
	
	private static final String TAG = "MovieSearch";
	
	private Button mSearchButton;
	private EditText mSearchEditText;
	
	private static final int ERROR = 0;
	private static final int SUCCESS = 1;

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
	
	private Handler mHandler = new Handler(new Handler.Callback() {
		
		public boolean handleMessage(Message msg) {
			
			Bundle b = msg.getData();
			
			switch (msg.what) {
				case SUCCESS:
					String title = (String) b.get("title");
					Log.i(TAG, "#handleMessage title: " + title);
					Toast.makeText(MovieSearch.this, title, Toast.LENGTH_SHORT).show();
					break;
					
				case ERROR:
					String error = (String) b.get("error");
					Toast.makeText(MovieSearch.this, error, Toast.LENGTH_SHORT).show();
					break;
	
				default:
					break;
			}

			return false;
		}
	});
	
	private void searchMovie(final String movieName) {
		
		Thread movieFinderThread = new Thread(new Runnable() {
			
			public void run() {
				ServerConnectionManager servConnMgr = new ServerConnectionManager();
				HashMap<String, String> outPut = new HashMap<String, String>();
				
				servConnMgr.searchMovie(movieName, outPut);
				
				Message msg = new Message();
				Bundle b = new Bundle();
				
				if( outPut.containsKey("error") ){
					Log.e(TAG, "#searchMovie error receiving response from the server.");
					
					msg.what = ERROR;
					
					b.putString("error", outPut.get("error") );
				} else{ // successful
					
					String title = outPut.get("title");
					if( outPut.containsKey("title") ) Log.i(TAG, "#searchMovie title: " +  title);
					if( outPut.containsKey("year") ) Log.i(TAG, "#searchMovie year: " + outPut.get("year") );
					if( outPut.containsKey("runtime") ) Log.i(TAG, "#searchMovie runtime: " + outPut.get("runtime") );
					if( outPut.containsKey("synopsis") ) Log.i(TAG, "#searchMovie synopsis: " + outPut.get("synopsis") );
					
					msg.what = SUCCESS;
					
					b.putString("title", title );
					
				}
				
				msg.setData(b);
				mHandler.sendMessage(msg);
			}
		});
		
		movieFinderThread.start();

	}
}