package llf.cool.adapter;


import llf.cool.R;
import llf.cool.data.HistoryDB;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HistoryAdapter extends BaseAdapter
{
	private Context mContext;
	private Cursor mcurssor;
	private LayoutInflater mInflater;

	public HistoryAdapter(Context context, Cursor curssor)
	{
		mContext = context;
		mcurssor = curssor;
		mInflater = LayoutInflater.from(mContext);
	}

	public int getCount()
	{
		// TODO Auto-generated method stub
		return mcurssor.getCount();
	}

	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.history_item_list, null);
			holder = new ViewHolder();
			holder.wordName = (TextView) convertView.findViewById(R.id.text);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		mcurssor.moveToPosition(mcurssor.getCount() - position - 1);
		String wordName = mcurssor.getString(mcurssor
				.getColumnIndex(HistoryDB.WORD_NAME));
		holder.wordName.setText(wordName);
		return convertView;

	}

	public static class ViewHolder
	{
		public TextView wordName = null;
	}
}
