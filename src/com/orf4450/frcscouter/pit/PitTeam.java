package com.orf4450.frcscouter.pit;

import com.shortcircuit.nbn.nugget.NuggetCompound;
import com.shortcircuit.nbn.nugget.NuggetInteger;
import com.shortcircuit.nbn.nugget.NuggetString;

/**
 * @author ShortCircuit908
 *         Created on 2/19/2016
 */
public class PitTeam {
	private short team_number;
	private String team_name;
	private String robot_description;

	public PitTeam(NuggetCompound compound){
		team_number = (short)(int)((NuggetInteger)compound.getNugget("team_number")).getValue();
		team_name = ((NuggetString)compound.getNugget("team_name")).getValue();
		robot_description = ((NuggetString)compound.getNugget("robot_description")).getValue();
	}
}
