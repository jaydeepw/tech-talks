package com.example.sqloperationscheatsheet;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private static final String TAG = MainActivity.class.getName();
	
	private Button mAddItem;
	private EditText mTodoItemET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.main );
        
        mAddItem = (Button) findViewById(R.id.todo_add);
        mTodoItemET = (EditText) findViewById(R.id.todo_item);
        
        initOnAddItemClicked();
        
        // showTodoItems();
    }

	private void initOnAddItemClicked() {
		mAddItem.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String todoItem = mTodoItemET.getText().toString();
				
				if( todoItem == null || TextUtils.isEmpty(todoItem) ) {
					showToastMsg("Please enter a todo item to add");
					return;	
				}
				
				addItem(todoItem);
			}
		});
	}

	protected void showToastMsg(String msg) {
		Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		toast.show();
	}

	protected void addItem(String todoItem) {
		
		TodoSQLiteHelper helper = null;
		SQLiteDatabase db = null;
		
		try {
			helper = new TodoSQLiteHelper(this);
			db = helper.getWritableDatabase();
			
			ContentValues values = new ContentValues();
			
			values.put(Constants.COL_ITEM, todoItem);
			long rowId = db.insert(Constants.TABLE_TODO_LIST, null, values);
			
			// error handling
			if( rowId == -1 ) {
				Log.e(TAG, "#addItem Error inserting the item into the table");
				showToastMsg("Error inserting the item");
			} else {
				showToastMsg("Item inserted successfully");
			}	
		} catch (Exception e) {
			// BEST PRACTICE: its always a good practice to print the reason when an exception rises.
			Log.i(TAG, "#addItem Exception while inserting into db. Reason: " + e.getMessage() );
		} finally {
			// BEST PRACTICE: Always close the cursor and DB in the 'finally' clause
			// not doing so may let you into debugging the entire app sometime later
			// when the project is about 2-3 year old.
			if( db != null && db.isOpen() )
				db.close();
			
			if( helper != null )
				helper.close();
			
			// NOTE: The sequence of closing DB and DBHelper and cursor matters a lot.
			// Read more about this here  http://stackoverflow.com/questions/4195089/what-does-invalid-statement-in-fillwindow-in-android-cursor-mean/8325904#8325904
		}
	}
    
}
