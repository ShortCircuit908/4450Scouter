package com.orf4450.frcscouter.master;

import android.bluetooth.BluetoothDevice;

/**
 * @author ShortCircuit908
 *         Created on 1/14/2016
 */
public class RemoteDeviceWrapper {
	private final BluetoothDevice device;

	public RemoteDeviceWrapper(BluetoothDevice device) {
		this.device = device;
	}

	public BluetoothDevice getDevice() {
		return device;
	}

	@Override
	public String toString() {
		return device.getName() + " (" + device.getAddress() + ")";
	}

	@Override
	public boolean equals(Object o) {
		return !(o == null || !(o instanceof RemoteDeviceWrapper))
				&& ((RemoteDeviceWrapper) o).device.equals(device);
	}
}
