package com.example.keyper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "servicedata.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       db.execSQL("CREATE TABLE Services(name TEXT primary key, password TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Services");
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Services", null);
        return cursor;
        }

    public Boolean equalsMasterKey(String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT password FROM Master WHERE name = ?", new String[] {"key"});

        if (cursor.moveToFirst()) {
            String str = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            if (password.equals(str)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public Boolean insertServicePassword(String name, String password) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", name);
            contentValues.put("password", password);
            long result = db.insert("Services", null, contentValues);
            if (result==-1) {
                return false;
            } else {
                return true;
            }
    }

    public Boolean updateServicePassword(String name, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("password", password);
        Cursor cursor = db.rawQuery("SELECT * FROM Services WHERE name = ?", new String[] {name});
        if (cursor.getCount()>0) {
            long result = db.update("Services", contentValues, "name=?", new String[] {name});
            if (result==-1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public Boolean deleteServicePassword(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Services WHERE name = ?", new String[] {name});
        if (cursor.getCount()>0) {
            long result = db.delete("Services", "name=?", new String[] {name});
            if (result==-1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

}
