package com.orf4450.frcscouter.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import com.orf4450.scouter.R;
import com.shortcircuit.nbn.Nugget;
import com.shortcircuit.nbn.nugget.*;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

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
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String asb = column_binder.generateSchema(SCOUTING_TABLE_NAME);
		for (String abs : asb.split(";")) {
			db.execSQL(abs);
		}
		db.execSQL("DROP TABLE IF EXISTS `upload_status`");
		db.execSQL("CREATE TABLE `upload_status` (`_id` INTEGER PRIMARY KEY, `uploaded` INT(1) NOT NULL DEFAULT 0)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public void save(){
		getWritableDatabase().execSQL("DELETE FROM `" + SCOUTING_TABLE_NAME + "`");
	}

	@Override
	public void upload(OutputStream out) throws IOException {
		Nugget<?> nugget = toNugget();
		Nugget.writeNugget(nugget, new DataOutputStream(out));
		getWritableDatabase().execSQL("UPDATE `upload_status` SET `uploaded`=1");
	}

	@Override
	public Nugget<?> toNugget() {
		StringBuilder query_builder = new StringBuilder("SELECT `")
				.append(column_binder.get(R.id.pit_team_number).getColumnName()).append("`, `")
				.append(column_binder.get(R.id.pit_team_name).getColumnName()).append("` FROM `")
				.append(SCOUTING_TABLE_NAME).append('`');
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(query_builder.toString(), null);
		NuggetCompound[] compounds = new NuggetCompound[cursor.getCount()];
		int i = 0;
		while (cursor.moveToNext()) {
			NuggetCompound compound = new NuggetCompound();
			compound.addNugget(new NuggetInteger("team_number", cursor.getInt(0)));
			compound.addNugget(new NuggetString("team_name", cursor.getString(1)));
			String file_name = "ROBOT_" + cursor.getInt(0) + ".jpg";
			File storage_dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
			compound.addNugget(new NuggetFile("robot_image", new File(storage_dir, file_name)));
			compounds[i++] = compound;
		}
		cursor.close();
		NuggetArray<List<Nugget<?>>, NuggetCompound> outer_nugget = new NuggetArray<>("pit_scouting", compounds);
		return outer_nugget;
	}

	public void resetUploaded() {
		getWritableDatabase().execSQL("UPDATE `upload_status` SET `uploaded`=0");
	}
}
