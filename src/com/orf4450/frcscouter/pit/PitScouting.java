package com.orf4450.frcscouter.pit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.orf4450.frcscouter.db.ColumnBinder;
import com.orf4450.frcscouter.db.TextViewColumnBinding;
import com.orf4450.scouter.R;

import java.io.File;
import java.io.IOException;

/**
 * @author Caleb Milligan
 *         Created on 2/10/2016
 */
public class PitScouting extends Activity {
	private SharedPreferences settings;
	private TextView team_number;
	private TextView team_name;
	private ImageView image_view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settings = getPreferences(Activity.MODE_PRIVATE);
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		LinearLayout temp = new LinearLayout(this);
		View post_load = inflater.inflate(R.layout.pit_scouting, temp);

		ColumnBinder column_bindings = new ColumnBinder();
		team_number = (TextView) post_load.findViewById(R.id.pit_team_number);
		column_bindings.add(new TextViewColumnBinding(team_number, "team_number", "INT", 4, false, null));
		team_name = (TextView) post_load.findViewById(R.id.pit_team_name);
		column_bindings.add(new TextViewColumnBinding(team_name, "team_name", 64));
		post_load.findViewById(R.id.take_picture).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (team_number.getText().toString().trim().isEmpty()) {
					Toast toast = Toast.makeText(PitScouting.this, "Please specify the team number", Toast.LENGTH_SHORT);
					toast.show();
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
			current_photo_file = null;
			try {
				current_photo_file = createImageFile();
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
			if (current_photo_file != null) {
				capture_image_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(current_photo_file));
				startActivityForResult(capture_image_intent, REQUEST_TAKE_PHOTO);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
			setPic(current_photo_file);
		}
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

	private File createImageFile() throws IOException {
		String file_name = "ROBOT_" + team_number.getText() + ".jpg";
		File storage_dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		return new File(storage_dir, file_name);
	}
}
