package llf.cool.adapter;

import llf.cool.R;
import llf.cool.model.Track;
import llf.cool.util.download.DownloadHelper;
import llf.cool.view.RemoteImageView;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SubResourceAdapter extends ArrayListAdapter<Track>{

	public SubResourceAdapter(Activity context){
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
			row = Inflater.inflate(R.layout.item_subresource_list, null);
			
			holder = new ViewHolder();
			holder.coverImg = (RemoteImageView)row.findViewById(R.id.image_type_icon);
			holder.titleText = (TextView)row.findViewById(R.id.text_type_item);
			
			row.setTag(holder);
		}
		
		holder.coverImg.setDefaultImage(R.drawable.icon);
		
    	//资源封面图片下载地址
		int classId = mList.get(position).getId();
    	String url = DownloadHelper.DOWNLOAD_URL + "?i=" + String.valueOf(classId)+"c" + DownloadHelper.USER_NAME;
		holder.coverImg.setImageUrl(url,
									 mList.get(position).getCoverName(),
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
