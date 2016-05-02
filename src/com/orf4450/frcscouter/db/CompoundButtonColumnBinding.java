package com.orf4450.frcscouter.db;

import android.os.Bundle;
import android.widget.CompoundButton;

/**
 * Bind a compound button (check boxes, radio buttons, switches, etc.) to an SQL table column
 *
 * @author Caleb Milligan
 *         Created on 2/4/2016
 */
public class CompoundButtonColumnBinding extends AbstractColumnBinding<CompoundButton, Boolean> {
	public CompoundButtonColumnBinding(CompoundButton view, String column_name) {
		super(view, column_name, Boolean.class, "TINYINT", 1, false, null);
	}

	@Override
	public Boolean getValue() {
		return view.isChecked();
	}

	@Override
	public void setValue(Boolean value) {
		view.setChecked(value);
	}

	@Override
	public void resetValue() {
		view.setChecked(false);
	}

	@Override
	public void saveToBundle(Bundle bundle) {
		bundle.putBoolean(column_name, getValue());
	}

	@Override
	public void loadFromBundle(Bundle bundle) {
		setValue(bundle.containsKey(column_name) && bundle.getBoolean(column_name));
	}
}
