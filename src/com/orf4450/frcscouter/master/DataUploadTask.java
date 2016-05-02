package com.orf4450.frcscouter.master;

import android.app.Activity;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Caleb Milligan
 *         Created on 2/24/2016
 */
public class DataUploadTask extends UploadTask{
	private final MasterDB database_helper;

	public DataUploadTask(Activity context, Callback callback) {
		super(callback);
		database_helper = new MasterDB(context);
	}

	@Override
	public void run() {
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(MasterActivity.APPLICATION_URL).openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setDoInput(true);
			connection.setRequestProperty("User-Agent", "4450Scouting/1.0");
			connection.connect();
			database_helper.upload(connection.getOutputStream());
			int response_code = connection.getResponseCode();
			String response_message = connection.getResponseMessage();
			connection.disconnect();
			if(response_code != 200){
				throw new IOException(response_code + ": " + response_message);
			}
			database_helper.deleteAllData();
		}
		catch (Throwable e) {
			e.printStackTrace();
			error = e;
		}
		callback.onUploadFinished(error);
	}
}
