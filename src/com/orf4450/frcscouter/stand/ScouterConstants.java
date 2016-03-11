package com.orf4450.frcscouter.stand;

import com.orf4450.scouter.R;

/**
 * @author Caleb Milligan
 *         Created on 2/4/2016
 */
public interface ScouterConstants {
	int[][] pickup_speed_bindings = {
			{R.id.auto_pickup_fast, 0},
			{R.id.auto_pickup_medium, 1},
			{R.id.auto_pickup_slow, 2},
			{-1, 3}
	};
	int[][] portcullis_speed_bindings = {
			{R.id.portcullis_fast, 0},
			{R.id.portcullis_medium, 1},
			{R.id.portcullis_slow, 2},
			{-1, 3}
	};
	int[][] chival_speed_bindings = {
			{R.id.chival_fast, 0},
			{R.id.chival_medium, 1},
			{R.id.chival_slow, 2},
			{-1, 3}
	};
	int[][] moat_speed_bindings = {
			{R.id.moat_fast, 0},
			{R.id.moat_medium, 1},
			{R.id.moat_slow, 2},
			{-1, 3}
	};
	int[][] ramparts_speed_bindings = {
			{R.id.ramparts_fast, 0},
			{R.id.ramparts_medium, 1},
			{R.id.ramparts_slow, 2},
			{-1, 3}
	};
	int[][] drawbridge_speed_bindings = {
			{R.id.drawbridge_fast, 0},
			{R.id.drawbridge_medium, 1},
			{R.id.drawbridge_slow, 2},
			{-1, 3}
	};
	int[][] sally_speed_bindings = {
			{R.id.sally_fast, 0},
			{R.id.sally_medium, 1},
			{R.id.sally_slow, 2},
			{-1, 3}
	};
	int[][] rock_speed_bindings = {
			{R.id.rock_fast, 0},
			{R.id.rock_medium, 1},
			{R.id.rock_slow, 2},
			{-1, 3}
	};
	int[][] rough_speed_bindings = {
			{R.id.rough_fast, 0},
			{R.id.rough_medium, 1},
			{R.id.rough_slow, 2},
			{-1, 3}
	};
	int[][] low_speed_bindings = {
			{R.id.low_fast, 0},
			{R.id.low_medium, 1},
			{R.id.low_slow, 2},
			{-1, 3}
	};
	int[][] endgame_bindings = {
			{R.id.on_ramp, 0},
			{R.id.climbed_tower, 1}
	};
}
