package com.orf4450.frcscouter.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.orf4450.frcscouter.stand.MatchDescriptor;
import com.orf4450.scouter.R;
import com.shortcircuit.nbn.Nugget;
import com.shortcircuit.nbn.NuggetFactory;
import com.shortcircuit.nbn.nugget.*;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Caleb Milligan
 *         Created on 2/4/2016
 */
public class StandDB extends ScouterDB {
	private static final int DATABASE_VERSION = 1;
	public static final String SCOUTING_TABLE_NAME = "stand_scouting";
	private final ColumnBinder column_binder;

	public StandDB(Context context, ColumnBinder column_binder) {
		super(context, "com.orf4450.frcscouter.Scouter_DB", DATABASE_VERSION);
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

	public boolean saveMatch() {
		if (column_binder != null) {
			TextViewColumnBinding match_number = column_binder.get(R.id.match_number);
			TextViewColumnBinding team_number = column_binder.get(R.id.team_number);
			TextViewColumnBinding team_name = column_binder.get(R.id.team_name);
			boolean validated = true;
			if (match_number.getValue().toString().trim().isEmpty()) {
				match_number.setError("This field is required");
				validated = false;
			}
			else {
				match_number.setError(null);
			}
			if (team_name.getValue().toString().trim().isEmpty()) {
				team_name.setError("This field is required");
				validated = false;
			}
			else {
				team_name.setError(null);
			}
			if (team_number.getValue().toString().trim().isEmpty()) {
				team_number.setError("This field is required");
				validated = false;
			}
			else {
				team_number.setError(null);
			}
			if (!validated) {
				return false;
			}
			MatchDescriptor descriptor = new MatchDescriptor(
					Integer.parseInt(match_number.getValue() + ""),
					Integer.parseInt(team_number.getValue() + ""));
			deleteMatch(descriptor);
			SQLiteDatabase db = getWritableDatabase();
			column_binder.save(db, SCOUTING_TABLE_NAME);
			return true;
		}
		return false;
	}

	@Override
	public void onOpen(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public void loadMatch(MatchDescriptor descriptor) {
		if (column_binder != null) {
			HashMap<String, Object> search_params = new HashMap<>(2);
			search_params.put(column_binder.get(R.id.match_number).getColumnName(), descriptor.getMatchNumber());
			search_params.put(column_binder.get(R.id.team_number).getColumnName(), descriptor.getTeamNumber());
			int id = column_binder.queryRowsMatchingParameters(getReadableDatabase(), SCOUTING_TABLE_NAME, search_params)[0];
			column_binder.load(getReadableDatabase(), SCOUTING_TABLE_NAME, id);
		}
	}

	public void deleteAllData() {
		try {
			getWritableDatabase().execSQL("DELETE FROM `" + SCOUTING_TABLE_NAME + "`");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<MatchDescriptor> getAllStoredMatches() {
		ArrayList<MatchDescriptor> descriptors = new ArrayList<>(0);
		if (column_binder != null) {
			String match_number_column = column_binder.get(R.id.match_number).getColumnName();
			StringBuilder query_builder = new StringBuilder("SELECT `").append(match_number_column).append("`, `")
					.append(column_binder.get(R.id.team_number).getColumnName()).append("` FROM `").append(SCOUTING_TABLE_NAME)
					.append("` ORDER BY `").append(match_number_column).append("` ASC");
			SQLiteDatabase db = getReadableDatabase();
			Cursor cursor = db.rawQuery(query_builder.toString(), null);
			descriptors = new ArrayList<>(cursor.getCount());
			while (cursor.moveToNext()) {
				descriptors.add(new MatchDescriptor(cursor.getInt(0), cursor.getInt(1)));
			}
			cursor.close();
		}
		return descriptors;
	}

	public int getLastMatchNumber() {
		if (column_binder != null) {
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
		return 0;
	}

	public void upload(OutputStream out) throws IOException {
		try {
			Nugget<?> nugget = toNugget();
			Nugget.writeNugget(nugget, new DataOutputStream(out));
			getWritableDatabase().execSQL("UPDATE `" + SCOUTING_TABLE_NAME + "` SET `uploaded`=1");
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

	public void deleteMatch(MatchDescriptor descriptor) {
		if (column_binder != null) {
			String match_number = column_binder.get(R.id.match_number).getColumnName();
			String team_number = column_binder.get(R.id.team_number).getColumnName();
			Cursor cursor = getReadableDatabase().rawQuery("SELECT `id` FROM `" + SCOUTING_TABLE_NAME + "` WHERE `"
					+ match_number + "`=" + descriptor.getMatchNumber() + " AND `" + team_number
					+ "`=" + descriptor.getTeamNumber(), null);
			if (!cursor.moveToNext()) {
				return;
			}
			int id = cursor.getInt(0);
			getWritableDatabase().execSQL("DELETE FROM `" + SCOUTING_TABLE_NAME + "` WHERE `id`=?", new Object[]{id});
		}
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
