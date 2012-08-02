package llf.cool.adapter;

import llf.cool.R;
import llf.cool.activity.MainActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class GridViewAdapter extends BaseAdapter {

	private Context mContext = null;
	private LayoutInflater mInflater = null;

	public GridViewAdapter(Context context) {
		super();
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return MainActivity.TYPE_ITEMS.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		int imageResourceId = 0;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_grid, null);
			holder = new ViewHolder();
			holder.mImage = (ImageView) convertView
					.findViewById(R.id.gridItem_imageView);
			holder.mText = (TextView) convertView
					.findViewById(R.id.gridItem_textView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (MainActivity.TYPE_ICONS.length - 1 > position) {
			imageResourceId = MainActivity.TYPE_ICONS[position];
		} else {
			imageResourceId = R.drawable.icon;
		}
		String text = MainActivity.TYPE_ITEMS[position];
		holder.mImage.setImageResource(imageResourceId);
		holder.mText.setText(text);
		return convertView;
	}

	class ViewHolder {
		ImageView mImage = null;
		TextView mText = null;
	}

}
