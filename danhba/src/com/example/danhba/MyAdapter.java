package com.example.danhba;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends ArrayAdapter<Contacter> {

	Activity context;
	int layoutid;
	ArrayList<Contacter> array;
	public MyAdapter(Activity context, int layoutid,ArrayList<Contacter> array ) {
		super(context, layoutid, array);
		// TODO Auto-generated constructor stub
		this.context=context;
		this.layoutid=layoutid;
		this.array= array;
	}
	public View getView(int posittion, View ConvertView, ViewGroup parent)
	{
		LayoutInflater inflater=context.getLayoutInflater();
		ConvertView=inflater.inflate(layoutid, null);
		if(array.size()>0&&posittion>=0)
		{
			 final TextView username=(TextView) ConvertView.findViewById(R.id.userName);
			 final Contacter contacter= array.get(posittion);
			 final TextView dienthoai= (TextView) ConvertView.findViewById(R.id.telephoneNumber);
			 username.setText(contacter.GetName());
			 if(!contacter.GetDidong().equals(""))
				 dienthoai.setText(contacter.GetDidong());
			 else if (!contacter.GetDienthoai().equals(""))
			 	 dienthoai.setText(contacter.GetDienthoai());
			 final ImageView image= (ImageView) ConvertView.findViewById(R.id.icon_user);
		
		}
		return ConvertView;
	}

}
