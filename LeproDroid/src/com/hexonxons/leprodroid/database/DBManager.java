package com.hexonxons.leprodroid.database;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager
{
    private DbOpenHelper mHelper        = null;
    private SQLiteDatabase mDatabase    = null;
    
    public DBManager(Context context)
    {
        mHelper = new DbOpenHelper(context);
        mDatabase = mHelper.getWritableDatabase();
    }
    
    /**
     * Save cookies to database.
     * @param cookies
     */
    public void saveCookies(Cookie[] cookies)
    {
        // Save cookies in single transaction.
        mDatabase.beginTransaction();
        
        for(Cookie cookie : cookies)
        {
            ContentValues values = new ContentValues();
            values.put(DbOpenHelper.COOKIES_COMMENT, cookie.getComment());
            values.putNull(DbOpenHelper.COOKIES_COMMENT_URL);
            values.put(DbOpenHelper.COOKIES_DOMAIN, cookie.getDomain());
            values.putNull(DbOpenHelper.COOKIES_EXPIRY_DATE);
            values.put(DbOpenHelper.COOKIES_NAME, cookie.getName());
            values.put(DbOpenHelper.COOKIES_PATH, cookie.getPath());
            values.putNull(DbOpenHelper.COOKIES_PORT);
            values.put(DbOpenHelper.COOKIES_VALUE, cookie.getValue());
            values.put(DbOpenHelper.COOKIES_VERSION, cookie.getVersion());
            values.putNull(DbOpenHelper.COOKIES_EXPIRED);
            values.putNull(DbOpenHelper.COOKIES_PERSISTENT);
            values.put(DbOpenHelper.COOKIES_SECURE, cookie.isSecure());
            
            mDatabase.insert(DbOpenHelper.COOKIES_TABLE, null, values);
        }
        
        // Set transaction successful.
        mDatabase.setTransactionSuccessful();
        // End transaction.
        mDatabase.endTransaction();
    }
    
    /**
     * @return saved cookies array.
     */
    public BasicClientCookie[] getCookies()
    {
        Cursor cursor = mDatabase.query(DbOpenHelper.COOKIES_TABLE, null, null, null, null, null, null);
        
        // No cookies or error happens.
        if(cursor.getColumnIndex(DbOpenHelper.COOKIES_NAME) == -1 || cursor.isAfterLast())
        {
            cursor.close();
            return null;
        }
        
        cursor.moveToFirst();
        
        BasicClientCookie[] cookies = new BasicClientCookie[cursor.getCount()];
        int index = 0;
        
        while(!cursor.isAfterLast())
        {
            BasicClientCookie cookie = new BasicClientCookie(
                    cursor.getString(cursor.getColumnIndex(DbOpenHelper.COOKIES_NAME)), 
                    cursor.getString(cursor.getColumnIndex(DbOpenHelper.COOKIES_VALUE)));
            
            cookie.setComment(cursor.getString(cursor.getColumnIndex(DbOpenHelper.COOKIES_COMMENT)));
            cookie.setDomain(cursor.getString(cursor.getColumnIndex(DbOpenHelper.COOKIES_DOMAIN)));
            cookie.setPath(cursor.getString(cursor.getColumnIndex(DbOpenHelper.COOKIES_PATH)));
            cookie.setVersion(cursor.getInt(cursor.getColumnIndex(DbOpenHelper.COOKIES_VERSION)));
            cookie.setSecure(cursor.getInt(cursor.getColumnIndex(DbOpenHelper.COOKIES_SECURE)) == 0 ? false : true);
            
            cookies[index++] = cookie;
            
            cursor.moveToNext();
        }
        
        cursor.close();
        
        return cookies;
    }
}
