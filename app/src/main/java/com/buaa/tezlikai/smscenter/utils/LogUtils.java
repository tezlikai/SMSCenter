package com.buaa.tezlikai.smscenter.utils;

import android.util.Log;

/**
 * Created by Administrator on 2016/3/28.
 */
public class LogUtils {

    public  static boolean isDebug = true;
    public static void i(Object tag,String msg){
        if (isDebug){
            Log.i(tag.getClass().getSimpleName(),msg);
        }
    }
    public static void i(String tag,String msg){
        if (isDebug){
            Log.i(tag,msg);
        }
    }
}
