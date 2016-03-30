package com.buaa.tezlikai.smscenter.ui.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.buaa.tezlikai.smscenter.R;
import com.buaa.tezlikai.smscenter.adapter.ConversationListAdapter;
import com.buaa.tezlikai.smscenter.base.BaseFragment;
import com.buaa.tezlikai.smscenter.dao.SimpleQueryHander;
import com.buaa.tezlikai.smscenter.dialog.ConfirmDialog;
import com.buaa.tezlikai.smscenter.dialog.DeleteMsgDialog;
import com.buaa.tezlikai.smscenter.globle.Constant;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.List;

/**
 * Created by Administrator on 2016/3/28.
 */
public class ConversationFragment extends BaseFragment {
    private Button bt_conversation_edit;
    private Button bt_conversation_new_msg;
    private Button bt_conversation_select_all;
    private Button bt_conversation_delete;
    private Button bt_conversation_cancel_select;
    private LinearLayout ll_conversation_edit_menu;
    private LinearLayout ll_conversation_select_menu;
    private ListView lv_conversation_list;
    private ConversationListAdapter adapter;
    private List<Integer> selectedConversationIds;

    static final int WHAT_DELETE_COMPLETE = 0;
    static final int WHAT_UPDATE_DELETE_PROGRESS = 1;

    private DeleteMsgDialog dialog;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case WHAT_DELETE_COMPLETE:
                    //退出选择模式，显示编辑菜单
                    adapter.setIsSelectMode(false);
                    adapter.notifyDataSetChanged();
                    showEditMeau();
                    dialog.dismiss();
                    break;
                case WHAT_UPDATE_DELETE_PROGRESS:
                    dialog.updateProgressAndTitle(msg.arg1+1);
                    break;
            }
        }
    };

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //填充布局文件，返回view
        View view = inflater.inflate(R.layout.fragment_conversation,null);
        bt_conversation_edit = (Button)view.findViewById(R.id.bt_conversation_edit);
        bt_conversation_new_msg = (Button)view.findViewById(R.id.bt_conversation_new_msg);
        bt_conversation_select_all = (Button)view.findViewById(R.id.bt_conversation_select_all);
        bt_conversation_delete = (Button)view.findViewById(R.id.bt_conversation_delete);
        bt_conversation_cancel_select = (Button)view.findViewById(R.id.bt_conversation_cancel_select);
        ll_conversation_edit_menu = (LinearLayout)view.findViewById(R.id.ll_conversation_edit_menu);
        ll_conversation_select_menu = (LinearLayout)view.findViewById(R.id.ll_conversation_select_menu);
        lv_conversation_list = (ListView)view.findViewById(R.id.lv_conversation_list);

        return view;
    }

    @Override
    public void initListener() {
        bt_conversation_edit.setOnClickListener(this);
        bt_conversation_new_msg.setOnClickListener(this);
        bt_conversation_select_all.setOnClickListener(this);
        bt_conversation_delete.setOnClickListener(this);
        bt_conversation_cancel_select.setOnClickListener(this);


        //为viewList每个条目设置监听
        lv_conversation_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (adapter.isSelectMode()) {
                    //选中选框
                    adapter.selectSingle(position);
                } else {
                    //进入会话详细
                }
            }
        });
        //
    }

    @Override
    public void initData() {
        /*//短信信息查询(同步查询)
        cursor = getActivity().getContentResolver().query(Constant.URI.URI_SMS_CONVERSATION,null,null,null,"date desc");
        CursorUtils.printCursor(cursor);*/
        //创建一个CursorAdapter
        adapter = new ConversationListAdapter(getContext(),null);
        lv_conversation_list.setAdapter(adapter);

        //信息的异步查询（常用）
        //arg0、arg1:可以用来携带一个int型和一个对象
        //arg2:查询结果
        SimpleQueryHander queryHander = new SimpleQueryHander(getActivity().getContentResolver());

        String[] projection = {
                "sms.body AS snippet",
                "sms.thread_id AS _id",
                "groups.msg_count AS msg_count",
                "address AS address",
                "date AS date"
        };
        queryHander.startQuery(0, adapter, Constant.URI.URI_SMS_CONVERSATION, projection, null, null, "date desc");
    }

    @Override
    public void processClick(View v) {
        switch (v.getId()){
            case R.id.bt_conversation_edit:
                showSelectMeau();
                //进入选择模式
                adapter.setIsSelectMode(true);
                //手动刷新
                adapter.notifyDataSetChanged();
                break;
            case R.id.bt_conversation_cancel_select:
                showEditMeau();
                //退出选择模式
                adapter.setIsSelectMode(false);
                adapter.cancleSelect();
                break;
            case R.id.bt_conversation_select_all:
                adapter.selectAll();
                break;
            case R.id.bt_conversation_delete:
                selectedConversationIds =  adapter.getSelectedConversationIds();
                if (selectedConversationIds.size() == 0)
                    return;
                showDeleteDialog();
//                deleteSms();
                break;

        }
    }
    /**
     * 选择菜单往上移动，编辑菜单往下移动
     */
    private void showSelectMeau(){
        ViewPropertyAnimator.animate(ll_conversation_edit_menu).translationY(ll_conversation_edit_menu.getHeight());
        //延迟200毫秒执行run方法的代码
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewPropertyAnimator.animate(ll_conversation_select_menu).translationY(0).setDuration(200);
            }
        },200);
    }
    /**
     * 编辑菜单往上移动，选择菜单往下移
     */
    private void showEditMeau(){
        ViewPropertyAnimator.animate(ll_conversation_select_menu).translationY(ll_conversation_select_menu.getHeight());
        //延迟200毫秒执行run方法的代码
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewPropertyAnimator.animate(ll_conversation_edit_menu).translationY(0).setDuration(200);
            }
        }, 200);
    }
    boolean isStopDelete = false;
    /**
     * 短信的删除
     */
    private void deleteSms(){

        dialog = DeleteMsgDialog.showDeleteDialog(getContext(), selectedConversationIds.size(), new DeleteMsgDialog.OnDeleteCancelListener() {
            @Override
            public void onCancel() {
                isStopDelete = true;
            }
        });

        Thread t = new Thread(){
            @Override
            public void run() {
                for (int i = 0;i < selectedConversationIds.size() ;i++){
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (isStopDelete){
                        isStopDelete = false;
                        break;
                    }
                    //取出集合中的会话，和id作为where条件删除所有符合条件的短信
                    String where = "thread_id = " + selectedConversationIds.get(i);
                    getActivity().getContentResolver().delete(Constant.URI.URI_SMS,where,null);

                    //发送消息，让删除进度条是刷新，同时把当前的删除进度传给进度条
                    Message msg = handler.obtainMessage();
                    msg.what = WHAT_UPDATE_DELETE_PROGRESS;
                    //把当前删除进度存入消息中
                    msg.arg1 = i;
                    handler.sendMessage(msg);
                }
                //删除会话后清空集合
                selectedConversationIds.clear();
                handler.sendEmptyMessage(WHAT_DELETE_COMPLETE);
            }
        };
       t.start();
    }

    /**
     * 显示删除提示信息
     */
    private void showDeleteDialog(){
       /* ConfirmDialog confirmDialog = new ConfirmDialog(getContext());
        confirmDialog.show();*/
        ConfirmDialog.showDialog(getContext(), "提示", "真的要删除会话吗?", new ConfirmDialog.OnConfirmListener() {
            @Override
            public void onCancel() {

            }
            @Override
            public void onConfirm() {
                deleteSms();
            }
        });
    }
}
