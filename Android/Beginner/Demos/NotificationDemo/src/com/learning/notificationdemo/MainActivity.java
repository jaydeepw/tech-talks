package com.learning.notificationdemo;

import android.os.Bundle;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
				showNotification();
			}
		});
	}

	@SuppressWarnings("deprecation")
	protected void showNotification() {
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		
		int icon = R.drawable.ic_launcher;
		CharSequence tickerText = "Hello";
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, tickerText, when);
		
		Context context = getApplicationContext();
		CharSequence contentTitle = "My notification";
		CharSequence contentText = "Hello World!";
		Intent notificationIntent = new Intent(this, MyClass.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		
		private static final int HELLO_ID = 1;

		mNotificationManager.notify(HELLO_ID, notification);
	}
    
}
