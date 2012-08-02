package llf.cool.adapter;

import java.util.ArrayList;
import java.util.Map;

import llf.cool.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Baseresult extends BaseAdapter {
	private ArrayList<Map<Object, String>> array ;
	private Context m_context;
	private int num ;
	holdView m_vh;
	public Baseresult(Context context,
			ArrayList<Map<Object, String>> array_list,int id) {
		this.array = array_list;
		this.m_context = context;
		this.num = id;
		// TODO Auto-generated constructor stub
	}

//	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return array.size();
	}

//	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

//	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

//	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			m_vh = new holdView();
			convertView = LayoutInflater.from(m_context).inflate(
					R.layout.result_list, null);
			m_vh.tv_1 = (TextView) convertView.findViewById(R.id.result_text1);
			m_vh.tv_2 = (TextView) convertView.findViewById(R.id.result_text2);
			m_vh.tv_3 = (TextView) convertView.findViewById(R.id.result_text3);
			m_vh.tv_4 = (TextView) convertView.findViewById(R.id.result_text4);
			m_vh.tv_5 = (TextView) convertView.findViewById(R.id.result_text5);
			convertView.setTag(m_vh);
		} else {
			m_vh = (holdView) convertView.getTag();
		}
		
		m_vh.tv_1.setText(array.get(position).get("digitals"));
		m_vh.tv_2.setText(array.get(position).get("words"));
		m_vh.tv_3.setText(array.get(position).get("answer"));
		m_vh.tv_4.setText(array.get(position).get("result"));
		if (num == 0){
			m_vh.tv_5.setText(array.get(position).get("right"));
		}else if(num == 1){
		}
		return convertView;
	}
	
	public void removeFunc(){
		array.clear();
		notifyDataSetChanged();
	}
	
	static class holdView{
		TextView tv_1;
		TextView tv_2;
		TextView tv_3;
		TextView tv_4;
		TextView tv_5;
	}

}
