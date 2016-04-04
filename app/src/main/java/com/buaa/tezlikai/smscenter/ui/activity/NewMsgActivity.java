
package com.buaa.tezlikai.smscenter.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.buaa.tezlikai.smscenter.R;
import com.buaa.tezlikai.smscenter.adapter.AutoSearchAdapter;
import com.buaa.tezlikai.smscenter.base.BaseActivity;
import com.buaa.tezlikai.smscenter.dao.SmsDao;
import com.buaa.tezlikai.smscenter.utils.ToastUtils;


public class NewMsgActivity extends BaseActivity {

    private AutoCompleteTextView et_newmsg_address;
    private AutoSearchAdapter adapter;
    private ImageView iv_newmsg_select_contact;
    private EditText et_newmsg_body;
    private Button bt_newmsg_send;

    @Override
    public void initView() {
        setContentView(R.layout.activity_newmsg);
        et_newmsg_address = (AutoCompleteTextView)findViewById(R.id.et_newmsg_address);
        iv_newmsg_select_contact = (ImageView)findViewById(R.id.iv_newmsg_select_contact);
        et_newmsg_body = (EditText)findViewById(R.id.et_newmsg_body);

        bt_newmsg_send = (Button)findViewById(R.id.bt_newmsg_send);


        //这是下拉列表的背景
//        et_newmsg_address.setDropDownBackgroundResource(R.drawable.bg_btn_normal);
        //这是下拉列表的竖直偏移
        et_newmsg_address.setDropDownVerticalOffset(5);
    }

    @Override
    public void initListener() {
        iv_newmsg_select_contact.setOnClickListener(this);
        bt_newmsg_send.setOnClickListener(this);
    }

    @Override
    public void initData() {
        adapter = new AutoSearchAdapter(this, null);
        //给输入框设置adapter，该adapter负责显示输入框的下拉列表
        et_newmsg_address.setAdapter(adapter);

        adapter.setFilterQueryProvider(new FilterQueryProvider() {

            //这个方法的调用，是用来执行查询
            //constraint:用户在输入框中输入的号码，也就是模糊查询的条件
            @Override
            public Cursor runQuery(CharSequence constraint) {
                String[] projection = {
                        "data1",
                        "display_name",
                        "_id"
                };
                //模糊查询
                String selection = "data1 like '%" + constraint + "%'";
                Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, selection, null, null);
//				CursorUtils.printCursor(cursor);
                //返回cursor，就是把cursor交给adapter
                return cursor;
            }
        });

        //设置标题
        initTitleBar();
    }

    @Override
    public void processClick(View v) {
        switch (v.getId()){
            case R.id.iv_titlebar_back_btn:
                finish();
                break;
            case R.id.iv_newmsg_select_contact:
                //跳转至系统提供的联系人选择Activity
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("vnd.android.cursor.dir/contact");
                //使用startActionForResult启动，那么选好联系人，该Activity销毁，回调onActivityResult
                startActivityForResult(intent, 0);
                break;
            case R.id.bt_newmsg_send:
                String address = et_newmsg_address.getText().toString();
                String body = et_newmsg_body.getText().toString();
                if (!TextUtils.isEmpty(address) &&!TextUtils.isEmpty(body)){
                    SmsDao.sendSms(this,address,body);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //data中会携带一个uri，就是用户要选择的联系人的uri
        Uri uri = data.getData();
        if (uri != null){
            //查询这个uri,获取联系人的id和是否有号码
            String[] projection = {
                "_id",
                "has_phone_number"
            };
            //不需要where条件，因为uri是"一个"联系人的uri
            Cursor cursor = getContentResolver().query(uri,projection,null,null,null);

            //不需要判断是否查到，但是必须移动指针
            cursor.moveToFirst();
            String _id = cursor.getString(0);
            int has_phone_number = cursor.getInt(1);

            if (has_phone_number ==0){
                ToastUtils.ShowToast(this,"该联系人没有号码");
            }else {
                //如果有号码，拿着联系人id去查询Phone.CONTENT_URI 查询号码
                String selection = "contact_id =" +_id;
                Cursor cursor2 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{"data1"}, selection, null, null);
                cursor2.moveToFirst();
                String data1 = cursor2.getString(0);

                et_newmsg_address.setText(data1);
                //选择联系人之后，内容输入框获取焦点
                et_newmsg_body.requestFocus();
            }
        }
    }

    private void initTitleBar(){
        findViewById(R.id.iv_titlebar_back_btn).setOnClickListener(this);
        ((TextView)findViewById(R.id.tv_titlebar_title)).setText("发送短信");
    }
}
