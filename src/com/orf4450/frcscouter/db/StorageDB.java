package com.orf4450.frcscouter.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.orf4450.scouter.R;

/**
 * @author ShortCircuit908
 *         Created on 2/4/2016
 */
public class StorageDB extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	public static final String SCOUTING_TABLE_NAME = "scouting";
	private final ColumnBinder column_binder;

	public StorageDB(Context context, ColumnBinder column_binder) {
		super(context, "com.orf4450.frcscouter.Scouter_DB", null, DATABASE_VERSION);
		this.column_binder = column_binder;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String asb = column_binder.generateSchema(SCOUTING_TABLE_NAME);
		for(String abs : asb.split(";")) {
			db.execSQL(abs);
		}
	}

	@Override
	public void onOpen(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public int getLastMatchNumber() {
		String match_number_column = column_binder.get(R.id.match_number).getColumnName();
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT `" + match_number_column + "` FROM `" + SCOUTING_TABLE_NAME
				+ "` ORDER BY `" + match_number_column + "` DESC LIMIT 1", null);
		int last = 0;
		if (cursor.moveToNext()) {
			last = cursor.getInt(0);
		}
		cursor.close();
		return last;
	}
}
