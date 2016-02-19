package com.orf4450.frcscouter;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import com.orf4450.frcscouter.db.ScouterDB;
import com.shortcircuit.nbn.Nugget;
import com.shortcircuit.nbn.nugget.NuggetCompound;

import java.io.DataOutputStream;
import java.util.UUID;

/**
 * @author Caleb Milligan
 *         Created on 1/14/2016
 */
public class UploadTask implements Runnable {
	private ScouterDB[] databases;
	private final BluetoothDevice device;
	private final UploadCallback callback;

	public UploadTask(BluetoothDevice device, UploadCallback callback, ScouterDB... databases) {
		this.databases = databases;
		this.device = device;
		this.callback = callback;
		new Thread(this).start();
	}

	@Override
	public void run() {
		Throwable thrown = null;
		try {
			BluetoothSocket socket = device.createRfcommSocketToServiceRecord(
					UUID.fromString("7674047e-6e47-4bf0-831f-209e3f9dd23f"));
			socket.connect();
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			NuggetCompound nugget = new NuggetCompound("scouting_data");
			for(ScouterDB database : databases) {
				nugget.addNugget(database.toNugget());
			}
			Nugget.writeNugget(nugget, out);
			out.flush();
			out.close();
			socket.close();
		}
		catch (Throwable e) {
			thrown = e;
		}
		if(thrown != null){
			thrown.printStackTrace();
		}
		callback.onUploadFinished(thrown);
	}
}
