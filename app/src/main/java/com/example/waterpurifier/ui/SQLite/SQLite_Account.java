package com.example.waterpurifier.ui.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.waterpurifier.ui.cart.contac_selectInformation;

public class SQLite_Account extends SQLiteOpenHelper {
    private static final String TAG = "SQLite_Account";

    static final String DB_NAME = "Account_List.db";
    static final String DB_TABLE_LOGIN = "registration";
    static final int DB_VERSION = 1;

    public SQLite_Account(Context context) {
        super(context, DB_NAME , null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE registration(ID INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT,password TEXT,phonenumber TEXT, address TEXT,name TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion != newVersion) {
            db.execSQL(" DROP TABLE IF EXISTS " + DB_TABLE_LOGIN);
            onCreate(db);
        }
    }

    public long addUser(String username, String password, String phonenumber, String address, String name) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("phonenumber", phonenumber);
        contentValues.put("address", address);
        contentValues.put("name", name);
        long res = db.insert("registration", null, contentValues);
        return res;
    }


    public boolean chekUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "select * from registration where username='"+ username +"' and password='"+ password+"'";
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(Query, null);//raw query always holds rawQuery(String Query,select args)
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(cursor!=null && cursor.getCount()>0){
            cursor.close();
            return true;
        }
        else{
            cursor.close();
            return false;
        }
    }
//    public String select_infor(String name, String phonenumber, String address)
//    {
//        SQLiteDatabase db = this.getReadableDatabase();
//        String Query =
//    }
public contac_selectInformation getProductByID(String Name) {
    contac_selectInformation selectInformation = null;
    SQLiteDatabase db = getReadableDatabase();
    Cursor cursor = db.rawQuery("SELECT name, phonenumber,address from registration where  name ='"+ Name+"'",
            new String[]{Name + ""});

    if (cursor.getCount() > 0) {
        cursor.moveToFirst();
        String name = cursor.getString(4);
        String phone = cursor.getString(5);
        String address = cursor.getString(6);
        selectInformation = new contac_selectInformation(name,phone,address);
    }
    cursor.close();
    return selectInformation;
}
}


