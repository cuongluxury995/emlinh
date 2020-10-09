package com.example.waterpurifier.ui.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.waterpurifier.ui.home.Contact_SPBanChay;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SQL_Helper extends SQLiteOpenHelper {
    private static final String TAG = "SQL_Helper";

    static final String DB_NAME = "Water_Purifier.db";
    static final String DB_TABLE = "ConTact_list_car";
    static final String DB_TABLE_LOGIN = "registration";
    static final int DB_VERSION = 1;
    SQLiteDatabase sqLiteDatabase;
    ContentValues contentValues;

    public SQL_Helper(Context context) {
        super(context, DB_NAME , null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryCreateTable = "Create Table ConTact_list_car(" +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "status TEXT," +
                "old_price TEXT," +
                "content TEXT," +
                "name TEXT," +
                "new_price TEXT," +
                "image TEXT)";

        db.execSQL(queryCreateTable);

        //db.execSQL("CREATE TABLE registration(ID INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT,password TEXT)");



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion != newVersion) {
            db.execSQL(" DROP TABLE IF EXISTS " + DB_TABLE);
            db.execSQL(" DROP TABLE IF EXISTS " + DB_TABLE_LOGIN);
            onCreate(db);
        }
    }


    public void insertCart(Contact_SPBanChay contact) {
        sqLiteDatabase = getWritableDatabase();
        contentValues = new ContentValues();

        contentValues.put("name", contact.getName_product());
        contentValues.put("new_price", contact.getNew_price());
        contentValues.put("image", contact.getImage());
        contentValues.put("old_price", contact.getOld_price());
        contentValues.put("status", contact.getStatus());
        contentValues.put("content", contact.getContent());
        sqLiteDatabase.insert(DB_TABLE, null, contentValues);

    }

    public boolean deleteAllProtect() {
        sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.delete(DB_TABLE, null, null) > 0;
    }

    public boolean deleteItemInCart(String name) {
        sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.delete(DB_TABLE, "name=?", new String[]{name}) >= 0;
    }

    public List<Contact_SPBanChay> getallProduct() {
        List<Contact_SPBanChay> contacts = new ArrayList<>();
        sqLiteDatabase = getWritableDatabase();
        Contact_SPBanChay contact;
        Cursor cursor = sqLiteDatabase.query(false, DB_TABLE,
                null,
                null,
                null,
                null,
                null,
                null,
                null);

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
//            Locale local =new Locale("vi","VN");
//            NumberFormat numberFormat = NumberFormat.getInstance(local);

            int new_price = (cursor.getInt(cursor.getColumnIndex("new_price")));
            String image = (cursor.getString(cursor.getColumnIndex("image")));
            String status = (cursor.getString(cursor.getColumnIndex("status")));
            String content = (cursor.getString(cursor.getColumnIndex("content")));
            String old_price = (cursor.getString(cursor.getColumnIndex("old_price")));
            contacts.add(new Contact_SPBanChay(new_price, old_price, content, image, name, status));
        }
        return contacts;
    }

    public long addUser(String username, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        long res = db.insert("registration", null, contentValues);
        return res;
    }

    public boolean chekUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "select * from registration where username='" + username + "' and password='" + password + "'";
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(Query, null);//raw query always holds rawQuery(String Query,select args)
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

}
