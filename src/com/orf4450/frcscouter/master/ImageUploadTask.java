package com.orf4450.frcscouter.master;

import com.orf4450.frcscouter.ScouterConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Caleb Milligan
 *         Created on 2/24/2016
 */
public class ImageUploadTask extends UploadTask {
	private final File file;
	private final String file_name;

	public ImageUploadTask(Callback callback, File file, String file_name) {
		super(callback);
		this.file = file;
		this.file_name = file_name;
	}

	@Override
	public void run() {
		try {
			if (file.exists()) {
				HttpURLConnection connection = (HttpURLConnection) new URL(ScouterConstants.IMAGE_UPLOAD_URL + "?filename=" + file_name).openConnection();
				connection.setRequestMethod("POST");
				connection.setRequestProperty("User-Agent", ScouterConstants.APP_USER_AGENT);
				connection.setDoOutput(true);
				connection.connect();
				FileInputStream in = new FileInputStream(file);
				OutputStream out = connection.getOutputStream();
				int n;
				byte[] buffer = new byte[4096];
				while ((n = in.read(buffer)) > -1) {
					out.write(buffer, 0, n);
				}
				out.flush();
				out.close();
				in.close();
				int response_code = connection.getResponseCode();
				String response_message = connection.getResponseMessage();
				connection.disconnect();
				if (response_code != 200) {
					throw new IOException(response_code + ": " + response_message);
				}
			}
		}
		catch (Throwable e) {
			e.printStackTrace();
			error = e;
		}
		callback.onUploadFinished(error);
	}
}
