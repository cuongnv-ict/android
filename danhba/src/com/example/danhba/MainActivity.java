package com.example.danhba;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.DropboxFileInfo;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;
import com.dropbox.client2.session.TokenPair;
import com.example.danhba.FeedReaderContract.Feedentry;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DialerFilter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;
import android.R.array;
import android.provider.ContactsContract.CommonDataKinds.Organization;

public class MainActivity extends TabActivity {
	ImageView btn_contact;
	ImageView btn_setting;
	ImageView btn_dn_dropbox, login_dropbox;
	ImageView btn_add_contact, update_contact_machine, update_contact_dropbox,
			up_dropbox, backup_dropbox;
	ArrayList<Contacter> array = null;
	MyAdapter adapter = null;
	ListView list;
	private FeedReaderDBHelper mDBHelper= new FeedReaderDBHelper(this);
	private SQLiteDatabase db;
	EditText Ed_search;
	/* _____________________________________________DropBox__________________________________________________ */
	final static private String APP_KEY = "04ad6fz9fodrhyb";
	final static private String APP_SECRET = "w6kexon2w8f3iog";
	final static private String ACCOUNT_PREFS_NAME = "prefs";
	
	DropboxAPI<AndroidAuthSession> mApi;
	private boolean mLoggedIn;

	/* ______________________________________________________________________________________________________ */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_danhsach);

		TabHost.TabSpec spec = getTabHost().newTabSpec("tag1");
		spec.setContent(R.id.list);
		spec.setIndicator("list");
		getTabHost().addTab(spec);

		spec = getTabHost().newTabSpec("tag2");
		spec.setContent(R.id.setting);
		spec.setIndicator("setting");
		getTabHost().addTab(spec);

		getTabHost().setCurrentTab(0);

		btn_contact = (ImageView) findViewById(R.id.btn_contact);
		btn_setting = (ImageView) findViewById(R.id.btn_setting);
		login_dropbox = (ImageView) findViewById(R.id.login_dropbox);
		btn_add_contact = (ImageView) findViewById(R.id.add_contact);
		list = (ListView) findViewById(R.id.list_show);
		Ed_search= (EditText) findViewById(R.id.Search);
		
		// khoi tao cac mang array
		array = new ArrayList<Contacter>();
		adapter = new MyAdapter(this, R.layout.row_list, array);
		list.setAdapter(adapter);
		readDatabase();

		/*
		 * ____________________________________click vào
		 * listview_______________________________
		 */
		
		

		list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				AlertDialog.Builder dialog = new AlertDialog.Builder(
						MainActivity.this);
				dialog.setTitle(array.get(arg2).GetName());
				dialog.setMessage("Di Động:" + array.get(arg2).GetDidong()
						+ "\n" + "Điện Thoại:" + array.get(arg2).GetDienthoai()
						+ "\n" + "Địa Chỉ:" + array.get(arg2).GetDiaChi()
						+ "\n" + "Email:" + array.get(arg2).GetEmail() + "\n"
						+ "Tổ Chức:" + array.get(arg2).GetTochuc());

				dialog.setNegativeButton("Đồng ý",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});
				/*
				 * dialog.setPositiveButton("Từ chối", new
				 * DialogInterface.OnClickListener() {
				 * 
				 * @Override public void onClick(DialogInterface dialog, int
				 * which) {
				 * 
				 * } });
				 */
				dialog.show();

				return false;
			}
		});
		// _______________________________________________________________________________________

		
		/*______________________________________sukientimkiem____________________________________ */
		Ed_search.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				search(s.toString());
			}
		});
		/*_______________________________________________________________________________________ */
		
		// ***********************
		update_contact_machine = (ImageView) findViewById(R.id.update_contact_machine);
		// update_contact_dropbox = (ImageView)
		// findViewById(R.id.update_contact_dropbox);
		up_dropbox = (ImageView) findViewById(R.id.up_dropbox);
		backup_dropbox = (ImageView) findViewById(R.id.backup_dropbox);
		/* __________________________________DropBox___________________________________________ */
		mLoggedIn = false;

		AndroidAuthSession session;
		AppKeyPair pair = new AppKeyPair(APP_KEY, APP_SECRET);

		SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
		String key = prefs.getString(APP_KEY, null);
		String secret = prefs.getString(APP_SECRET, null);

		if (key != null && secret != null) {
			AccessTokenPair token = new AccessTokenPair(key, secret);
			session = new AndroidAuthSession(pair, AccessType.APP_FOLDER, token);
		} else {
			session = new AndroidAuthSession(pair, AccessType.APP_FOLDER);
		}
		mApi = new DropboxAPI<AndroidAuthSession>(session);
		/* ___________________________________________________________________________________ */
		// khi nhấn vào tab danh bạ
		btn_contact.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btn_contact.setBackgroundResource(R.drawable.btn_contact_click);
				btn_setting.setBackgroundResource(R.drawable.btn_setting);
				getTabHost().setCurrentTab(0);
			}
		});

		// khi nhấn vào tab cài đặt
		btn_setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btn_contact.setBackgroundResource(R.drawable.btn_contact);
				btn_setting.setBackgroundResource(R.drawable.btn_setting_click);
				getTabHost().setCurrentTab(1);
			}
		});

		login_dropbox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mLoggedIn) {
					mLoggedIn = false;
					mApi.getSession().unlink();
					login_dropbox.setImageResource(R.drawable.login_dropbox);
				} else {
					mApi.getSession().startAuthentication(MainActivity.this);

				}
			}
		});

		// click vào nút thêm mới danh bạ
		btn_add_contact.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(MainActivity.this,
						add_contact.class);
				startActivityForResult(myIntent, 1);
			}
		});

		// cập nhật danh bạ từ máy
		update_contact_machine.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TBUp_db_machine();
			}
		});

		// Cập nhật danh bạ từ dropbox
		// update_contact_dropbox.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// TBUp_db_dropbox();
		// }
		// });

		// up dữ liệu lên dropbox
		up_dropbox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TBUp_len_dropbox();
			}
		});

		// backup dữ liệu từ dropbox
		backup_dropbox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TBBackup_dropbox();
			}
		});
	}

	protected void onResume() {
		super.onResume();

		AndroidAuthSession session = mApi.getSession();
		if (session.authenticationSuccessful()) {
			try {
				session.finishAuthentication();

				TokenPair tokens = session.getAccessTokenPair();
				SharedPreferences prefs = getSharedPreferences(
						ACCOUNT_PREFS_NAME, 0);
				Editor editor = prefs.edit();
				editor.putString(APP_KEY, tokens.key);
				editor.putString(APP_SECRET, tokens.secret);
				editor.commit();
				mLoggedIn = true;
				login_dropbox.setImageResource(R.drawable.logout_dropbox);
			} catch (IllegalStateException e) {
				Toast.makeText(this, "Lỗi xác thực tài khoản",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	// dialog xử lý khi cập nhật danh bạ từ máy
	public void TBUp_db_machine() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
		dialog.setTitle("Cập nhật dữ liệu");
		dialog.setMessage("Bạn có đồng ý đồng bộ danh bạ từ máy không ?");

		dialog.setNegativeButton("Đồng ý",
				new DialogInterface.OnClickListener() {
					int id;
					@Override
					public void onClick(DialogInterface dialog, int which) {

						Vector contact = readContacts2();
						Toast.makeText(MainActivity.this, "thanh cong",
								Toast.LENGTH_SHORT).show();

						array.clear();
						adapter.notifyDataSetChanged();
						for (int i = 0; i < contact.size(); i++) {
							ArrayList a = new ArrayList();
							// arraylist a chua thong tin cua mot nguoi
							a = (ArrayList) contact.get(i);
							id= Integer.parseInt(a.get(0).toString());
							System.out.println("ket qua tra ve" + checkDatabase(id));
							if(checkDatabase(id))
							{
								writeDatabase(Integer.parseInt(a.get(0).toString()), a.get(1).toString(), a.get(2).toString(), a.get(3).toString(), a.get(4).toString(), a.get(5).toString(), a.get(6).toString());
								
							}
							
							
						}
						readDatabase();

					}
				});
		dialog.setPositiveButton("Từ chối",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		dialog.show();
	}

	// dialog xử lý khi đồng bộ danh bạ từ dropbox
	// public void TBUp_db_dropbox(){
	// AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
	// dialog.setTitle("");
	// dialog.setMessage("Bạn có đồng ý đồng bộ danh bạ từ dropbox ?");
	// dialog.setPositiveButton("�?ồng ý",new DialogInterface.OnClickListener()
	// {
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	//
	// }
	// });
	// dialog.setNegativeButton("Từ chối", new DialogInterface.OnClickListener()
	// {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	//
	// }
	// });
	// dialog.show();
	// }

	// dialog xử lý khi muốn up dữ liệu lên dropbox
	public void TBUp_len_dropbox() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
		dialog.setTitle("Up dữ liệu");
		dialog.setMessage("Bạn có đồng ý up dữ liệu lên dropbox ?");

		dialog.setNegativeButton("Đồng ý",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (mLoggedIn) {
							UpLoadFile up = new UpLoadFile(MainActivity.this,mApi, "/Android/",MainActivity.this);
							up.execute();
						} else {
							Toast.makeText(MainActivity.this,
									"Bạn chưa đăng nhập dropbox",
									Toast.LENGTH_LONG).show();
							;
						}
					}
				});
		dialog.setPositiveButton("Từ chối",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		dialog.show();
	}

	// dialog xử lý khi muốn backup dữ liệu từ dropbox
	public void TBBackup_dropbox() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
		dialog.setTitle("Backup dữ liệu");
		dialog.setMessage("Bạn có đồng ý backup dữ liệu từ dropbox ?");

		dialog.setNegativeButton("Đồng ý",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (mLoggedIn) {
							DowloadFile dow = new DowloadFile(MainActivity.this,mApi, "/Android/");
							dow.execute();
						} else {
							Toast.makeText(MainActivity.this,
									"Bạn chưa đăng nhập dropbox",
									Toast.LENGTH_LONG).show();
							;
						}
					}
				});
		dialog.setPositiveButton("Từ chối",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		dialog.show();
	}

	// dialog đăng nhập dropbox
	// dialog xử lý nếu có lỗi
	public void TBloi() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
		dialog.setTitle("Error");
		dialog.setMessage("không thể thực hiện được thao tác ?");
		dialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		dialog.show();
	}

	
	/* _________________________________________nhandulieu________________________________________ */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Nếu kết quả code trả về là "RESULT_OK" với đoạn REQUEST_CODE tương
		// ứng
		array.clear();
		adapter.notifyDataSetChanged();
		if (resultCode == RESULT_OK && requestCode == 1) {
			String name = data.getStringExtra("name");
			String didong = data.getStringExtra("didong");
			String dienthoai = data.getStringExtra("dienthoai");
			String diachi = data.getStringExtra("diachi");
			String tochuc = data.getStringExtra("tochuc");
			String email = data.getStringExtra("email");
			if (!name.equals("")) {
				if (!didong.equals("")) {
					int id=array.size()+1000;
					writeDatabase(id, name, didong, dienthoai, email, tochuc, diachi);
				} else if (!dienthoai.equals("")) {
					int id=array.size()+1000;
					writeDatabase(id, name, didong, dienthoai, email, tochuc, diachi);
				}
			}
		}
		readDatabase();
	}

	/* ___________________________________________________________________________________________ */

	/* ___________________________________ReadContact____________________________________________________ */

	public Vector readContacts2() {
		  StringBuffer sb = new StringBuffer();
		  Vector v = new Vector();
		  sb.append("......Contact Details.....");
		  ContentResolver cr = getContentResolver();
		  Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
		    null, null, null);
		  String phone = null;
		  String mobile = "00null";
		  String homephone = "00null";
		  String emailContact = "00null";
		  String emailType = null;
		  String phoneType = null;
		  String address = "00null";
		  String company = "00null";
		  String image_uri = "";
		  Bitmap bitmap = null;
		  if (cur.getCount() > 0) {
		   while (cur.moveToNext()) {
			 ArrayList info = new ArrayList();
		    String id = cur.getString(cur
		      .getColumnIndex(ContactsContract.Contacts._ID));
		    info.add(id);
		    String name = cur
		      .getString(cur
		        .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

		    image_uri = cur
		      .getString(cur
		        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
		    if (Integer
		      .parseInt(cur.getString(cur
		        .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
		     System.out.println("name : " + name + ", ID : " + id);
		     sb.append("\n Contact Name:" + name);
		     info.add(name);
		     //lay so dien thoai
		     Cursor pCur = cr.query(
		       ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
		       null,
		       ContactsContract.CommonDataKinds.Phone.CONTACT_ID
		         + " = ?", new String[] { id }, null);
		     while (pCur.moveToNext()) {
		      phone = pCur
		        .getString(pCur
		          .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
		      phoneType = pCur
		    	        .getString(pCur
		    	          .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
		      sb.append("\n Phone number:" + phone + " type: "+phoneType);
		      if(phoneType.equals("1")){
		    	  homephone = phone;
		      }
		      if(phoneType.equals("2")){
		    	  mobile = phone;
		      }
		      System.out.println("phone" + phone);
		     }
		     info.add(mobile);
		     info.add(homephone);
		     pCur.close();
		     //lay Email
		     Cursor emailCur = cr.query(
		       ContactsContract.CommonDataKinds.Email.CONTENT_URI,
		       null,
		       ContactsContract.CommonDataKinds.Email.CONTACT_ID
		         + " = ?", new String[] { id }, null);
		     while (emailCur.moveToNext()) {
		      emailContact = emailCur
		        .getString(emailCur
		          .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
		      emailType = emailCur
		        .getString(emailCur
		          .getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
		      sb.append("\nEmail:" + emailContact + "Email type:" + emailType);
		      System.out.println("Email " + emailContact
		        + " Email Type : " + emailType);
		     }
		     info.add(emailContact);
		     emailCur.close();
	// lay ten cong ty
		     
		     String orgWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
		     String[] orgWhereParams = new String[]{id,
		         ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
		     Cursor orgCur = cr.query(ContactsContract.Data.CONTENT_URI,
		                 null, orgWhere, orgWhereParams, null);
		     if (orgCur.moveToFirst()) {
		         String orgName = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA));
		         sb.append("\n Company:"+ orgName);
		         String title = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
		         company = orgName;
		     }
		     info.add(company);
		     orgCur.close();
		     // tim dia chi
		     String addrWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
		     String[] addrWhereParams = new String[]{id,
		         ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE};
		     Cursor AddrCur = cr.query(ContactsContract.Data.CONTENT_URI,
		                 null,addrWhere, addrWhereParams, null);
		     if (AddrCur.moveToFirst()) {
		         String orgName = AddrCur.getString(AddrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.DATA));
		         sb.append("\n address:"+ orgName);
		         address = orgName;
		     }
		     info.add(address);
		     AddrCur.close();
		    }
		    
		    if (image_uri != null) {
		     System.out.println(Uri.parse(image_uri));
		     try {
		      bitmap = MediaStore.Images.Media
		        .getBitmap(this.getContentResolver(),
		          Uri.parse(image_uri));
		      sb.append("\n Image in Bitmap:" + bitmap);
		      System.out.println(bitmap);

		     } catch (FileNotFoundException e) {
		      // TODO Auto-generated catch block
		      e.printStackTrace();
		     } catch (IOException e) {
		      // TODO Auto-generated catch block
		      e.printStackTrace();
		     }

		    }
		   
		    
		    sb.append("\n........................................");
		    v.add(info);
		    //reset lai cac gia tri
		    mobile = "00null";
			homephone = "00null";
			emailContact = "00null";
			address = "00null";
			company = "00null";
		   }

		  }
		return v;
		 }
	/* __________________________________________________________________________________________________ */
	
	
	
	/*_________________________________________taocsdlmoivasudung________________________________________ */
		public void readDatabase()
		{
			db=mDBHelper.getReadableDatabase();
			String [] output= {Feedentry.Id,Feedentry.Name,Feedentry.DiDong,Feedentry.DienThoai,Feedentry.Email,Feedentry.ToChuc,Feedentry.DiaChi};
			Cursor c = db.query(Feedentry.TableName,output ,null , null, null, null, null);
			c.moveToFirst();
			
			do
			{
				Contacter contacter = new Contacter();
				contacter.SetId(Integer.parseInt(c.getString(0)));
				contacter.SetName(c.getString(1));
				contacter.SetDidong(c.getString(2));
				contacter.SetDienthoai(c.getString(3));
				contacter.SetEmail(c.getString(4));
				contacter.SetTochuc(c.getString(5));
				contacter.SetDiachi(c.getString(6));
				array.add(contacter);
				adapter.notifyDataSetChanged();
			}
			while(c.moveToNext());
		}
		public boolean checkDatabase(int id)
		{
			db=mDBHelper.getReadableDatabase();
			String [] output={Feedentry.Id,Feedentry.Name};
			String [] wherearg={Integer.toString(id)};
			String test="";
			Cursor c = db.query(Feedentry.TableName, output,Feedentry.Id + " = ?" ,wherearg , null, null, null);
			c.moveToFirst();
			int so=c.getColumnIndex(Feedentry.Name);
			do
			{
			test=c.getString(c.getColumnIndex(Feedentry.Name));
			System.out.println(test);
			}
			while(c.moveToNext());
			if(!test.equals(""))
				return false;
			return true;
		}
		public void writeDatabase(int id, String name, String didong, String dienthoai, String email,String tochuc, String diachi)
		{
			db=mDBHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(Feedentry.Id, id);
			values.put(Feedentry.Name, name);
			values.put(Feedentry.DiDong, didong);
			values.put(Feedentry.DienThoai, dienthoai);
			values.put(Feedentry.Email, email);
			values.put(Feedentry.ToChuc, tochuc);
			values.put(Feedentry.DiaChi, diachi);
			long newidrow;
			newidrow= db.insert(Feedentry.TableName, null, values);
		}
		public boolean editDatabase(int id,String name, String didong, String dienthoai,String email, String tochuc, String diachi)
		{
			db=mDBHelper.getReadableDatabase();
			ContentValues values= new ContentValues() ;
			values.put(Feedentry.Name, name);
			values.put(Feedentry.DiDong, didong);
			values.put(Feedentry.DienThoai, dienthoai);
			values.put(Feedentry.Email, email);
			values.put(Feedentry.ToChuc, tochuc);
			values.put(Feedentry.DiaChi, diachi);
			String where = Feedentry._ID + " = ? ";
			String [] wherearg={Integer.toString(id)};
			int count =db.update(Feedentry.Name, values, where, wherearg);
			if(count!=0) return true;
			return false;
		}
		public void search(String name)
		{
			array.clear();
			adapter.notifyDataSetChanged();
			db=mDBHelper.getReadableDatabase();
			String [] output= {Feedentry.Id,Feedentry.Name,Feedentry.DiDong,Feedentry.DienThoai,Feedentry.Email,Feedentry.ToChuc,Feedentry.DiaChi};
			String whereClause= Feedentry.Name + " LIKE ? ";
			String [] whereArgs= {"%"+name+"%"};
			Cursor c = db.query(Feedentry.TableName,output , whereClause , whereArgs , null, null, null);
			c.moveToFirst();
			do
			{
				Contacter contacter = new Contacter();
				contacter.SetId(Integer.parseInt(c.getString(0)));
				contacter.SetName(c.getString(1));
				contacter.SetDidong(c.getString(2));
				contacter.SetDienthoai(c.getString(3));
				contacter.SetEmail(c.getString(4));
				contacter.SetTochuc(c.getString(5));
				contacter.SetDiachi(c.getString(6));
				array.add(contacter);
				adapter.notifyDataSetChanged();
			}
			while(c.moveToNext());
		}
	/*___________________________________________________________________________________________________ */
}
