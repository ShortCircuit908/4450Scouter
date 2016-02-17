package com.orf4450.frcscouter.master;

import android.bluetooth.BluetoothSocket;
import android.widget.ArrayAdapter;
import com.shortcircuit.nbn.Nugget;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author Caleb Milligan
 *         Created on 1/14/2016
 */
public class ConnectedScouter implements Runnable {
	private final BluetoothSocket socket;
	private final ScouterCallback callback;
	private final DataInputStream in;
	private final RemoteDeviceWrapper device_wrapper;
	private final ArrayAdapter<RemoteDeviceWrapper> list_adapter;

	public ConnectedScouter(BluetoothSocket socket, ScouterCallback callback, ArrayAdapter<RemoteDeviceWrapper> list_adapter) throws IOException {
		this.socket = socket;
		this.callback = callback;
		this.list_adapter = list_adapter;
		this.in = new DataInputStream(socket.getInputStream());
		synchronized (this.list_adapter) {
			list_adapter.add(this.device_wrapper = new RemoteDeviceWrapper(socket.getRemoteDevice()));
		}
		new Thread(this).start();
	}

	@Override
	@SuppressWarnings("unchecked")
	public void run() {
		while (true) {
			try {
				if (in.available() > 0) {
					Nugget<?> nugget = Nugget.readNugget(in);
					callback.onDataRecieved(nugget);
					close();
					return;
				}
			}
			catch (IOException e) {
				close();
				return;
			}
		}
	}

	public void close() {
		if (socket.isConnected()) {
			try {
				in.close();
				socket.close();
				synchronized (list_adapter) {
					list_adapter.remove(device_wrapper);
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
