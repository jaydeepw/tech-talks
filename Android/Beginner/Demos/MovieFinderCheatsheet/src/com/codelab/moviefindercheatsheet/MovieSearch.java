package com.codelab.moviefindercheatsheet;

import com.codelab.restapi.ServerConnectionManager;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MovieSearch extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_search);
        
        new ServerConnectionManager().hitApi();
    }
}
