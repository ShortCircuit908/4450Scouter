package com.orf4450.frcscouter.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Environment;
import com.orf4450.scouter.R;
import com.shortcircuit.nbn.Nugget;
import com.shortcircuit.nbn.nugget.NuggetArray;
import com.shortcircuit.nbn.nugget.NuggetCompound;
import com.shortcircuit.nbn.nugget.NuggetInteger;
import com.shortcircuit.nbn.nugget.NuggetString;

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

	public boolean save() {
		if(column_binder != null) {
			TextViewColumnBinding team_number = column_binder.get(R.id.pit_team_number);
			TextViewColumnBinding team_name = column_binder.get(R.id.pit_team_name);
			boolean validated = true;
			if (team_number.getValue().toString().trim().isEmpty()) {
				team_number.setError("This field is required");
				validated = false;
			}
			else {
				team_number.setError(null);
			}
			if (team_name.getValue().toString().trim().isEmpty()) {
				team_name.setError("This field is required");
				validated = false;
			}
			else {
				team_name.setError(null);
			}
			if (!validated) {
				return false;
			}
			SQLiteDatabase db = getWritableDatabase();
			db.execSQL("DELETE FROM `" + SCOUTING_TABLE_NAME + "` WHERE `team_number`=?", new Object[]{Integer.parseInt(team_number.getValue() + "")});
			column_binder.save(db, SCOUTING_TABLE_NAME);
			return true;
		}
		return false;
	}

	public void deleteAllData() {
		if(column_binder != null) {
			getWritableDatabase().execSQL("DELETE FROM `" + SCOUTING_TABLE_NAME + "`");
			File storage_dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
			for (File file : storage_dir.listFiles()) {
				if (file.exists() && file.getName().matches("ROBOT_\\d+.jpg\n")) {
					file.delete();
				}
			}
		}
	}

	@Override
	public void upload(OutputStream out) throws IOException {
		if(column_binder != null) {
			Nugget<?> nugget = toNugget();
			Nugget.writeNugget(nugget, new DataOutputStream(out));
			getWritableDatabase().execSQL("UPDATE `" + SCOUTING_TABLE_NAME + "` SET `uploaded`=1");
		}
	}

	@Override
	public Nugget<?> toNugget() {
		NuggetCompound[] compounds = new NuggetCompound[0];
		if(column_binder != null) {
			try {
				SQLiteDatabase db = getReadableDatabase();
				Cursor cursor = db.rawQuery("SELECT * FROM `" + SCOUTING_TABLE_NAME + "` WHERE `uploaded`=0", null);
				compounds = new NuggetCompound[cursor.getCount()];
				if (cursor.getCount() > 0) {
					int i = 0;
					while (cursor.moveToNext()) {
						int team_number = cursor.getInt(1);
						NuggetCompound compound = new NuggetCompound();
						compound.addNugget(new NuggetInteger("team_number", team_number));
						compound.addNugget(new NuggetString("team_name", cursor.getString(2)));
						compound.addNugget(new NuggetString("robot_description", cursor.getString(3)));
					/*
					File image_file = PitScouting.getImageFile(team_number);
					if(image_file.exists()){
						compound.addNugget(new NuggetFile("image", image_file));
					}
					*/
						compounds[i++] = compound;
					}
				}
				cursor.close();
			}
			catch (SQLiteException e) {
				// Do nothing;
			}
		}
		return new NuggetArray<>(SCOUTING_TABLE_NAME, compounds);
	}

	public void resetUploaded() {
		if(column_binder != null) {
			getWritableDatabase().execSQL("UPDATE `" + SCOUTING_TABLE_NAME + "` SET `uploaded`=0");
		}
	}
}
