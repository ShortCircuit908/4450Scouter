package com.orf4450.frcscouter.master;

import android.content.Context;
import android.os.Environment;
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
	private final Context context;

	public MasterDB(Context context) {
		data_dir = new File(Environment.getExternalStorageDirectory() + "/scouting_data");
		data_dir.mkdirs();
		this.context = context;
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
					for (final NuggetCompound compound : nugget.getValue()) {
						bundle.addTeam(new PitTeam(compound));
						/*
						NuggetFile image_nugget = (NuggetFile) compound.removeNugget("image");
						if (image_nugget != null) {
							final File image_file = image_nugget.getValue();
							final String file_name = image_nugget.getFileName();
							if (image_file.exists()) {
								UploadScheduler.scheduleTask(new ImageUploadTask(new UploadTask.Callback() {
									@Override
									public void onUploadFinished(Throwable e) {
										if (e == null) {
											Toast.makeText(context, "Uploaded " + file_name, Toast.LENGTH_SHORT).show();
											image_file.delete();
										}
										else {
											Toast.makeText(context, "Failed to upload " + file_name, Toast.LENGTH_SHORT).show();
											e.printStackTrace();
										}
									}
								}, image_file, file_name));
							}
						}
						*/
					}
				}
			}
			catch (IOException e) {
				e.printStackTrace();
				throw e;
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
		System.out.println(nugget);
		File file = getNextAvailableFile();
		file.createNewFile();
		Nugget.writeNugget(nugget, new DataOutputStream(new FileOutputStream(file)));
	}

	private File getNextAvailableFile() {
		File file;
		while ((file = new File(data_dir + "/data_" + file_id++ + ".nbn")).exists()) {
			// Do nothing
		}
		return file;
	}
}
