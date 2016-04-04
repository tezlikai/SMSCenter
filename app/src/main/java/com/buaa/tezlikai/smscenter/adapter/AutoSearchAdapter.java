package com.buaa.tezlikai.smscenter.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.buaa.tezlikai.smscenter.R;

public class AutoSearchAdapter extends CursorAdapter {

	public AutoSearchAdapter(Context context, Cursor c) {
		super(context, c);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return View.inflate(context, R.layout.item_auto_search_tv, null);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		//拿到所有组件，设置显示内容
		ViewHolder holder = getHolder(view);
		holder.tv_autosearch_name.setText(cursor.getString(cursor.getColumnIndex("display_name")));
		holder.tv_autosearch_address.setText(cursor.getString(cursor.getColumnIndex("data1")));

	}

	private ViewHolder getHolder(View view) {
		//从缓存中取出来
		ViewHolder holder = (ViewHolder) view.getTag();
		if(holder == null){
			holder = new ViewHolder(view);
			view.setTag(holder);
		}
		return holder;

	}
	
	class ViewHolder{
		private TextView tv_autosearch_name;
		private TextView tv_autosearch_address;

		public ViewHolder(View view) {
			tv_autosearch_name = (TextView) view.findViewById(R.id.tv_autosearch_name);
			tv_autosearch_address = (TextView) view.findViewById(R.id.tv_autosearch_address);
		}
	}

	/**
	 * 点击下拉列表条目时返回值
	 * @param cursor
	 * @return :
	 */
	@Override
	public CharSequence convertToString(Cursor cursor) {
		return cursor.getString(cursor.getColumnIndex("data1"));
	}

}
