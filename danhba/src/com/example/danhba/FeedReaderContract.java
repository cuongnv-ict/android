package com.example.danhba;

import android.provider.BaseColumns;

public final class FeedReaderContract {
	public FeedReaderContract() {
	}
	public static abstract class Feedentry implements BaseColumns
	{
	public static String TableName="contact";
	public static String Id="id";
	public static String Name="name";
	public static String DiaChi="diachi";
	public static String DienThoai="dienthoai";
	public static String DiDong="didong";
	public static String ToChuc="tochuc";
	public static String Email="Email";
	}
	;
}
