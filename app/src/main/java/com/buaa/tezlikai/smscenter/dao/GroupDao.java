package com.buaa.tezlikai.smscenter.dao;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import com.buaa.tezlikai.smscenter.globle.Constant;

/**
 * Created by Administrator on 2016/4/3.
 */
public class GroupDao {
    /**
     * 插入新的群组
     */
    public static void insertGroup(ContentResolver resolver, String groupName){
        ContentValues values = new ContentValues();
        values.put("name", groupName);
        values.put("thread_count", 0);
        values.put("create_date", System.currentTimeMillis());
        //符合内容提供者给出的路径匹配规则，就可以将想要插入的数据插入到group表当中了
        resolver.insert(Constant.URI.URI_GROUP_INSERT, values);
    }
    /**
     * 更新操作
     */
    public static void updateGroupName(ContentResolver resolver, String groupName,int _id){
        ContentValues values = new ContentValues();
        values.put("name",groupName);
        resolver.update(Constant.URI.URI_GROUP_UPDATE, values, "_id = " + _id, null);
    }

    /**
     * 删除group
     */
    public static void deleteGroup(ContentResolver resolver, int _id){
        resolver.delete(Constant.URI.URI_GROUP_DELETE, "_id = " + _id, null);
    }

    /**
     * 通过群组id查询群组名字
     * @param resolver
     * @param _id
     * @return
     */
    public static String getGroupNameByGroupId(ContentResolver resolver, int _id){
        String name = null;
        Cursor cursor = resolver.query(Constant.URI.URI_GROUP_QUERY, new String[]{"name"}, "_id = " + _id, null, null);
        if(cursor.moveToFirst()){
            name = cursor.getString(0);
        }
        return name;
    }
    /**
     * 获取指定群组存放的会话的数量
     * @param resolver
     * @param _id
     * @return
     */
    public static int getThreadCount(ContentResolver resolver, int _id){
        int threadCount = -1;
        Cursor cursor = resolver.query(Constant.URI.URI_GROUP_QUERY, new String[]{"thread_count"}, "_id = " + _id, null, null);
        if(cursor.moveToNext()){
            threadCount = cursor.getInt(0);
        }
        return threadCount;
    }

    /**
     * 更新指定群组的会话数量
     * @param resolver
     * @param _id
     * @param threadCount
     */
    public static void updateThreadCount(ContentResolver resolver, int _id, int threadCount){
        ContentValues values = new ContentValues();
        values.put("thread_count", threadCount);
        resolver.update(Constant.URI.URI_GROUP_UPDATE, values, "_id = " + _id, null);
    }
}

