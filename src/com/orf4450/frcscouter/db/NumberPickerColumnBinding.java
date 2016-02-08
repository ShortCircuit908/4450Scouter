package com.orf4450.frcscouter.db;

import android.widget.NumberPicker;

/**
 * @author ShortCircuit908
 *         Created on 2/4/2016
 */
public class NumberPickerColumnBinding extends AbstractColumnBinding<NumberPicker, Integer> {
	public NumberPickerColumnBinding(NumberPicker view, String column_name) {
		super(view, column_name, Integer.class, "INT", Math.max((view.getMaxValue() + "").length(), (view.getMinValue() + "").length()), false, 0);
	}

	@Override
	public Integer getValue() {
		return view.getValue();
	}

	@Override
	public void setValue(Integer value) {
		view.setValue(value);
	}
}
