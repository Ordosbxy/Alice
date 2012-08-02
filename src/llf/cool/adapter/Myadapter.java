package llf.cool.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import llf.cool.R;
import llf.cool.activity.NewWordsActivity;
import llf.cool.data.MyDatabase;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Myadapter extends BaseAdapter {

	private Context m_context = null;
	public Cursor m_cursor = null;
	private ArrayList<HashMap<Object, String>> adapter_data = new ArrayList<HashMap<Object,String>>();
	private HashMap<Object, String> map = new HashMap<Object, String>();
	holdView m_vh = null;
	private MyDatabase data_baseadapter;
	private int temp = 0;
	private String get_str;
	private int get_num;
	private int m_postion;
	private String str_table ;
	private int inModelNum;
	private static final int WORDS_MODEL = 0;
	private static final int DICTIONAY_MODEL =1;
	private String str_dif ;
	private Drawable m_drawable =null;
	private Bitmap explain_bitmap;
	private Bitmap new_bitmap ;
	private Bitmap words_bitmap;
	private Drawable words_draw = null; 
	int length = 0;
	private static final int PHONETIC_LENGTH = 105;
	private static final int EXPLIAN_LENGTH = 355;
	private int Item  = -1;
	private boolean all ;
	private int i;
	private int id ;
	public Myadapter(Context context,Cursor cursor,MyDatabase mydatabase,String table_name,int num) {
		// TODO Auto-generated constructor stub
		m_context = context;
		m_cursor = cursor;
		str_dif = "容易";
		this.data_baseadapter = mydatabase;
		this.str_table = table_name;
		this.inModelNum =num;
	}
	
//	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.m_cursor.getCount();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		m_cursor.moveToPosition(position);
		if (convertView == null) {
			m_vh = new holdView();
			convertView = LayoutInflater.from(m_context).inflate(
					R.layout.provider, null);
			m_vh.tv_1 = (TextView) convertView.findViewById(R.id.text1);
			m_vh.tv_2 = (TextView) convertView.findViewById(R.id.text2);
//			m_vh.tv_3 = (TextView) convertView.findViewById(R.id.text3);
//			m_vh.tv_4 = (TextView) convertView.findViewById(R.id.text4);
			m_vh.tv_5 = (TextView) convertView.findViewById(R.id.text5);
//			m_vh.tv_6 = (TextView)convertView.findViewById(R.id.text6);
//			m_vh.tv_6.setOnClickListener(new OnClickListener()
//			{
//				
//				@Override
//				public void onClick(View v)
//				{
//					// TODO Auto-generated method stub
//					
//				}
//			});
			convertView.setTag(m_vh);
		} else {
			m_vh = (holdView) convertView.getTag();
		}
		this.m_postion = position;
		m_vh.tv_1.setText(String.valueOf(position+1));
		m_vh.tv_2.setText(this.m_cursor.getString(2));
		m_vh.tv_5.setText(this.m_cursor.getString(5));
		if (position == Item) {
			convertView.setBackgroundResource(R.drawable.sel_item);
		}else{
			convertView.setBackgroundResource(R.drawable.item_back);
		}
		if (all == true){
			convertView.setBackgroundResource(R.drawable.sel_item);
		}
		id = this.m_cursor.getInt(m_cursor.getColumnIndex(data_baseadapter.FILED_ID));
		map.put(position, String.valueOf(id));
		if (inModelNum == WORDS_MODEL){
			Typeface m_face = NewWordsActivity.getFont(m_context);
//			m_vh.tv_3.setTypeface(m_face);
//			m_vh.tv_3.setText("<"+this.m_cursor.getString(3)+">");
			
			byte[] words_phonetic = m_cursor.getBlob(3);
			Bitmap bitmap = null;
			if (words_phonetic != null){
				words_bitmap = BitmapFactory.decodeByteArray(words_phonetic, 0, words_phonetic.length);
				if (words_bitmap != null){
					if (words_bitmap.getWidth() > PHONETIC_LENGTH){
						bitmap = Bitmap.createBitmap(words_bitmap, 0, 0, 105, words_bitmap.getHeight());
					}else {
						bitmap = Bitmap.createBitmap(words_bitmap, 0, 0, words_bitmap.getWidth(), words_bitmap.getHeight());
					}
				}
				words_draw = new BitmapDrawable(bitmap);
//				m_vh.tv_3.setBackgroundDrawable(words_draw);
			}
//			m_vh.tv_4.setText("  "+this.m_cursor.getString(4));
		}else if(inModelNum == DICTIONAY_MODEL){
			m_vh.tv_1.setTextSize(16.0f);
			m_vh.tv_2.setTextSize(16.0f);
			m_vh.tv_5.setTextSize(16.0f);
			getBitmap(position);
		}
		
		
		change();
//		m_vh.tv_6.setText(str_dif);
//		if (inModelNum == DICTIONAY_MODEL){
//			m_vh.tv_6.setTextSize(16.0f);
//		}
//		m_vh.tv_6.setOnClickListener(new lvbuttonListener(map.get(position)));
		getHashMap(m_postion);
		
		return convertView;
	}
	
	//得到图片
	public void getBitmap(final int pos){
		byte[] in = m_cursor.getBlob(m_cursor.getColumnIndex(data_baseadapter.FIELD_BITMAP));
		Bitmap bitmap = null;
		if (in != null){
			explain_bitmap = BitmapFactory.decodeByteArray(in, 0, in.length);
			if (explain_bitmap.getWidth() > EXPLIAN_LENGTH){
				bitmap = Bitmap.createBitmap(explain_bitmap, 0, 0, EXPLIAN_LENGTH, explain_bitmap.getHeight());
			}else {
				bitmap = Bitmap.createBitmap(explain_bitmap, 0, 0, explain_bitmap.getWidth(), explain_bitmap.getHeight());
			}
			m_drawable = new BitmapDrawable(bitmap);
//			m_vh.tv_4.setBackgroundDrawable(m_drawable);
		}
//		m_vh.tv_4.setOnClickListener(new OnClickListener() {// 词典
//			
////			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if (NewWords.phoneticClickFunc()){
//					return;
//				}
//				m_cursor.moveToPosition(pos);
////				m_cursor = data_baseadapter.selectSomeTable(str_table);
//				String dic_words = m_cursor.getString(2);
//				if (dic_words != null && dic_words.length() != 0){
//					NewWords.searchFunc(m_context,dic_words);
//				}
////				NewWords.showInfo(m_context);
//			}
//		});
		
		byte[] phonetic_byte = m_cursor.getBlob(m_cursor.getColumnIndex(data_baseadapter.FIELD_BYTE_EXPLAIN));
		if (phonetic_byte == null){
			String temp_phonetic ="";
			phonetic_byte = temp_phonetic.getBytes();
		}
		Bitmap phonetic_bitmap = BitmapFactory.decodeByteArray(phonetic_byte, 0, phonetic_byte.length);
		if (phonetic_bitmap == null){
			new_bitmap = phonetic_bitmap ;
		}else {
			if (phonetic_bitmap.getWidth() > PHONETIC_LENGTH){
				new_bitmap = Bitmap.createBitmap(phonetic_bitmap, 0, 0, PHONETIC_LENGTH, phonetic_bitmap.getHeight());
			}else {
				new_bitmap = Bitmap.createBitmap(phonetic_bitmap, 0, 0, phonetic_bitmap.getWidth(), phonetic_bitmap.getHeight());
			}
		}
		Drawable explain_drawable = new BitmapDrawable(new_bitmap);
		m_vh.tv_3.setBackgroundDrawable(explain_drawable);
		m_vh.tv_3.setOnClickListener(new OnClickListener() {
			
//			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (NewWordsActivity.phoneticClickFunc()){
					return;
				}
				m_cursor.moveToPosition(pos);
//				m_cursor = data_baseadapter.selectSomeTable(str_table);
				String dic_words = m_cursor.getString(2);
				if (dic_words != null && dic_words.length() != 0){
					NewWordsActivity.searchFunc(m_context,dic_words);
				}
			}
		});
		
//		m_vh.tv_2.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
////				m_cursor = data_baseadapter.selectSomeTable(str_table);
//				m_cursor.moveToPosition(pos);
//				String bing_words = m_cursor.getString(2);
//				if (bing_words != null && bing_words.length() != 0){
//					NewWords.searchFunc(m_context,bing_words);
//				}
//			}
//		});

	}
	public void add(HashMap<Object, String> map) {
		this.adapter_data.add(map);
		notifyDataSetChanged();
	}
	
	class lvbuttonListener implements OnClickListener {
		private int pos;
		public lvbuttonListener(String postions) {
			// TODO Auto-generated constructor stub
			pos = Integer.valueOf(postions).intValue();
		}
//		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			temp++;
			if (temp == 3){
				temp = 0;
			}
			updata(pos,temp);
			NewWordsActivity.getReturn();
			notifyDataSetInvalidated();
			NewWordsActivity.allSpinnerStatus();
		}
	}
	
	public void updata(int id,int temper){
		data_baseadapter.updateImage(id, String.valueOf(temper),str_table);
		m_cursor = data_baseadapter.selectSomeTable(str_table);
		NewWordsActivity.manageCursor(m_context, m_cursor);
		this.m_cursor.moveToPosition(m_postion);
		change();
//		m_vh.tv_6.setText(str_dif);
	}
	
	public void change(){
		get_str = this.m_cursor.getString(12);
		get_num = Integer.valueOf(get_str).intValue();
		if (get_num == 0){
			str_dif ="困难";
		}else if (get_num == 1){
			str_dif ="中等";
		}else if (get_num ==2){
			str_dif ="容易";
		}
	}
	public int getHashMap(int getid ){
		return (Integer.valueOf(map.get(getid)).intValue());
		
	}
	
	public void set(Cursor cursor){
		this.m_cursor = cursor;
		notifyDataSetInvalidated();
	}
	public void getTablename(String str){
		this.str_table = str;
	}
	public void setSelectedItem(int select_item){
		this.Item =  select_item;
		notifyDataSetInvalidated();
		all = false;
	}
	public void setAllItem(){
		all = true;
	}
	static class holdView {
		TextView tv_1 = null;
		TextView tv_2 = null;
		TextView tv_3 = null;
		TextView tv_4 = null;
		TextView tv_5 = null;
//		ImageButton ib_6 = null;
		TextView tv_6 = null;
	}
}
