package com.example.danhba;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


public class add_contact extends Activity{
	ImageView btn_add_contact;
	EditText add_name,add_dd,add_dt,add_dc,add_tc,add_email; 
	String name,didong,dienthoai,diachi,tochuc,email;
	
	public add_contact() {
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_contact);
		btn_add_contact = (ImageView) findViewById(R.id.btn_add_contact);
		add_name= (EditText) findViewById(R.id.add_name);
		add_dd= (EditText) findViewById(R.id.add_dd);
		add_dt= (EditText) findViewById(R.id.add_dt);
		add_dc= (EditText) findViewById(R.id.add_dc);
		add_email= (EditText) findViewById(R.id.add_email);
		add_tc= (EditText) findViewById(R.id.add_tc);
		//add_qh= (EditText) findViewById(R.id.add);
		// xử lý sự kiện thêm mới danh bạ
		btn_add_contact.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				name=add_name.getText() + "";
				didong= add_dd.getText() + "";
				dienthoai= add_dt.getText() + "";
				diachi=  add_dc.getText() + "";
				tochuc= add_tc.getText() + "";
				email= add_email.getText() + "";
				if(!(name.equals("")||(didong.equals("")&&dienthoai.equals(""))))
				{
					if ((!didong.equals("")&&didong.length()>=10)||(dienthoai.length()>=10)&&!dienthoai.equals(""))
					{
					btn_add_contact.setImageResource(R.drawable.btn_add_contact_click);
					Intent i = new Intent();
					i.putExtra("name", name);
					i.putExtra("didong", didong);
					i.putExtra("dienthoai", dienthoai);
					i.putExtra("diachi", diachi);
					i.putExtra("email", email);
					i.putExtra("tochuc", tochuc);
					setResult(RESULT_OK, i);
					finish();
					}

					else Toast.makeText(add_contact.this, "sdt không đúng", Toast.LENGTH_SHORT).show();
					onDestroy();
				}
				else Toast.makeText(add_contact.this, "loi nhap du lieu!", Toast.LENGTH_SHORT).show();
				onDestroy();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
