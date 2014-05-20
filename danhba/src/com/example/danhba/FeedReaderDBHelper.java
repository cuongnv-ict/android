package com.example.danhba;

import com.example.danhba.FeedReaderContract.Feedentry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FeedReaderDBHelper extends SQLiteOpenHelper {
	private static final int Version= 1;
	private static final String NameDatabase= "contacts.db";
	private static final String TextType=" TEXT";
	private static final String Command=", ";
	private static final String SQL_CREATE_DATABASE= "CREATE TABLE " + Feedentry.TableName + " (" 
										+ Feedentry.Id + " INTEGER PRIMARY KEY, " 	
										+ Feedentry.Name + TextType + Command 
										+ Feedentry.DiaChi + TextType + Command 
										+ Feedentry.DienThoai + TextType + Command
										+ Feedentry.DiDong + TextType + Command 
										+ Feedentry.ToChuc + TextType + Command 
										+ Feedentry.Email + TextType + ")";
	private static final String SQL_DELETE_DATABASE= "DROP TABLE IF EXISTS " + Feedentry.TableName;
	
	public FeedReaderDBHelper(Context context) {
		super(context,NameDatabase,null, Version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(SQL_CREATE_DATABASE);
		Log.d("hay", "hay");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL(SQL_DELETE_DATABASE);
		onCreate(db);
		}
	
	

}
