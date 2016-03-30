package com.buaa.tezlikai.smscenter.utils;

import android.database.Cursor;

/**
 * Created by Administrator on 2016/3/29.
 */
public class CursorUtils {
    public static void printCursor(Cursor cursor){
        //获取行数
        LogUtils.i(cursor, "一共有" + cursor.getCount() + "条数据");

        while (cursor.moveToNext()){
            //获取字段数量
            for (int i = 0; i<cursor.getColumnCount(); i++){
                //获取字段名字
                String name = cursor.getColumnName(i);
                String content = cursor.getString(i);
                LogUtils.i(cursor,name + ":" + content);
            }
            LogUtils.i(cursor,"======================================================");
        }
    }
}
