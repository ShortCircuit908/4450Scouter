package com.orf4450.frcscouter.master;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.orf4450.frcscouter.pit.PitTeam;
import com.orf4450.frcscouter.stand.Match;
import com.shortcircuit.nbn.Nugget;
import com.shortcircuit.nbn.nugget.NuggetArray;
import com.shortcircuit.nbn.nugget.NuggetCompound;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author Caleb Milligan
 *         Created on 2/17/2016
 */
public class MasterDB {
	private static final Gson gson = new GsonBuilder().serializeNulls().enableComplexMapKeySerialization().create();
	private static final Type bundle_type = new TypeToken<DataBundle>() {
	}.getType();
	private long file_id = 0;
	private final File data_dir;

	public MasterDB(Context context) {
		data_dir = new File(context.getApplicationInfo().dataDir);
	}

	public void upload(OutputStream out) throws IOException {
		DataBundle bundle = new DataBundle();
		for (File file : data_dir.listFiles()) {
			try {
				NuggetArray<List<Nugget<?>>, NuggetCompound> nugget = Nugget.readNugget(new DataInputStream(new FileInputStream(file)));
				if (nugget.getName().equals("stand_scouting")) {
					for (NuggetCompound compound : nugget.getValue()) {
						bundle.addMatch(new Match(compound));
					}
				}
				else {
					for (NuggetCompound compound : nugget.getValue()) {
						bundle.addTeam(new PitTeam(compound));
					}
				}
				file.delete();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		PrintStream print = new PrintStream(out);
		gson.toJson(bundle, bundle_type, print);
		print.flush();
		print.close();
	}

	public void deleteAllData() {
		for (File file : data_dir.listFiles()) {
			file.delete();
		}
	}

	public void saveNugget(Nugget<?> nugget) throws IOException {
		if (nugget == null) {
			return;
		}
		File file = getNextAvailableFile();
		file.createNewFile();
		Nugget.writeNugget(nugget, new DataOutputStream(new FileOutputStream(file)));
	}

	private File getNextAvailableFile() {
		File file;
		while ((file = new File(data_dir + "\\data_" + file_id++ + ".nbn")).exists()) {
			// Do nothing
		}
		return file;
	}
}
