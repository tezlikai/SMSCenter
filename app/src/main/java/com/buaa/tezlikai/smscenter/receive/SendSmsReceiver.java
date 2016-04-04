package com.buaa.tezlikai.smscenter.receive;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.buaa.tezlikai.smscenter.utils.ToastUtils;

/**
 * Created by Administrator on 2016/3/31.
 */
public class SendSmsReceiver extends BroadcastReceiver {
    public static final String ACTION_SEND_SMS = "com.buaa.tezlikai.smscenter";
    @Override
    public void onReceive(Context context, Intent intent) {
        int code = getResultCode();
        if (code == Activity.RESULT_OK){
            ToastUtils.ShowToast(context,"发送成功");
        }else {
            ToastUtils.ShowToast(context,"发送失败");
        }
    }
}
