package com.orf4450.frcscouter.pit;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.*;
import android.widget.*;
import com.orf4450.frcscouter.TimedConfirmation;
import com.orf4450.frcscouter.UploadActivity;
import com.orf4450.frcscouter.db.ColumnBinder;
import com.orf4450.frcscouter.db.PitDB;
import com.orf4450.frcscouter.db.TextViewColumnBinding;
import com.orf4450.frcscouter.master.MasterDB;
import com.orf4450.scouter.R;

import java.io.File;
import java.io.IOException;

/**
 * @author Caleb Milligan
 *         Created on 2/10/2016
 */
public class PitScouting extends Activity {
	private PitDB database;
	private SharedPreferences settings;
	private TextView team_number;
	private TextView team_name;
	private ImageView image_view;
	private ColumnBinder column_bindings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settings = getPreferences(Activity.MODE_PRIVATE);
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		LinearLayout temp = new LinearLayout(this);
		View post_load = inflater.inflate(R.layout.pit_scouting, temp);

		column_bindings = new ColumnBinder();
		team_number = (TextView) post_load.findViewById(R.id.pit_team_number);
		column_bindings.add(new TextViewColumnBinding(team_number, "team_number", "INT", 4, false, null));
		team_name = (TextView) post_load.findViewById(R.id.pit_team_name);
		column_bindings.add(new TextViewColumnBinding(team_name, "team_name", 64));
		column_bindings.add(new TextViewColumnBinding((TextView) post_load.findViewById(R.id.robot_description), "robot_description", "TEXT"));
		database = new PitDB(this, column_bindings);
		post_load.findViewById(R.id.take_picture).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (team_number.getText().toString().trim().isEmpty()) {
					team_number.setError("This field is required");
					team_number.requestFocus();
					return;
				}
				dispatchTakePictureIntent();
			}
		});
		image_view = (ImageView) post_load.findViewById(R.id.image_view);
		setContentView(post_load);
	}

	static final int REQUEST_TAKE_PHOTO = 1;
	private File current_photo_file;

	private void dispatchTakePictureIntent() {
		Intent capture_image_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (capture_image_intent.resolveActivity(getPackageManager()) != null) {
			current_photo_file = getImageFile(team_number.getText().toString().trim());
			capture_image_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(current_photo_file));
			startActivityForResult(capture_image_intent, REQUEST_TAKE_PHOTO);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
			setPic(current_photo_file);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.pit_menu, menu);
		menu.findItem(R.id.delete_all).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				final Dialog dialog = new Dialog(PitScouting.this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
				dialog.setContentView(R.layout.delete_all);
				final ProgressBar progress_bar = (ProgressBar) dialog.findViewById(R.id.progress_delete_all);
				final TimedConfirmation confirmation = new TimedConfirmation(3000, progress_bar, new Runnable() {
					@Override
					public void run() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								dialog.dismiss();
								Toast.makeText(PitScouting.this, "Deleting", Toast.LENGTH_SHORT).show();
								database.deleteAllData();
								Toast.makeText(PitScouting.this, "All data deleted", Toast.LENGTH_SHORT).show();
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
				return true;
			}
		});
		menu.findItem(R.id.save).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Toast.makeText(PitScouting.this, "Saving", Toast.LENGTH_SHORT).show();
				if (database.save()) {
					resetFields();
					Toast.makeText(PitScouting.this, "Saved", Toast.LENGTH_SHORT).show();
				}
				else {
					Toast.makeText(PitScouting.this, "Save failed", Toast.LENGTH_SHORT).show();
				}
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
				startActivity(new Intent(PitScouting.this, UploadActivity.class));
				return true;
			}
		});
		menu.findItem(R.id.export).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Toast.makeText(PitScouting.this, "Exporting", Toast.LENGTH_SHORT).show();
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							new MasterDB(PitScouting.this).saveNugget(database.toNugget());
							database.setUploaded();
							PitScouting.this.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(PitScouting.this, "Exported", Toast.LENGTH_SHORT).show();
								}
							});
						}
						catch (IOException e) {
							PitScouting.this.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(PitScouting.this, "Export failed", Toast.LENGTH_SHORT).show();
								}
							});
						}
					}
				}).start();
				return true;
			}
		});
		return true;
	}

	public void resetFields() {
		column_bindings.resetAll();
		image_view.setImageResource(android.R.color.transparent);
	}

	private void setPic(File file) {
		if (file == null) {
			return;
		}
		int target_width = image_view.getWidth();
		int target_height = image_view.getHeight();
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(file.getAbsolutePath(), options);
		int out_width = options.outWidth;
		int out_height = options.outHeight;
		int scale = Math.min(out_width / target_width, out_height / target_height);
		options.inJustDecodeBounds = false;
		options.inSampleSize = scale;
		options.inBitmap = image_view.getDrawingCache();
		Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
		image_view.setImageBitmap(bitmap);
	}

	public static File getImageFile(Object team_number) {
		String file_name = "ROBOT_" + team_number + ".jpg";
		File storage_dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		return new File(storage_dir, file_name);
	}
}
