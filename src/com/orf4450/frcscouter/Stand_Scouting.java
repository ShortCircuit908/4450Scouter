package com.orf4450.frcscouter;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.orf4450.frcscouter.db.*;
import com.orf4450.scouter.R;

public class Stand_Scouting extends Activity {
	private StorageDB database;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		LinearLayout temp = new LinearLayout(this);
		View post_load = inflater.inflate(R.layout.stand_scouting, temp);
		NumberPicker high_goal_scored = (NumberPicker) post_load.findViewById(R.id.high_goal_scored);
		NumberPicker low_goal_scored = (NumberPicker) post_load.findViewById(R.id.low_goal_scored);
		high_goal_scored.setMinValue(0);
		high_goal_scored.setMaxValue(99);
		high_goal_scored.setWrapSelectorWheel(false);
		low_goal_scored.setMinValue(0);
		low_goal_scored.setMaxValue(99);
		low_goal_scored.setWrapSelectorWheel(false);

		ColumnBinder column_bindings = new ColumnBinder();

		TextView match_number_view = (TextView) post_load.findViewById(R.id.match_number);
		column_bindings.add(new TextViewColumnBinding(match_number_view, "match_number", "INT", 4, false, "NULL"));

		column_bindings.add(new TextViewColumnBinding((TextView) post_load.findViewById(R.id.team_number), "team_number", "INT", 4, false, "NULL"));

		column_bindings.add(new TextViewColumnBinding((TextView) post_load.findViewById(R.id.match_number), "team_name", "VARCHAR", 64, false, "NULL"));

		column_bindings.add(new TextViewColumnBinding((TextView) post_load.findViewById(R.id.autonomous_behavior), "autonomous_behavior"));

		column_bindings.add(new RadioGroupColumnBinding((RadioGroup) post_load.findViewById(R.id.radio_group_pickup), "pickup_speed", ScouterConstants.pickup_speed_bindings));

		column_bindings.add(new RadioGroupColumnBinding(
				(RadioGroup) post_load.findViewById(R.id.radio_group_obstacles_0),
				"portcullis_speed",
				ScouterConstants.portcullis_speed_bindings));

		column_bindings.add(new RadioGroupColumnBinding(
				(RadioGroup) post_load.findViewById(R.id.radio_group_obstacles_1),
				"chival_speed",
				ScouterConstants.chival_speed_bindings));

		column_bindings.add(new RadioGroupColumnBinding(
				(RadioGroup) post_load.findViewById(R.id.radio_group_obstacles_2),
				"moat_speed",
				ScouterConstants.moat_speed_bindings));

		column_bindings.add(new RadioGroupColumnBinding(
				(RadioGroup) post_load.findViewById(R.id.radio_group_obstacles_3),
				"ramparts_speed",
				ScouterConstants.ramparts_speed_bindings));

		column_bindings.add(new RadioGroupColumnBinding(
				(RadioGroup) post_load.findViewById(R.id.radio_group_obstacles_4),
				"drawbridge_speed",
				ScouterConstants.drawbridge_speed_bindings));

		column_bindings.add(new RadioGroupColumnBinding(
				(RadioGroup) post_load.findViewById(R.id.radio_group_obstacles_5),
				"sally_speed",
				ScouterConstants.sally_speed_bindings));

		column_bindings.add(new RadioGroupColumnBinding(
				(RadioGroup) post_load.findViewById(R.id.radio_group_obstacles_6),
				"rock_speed",
				ScouterConstants.rock_speed_bindings));

		column_bindings.add(new RadioGroupColumnBinding(
				(RadioGroup) post_load.findViewById(R.id.radio_group_obstacles_7),
				"rough_speed",
				ScouterConstants.rough_speed_bindings));

		column_bindings.add(new RadioGroupColumnBinding(
				(RadioGroup) post_load.findViewById(R.id.radio_group_obstacles_8),
				"low_speed",
				ScouterConstants.low_speed_bindings));

		column_bindings.add(new NumberPickerColumnBinding(high_goal_scored, "high_goals"));

		column_bindings.add(new NumberPickerColumnBinding(low_goal_scored, "low_goals"));

		column_bindings.add(new RadioGroupColumnBinding((RadioGroup) post_load.findViewById(R.id.radio_group_endgame), "endgame", ScouterConstants.endgame_bindings));

		database = new StorageDB(this, column_bindings);

//		match_number_view.setText(database.getLastMatchNumber() + 1);

		setContentView(post_load);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		menu.findItem(R.id.delete_all).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				final Dialog dialog = new Dialog(Stand_Scouting.this, android.R.style.Theme_Material_Dialog_NoActionBar);
				dialog.setContentView(R.layout.delete_all);
				final ProgressBar progress_bar = (ProgressBar) dialog.findViewById(R.id.progress_delete_all);
				new TimedConfirmation(3000, progress_bar, new Runnable() {
					@Override
					public void run() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								dialog.dismiss();
							}
						});
					}
				});
				dialog.show();
				return false;
			}
		});
		return true;
	}
}
