package com.hexonxons.leprodroid.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbOpenHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME   = "leprodroid.db";
    private static final int DATABASE_VERSION   = 1;
    
    /**
     * Cookie table.
     * http://developer.android.com/reference/org/apache/http/cookie/Cookie.html
     */
    
    public static final String COOKIES_TABLE        = "cookies_table";
    public static final String COOKIES_COMMENT      = "_comment";
    public static final String COOKIES_COMMENT_URL  = "_comment_url";       // TODO: only null now
    public static final String COOKIES_DOMAIN       = "_domain";
    public static final String COOKIES_EXPIRY_DATE  = "_expiry_date";       // TODO: only null now
    public static final String COOKIES_NAME         = "_name";
    public static final String COOKIES_PATH         = "_path";
    public static final String COOKIES_PORT         = "_port";              // TODO: only null now
    public static final String COOKIES_VALUE        = "_value";
    public static final String COOKIES_VERSION      = "_version";
    public static final String COOKIES_EXPIRED      = "_expired";           // TODO: only null now
    public static final String COOKIES_PERSISTENT   = "_persistent";        // TODO: only null now
    public static final String COOKIES_SECURE       = "_secure";
    
    private static final String COOKIES_TABLE_CREATE    = "create table if not exists " + COOKIES_TABLE         + "("
                                                                                        + COOKIES_COMMENT       + " text, "
                                                                                        + COOKIES_COMMENT_URL   + " text, "
                                                                                        + COOKIES_DOMAIN        + " text not null, "
                                                                                        + COOKIES_EXPIRY_DATE   + " text, "
                                                                                        + COOKIES_NAME          + " text not null unique primary key, "
                                                                                        + COOKIES_PATH          + " text not null, "
                                                                                        + COOKIES_PORT          + " text, "
                                                                                        + COOKIES_VALUE         + " text, "
                                                                                        + COOKIES_VERSION       + " integer, "
                                                                                        + COOKIES_EXPIRED       + " integer, "
                                                                                        + COOKIES_PERSISTENT    + " integer, "
                                                                                        + COOKIES_SECURE        + " integer);";
    
    public DbOpenHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.d("CoreDbOpenHelper", "Creating table " + COOKIES_TABLE);
        db.execSQL(COOKIES_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.d("CoreDbOpenHelper", "Upgrade database from version " + oldVersion + " to " + newVersion);
    }
}
