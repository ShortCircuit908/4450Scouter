package com.orf4450.frcscouter.master;

import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.widget.ArrayAdapter;
import com.shortcircuit.nbn.Nugget;

import java.io.IOException;
import java.util.UUID;

/**
 * @author ShortCircuit908
 *         Created on 1/14/2016
 */
public class IncomingConnectionSink implements Runnable {
	private final BluetoothManager bluetooth_manager;
	private final BluetoothServerSocket server_socket;
	private final MasterDB database;
	private final ArrayAdapter<RemoteDeviceWrapper> list_adapter;
	private final MasterActivity activity;

	public IncomingConnectionSink(MasterActivity activity, BluetoothManager bluetooth_manager, MasterDB database, ArrayAdapter<RemoteDeviceWrapper> list_adapter) throws IOException {
		this.activity = activity;
		this.bluetooth_manager = bluetooth_manager;
		this.database = database;
		this.list_adapter = list_adapter;
		this.server_socket = bluetooth_manager.getAdapter().listenUsingRfcommWithServiceRecord(
				"Scoutmaster", UUID.fromString("7674047e-6e47-4bf0-831f-209e3f9dd23f"));
		new Thread(this).start();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		while (true) {
			try {
				BluetoothSocket socket = server_socket.accept();
				new ConnectedScouter(socket, new ScouterCallback() {
					@Override
					public void onDataRecieved(Nugget<?> data) {
						try {
							database.saveNugget(data);
							if(activity.shouldAutoUpload()){
								activity.uploadSilent();
							}
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
