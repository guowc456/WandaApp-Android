package com.wanta.mobile.wantaproject.domain;

import java.text.SimpleDateFormat;

/**
 * Created by WangYongqiang on 2016/11/26.
 */
public class JudgeDate {

    public static  boolean isDate(String str_input,String rDateFormat){
        if (!isNull(str_input)) {
            SimpleDateFormat formatter = new SimpleDateFormat(rDateFormat);
            formatter.setLenient(false);
            try {
                formatter.format(formatter.parse(str_input));
            } catch (Exception e) {
                return false;
            }
            return true;
        }
        return false;
    }
    public static boolean isNull(String str){
        if(str==null)
            return true;
        else
            return false;
    }
}