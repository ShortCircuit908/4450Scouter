package com.orf4450.frcscouter.db;

import android.os.Bundle;
import android.view.View;

/**
 * A dummy column binding for custom data
 *
 * @author Caleb Milligan
 *         Created on 2/10/2016
 */
public class DummyColumnBinding<T> extends AbstractColumnBinding<View, T> {
	private T default_value;
	private T value;

	public DummyColumnBinding(String column_name, Class<T> value_class, String column_class) {
		this(column_name, value_class, column_class, null);
	}

	public DummyColumnBinding(String column_name, Class<T> value_class, String column_class, T default_value) {
		this(column_name, value_class, column_class, -1, true, default_value);
	}

	public DummyColumnBinding(String column_name, Class<T> value_class, String column_class, int column_length, boolean can_null, T default_value) {
		super(null, column_name, value_class, column_class, column_length, can_null, default_value);
		this.default_value = default_value;
	}

	@Override
	public T getValue() {
		return value;
	}

	@Override
	public void setValue(T value) {
		this.value = value;
	}

	@Override
	public void resetValue() {
		value = default_value;
	}

	@Override
	public void saveToBundle(Bundle bundle) {

	}

	@Override
	public void loadFromBundle(Bundle bundle) {

	}

	public T getDefaultValue() {
		return default_value;
	}

	public void setDefaultValue(T default_value) {
		this.default_value = default_value;
	}

	@Override
	public int getViewId() {
		return -1;
	}
}
