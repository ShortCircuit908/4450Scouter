package com.orf4450.frcscouter.master;

import com.shortcircuit.nbn.Nugget;

/**
 * @author ShortCircuit908
 *         Created on 1/14/2016
 */
public interface ScouterCallback {
	void onDataRecieved(Nugget<?> data);
}
