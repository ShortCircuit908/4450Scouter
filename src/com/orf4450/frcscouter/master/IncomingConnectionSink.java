package com.orf4450.frcscouter.master;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.orf4450.frcscouter.ScouterConstants;
import com.shortcircuit.nbn.Nugget;
import com.shortcircuit.nbn.nugget.NuggetCompound;

import java.io.IOException;
import java.util.UUID;

/**
 * @author Caleb Milligan
 *         Created on 1/14/2016
 */
public class IncomingConnectionSink implements Runnable {
	private final BluetoothServerSocket server_socket;
	private final MasterDB database;
	private final ArrayAdapter<RemoteDeviceWrapper> list_adapter;
	private final MasterActivity activity;

	public IncomingConnectionSink(MasterActivity activity, MasterDB database, ArrayAdapter<RemoteDeviceWrapper> list_adapter) throws IOException {
		this.activity = activity;
		this.database = database;
		this.list_adapter = list_adapter;
		this.server_socket = BluetoothAdapter.getDefaultAdapter().listenUsingRfcommWithServiceRecord(
				"Scoutmaster", UUID.fromString(ScouterConstants.APP_UUID));
		new Thread(this).start();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		while (true) {
			try {
				BluetoothSocket socket = server_socket.accept();
				new ConnectedScouter(activity, socket, new ScouterCallback() {
					@Override
					public void onDataRecieved(Nugget<?> data) {
						try {
							NuggetCompound compound = (NuggetCompound) data;
							database.saveNugget(compound.getNugget("stand_scouting"));
							database.saveNugget(compound.getNugget("pit_scouting"));
							activity.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									if (activity.shouldAutoUpload()) {
										activity.upload();
									}
									Toast toast = Toast.makeText(activity, "Data received", Toast.LENGTH_SHORT);
									toast.show();
								}
							});
						}
						catch (IOException e) {
							e.printStackTrace();
						}
					}
				}, list_adapter);
			}
			catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
	}

	public void close() throws IOException {
		server_socket.close();
	}
}
