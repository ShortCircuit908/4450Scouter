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
import java.util.List;

/**
 * @author Caleb Milligan
 *         Created on 2/4/2016
 */
public class StandDB extends ScouterDB {
	private static final int DATABASE_VERSION = 1;
	public static final String SCOUTING_TABLE_NAME = "scouting";
	private final ColumnBinder column_binder;

	public StandDB(Context context, ColumnBinder column_binder) {
		super(context, "com.orf4450.frcscouter.Scouter_DB", DATABASE_VERSION);
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

	public void saveMatch() {
		TextViewColumnBinding match_number = column_binder.get(R.id.match_number);
		TextViewColumnBinding team_number = column_binder.get(R.id.team_number);
		boolean validated = true;
		if(match_number.getValue().toString().trim().isEmpty()){
			match_number.setError("This field is required");
			validated = false;
		}
		else {
			match_number.setError(null);
		}
		if(team_number.getValue().toString().trim().isEmpty()){
			team_number.setError("This field is required");
			validated = false;
		}
		else{
			team_number.setError(null);
		}
		if(!validated){
			return;
		}
		MatchDescriptor descriptor = new MatchDescriptor(
				Integer.parseInt(match_number.getValue() + ""),
				Integer.parseInt(team_number.getValue() + ""));
		deleteMatch(descriptor);
		column_binder.save(getWritableDatabase(), SCOUTING_TABLE_NAME);
		HashMap<String, Object> search_params = new HashMap<>(2);
		search_params.put(column_binder.get(R.id.match_number).getColumnName(), descriptor.getMatchNumber());
		search_params.put(column_binder.get(R.id.team_number).getColumnName(), descriptor.getTeamNumber());
		int id = column_binder.queryRowsMatchingParameters(getReadableDatabase(), SCOUTING_TABLE_NAME, search_params)[0];
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("INSERT INTO `upload_status` (`_id`, `uploaded`) VALUES (?, ?)", new Object[]{id, 0});
	}

	@Override
	public void onOpen(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public void loadMatch(MatchDescriptor descriptor) {
		HashMap<String, Object> search_params = new HashMap<>(2);
		search_params.put(column_binder.get(R.id.match_number).getColumnName(), descriptor.getMatchNumber());
		search_params.put(column_binder.get(R.id.team_number).getColumnName(), descriptor.getTeamNumber());
		int id = column_binder.queryRowsMatchingParameters(getReadableDatabase(), SCOUTING_TABLE_NAME, search_params)[0];
		column_binder.load(getReadableDatabase(), SCOUTING_TABLE_NAME, id);
	}

	public void deleteAllData() {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("DELETE FROM `" + SCOUTING_TABLE_NAME + "`");
		db.execSQL("DELETE FROM `upload_status`");
	}

	public ArrayList<MatchDescriptor> getAllStoredMatches() {
		String match_number_column = column_binder.get(R.id.match_number).getColumnName();
		StringBuilder query_builder = new StringBuilder("SELECT `").append(match_number_column).append("`, `")
				.append(column_binder.get(R.id.team_number).getColumnName()).append("` FROM `").append(SCOUTING_TABLE_NAME)
				.append("` ORDER BY `").append(match_number_column).append("` ASC");
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(query_builder.toString(), null);
		ArrayList<MatchDescriptor> descriptors = new ArrayList<>(cursor.getCount());
		while (cursor.moveToNext()) {
			descriptors.add(new MatchDescriptor(cursor.getInt(0), cursor.getInt(1)));
		}
		cursor.close();
		return descriptors;
	}

	public ArrayList<MatchDescriptor> getAllStoredMatches(boolean uploaded) {
		String match_number_column = column_binder.get(R.id.match_number).getColumnName();
		StringBuilder query_builder = new StringBuilder("SELECT `").append(SCOUTING_TABLE_NAME).append("`.`")
				.append(match_number_column).append("`, `").append(SCOUTING_TABLE_NAME)
				.append("`.`").append(column_binder.get(R.id.team_number)).append("` FROM `").append(SCOUTING_TABLE_NAME)
				.append("` WHERE `upload_status`.`uploaded`=").append(uploaded ? "1" : "0").append(" INNER JOIN `upload_status` ")
				.append("ON `upload_status`.`_id`=`").append(SCOUTING_TABLE_NAME).append("`.`_id` ORDER BY `")
				.append(SCOUTING_TABLE_NAME).append("`.`").append(match_number_column).append("` ASC");
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(query_builder.toString(), null);
		ArrayList<MatchDescriptor> descriptors = new ArrayList<>(cursor.getCount());
		while (cursor.moveToNext()) {
			descriptors.add(new MatchDescriptor(cursor.getInt(0), cursor.getInt(1)));
		}
		cursor.close();
		return descriptors;
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

	public void upload(OutputStream out) throws IOException {
		Nugget<?> nugget = toNugget();
		Nugget.writeNugget(nugget, new DataOutputStream(out));
		getWritableDatabase().execSQL("UPDATE `upload_status` SET `uploaded`=1");
	}

	@Override
	public Nugget<?> toNugget() {
		StringBuilder query_builder = new StringBuilder("SELECT `*` FROM `").append(SCOUTING_TABLE_NAME)
				.append("` WHERE `upload_status`.`uploaded`=0 INNER JOIN `upload_status` ")
				.append("ON `upload_status`.`_id`=`").append(SCOUTING_TABLE_NAME).append("`.`_id`");
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(query_builder.toString(), null);
		NuggetCompound[] compounds = new NuggetCompound[cursor.getCount()];
		int i = 0;
		while (cursor.moveToNext()) {
			NuggetCompound compound = new NuggetCompound();
			for (int j = 0; j < cursor.getColumnCount(); j++) {
				String name = cursor.getColumnName(j);
				switch (cursor.getType(j)) {
					case Cursor.FIELD_TYPE_FLOAT:
						compound.addNugget(new NuggetDouble(name, cursor.getDouble(j)));
						continue;
					case Cursor.FIELD_TYPE_INTEGER:
						compound.addNugget(new NuggetLong(name, cursor.getLong(j)));
						continue;
					case Cursor.FIELD_TYPE_NULL:
						compound.addNugget(new NuggetString(name));
						continue;
					case Cursor.FIELD_TYPE_STRING:
						compound.addNugget(new NuggetString(name, cursor.getString(j)));
						continue;
					case Cursor.FIELD_TYPE_BLOB:
						try {
							compound.addNugget(NuggetFactory.toNugget(name, cursor.getBlob(j), byte[].class));
						}
						catch (IllegalAccessException e) {
							e.printStackTrace();
						}
						catch (InstantiationException e) {
							e.printStackTrace();
						}
				}
			}
			compounds[i++] = compound;
		}
		cursor.close();
		NuggetArray<List<Nugget<?>>, NuggetCompound> outer_nugget = new NuggetArray<>("stand_scouting", compounds);
		return outer_nugget;
	}

	public void deleteMatch(MatchDescriptor descriptor) {
		String match_number = column_binder.get(R.id.match_number).getColumnName();
		String team_number = column_binder.get(R.id.team_number).getColumnName();
		Cursor cursor = getReadableDatabase().rawQuery("SELECT `_id` FROM `" + SCOUTING_TABLE_NAME + "` WHERE `"
				+ match_number + "`=" + descriptor.getMatchNumber() + " AND `" + team_number
				+ "`=" + descriptor.getTeamNumber(), null);
		if (!cursor.moveToNext()) {
			return;
		}
		int id = cursor.getInt(0);
		getWritableDatabase().execSQL("DELETE FROM `" + SCOUTING_TABLE_NAME + "` WHERE `_id`=?", new Object[]{id});
		getWritableDatabase().execSQL("DELETE FROM `upload_status` WHERE `_id`=?", new Object[]{id});
	}

	public void resetUploaded() {
		getWritableDatabase().execSQL("UPDATE `upload_status` SET `uploaded`=0");
	}
}
