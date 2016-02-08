package com.orf4450.frcscouter.db;

import android.widget.CompoundButton;

/**
 * @author ShortCircuit908
 *         Created on 2/4/2016
 */
public class CompoundButtonColumnBinding extends AbstractColumnBinding<CompoundButton, Boolean> {
	public CompoundButtonColumnBinding(CompoundButton view, String column_name) {
		super(view, column_name, Boolean.class, "BOOLEAN", -1, false, "NULL");
	}

	@Override
	public Boolean getValue() {
		return view.isChecked();
	}

	@Override
	public void setValue(Boolean value) {
		view.setChecked(value);
	}
}
