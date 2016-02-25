package com.orf4450.frcscouter.master;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author ShortCircuit908
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
				HttpURLConnection connection = (HttpURLConnection) new URL(MasterActivity.IMAGE_UPLOAD_URL + "?filename=" + file_name).openConnection();
				connection.setRequestMethod("POST");
				connection.setRequestProperty("User-Agent", "4450Scouting/1.0");
				connection.setDoOutput(true);
				connection.connect();
				FileInputStream in = new FileInputStream(file);
				OutputStream out = connection.getOutputStream();
				int cur_byte;
				while ((cur_byte = in.read()) != -1) {
					out.write(cur_byte);
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
