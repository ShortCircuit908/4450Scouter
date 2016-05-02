package com.orf4450.frcscouter.master;

import android.app.Activity;
import com.orf4450.frcscouter.ScouterConstants;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Caleb Milligan
 *         Created on 2/24/2016
 */
public class DataUploadTask extends UploadTask {
	private final MasterDB database_helper;

	public DataUploadTask(Activity context, Callback callback) {
		super(callback);
		database_helper = new MasterDB(context);
	}

	@Override
	public void run() {
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(ScouterConstants.DATA_UPLOAD_URL).openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setDoInput(true);
			connection.setRequestProperty("User-Agent", ScouterConstants.APP_USER_AGENT);
			connection.connect();
			database_helper.upload(connection.getOutputStream());
			int response_code = connection.getResponseCode();
			String response_message = connection.getResponseMessage();
			connection.disconnect();
			if (response_code != 200) {
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
