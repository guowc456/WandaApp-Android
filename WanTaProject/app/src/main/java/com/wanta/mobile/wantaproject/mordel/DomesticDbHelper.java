package com.wanta.mobile.wantaproject.mordel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wanta.mobile.wantaproject.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangYongqiang on 2016/12/17.
 */
public class DomesticDbHelper extends DbHelper {
    private String TABLE_NAME = "location_data";//当前的表名
    private String Domestic_Country = "country";//记录区的名字
    private String Domestic_Character = "name";//记录区的首字母

    public DomesticDbHelper(Context context) {
        super(context);
    }
    //获取数据库对象
    public SQLiteDatabase open(){
        SQLiteDatabase writableDatabase = getWritableDatabase();
        return writableDatabase;
    }
    public void delete(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql="delete from location_data";
        db.execSQL(sql);
    }
    //往数据库中插入数据
    public void insertMessage(String name,String country, SQLiteDatabase sd){
        ContentValues values = new ContentValues();
        values.put(Domestic_Character,name);
        values.put(Domestic_Country,country);
        sd.replace(TABLE_NAME,null,values);
    }

    /**
     * //获取对应字母的区域
     * @param string
     * @return
     */
    public List<String> getCloumnMessage(String string){
        List<String> array = new ArrayList<>();
        String sql = " select * from location_data where name = '"+string+"'";
        LogUtils.showVerbose("DomesticDbHelper","sql="+sql);
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery(sql, null);
        while (cursor.moveToNext()){
            String county = cursor.getString(cursor.getColumnIndex("country"));
//            LogUtils.showVerbose("DomesticDbHelper","county="+county);
            array.add(county);
        }
        LogUtils.showVerbose("DomesticDbHelper","size="+array.size());
        cursor.close();
        return array;
    }
    public List<String> getAllMessage(){
        List<String> array = new ArrayList<>();
        String sql = " select * from location_data ";
        LogUtils.showVerbose("DomesticDbHelper","sql="+sql);
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery(sql, null);
        while (cursor.moveToNext()){
//            String county = cursor.getString(1);
            String county = cursor.getString(cursor.getColumnIndex("country"));
//            LogUtils.showVerbose("DomesticDbHelper","county="+county);
            array.add(county);
        }
        LogUtils.showVerbose("DomesticDbHelper","size="+array.size());
        cursor.close();
        return array;
    }
}
