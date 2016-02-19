package com.orf4450.frcscouter.pit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import com.shortcircuit.nbn.nugget.NuggetCompound;
import com.shortcircuit.nbn.nugget.NuggetFile;
import com.shortcircuit.nbn.nugget.NuggetLong;
import com.shortcircuit.nbn.nugget.NuggetString;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * @author ShortCircuit908
 *         Created on 2/19/2016
 */
public class PitTeam {
	private short team_number;
	private String team_name;
	private String robot_description;
	private String image_base64;

	public PitTeam(NuggetCompound compound){
		team_number = (short)(long)((NuggetLong)compound.getNugget("team_number")).getValue();
		team_name = ((NuggetString)compound.getNugget("team_name")).getValue();
		robot_description = ((NuggetString)compound.getNugget("robot_description")).getValue();
		setImageFile(((NuggetFile)compound.getNugget("robot_image")).getValue());
	}

	public PitTeam setImageFile(File file) {
		Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] b = baos.toByteArray();
		image_base64 = Base64.encodeToString(b, Base64.DEFAULT);
		return this;
	}
}
