package com.buaa.tezlikai.smscenter.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/3/31.
 */
public class ToastUtils {
    public static void ShowToast(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
