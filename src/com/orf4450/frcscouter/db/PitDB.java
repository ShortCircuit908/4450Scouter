package com.orf4450.frcscouter.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Environment;
import com.orf4450.scouter.R;
import com.shortcircuit.nbn.Nugget;
import com.shortcircuit.nbn.nugget.*;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Caleb Milligan
 *         Created on 2/11/2016
 */
public class PitDB extends ScouterDB {
	private static final int DATABASE_VERSION = 1;
	public static final String SCOUTING_TABLE_NAME = "pit_scouting";
	private final ColumnBinder column_binder;

	public PitDB(Context context, ColumnBinder column_binder) {
		super(context, "com.orf4450.frcscouter.Pit_Scouter_DB", DATABASE_VERSION);
		this.column_binder = column_binder;
		if(column_binder != null) {
			column_binder.add(new DummyColumnBinding<>("uploaded", Integer.class, "TINYINT", 1, false, 0));
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if(column_binder != null) {
			String asb = column_binder.generateSchema(SCOUTING_TABLE_NAME);
			for (String abs : asb.split(";")) {
				db.execSQL(abs);
			}
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public void save() {
		TextViewColumnBinding team_number = column_binder.get(R.id.pit_team_number);
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("DELETE FROM `" + SCOUTING_TABLE_NAME + "` WHERE `team_number`=?", new Object[]{Integer.parseInt(team_number.getValue() + "")});
		column_binder.save(db, SCOUTING_TABLE_NAME);
	}

	public void deleteAllData() {
		getWritableDatabase().execSQL("DELETE FROM `" + SCOUTING_TABLE_NAME + "`");
		File storage_dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		for (File file : storage_dir.listFiles()) {
			if (file.exists() && file.getName().matches("ROBOT_\\d+.jpg\n")) {
				file.delete();
			}
		}
	}

	@Override
	public void upload(OutputStream out) throws IOException {
		Nugget<?> nugget = toNugget();
		Nugget.writeNugget(nugget, new DataOutputStream(out));
		getWritableDatabase().execSQL("UPDATE `" + SCOUTING_TABLE_NAME + "` SET `uploaded`=1");
	}

	@Override
	public Nugget<?> toNugget() {
		NuggetCompound[] compounds = new NuggetCompound[0];
		try {
			SQLiteDatabase db = getReadableDatabase();
			Cursor cursor = db.rawQuery("SELECT * FROM `" + SCOUTING_TABLE_NAME + "` WHERE `uploaded`=0", null);
			compounds = new NuggetCompound[cursor.getCount()];
			if (cursor.getCount() > 0) {
				int i = 0;
				while (cursor.moveToNext()) {
					NuggetCompound compound = new NuggetCompound();
					compound.addNugget(new NuggetInteger("team_number", cursor.getInt(1)));
					compound.addNugget(new NuggetString("team_name", cursor.getString(2)));
					compound.addNugget(new NuggetString("robot_description", cursor.getString(3)));
					String file_name = "ROBOT_" + cursor.getInt(1) + ".jpg";
					File storage_dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
					compound.addNugget(new NuggetFile("robot_image", new File(storage_dir, file_name)));
					compounds[i++] = compound;
				}
			}
			cursor.close();
		}
		catch (SQLiteException e){
			// Do nothing;
		}
		return new NuggetArray<>(SCOUTING_TABLE_NAME, compounds);
	}

	public void resetUploaded() {
		getWritableDatabase().execSQL("UPDATE `" + SCOUTING_TABLE_NAME + "` SET `uploaded`=0");
	}
}
