package com.agl.product.adw8_new.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class IAdv8Database extends SQLiteOpenHelper {
    //declare db name*******************************************
    public static String DbName="Iadv8.db";
    //Table name************************************************
    public static String FilterTable="Filter";
    //column Name***********************************************
    public static String FilterName="FilterName";
    public static String FilterChecked="FilterChecked";
    public static String FilterFlag="FilterFlag";
    public static String FilterIds="FilterIds";
    //declare Dbversion*****************************************
    public static int dbver=1;


    public IAdv8Database(Context context, String DbName, SQLiteDatabase.CursorFactory factory, int dbver) {
        super(context, DbName, factory, dbver);
        // TODO Auto-generated constructor stub
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
    String str="create table Filter(_id integer primary key autoincrement,FilterName text,FilterChecked text,FilterFlag text,FilterIds text);";
    db.execSQL(str);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
