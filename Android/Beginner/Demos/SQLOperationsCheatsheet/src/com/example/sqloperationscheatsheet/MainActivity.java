package com.example.sqloperationscheatsheet;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	
	private static final String TAG = "MainActivity";
	
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
				Log.d(TAG, "#addItemClicked ");
				String todoItem = mTodoItemET.getText().toString();
				
				addItem(todoItem);
			}
		});
	}

	protected void addItem(String todoItem) {
		Log.d(TAG, "#addItem current item " + todoItem );
		
		TodoSQLiteHelper helper = new TodoSQLiteHelper(this);
		
		SQLiteDatabase db = helper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		
		values.put(Constants.COL_ITEM, todoItem);
		long rowId = db.insert(Constants.TABLE_TODO_LIST, null, values);
		
		// error handling
		if( rowId == -1 ) {
			Log.e(TAG, "#addItem Error inserting the item into the table");
		} else
			Log.d(TAG, "#addItem Item added successfully");
	}
    
}
