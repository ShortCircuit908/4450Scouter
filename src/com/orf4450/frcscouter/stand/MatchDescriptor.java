package com.orf4450.frcscouter.stand;

/**
 * @author Caleb Milligan
 *         Created on 1/13/2016
 */
public class MatchDescriptor {
	private final int match_number;
	private final int team_number;

	public MatchDescriptor(int match_number, int team_number) {
		this.match_number = match_number;
		this.team_number = team_number;
	}

	public int getMatchNumber() {
		return match_number;
	}

	public int getTeamNumber() {
		return team_number;
	}

	@Override
	public String toString() {
		return "#" + match_number + " (Team " + team_number + ")";
	}
}
