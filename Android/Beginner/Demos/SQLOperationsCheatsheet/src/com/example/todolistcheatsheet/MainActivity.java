package com.example.todolistcheatsheet;

import java.util.HashMap;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private static final String TAG = MainActivity.class.getName();
	
	private Button mAddItem;
	private EditText mTodoItemET;
	
	private HashMap<Integer, String> mTodoMap = new HashMap<Integer, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.main );
        
        mAddItem = (Button) findViewById(R.id.todo_add);
        mTodoItemET = (EditText) findViewById(R.id.todo_item);
        
        initOnAddItemClicked();
        
        retrieveTodoItems();
    }

	private void retrieveTodoItems() {
		TodoSQLiteHelper helper = null;
		SQLiteDatabase db = null;
		Cursor cur = null;
		
		try {
			helper = new TodoSQLiteHelper(this);
			
			// BEST PRACTICE: Always prefer opening a DB in READABLE mode unless
			// its really required
			db = helper.getReadableDatabase();
			
			String[] columns = { Constants.COL_ID, Constants.COL_ITEM };
			cur = db.query( Constants.TABLE_TODO_LIST, columns, null, null, null, null, null );
			
			Log.d(TAG, "#retrieveTodoItems cur.size: " + cur.getCount() );
			
			int id = 0;
			String item = null;
			while( cur.moveToNext() ) {
				id = cur.getInt( cur.getColumnIndex(Constants.COL_ID) );
				item = cur.getString( cur.getColumnIndex(Constants.COL_ITEM) );
				mTodoMap.put(id, item);
			}
			
			Log.d( TAG, "#retrieveTodoItems mTodoMap.size: " + mTodoMap.size() );
			
		} catch (Exception e) {
			// BEST PRACTICE: its always a good practice to print the reason when an exception rises.
			Log.i(TAG, "#retrieveTodoItems Exception while inserting into db. Reason: " + e.getMessage() );
		} finally {
			// BEST PRACTICE: Always close the cursor and DB in the 'finally' clause
			// not doing so may let you into debugging the entire app sometime later
			// when the project is about 2-3 year old.
			if( cur != null && !cur.isClosed() )
				cur.close();
			
			if( db != null && db.isOpen() )
				db.close();
			
			if( helper != null )
				helper.close();
			
			// NOTE: The sequence of closing DB and DBHelper and cursor matters a lot.
			// Read more about this here  http://stackoverflow.com/questions/4195089/what-does-invalid-statement-in-fillwindow-in-android-cursor-mean/8325904#8325904
		}
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
			
			// BEST PRACTICE: Always prefer opening a DB in READABLE mode unless
			// its really required
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
				Log.d(TAG, "#addItem Item inserted successfully");
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
