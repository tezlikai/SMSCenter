package com.buaa.tezlikai.smscenter.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/4/3.
 */
public class GroupOpenHelper extends SQLiteOpenHelper {
    //使用单例模式获取GroupOpenHelper实例
    private static GroupOpenHelper instance;

    public static GroupOpenHelper getInstance(Context context){
        if (instance == null){
            instance = new GroupOpenHelper(context,"group.db",null,1);
        }
        return instance;
    }

    private GroupOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建groups表
        db.execSQL("create table groups(" +
                "_id integer primary key autoincrement, " +
                "name varchar, " +
                "create_date integer, " +
                "thread_count integer" +
                ")");
        //创建会话和群组的映射表
        db.execSQL("create table thread_group(" +
                "_id integer primary key autoincrement, " +
                "group_id integer, " +
                "thread_id integer" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
