package com.buaa.tezlikai.smscenter.dao;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import com.buaa.tezlikai.smscenter.globle.Constant;
import com.buaa.tezlikai.smscenter.receive.SendSmsReceiver;

import java.util.List;

/**
 * Created by Administrator on 2016/3/31.
 */
public class SmsDao {
    /**
     * 短信的发送功能
     * @param address
     * @param body
     */
    public static void sendSms(Context context,String address,String body){
        SmsManager manager = SmsManager.getDefault();
        List<String> smss = manager.divideMessage(body);

        Intent intent = new Intent(SendSmsReceiver.ACTION_SEND_SMS);
        //短信发出去后，系统会发送一条广播，告知我们短信发送是成功还是失败
        PendingIntent semtIntent = PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_ONE_SHOT);
        for (String text :smss){
            //这个api只负责发送短信，不会把短息写入数据库
            manager.sendTextMessage(address,null,text,semtIntent,null);

            //把短息插入数据库
            insertSms(context,address,body);
        }
    }

    /**
     * 发送完短息后，需要把短息插入数据库，这样才能在发信息的listView上刷新显示
     * @param context
     * @param address
     * @param body
     */
    public static void insertSms(Context context,String address,String body){

        ContentValues values = new ContentValues();
        values.put("address",address);
        values.put("body",body);
        values.put("type",Constant.SMS.TYPE_SEND);

        context.getContentResolver().insert(Constant.URI.URI_SMS,values);
    }
}
