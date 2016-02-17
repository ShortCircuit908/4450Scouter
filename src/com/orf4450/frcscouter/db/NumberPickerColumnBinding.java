package com.orf4450.frcscouter.db;

import android.os.Bundle;
import android.widget.NumberPicker;

/**
 * @author Caleb Milligan
 *         Created on 2/4/2016
 */
public class NumberPickerColumnBinding extends AbstractColumnBinding<NumberPicker, Integer> {
	public NumberPickerColumnBinding(NumberPicker view, String column_name) {
		super(view, column_name, Integer.class, "INT", Math.max((view.getMaxValue() + "").length(), (view.getMinValue() + "").length()), false, null);
	}

	@Override
	public Integer getValue() {
		return view.getValue();
	}

	@Override
	public void setValue(Integer value) {
		view.setValue(value);
	}

	@Override
	public void resetValue() {
		view.setValue(view.getMinValue());
	}

	@Override
	public void saveToBundle(Bundle bundle) {
		bundle.putInt(column_name, getValue());
	}

	@Override
	public void loadFromBundle(Bundle bundle) {
		setValue(bundle.containsKey(column_name) ? bundle.getInt(column_name) : view.getMinValue());
	}
}
