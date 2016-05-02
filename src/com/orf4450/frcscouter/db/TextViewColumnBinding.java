package com.orf4450.frcscouter.db;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Bind a text view (TextView, EditText, etc.)
 *
 * @author Caleb Milligan
 *         Created on 2/4/2016
 */
public class TextViewColumnBinding extends AbstractColumnBinding<TextView, CharSequence> {
	public TextViewColumnBinding(TextView view, String column_name) {
		this(view, column_name, "TEXT");
	}

	public TextViewColumnBinding(TextView view, String column_name, int column_length) {
		this(view, column_name, "VARCHAR", column_length);
	}

	public TextViewColumnBinding(TextView view, String column_name, String column_class) {
		this(view, column_name, column_class, -1);
	}

	public TextViewColumnBinding(TextView view, String column_name, String column_class, int column_length) {
		this(view, column_name, column_class, column_length, true, null);
	}

	public TextViewColumnBinding(TextView view, String column_name, String column_class, int column_length, boolean can_null, Object default_value) {
		super(view, column_name, CharSequence.class, column_class, column_length, can_null, default_value);
	}

	@Override
	public CharSequence getValue() {
		return view.getText();
	}

	@Override
	public void setValue(CharSequence value) {
		view.setText(value);
	}

	@Override
	public void resetValue() {
		view.setText("");
	}

	@Override
	public void saveToBundle(Bundle bundle) {
		bundle.putCharSequence(column_name, getValue());
	}

	@Override
	public void loadFromBundle(Bundle bundle) {
		setValue(bundle.containsKey(column_name) ? bundle.getCharSequence(column_name) : "");
	}

	public CharSequence getError() {
		return view.getError();
	}

	public void setError(CharSequence error) {
		view.setError(error);
	}

	public void setError(CharSequence error, Drawable drawable) {
		view.setError(error, drawable);
	}
}
