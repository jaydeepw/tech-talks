package com.learning.notificationdemo;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v4.app.NavUtils;

public class MainActivity extends Activity {
	
	private static final String TAG = "MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        initShowNotificaitonButton();
    }

	private void initShowNotificaitonButton() {
		Button showNotif = (Button) findViewById(R.id.show_notification);
		
		showNotif.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i(TAG, "#initShowNotificaiton: Button has been clicked.");
			}
		});
	}
    
}
