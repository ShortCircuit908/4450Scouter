package com.orf4450.frcscouter.master;

import com.orf4450.frcscouter.pit.PitTeam;
import com.orf4450.frcscouter.stand.Match;

import java.util.LinkedList;

/**
 * @author Caleb Milligan
 *         Created on 2/19/2016
 */
public class DataBundle {
	private LinkedList<Match> matches = new LinkedList<>();
	private LinkedList<PitTeam> teams = new LinkedList<>();

	public LinkedList<Match> getMatches() {
		return matches;
	}

	public LinkedList<PitTeam> getTeams() {
		return teams;
	}

	public void addMatch(Match match){
		matches.add(match);
	}

	public void addTeam(PitTeam team){
		teams.add(team);
	}
}
