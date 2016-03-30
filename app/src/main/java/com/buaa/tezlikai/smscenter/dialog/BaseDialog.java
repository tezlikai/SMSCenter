package com.buaa.tezlikai.smscenter.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.buaa.tezlikai.smscenter.R;

/**
 * Created by Administrator on 2016/3/30.
 */
public abstract class BaseDialog extends AlertDialog implements View.OnClickListener{

    public BaseDialog(Context context) {
        //通过构造指定主题，主题中就设置了弧形边角的背景
        super(context,R.style.BaseDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
        initData();
    }
    public abstract void initView();
    public abstract void initListener();
    public abstract void initData();
    public abstract void processClick(View view);

    @Override
    public void onClick(View v) {
        processClick(v);
    }
}
