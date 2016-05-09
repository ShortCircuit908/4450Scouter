package com.orf4450.frcscouter;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import com.orf4450.frcscouter.db.ScouterDB;
import com.shortcircuit.nbn.Nugget;
import com.shortcircuit.nbn.nugget.NuggetCompound;

import java.io.DataOutputStream;
import java.util.UUID;
import java.util.zip.DeflaterOutputStream;

/**
 * Runnable for asynchronous uploading of collected data over Bluetooth
 *
 * @author Caleb Milligan
 *         Created on 1/14/2016
 */
public class UploadTask implements Runnable {
	private final BluetoothDevice device;
	private final UploadCallback callback;
	private ScouterDB[] databases;

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
			// Establish a Bluetooth connection
			BluetoothSocket socket = device.createRfcommSocketToServiceRecord(
					UUID.fromString(ScouterConstants.APP_UUID));
			socket.connect();
			// Set up the OutputStream
			DataOutputStream out = new DataOutputStream(new DeflaterOutputStream(socket.getOutputStream()));
			// Create a new nugget to contain accumulated data
			NuggetCompound nugget = new NuggetCompound("scouting_data");
			// Serialize each database's contents into the container
			for (ScouterDB database : databases) {
				nugget.addNugget(database.toNugget());
			}
			// Write the data
			Nugget.writeNugget(nugget, out);
			out.flush();
			// Close the connection
			out.close();
			socket.close();
		}
		catch (Throwable e) {
			thrown = e;
		}
		if (thrown != null) {
			thrown.printStackTrace();
		}
		// Run the callback
		callback.onUploadFinished(thrown);
	}
}
