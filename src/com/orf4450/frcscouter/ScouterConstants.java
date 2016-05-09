package com.orf4450.frcscouter;

import com.orf4450.scouter.R;

/**
 * A bunch of constants used throughout the app
 *
 * @author Caleb Milligan
 *         Created on 2/4/2016
 */
public interface ScouterConstants {
	String APP_UUID = "7674047e-6e47-4bf0-831f-209e3f9dd23f";
	String APP_USER_AGENT = "4450Scouting/1.0";
	String DATA_UPLOAD_URL = "http://orf.hulk.osd.wednet.edu/scouting/upload.php";
	String IMAGE_UPLOAD_URL = "http://orf.hulk.osd.wednet.edu/scouting/image-upload.php";

	/**
	 * This is used for the RadioGroupColumnBindings responsible for tracking obstacle cross speeds.
	 * Each speed, 0-2, is bound to the Android resource ID of its specific RadioButton.
	 *
	 * 2: fast
	 * 1: medium
	 * 0: slow
	 */
	int[][] pickup_speed_bindings = {
			{R.id.auto_pickup_fast, 2},
			{R.id.auto_pickup_medium, 1},
			{R.id.auto_pickup_slow, 0},
	};

	/**
	 * This is used for the RadioGroupColumnBindings responsible for tracking obstacle cross speeds.
	 * Each speed, 0-2, is bound to the Android resource ID of its specific RadioButton.
	 *
	 * 2: fast
	 * 1: medium
	 * 0: slow
	 */
	int[][] portcullis_speed_bindings = {
			{R.id.portcullis_fast, 2},
			{R.id.portcullis_medium, 1},
			{R.id.portcullis_slow, 0},
	};

	/**
	 * This is used for the RadioGroupColumnBindings responsible for tracking obstacle cross speeds.
	 * Each speed, 0-2, is bound to the Android resource ID of its specific RadioButton.
	 *
	 * 2: fast
	 * 1: medium
	 * 0: slow
	 */
	int[][] chival_speed_bindings = {
			{R.id.chival_fast, 2},
			{R.id.chival_medium, 1},
			{R.id.chival_slow, 0},
	};

	/**
	 * This is used for the RadioGroupColumnBindings responsible for tracking obstacle cross speeds.
	 * Each speed, 0-2, is bound to the Android resource ID of its specific RadioButton.
	 *
	 * 2: fast
	 * 1: medium
	 * 0: slow
	 */
	int[][] moat_speed_bindings = {
			{R.id.moat_fast, 2},
			{R.id.moat_medium, 1},
			{R.id.moat_slow, 0},
	};

	/**
	 * This is used for the RadioGroupColumnBindings responsible for tracking obstacle cross speeds.
	 * Each speed, 0-2, is bound to the Android resource ID of its specific RadioButton.
	 *
	 * 2: fast
	 * 1: medium
	 * 0: slow
	 */
	int[][] ramparts_speed_bindings = {
			{R.id.ramparts_fast, 2},
			{R.id.ramparts_medium, 1},
			{R.id.ramparts_slow, 0},
	};

	/**
	 * This is used for the RadioGroupColumnBindings responsible for tracking obstacle cross speeds.
	 * Each speed, 0-2, is bound to the Android resource ID of its specific RadioButton.
	 *
	 * 2: fast
	 * 1: medium
	 * 0: slow
	 */
	int[][] drawbridge_speed_bindings = {
			{R.id.drawbridge_fast, 2},
			{R.id.drawbridge_medium, 1},
			{R.id.drawbridge_slow, 0},
	};

	/**
	 * This is used for the RadioGroupColumnBindings responsible for tracking obstacle cross speeds.
	 * Each speed, 0-2, is bound to the Android resource ID of its specific RadioButton.
	 *
	 * 2: fast
	 * 1: medium
	 * 0: slow
	 */
	int[][] sally_speed_bindings = {
			{R.id.sally_fast, 2},
			{R.id.sally_medium, 1},
			{R.id.sally_slow, 0},
	};

	/**
	 * This is used for the RadioGroupColumnBindings responsible for tracking obstacle cross speeds.
	 * Each speed, 0-2, is bound to the Android resource ID of its specific RadioButton.
	 *
	 * 2: fast
	 * 1: medium
	 * 0: slow
	 */
	int[][] rock_speed_bindings = {
			{R.id.rock_fast, 2},
			{R.id.rock_medium, 1},
			{R.id.rock_slow, 0},
	};

	/**
	 * This is used for the RadioGroupColumnBindings responsible for tracking obstacle cross speeds.
	 * Each speed, 0-2, is bound to the Android resource ID of its specific RadioButton.
	 *
	 * 2: fast
	 * 1: medium
	 * 0: slow
	 */
	int[][] rough_speed_bindings = {
			{R.id.rough_fast, 2},
			{R.id.rough_medium, 1},
			{R.id.rough_slow, 0},
	};

	/**
	 * This is used for the RadioGroupColumnBindings responsible for tracking obstacle cross speeds.
	 * Each speed, 0-2, is bound to the Android resource ID of its specific RadioButton.
	 *
	 * 2: fast
	 * 1: medium
	 * 0: slow
	 */
	int[][] low_speed_bindings = {
			{R.id.low_fast, 2},
			{R.id.low_medium, 1},
			{R.id.low_slow, 0},
	};

	/**
	 * This is used for the RadioGroupColumnBindings responsible for tracking endgame.
	 * Each action, 0-1, is bound to the Android resource ID of its specific RadioButton.
	 *
	 * 1: climbed tower
	 * 0: parked on ramp
	 */
	int[][] endgame_bindings = {
			{R.id.on_ramp, 0},
			{R.id.climbed_tower, 1}
	};
}
