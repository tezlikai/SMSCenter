package com.buaa.tezlikai.smscenter.globle;

import android.net.Uri;

/**
 * Created by Administrator on 2016/3/29.
 */
public class Constant {
    public interface URI{
        Uri URI_SMS_CONVERSATION = Uri.parse("content://sms//conversations");
        Uri URI_SMS = Uri.parse("content://sms");
    }
}
