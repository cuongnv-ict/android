package com.example.danhba;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Vector;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;

public class DowloadFile extends AsyncTask<Void, Void, Boolean> {
	 
    private DropboxAPI<?> dropbox;
    private String path;
    private Context context;
    private Vector<ArrayList<String>> v;
    private final String MATACH = "{#-#}";
    public DowloadFile(Context context, DropboxAPI<?> dropbox,String path) {
        this.context = context.getApplicationContext();
        this.dropbox = dropbox;
        this.path = path;
        v = new Vector<ArrayList<String>>();
    }
 
    @Override
    protected Boolean doInBackground(Void... params) {
        final File tempDir = context.getCacheDir();
        File tempFile;
        try {
        	File f = new File(tempDir+"FileContact.txt");
        	f.delete();
            tempFile = File.createTempFile("FileContact", ".txt", tempDir);
            FileOutputStream fileOutPutStream = new FileOutputStream(tempFile);
            dropbox.getFile(path+"FileContact.txt", null,fileOutPutStream, null);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (DropboxException e) {
        	e.printStackTrace();
            return false;
        } 
    }
 
    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            Toast.makeText(context, "Dowload file thành công!",Toast.LENGTH_LONG).show();
            try {
				BufferedReader read = new BufferedReader(new FileReader(context.getCacheDir()+"FileContact.txt"));
				String people = read.readLine();
				while(people!=null){
					String pInfo[] = people.split(MATACH);
					ArrayList<String> obj = new ArrayList<String>();
					for(int i=0;i<pInfo.length;i++){
						obj.add(pInfo[i]);
					}
					v.add(obj);
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } else {
            Toast.makeText(context, "Dowload file thất bại", Toast.LENGTH_LONG).show();
        	}
    }
    public Vector<ArrayList<String>> getDataFile(){
    	 return v;
    }
}
