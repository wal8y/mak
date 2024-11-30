package com.example.sqllight;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBmang {
    private SQLiteDatabase sqlDB;
    static final String DBName = "expenses";
    static final String TableName = "items";
    static final String amount_name = "cash";
    static final String date_name = "date";
    static final String type_name = "type";
    static final int DBversion = 1;

    static final String CreateTable = "CREATE TABLE " + TableName +
            "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            type_name + " TEXT, " +
            date_name + " TEXT, " +
            amount_name + " INTEGER)";

    static class DBHelperUser extends SQLiteOpenHelper {
        DBHelperUser(Context context) {
            super(context, DBName, null, DBversion);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CreateTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TableName);
            onCreate(db);
        }
    }

    public DBmang(Context context) {
        DBHelperUser db = new DBHelperUser(context);
        sqlDB = db.getWritableDatabase();
    }

    public long insertExpense(String type, String date, int cash) {
        ContentValues values = new ContentValues();
        values.put(type_name, type);
        values.put(date_name, date);
        values.put(amount_name, cash);
        return sqlDB.insert(TableName, null, values);
    }

    public List<Expense> getAllExpenses() {
        List<Expense> expenses = new ArrayList<>();
        Cursor cursor = sqlDB.rawQuery("SELECT type, SUM(cash) as total FROM " + TableName + " GROUP BY type", null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String type = cursor.getString(cursor.getColumnIndex(type_name));
                @SuppressLint("Range") int cash = cursor.getInt(cursor.getColumnIndex("total"));
                expenses.add(new Expense(0, type, "", cash));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return expenses;
    }

    public List<Expense> getMonthlyExpenses() {
        List<Expense> monthlyExpenses = new ArrayList<>();
        Cursor cursor = sqlDB.rawQuery(
                "SELECT strftime('%Y-%m', date) AS month, SUM(cash) AS total FROM " + TableName +
                        " GROUP BY month", null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int cash = cursor.getInt(cursor.getColumnIndex("total"));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("month"));
                monthlyExpenses.add(new Expense(0, "Total", date, cash));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return monthlyExpenses;
    }

    public List<Expense> getExpensesByType(String type) {
        List<Expense> expenses = new ArrayList<>();
        Cursor cursor = sqlDB.rawQuery("SELECT * FROM " + TableName + " WHERE type = ?", new String[]{type});
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("ID"));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(date_name));
                @SuppressLint("Range") int cash = cursor.getInt(cursor.getColumnIndex(amount_name));
                expenses.add(new Expense(id, type, date, cash));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return expenses;
    }

    public long deleteExpense(int id) {
        return sqlDB.delete(TableName, "ID = ?", new String[]{String.valueOf(id)});
    }
}

// Expense Model Class
class Expense {
    int id;
    String type;
    String date;
    int cash;

    public Expense(int id, String type, String date, int cash) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.cash = cash;
    }
}
