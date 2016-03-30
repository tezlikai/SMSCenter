package com.buaa.tezlikai.smscenter.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.buaa.tezlikai.smscenter.R;

/**
 * Created by Administrator on 2016/3/30.
 */
public class ConfirmDialog extends BaseDialog {

    private String title;
    private String message;
    private TextView tv_dialog_title;
    private TextView tv_dialog_message;
    private Button bt_dialog_confirm;
    private Button bt_dialog_cancel;

    private OnConfirmListener confirmListener;

    public OnConfirmListener getConfirmListener() {
        return confirmListener;
    }

    public void setConfirmListener(OnConfirmListener confirmListener) {
        this.confirmListener = confirmListener;
    }

    public ConfirmDialog(Context context) {
        super(context);
    }
    public static void showDialog(Context context,String title,String message,OnConfirmListener confirmListener){
        ConfirmDialog dialog = new ConfirmDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setConfirmListener(confirmListener);
        dialog.show();

    }


    @Override
    public void initView() {
    //设置对话框显示的布局文件
        setContentView(R.layout.dialog_confirm);

        tv_dialog_title = (TextView)findViewById(R.id.tv_dialog_title);
        tv_dialog_message = (TextView)findViewById(R.id.tv_dialog_message);
        bt_dialog_confirm = (Button)findViewById(R.id.bt_dialog_confirm);
        bt_dialog_cancel = (Button)findViewById(R.id.bt_dialog_cancel);

    }

    @Override
    public void initListener() {
        bt_dialog_confirm.setOnClickListener(this);
        bt_dialog_cancel.setOnClickListener(this);
    }

    @Override
    public void initData() {
        tv_dialog_title.setText(title);
        tv_dialog_message.setText(message);
    }

    @Override
    public void processClick(View view) {
        switch (view.getId()){
            case R.id.bt_dialog_cancel:
                //如果取消按钮按下时，监听存在，那么调用监听的onCancel
                if (confirmListener != null){
                    confirmListener.onCancel();
                }
                break;

            case R.id.bt_dialog_confirm:
                //如果确定按钮按下时，监听存在，那么调用监听的onCancel
                if (confirmListener != null){
                    confirmListener.onConfirm();
                }
                break;
        }
        //对话框消失
        dismiss();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public interface OnConfirmListener{
        void onCancel();
        void onConfirm();
    }
}
