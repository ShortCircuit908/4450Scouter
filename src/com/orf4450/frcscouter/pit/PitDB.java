package com.orf4450.frcscouter.pit;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import com.orf4450.frcscouter.ScouterDB;
import com.orf4450.frcscouter.db.ColumnBinder;
import com.orf4450.frcscouter.db.DummyColumnBinding;
import com.orf4450.frcscouter.db.TextViewColumnBinding;
import com.orf4450.scouter.R;
import com.shortcircuit.nbn.Nugget;
import com.shortcircuit.nbn.NuggetFactory;
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
	public static final String SCOUTING_TABLE_NAME = "pit_scouting";
	private static final int DATABASE_VERSION = 1;
	private final ColumnBinder column_binder;

	public PitDB(Context context, ColumnBinder column_binder) {
		super(context, "com.orf4450.frcscouter.Pit_Scouter_DB", DATABASE_VERSION);
		this.column_binder = column_binder;
		if (column_binder != null) {
			column_binder.add(new DummyColumnBinding<>("uploaded", Integer.class, "TINYINT", 1, false, 0));
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (column_binder != null) {
			String asb = column_binder.generateSchema(SCOUTING_TABLE_NAME);
			System.out.println(asb);
			for (String abs : asb.split(";")) {
				db.execSQL(abs);
			}
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public boolean save() {
		if (column_binder != null) {
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
		try {
			getWritableDatabase().execSQL("DELETE FROM `" + SCOUTING_TABLE_NAME + "`");
			File storage_dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
			for (File file : storage_dir.listFiles()) {
				if (file.exists() && file.getName().matches("ROBOT_\\d+.jpg\n")) {
					file.delete();
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void upload(OutputStream out) throws IOException {
		try {
			Nugget<?> nugget = toNugget();
			Nugget.writeNugget(nugget, new DataOutputStream(out));
			setUploaded();
			deleteAllData();
		}
		catch (IOException e) {
			throw e;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setUploaded() {
		try {
			getWritableDatabase().execSQL("UPDATE `" + SCOUTING_TABLE_NAME + "` SET `uploaded`=1");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Nugget<?> toNugget() {
		NuggetCompound[] compounds = new NuggetCompound[0];
		try {
			StringBuilder query_builder = new StringBuilder("SELECT * FROM `").append(SCOUTING_TABLE_NAME)
					.append("` WHERE `uploaded`=0");
			SQLiteDatabase db = getReadableDatabase();
			Cursor cursor = db.rawQuery(query_builder.toString(), null);
			compounds = new NuggetCompound[cursor.getCount()];
			if (cursor.getCount() > 0) {
				int i = 0;
				while (cursor.moveToNext()) {
					NuggetCompound compound = new NuggetCompound();
					for (int j = 1; j < cursor.getColumnCount(); j++) {
						String name = cursor.getColumnName(j);
						switch (cursor.getType(j)) {
							case Cursor.FIELD_TYPE_FLOAT:
								compound.addNugget(new NuggetDouble(name, cursor.getDouble(j)));
								continue;
							case Cursor.FIELD_TYPE_INTEGER:
								compound.addNugget(new NuggetShort(name, cursor.getShort(j)));
								continue;
							case Cursor.FIELD_TYPE_NULL:
								compound.addNugget(new NuggetString(name));
								continue;
							case Cursor.FIELD_TYPE_STRING:
								compound.addNugget(new NuggetString(name, cursor.getString(j)));
								continue;
							case Cursor.FIELD_TYPE_BLOB:
								compound.addNugget(NuggetFactory.wrapNuggetArray(cursor.getBlob(j)));
						}
					}
					File image_file = PitScouting.getImageFile(compound.getNugget("team_number").getValue());
					if (image_file.exists()) {
						compound.addNugget(new NuggetFile("image", image_file));
					}
					compounds[i++] = compound;
				}
			}
			cursor.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return new NuggetArray<>(SCOUTING_TABLE_NAME, compounds);
	}

	public void resetUploaded() {
		try {
			getWritableDatabase().execSQL("UPDATE `" + SCOUTING_TABLE_NAME + "` SET `uploaded`=0");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
