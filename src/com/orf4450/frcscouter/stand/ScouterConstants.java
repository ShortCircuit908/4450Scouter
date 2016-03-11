package com.orf4450.frcscouter.stand;

import com.orf4450.scouter.R;

/**
 * @author Caleb Milligan
 *         Created on 2/4/2016
 */
public interface ScouterConstants {
	int[][] pickup_speed_bindings = {
			{R.id.auto_pickup_fast, 2},
			{R.id.auto_pickup_medium, 1},
			{R.id.auto_pickup_slow, 0},
	};
	int[][] portcullis_speed_bindings = {
			{R.id.portcullis_fast, 2},
			{R.id.portcullis_medium, 1},
			{R.id.portcullis_slow, 0},
	};
	int[][] chival_speed_bindings = {
			{R.id.chival_fast, 2},
			{R.id.chival_medium, 1},
			{R.id.chival_slow, 0},
	};
	int[][] moat_speed_bindings = {
			{R.id.moat_fast, 2},
			{R.id.moat_medium, 1},
			{R.id.moat_slow, 0},
	};
	int[][] ramparts_speed_bindings = {
			{R.id.ramparts_fast, 2},
			{R.id.ramparts_medium, 1},
			{R.id.ramparts_slow, 0},
	};
	int[][] drawbridge_speed_bindings = {
			{R.id.drawbridge_fast, 2},
			{R.id.drawbridge_medium, 1},
			{R.id.drawbridge_slow, 0},
	};
	int[][] sally_speed_bindings = {
			{R.id.sally_fast, 2},
			{R.id.sally_medium, 1},
			{R.id.sally_slow, 0},
	};
	int[][] rock_speed_bindings = {
			{R.id.rock_fast, 2},
			{R.id.rock_medium, 1},
			{R.id.rock_slow, 0},
	};
	int[][] rough_speed_bindings = {
			{R.id.rough_fast, 2},
			{R.id.rough_medium, 1},
			{R.id.rough_slow, 0},
	};
	int[][] low_speed_bindings = {
			{R.id.low_fast, 2},
			{R.id.low_medium, 1},
			{R.id.low_slow, 0},
	};
	int[][] endgame_bindings = {
			{R.id.on_ramp, 0},
			{R.id.climbed_tower, 1}
	};
}
