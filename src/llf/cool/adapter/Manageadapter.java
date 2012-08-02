package llf.cool.adapter;


import llf.cool.R;
import llf.cool.data.MyDatabase;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Manageadapter extends BaseAdapter {
	public Cursor manage_cursor = null;
	private Context m_context;
	private holdView m_holdview;
	private MyDatabase manage_database;
	private int select_item = -1;
	public Manageadapter(Context context, Cursor group_cursor, MyDatabase words_database) {
		// TODO Auto-generated constructor stub
		manage_cursor = group_cursor;
		m_context = context;
		manage_database = words_database;
	}

	//@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.manage_cursor.getCount();
	}

	//@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	//@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	//@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		manage_cursor.moveToPosition(position);
		if (convertView == null) {
			m_holdview = new holdView();
			convertView = LayoutInflater.from(m_context).inflate(
					R.layout.manage_list, null);
			m_holdview.tv_1 = (TextView) convertView.findViewById(R.id.dialog_list_text_1);
			convertView.setTag(m_holdview);
		} else {
			m_holdview = (holdView) convertView.getTag();
		}
		if (position == select_item){
			convertView.setBackgroundResource(R.drawable.mange_item);
		}else {
			convertView.setBackgroundResource(R.drawable.mange_back);
		}
		m_holdview.tv_1.setText(manage_cursor.getString(manage_cursor.getColumnIndex(manage_database.FILED_GROUP)));
		return convertView;
	}
	public void setBackground(int item){
		this.select_item = item;
	}
	public void setChange(Cursor cursor){
		this.manage_cursor = cursor;
		notifyDataSetInvalidated();
	}
	static class holdView {
		TextView tv_1 = null;
	}
}
