package com.orf4450.frcscouter;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import com.shortcircuit.nbn.Nugget;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Caleb Milligan
 *         Created on 2/12/2016
 */
public abstract class ScouterDB extends SQLiteOpenHelper{
	public ScouterDB(Context context, String name, int version) {
		super(context, name, null, version);
	}

	public abstract void upload(OutputStream out) throws IOException;

	public abstract Nugget<?> toNugget();
}
