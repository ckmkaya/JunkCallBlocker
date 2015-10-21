package xyz.ayadev.junkcallblocker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {
        super(context, "JunkCall.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase Database) {
        Database.execSQL("CREATE TABLE Business (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, ID INTEGER, Name CHAR, Desc CHAR)");
        Database.execSQL("CREATE TABLE Type (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, ID INTEGER, Name CHAR, Desc CHAR)");
        Database.execSQL("CREATE TABLE Level (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, ID INTEGER, Name CHAR, Desc CHAR)");
        Database.execSQL("CREATE TABLE Company (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, Name CHAR, Business INTEGER, Frequency INTEGER)");
        Database.execSQL("CREATE TABLE Phone (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, Record CHAR, Company CHAR, Number CHAR, Type CHAR, Date CHAR, Level CHAR, Business CHAR)");
        Database.execSQL("CREATE TABLE Phone0 (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, Record CHAR, Company CHAR, Number CHAR, Type CHAR, Date CHAR, Level CHAR, Business CHAR)");
        Database.execSQL("CREATE TABLE Phone1 (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, Record CHAR, Company CHAR, Number CHAR, Type CHAR, Date CHAR, Level CHAR, Business CHAR)");
        Database.execSQL("CREATE TABLE Phone2 (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, Record CHAR, Company CHAR, Number CHAR, Type CHAR, Date CHAR, Level CHAR, Business CHAR)");
        Database.execSQL("CREATE TABLE Phone3 (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, Record CHAR, Company CHAR, Number CHAR, Type CHAR, Date CHAR, Level CHAR, Business CHAR)");
        Database.execSQL("CREATE TABLE Phone4 (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, Record CHAR, Company CHAR, Number CHAR, Type CHAR, Date CHAR, Level CHAR, Business CHAR)");
        Database.execSQL("CREATE TABLE Phone5 (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, Record CHAR, Company CHAR, Number CHAR, Type CHAR, Date CHAR, Level CHAR, Business CHAR)");
        Database.execSQL("CREATE TABLE Phone6 (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, Record CHAR, Company CHAR, Number CHAR, Type CHAR, Date CHAR, Level CHAR, Business CHAR)");
        Database.execSQL("CREATE TABLE Phone7 (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, Record CHAR, Company CHAR, Number CHAR, Type CHAR, Date CHAR, Level CHAR, Business CHAR)");
        Database.execSQL("CREATE TABLE Phone8 (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, Record CHAR, Company CHAR, Number CHAR, Type CHAR, Date CHAR, Level CHAR, Business CHAR)");
        Database.execSQL("CREATE TABLE Phone9 (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, Record CHAR, Company CHAR, Number CHAR, Type CHAR, Date CHAR, Level CHAR, Business CHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase Database, int oldVersion, int newVersion) {
        Database.execSQL("DROP TABLE IF EXISTS Business");
        Database.execSQL("DROP TABLE IF EXISTS Type");
        Database.execSQL("DROP TABLE IF EXISTS Level");
        Database.execSQL("DROP TABLE IF EXISTS Company");
        Database.execSQL("DROP TABLE IF EXISTS Phone");
        Database.execSQL("DROP TABLE IF EXISTS Phone0");
        Database.execSQL("DROP TABLE IF EXISTS Phone1");
        Database.execSQL("DROP TABLE IF EXISTS Phone2");
        Database.execSQL("DROP TABLE IF EXISTS Phone3");
        Database.execSQL("DROP TABLE IF EXISTS Phone4");
        Database.execSQL("DROP TABLE IF EXISTS Phone5");
        Database.execSQL("DROP TABLE IF EXISTS Phone6");
        Database.execSQL("DROP TABLE IF EXISTS Phone7");
        Database.execSQL("DROP TABLE IF EXISTS Phone8");
        Database.execSQL("DROP TABLE IF EXISTS Phone9");
        onCreate(Database);
    }

}