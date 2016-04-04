package com.buaa.tezlikai.smscenter.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.buaa.tezlikai.smscenter.R;

/**
 * Created by Administrator on 2016/4/3.
 */
public class InputDialog extends BaseDialog {

    private String title;
    private TextView tv_inputdialog_title;
    private EditText et_inputdialog_message;
    private Button bt_inputdialog_cancel;
    private Button bt_inputdialog_confirm;
    private OnInputDialogListener onInputDialogListener;

    public InputDialog(Context context,String title,OnInputDialogListener onInputDialogListener) {
        super(context);
        //通过构造方法把title传进来
        this.title = title;
        this.onInputDialogListener = onInputDialogListener;
    }

    /**
     * 把对话框弹出来
     */
    public static void showDialog(Context context,String title,OnInputDialogListener onInputDialogListener){
        InputDialog dialog = new InputDialog(context,title,onInputDialogListener);
        //对话框默认不支持文本输入，手动把一个输入框设置为对话框的内容，Android自动对其进行设置
        dialog.setView(new EditText(context));
        dialog.show();
    }

    @Override
    public void initView() {
        setContentView(R.layout.dialog_input);
        tv_inputdialog_title = (TextView)findViewById(R.id.tv_inputdialog_title);
        et_inputdialog_message = (EditText)findViewById(R.id.et_inputdialog_message);
        bt_inputdialog_cancel = (Button)findViewById(R.id.bt_inputdialog_cancel);
        bt_inputdialog_confirm = (Button)findViewById(R.id.bt_inputdialog_confirm);
    }

    @Override
    public void initListener() {
        bt_inputdialog_cancel.setOnClickListener(this);
        bt_inputdialog_confirm.setOnClickListener(this);
    }

    @Override
    public void initData() {
        tv_inputdialog_title.setText(title);
    }

    @Override
    public void processClick(View view) {
        switch (view.getId()){
            case R.id.bt_inputdialog_cancel:
                if (onInputDialogListener != null){
                    onInputDialogListener.onCancel();
                }
                break;
            case R.id.bt_inputdialog_confirm:
                if (onInputDialogListener != null){
                    //因为OnInputDialogListener的实现类访问et_inputdialog_message不方便，所以把输入框的内容作为参数传入监听的回调中
                    onInputDialogListener.onConfirm(et_inputdialog_message.getText().toString());
                }
                break;
        }
        dismiss();
    }
    public interface OnInputDialogListener{
        void onCancel();
        void onConfirm(String text);
    }
}
