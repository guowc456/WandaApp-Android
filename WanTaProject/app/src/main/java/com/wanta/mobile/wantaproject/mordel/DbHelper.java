package com.wanta.mobile.wantaproject.mordel;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wanta.mobile.wantaproject.utils.Constants;

/**
 * Created by WangYongqiang on 2016/12/17.
 */
public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {
        super(context, Constants.WANTA_DB_NAME, null, Constants.WANTA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sq1 = " create table if not exists location_data(name varchar(10),country text); ";
        String sq2 = " create table if not exists mostpopular_data(" +
                "icnfid varchar(100),title text,content text,likenum varchar(100),storenum varchar(100),userid text,username text,useravatar text," +
                "favourstate varchar(100),storedstate varchar(100),lng varchar(100),lat varchar(100),address text,createdat text,images text," +
                "weight varchar(100),bust varchar(100),uaddress varchar(100),height varchar(100),followstate varchar(100),bra varchar(100),cmtnum INTEGER); ";
        String sql3 = " create table if not exists comment_data( cmtid INTEGER,comment text,createdat text,rpnum INTEGER," +
                "userid INTEGER,username text,avatar text,cmtfavour varchar(100)); ";
        db.execSQL(sq1);
        db.execSQL(sq2);
        db.execSQL(sql3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists location_data");
        db.execSQL("drop table if exists mostpopular_data");
        db.execSQL("drop table if exists comment_data");
        this.onCreate(db);
    }
}
