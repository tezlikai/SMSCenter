package com.buaa.tezlikai.smscenter.dialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.buaa.tezlikai.smscenter.R;

/**
 * Created by Administrator on 2016/4/4.
 */
public class ListDialog extends BaseDialog {

    private String title;
    private String[] items;
    private TextView tv_listdialog_title;
    private ListView lv_listdialog;

    private Context context;
    private OnListDialogListener onListDialogListener;

    public ListDialog(Context context,String title,String[] items,OnListDialogListener onListDialogListener) {
        super(context);
        this.context = context;
        this.title = title;
        this.items = items;
        this.onListDialogListener = onListDialogListener;
    }

    //显示Dialog
    public static  void showDialog(Context context,String title,String[] items,OnListDialogListener onListDialogListener){
        ListDialog dialog = new ListDialog(context,title,items,onListDialogListener);
        dialog.show();
    }

    @Override
    public void initView() {
        setContentView(R.layout.dialog_list);
        tv_listdialog_title = (TextView)findViewById(R.id.tv_listdialog_title);
        lv_listdialog = (ListView)findViewById(R.id.lv_listdialog);
    }

    @Override
    public void initListener() {
        //给lv_listdialog设置条目侦听
        lv_listdialog.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(onListDialogListener != null){
                    onListDialogListener.onItemClick(parent, view, position, id);
                }
                dismiss();
            }
        });

    }
    @Override
    public void initData() {
        tv_listdialog_title.setText(title);
        lv_listdialog.setAdapter(new MyAdapter());
    }

    @Override
    public void processClick(View view) {

    }
    public interface OnListDialogListener{
        void onItemClick(AdapterView<?> parent, View view, int position, long id);
    }

    class MyAdapter extends BaseAdapter{

        private TextView tv_item_listdialog;

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(context, R.layout.item_listdialog, null);
            tv_item_listdialog = (TextView) view.findViewById(R.id.tv_item_listdialog);
            tv_item_listdialog.setText(items[position]);
            return view;
        }
    }
}
