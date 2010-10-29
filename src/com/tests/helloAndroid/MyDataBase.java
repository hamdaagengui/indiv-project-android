package com.tests.helloAndroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.hardware.SensorManager;

public class MyDataBase extends SQLiteOpenHelper{
	private static final String DATABASE_NAME = "db";
	private static final String TITLE = "title";
	private static final String VALUE = "value";
	
	
	public MyDataBase(Context context){
		super(context,DATABASE_NAME,null,1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE myTable (_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, value REAL);");
		
		ContentValues cv = new ContentValues();
		
		cv.put("TITLE", "Gravity Death Star");
		cv.put(VALUE, SensorManager.GRAVITY_DEATH_STAR_I);
		db.insert("myTable", TITLE, cv);
		
		cv.put("TITLE", "Gravity Earth");
		cv.put(VALUE, SensorManager.GRAVITY_EARTH);
		db.insert("myTable", TITLE, cv);	
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	

}
