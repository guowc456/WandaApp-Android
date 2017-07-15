package com.wanta.mobile.wantaproject.uploadimage;

/**
 * Created by WangYongqiang on 2016/11/16.
 */
public class StringUtils {
    public static String getLastPathSegment(String content) {
        if(content == null || content.length() == 0){
            return "";
        }
        String[] segments = content.split("/");
        if(segments.length > 0) {
            return segments[segments.length - 1];
        }
        return "";
    }

}

