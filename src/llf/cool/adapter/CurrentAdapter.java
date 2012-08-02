package llf.cool.adapter;

import java.io.File;
import java.util.ArrayList;

import llf.cool.R;
import llf.cool.service.AudioService;
import llf.cool.util.PubFunc;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class CurrentAdapter extends BaseAdapter
{

	private Context mContext = null;
	private LayoutInflater mLayoutInflater = null;
	private ArrayList<File> folder = null;
	private String mpath=null;
	public CurrentAdapter(Context context,String path)
	{
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mLayoutInflater = LayoutInflater.from(mContext);
		this.folder = AudioService.getFile(path);

		PubFunc.sortFileListInfo(folder);
		this.mpath=path;
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		if (folder != null)
		{
			return folder.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertview, ViewGroup viewGroup)
	{
		// TODO Auto-generated method stub
		ViewHolder holder = null;

		if (convertview == null)
		{
			convertview = mLayoutInflater
					.inflate(R.layout.item_file_list, null);
			holder = new ViewHolder();
			
//			holder.mIndex = (TextView) convertview
//					.findViewById(R.id.tv_resourceIndex);
			holder.mName = (TextView) convertview
					.findViewById(R.id.tv_resourceName);
			holder.mDelay = (TextView) convertview
					.findViewById(R.id.tv_resourceDelay);
			holder.mPath = (TextView) convertview
					.findViewById(R.id.tv_resourcePath);
			holder.image=(ImageView) convertview.findViewById(R.id.tv_resourceImage);
			
			convertview.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertview.getTag();
		}
		
		if (folder.size() > 0)
		{
			final File f = (File) folder.get(position);
			holder.mName.setText(f.getName());
			holder.mPath.setText("mnt"+f.getPath());
		}

		return convertview;
	}
	public void showDialog(Context context,final String str)
	{
		final EditText edit = new EditText(context);
		edit.setHint(str);
		new AlertDialog.Builder(context).setTitle("新建列表")
		.setView(edit).setPositiveButton(R.string.confirm, new  DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog,
					int which)
			{
				String filePath ="";
				if(edit.getText().toString().equals(""))
				{
					filePath = edit.getHint().toString();
				}
				else
				{
					filePath = edit.getText().toString();
				}
				File f = new File(AudioService.MUSIC_PATH + str);
				if(f.exists())
				{
					File destfile = new File(AudioService.MUSIC_PATH + filePath);
					f.renameTo(destfile);
				}
				folder = AudioService.getFolder(mpath);	
				notifyDataSetChanged();
			}
		})
		.setNegativeButton("取消", null).show();
		
	}
	class ViewHolder
	{
		TextView mIndex = null;
		TextView mName = null;
		TextView mDelay = null;
		TextView mPath = null;
		ImageView image = null;
	}
	
}
