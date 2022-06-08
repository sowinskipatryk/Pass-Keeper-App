package com.example.keyper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {
    public DBHandler(Context context) {
        super(context, "servicedata.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Master(name TEXT UNIQUE primary key, password TEXT)");
       db.execSQL("CREATE TABLE IF NOT EXISTS Services(name TEXT UNIQUE primary key, password TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Services");
    }

    public ArrayList<Service> returnPasswords() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Services", null);
        ArrayList<Service> services = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                services.add(new Service(cursor.getString(0),
                        cursor.getString(1)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return services;
    }

    public Boolean equalsMasterKey(String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT password FROM Master WHERE name = ?", new String[] {"Key"});

        if (cursor.moveToFirst()) {
            String str = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            cursor.close();
            return password.equals(str);
        } else {
            return false;
        }
    }

    public Boolean setMasterKey() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", "Key");
        contentValues.put("password", "1234");
        long result = db.insert("Master", null, contentValues);
        return result != -1;
    }

    public Boolean insertServicePassword(String name, String password) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase());
            contentValues.put("password", password);
            long result = db.insert("Services", null, contentValues);
        return result != -1;
    }

    public Boolean updateServicePassword(String name, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("password", password);
        Cursor cursor = db.rawQuery("SELECT * FROM Services WHERE name = ?", new String[] {name});
        if (cursor.getCount()>0) {
            cursor.close();
            long result = db.update("Services", contentValues, "name=?", new String[] {name});
            return result != -1;
        } else {
            return false;
        }
    }

    public Boolean deleteServicePassword(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Services WHERE name = ?", new String[] {name});
        if (cursor.getCount()>0) {
            cursor.close();
            long result = db.delete("Services", "name=?", new String[] {name});
            return result != -1;
        } else {
            return false;
        }
    }

}
