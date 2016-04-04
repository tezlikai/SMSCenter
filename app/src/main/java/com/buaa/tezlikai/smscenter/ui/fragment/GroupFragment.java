package com.buaa.tezlikai.smscenter.ui.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.buaa.tezlikai.smscenter.Bean.Group;
import com.buaa.tezlikai.smscenter.R;
import com.buaa.tezlikai.smscenter.adapter.GroupListAdapter;
import com.buaa.tezlikai.smscenter.base.BaseFragment;
import com.buaa.tezlikai.smscenter.dao.GroupDao;
import com.buaa.tezlikai.smscenter.dao.SimpleQueryHander;
import com.buaa.tezlikai.smscenter.dialog.InputDialog;
import com.buaa.tezlikai.smscenter.dialog.ListDialog;
import com.buaa.tezlikai.smscenter.globle.Constant;
import com.buaa.tezlikai.smscenter.ui.activity.GroupDetailActivity;
import com.buaa.tezlikai.smscenter.utils.ToastUtils;

import java.util.InvalidPropertiesFormatException;

/**
 * Created by Administrator on 2016/3/28.
 */
public class GroupFragment extends BaseFragment {
    private Button bt_group_newgroup;
    private ListView lv_group_list;
    private GroupListAdapter adapter;
    //用于异步查询
    private SimpleQueryHander queryHander;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group,null);
        bt_group_newgroup = (Button)view.findViewById(R.id.bt_group_newgroup);

        lv_group_list = (ListView)view.findViewById(R.id.lv_group_list);
        return view;
    }

    @Override
    public void initListener() {
        bt_group_newgroup.setOnClickListener(this);
        //给条目设置长按侦听
        lv_group_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                Cursor cursor  = (Cursor) adapter.getItem(position);
                final Group group = Group.createFromCursor(cursor);
                //长按之后会跳出来一个对话框。
                ListDialog.showDialog(getActivity(), "选择操作", new String[]{"修改","删除"}, new ListDialog.OnListDialogListener(){

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {
                        switch (position) {
                            case 0://修改
                                //弹出输入对话框
                                InputDialog.showDialog(getActivity(), "修改群组", new InputDialog.OnInputDialogListener() {

                                    @Override
                                    public void onConfirm(String text) {
                                        //确认修改群组名字
                                        GroupDao.updateGroupName(getActivity().getContentResolver(), text, group.get_id());
                                    }
                                    @Override
                                    public void onCancel() {
                                    }
                                });
                                break;
                            case 1://删除
                                GroupDao.deleteGroup(getActivity().getContentResolver(), group.get_id());
                                break;
                        }
                    }
                });
                return false;
            }
        });
        //点击群组列表的条目，跳转到群组详细Activity
        lv_group_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), GroupDetailActivity.class);
                //跳转时携带群组名字、群组id
                Cursor cursor = (Cursor)adapter.getItem(position);
                Group group = Group.createFromCursor(cursor);
                //判断一下群组是否有会话
                if (group.getThread_count() > 0) {
                    intent.putExtra("name", group.getName());
                    intent.putExtra("group_id", group.get_id());
                    startActivity(intent);
                }else {
                    ToastUtils.ShowToast(getContext(),"当前群组没有任何会话");
                }
            }
        });
    }

    @Override
    public void initData() {
        adapter = new GroupListAdapter(getContext(),null);
        lv_group_list.setAdapter(adapter);

        queryHander = new SimpleQueryHander(getActivity().getContentResolver());
        queryHander.startQuery(0,adapter,Constant.URI.URI_GROUP_QUERY,null,null,null,"create_date desc");
    }

    @Override
    public void processClick(View v) {
        switch (v.getId()) {
            case R.id.bt_group_newgroup:
                InputDialog.showDialog(getActivity(), "创建群组", new  InputDialog.OnInputDialogListener() {

                    @Override//这里将用户输入的名字作为onConfirm方法的参数传递进去。
                    public void onConfirm(String text) {
                        if(!TextUtils.isEmpty(text)){
                            GroupDao.insertGroup(getActivity().getContentResolver(), text);
                        }
                        else{
                            ToastUtils.ShowToast(getActivity(), "群组名不能为空");
                        }
                    }
                    @Override
                    public void onCancel() {
                    }
                });
                break;
        }
    }
}
