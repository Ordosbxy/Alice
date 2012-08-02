package llf.cool.activity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import llf.cool.R;
import llf.cool.adapter.Baseresult;
import llf.cool.adapter.Manageadapter;
import llf.cool.adapter.Myadapter;
import llf.cool.data.MyDatabase;
import llf.cool.view.Mytestview;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.util.Log;

import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class NewWordsActivity extends Activity implements OnClickListener,OnGestureListener,OnTouchListener
{
	private ListView m_listview = null;
	// add new
	private ListView dia_list = null;
	private SimpleCursorAdapter dia_list_adapter = null;
	private Cursor group_cursor = null;
	private Cursor tablecount_cursor = null;
	//
	private Manageadapter mange_adapter = null;
	private MyDatabase words_database = null;
	private Cursor words_cursor = null;
	private Dialog m_d = null;
	private EditText m_edit = null;
	private Myadapter m_adapter = null;
	private SimpleAdapter m_simpleadapter = null;
	private static Spinner spinner_time, spinner_order;
	private Rect[] rect = { new Rect(230, 400, 312, 430),
			new Rect(330, 400, 412, 440) };
	private Rect[] rect_select = { new Rect(160, 150, 260, 180),
			new Rect(160, 200, 260, 230), new Rect(160, 250, 260, 280),
			new Rect(160, 300, 260, 330) };
	private Rect[] dictItem_rect = { new Rect(160, 150, 560, 180),
			new Rect(160, 200, 560, 230), new Rect(160, 250, 560, 280),
			new Rect(160, 300, 560, 330) };
	private int resId[] = { R.drawable.jump, R.drawable.commit };

	private Rect[] repareRect = { new Rect(340, 275, 400, 300),
			new Rect(70, 100, 137, 116), new Rect(70, 120, 137, 136),
			new Rect(10, 10, 74, 42), new Rect(65, 10, 136, 40) };
	private Mytestview m_testview;
	private boolean tableFlag = false;
	private int temp = 0;// 题目的增加

	private static final int[] BTN = { R.id.button_close, R.id.button_all_del,
			R.id.button_del, R.id.button_save, R.id.button_change };
	private static final int[] TEST_BTN = { R.id.start_menu, R.id.start,
			R.id.start_close };
	private static final int[] DIALOGUE_BTN = { R.id.dialog_button_1,
			R.id.dialog_button_2, R.id.dialog_button_3, R.id.dialog_button_4 };
	private static final int[] WIDGT_ID = new int[] { R.id.text1, R.id.text2,
	/* R.id.text3, R.id.text4,*/ R.id.text5 };
	private static final String[] SHOW_STR = new String[] {
			MyDatabase.FILED_CURSOR_ID, MyDatabase.FILED_WORDS,
			MyDatabase.FILED_PHONETICS, MyDatabase.FILED_EXPLAIN,
			MyDatabase.FILED_TIME };

	private int id;
	private int table_id;
	private int count;
	private int testcount;
	private StringBuffer m_sb;
	private int newwords_year;
	private int newwords_month;
	private int newwords_day;
	private String table_str = "newwords_table";
	private Random m_random = new Random();
	private boolean flag = false;
	private int flag_nums;
	int xiangyingnum = 0;
	private String[] str_buf = new String[4];
	private String str_explain;
	private String a, b, c, d;
	private String word_str = null;
	private String dict_str = null;
	private int rightNum = 0;
	private int errorNum = 0;
	private int notAnswerNum = 0;
	private int rand = 0;
	private int select_right = 2;
	private int temp_b, temp_c, temp_d;
	private boolean temp_flag;
	// private TextView m_text;
	private TextView showresult_test;
	// add new
	private TextView wordsCount_text;
	private int currentWords_count = 0;
	private String ran_str = "so great";
	private int jump_repeat_words = 0;
	private ListView result_list;
	private ArrayList<Map<Object, String>> array_list = new ArrayList<Map<Object, String>>();
	private Baseresult m_result_base;
	private String get_answer;
	private String get_result;
	private String get_right_answer;
	private String word_str_subject, word_str_answer, word_str_result,
			word_str_rightanswer;
	private byte[] dict_str_rightanswer;
	private boolean isAdd = false;
	private boolean allDifficultly;
	private String getImageNum = "0";

	private ImageButton m_b1, m_b2;
	private int selected_subject = 0;
	private int whichTableId = 0;
	private byte[] newwords_explain;
	private byte[] newwords_phonetic;
	private Dialog newtable_dialog;
	private Dialog testresult_dialog;
	private static Dialog explain_dialog;
	private boolean isSave = false;
	private boolean isDeleteAll = false;
	private int[] saveOrderNum = new int[2];
	private byte[] getexplainbyteAns;
	private byte[] getexplain_byte_one;
	private byte[] getexplain_byte_two;
	private byte[] getexplain_byte_three;
	private byte[] byte_a;
	private byte[] byte_b;
	private byte[] byte_c;
	private byte[] byte_d;
	private byte[] getDictAnswer;
	Intent intent;
	private Dialog isSureDialog;
	private boolean selectedFlag;
	private int getTableNum;
	private String str_getTableNum;
	private boolean isOnTableManage = false;
	private String current_book;
	private Dialog menu_dialog;
	private int addTableNum;
	private ImageButton[] dia_button = new ImageButton[4];
	private boolean isSlectedItem;
	private boolean sel_one;
	private boolean set_two;
	private TextView m_dialog_text;
	// add
	private ImageButton menu_button;
	private ViewGroup menuShowLayer;
	private static ViewGroup menuShow;
	private TextView helpText;
	private OnTouchListener mOnTouchListener;
	private String temp_table;
	private boolean mangeflag;
	private boolean isOpenTable;
	private String saveOpenTable;
	private String str_drawwords, str_drawexplain;
	private byte[] str_drawphonetic;
	private LinearLayout mainlayout;
	private GestureDetector mGestureDetector;
	// add 06-11
	private ImageButton m_move;
	private Dialog move_dialog;
	private ListView move_list;
	private ImageButton move_add, move_cancel;
	private byte[] byte_drawphonetic, byte_drawexplain;
	private String str_move;
	private boolean move_flag;
	private boolean isInitManage = false;
	private boolean selecetedOkay = false;
	private TextView m_titleView;
	private String my_newwords = "我的生词本";
	private boolean isSucess = false;
	private boolean defaultFlag = false;
	private boolean delFlag = false;
	private int data_num = 0;
	private static boolean getreturn;
	private boolean newordsFlag = false;
	private boolean savenewwordsFlag = false;
	private boolean confirmFlag = false;
	private static final int JUMP_MSG = 1;
	private ProgressDialog mypDialog;
	private ReadDataThread m_thread;
	private boolean select_item_flag = false;
	private boolean initFlag = false;
	private boolean isMoveFlag = false;
	private LinearLayout explainShow;
	private TextView mtext;

	/** Called when the activity is first created. */
	// //@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.newwords);
		progress();
		m_thread = new ReadDataThread();
		m_thread.start();
		Log.i("bxy", "onCreate");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("bxy", "onResume");
	}
	
	class ReadDataThread extends Thread
	{
		public void run()
		{
			handler.sendEmptyMessageDelayed(JUMP_MSG, 1000);
		}
	}

	private Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case JUMP_MSG:
				initdata();
				mypDialog.dismiss();
				m_thread = null;
				break;
			}
		}
	};

	public void progress()
	{
		mypDialog = new ProgressDialog(this);
		mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mypDialog.setTitle("提示");
		mypDialog.setMessage("正在搜索中，请稍候......");
		mypDialog.setIndeterminate(false);
		mypDialog.setCancelable(true);
		mypDialog.setMax(20);
		mypDialog.show();
		mypDialog.setOnKeyListener(new OnKeyListener()
		{

			// @Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event)
			{
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK)
				{
					System.exit(0);
				}
				return false;
			}
		});
	}

	public void initdata()
	{
		setContentView(R.layout.newwords);
		explainShow = (LinearLayout)findViewById(R.id.explainShow);
		mtext = (TextView) findViewById(R.id.explainText);
		menuShowLayer = (ViewGroup) findViewById(R.id.menuShowLayer);
		menuShow = (ViewGroup) findViewById(R.id.menuShow);
		helpText = (TextView) findViewById(R.id.helpText);
		m_titleView = (TextView) findViewById(R.id.title);
		mainlayout = (LinearLayout) findViewById(R.id.mainlayout);
		mGestureDetector = new GestureDetector(this);
		mainlayout.setOnTouchListener(this);
		mainlayout.setLongClickable(true);
		for (int id : BTN)
		{
			ImageButton btn = (ImageButton) findViewById(id);
			btn.setOnClickListener(this);
		}
		m_move = (ImageButton) findViewById(R.id.button_move);
		m_move.setOnClickListener(this);
		menu_button = (ImageButton) findViewById(R.id.button_menu);
		menu_button.setOnClickListener(this);
		mOnTouchListener = new OnTouchListener()
		{

			// //@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				// TODO Auto-generated method stub
				menuOut();
				return true;
			}
		};
		// m_text = (TextView) findViewById(R.id.text123);
		if (words_database == null)
		{
			words_database = new MyDatabase(this);
		}
		initUi();
		if (whichTableId == 0)
		{
			tablecount_cursor = words_database
					.cursorTable(words_database.FIELD_TABLE_COUNT);
			m_titleView.setText("单词生词本");
		}
		startManagingCursor(tablecount_cursor);
		addTableNum = tablecount_cursor.getCount();
	}

	public void initUi()
	{
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter.add(getResources().getString(R.string.alltime));
		adapter.add(getResources().getString(R.string.nowday));
		adapter.add(getResources().getString(R.string.inthreedays));
		adapter.add(getResources().getString(R.string.weekly));
		//
		m_listview = (ListView) findViewById(R.id.my_list);
		spinner_time = (Spinner) findViewById(R.id.Spinner_1);
		spinner_time.setAdapter(adapter);
		spinner_time.setOnTouchListener(new OnTouchListener()
		{

			// @Override
			public boolean onTouch(View v, MotionEvent event)
			{
				// TODO Auto-generated method stub
				if (menuShow.getVisibility() == View.VISIBLE)
				{
					return true;
				}
				return false;
			}
		});

		spinner_time.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			// //@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3)
			{
				// TODO Auto-generated method stub
				allDifficultly = false;
				select_item_flag = true;
				sortTime(position);
				if (m_adapter != null)
				{
					m_adapter.setSelectedItem(-1);
				}
				saveOrderNum[0] = position;
			}

			// //@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
				// TODO Auto-generated method stub
			}
		});
		if (words_database == null)
		{
			words_database = new MyDatabase(this);
		}
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null)
		{
			int which = bundle.getInt("Key_sometable");
			if (which == 0)
			{
				words_cursor = words_database.selectSomeTable("newwords_table");
				table_str = "newwords_table";
				whichTableId = which;
			}

			startManagingCursor(words_cursor);
			m_adapter = new Myadapter(this, words_cursor, words_database,
					table_str, whichTableId);
			
			Log.i("NewWords", ">>>wordscursor+"+words_cursor.getCount());
			
			m_listview.setAdapter(m_adapter);
			m_listview.setOnTouchListener(this);
			m_listview.setLongClickable(true);
			m_listview.setOnItemClickListener(new OnItemClickListener()
			{
				// //@Override
				public void onItemClick(AdapterView<?> arg0, View view,
						int position, long arg3)
				{
					// TODO Auto-generated method stub
					if (menuShow.getVisibility() == View.VISIBLE)
					{
						menuOut();
						return;
					}
					isDeleteAll = false;
					selectedFlag = true;
					selecetedOkay = true;
					select_item_flag = false;
					m_listview.setBackgroundResource(R.drawable.list_back);
					if (delFlag == true)
					{
						if (saveOrderNum[0] != 0 || saveOrderNum[1] != 0)
						{
							if (!allDifficultly)
							{
								words_cursor = words_database.partQuery(
										table_str,
										String.valueOf(newwords_day), data_num,
										saveOrderNum[1],
										String.valueOf(newwords_year),
										String.valueOf(newwords_month));
							}
							else
							{
								words_cursor = words_database.getQuery(
										table_str, data_num - 1,
										saveOrderNum[0],
										String.valueOf(newwords_day),
										String.valueOf(newwords_year),
										String.valueOf(newwords_month));
							}
							startManagingCursor(words_cursor);
						}
						else
						{
							words_cursor = words_database
									.selectSomeTable(table_str);
							startManagingCursor(words_cursor);
						}
					}
					else
					{
						words_cursor = words_database
								.selectSomeTable(table_str);
						startManagingCursor(words_cursor);
					}
					if (getreturn == true)
					{
						words_cursor = words_database
								.selectSomeTable(table_str);
						startManagingCursor(words_cursor);
						getreturn = false;
						delFlag = false;
					}
					words_cursor.moveToPosition(position);
					id = words_cursor.getInt(0);
					str_drawwords = words_cursor.getString(2);
					if (whichTableId == 0)
					{
						// str_drawphonetic = words_cursor.getString(3);
						str_drawphonetic = words_cursor.getBlob(3);
						str_drawexplain = words_cursor.getString(4);
//						explainShow.setVisibility(View.VISIBLE);
						mtext.setText("");
						mtext.setText(str_drawexplain);
						callActivity(str_drawexplain,view.getBottom(),view.getBottom()+mtext.getHeight());
						
//				      int buttom=view.getBottom();
//				     int b = m_listview.getChildAt(position).getBottom();
//				     System.out.println("   b ="+b+" buttom = "+buttom);
//				      int left = view.getLeft();
//				      int height = view.getHeight();
//				      explainShow.setLayoutParams(new AbsoluteLayout.LayoutParams(130, buttom, 130+mtext.getWidth(), buttom+mtext.getHeight()));
//				      System.out.println(explainShow.getLeft()+"   "+explainShow.getRight()+"   "+explainShow.getBottom()+"   "+explainShow.getHeight()+"  buttom = "+buttom);
					}
					Drawable m_draw = getResources().getDrawable(
							R.drawable.selected_item);
					m_listview.setSelector(m_draw);
					m_listview.setClickable(true);
					m_adapter.setSelectedItem(position);
				}
			});
			m_listview.setOnItemSelectedListener(new OnItemSelectedListener()
			{
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3)
				{
					// TODO Auto-generated method stub
					Drawable m_draw = getResources().getDrawable(
							R.drawable.selected_item);
					m_listview.setSelector(m_draw);
				}

				public void onNothingSelected(AdapterView<?> arg0)
				{
					// TODO Auto-generated method stub
				}
			});
			startManagingCursor(words_cursor);
			if (whichTableId == 0)
			{
				group_cursor = words_database.query();
			}
			else if (whichTableId == 1)
			{
				group_cursor = words_database.queryDict();
			}
			startManagingCursor(group_cursor);
			// mange_adapter.setChange(group_cursor);
			// add 2011-06-14
			for (int i = 0; i < group_cursor.getCount(); i++)
			{
				String str_getTablename = null;
				group_cursor.moveToPosition(i);
				str_getTablename = group_cursor.getString(group_cursor
						.getColumnIndex(words_database.FILED_GROUP));
				if (my_newwords.equals(str_getTablename))
				{
					return;
				}
			}
			if (whichTableId == 0)
			{
				words_database.insertTable(my_newwords, "manage_words");
				words_database.insertTableCount(
						words_database.FIELD_TABLE_COUNT, addTableNum);
				words_database.addTable("manage_words");
			}
		}
	}

	private void callActivity(String explain,int top,int buttom)
	{
		Intent i = new Intent(this,RandActivity.class);
		Bundle b = new Bundle();
		b.putString("explain", explain);
		b.putInt("top", top);
		b.putInt("buttom", buttom);
		i.putExtras(b);
		startActivity(i);
		
	}
	static public Typeface getFont(Context context)
	{
		Typeface m_type = Typeface.createFromAsset(context.getAssets(),
				"font/DFHGWPRN.TTF");
		return m_type;
	}

	static public void allSpinnerStatus()
	{
		spinner_order.setSelection(0);
		spinner_time.setSelection(0);
	}

	static public void showInfo(Context context)
	{
		LayoutInflater lf = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = lf.inflate(R.layout.testview, null);
	}

	public void menuOut()
	{
//		menuShowLayer.setOnTouchListener(null);
//		explainShow.setOnTouchListener(null);
		Animation mAnimation2 = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
				0.0f);
		mAnimation2.setDuration(1000);
		menuShow.startAnimation(mAnimation2);
		menuShow.setVisibility(View.GONE);
//		explainShow.setVisibility(View.GONE);
	}

	static public boolean phoneticClickFunc()
	{
		if (menuShow.getVisibility() == View.VISIBLE)
		{
			return true;
		}
		return false;
	}

	// //@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
//		if (explainShow.getVisibility() == View.VISIBLE)
		
		switch (v.getId())
		{
		case R.id.button_menu:
			// showMenu();
//			if (menuShow.getVisibility() == View.GONE)
//			{
//				Animation mAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f,
//						1.0f, Animation.RELATIVE_TO_SELF, 1.0f,
//						Animation.RELATIVE_TO_SELF, 0.0f);
//				mAnimation.setDuration(1000);
//				menuShow.setVisibility(View.VISIBLE);
////				menuShowLayer.setOnTouchListener(mOnTouchListener);
//				helpText.setText(menuInfo());
//				menuShow.startAnimation(mAnimation);
//				menuShowLayer.bringToFront();
//				menuShow.bringToFront();
//			}
//			else
//			{
//				menuOut();
//			}
			break;
		case R.id.button_close:
			this.setResult(1, intent);
			finish();
			break;
		case R.id.button_all_del:// 全删
			if (menuShow.getVisibility() == View.VISIBLE)
			{
				return;
			}
			mangeflag = false;
			select_item_flag = false;
			selectAllItem();
			break;
		case R.id.button_del:// 删除
			if (menuShow.getVisibility() == View.VISIBLE)
			{
				return;
			}
			if (!selecetedOkay)
			{
				return;
			}
			if (!initFlag)
			{
				select_item_flag = false;
				delFlag = false;
				initFlag = true;
			}
			if (select_item_flag == true)
			{
				return;
			}
			select_item_flag = true;
			words_cursor = words_database.selectSomeTable(table_str);
			startManagingCursor(words_cursor);
			int Count_new = words_cursor.getCount();
			if (selectedFlag == true && Count_new != 0)
			{
				selectedFlag = false;
				if (!mangeflag)
				{
					isDelete();
				}
				mangeflag = false;
			}
			break;
		case R.id.button_save:// 存为新生词本
			// saveTable();
			if (menuShow.getVisibility() == View.VISIBLE)
			{
				return;
			}
			isSave = true;
			if (!savenewwordsFlag)
			{
				savenewwordsFlag = true;
				confirmFlag = false;
				buildTableName();
			}
			break;
		case R.id.button_change:// 生词本管理
			if (menuShow.getVisibility() == View.VISIBLE)
			{
				return;
			}
			if (!newordsFlag)
			{
				newordsFlag = true;
				showDialog();
			}
			sel_one = false;
			isSlectedItem = false;
			set_two = false;
			isInitManage = true;

			break;
		// case R.id.button_test://测试
		// if (words_cursor.getCount() < 4){
		// return;
		// }
		// beginFunc();
		// break;

		case R.id.dialog_button_1:// 打开选中的表名
			if (isSlectedItem == true)
			{
				isSlectedItem = false;
				dia_button[0].setBackgroundResource(R.drawable.open_table_inv);
			}
			if (!set_two)
			{
				return;
			}

			if (!isExistTable(temp_table))
			{
				return;
			}
			;

			isOpenTable = true;
			table_str = temp_table;
			saveOpenTable = table_str;

			selecetedOkay = false;
			selectedFlag = false;
			defaultFlag = true;

			dia_button[0].setBackgroundResource(R.drawable.open_table_inv);
			// add
			if (table_str.equals("manage_words"))
			{
				table_str = "newwords_table";
			}
			else if (table_str.equals("manage_dict"))
			{
				table_str = "words_table";
			}
			newordsFlag = false;
			openTable(table_str);
			spinner_order.setSelection(0);
			spinner_time.setSelection(0);
			delFlag = false;
			// m_text.setText(getResources().getString(R.string.currentwordsbook)
			// + current_book);
			// showCurrentCount();
			m_d.dismiss();
			break;
		case R.id.dialog_button_2:// 新建表名
			confirmFlag = false;
			buildTableName();
			break;
		case R.id.dialog_button_3:// 删除表名
			if (isSlectedItem == true)
			{
				isSlectedItem = false;
				sel_one = true;
				dia_button[2]
						.setBackgroundResource(R.drawable.delete_table_inv);
			}
			if (!set_two)
			{
				return;
			}

			if (temp_table.equals("manage_words"))
			{
				table_str = "newwords_table";
				dia_button[2].setBackgroundResource(R.drawable.delete_table);
				return;
			}
			else if (temp_table.equals("manage_dict"))
			{
				table_str = "words_table";
				dia_button[2].setBackgroundResource(R.drawable.delete_table);
				return;
			}
			if (!isExistTable(temp_table))
			{
				return;
			}
			;

			// if (table_str.equals("newwords_table") ||
			// table_str.equals("words_table")){
			// return ;
			// }
			dia_button[2].setBackgroundResource(R.drawable.delete_table_inv);
			// isDeleteAll = false;
			isOnTableManage = true;
			isDelete();
			break;
		case R.id.dialog_button_4:// 关闭对话框
			m_d.dismiss();
			set_two = false;
			newordsFlag = false;
			break;
		case R.id.b1:// 确定
			errorNum = 0;
			rightNum = 0;
			notAnswerNum = 0;
			m_result_base.removeFunc();
			beginFunc();
			break;
		case R.id.b2:// 再测试
			if (m_testview == null)
			{
				m_testview = new Mytestview(this);
			}
			errorNum = 0;
			rightNum = 0;
			notAnswerNum = 0;
			m_result_base.removeFunc();
			setContentView(m_testview);
			testFunc();
			break;
		case R.id.okay:
			String tableName = m_edit.getText().toString().trim();
			if (tableName.equals(""))
			{
				return;
			}
			if (!confirmFlag)
			{
				confirmFlag = true;
				if (isSave == true)
				{
					if (whichTableId == 0)
					{
						group_cursor = words_database.query();
					}
					else if (whichTableId == 1)
					{
						group_cursor = words_database.queryDict();
					}
					startManagingCursor(group_cursor);
				}

				for (int i = 0; i < group_cursor.getCount(); i++)
				{
					String str_getTablename = null;
					group_cursor.moveToPosition(i);
					str_getTablename = group_cursor.getString(group_cursor
							.getColumnIndex(words_database.FILED_GROUP));
					if (tableName.equals(str_getTablename))
					{
						// newtable_dialog.dismiss();
						information();
						m_edit.setText("");
						return;
					}
				}
				++addTableNum;
				if (whichTableId == 0)
				{
					str_getTableNum = "abc" + String.valueOf(addTableNum);
				}
				else if (whichTableId == 1)
				{
					str_getTableNum = "dict" + String.valueOf(addTableNum);
				}
				if (isSave == true)
				{
					isSave = false;
					savenewwordsFlag = false;
					if (whichTableId == 0)
					{// 背单词
						words_database.insertTable(tableName, str_getTableNum);
						words_database.insertTableCount(
								words_database.FIELD_TABLE_COUNT, addTableNum);
						words_database.addTable(str_getTableNum);
						String getwords, getexplain, gettime, getimage;
						byte[] getphonetic;
						String getyear, getmonth, getday;
						int getNum = words_cursor.getCount();
						for (int i = 0; i < getNum; i++)
						{
							words_cursor.moveToPosition(i);
							getwords = words_cursor.getString(2);
							// getphonetic = words_cursor.getString(3);
							getphonetic = words_cursor.getBlob(3);
							getexplain = words_cursor.getString(4);
							gettime = words_cursor.getString(5);
							getimage = words_cursor.getString(12);

							getyear = words_cursor.getString(words_cursor
									.getColumnIndex(words_database.FIELD_YEAR));
							getmonth = words_cursor
									.getString(words_cursor
											.getColumnIndex(words_database.FIELD_MONTH));
							getday = words_cursor.getString(words_cursor
									.getColumnIndex(words_database.FIELD_DAY));
							words_database.getString(getyear, getmonth, getday);
							words_database.insertOnTable(str_getTableNum,
									getwords, getphonetic, getexplain, gettime,
									getimage, "0");
						}
					}
					group_cursor.requery();
				}
				else
				{
					if (whichTableId == 0)
					{
						words_database.insertTable(tableName, str_getTableNum);
						words_database.insertTableCount(
								words_database.FIELD_TABLE_COUNT, addTableNum);
					}
					words_database.addTable(str_getTableNum);
					if (whichTableId == 0)
					{
						group_cursor = words_database.query();
					}
					// else if (whichTableId == 1)
					// {
					// group_cursor = words_database.queryDict();
					// }
					startManagingCursor(group_cursor);
					mange_adapter.setChange(group_cursor);
					// group_cursor.requery();
					// dia_list.invalidateViews();

				}
				newtable_dialog.dismiss();
			}

			break;
		case R.id.cancel:
			isSave = false;
			savenewwordsFlag = false;
			newtable_dialog.dismiss();
			break;
		case R.id.look:
			setContentView(R.layout.result);
			ImageButton menu_btn = (ImageButton) findViewById(R.id.result_menu);
			ImageButton close_btn = (ImageButton) findViewById(R.id.result_close);
			menu_btn.setOnClickListener(this);
			close_btn.setOnClickListener(this);
			showresult();
			words_cursor.requery();
			result_list.invalidateViews();
			testresult_dialog.dismiss();
			break;
		case R.id.result_menu:
			// new Menudialog(this).show();
			break;
		case R.id.result_close:
			finish();
			break;
		// 测试界面按钮
		case R.id.start_menu:
			break;
		case R.id.start_close:
			finish();
			break;
		case R.id.start:
			m_testview = new Mytestview(this);
			setContentView(m_testview);
			m_testview.loadBack(R.drawable.background);
			if (whichTableId == 0)
			{
				testFunc();
			}
			// else if (whichTableId == 1)
			// {
			// dictTestFunc();
			// }
			break;
		case R.id.yes:
			if (isOnTableManage == true)
			{
				// add
				// table_str = temp_table ;
				mange_adapter.setBackground(-1);
				isOnTableManage = false;
				// delAllItem();
				if (whichTableId == 0)
				{
					words_database.deleteListTable(table_id);
				}
				else if (whichTableId == 1)
				{
					words_database.delListTableOnDic(table_id);
				}
				if (isSlectedItem == true || sel_one == true)
				{
					sel_one = false;
					dia_button[2].setBackgroundResource(R.drawable.del_click);
				}

				words_database.deleteTable(temp_table);

				if (whichTableId == 0)
				{
					group_cursor = words_database.query();
				}
				else if (whichTableId == 1)
				{
					group_cursor = words_database.queryDict();
				}
				startManagingCursor(group_cursor);
				dia_button[0].setBackgroundResource(R.drawable.open_table);
				dia_button[2].setBackgroundResource(R.drawable.delete_table);
				mange_adapter.setChange(group_cursor);

				group_cursor.moveToPosition(0);
				table_str = group_cursor.getString(2);
				// m_text.setText(getResources().getString(
				// R.string.currentwordsbook)
				// + group_cursor.getString(1));
				if (table_str.equals("manage_words"))
				{
					table_str = "newwords_table";
				}
				else if (table_str.equals("manage_dict"))
				{
					table_str = "words_table";
				}

				saveOpenTable = table_str;
				defaultFlag = true;
				isOpenTable = true;
				openTable(table_str);
				selecetedOkay = false;
			}
			else
			{
				m_adapter.setSelectedItem(-1);
				if (!defaultFlag)
				{
					if (table_str.equals("manage_words"))
					{
						table_str = "newwords_table";
					}
					else if (table_str.equals("manage_dict"))
					{
						table_str = "words_table";
					}
				}
				if (isOpenTable == true)
				{
					table_str = saveOpenTable;
					if (table_str.equals("manage_words"))
					{
						table_str = "newwords_table";
					}
					else if (table_str.equals("manage_dict"))
					{
						table_str = "words_table";
					}
				}
				if (isDeleteAll == true)
				{
					isDeleteAll = false;
					if (delFlag == true)
					{
						delAccordingtodifficult();
					}
					else
					{
						delAllItem();
					}
				}
				else
				{
					words_database.delOnTable(table_str, id);
					if (delFlag == true)
					{
						Cursor diffcult_cursor = words_database.getQuery(
								table_str, saveOrderNum[1] - 1,
								saveOrderNum[0], String.valueOf(newwords_day),
								String.valueOf(newwords_year),
								String.valueOf(newwords_month));
						startManagingCursor(diffcult_cursor);
						m_adapter.set(diffcult_cursor);
					}
					else
					{
						words_cursor = words_database
								.selectSomeTable(table_str);
						startManagingCursor(words_cursor);
						m_adapter.set(words_cursor);
					}
				}
				// showCurrentCount();
			}
			isSureDialog.dismiss();
			break;
		case R.id.dismiss:
			selectedFlag = true;
			isOnTableManage = false;
			select_item_flag = false;
			sel_one = true;
			if (isSlectedItem == true || sel_one == true)
			{
				sel_one = false;
				if (isInitManage == true)
				{
					dia_button[2].setBackgroundResource(R.drawable.del_click);
				}
				if (!isOpenTable)
				{
					if (whichTableId == 0)
					{
						table_str = "newwords_table";
					}
					else if (whichTableId == 1)
					{
						table_str = "words_table";
					}
				}
			}
			isSureDialog.dismiss();
			break;
		case R.id.button_move:
			if (menuShow.getVisibility() == View.VISIBLE)
			{
				return;
			}
			if (!selecetedOkay)
			{
				return;
			}

			if (select_item_flag == true)
			{
				return;
			}
			if (selectedFlag == true)
			{
				selectedFlag = false;
				moveFunc();
			}

			break;
		case R.id.move_add:
			if (move_flag == true)
			{
				move_flag = false;
				if (!isDeleteAll)
				{
					wordsMoveToSomeTable();
				}
				else
				{
					allMoveFunc();
				}
				selectedFlag = true;
				move_dialog.dismiss();
			}
			break;
		case R.id.move_cancel:
			move_flag = false;
			selectedFlag = true;
			move_dialog.dismiss();
			break;
		}
	}

	public void information()
	{
		final Builder mypDialog = new AlertDialog.Builder(this);
		mypDialog.setTitle("提示");
		mypDialog.setMessage("生词本已存在，请重新输入...");
		mypDialog.setNegativeButton("确定", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int whichButton)
			{
				confirmFlag = false;
			}
		});
		mypDialog.setOnKeyListener(new OnKeyListener()
		{

			// @Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event)
			{
				// TODO Auto-generated method stub
				confirmFlag = false;
				return false;
			}
		});

		mypDialog.show();
	}

	public void moveFunc()
	{
		move_dialog = new Dialog(this, R.style.dialog);
		move_dialog
				.requestWindowFeature(move_dialog.getWindow().FEATURE_NO_TITLE);
		move_dialog.setContentView(R.layout.move_table);
		move_list = (ListView) move_dialog.findViewById(R.id.move_list);

		move_add = (ImageButton) move_dialog.findViewById(R.id.move_add);
		move_cancel = (ImageButton) move_dialog.findViewById(R.id.move_cancel);
		move_add.setOnClickListener(this);
		move_cancel.setOnClickListener(this);

		if (whichTableId == 0)
		{
			group_cursor = words_database.query();
		}
		else if (whichTableId == 1)
		{
			group_cursor = words_database.queryDict();
		}
		startManagingCursor(group_cursor);
		mange_adapter = new Manageadapter(this, group_cursor, words_database);

		move_list.setAdapter(mange_adapter);
		move_list.setOnItemClickListener(new OnItemClickListener()
		{

			// @Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3)
			{
				// TODO Auto-generated method stub
				group_cursor.moveToPosition(position);
				str_move = group_cursor.getString(2);
				m_adapter.getTablename(str_move);
				mange_adapter.setBackground(position);
				mange_adapter.notifyDataSetInvalidated();
				move_flag = true;
			}
		});
		move_dialog.setOnKeyListener(new OnKeyListener()
		{

			// @Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event)
			{
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK)
				{
					move_flag = false;
					selectedFlag = true;
				}
				return false;
			}
		});
		move_dialog.show();
	}

	// add 2010-06-11
	public void wordsMoveToSomeTable()
	{
		if (str_move.equals("manage_words"))
		{
			str_move = "newwords_table";
		}
		else if (str_move.equals("manage_dict"))
		{
			str_move = "words_table";
		}

		words_cursor = words_database.selectSomeTable(str_move);
		startManagingCursor(words_cursor);
		count = words_cursor.getCount();
		for (int x = 0; x < count; x++)
		{
			words_cursor.moveToPosition(x);
			if (str_drawwords.equals(words_cursor.getString(2)))
			{
				jump_repeat_words = 1;
			}
		}
		if (jump_repeat_words != 1)
		{
			getCurrentSystemDate();
			words_database.getString(String.valueOf(newwords_year),
					String.valueOf(newwords_month),
					String.valueOf(newwords_day));
			if (whichTableId == 0)
			{
				group_cursor = words_database.query();
				words_database.insertOnTable(str_move, str_drawwords,
						str_drawphonetic, str_drawexplain, m_sb.toString(),
						"0", "0");
			}
			else if (whichTableId == 1)
			{
				group_cursor = words_database.queryDict();
				words_database.insertOnDictTable(str_move, str_drawwords,
						byte_drawphonetic, byte_drawexplain, m_sb.toString(),
						"0", "0");
			}
			startManagingCursor(group_cursor);
		}
		jump_repeat_words = 0;
	}

	public void allMoveFunc()
	{
		String getwords, gettime, getimage;
		String getyear, getmonth, getday;
		// String getphonetic = null;
		byte[] getphonetic = null;
		String getexplain = null;
		byte[] move_dict_phonetic = null;
		byte[] move_dict_explain = null;
		int yy = 0;
		int moveDayId = 0;
		words_cursor = words_database.selectSomeTable(table_str);
		int getNum = words_cursor.getCount();
		for (int i = 0; i < getNum; i++)
		{
			words_cursor = words_database.selectSomeTable(table_str);
			startManagingCursor(words_cursor);
			words_cursor.moveToPosition(i);
			yy = words_cursor.getInt(words_cursor
					.getColumnIndex(MyDatabase.FIELD_IMAGE_NUM));
			getwords = words_cursor.getString(2);
			if (whichTableId == 0)
			{
				// getphonetic = words_cursor.getString(3);
				getphonetic = words_cursor.getBlob(3);
				getexplain = words_cursor.getString(4);
			}
			else if (whichTableId == 1)
			{
				move_dict_phonetic = words_cursor.getBlob(words_cursor
						.getColumnIndex(MyDatabase.FIELD_BYTE_EXPLAIN));
				move_dict_explain = words_cursor.getBlob(words_cursor
						.getColumnIndex(MyDatabase.FIELD_BITMAP));
			}
			gettime = words_cursor.getString(5);
			getimage = words_cursor.getString(12);

			getyear = words_cursor.getString(words_cursor
					.getColumnIndex(words_database.FIELD_YEAR));
			getmonth = words_cursor.getString(words_cursor
					.getColumnIndex(words_database.FIELD_MONTH));
			getday = words_cursor.getString(words_cursor
					.getColumnIndex(words_database.FIELD_DAY));
			words_database.getString(getyear, getmonth, getday);
			// ===
			if (str_move.equals("manage_words"))
			{
				str_move = "newwords_table";
			}
			else if (str_move.equals("manage_dict"))
			{
				str_move = "words_table";
			}
			words_cursor = words_database.selectSomeTable(str_move);
			startManagingCursor(words_cursor);
			count = words_cursor.getCount();
			for (int x = 0; x < count; x++)
			{
				words_cursor.moveToPosition(x);
				if (getwords.equals(words_cursor.getString(2)))
				{
					jump_repeat_words = 1;
				}
			}
			if (jump_repeat_words != 1)
			{
				switch (saveOrderNum[0])
				{
				case 0:
					if (String.valueOf(saveOrderNum[1] - 1).equals(
							String.valueOf(yy))
							|| saveOrderNum[1] == 0)
					{
						if (whichTableId == 0)
						{
							words_database.insertOnTable(str_move, getwords,
									getphonetic, getexplain, gettime, getimage,
									"0");
						}
						else if (whichTableId == 1)
						{
							words_database.insertOnDictTable(str_move,
									getwords, move_dict_phonetic,
									move_dict_explain, gettime, getimage, "0");
						}
					}
					break;
				case 1:
					words_cursor = words_database.selectSomeTable(table_str);
					startManagingCursor(words_cursor);
					moveDayId = words_cursor.getInt(words_cursor
							.getColumnIndex(MyDatabase.FIELD_DAY));
					if ((String.valueOf(saveOrderNum[1] - 1).equals(
							String.valueOf(yy)) || saveOrderNum[1] == 0)
							&& String.valueOf(moveDayId).equals(
									String.valueOf(newwords_day)))
					{
						if (whichTableId == 0)
						{
							words_database.insertOnTable(str_move, getwords,
									getphonetic, getexplain, gettime, getimage,
									"0");
						}
						else if (whichTableId == 1)
						{
							words_database.insertOnDictTable(str_move,
									getwords, move_dict_phonetic,
									move_dict_explain, gettime, getimage, "0");
						}
					}
					break;
				case 2:
					words_cursor = words_database.selectSomeTable(table_str);
					startManagingCursor(words_cursor);
					moveDayId = words_cursor.getInt(words_cursor
							.getColumnIndex(MyDatabase.FIELD_DAY));
					if ((String.valueOf(saveOrderNum[1] - 1).equals(
							String.valueOf(yy)) || saveOrderNum[1] == 0)
							&& (moveDayId - 2) <= newwords_day
							&& moveDayId >= (newwords_day - 2))
					{
						if (whichTableId == 0)
						{
							words_database.insertOnTable(str_move, getwords,
									getphonetic, getexplain, gettime, getimage,
									"0");
						}
						else if (whichTableId == 1)
						{
							words_database.insertOnDictTable(str_move,
									getwords, move_dict_phonetic,
									move_dict_explain, gettime, getimage, "0");
						}
					}
					break;
				case 3:
					words_cursor = words_database.selectSomeTable(table_str);
					startManagingCursor(words_cursor);
					moveDayId = words_cursor.getInt(words_cursor
							.getColumnIndex(MyDatabase.FIELD_DAY));
					if ((String.valueOf(saveOrderNum[1] - 1).equals(
							String.valueOf(yy)) || saveOrderNum[1] == 0)
							&& (moveDayId - 6) <= newwords_day
							&& moveDayId >= (newwords_day - 6))
					{
						if (whichTableId == 0)
						{
							words_database.insertOnTable(str_move, getwords,
									getphonetic, getexplain, gettime, getimage,
									"0");
						}
						else if (whichTableId == 1)
						{
							words_database.insertOnDictTable(str_move,
									getwords, move_dict_phonetic,
									move_dict_explain, gettime, getimage, "0");
						}
					}
					break;
				}
			}
			jump_repeat_words = 0;
		}
	}

	public void showCurrentCount()
	{
		currentWords_count = words_cursor.getCount();
		wordsCount_text.setText("共计" + currentWords_count + "个生词");
	}

	public boolean isExistTable(String table_name)
	{
		boolean isequals = false;
		for (int i = 0; i < group_cursor.getCount(); i++)
		{
			group_cursor.moveToPosition(i);
			String str_temp = group_cursor.getString(group_cursor
					.getColumnIndex(words_database.FILED_GET_NEW_TABLE));
			if (table_name.equals(str_temp))
			{
				isequals = true;
			}
		}
		if (!isequals)
		{
			return false;
		}
		return true;
	}

	static public void manageCursor(Context context, Cursor cursor)
	{
		((Activity) context).startManagingCursor(cursor);
	}

	public void beginFunc()
	{
		setContentView(R.layout.begin);
		for (int id : TEST_BTN)
		{
			ImageButton btn = (ImageButton) findViewById(id);
			btn.setOnClickListener(this);
		}
	}

	// 准备
	// public void repareFunc(){
	// if (m_testview == null){
	// m_testview = new Mytestview(this);
	// }
	// setContentView(m_testview);
	// m_testview.loadBack(R.drawable.background);
	// m_testview.showBack();
	// for (int x = 0; x <5;x++){
	// m_testview.loadRect(repareResId[x],repareRect[x],x);
	// m_testview.showTest(0);
	// }
	// flag =true;
	// flag_nums =1;
	// }
	// 单词测试
	public void testFunc()
	{
		loadAllImage();
		m_testview.showChageBack();
		m_testview.showImage(0);
		m_testview.showImage(1);
		// 选项
		if (selected_subject == 0)
		{
			words_cursor.moveToPosition(0);
			word_str = words_cursor.getString(2);
			m_testview.showText(temp = 1, word_str);
			str_explain = words_cursor.getString(4);
			ranFunc(4);
		}
		else if (selected_subject == 1)
		{
			words_cursor.moveToPosition(0);
			word_str = words_cursor.getString(4);
			m_testview.showText(temp = 1, word_str);
			str_explain = words_cursor.getString(2);
			ranFunc(2);
		}

		flag_nums = 0;

		int getid = 0;
		for (int x = 0; x < words_cursor.getCount(); x++)
		{
			getid = m_adapter.getHashMap(x);
			words_database.update(getid, "", "", "");
			words_cursor.requery();
		}
		flag = true;
	}

	// 词典测试
	public void dictTestFunc()
	{
		loadAllImage();
		m_testview.showChageBack();
		m_testview.showImage(0);
		m_testview.showImage(1);

		words_cursor.moveToPosition(0);
		dict_str = words_cursor.getString(2);
		getexplainbyteAns = words_cursor.getBlob(words_cursor
				.getColumnIndex(words_database.FIELD_BITMAP));
		m_testview.showText(temp = 1, dict_str);

		dictRandFunc();
		flag_nums = 0;
		flag = true;

	}

	public void loadAllImage()
	{
		for (int x = 0; x < 2; x++)
		{
			m_testview.loadImage(resId[x], rect[x], x);
		}
	}

	public void getCurrentSystemDate()
	{
		m_sb = new StringBuffer();
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		this.newwords_year = year;
		this.newwords_month = month + 1;
		this.newwords_day = day;
		if (newwords_month > 10 && day > 10)
		{
			m_sb.append(year + "-");
			m_sb.append(newwords_month + "-");
		}
		else
		{
			if (newwords_month >= 10)
			{
				m_sb.append(year + "-");
			}
			else
			{
				m_sb.append(year + "-" + "0");
			}

			if (day >= 10)
			{
				m_sb.append(newwords_month + "-");
			}
			else
			{
				m_sb.append(newwords_month + "-" + "0");
			}
		}
		m_sb.append(day);
	}

	// 外面传递的参数
	public int getTransferData(MyDatabase m_data, int id, String words,
			byte[] phonetic, String explain)
	{
		this.words_database = m_data;
//		System.out.println(jump_repeat_words+"------------id--------"+id);
		if (id == 0)
		{
			words_cursor = words_database.selectSomeTable("newwords_table");
		}
//		System.out.println("-----words_cursor---------------"+words_cursor);
		startManagingCursor(words_cursor);
//		System.out.println("--------------------");

		count = words_cursor.getCount();
//		System.out.println("------count--------------"+count);

		for (int x = 0; x < count; x++)
		{
			words_cursor.moveToPosition(x);
//			System.out.println(words_cursor.getString(2)+"-------words------"+words.equals(words_cursor.getString(2))+"-------"+words);
			if (words.equals(words_cursor.getString(2)))
			{
//				System.out.println("00000000000000000000");
				jump_repeat_words = 1;
			}
		}
		
		getCurrentSystemDate();
//		System.out.println("--------------------");
		words_database.getString(String.valueOf(newwords_year),
				String.valueOf(newwords_month), String.valueOf(newwords_day));
//		System.out.println("-------words_database-------------"+words_database);

		if (words_cursor != null)
		{
			words_cursor.close();
		}
//		System.out.println(jump_repeat_words+"-------words_cursor-------------"+words_cursor);

		if (jump_repeat_words != 1)
		{
			if (id == 0)
			{
				
				words_database.insertOnTable("newwords_table", words, phonetic,
						explain, m_sb.toString(), getImageNum, "0");
//				System.out.println("单词插入成功------------");
			}

		}

		else
		{
			return 0;
		}
		jump_repeat_words = 0;

		return 1;
	}

	public void getByteData(byte[] phonetic, byte[] explain)
	{
		this.newwords_explain = explain;
		this.newwords_phonetic = phonetic;
	}

	public void showMenu()
	{
		menu_dialog = new Dialog(this);
		menu_dialog
				.requestWindowFeature(menu_dialog.getWindow().FEATURE_NO_TITLE);
		Window mWindow = menu_dialog.getWindow();
		WindowManager.LayoutParams lp = mWindow.getAttributes();
		lp.x = 260; // 新位置X坐标
		lp.y = -15; // 新位置Y坐标
		menu_dialog.onWindowAttributesChanged(lp);

		menu_dialog.setContentView(R.layout.testview);
		TextView dia_text_str = (TextView) menu_dialog
				.findViewById(R.id.dialog_text_1);
		dia_text_str.setText(menuInfo());
		Animation m_animation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
				Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
				0.0f);
		m_animation.setDuration(500);
		dia_text_str.startAnimation(m_animation);
		menu_dialog.show();
	}

	public String menuInfo()
	{
		String result = "";

		InputStream in = null;
		try
		{
			if (whichTableId == 0)
			{
				in = getResources().openRawResource(R.raw.words_instruction);
			}
			int lenght = in.available();
			byte[] buffer = new byte[lenght];
			in.read(buffer);
			result = EncodingUtils.getString(buffer, "gb2312");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	public void showDialog()
	{
		m_d = new Dialog(this, R.style.dialog);
		// m_d.requestWindowFeature(m_d.getWindow().FEATURE_NO_TITLE);
		m_d.setContentView(R.layout.dialog);
		dia_list = (ListView) m_d.findViewById(R.id.dialog_list_1);
		// m_d.setTitle("生词管理本");
		for (int i = 0; i < 4; i++)
		{
			dia_button[i] = (ImageButton) m_d.findViewById(DIALOGUE_BTN[i]);
			dia_button[i].setOnClickListener(this);
		}
		// add new
		if (whichTableId == 0)
		{
			group_cursor = words_database.query();
		}
		else if (whichTableId == 1)
		{
			group_cursor = words_database.queryDict();
		}
		startManagingCursor(group_cursor);
		mange_adapter = new Manageadapter(this, group_cursor, words_database);

		dia_list.setAdapter(mange_adapter);
		m_d.setOnKeyListener(new OnKeyListener()
		{

			// @Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event)
			{
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK)
				{
					isOnTableManage = false;
					newordsFlag = false;
				}
				return false;
			}
		});
		dia_list.setOnItemClickListener(new OnItemClickListener()
		{

			// //@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3)
			{
				// TODO Auto-generated method stub
				group_cursor.moveToPosition(position);
				table_id = group_cursor.getInt(0);
				// table_str = group_cursor.getString(2);
				temp_table = group_cursor.getString(2);
				Log.e("tableName", "===temp_table" + temp_table);
				current_book = group_cursor.getString(1);

				dia_button[0].setBackgroundResource(R.drawable.open_click);
				if (temp_table.equals("manage_words")
						|| temp_table.equals("manage_dict"))
				{
					dia_button[2]
							.setBackgroundResource(R.drawable.delete_table);
				}
				else
				{
					dia_button[2].setBackgroundResource(R.drawable.del_click);
				}
				isSlectedItem = true;
				set_two = true;
				mange_adapter.setBackground(position);
				mange_adapter.notifyDataSetInvalidated();
			}
		});
		m_d.show();
	}

	public void testDialog()
	{
		testresult_dialog = new Dialog(this);
		testresult_dialog
				.requestWindowFeature(testresult_dialog.getWindow().FEATURE_NO_TITLE);
		testresult_dialog.setContentView(R.layout.showresult);
		ImageButton button_result = (ImageButton) testresult_dialog
				.findViewById(R.id.look);
		showresult_test = (TextView) testresult_dialog
				.findViewById(R.id.showresutl_text);
		showresult_test.setText(getResources().getString(R.string.testofall)
				+ words_cursor.getCount()
				+ getResources().getString(R.string.subject) + "\n\t\t\t"
				+ getResources().getString(R.string.rightcount)
				+ String.valueOf(rightNum) + "\n\t\t\t"
				+ getResources().getString(R.string.wrongcount)
				+ String.valueOf(errorNum) + "\n\t\t\t"
				+ getResources().getString(R.string.notanswercount)
				+ String.valueOf(notAnswerNum));
		button_result.setOnClickListener(this);
		testresult_dialog.show();
	}

	public void openTable(String listTable)
	{
		words_cursor = words_database.selectSomeTable(listTable);
		startManagingCursor(words_cursor);
		newFunc();
	}

	public void newFunc()
	{
		m_adapter = new Myadapter(this, words_cursor, words_database,
				table_str, whichTableId);
		m_listview.setAdapter(m_adapter);
	}

	public void buildTableName()
	{
		newtable_dialog = new Dialog(this, R.style.dialog);
		newtable_dialog
				.requestWindowFeature(newtable_dialog.getWindow().FEATURE_NO_TITLE);
		newtable_dialog.setContentView(R.layout.edit_name);
		newtable_dialog.show();
		ImageButton button_okay = (ImageButton) newtable_dialog
				.findViewById(R.id.okay);
		ImageButton button_cancel = (ImageButton) newtable_dialog
				.findViewById(R.id.cancel);
		m_edit = (EditText) newtable_dialog.findViewById(R.id.edittext);
		m_edit.setHint("请输入");
		button_okay.setOnClickListener(this);
		button_cancel.setOnClickListener(this);
		newtable_dialog.setOnKeyListener(new OnKeyListener()
		{

			// @Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event)
			{
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK)
				{
					isSave = false;
					isOnTableManage = false;
					savenewwordsFlag = false;
				}
				return false;
			}
		});
	}

	// add new
	public void isDelete()
	{
		isSureDialog = new Dialog(this, R.style.dialog);
		isSureDialog
				.requestWindowFeature(isSureDialog.getWindow().FEATURE_NO_TITLE);
		isSureDialog.setContentView(R.layout.del);
		isSureDialog.show();
		ImageButton button_yes = (ImageButton) isSureDialog
				.findViewById(R.id.yes);
		ImageButton button_dismiss = (ImageButton) isSureDialog
				.findViewById(R.id.dismiss);
		button_yes.setOnClickListener(this);
		button_dismiss.setOnClickListener(this);
		isSureDialog.setOnKeyListener(new OnKeyListener()
		{

			// @Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event)
			{
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK)
				{
					selectedFlag = true;
					isOnTableManage = false;
					sel_one = true;
					select_item_flag = false;
					if (isSlectedItem == true || sel_one == true)
					{
						sel_one = false;
						if (isInitManage == true)
						{
							dia_button[2]
									.setBackgroundResource(R.drawable.del_click);
						}
						if (!isOpenTable)
						{
							if (whichTableId == 0)
							{
								table_str = "newwords_table";
							}
							else if (whichTableId == 1)
							{
								table_str = "words_table";
							}
						}
					}
				}
				return false;
			}
		});
		TextView m_text_del = (TextView) isSureDialog
				.findViewById(R.id.del_text);
		if (isOnTableManage == true)
		{
			m_text_del.setText("是否要删除 ?");
			return;
		}
		if (isDeleteAll == true)
		{
			m_text_del.setText("是否要全部删除 ?");
		}
		else
		{
			m_text_del.setText("是否要删除 ?");
		}
	}

	static public void showExplainFunc(Drawable drawable, Context context)
	{
		// explain_dialog = new Dialog(context);
		// explain_dialog.requestWindowFeature(explain_dialog.getWindow().FEATURE_NO_TITLE);
		// explain_dialog.setContentView(R.layout.explain);
		// explain_dialog.show();
		// TextView m_textView =
		// (TextView)explain_dialog.findViewById(R.id.explain_id);
		// m_textView.setBackgroundDrawable(drawable);

	}

	public static void searchFunc(Context context, String words)
	{
		Intent intent = new Intent();
		intent.setClassName("vanhon.dictionary",
				"vanhon.dictionary.DictionaryQuery");
		intent.putExtra("searchWord", words);
		intent.putExtra("enterType", 0);
		context.startActivity(intent);
	}

	public void sortTime(int item)
	{
		getCurrentSystemDate();
		switch (item)
		{
		case 0:
			data_num = 0;
			delFlag = true;
			break;
		case 1:
			data_num = 1;
			isAdd = true;
			delFlag = true;
			break;
		case 2:
			data_num = 2;
			isAdd = true;
			delFlag = true;
			break;
		case 3:
			data_num = 3;
			isAdd = true;
			delFlag = true;
			break;
		}
		if (isAdd == false || words_cursor.getCount() <= 0)
		{
			return;
		}
		initFlag = true;
		getreturn = false;
		if (!allDifficultly)
		{
			Cursor temp_cursor = words_database.partQuery(table_str,
					String.valueOf(newwords_day), data_num, saveOrderNum[1],
					String.valueOf(newwords_year),
					String.valueOf(newwords_month));
			startManagingCursor(temp_cursor);
			m_adapter.set(temp_cursor);
		}
		else
		{
			Cursor diffcult_cursor = words_database.getQuery(table_str,
					data_num - 1, saveOrderNum[0],
					String.valueOf(newwords_day),
					String.valueOf(newwords_year),
					String.valueOf(newwords_month));
			startManagingCursor(diffcult_cursor);
			m_adapter.set(diffcult_cursor);
		}
	}

	static public void getReturn()
	{
		getreturn = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onTouchEvent(android.view.MotionEvent)
	 */
	// //@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		// TODO Auto-generated method stub
		int test_x = (int) event.getX();
		int test_y = (int) event.getY();
		switch (event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
		
			explainShow.setVisibility(View.GONE);
			
			if (flag == true)
			{
				if (flag_nums == 0)
				{
					int getReturnNum = 0;

					if (whichTableId == 1)
					{// 词典测试界面
						if (-1 != (getReturnNum = (m_testview.selectedItem(
								test_x, test_y))))
						{
							temp_flag = true;
							m_testview.showChageBack();
							m_testview.showText(temp, word_str);
							m_testview.showImage(0);
							m_testview.showImage(1);
							showDictItem();
						}
					}
					else if (whichTableId == 0)
					{// 单词测试界面
						getReturnNum = m_testview.getchoice(test_x, test_y);
						if (getReturnNum >= 0 && getReturnNum < 4)
						{// 在测试中 。。。
							temp_flag = true;
							m_testview.showChageBack();
							m_testview.showText(temp, word_str);
							m_testview.showImage(0);
							m_testview.showImage(1);
							showItem();
						}
					}

					// 选中某个选项
					switch (getReturnNum)
					{
					case 0:
						m_testview.selectAnswer(0, whichTableId);
						select_right = 1;
						get_answer = getResources().getString(R.string.a_str);
						if (rand == 0)
						{
							select_right = 0;
						}
						break;
					case 1:
						m_testview.selectAnswer(1, whichTableId);
						select_right = 1;
						get_answer = getResources().getString(R.string.b_str);
						if (rand == 1)
						{
							select_right = 0;
						}
						break;
					case 2:
						m_testview.selectAnswer(2, whichTableId);
						select_right = 1;
						get_answer = getResources().getString(R.string.c_str);
						if (rand == 2)
						{
							select_right = 0;
						}
						break;
					case 3:
						m_testview.selectAnswer(3, whichTableId);
						select_right = 1;
						get_answer = getResources().getString(R.string.d_str);
						if (rand == 3)
						{
							select_right = 0;
						}
						break;
					default:
						get_answer = "";
						break;

					}

					if (m_testview.getInt(test_x, test_y) == 0)
					{// 跳过
						judgeCount();
					}
					else if (m_testview.getInt(test_x, test_y) == 1)
					{// 查看结果
						if (select_right == 0)
						{// 正确数目
							rightNum++;
							get_result = "correct";
							get_right_answer = "";
						}
						else
						{
							if (temp_flag == true)
							{// 错误数目
								errorNum++;
								get_result = "wrong";
								words_cursor.moveToPosition(temp - 1);
								if (selected_subject == 0)
								{
									get_right_answer = words_cursor
											.getString(4);
								}
								else if (selected_subject == 1)
								{
									get_right_answer = words_cursor
											.getString(2);
								}
							}
						}
						if (temp_flag == false)
						{// 未答数目
							get_result = "not answer";
							notAnswerNum++;
							words_cursor.moveToPosition(temp - 1);
							if (selected_subject == 0)
							{
								if (whichTableId == 0)
								{
									get_right_answer = words_cursor
											.getString(4);
								}
								else if (whichTableId == 1)
								{
									getDictAnswer = words_cursor
											.getBlob(words_cursor
													.getColumnIndex(words_database.FIELD_BITMAP));
								}
							}
							else if (selected_subject == 1)
							{
								get_right_answer = words_cursor.getString(2);
							}
						}
						temp_flag = false;
						select_right = 1;
						int getid = m_adapter.getHashMap(temp - 1);
						if (whichTableId == 0)
						{
							words_database.update(getid, get_answer,
									get_result, get_right_answer);
						}
						words_cursor.requery();
						testDialog();
					}
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			break;
		}
		return true;
	}

	// 判断测试情况
	public void judgeCount()
	{
		if (temp + 1 > words_cursor.getCount())
		{
			return;
		}
		if (select_right == 0)
		{// 正确数目
			rightNum++;
			get_result = "correct";
			get_right_answer = "";
		}
		else
		{
			if (temp_flag == true)
			{// 错误数目
				errorNum++;
				get_result = "wrong";
				words_cursor.moveToPosition(temp - 1);
				if (selected_subject == 0)
				{
					if (whichTableId == 0)
					{
						get_right_answer = words_cursor.getString(4);
					}
					else if (whichTableId == 1)
					{
						getDictAnswer = words_cursor.getBlob(words_cursor
								.getColumnIndex(words_database.FIELD_BITMAP));
					}
				}
				else if (selected_subject == 1)
				{
					get_right_answer = words_cursor.getString(2);
				}
			}
		}
		if (temp_flag == false)
		{// 未答数目
			get_result = "not answer";
			notAnswerNum++;
			words_cursor.moveToPosition(temp - 1);
			if (selected_subject == 0)
			{
				if (whichTableId == 0)
				{
					get_right_answer = words_cursor.getString(4);
				}
				else if (whichTableId == 1)
				{
					getDictAnswer = words_cursor.getBlob(words_cursor
							.getColumnIndex(words_database.FIELD_BITMAP));
				}
			}
			else if (selected_subject == 1)
			{
				get_right_answer = words_cursor.getString(2);
			}
		}
		// 刷新数据
		temp_flag = false;
		select_right = 1;
		m_testview.showChageBack();
		m_testview.showImage(0);
		m_testview.showImage(1);
		int getid = m_adapter.getHashMap(temp - 1);
		if (whichTableId == 0)
		{
			words_database.update(getid, get_answer, get_result,
					get_right_answer);
		}
		else if (whichTableId == 1)
		{
			words_database.updateOndict(getid, get_answer, get_result,
					getDictAnswer);
		}
		words_cursor.requery();
		temp++;
		if (selected_subject == 0)
		{
			if (whichTableId == 0)
			{
				showTiMu(2, 4);
				ranFunc(4);
			}
			else if (whichTableId == 1)
			{
				showNextDict();
				dictRandFunc();

			}
		}
		else if (selected_subject == 1)
		{
			showTiMu(4, 2);
			ranFunc(2);
		}
	}

	public void showTiMu(int x, int y)
	{
		words_cursor.moveToPosition(temp - 1);
		word_str = words_cursor.getString(x);
		str_explain = words_cursor.getString(y);
		m_testview.showText(temp, word_str);
	}

	public void showNextDict()
	{
		words_cursor.moveToPosition(temp - 1);
		String dict_str = words_cursor.getString(2);
		m_testview.showText(temp, dict_str);
		getexplainbyteAns = words_cursor.getBlob(words_cursor
				.getColumnIndex(words_database.FIELD_BITMAP));
	}

	// dictionary
	public void dictRandFunc()
	{
		do
		{
			for (int j = 0; j < 3; j++)
			{
				int random_dic = m_random.nextInt(words_cursor.getCount());
				words_cursor.moveToPosition(random_dic);
				if (j == 0)
				{
					getexplain_byte_one = words_cursor.getBlob(words_cursor
							.getColumnIndex(words_database.FIELD_BITMAP));
				}
				else if (j == 1)
				{
					getexplain_byte_two = words_cursor.getBlob(words_cursor
							.getColumnIndex(words_database.FIELD_BITMAP));
				}
				else if (j == 2)
				{
					getexplain_byte_three = words_cursor.getBlob(words_cursor
							.getColumnIndex(words_database.FIELD_BITMAP));
				}
			}
		}
		while (equals(getexplain_byte_one, getexplainbyteAns)
				|| equals(getexplain_byte_two, getexplainbyteAns)
				|| equals(getexplain_byte_three, getexplainbyteAns)
				|| equals(getexplain_byte_one, getexplain_byte_two)
				|| equals(getexplain_byte_one, getexplain_byte_three)
				|| equals(getexplain_byte_two, getexplain_byte_three));
		getDictCount();
	}

	public void getDictCount()
	{
		int randNum = m_random.nextInt(4);
		switch (randNum)
		{
		case 0:
			byte_a = getexplainbyteAns;
			byte_b = getexplain_byte_one;
			byte_c = getexplain_byte_two;
			byte_d = getexplain_byte_three;
			break;
		case 1:
			byte_b = getexplainbyteAns;
			byte_a = getexplain_byte_one;
			byte_c = getexplain_byte_two;
			byte_d = getexplain_byte_three;
			break;
		case 2:
			byte_c = getexplainbyteAns;
			byte_a = getexplain_byte_one;
			byte_b = getexplain_byte_two;
			byte_d = getexplain_byte_three;
			break;
		case 3:
			byte_d = getexplainbyteAns;
			byte_a = getexplain_byte_one;
			byte_b = getexplain_byte_two;
			byte_c = getexplain_byte_three;
			break;
		}
		showDictItem();
	}

	// 显示词典选项
	public void showDictItem()
	{
		m_testview.drawImage(byte_a, dictItem_rect[0], 0);
		m_testview.drawImage(byte_b, dictItem_rect[1], 1);
		m_testview.drawImage(byte_c, dictItem_rect[2], 2);
		m_testview.drawImage(byte_d, dictItem_rect[3], 3);
	}

	static public boolean equals(byte[] a1, byte[] a2)
	{
		if (a1 == null || a2 == null)
		{
			return (a1 == a2);
		}
		int nlength = a1.length;
		if (nlength != a2.length)
		{
			return false;
		}
		for (int i = 0; i < nlength; i++)
		{
			if (a1[i] != a2[i])
			{
				return false;
			}
		}
		return true;
	}

	public void ranFunc(int column)
	{
		do
		{
			for (int j = 0; j < 3; j++)
			{
				int random = m_random.nextInt(words_cursor.getCount());
				words_cursor.moveToPosition(random);
				str_buf[j] = words_cursor.getString(column);
			}
		}
		while (str_buf[0].equals(str_explain) || str_buf[1].equals(str_explain)
				|| str_buf[2].equals(str_explain)
				|| str_buf[0].equals(str_buf[1])
				|| str_buf[0].equals(str_buf[2])
				|| str_buf[1].equals(str_buf[2]));

		getRanNum();
	}

	public void getRanNum()
	{
		rand = m_random.nextInt(4);
		switch (rand)
		{
		case 0:
			a = str_explain;
			b = str_buf[0];
			c = str_buf[1];
			d = str_buf[2];
			break;
		case 1:
			b = str_explain;
			a = str_buf[0];
			c = str_buf[1];
			d = str_buf[2];
			break;
		case 2:
			c = str_explain;
			a = str_buf[0];
			b = str_buf[1];
			d = str_buf[2];
			break;
		case 3:
			d = str_explain;
			a = str_buf[0];
			b = str_buf[1];
			c = str_buf[2];
			break;
		}
		showItem();
	}

	// 显示选项
	public void showItem()
	{
		m_testview.showTextOne(a, rect_select[0]);
		m_testview.showTextTwo(b, rect_select[1]);
		m_testview.showTextThree(c, rect_select[2]);
		m_testview.showTextFour(d, rect_select[3]);
	}

	public void showresult()
	{
		result_list = (ListView) findViewById(R.id.list_result);
		m_result_base = new Baseresult(this, array_list, whichTableId);
		m_b1 = (ImageButton) findViewById(R.id.b1);
		m_b2 = (ImageButton) findViewById(R.id.b2);
		m_b1.setOnClickListener(this);
		m_b2.setOnClickListener(this);

		int getNum = words_cursor.getCount();
		for (int x = 0; x < getNum; x++)
		{
			words_cursor.moveToPosition(x);
			if (selected_subject == 0)
			{
				word_str_subject = words_cursor.getString(2);
			}
			else if (selected_subject == 1)
			{
				word_str_subject = words_cursor.getString(4);
			}
			word_str_answer = words_cursor.getString(6);
			word_str_result = words_cursor.getString(7);
			if (whichTableId == 0)
			{
				word_str_rightanswer = words_cursor.getString(8);
			}
			else if (whichTableId == 1)
			{
				dict_str_rightanswer = words_cursor.getBlob(words_cursor
						.getColumnIndex(words_database.FIELD_BITMAP));
			}
			HashMap<Object, String> map = new HashMap<Object, String>();
			map.put("digitals", String.valueOf(x + 1));
			map.put("words", word_str_subject);
			map.put("answer", word_str_answer);
			map.put("result", word_str_result);
			if (whichTableId == 0)
			{
				map.put("right", word_str_rightanswer);
			}
			else if (whichTableId == 1)
			{
			}
			array_list.add(map);
		}
		result_list.setAdapter(m_result_base);

	}

	//
	public void selectAllItem()
	{
		isDeleteAll = true;
		selectedFlag = true;
		selecetedOkay = true;
		move_flag = true;
		// Drawable m_back =
		// getResources().getDrawable(R.drawable.selected_item);
		// m_listview.setBackgroundDrawable(m_back);
		m_adapter.setAllItem();
		m_adapter.notifyDataSetInvalidated();

	}

	//
	public void delAllItem()
	{
		for (int x = 0; x < words_cursor.getCount(); x++)
		{
			words_cursor.moveToPosition(x);
			id = words_cursor.getInt(0);
			words_database.delOnTable(table_str, id);
		}
		words_cursor = words_database.selectSomeTable(table_str);
		startManagingCursor(words_cursor);
		m_adapter.set(words_cursor);
	}

	public void delAccordingtodifficult()
	{
		int timeId = 0;
		int yy = 0;
		int compareday = 0;
		Cursor diffcult_cursor = null;
		for (int x = 0; x < words_cursor.getCount(); x++)
		{
			words_cursor.moveToPosition(x);
			yy = words_cursor.getInt(words_cursor
					.getColumnIndex(MyDatabase.FIELD_IMAGE_NUM));
			switch (saveOrderNum[0])
			{
			case 0:
				if (String.valueOf(saveOrderNum[1] - 1).equals(
						String.valueOf(yy))
						|| saveOrderNum[1] == 0)
				{
					timeId = words_cursor.getInt(0);
					words_database.delOnTable(table_str, timeId);
				}
				diffcult_cursor = words_database.getQuery(table_str,
						saveOrderNum[1] - 1, 0, String.valueOf(newwords_day),
						String.valueOf(newwords_year),
						String.valueOf(newwords_month));
				break;
			case 1:
				compareday = words_cursor.getInt(words_cursor
						.getColumnIndex(MyDatabase.FIELD_DAY));
				if ((String.valueOf(saveOrderNum[1] - 1).equals(
						String.valueOf(yy)) || saveOrderNum[1] == 0)
						&& String.valueOf(compareday).equals(
								String.valueOf(newwords_day)))
				{
					timeId = words_cursor.getInt(0);
					words_database.delOnTable(table_str, timeId);
				}

				diffcult_cursor = words_database.getQuery(table_str,
						saveOrderNum[1] - 1, 1, String.valueOf(newwords_day),
						String.valueOf(newwords_year),
						String.valueOf(newwords_month));
				break;
			case 2:
				compareday = words_cursor.getInt(words_cursor
						.getColumnIndex(MyDatabase.FIELD_DAY));
				if ((String.valueOf(saveOrderNum[1] - 1).equals(
						String.valueOf(yy)) || saveOrderNum[1] == 0)
						&& (compareday - 2) <= newwords_day
						&& compareday >= (newwords_day - 2))
				{
					timeId = words_cursor.getInt(0);
					words_database.delOnTable(table_str, timeId);
				}
				diffcult_cursor = words_database.getQuery(table_str,
						saveOrderNum[1] - 1, 2, String.valueOf(newwords_day),
						String.valueOf(newwords_year),
						String.valueOf(newwords_month));
				break;
			case 3:
				compareday = words_cursor.getInt(words_cursor
						.getColumnIndex(MyDatabase.FIELD_DAY));
				if ((String.valueOf(saveOrderNum[1] - 1).equals(
						String.valueOf(yy)) || saveOrderNum[1] == 0)
						&& (compareday - 6) <= newwords_day
						&& compareday >= (newwords_day - 6))
				{
					timeId = words_cursor.getInt(0);
					words_database.delOnTable(table_str, timeId);
				}
				diffcult_cursor = words_database.getQuery(table_str,
						saveOrderNum[1] - 1, 3, String.valueOf(newwords_day),
						String.valueOf(newwords_year),
						String.valueOf(newwords_month));
				break;
			}
			startManagingCursor(diffcult_cursor);
			m_adapter.set(diffcult_cursor);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	// //@Override
	private void exit()
	{
		if (words_cursor != null)
		{
			words_cursor.close();
		}
		if (group_cursor != null)
		{
			group_cursor.close();
		}
		if (tablecount_cursor != null)
		{
			tablecount_cursor.close();
		}
		if (m_adapter != null && m_adapter.m_cursor != null)
		{
			m_adapter.m_cursor.close();
		}
		if (mange_adapter != null && mange_adapter.manage_cursor != null)
		{
			mange_adapter.manage_cursor.close();
		}
		if (words_database != null)
		{
			words_database.close();
		}
	}
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		exit();
//		System.gc();
//		System.exit(0);
		finish();
		super.onDestroy();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		// TODO Auto-generated method stub
		return mGestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY)
	{
//		System.out.println("----------onFling---------");
		if(e1.getX()-e2.getX()>120)
		{
			Intent intent = new Intent(this, AudioActivity.class);
			intent.putExtra("CurrentListActivity", "CurrentListActivity");
			startActivity(intent);
			overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
			
		}
		//向右手势
		else if(e2.getX()-e1.getX()>120)
		{
			Intent intent = new Intent(this,DictionaryActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	// //@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event)
//	{
//		// TODO Auto-generated method stub
//		if (keyCode == KeyEvent.KEYCODE_BACK)
//		{
//			finish();
//		}
//		return super.onKeyDown(keyCode, event);
//	}
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(keyCode == event.KEYCODE_BACK)
		{
			Intent intent = new Intent(this,FileListActivity.class);
			startActivity(intent);
			exit();
			this.finish();
			super.onDestroy();

		}
		return super.onKeyDown(keyCode, event);
	}

}
