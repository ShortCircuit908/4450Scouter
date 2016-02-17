package com.orf4450.frcscouter.master;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.orf4450.frcscouter.TimedConfirmation;
import com.orf4450.scouter.R;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Caleb Milligan
 *         Created on 2/17/2016
 */
public class MasterActivity extends Activity {
	private IncomingConnectionSink sink;
	private BluetoothManager bluetooth_manager;
	private ArrayAdapter<RemoteDeviceWrapper> list_adapter;
	private ToggleButton switch_accept_connections;
	private SharedPreferences settings;
	private MasterDB database_helper;
	public static final String APPLICATION_URL = "https://www.example.com/upload"; //TODO: Set up the actual webserver for this thx

	@Override
	public void onCreate(Bundle state) {
		super.onCreate(state);
		setContentView(R.layout.master);
		settings = getPreferences(Activity.MODE_PRIVATE);
		database_helper = new MasterDB(this);
		bluetooth_manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
		list_adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
		ListView list_active_connections = (ListView) findViewById(R.id.list_active_connections);
		list_active_connections.setAdapter(list_adapter);
		switch_accept_connections = (ToggleButton) findViewById(R.id.switch_accept_connections);
		switch_accept_connections.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (switch_accept_connections.isChecked()) {
					createSink();
				}
				else {
					closeSink();
				}
			}
		});
	}

	public void uploadSilent() {
		Dialog status_dialog = status_dialog = new Dialog(this);
		status_dialog.setContentView(R.layout.upload_status);
		status_dialog.show();
		try {
			upload();
		}
		catch (IOException e) {
			new AlertDialog.Builder(this)
					.setTitle("Upload failed")
					.setMessage(e.getClass() + ": " + e.getMessage())
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setOnDismissListener(new DialogInterface.OnDismissListener() {
						@Override
						public void onDismiss(DialogInterface dialog) {
						}
					})
					.show();
		}
		status_dialog.dismiss();
	}

	private void upload() throws IOException {
		HttpURLConnection connection = (HttpURLConnection) new URL(APPLICATION_URL).openConnection();
		connection.setRequestMethod("POST");
		connection.setConnectTimeout(5000);
		connection.connect();
		OutputStream out = connection.getOutputStream();
		database_helper.upload(out);
		out.flush();
		out.close();
		connection.disconnect();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.stand_menu, menu);
		menu.findItem(R.id.delete_all).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				final Dialog dialog = new Dialog(MasterActivity.this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
				dialog.setContentView(R.layout.delete_all);
				final ProgressBar progress_bar = (ProgressBar) dialog.findViewById(R.id.progress_delete_all);
				new TimedConfirmation(3000, progress_bar, new Runnable() {
					@Override
					public void run() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								dialog.dismiss();
								Toast toast = Toast.makeText(MasterActivity.this, "Deleting", Toast.LENGTH_SHORT);
								toast.show();
								database_helper.deleteAllData();
								toast = Toast.makeText(MasterActivity.this, "All data deleted", Toast.LENGTH_SHORT);
								toast.show();
							}
						});
					}
				});
				dialog.show();
				return false;
			}
		});
		menu.findItem(R.id.auto_upload).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				item.setChecked(!item.isChecked());
				settings.edit().putBoolean("auto_upload", item.isChecked()).apply();
				return true;
			}
		}).setChecked(settings.getBoolean("auto_upload", true));
		return true;
	}


	public boolean shouldAutoUpload() {
		return settings.getBoolean("auto_upload", true);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		closeSink();
	}

	private void closeSink() {
		if (sink != null) {
			try {
				sink.close();
				sink = null;
				new AlertDialog.Builder(this).setTitle("Sink destroyed")
						.setMessage("Device is no longer listening for connections")
						.setIcon(android.R.drawable.ic_delete)
						.show();
			}
			catch (IOException e) {
				if (!isFinishing()) {
					new AlertDialog.Builder(this).setTitle("Could not destroy sink")
							.setMessage(e.getClass().getName() + ": " + e.getMessage())
							.setIcon(android.R.drawable.ic_dialog_alert)
							.show();
				}
				else {
					e.printStackTrace();
				}
			}
		}
	}

	private void createSink() {
		if (sink != null) {
			new AlertDialog.Builder(this).setTitle("Could not create sink")
					.setMessage("Connection sink is already running")
					.setIcon(android.R.drawable.ic_dialog_alert)
					.show();
			return;
		}
		try {
			sink = new IncomingConnectionSink(this, bluetooth_manager, database_helper, list_adapter);
			new AlertDialog.Builder(this).setTitle("Sink created")
					.setMessage("Device is now listening for connections")
					.setIcon(android.R.drawable.stat_sys_data_bluetooth)
					.show();
		}
		catch (IOException e) {
			new AlertDialog.Builder(this).setTitle("Could not create sink")
					.setMessage(e.getClass().getName() + ": " + e.getMessage())
					.setIcon(android.R.drawable.ic_dialog_alert)
					.show();
		}
	}
}
