package com.example.sqloperationscheatsheet;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TodoSQLiteHelper extends SQLiteOpenHelper {

  public static final String TABLE_TODO_LIST = "todo_list";
  public static final String COL_ID = "_id";
  public static final String COL_ITEM = "item";

  private static final String DATABASE_NAME = "todo.db";
  private static final int DATABASE_VERSION = 1;

  // Database creation sql statement
  private static final String DATABASE_CREATE = "create table "
      + TABLE_TODO_LIST + "(" + COL_ID
      + " integer primary key autoincrement, " + COL_ITEM
      + " text not null);";

  public TodoSQLiteHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    database.execSQL(DATABASE_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(TodoSQLiteHelper.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO_LIST);
    onCreate(db);
  }

} 