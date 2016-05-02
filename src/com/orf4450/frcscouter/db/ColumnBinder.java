package com.orf4450.frcscouter.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Core class to handle Android input view column bindings.
 * Can be used to generate database schemas from data through {@link #generateSchema(String)}
 *
 * @author Caleb Milligan
 *         Created on 2/4/2016
 */
public class ColumnBinder {
	private final LinkedList<AbstractColumnBinding<?, ?>> bindings = new LinkedList<>();

	/**
	 * Add a binding
	 *
	 * @param binding
	 */
	public void add(AbstractColumnBinding<?, ?> binding) {
		bindings.add(binding);
	}

	/**
	 * Get a binding by its column name
	 *
	 * @param column_name
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends AbstractColumnBinding<?, ?>> T get(String column_name) {
		for (AbstractColumnBinding<?, ?> binding : bindings) {
			if (binding.getColumnName().equals(column_name)) {
				return (T) binding;
			}
		}
		return null;
	}

	/**
	 * Get a binding by its bound view
	 *
	 * @param view
	 * @param <T>
	 * @return
	 */
	public <T extends AbstractColumnBinding<?, ?>> T get(View view) {
		return get(view.getId());
	}

	/**
	 * Get a binding by its bound view ID
	 *
	 * @param viewid
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends AbstractColumnBinding<?, ?>> T get(int viewid) {
		for (AbstractColumnBinding<?, ?> binding : bindings) {
			if (binding.getView().getId() == viewid) {
				return (T) binding;
			}
		}
		return null;
	}

	/**
	 * Generate a MySQL/SQLite database schema with columns from the current bindings
	 *
	 * @param table_name
	 * @return
	 */
	public String generateSchema(String table_name) {
		if (bindings.size() < 1) {
			throw new IllegalStateException("At least one binding is required");
		}
		StringBuilder builder = new StringBuilder("DROP TABLE IF EXISTS `")
				.append(table_name)
				.append("`; CREATE TABLE `")
				.append(table_name)
				.append("` (`id` INTEGER PRIMARY KEY");
		for (AbstractColumnBinding<?, ?> binding : bindings) {
			builder.append(", `").append(binding.getColumnName()).append("` ").append(binding.getColumnClass());
			if (binding.getColumnLength() > -1) {
				builder.append('(').append(binding.getColumnLength()).append(')');
			}
			if (!binding.canNull()) {
				builder.append(" NOT NULL");
			}
			if (binding.getDefaultValue() != null) {
				builder.append(" DEFAULT ");
				builder.append(binding.getDefaultValue());
			}
		}
		builder.append(");");
		return builder.toString();
	}

	/**
	 * Save the state of all bindings to a database
	 *
	 * @param db
	 * @param table_name
	 * @return
	 */
	public int save(SQLiteDatabase db, String table_name) {
		if (bindings.size() < 1) {
			throw new IllegalStateException("At least one binding is required");
		}
		ArrayList<Object> bindargs = new ArrayList<>(bindings.size());
		StringBuilder query_builder = new StringBuilder("INSERT OR REPLACE INTO `").append(table_name).append("` (");
		for (AbstractColumnBinding<?, ?> binding : bindings) {
			query_builder.append('`').append(binding.getColumnName()).append("`, ");
		}
		query_builder.delete(query_builder.length() - 2, query_builder.length())
				.append(") VALUES (");
		for (AbstractColumnBinding<?, ?> binding : bindings) {
			query_builder.append("?, ");
			bindargs.add(binding.getValue());
		}
		query_builder.delete(query_builder.length() - 2, query_builder.length())
				.append(");");
		db.execSQL(query_builder.toString(), bindargs.toArray());
		Cursor cursor = db.rawQuery("SELECT last_insert_rowid()", null);
		int id = -1;
		if (cursor.moveToNext()) {
			id = cursor.getInt(0);
		}
		cursor.close();
		return id;
	}

	/**
	 * Get an array of row IDs with fields matching the provided parameters (only '=' matching)
	 *
	 * @param db                the database to use
	 * @param table_name        the table to query
	 * @param search_parameters a map of column names and value fields
	 * @return array containing row IDs of matching rows
	 */
	public Integer[] queryRowsMatchingParameters(SQLiteDatabase db, String table_name, HashMap<String, Object> search_parameters) {
		StringBuilder query_builder = new StringBuilder("SELECT `id` FROM `")
				.append(table_name).append("`");
		appendWhereClause(query_builder, search_parameters);
		query_builder.append(';');
		Cursor cursor = db.rawQuery(query_builder.toString(), null);
		ArrayList<Integer> ids = new ArrayList<>(cursor.getCount());
		while (cursor.moveToNext()) {
			ids.add(cursor.getInt(0));
		}
		cursor.close();
		return ids.toArray(new Integer[ids.size()]);
	}

	/**
	 * Don't touch this unless it breaks
	 *
	 * @param query_builder
	 * @param search_parameters
	 */
	private void appendWhereClause(StringBuilder query_builder, HashMap<String, Object> search_parameters) {
		if (search_parameters.size() > 0) {
			query_builder.append(" WHERE ");
			for (Map.Entry<String, Object> search_parameter : search_parameters.entrySet()) {
				query_builder.append('`')
						.append(search_parameter.getKey())
						.append("`=");
				if (search_parameter.getValue() != null && search_parameter.getValue() instanceof CharSequence) {
					query_builder.append('\"');
				}
				if (search_parameter.getValue() != null && (search_parameter.getValue().getClass().equals(Boolean.class) || search_parameter.getValue().getClass().equals(boolean.class))) {
					query_builder.append(((boolean) search_parameter.getValue()) ? 1 : 0);
				}
				else {
					query_builder.append(search_parameter.getValue());
				}
				if (search_parameter.getValue() != null && search_parameter.getValue() instanceof CharSequence) {
					query_builder.append('\"');
				}
				query_builder.append(" AND ");
			}
			query_builder.delete(query_builder.length() - 5, query_builder.length());
		}
	}

	/**
	 * Delete rows with fields matching the provided parameters (only '=' matching)
	 *
	 * @param db                the database to use
	 * @param table_name        the table to query
	 * @param search_parameters a map of column names and value fields
	 */
	public void deleteRowsMatchingParameters(SQLiteDatabase db, String table_name, HashMap<String, Object> search_parameters) {
		StringBuilder query_builder = new StringBuilder("DELETE FROM `")
				.append(table_name).append("`");
		appendWhereClause(query_builder, search_parameters);
		query_builder.append(';');
		db.execSQL(query_builder.toString());
	}

	/**
	 * Load the state of all bindings from the database
	 *
	 * @param db         the database to use
	 * @param table_name the table to query
	 * @param id         the desired row ID
	 */
	@SuppressWarnings("unchecked")
	public void load(SQLiteDatabase db, String table_name, int id) {
		Cursor cursor = db.rawQuery("SELECT * FROM `" + table_name + "` WHERE `id`=" + id, null);
		if (cursor.moveToNext()) {
			for (AbstractColumnBinding binding : bindings) {
				int column_index = cursor.getColumnIndexOrThrow(binding.getColumnName());
				Class<?> value_class = binding.getValueClass();
				switch (cursor.getType(column_index)) {
					case Cursor.FIELD_TYPE_NULL:
						binding.setValue(null);
						break;
					case Cursor.FIELD_TYPE_INTEGER:
					case Cursor.FIELD_TYPE_FLOAT:
						switch (value_class.getSimpleName().toLowerCase()) {
							case "boolean":
								binding.setValue(cursor.getInt(column_index) != 0);
								break;
							case "byte":
								binding.setValue((byte) cursor.getInt(column_index));
								break;
							case "short":
								binding.setValue((short) cursor.getInt(column_index));
								break;
							case "char":
							case "character":
								binding.setValue((char) cursor.getInt(column_index));
								break;
							case "int":
							case "integer":
								binding.setValue(cursor.getInt(column_index));
								break;
							case "long":
								binding.setValue(cursor.getLong(column_index));
								break;
							case "float":
								binding.setValue(cursor.getFloat(column_index));
								break;
							case "double":
								binding.setValue(cursor.getDouble(column_index));
								break;
							case "string":
							case "charsequence":
								binding.setValue(cursor.getString(column_index));
								break;
							default:
								throw new IllegalArgumentException("Wrong class for column type " + cursor.getType(column_index));
						}
						break;
					case Cursor.FIELD_TYPE_STRING:
						switch (value_class.getSimpleName().toLowerCase()) {
							case "char":
							case "character":
								binding.setValue(cursor.getString(column_index).charAt(0));
								break;
							default:
								binding.setValue(cursor.getString(column_index));
								break;
						}
						break;
					case Cursor.FIELD_TYPE_BLOB:
						throw new IllegalArgumentException("I don't know how to handle a blob!");
				}
			}
		}
		cursor.close();
	}

	/**
	 * Save the state of all bindings to a new {@link android.os.Bundle}
	 *
	 * @return a new {@link android.os.Bundle} containing the state of all bindings
	 */
	public Bundle saveToBundle() {
		return saveToBundle(null);
	}

	/**
	 * Save the state of all bindings to an existing {@link android.os.Bundle}
	 *
	 * @return the {@link android.os.Bundle} containing the state of all bindings
	 */
	public Bundle saveToBundle(Bundle bundle) {
		if (bundle == null) {
			bundle = new Bundle(bindings.size());
		}
		for (AbstractColumnBinding<?, ?> binding : bindings) {
			binding.saveToBundle(bundle);
		}
		return bundle;
	}

	/**
	 * Load the state of all bindings from a {@link android.os.Bundle}
	 *
	 * @param bundle the bundle to load from
	 */
	public void loadFromBundle(Bundle bundle) {
		if (bundle == null) {
			resetAll();
		}
		else {
			for (AbstractColumnBinding<?, ?> binding : bindings) {
				binding.loadFromBundle(bundle);
			}
		}
	}

	/**
	 * Reset the state of all bindings
	 */
	public void resetAll() {
		for (AbstractColumnBinding<?, ?> binding : bindings) {
			binding.resetValue();
		}
	}
}
