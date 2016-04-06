package com.buaa.tezlikai.smscenter.adapter;

import android.content.Context;
import android.database.Cursor;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.buaa.tezlikai.smscenter.R;
import com.buaa.tezlikai.smscenter.base.Sms;
import com.buaa.tezlikai.smscenter.globle.Constant;

/**
 * Created by Administrator on 2016/3/31.
 */
public class ConversationDetailAdapter extends CursorAdapter {

    private ListView lv;
    //设置短信时间间隔
    static final int DURATION = 3*60*1000;
    public ConversationDetailAdapter(Context context, Cursor c,ListView lv) {
        super(context, c);
        this.lv = lv;
    }
    /**
     * 只负责填充对象
     * @param context
     * @param cursor
     * @param parent
     * @return
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return View.inflate(context,R.layout.item_conversation_detail,null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //组件对象全在holder里
        ViewHolder holder = getHolder(view);
        //数据全在sms对象里
        Sms sms = Sms.createFromCursor(cursor);

        //设置显示内容

        //判断当前短信与上一条短信的时间间隔是否超过3分钟
        //第一条短信无需对比
        if (cursor.getPosition() == 0){
            holder.tv_conversation_detail_date.setVisibility(View.VISIBLE);
            showData(context,holder,sms);
        }else {
            //判断当前短信与上一条短信的时间间隔是否超过3分钟
            long preDate = getPreviousSmsDate(cursor.getPosition());
            if (sms.getDate() - preDate >DURATION){
                holder.tv_conversation_detail_date.setVisibility(View.VISIBLE);
                showData(context, holder, sms);
            }else {
                holder.tv_conversation_detail_date.setVisibility(View.GONE);
            }
        }

        //判断蓝色/白色的对话框是否显示
        holder.tv_conversation_detail_receive.setVisibility(sms.getType() == Constant.SMS.TYPE_RECEIVE ? View.VISIBLE : View.GONE);
        holder.tv_conversation_detail_send.setVisibility(sms.getType() == Constant.SMS.TYPE_SEND ? View.VISIBLE : View.GONE);
        //赋值操作
        if (sms.getType() == Constant.SMS.TYPE_RECEIVE ){
            holder.tv_conversation_detail_receive.setText(sms.getBody());
        }else {
            holder.tv_conversation_detail_send.setText(sms.getBody());
        }
    }

    private ViewHolder getHolder(View view){
        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder == null){
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        return holder;
    }

    class ViewHolder{

        private final TextView tv_conversation_detail_date;
        private final TextView tv_conversation_detail_receive;
        private final TextView tv_conversation_detail_send;

        public ViewHolder(View view){
           tv_conversation_detail_date =(TextView)view.findViewById(R.id.tv_conversation_detail_date);
            tv_conversation_detail_receive =(TextView)view.findViewById(R.id.tv_conversation_detail_receive);
            tv_conversation_detail_send =(TextView)view.findViewById(R.id.tv_conversation_detail_send);

        }
    }
    private void showData(Context context,ViewHolder holder,Sms sms){
        if (DateUtils.isToday(sms.getDate())){
            holder.tv_conversation_detail_date.setText(DateFormat.getTimeFormat(context).format(sms.getDate()));
        }else {
            holder.tv_conversation_detail_date.setText(DateFormat.getDateFormat(context).format(sms.getDate()));
        }
    }

    /**
     * 上一条短信的时间
     * @return
     */
    private long getPreviousSmsDate(int positon){
        Cursor cursor = (Cursor)getItem(positon-1);
        Sms sms = Sms.createFromCursor(cursor);
        return sms.getDate();
    }

    /**
     * 打开短信的时候，需要滑动到最新的条目，则需要重写changCursor
     */
    @Override
    public void changeCursor(Cursor cursor) {
        super.changeCursor(cursor);
        //让listView滑动到指定的条目上
        lv.setSelection(getCount());
    }
}
