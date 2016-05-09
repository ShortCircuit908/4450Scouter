package com.orf4450.frcscouter.db;

import android.os.Bundle;
import android.widget.RadioGroup;

/**
 * Bind a radio group to an SQL table column
 *
 * @author Caleb Milligan
 *         Created on 2/4/2016
 */
public class RadioGroupColumnBinding extends AbstractColumnBinding<RadioGroup, Integer> {
	private final int[][] id_to_value_bindings;

	/**
	 * @param view                 the {@link RadioGroup} to bind
	 * @param column_name          the name of the bound SQL table column
	 * @param id_to_value_bindings two-dimensional array containing keys and values
	 */
	public RadioGroupColumnBinding(RadioGroup view, String column_name, int[][] id_to_value_bindings) {
		super(view, column_name, Integer.class, "INT", (view.getChildCount() + "").length(), false, null);
		this.id_to_value_bindings = id_to_value_bindings;
	}

	/**
	 * Get the assigned value of a bound RadioButton
	 *
	 * @param id the ID of the bound RadioButton
	 * @return the assigned value, or -1 if it does not exist
	 */
	public int getValueFromId(int id) {
		for (int[] binding : id_to_value_bindings) {
			if (binding[0] == id) {
				return binding[1];
			}
		}
		return -1;
	}

	/**
	 * Get the resource ID of the bound RadioButton
	 *
	 * @param value the assigned value of the RadioButton
	 * @return the resource ID of the RadioButton, or -1 if it does not exist
	 */
	public int getIdFromValue(int value) {
		for (int[] binding : id_to_value_bindings) {
			if (binding[1] == value) {
				return binding[0];
			}
		}
		return -1;
	}

	@Override
	public Integer getValue() {
		return getValueFromId(view.getCheckedRadioButtonId());
	}

	@Override
	public void setValue(Integer value) {
		int id = getIdFromValue(value);
		if (id > -1) {
			view.check(id);
		}
		else {
			view.clearCheck();
		}
	}

	@Override
	public void resetValue() {
		view.clearCheck();
	}

	@Override
	public void saveToBundle(Bundle bundle) {
		bundle.putInt(column_name, getValue());
	}

	@Override
	public void loadFromBundle(Bundle bundle) {
		setValue(bundle.containsKey(column_name) ? bundle.getInt(column_name) : -1);
	}

}
