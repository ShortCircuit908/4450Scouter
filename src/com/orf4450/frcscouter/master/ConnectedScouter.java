package com.orf4450.frcscouter.master;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.widget.ArrayAdapter;
import com.shortcircuit.nbn.Nugget;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.zip.InflaterInputStream;

/**
 * @author Caleb Milligan
 *         Created on 1/14/2016
 */
public class ConnectedScouter implements Runnable {
	private final Activity context;
	private final BluetoothSocket socket;
	private final ScouterCallback callback;
	private final DataInputStream in;
	private final RemoteDeviceWrapper device_wrapper;
	private final ArrayAdapter<RemoteDeviceWrapper> list_adapter;

	public ConnectedScouter(Activity context, BluetoothSocket socket, ScouterCallback callback, final ArrayAdapter<RemoteDeviceWrapper> list_adapter) throws IOException {
		this.context = context;
		this.socket = socket;
		this.callback = callback;
		this.list_adapter = list_adapter;
		this.in = new DataInputStream(new InflaterInputStream(socket.getInputStream()));
		this.device_wrapper = new RemoteDeviceWrapper(socket.getRemoteDevice());
		context.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				synchronized (list_adapter) {
					list_adapter.add(device_wrapper);
				}
			}
		});
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
				context.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						synchronized (list_adapter) {
							list_adapter.remove(device_wrapper);
						}
					}
				});
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
