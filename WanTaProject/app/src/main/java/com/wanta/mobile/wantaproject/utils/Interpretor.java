package com.wanta.mobile.wantaproject.utils;

import android.app.Activity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Created by WangYongqiang on 2016/8/5.
 */
public final class Interpretor {
    private final Activity mActivity;
    //构造方法
    private Interpretor(Activity activity) {
        mActivity = activity;
    }
    //获取Interpretor实例
    public static Interpretor get(Activity activity) {
        return new Interpretor(activity);
    }
    //这个方法最重要
    public void interpret() {
        //遍历
        for (Field field : mActivity.getClass().getDeclaredFields()) {
            //获取注解
            for (Annotation annotation : field.getAnnotations()) {
                //当注解为InterpretView时候，执行相应的动作
                //在这个地方执行findViewById方法，然后将值赋给相应的变量
                if (annotation.annotationType().equals(InterpretView.class)) {
                    try {
                        Class<?> fieldType = field.getType();
                        int idValue = InterpretView.class.cast(annotation).value();
                        field.setAccessible(true);
                        Object injectedValue = fieldType.cast(mActivity
                                .findViewById(idValue));
                        if (injectedValue == null) {
                            throw new IllegalStateException("findViewById("
                                    + idValue + ") gave null for " + field
                                    + ", can't inject");
                        }
                        field.set(mActivity, injectedValue);
                        field.setAccessible(false);
                    } catch (IllegalAccessException e) {
                        throw new IllegalStateException(e);
                    }
                }
            }
        }
    }
}
