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
	private String scouter_name;
	private String autonomous_behavior;
	private boolean no_show;
	private boolean died_on_field;
	private boolean defended;
	private byte pickup_speed;
	private byte portcullis_crosses;
	private byte portcullis_speed;
	private byte chival_crosses;
	private byte chival_speed;
	private byte moat_crosses;
	private byte moat_speed;
	private byte ramparts_crosses;
	private byte ramparts_speed;
	private byte drawbridge_crosses;
	private byte drawbridge_speed;
	private byte sally_crosses;
	private byte sally_speed;
	private byte rock_crosses;
	private byte rock_speed;
	private byte rough_crosses;
	private byte rough_speed;
	private byte low_crosses;
	private byte low_speed;
	private byte high_goals;
	private byte low_goals;
	private byte endgame;
	private String notes;

	public Match(NuggetCompound compound) {
		match_number = ((NuggetShort) compound.getNugget("match_number")).getValue();
		team_number = ((NuggetShort) compound.getNugget("team_number")).getValue();
		scouter_name = ((NuggetString) compound.getNugget("scouter_name")).getValue();
		autonomous_behavior = ((NuggetString) compound.getNugget("autonomous_behavior")).getValue();
		no_show = ((NuggetShort) compound.getNugget("no_show")).getValue() != 0;
		died_on_field = ((NuggetShort) compound.getNugget("died_on_field")).getValue() != 0;
		defended = ((NuggetShort) compound.getNugget("defended")).getValue() != 0;
		pickup_speed = (byte) (short) ((NuggetShort) compound.getNugget("pickup_speed")).getValue();
		portcullis_crosses = (byte) (short) ((NuggetShort) compound.getNugget("portcullis_crosses")).getValue();
		portcullis_speed = (byte) (short) ((NuggetShort) compound.getNugget("portcullis_speed")).getValue();
		chival_crosses = (byte) (short) ((NuggetShort) compound.getNugget("chival_crosses")).getValue();
		chival_speed = (byte) (short) ((NuggetShort) compound.getNugget("chival_speed")).getValue();
		moat_crosses = (byte) (short) ((NuggetShort) compound.getNugget("moat_crosses")).getValue();
		moat_speed = (byte) (short) ((NuggetShort) compound.getNugget("moat_speed")).getValue();
		ramparts_crosses = (byte) (short) ((NuggetShort) compound.getNugget("ramparts_crosses")).getValue();
		ramparts_speed = (byte) (short) ((NuggetShort) compound.getNugget("ramparts_speed")).getValue();
		drawbridge_crosses = (byte) (short) ((NuggetShort) compound.getNugget("drawbridge_crosses")).getValue();
		drawbridge_speed = (byte) (short) ((NuggetShort) compound.getNugget("drawbridge_speed")).getValue();
		sally_crosses = (byte) (short) ((NuggetShort) compound.getNugget("sally_crosses")).getValue();
		sally_speed = (byte) (short) ((NuggetShort) compound.getNugget("sally_speed")).getValue();
		rock_crosses = (byte) (short) ((NuggetShort) compound.getNugget("rock_crosses")).getValue();
		rock_speed = (byte) (short) ((NuggetShort) compound.getNugget("rock_speed")).getValue();
		rough_crosses = (byte) (short) ((NuggetShort) compound.getNugget("rough_crosses")).getValue();
		rough_speed = (byte) (short) ((NuggetShort) compound.getNugget("rough_speed")).getValue();
		low_speed = (byte) (short) ((NuggetShort) compound.getNugget("low_speed")).getValue();
		high_goals = (byte) (short) ((NuggetShort) compound.getNugget("high_goals")).getValue();
		low_goals = (byte) (short) ((NuggetShort) compound.getNugget("low_goals")).getValue();
		endgame = (byte) (short) ((NuggetShort) compound.getNugget("endgame")).getValue();
		notes = ((NuggetString) compound.getNugget("notes")).getValue();
	}
}
