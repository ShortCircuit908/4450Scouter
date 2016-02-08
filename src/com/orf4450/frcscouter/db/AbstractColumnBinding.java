package com.orf4450.frcscouter.db;

import android.view.View;

/**
 * Class to establish a relationship between an input field and an SQL table column
 *
 * @author ShortCircuit908
 *         Created on 2/4/2016
 */
public abstract class AbstractColumnBinding<T extends View, E> {
	protected final String column_class;
	protected final Class<E> value_class;
	protected final int column_length;
	protected final T view;
	protected final String column_name;
	protected final boolean can_null;
	protected final Object default_value;

	public AbstractColumnBinding(T view, String column_name, Class<E> value_class, String column_class) {
		this(view, column_name, value_class, column_class, -1);
	}

	public AbstractColumnBinding(T view, String column_name, Class<E> value_class, String column_class, int column_length) {
		this(view, column_name, value_class, column_class, column_length, true, null);
	}

	public AbstractColumnBinding(T view, String column_name, Class<E> value_class, String column_class, int column_length, boolean can_null, Object default_value) {
		this.view = view;
		this.column_name = column_name;
		this.column_class = column_class;
		this.value_class = value_class;
		this.column_length = column_length;
		this.can_null = can_null;
		this.default_value = default_value;
	}

	public Class<E> getValueClass() {
		return value_class;
	}

	public boolean canNull() {
		return can_null;
	}

	public Object getDefaultValue() {
		return default_value;
	}

	public String getColumnClass() {
		return column_class;
	}

	public T getView() {
		return view;
	}

	public String getColumnName() {
		return column_name;
	}

	public int getColumnLength() {
		return column_length;
	}

	public abstract E getValue();

	public abstract void setValue(E value);
}
