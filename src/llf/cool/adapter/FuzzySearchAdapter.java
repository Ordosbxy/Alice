package llf.cool.adapter;

import java.util.ArrayList;

import llf.cool.R;
import llf.dictionary.engine.LLFDic;
import llf.dictionary.engine.VHConst;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FuzzySearchAdapter extends BaseAdapter
{

	private Context mContext;
	private int mCount=0;
	private LayoutInflater mInflater;
	private ArrayList<Long> m_IdList; // 词条序号列表
	private boolean m_bUseIdList = false; // 是否使用IdList, 是:
											// id=m_IdList.get(position), 否:
											// id=position
	public FuzzySearchAdapter(Context context)
	{
		mContext=context;
		mInflater=LayoutInflater.from(mContext);
	}
	public void setIdList(long[] lst)
	{
		ArrayList<Long> al = new ArrayList<Long>();

		for (int i = 0; i < lst.length; i++)
		{
			al.add(lst[i]);
		}

		setIdList(al, 0);
	}

	public void setIdList(ArrayList<Long> lst, int size)
	{
		if (lst == null)
		{
			m_bUseIdList = false;
			mCount = size;
		}
		else
		{
			m_bUseIdList = true;
			m_IdList = lst;
			mCount = lst.size();
		}
	}
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return mCount;
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position)
	{
		if (m_bUseIdList)
		{
			if (m_IdList.isEmpty())
			{
				return VHConst.INVALID_INDEX;
			}
			else
			{
				if (position >= 0 && position < m_IdList.size())
				{
					return m_IdList.get(position);
				}
				else
				{
					return m_IdList.get(m_IdList.size() - 1);
				}
			}
		}
		else
		{
			return position;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Long index=getItemId(position);
		ViewHolder holder = null;
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.image_item_resultlist, null);
			holder = new ViewHolder();
			holder.wordText = (TextView) convertView.findViewById(R.id.ItemText);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		 char[] bs=LLFDic.getWordByIndex(index);
         String word=new String(bs);
         holder.wordText.setText(word);
		return convertView;

	}

	public static class ViewHolder
	{
		public TextView wordText= null;
	}
}
