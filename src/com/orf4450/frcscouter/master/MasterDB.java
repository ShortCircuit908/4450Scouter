package com.orf4450.frcscouter.master;

import android.content.Context;
import com.shortcircuit.nbn.Nugget;

import java.io.*;

/**
 * @author ShortCircuit908
 *         Created on 2/17/2016
 */
public class MasterDB {
	private final File data_dir;
	private long file_id = 0;

	public MasterDB(Context context) {
		data_dir = new File(context.getApplicationInfo().dataDir);
	}

	public void upload(OutputStream out) {
		for (File file : data_dir.listFiles()) {
			if (!file.exists() || file.length() == 0) {
				file.delete();
				continue;
			}
			try {
				Nugget.writeNugget(Nugget.readNugget(new DataInputStream(new FileInputStream(file))), new DataOutputStream(out));
				file.delete();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void deleteAllData(){

	}

	public void saveNugget(/*NuggetArray<List<Nugget<?>>, NuggetCompound> nugget*/ Nugget<?> nugget) throws IOException {
		File file = getNextAvailableFile();
		file.createNewFile();
		Nugget.writeNugget(nugget, new DataOutputStream(new FileOutputStream(file)));
		/*
		if(nugget.getValue() == null){
			return;
		}
		if(nugget.getValue().length == 0){
			return;
		}
		NuggetCompound first_compound = nugget.getValue()[0];
		StringBuilder query_builder = new StringBuilder("INSERT INTO `").append(table_name)
				.append("` (");
		for(String name : first_compound.getNuggetNames()){
			query_builder.append('`').append(name).append("`, ");
		}
		query_builder.delete(query_builder.length() - 2, query_builder.length());
		query_builder.append(") VALUES (");
		Object[] bindargs = new Object[nugget.getValue().length * first_compound.getSize()];
		for(int i = 0; i < bindargs.length; i++){
			NuggetCompound compound = nugget.getValue()[i];
			if(compound.getValue() == null || compound.getValue().isEmpty()){
				continue;
			}
			query_builder.append('(');
			for(int j = 0; j < compound.getSize(); j++){
				query_builder.append("?, ");
				bindargs[i * compound.getSize() + j] = compound.getNugget(j);
			}
			query_builder.delete(query_builder.length() - 2, query_builder.length());
			query_builder.append("), ");
		}
		query_builder.delete(query_builder.length() - 2, query_builder.length());
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(query_builder.toString(), bindargs);
		*/
	}

	private File getNextAvailableFile() {
		File file;
		while ((file = new File(data_dir + "\\data_" + file_id++ + ".nbn")).exists()) {
			// Do nothing
		}
		return file;
	}
}
