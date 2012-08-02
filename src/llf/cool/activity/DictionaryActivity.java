package llf.cool.activity;

import java.util.ArrayList;

import llf.cool.R;
import llf.cool.adapter.FuzzySearchAdapter;
import llf.cool.adapter.HistoryAdapter;
import llf.cool.adapter.HistoryAdapter.ViewHolder;
import llf.cool.data.HistoryDB;
import llf.cool.util.PubFunc;
import llf.dictionary.engine.LLFDic;
import llf.dictionary.engine.VHConst;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class DictionaryActivity extends TabActivity implements OnClickListener,	
																OnGestureListener,
																 OnTouchListener
{
	/** Called when the activity is first created. */
	private TabHost m_TabHost;
	private EditText m_edit;
	private ImageButton m_search_button;
	private ListView m_math_result_listview, m_history_listview,
			m_setting_listview;
	private TextView m_show_explain;
	private TextView m_intro_TextView;
	private LinearLayout m_show_explain_linearLayout;
	private LinearLayout m_search_linLinearLayout;
	private LinearLayout m_setting_options_linearLayout;
	private LinearLayout m_intro_linearLayout;
	private LinearLayout tab1,tab2,tab3;
	private HistoryAdapter m_HistoryAdapter;
	private FuzzySearchAdapter m_FuzzySearchAdapter;
	private RelativeLayout relative;
	private HistoryDB DB;
	private Context mContext;
	private Cursor mCursor;
	private static final int MSG_SEARCHED_COMPLETE = 0;
	private String m_eng_dicPaths = "/sdcard/yingyutt/dic/llf_eng_chn.dic";
	private String m_chi_dicPaths = "/sdcard/yingyutt/dic/llf_chn_eng.dic";
	private long m_wordIndex;
	private int m_WordCount;
	public boolean m_isFuzzySearch = false; // 是否模糊搜索
	public boolean m_FuzzyIsFinish = false; // 模糊搜索是否完毕
	private boolean m_FuzzyFlag = false;
	private ArrayList<Long> m_FuzzyResultList = null; // 模糊搜索结果
	private String[] m_setting_options;
	private GestureDetector mGestureDetector;

	// @Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initUI();
		initData();
		initListener();
	}

	public void initUI()
	{
		m_edit = (EditText) findViewById(R.id.edit);
		m_search_button = (ImageButton) findViewById(R.id.search);
		m_search_linLinearLayout = (LinearLayout) findViewById(R.id.search_linearLayout);
		m_show_explain_linearLayout = (LinearLayout) findViewById(R.id.show_explain_linearLayout);
		m_setting_options_linearLayout = (LinearLayout) findViewById(R.id.setting_options_linearLayout);
		m_intro_linearLayout = (LinearLayout) findViewById(R.id.intro_linearLayout);
		m_math_result_listview = (ListView) findViewById(R.id.math_result_list);
		m_history_listview = (ListView) findViewById(R.id.history_list);
		m_intro_TextView = (TextView) findViewById(R.id.intro);
		m_setting_listview = (ListView) findViewById(R.id.setting_list);
		m_show_explain = (TextView) findViewById(R.id.show_explain);
		
		tab1 = (LinearLayout) findViewById(R.id.tab1);
		relative = (RelativeLayout) findViewById(R.id.relative);
		
		mGestureDetector = new GestureDetector(this);
		m_math_result_listview.setOnTouchListener(this);
		m_math_result_listview.setLongClickable(true);
		m_history_listview.setOnTouchListener(this);
		m_history_listview.setLongClickable(true);
		m_setting_listview.setOnTouchListener(this);
		m_setting_listview.setLongClickable(true);
	}

	public void initData()
	{
		mContext = this;
		m_TabHost = getTabHost();

		//tab1--首页
		TextView text = new TextView(this);
		text.setText("首页");
		text.setTextSize(21f);
		text.setGravity(Gravity.CENTER);
		text.setHeight(80);		
		m_TabHost.addTab(m_TabHost.newTabSpec("tab1")
									.setIndicator(text)
										.setContent(R.id.tab1));
		
		//tab2--历史记录
		TextView text01 = new TextView(this);
		text01.setText("历史记录");
		text01.setTextSize(21f);
		text01.setGravity(Gravity.CENTER);
		text01.setHeight(80);
		m_TabHost.addTab(m_TabHost.newTabSpec("tab2")
									.setIndicator(text01)
										.setContent(R.id.tab2));
		//tab3--设置
		TextView text02 = new TextView(this);
		text02.setText("设置");
		text02.setTextSize(21f);
		text02.setGravity(Gravity.CENTER);
		text02.setHeight(80);
		m_TabHost.addTab(m_TabHost.newTabSpec("tab3")
									.setIndicator(text02)
										.setContent(R.id.tab3));
		
		DB = new HistoryDB(this);
		DB.open();
		m_FuzzySearchAdapter = new FuzzySearchAdapter(this);
		m_FuzzyResultList = new ArrayList<Long>();
		m_setting_options = new String[1];
		// m_setting_options[0]=getResources().getString(R.string.intro);
		m_setting_options[0] = getResources().getString(R.string.clean_words);
		m_setting_listview.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, m_setting_options));
	}

	private void initEngLibs()
	{
		int retInt = LLFDic.init(m_eng_dicPaths);
		// System.out.println("retInt=" +
		// retInt+" m_eng_dicPaths="+m_eng_dicPaths);
		if (retInt != 0)
		{
			Toast.makeText(mContext,
					getResources().getString(R.string.init_faile),
					Toast.LENGTH_SHORT).show();
		}
	}

	private void initChiLibs()
	{
		int retInt = LLFDic.init(m_chi_dicPaths);
		if (retInt != 0)
		{
			Toast.makeText(mContext,
					getResources().getString(R.string.init_faile),
					Toast.LENGTH_SHORT).show();
		}
	}

	public void initListener()
	{
		m_search_button.setOnClickListener(this);		
		tab1.setOnTouchListener(this);
		tab1.setLongClickable(true);
		relative.setOnTouchListener(this);
		relative.setLongClickable(true);
		
		m_TabHost.setOnTabChangedListener(new OnTabChangeListener(){
			@Override
			public void onTabChanged(String tabId)
			{
				// TODO Auto-generated method stub
				if (tabId.equals("tab1")){
				}
				else if (tabId.equals("tab2")){
					mCursor = DB.select();
					startManagingCursor(mCursor);
					m_HistoryAdapter = new HistoryAdapter(mContext, mCursor);
					m_history_listview.setAdapter(m_HistoryAdapter);					
				}else{
					m_setting_options_linearLayout.setVisibility(View.VISIBLE);
					m_intro_linearLayout.setVisibility(View.GONE);
				}
			}
		});
		
		m_math_result_listview.setOnItemClickListener(new OnItemClickListener()
		{

			// @Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				// TODO Auto-generated method stub
				m_search_linLinearLayout.setVisibility(View.GONE);
				m_show_explain_linearLayout.setVisibility(View.VISIBLE);
				m_show_explain.setText(null);
				String editStr = m_edit.getText().toString().trim();
				if (null != DB.selectWord(editStr))
				{
					DB.delete(editStr);
				}
				DB.insert(editStr);
				String word = new String(LLFDic.getWordByIndex(position));
				String explain = new String(LLFDic.getExplainByIndex(position));
				explain = PubFunc.filterEnglish(word,explain);
				m_show_explain.append(PubFunc.filter(explain));
				
			}
		});
		
		m_history_listview.setOnItemClickListener(new OnItemClickListener()
		{

			// @Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				// TODO Auto-generated method stub
				ViewHolder holder = (ViewHolder) view.getTag();
				String str = holder.wordName.getText().toString();
				if (str.toLowerCase().trim()!=null&&!str.toLowerCase().trim().equals(""))
				{
					if (isInputEng(str))
					{
						initEngLibs();
					}
					else
					{
						initChiLibs();
					}
					long index = LLFDic.getIndexByInput(str,1);
					String explain = new String(LLFDic.getExplainByIndex(index));

					if (null != DB.selectWord(str))
					{
						DB.delete(str);
					}
					DB.insert(str);
					m_TabHost.setCurrentTab(0);
					m_search_linLinearLayout.setVisibility(View.GONE);
					m_show_explain_linearLayout.setVisibility(View.VISIBLE);
					m_show_explain.setText(null);
					explain = PubFunc.filterEnglish(str,explain);
					m_show_explain.append(PubFunc.filter(explain));
				}
				
			}
		});
		m_setting_listview.setOnItemClickListener(new OnItemClickListener()
		{

			// @Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				// TODO Auto-generated method stub
				switch (position)
				{
				case 0:
					showMessage();
					break;
				case 1:
					m_setting_options_linearLayout.setVisibility(View.GONE);
					m_intro_linearLayout.setVisibility(View.VISIBLE);
					m_intro_TextView.setText(null);
					String intro = new String(LLFDic.getIntroText());
					m_intro_TextView.append(intro);
					break;
				}

			}
		});
		m_edit.addTextChangedListener(new TextWatcher()
		{

			// @Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count)
			{
				// TODO Auto-generated method stub
				if (!m_search_linLinearLayout.isShown())
				{
					m_search_linLinearLayout.setVisibility(View.VISIBLE);
					m_show_explain_linearLayout.setVisibility(View.GONE);
				}
				if (m_edit.getText().toString().trim() != null)
				{
					if (!m_edit.getText().toString().trim().equals(""))
					{
						if (isInputEng(m_edit.getText().toString().trim()))
						{
							initEngLibs();
						}
						else
						{
							initChiLibs();
						}
						m_WordCount = (int) LLFDic.getWordCount();
						m_FuzzySearchAdapter.setIdList(null, (int) m_WordCount);
						m_math_result_listview.setAdapter(m_FuzzySearchAdapter);
						search(m_edit.getText().toString().trim());

					}
				}
			}

			// @Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s)
			{
				// TODO Auto-generated method stub

			}
		});
	}

	public boolean isInputEng(String str)
	{

		char first = str.charAt(0);
		if ((first >= 'a' && first <= 'z') || (first >= 'A' && first <= 'Z'))
		{
			return true;
		}

		else
			return false;

	}

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		LLFDic.free();
		DB.close();
		super.onDestroy();
	}

	public void showMessage()
	{
		new AlertDialog.Builder(this)
				.setMessage(R.string.clean_words)
				.setPositiveButton(R.string.yes,
						new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog,
									int which)
							{
								DB.clearAll();
							}
						})
				.setNegativeButton(R.string.no,
						new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog,
									int which)
							{

							}

						}).show();
	}

	private void search(String searchStr)
	{

		// 模糊查询
		int pos1 = searchStr.indexOf("?");
		int pos2 = searchStr.indexOf("*");
		m_isFuzzySearch = false;
		if (pos1 > 0 || pos2 > 0)
		{
			m_isFuzzySearch = true;

			if (LLFDic.fuzzySearchReady(searchStr))
			{
				m_FuzzyFlag = true;
				initFuzzyData();
			}
		}
		else
		{
			initNormalData(searchStr);
		}
	}

	// 模糊搜索生成动态数组，加入数据
	private void initFuzzyData()
	{
		m_FuzzyIsFinish = false;
		m_FuzzyResultList.clear();
		ProFuzzySearch fuzzySearch = new ProFuzzySearch();
		fuzzySearch.start();
	}

	private void initNormalData(String searchStr)
	{

		long index = LLFDic.getIndexByInput(searchStr,
				VHConst.FIND_LEXICAL_NEAREST);
		if (m_FuzzyFlag == true)
		{
			m_FuzzyFlag = false;
			m_FuzzySearchAdapter.setIdList(null, (int) m_WordCount);
		}
		m_FuzzySearchAdapter.notifyDataSetChanged();
		m_math_result_listview.setSelection((int) index);
		// showExplain(m_dicPaths[m_spinnerPosition], index);
	}

	class ProFuzzySearch extends Thread
	{
		private long index = 0;

		public void run()
		{
			while (true)
			{
				index = LLFDic.fuzzySearchNext();
				if (VHConst.UINT_MAX == index)
				{
					m_FuzzyIsFinish = true;
					handler.sendEmptyMessage(MSG_SEARCHED_COMPLETE);
					break;
				}
				m_FuzzyResultList.add(index);
			}
		}
	}

	private Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case MSG_SEARCHED_COMPLETE:
				m_FuzzySearchAdapter.setIdList(m_FuzzyResultList, 0);
				m_FuzzySearchAdapter.notifyDataSetChanged();
				if (m_FuzzyResultList.isEmpty())
				{
					m_wordIndex = VHConst.INVALID_INDEX;
					// showExplain(m_wordIndex); // hint: no result
				}
				else
				{
					m_math_result_listview.setSelection(0);
					m_wordIndex = m_FuzzyResultList.get(0);
					// showExplain(m_wordIndex);
				}
				break;
			}
		}
	};

	
	@Override
	public void onClick(View v){
		// TODO Auto-generated method stub
		switch (v.getId()){
			case R.id.search:
				String editStr = m_edit.getText().toString().trim();
				if (null != editStr && !editStr.equals("")){
					if (isInputEng(editStr)){
						initEngLibs();
					} else {
						initChiLibs();
					}
					
					if (null != DB.selectWord(editStr)){
						DB.delete(editStr);
					}
					
					DB.insert(editStr);
	
					m_search_linLinearLayout.setVisibility(View.GONE);
					m_show_explain_linearLayout.setVisibility(View.VISIBLE);
					m_show_explain.setText(null);
	//				String word = new String(LLFDic.getWordByIndex(LLFDic
	//						.getIndexByInput(editStr, 1)));
					String explain = new String(LLFDic.getExplainByIndex(LLFDic
							.getIndexByInput(editStr, 1)));
					// m_edit.setText(word);
					explain = PubFunc.filterEnglish(editStr, explain);
	//				m_show_explain.append(word);
	//				m_show_explain.append("\n");
					m_show_explain.append(explain);
	
				}else{
					Toast.makeText(mContext,
							getResources().getString(R.string.please_input),
							Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event){
		// TODO Auto-generated method stub
		System.out.println("---------onTouch----------------");
		return mGestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e){
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, 
								float velocityX, float velocityY){
		//向左手势
		if(e1.getX()-e2.getX()>120){
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putInt("Key_sometable", 0);
			intent.setClass(mContext, NewWordsActivity.class);
			intent.putExtras(bundle);
			mContext.startActivity(intent);
			overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
			
		}else if(e2.getX()-e1.getX()>120){ //向右手势
			Intent intent = new Intent(this, AudioActivity.class);
			intent.putExtra("CurrentListActivity", "CurrentListActivity");
			startActivity(intent);
			overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e){
		// TODO Auto-generated method stub		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, 
								float distanceX, float distanceY){
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e){
		// TODO Auto-generated method stub		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e){
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(keyCode == event.KEYCODE_BACK){
			Intent intent = new Intent(this,FileListActivity.class);
			startActivity(intent);
			this.finish();
			super.onDestroy();
		}
		return super.onKeyDown(keyCode, event);
	}
}