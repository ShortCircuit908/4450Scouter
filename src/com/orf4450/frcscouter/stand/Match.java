package com.orf4450.frcscouter.stand;

import com.shortcircuit.nbn.nugget.NuggetCompound;
import com.shortcircuit.nbn.nugget.NuggetShort;
import com.shortcircuit.nbn.nugget.NuggetString;

/**
 * @author ShortCircuit908
 *         Created on 2/19/2016
 */
public class Match {
	private short match_number;
	private short team_number;
	private String team_name;
	private String autonomous_behavior;
	private byte pickup_speed;
	private byte portcullis_speed;
	private byte chival_speed;
	private byte moat_speed;
	private byte ramparts_speed;
	private byte drawbridge_speed;
	private byte sally_speed;
	private byte rock_speed;
	private byte rough_speed;
	private byte low_speed;
	private byte high_goals;
	private byte low_goals;
	private byte endgame;

	public Match(NuggetCompound compound){
		match_number = ((NuggetShort)compound.getNugget("match_number")).getValue();
		team_number = ((NuggetShort)compound.getNugget("team_number")).getValue();
		team_name = ((NuggetString)compound.getNugget("team_name")).getValue();
		autonomous_behavior = ((NuggetString)compound.getNugget("autonomous_behavior")).getValue();
		pickup_speed = (byte)(short)((NuggetShort)compound.getNugget("pickup_speed")).getValue();
		portcullis_speed = (byte)(short)((NuggetShort)compound.getNugget("portcullis_speed")).getValue();
		chival_speed = (byte)(short)((NuggetShort)compound.getNugget("chival_speed")).getValue();
		moat_speed = (byte)(short)((NuggetShort)compound.getNugget("moat_speed")).getValue();
		ramparts_speed = (byte)(short)((NuggetShort)compound.getNugget("ramparts_speed")).getValue();
		drawbridge_speed = (byte)(short)((NuggetShort)compound.getNugget("drawbridge_speed")).getValue();
		sally_speed = (byte)(short)((NuggetShort)compound.getNugget("sally_speed")).getValue();
		rock_speed = (byte)(short)((NuggetShort)compound.getNugget("rock_speed")).getValue();
		rough_speed = (byte)(short)((NuggetShort)compound.getNugget("rough_speed")).getValue();
		low_speed = (byte)(short)((NuggetShort)compound.getNugget("low_speed")).getValue();
		high_goals = (byte)(short)((NuggetShort)compound.getNugget("high_goals")).getValue();
		low_goals = (byte)(short)((NuggetShort)compound.getNugget("low_goals")).getValue();
		endgame = (byte)(short)((NuggetShort)compound.getNugget("endgame")).getValue();
	}
}
