package com.orf4450.frcscouter.stand;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.orf4450.frcscouter.TimedConfirmation;
import com.orf4450.frcscouter.UploadActivity;
import com.orf4450.frcscouter.db.*;
import com.orf4450.frcscouter.master.MasterDB;
import com.orf4450.scouter.R;

import java.io.IOException;

/**
 * @author Caleb Milligan
 *         Created on 2/3/2016
 */
public class StandScouting extends Activity {
	private StandDB database;
	private ColumnBinder column_bindings;
	private SharedPreferences settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settings = getPreferences(Activity.MODE_PRIVATE);
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

		column_bindings = new ColumnBinder();

		TextView match_number_view = (TextView) post_load.findViewById(R.id.match_number);
		column_bindings.add(new TextViewColumnBinding(match_number_view, "match_number", "INT", 4, false, null));

		column_bindings.add(new TextViewColumnBinding((TextView) post_load.findViewById(R.id.team_number), "team_number", "INT", 4, false, null));

		column_bindings.add(new TextViewColumnBinding((TextView) post_load.findViewById(R.id.team_name), "team_name", "VARCHAR", 64));

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

		column_bindings.add(new RadioGroupColumnBinding(
				(RadioGroup) post_load.findViewById(R.id.radio_group_endgame),
				"endgame",
				ScouterConstants.endgame_bindings));

		database = new StandDB(this, column_bindings);

		resetFields();

		setContentView(post_load);
	}

	public boolean shouldAutoUpload() {
		return settings.getBoolean("auto_upload", true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.stand_menu, menu);
		menu.findItem(R.id.delete_all).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				final Dialog dialog = new Dialog(StandScouting.this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
				dialog.setContentView(R.layout.delete_all);
				final ProgressBar progress_bar = (ProgressBar) dialog.findViewById(R.id.progress_delete_all);
				final TimedConfirmation confirmation = new TimedConfirmation(3000, progress_bar, new Runnable() {
					@Override
					public void run() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								dialog.dismiss();
								Toast toast = Toast.makeText(StandScouting.this, "Deleting", Toast.LENGTH_SHORT);
								toast.show();
								database.deleteAllData();
								toast = Toast.makeText(StandScouting.this, "All data deleted", Toast.LENGTH_SHORT);
								toast.show();
							}
						});
					}
				});
				dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						confirmation.exit();
					}
				});
				dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						confirmation.exit();
					}
				});
				dialog.show();
				return false;
			}
		});
		menu.findItem(R.id.save).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Toast.makeText(StandScouting.this, "Saving", Toast.LENGTH_SHORT).show();
				if(database.saveMatch()) {
					resetFields();
					Toast.makeText(StandScouting.this, "Saved", Toast.LENGTH_SHORT).show();
				}
				else{
					Toast.makeText(StandScouting.this, "Save failed", Toast.LENGTH_SHORT).show();
				}
				return false;
			}
		});
		menu.findItem(R.id.load).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// Create a popup window
				final Dialog dialog = new Dialog(StandScouting.this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
				dialog.setContentView(R.layout.load_match);
				ListView list_view = (ListView) dialog.findViewById(R.id.list_load_match);
				// Create an ArrayAdapter and populate it with stored match descriptors
				final ArrayAdapter<MatchDescriptor> adapter = new ArrayAdapter<>(StandScouting.this,
						android.R.layout.simple_list_item_1);
				list_view.setAdapter(adapter);
				adapter.addAll(database.getAllStoredMatches());
				adapter.notifyDataSetChanged();
				// Load matches on click
				list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						MatchDescriptor descriptor = adapter.getItem(position);
						Toast toast = Toast.makeText(StandScouting.this, "Loading", Toast.LENGTH_SHORT);
						toast.show();
						database.loadMatch(descriptor);
						dialog.dismiss();
					}
				});
				list_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
						PopupMenu menu = new PopupMenu(StandScouting.this, view);
						MenuItem item = menu.getMenu().add("Remove");
						item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
							@Override
							public boolean onMenuItemClick(MenuItem item) {
								MatchDescriptor descriptor = adapter.getItem(position);
								Toast toast = Toast.makeText(StandScouting.this, "Deleting", Toast.LENGTH_SHORT);
								toast.show();
								database.deleteMatch(descriptor);
								toast = Toast.makeText(StandScouting.this, "Match deleted", Toast.LENGTH_SHORT);
								toast.show();
								adapter.remove(descriptor);
								adapter.notifyDataSetChanged();
								return true;
							}
						});
						menu.show();
						return false;
					}
				});
				// Show the dialog
				dialog.show();
				return true;
			}
		});
		menu.findItem(R.id.reset).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				resetFields();
				return true;
			}
		});
		menu.findItem(R.id.upload).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				startActivity(new Intent(StandScouting.this, UploadActivity.class));
				return true;
			}
		});
		menu.findItem(R.id.export).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				try {
					Toast.makeText(StandScouting.this, "Exporting", Toast.LENGTH_SHORT).show();
					new MasterDB(StandScouting.this).saveNugget(database.toNugget());
					Toast.makeText(StandScouting.this, "Exported", Toast.LENGTH_SHORT).show();
				}
				catch (IOException e) {
					Toast.makeText(StandScouting.this, "Export failed", Toast.LENGTH_SHORT).show();
				}
				return true;
			}
		});
		return true;
	}

	public void resetFields() {
		TextViewColumnBinding team_name = column_bindings.get(R.id.team_name);
		CharSequence name = team_name.getValue();
		column_bindings.resetAll();
		((TextViewColumnBinding) column_bindings.get(R.id.match_number)).setValue((database.getLastMatchNumber() + 1) + "");
		team_name.setValue(name);
	}
}
