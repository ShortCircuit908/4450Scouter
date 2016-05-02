package com.orf4450.frcscouter.pit;

import com.shortcircuit.nbn.nugget.NuggetCompound;
import com.shortcircuit.nbn.nugget.NuggetShort;
import com.shortcircuit.nbn.nugget.NuggetString;

/**
 * @author Caleb Milligan
 *         Created on 2/19/2016
 */
public class PitTeam {
	private short team_number;
	private String team_name;
	private String robot_description;
	private String auto_notes;
	private String defense_notes;
	private String drive_base_notes;
	private String pickup_notes;
	private String shooting_notes;

	public PitTeam(NuggetCompound compound){
		team_number = ((NuggetShort)compound.getNugget("team_number")).getValue();
		team_name = ((NuggetString)compound.getNugget("team_name")).getValue();
		robot_description = ((NuggetString)compound.getNugget("robot_description")).getValue();
		auto_notes = ((NuggetString)compound.getNugget("auto_notes")).getValue();
		defense_notes = ((NuggetString)compound.getNugget("defense_notes")).getValue();
		drive_base_notes = ((NuggetString)compound.getNugget("drive_base_notes")).getValue();
		pickup_notes = ((NuggetString)compound.getNugget("pickup_notes")).getValue();
		shooting_notes = ((NuggetString)compound.getNugget("shooting_notes")).getValue();
	}
}
