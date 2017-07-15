package com.wanta.mobile.wantaproject.mordel;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wanta.mobile.wantaproject.utils.Constants;
import com.wanta.mobile.wantaproject.utils.LogUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by WangYongqiang on 2016/12/17.
 */
public class DataCatogryHelper {

    private static InputStream inputStream;
    private static OutputStream outputstream;
    private static File cacheFile;
    private static String[] mStrings3 = {
            "当前", "历史", "热门", "a", "b", "c", "d", "e", "f", "g", "h", "j", "k", "l", "m", "n", "p", "q", "r", "s", "t", "w", "x", "y", "z"
    };
    private static String[] mStrings4 = {
            "当前", "历史", "热门", "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "W", "X", "Y", "Z"
    };
    private static String[] mStrings = {
            "北京", "上海", "天津"
    };

    //把国内的区的数据插入到数据库中
    public static void initDomestic(Activity context) {

        String currentPath = "";
        if(android.os.Build.VERSION.SDK_INT >= 4.2){
            currentPath= context.getApplicationInfo().dataDir+"/databases/wanta_location.db";
        } else {
            currentPath="/data/data/" + context.getPackageName() + "/databases/wanta_location.db";
        }
//        String currentPath = "/data/data/" + context.getPackageName() + "/databases/wanta_location.db";

        //产生文件目录
        File file = new File(currentPath);
        File fileParent = file.getParentFile();
        if (!fileParent.exists()) {
            fileParent.mkdirs();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            LogUtils.showVerbose("DataCatogryHelper", "创建文件失败");

        }
        //产生对应的文件
        LogUtils.showVerbose("DataCatogryHelper", "路径是否存在" + file.exists());
        try {
            inputStream = context.getResources().getAssets().open("wanta_location.db");

        } catch (IOException e) {
            LogUtils.showVerbose("DataCatogryHelper", "读取assets中的文件错误");
        }
        //要明确FileOutputStream并不会帮你创建不存在的路径，所以要先创建路径，再创建文件。
        try {
            outputstream = new FileOutputStream(currentPath);
        } catch (FileNotFoundException e) {
            LogUtils.showVerbose("DataCatogryHelper", "文件没有找到异常");
        }
        byte[] buffer = new byte[1024];
        int length = 0;
        try {
            while ((length = inputStream.read(buffer)) > 0) {
                outputstream.write(buffer, 0, length);
            }
            outputstream.flush();
            outputstream.close();
            inputStream.close();
        } catch (IOException e) {
            LogUtils.showVerbose("DataCatogryHelper", "文件读取流异常");
        }


        if (file.exists()) {
            LogUtils.showVerbose("DataCatogryHelper", "目录存在");
            DomesticDbHelper helper = new DomesticDbHelper(context);
            helper.delete();
            SQLiteDatabase open = helper.open();
            open.beginTransaction();
            SQLiteDatabase database = SQLiteDatabase.openDatabase(currentPath, null, SQLiteDatabase.OPEN_READWRITE);
            Cursor cursor = database.rawQuery("select * from wanta_city_location", null);
            if (cursor!=null){
                while (cursor.moveToNext()) {
                    String county = cursor.getString(1);
                    String name = cursor.getString(0);
                    helper.insertMessage(name, county, open);

                }
                cursor.close();
                database.close();
                open.setTransactionSuccessful();
                open.endTransaction();
                LogUtils.showVerbose("DataCatogryHelper", "总的大小：" + helper.getAllMessage());
                helper.close();
            }
            initLocationCity(context);
        }
    }
    public static void initLocationCity(Activity activity) {
        DomesticDbHelper helper = new DomesticDbHelper(activity);
        for (int j = 0; j < mStrings3.length; j++) {
            Constants.all_location_msg.add(mStrings4[j]);
            if (j <= 2) {
                for (int i = 0; i < mStrings.length; i++) {
                    Constants.all_location_msg.add(mStrings[i]);
                }
            } else {
                for (int k = 0; k < helper.getCloumnMessage(mStrings3[j]).size(); k++) {
                    Constants.all_location_msg.add(helper.getCloumnMessage(mStrings3[j]).get(k));
                }
            }


        }
    }

}
