package com.buaa.tezlikai.smscenter.dao;


import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

import java.io.InputStream;

/**
 * Created by Administrator on 2016/3/30.
 */
public class ContactDao {
    /**
     * 通过号码获取联系人名字
     * @param resolver
     * @param address
     * @return
     */
    public static String getNameAddress(ContentResolver resolver,String address){
        String name = null;
        //把指定数据拼接在uri后面
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,address);
        Cursor cursor = resolver.query(uri,new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME},null,null,null);
        if (cursor.moveToFirst()){
            name = cursor.getString(0);
            cursor.close();
        }
        return name;
    }
   //通过号码获取联系人头像
    public static Bitmap getAvatarByAddress(ContentResolver resolver,String address){
        Bitmap avatar = null;
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,address);
        Cursor cursor = resolver.query(uri,new String[]{ContactsContract.PhoneLookup._ID},null,null,null);
        if (cursor.moveToFirst()){
            String _id = cursor.getString(0);
            //获取联系人的照片
            InputStream is = ContactsContract.Contacts.openContactPhotoInputStream(resolver, Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI,_id));
            avatar = BitmapFactory.decodeStream(is);
        }
        return avatar;
    }

}
