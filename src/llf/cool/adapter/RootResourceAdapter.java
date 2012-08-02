package llf.cool.adapter;

import llf.cool.R;
import llf.cool.activity.HomeActivity;
import llf.cool.model.RootResource;
import llf.cool.util.download.DownloadHelper;
import llf.cool.view.RemoteImageView;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RootResourceAdapter extends ArrayListAdapter<RootResource>{
	
	public RootResourceAdapter(Activity context){
		super(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View row =  convertView;
		ViewHolder holder = null;
		if(row !=null){
			holder = (ViewHolder)convertView.getTag();
		} else {
			LayoutInflater Inflater = LayoutInflater.from(mContext);
			row = Inflater.inflate(R.layout.item_rootresource_list, null);
			
			holder = new ViewHolder();
			holder.coverImg = (RemoteImageView)row.findViewById(R.id.image_type_icon);
			holder.titleText = (TextView)row.findViewById(R.id.text_type_item);
			
			row.setTag(holder);
		}
		
		holder.coverImg.setDefaultImage(R.drawable.icon);
		
    	//分类封面图片下载地址
		int classId = mList.get(position).getId();
    	String url = DownloadHelper.DOWNLOAD_URL + "?i=" + String.valueOf(classId)+"i" + DownloadHelper.USER_NAME;
		holder.coverImg.setImageUrl(url,
									 mList.get(position).getIconName(),
									  position, 
									   getListView());
		holder.titleText.setText(mList.get(position).getTitle());
		return row;
	}
	
	private class ViewHolder{
		RemoteImageView coverImg = null;
		TextView titleText = null;
	}
	
}
