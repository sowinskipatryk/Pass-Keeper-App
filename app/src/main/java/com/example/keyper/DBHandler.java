package com.example.keyper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.scottyab.aescrypt.AESCrypt;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {
    public DBHandler(Context context) {
        super(context, "servicedata.db", null, 1);
    }

    public String cryptCode = "SuperSecretCode";

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
                String encryptedName = cursor.getString(0);
                String encryptedPassword = cursor.getString(1);
                try {
                    String decryptedName = AESCrypt.decrypt(cryptCode, encryptedName);
                    String decryptedPassword = AESCrypt.decrypt(cryptCode, encryptedPassword);
                    services.add(new Service(decryptedName, decryptedPassword));
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return services;
    }

    public Boolean equalsMasterKey(String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String encryptedName = AESCrypt.encrypt(cryptCode, "Key");
            String encryptedPassword = AESCrypt.encrypt(cryptCode, password);
            Cursor cursor = db.rawQuery("SELECT password FROM Master WHERE name = ?", new String[] {encryptedName});
            if (cursor.moveToFirst()) {
                String str = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                cursor.close();
                return encryptedPassword.equals(str);
            } else {
                return false;
            }
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean setMasterKey() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String encryptedName = AESCrypt.encrypt(cryptCode, "Key");
            String encryptedPassword = AESCrypt.encrypt(cryptCode, "1234");
            Cursor cursor = db.rawQuery("SELECT password FROM Master WHERE name = ?", new String[] {encryptedName});
            if (cursor.moveToFirst()) {
                return false;
            } else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("name", encryptedName);
                contentValues.put("password", encryptedPassword);
                long result = db.insert("Master", null, contentValues);
                return result != -1;
            }
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean updateMasterKey(String password) {
        try {
            String encryptedName = AESCrypt.encrypt(cryptCode, "Key");
            String encryptedPassword = AESCrypt.encrypt(cryptCode, password);
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", encryptedName);
            contentValues.put("password", encryptedPassword);
            long result = db.update("Master", contentValues, "name=?", new String[] {encryptedName});
            return result != -1;
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean insertServicePassword(String name, String password) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            try {
                String encryptedName = AESCrypt.encrypt(cryptCode, name);
                String encryptedPassword = AESCrypt.encrypt(cryptCode, password);
                contentValues.put("name", encryptedName);
                contentValues.put("password", encryptedPassword);
                long result = db.insert("Services", null, contentValues);
                return result != -1;
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
                return false;
            }
    }

    public Boolean updateServicePassword(String name, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        try {
            String encryptedName = AESCrypt.encrypt(cryptCode, name);
            String encryptedPassword = AESCrypt.encrypt(cryptCode, password);
            contentValues.put("name", encryptedName);
            contentValues.put("password", encryptedPassword);
            Cursor cursor = db.rawQuery("SELECT * FROM Services WHERE name = ?", new String[] {encryptedName});
            if (cursor.getCount()>0) {
                cursor.close();
                long result = db.update("Services", contentValues, "name=?", new String[] {encryptedName});
                return result != -1;
            } else {
                return false;
            }
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean deleteServicePassword(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String encryptedName = AESCrypt.encrypt(cryptCode, name);
            Cursor cursor = db.rawQuery("SELECT * FROM Services WHERE name = ?", new String[] {encryptedName});
            if (cursor.getCount()>0) {
                cursor.close();
                long result = db.delete("Services", "name=?", new String[] {encryptedName});
                return result != -1;
            } else {
                return false;
            }
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            return false;
        }
    }

}
