package com.melayer.savedata;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button btnSharedPrefs;

	private Button btnReadPrefs;

	private Button btnWriterInternal, btnReadInternal;

	private Button btnWritePublic, btnReadPublic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ContentResolver resolver = getContentResolver(); //1 

		// Uri uri = Uri.parse("content://com.melayer.temp.provider");
		Cursor c = resolver.query(
				android.provider.ContactsContract.Contacts.CONTENT_URI, null,
				null, null, null);

		while (c.moveToNext()) {

			Log.i("####### DATA FROM OTHER APP #######",
					""
							+ c.getString(c
									.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
		}

		btnSharedPrefs = (Button) findViewById(R.id.btnSharedPrefs);
		btnSharedPrefs.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				SharedPreferences prefs = getSharedPreferences("my_prefs",
						Context.MODE_APPEND);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString("KEY_MY_NAME", "android");
				editor.commit();
			}
		});

		btnReadPrefs = (Button) findViewById(R.id.btnSharedPrefsRead);
		btnReadPrefs.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				SharedPreferences prefs = getSharedPreferences("my_prefs",
						Context.MODE_APPEND);

				btnReadPrefs.setText(prefs.getString("KEY_MY_NAME", "none"));
			}
		});

		btnWriterInternal = (Button) findViewById(R.id.btnWriteInternalFile);
		btnWriterInternal.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Log.i("##### "+MainActivity.class.getCanonicalName()+" #####",
				// " Path by getFilesDir() - "+getFilesDir().getAbsolutePath());
				// File myFile = new File(getFilesDir(), "my.txt");

				File myFile = new File(getExternalFilesDir(""), "my.txt");
				Log.i("##### " + MainActivity.class.getCanonicalName()
						+ " #####", " Path by getExternalFilesDir() - "
						+ myFile.getAbsolutePath());
				try {
					String data = "hello world";

					FileOutputStream fos = new FileOutputStream(myFile);
					fos.write(data.getBytes());
					fos.close();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		btnReadInternal = (Button) findViewById(R.id.btnReadInternal);
		btnReadInternal.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				try {

					// File myFile = new File(getFilesDir(), "my.txt");
					File myFile = new File(getExternalFilesDir(""), "my.txt");
					FileInputStream fis = new FileInputStream(myFile);
					long length = myFile.length();
					byte[] dataBytes = new byte[(int) length];
					fis.read(dataBytes);
					fis.close();

					String data = new String(dataBytes);

					Log.i("######## DATA FORM FILE ######", "" + data);

					File fileRoot = new File("/");

					File[] arrUnderRoot = fileRoot.listFiles();

					for (File f : arrUnderRoot) {

						Log.i("##########", (f.isDirectory() ? " $ " : " - ")
								+ f.getName());
					}
					// File.listRoots();

					Toast.makeText(MainActivity.this, "" + data,
							Toast.LENGTH_LONG).show();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		btnWritePublic = (Button) findViewById(R.id.btnWritePublic);
		btnWritePublic.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {

					try {
						File publicFile = new File(Environment
								.getExternalStoragePublicDirectory(""),
								"my.txt");

						Log.i("######## PUBLIC DIRECTORY #########",
								"getExternalStoragePublicDirectory returns - "
										+ publicFile.getAbsolutePath());
						publicFile.createNewFile();
						FileOutputStream fos = new FileOutputStream(publicFile);
						fos.write("Heloo world".getBytes());
						fos.close();
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {

					Toast.makeText(MainActivity.this, "Bad Media",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		btnReadPublic = (Button) findViewById(R.id.btnReadPublic);

	}
}
