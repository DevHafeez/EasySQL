package com.hafeez.easysql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EasySQL extends SQLiteOpenHelper {

    public static final String DEFAULT_DATABASE_NAME = "my_easy_sql_database.db";
    private static final int DATABASE_VERSION = 1;

    private String currentTableName;
    private OnTableCreatedListener tableCreatedListener;
    private OnErrorListener errorListener;

    public static final String COLUMN_TYPE_TEXT = "TEXT";
    public static final String COLUMN_TYPE_INTEGER = "INTEGER";
    public static final String COLUMN_TYPE_REAL = "REAL";
    public static final String COLUMN_TYPE_BLOB = "BLOB";
    public static final String COLUMN_TYPE_INT_PRIMARY_KEY = "int PRIMARY KEY";
    public static final String COLUMN_TYPE_INT_PRIMARY_KEY_AUTO_INC = "int PRIMARY KEY AUTOINCREMENT";

    public EasySQL(Context context, String databaseName) {
        super(context, databaseName, null, DATABASE_VERSION);
    }

    public interface OnTableCreatedListener {
        void onTableCreated();
    }

    public interface OnErrorListener {
        void onError(String errorMessage);
    }

    public EasySQL createTable(String tableName) {
        currentTableName = tableName;
        return this;
    }

    public EasySQL setOnTableCreatedListener(OnTableCreatedListener listener) {
        tableCreatedListener = listener;
        return this;
    }

    public EasySQL setOnErrorListener(OnErrorListener listener) {
        errorListener = listener;
        return this;
    }

    public EasySQL addColumn(String columnName, String columnType) {
        StringBuilder columnDefinition = new StringBuilder();
        columnDefinition.append(columnName)
                .append(" ")
                .append(columnType);

        return addColumnToDB(columnDefinition.toString());
    }

    private EasySQL addColumnToDB(String columnDefinition) {
        StringBuilder alterTableQuery = new StringBuilder();
        alterTableQuery.append("ALTER TABLE ")
                .append(currentTableName)
                .append(" ADD COLUMN ")
                .append(columnDefinition);

        try {
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL(alterTableQuery.toString());
            db.close();

            if (tableCreatedListener != null) {
                tableCreatedListener.onTableCreated();
            }
        } catch (Exception e) {
            if (errorListener != null) {
                errorListener.onError(e.getMessage());
            }
        }

        return this;
    }

    private ContentValues insertValues;

    public EasySQL insertInto(String tableName) {
        insertValues = new ContentValues();
        currentTableName = tableName;
        return this;
    }

    public EasySQL add(String columnName, Object value) {
        if (insertValues != null) {
            if (value instanceof String) {
                insertValues.put(columnName, (String) value);
            } else if (value instanceof Integer) {
                insertValues.put(columnName, (int) value);
            } else if (value instanceof Long) {
                insertValues.put(columnName, (long) value);
            } else if (value instanceof Float) {
                insertValues.put(columnName, (float) value);
            } else if (value instanceof Double) {
                insertValues.put(columnName, (double) value);
            } else if (value instanceof Boolean) {
                insertValues.put(columnName, (boolean) value);
            } else {

            }
        }
        return this;
    }

    public void executeInsert(final InsertCallback callback) {
        SQLiteDatabase db = getWritableDatabase();
        long rowId = db.insert(currentTableName, null, insertValues);
        db.close();

        if (rowId != -1) {
            if (callback != null) {
                callback.onInsertSuccess(rowId);
            }
        } else {
            if (callback != null) {
                callback.onInsertFailure();
            }
        }
    }

    public interface InsertCallback {
        void onInsertSuccess(long rowId);

        void onInsertFailure();
    }

    private String selectTableName;
    private String whereColumn;
    private Object whereValue;

    public EasySQL selectFrom(String tableName) {
        selectTableName = tableName;
        return this;
    }

    public List<HashMap<String, Object>> where(String columnName, Object value) {
        whereColumn = columnName;
        whereValue = value;
        List<HashMap<String, Object>> resultList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + selectTableName + " WHERE " + columnName + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(value)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                HashMap<String, Object> rowMap = new HashMap<>();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    String column = cursor.getColumnName(i);
                    Object columnValue = null;
                    int columnType = cursor.getType(i);

                    switch (columnType) {
                        case Cursor.FIELD_TYPE_NULL:
                            break;
                        case Cursor.FIELD_TYPE_INTEGER:
                            columnValue = cursor.getInt(i);
                            break;
                        case Cursor.FIELD_TYPE_FLOAT:
                            columnValue = cursor.getFloat(i);
                            break;
                        case Cursor.FIELD_TYPE_STRING:
                            columnValue = cursor.getString(i);
                            break;
                        case Cursor.FIELD_TYPE_BLOB:
                            columnValue = cursor.getBlob(i);
                            break;
                    }

                    rowMap.put(column, columnValue);
                }
                resultList.add(rowMap);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return resultList;
    }

    private String updateTableName;
    private String updateColumnName;
    private Object updateNewValue;

    public EasySQL updateIn(String tableName) {
        updateTableName = tableName;
        return this;
    }

    public EasySQL set(String columnName, Object value) {
        updateColumnName = columnName;
        updateNewValue = value;
        return this;
    }

    public void executeUpdate(final UpdateCallback callback) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(updateColumnName, String.valueOf(updateNewValue));

        int rowsAffected = db.update(updateTableName, values, null, null);

        if (rowsAffected > 0) {
            if (callback != null) {
                callback.onUpdateSuccess(rowsAffected);
            }
        } else {
            if (callback != null) {
                callback.onUpdateFailure();
            }
        }

        db.close();
    }

    public void deleteTable(String tableName, final DeleteCallback callback) {
        SQLiteDatabase db = getWritableDatabase();
        int rowsAffected = db.delete(tableName, null, null);

        if (rowsAffected > 0) {
            if (callback != null) {
                callback.onDeleteSuccess(rowsAffected);
            }
        } else {
            if (callback != null) {
                callback.onDeleteFailure();
            }
        }

        db.close();
    }

    public EasySQL deleteFrom(String tableName) {
        currentTableName = tableName;
        return this;
    }

    public EasySQL with(String columnName, Object value) {
        whereColumn = columnName;
        whereValue = value;
        return this;
    }

    public void executeDelete(final DeleteCallback callback) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = whereColumn + " = ?";
        String[] whereArgs = new String[]{String.valueOf(whereValue)};
        int rowsAffected = db.delete(currentTableName, whereClause, whereArgs);
        db.close();

        if (rowsAffected > 0) {
            if (callback != null) {
                callback.onDeleteSuccess(rowsAffected);
            }
        } else {
            if (callback != null) {
                callback.onDeleteFailure();
            }
        }
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // No implementation needed here
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No implementation needed here
    }

    public interface UpdateCallback {
        void onUpdateSuccess(int rowsAffected);

        void onUpdateFailure();
    }

    public interface DeleteCallback {
        void onDeleteSuccess(int rowsAffected);

        void onDeleteFailure();
    }
}

