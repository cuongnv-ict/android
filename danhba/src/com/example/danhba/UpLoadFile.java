package com.example.danhba;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;

public class UpLoadFile extends AsyncTask<Void, Void, Boolean> {

	private DropboxAPI<?> dropbox;
	private String path;
	private Context context;
	private MainActivity ac;
	private final String MATACH = "{#-#}";
	private File tempFile;

	public UpLoadFile(Context context, DropboxAPI<?> dropbox, String path,
			MainActivity ac) {
		this.context = context.getApplicationContext();
		this.dropbox = dropbox;
		this.path = path;
		this.ac = ac;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		final File tempDir = context.getCacheDir();
		FileWriter fr;
		File f = new File(tempDir+"FileContact.txt");
    	f.delete();
		try {
			tempFile = File.createTempFile("FileContact", ".txt", tempDir);
			fr = new FileWriter(tempFile);
			Vector v = new Vector();
				v= ac.readContacts2();
			ArrayList <?> s;
			for (int i = 0; i < v.size(); i++) {
				s = (ArrayList) v.get(i);
				String data = String.valueOf(s.get(0)) + MATACH
							+ String.valueOf(s.get(1)) + MATACH
							+ String.valueOf(s.get(2)) + MATACH
							+ String.valueOf(s.get(3)) + MATACH
							+ String.valueOf(s.get(4)) + MATACH
							+ String.valueOf(s.get(5)) + MATACH
							+ String.valueOf(s.get(6));
				fr.write(data + "\n");
			}
			fr.close();
			dropbox.delete(path + "FileContact.txt");
			FileInputStream fileInputStream = new FileInputStream(tempFile);
			dropbox.putFile(path + "FileContact.txt", fileInputStream,
					tempFile.length(), null, null);
			tempFile.delete();
			return true;
		} catch (IOException e) {
			e.toString();
			return false;
		} catch (DropboxException e) {

			try {
				FileInputStream fileInputStream = new FileInputStream(tempFile);
				dropbox.putFile(path + "FileContact.txt", fileInputStream,
						tempFile.length(), null, null);
				tempFile.delete();
				return true;
			} catch (DropboxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return false;
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return false;
			}
		}
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (result) {
			Toast.makeText(context, "Upload dữ liệu thành công!",
					Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(context, "Upload dữ liệu thất bại",
					Toast.LENGTH_LONG).show();
		}
	}
}
