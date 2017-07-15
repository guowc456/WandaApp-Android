package com.wanta.mobile.wantaproject.mordel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wanta.mobile.wantaproject.domain.MostPopularInfo;
import com.wanta.mobile.wantaproject.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangYongqiang on 2016/12/24.
 */
public class MostPopularHelper extends DbHelper {

    private final String TABLE_NAME = "mostpopular_data";
    private final String MostPopular_icnfig = "icnfid";//首页icon的id
    private final String MostPopular_title = "title";//首页标题
    private final String MostPopular_content = "content";//首页内容
    private final String MostPopular_likenum = "likenum";//点赞数
    private final String MostPopular_storenum = "storenum";//收藏数
    private final String MostPopular_userid = "userid";//用户id
    private final String MostPopular_username = "username";//用户名字
    private final String MostPopular_useravatar = "useravatar";//用户图标
    private final String MostPopular_favourstate = "favourstate";//是否已被该用户点赞
    private final String MostPopular_storedstate = "storedstate";//是否已被该用户收藏
    private final String MostPopular_lng = "lng";//经度
    private final String MostPopular_lat = "lat";//纬度
    private final String MostPopular_address = "address";//地点
    private final String MostPopular_createdat = "createdat";//创建时间
    private final String MostPopular_images = "images";//图片集合
    private final String MostPopular_cmtnum = "cmtnum";//评论数

    public MostPopularHelper(Context context) {
        super(context);
    }

    public synchronized SQLiteDatabase open(){
        SQLiteDatabase writableDatabase = getWritableDatabase();
        return writableDatabase;
    }

    public void insertMessage(String icnfig,String title,String content,String likenum,String storenum,
                              String userid,String username,String useravatar,String favourstate,
                              String storedstate,String lng,String lat,String address,String createdat,String images,int cmtnum,SQLiteDatabase sd){
        ContentValues values = new ContentValues();
        values.put(MostPopular_icnfig,icnfig);
        values.put(MostPopular_title,title);
        values.put(MostPopular_content,content);
        values.put(MostPopular_likenum,likenum);
        values.put(MostPopular_storenum,storenum);
        values.put(MostPopular_userid,userid);
        values.put(MostPopular_username,username);
        values.put(MostPopular_useravatar,useravatar);
        values.put(MostPopular_favourstate,favourstate);
        values.put(MostPopular_storedstate,storedstate);
        values.put(MostPopular_lng,lng);
        values.put(MostPopular_lat,lat);
        values.put(MostPopular_address,address);
        values.put(MostPopular_createdat,createdat);
        values.put(MostPopular_images,images);
        values.put(MostPopular_cmtnum,cmtnum);
        sd.replace(TABLE_NAME,null,values);
    }
//    //获取首页一级界面显示的信息
//    public synchronized List<MostPopularInfo> getFirstMessage(){
//        List<MostPopularInfo> list = new ArrayList<>();
//        SQLiteDatabase readableDatabase = getReadableDatabase();
//        String sql = " select * from  mostpopular_data order by icnfid DESC";
//        Cursor cursor = readableDatabase.rawQuery(sql, null);
//        while (cursor.moveToNext()){
//            int icnfid = cursor.getInt(cursor.getColumnIndex("icnfid"));
//            String title = cursor.getString(cursor.getColumnIndex("title"));
//            String content = cursor.getString(cursor.getColumnIndex("content"));
//            String useravatar = cursor.getString(cursor.getColumnIndex("useravatar"));
//            String username = cursor.getString(cursor.getColumnIndex("username"));
//            int likenum = cursor.getInt(cursor.getColumnIndex("likenum"));
//            String images = cursor.getString(cursor.getColumnIndex("images"));
//            int storenum = cursor.getInt(cursor.getColumnIndex("storenum"));
//            int userid = cursor.getInt(cursor.getColumnIndex("userid"));
//            String favourstate = cursor.getString(cursor.getColumnIndex("favourstate"));
//            String storedstate = cursor.getString(cursor.getColumnIndex("storedstate"));
//            String lng = cursor.getString(cursor.getColumnIndex("lng"));
//            String lat = cursor.getString(cursor.getColumnIndex("lat"));
//            String address = cursor.getString(cursor.getColumnIndex("address"));
//            String createdat = cursor.getString(cursor.getColumnIndex("createdat"));
//            String weight = cursor.getString(cursor.getColumnIndex("weight"));
//            String bust = cursor.getString(cursor.getColumnIndex("bust"));
//            String uaddress = cursor.getString(cursor.getColumnIndex("uaddress"));
//            String height = cursor.getString(cursor.getColumnIndex("height"));
//            String followstate = cursor.getString(cursor.getColumnIndex("followstate"));
//            String bra = cursor.getString(cursor.getColumnIndex("bra"));
//            int cmtnum = cursor.getInt(cursor.getColumnIndex("cmtnum"));
//
//
//            MostPopularInfo mostPopularInfo = new MostPopularInfo(icnfid,title,content,likenum,storenum,userid,username,useravatar,favourstate,storedstate,
//                    lng,lat,address,createdat,images,weight,bust,uaddress,height,followstate,bra,cmtnum);
//            LogUtils.showVerbose("MostPopularHelper","images="+images);
//            list.add(mostPopularInfo);
//        }
//        return list;
//    }
//    //获取首页二级界面显示的信息
//    public synchronized List<MostPopularInfo> getSecondMessage(){
//        List<MostPopularInfo> list = new ArrayList<>();
//        SQLiteDatabase readableDatabase = getReadableDatabase();
//        String sql = " select * from  mostpopular_data";
//        Cursor cursor = readableDatabase.rawQuery(sql, null);
//        while (cursor.moveToNext()){
//            String icnfid = cursor.getString(cursor.getColumnIndex("icnfid"));
//            String title = cursor.getString(cursor.getColumnIndex("title"));
//            String content = cursor.getString(cursor.getColumnIndex("content"));
//            String useravatar = cursor.getString(cursor.getColumnIndex("useravatar"));
//            String username = cursor.getString(cursor.getColumnIndex("username"));
//            String likenum = cursor.getString(cursor.getColumnIndex("likenum"));
//            String images = cursor.getString(cursor.getColumnIndex("images"));
//            String storenum = cursor.getString(cursor.getColumnIndex("storenum"));
//            String userid = cursor.getString(cursor.getColumnIndex("userid"));
//            String favourstate = cursor.getString(cursor.getColumnIndex("favourstate"));
//            String storedstate = cursor.getString(cursor.getColumnIndex("storedstate"));
//            String lng = cursor.getString(cursor.getColumnIndex("lng"));
//            String lat = cursor.getString(cursor.getColumnIndex("lat"));
//            String address = cursor.getString(cursor.getColumnIndex("address"));
//            String createdat = cursor.getString(cursor.getColumnIndex("createdat"));
//
//            MostPopularInfo mostPopularInfo = new MostPopularInfo(icnfid,title,content,useravatar,username,likenum,images);
//            LogUtils.showVerbose("MostPopularHelper","images="+images);
//            list.add(mostPopularInfo);
//        }
//        return list;
//    }

    public synchronized void delete(){
        String sql = "  delete from mostpopular_data";
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.execSQL(sql);
    }

}
