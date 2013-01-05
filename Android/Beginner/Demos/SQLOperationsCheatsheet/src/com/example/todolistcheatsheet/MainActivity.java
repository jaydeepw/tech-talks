package com.example.todolistcheatsheet;

import java.util.HashMap;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private static final String TAG = MainActivity.class.getName();

	protected static final int DB_READ_ERROR = 0;
	protected static final int DB_READ_SUCCESS = 1;
	
	// usually its a convention in industry that all the member
	// variables are name with initial 'm' in their names.
	// Sometimes you will also find them starting with an
	// underscore.
	private Button mAddItem;
	private EditText mTodoItemET;
	private Handler mHandler;
	
	private View.OnClickListener mOnItemClickListerner;
	
	private HashMap<Integer, String> mTodoMap = new HashMap<Integer, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.main );
        
        mAddItem = (Button) findViewById(R.id.todo_add);
        mTodoItemET = (EditText) findViewById(R.id.todo_item);
        
        initOnAddItemClicked();
        intiHandler();
        initItemClickListener();
        
        retrieveTodoItems();
    }

	private void initItemClickListener() {
		mOnItemClickListerner = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Object tag = v.getTag();
				
				if( tag != null ) {
					Log.i(TAG, "#onClick view has been clicked. view tag: " + tag.toString() );
					Integer id = (Integer) tag;
			 		// deleteItem(id);
				}
					
			}
		};
	}

	private void deleteItem(Integer id) {
		
	}

	private void intiHandler() {
		mHandler = new Handler( new Handler.Callback() {
			
			@Override
			public boolean handleMessage(Message msg) {
				
				switch ( msg.what ) {
					case DB_READ_SUCCESS:
						showTodoItems();
						break;
						
					case DB_READ_ERROR:
						Log.i(TAG, "#handleMessage Error while reading the todo items");
						// TODO: probably we shoud show some message in UI at this time.
						break;
	
					default:
						break;
				}
				
				return true;
			}
		});
	}

	private void showTodoItems() {
		
		if( mTodoMap.size() == 0 ) {
			Log.w(TAG, "#showTodoItems no todo items to show");
			return;
		}
		
		String todoItem = null;
		LinearLayout todoList = (LinearLayout) findViewById(R.id.todo_list);
		todoList.removeAllViews();
		
		for( Integer i = 1; i <= mTodoMap.size(); i++ ) {
			TextView tv = new TextView(this);
			todoItem = mTodoMap.get(i);
			tv.setText( todoItem );
			// tv.setText( "" + i + " " + todoItem );	// uncomment to show items with numbers
			
			// this will help us in detecting which item has to be deleted.
			tv.setTag(i);
			
			// make it a bit beautiful. Right now, I am really BAD at this.
			tv.setPadding(10, 5, 10, 5);
			
			tv.setOnClickListener(mOnItemClickListerner);
			todoList.addView(tv);
		}	// end for
		
	}

	private void retrieveTodoItems() {
		TodoSQLiteHelper helper = null;
		SQLiteDatabase db = null;
		Cursor cur = null;
		Message msg = new Message();
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
			msg.what = DB_READ_SUCCESS;
			
		} catch (Exception e) {
			// BEST PRACTICE: its always a good practice to print the reason when an exception rises.
			Log.i(TAG, "#retrieveTodoItems Exception while inserting into db. Reason: " + e.getMessage() );
			msg.what = DB_READ_ERROR;
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
		
		// update the UI thread
		mHandler.sendMessage(msg);
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
				retrieveTodoItems();
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
