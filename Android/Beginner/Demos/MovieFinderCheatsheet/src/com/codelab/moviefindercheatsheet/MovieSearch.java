package com.codelab.moviefindercheatsheet;

import com.codelab.restapi.ServerConnectionManager;

import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MovieSearch extends Activity {
	
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
				new ServerConnectionManager().searchMovie(movieName);
			}
		});
	}
}