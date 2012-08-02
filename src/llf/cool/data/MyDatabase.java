package llf.cool.data;

import java.io.ByteArrayOutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.os.Parcelable.Creator;
import android.util.Log;

public class MyDatabase extends SQLiteOpenHelper {
	private final static String DATABASE_NAME = "newwords";
	private final static String TABLE_NAME = "newwords_table";
	private final static String TABLE_MANAGE = "newwords_manage";
	private final static int DATABASE_VERSION = 2;
	private final static String WORDS_TABLE = "words_table";
	private final static String TABLE_DICT ="newwords_dict_manage";
	
	public final static String FILED_GROUP = "table_group";
	public final static String FILED_GET_NEW_TABLE ="new_table";
	public final static String FILED_GET_TABLE_NUM= "table_num";
	public final static String FILED_ID = "_id";
	public final static String FILED_CURSOR_ID = "newwords_nums";
	public final static String FILED_WORDS = "newwords_text";
	public final static String FILED_PHONETICS = "newwords_phonetic";
	public final static String FILED_EXPLAIN = "newwords_explain";
	public final static String FILED_TIME ="newwords_time";
	
	public final static String FIELD_YOUR_ANSWER = "result_answer";
	public final static String FIELD_RESULT = "result_last_answer";
	public final static String FIELD_RIGHT_ANSWER ="result_right_answer";
	// 存储日期
	public final static String FIELD_YEAR ="newwords_year";
	public final static String FIELD_MONTH = "newwords_month";
	public final static String FIELD_DAY = "newwords_day";
	//不断添加表
	public final static String FIELD_TABLE_COUNT ="newwords_table_count";
	public final static String FIELD_TABLE_SUM = "newwords_sum";
	
	public final static String FIELD_TABLE_COUNT_DICT ="newwords_table_count_dict";
	
	public final static String FIELD_IMAGE_NUM = "newwords_image";
	public final static String FIELD_BITMAP = "newwords_bitmap";
	public final static String FIELD_BYTE_EXPLAIN = "newwords_byte_explain";
	
	public final static String FIELD_GET_PART_RECORD = "part_record";
	private final static int THREE_DAY = 3;
	private final static int WEEKLY_NUM = 7;
	private final static int NUM_ZERO = 0;
	private final static int NUM_ONE = 1;
	private final static int NUM_TWO = 2;
	private final static int NUM_THREE =3;
	
	private String data_year;
	private String data_month;
	private String data_day;
	
	
	public MyDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

//	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "CREATE table " + TABLE_NAME + 
									" (" + FILED_ID+ " INTEGER primary key autoincrement, "+ 
											FILED_CURSOR_ID + " INTEGER, "+
											FILED_WORDS + " text, " + 
											FILED_PHONETICS + " blob, " + 
											FILED_EXPLAIN+" text, "+
											FILED_TIME +" text, " +  
											FIELD_YOUR_ANSWER+" text, "+FIELD_RESULT +" text, "+FIELD_RIGHT_ANSWER+ " text, "+
											FIELD_YEAR+" INTEGER, "+FIELD_MONTH+" INTEGER, "+FIELD_DAY+" INTEGER, "
											+FIELD_IMAGE_NUM+" INTEGER, "+FIELD_BITMAP+" blob, "+FIELD_BYTE_EXPLAIN+" blob, "
											+FIELD_GET_PART_RECORD + " INTEGER)";
		db.execSQL(sql);
		
		StringBuilder str_b = new StringBuilder();
		str_b.append("CREATE table ").append(TABLE_MANAGE).append(" (")
				.append(FILED_ID + " INTEGER primary key autoincrement, ")
					.append(FILED_GROUP + " text, ")
					.append(FILED_GET_NEW_TABLE + " text)");
		db.execSQL(str_b.toString());	
		
		String sql_words =  "CREATE table " + WORDS_TABLE + 
								" (" + FILED_ID+ " INTEGER primary key autoincrement, "+ 
								FILED_CURSOR_ID + " INTEGER, "+
								FILED_WORDS + " text, " + 
								FILED_PHONETICS + " blob, " + 
								FILED_EXPLAIN+" text, " + FILED_TIME +" text, " +  
								FIELD_YOUR_ANSWER+" text, "+FIELD_RESULT +" text, "+FIELD_RIGHT_ANSWER+
								" text, "+FIELD_YEAR+" INTEGER, "+FIELD_MONTH+" INTEGER, "+FIELD_DAY+" INTEGER, "
								+FIELD_IMAGE_NUM+" INTEGER, "+FIELD_BITMAP+" blob, "+FIELD_BYTE_EXPLAIN+" blob, "
								+FIELD_GET_PART_RECORD + " INTEGER)";
		db.execSQL(sql_words);
		
		StringBuilder str_dict = new StringBuilder();
		str_dict.append("CREATE table ").append(TABLE_DICT).append(" (")
				.append(FILED_ID + " INTEGER primary key autoincrement, ")
						.append(FILED_GROUP + " text, ")
					.append(FILED_GET_NEW_TABLE + " text)");
		db.execSQL(str_dict.toString());
		
		StringBuilder allTable_count = new StringBuilder();
		allTable_count.append("CREATE table ").append(FIELD_TABLE_COUNT).append(" (")
				.append(FILED_ID + " INTEGER primary key autoincrement, ")
						.append(FIELD_TABLE_SUM + " INTEGER)");
		db.execSQL(allTable_count.toString());
		
		StringBuilder allTable_count_dict = new StringBuilder();
		allTable_count_dict.append("CREATE table ").append(FIELD_TABLE_COUNT_DICT).append(" (")
				.append(FILED_ID + " INTEGER primary key autoincrement, ")
						.append(FIELD_TABLE_SUM + " INTEGER)");
		db.execSQL(allTable_count_dict.toString());
	}

//	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String sql = "drop table if exists " + TABLE_NAME;
		db.execSQL(sql);
		onCreate(db);
	}
	
	//对某个表进行条件查询
	public Cursor partQuery(String table_name ,String current_time,int id,int degree ,String year ,String month){
		String column = FILED_ID+","+FILED_CURSOR_ID+","+FILED_WORDS+","+FILED_PHONETICS+","+FILED_EXPLAIN+","+FILED_TIME +","+
			FIELD_YOUR_ANSWER+","+FIELD_RESULT+","+FIELD_RIGHT_ANSWER
			+","+FIELD_YEAR+","+FIELD_MONTH+","+FIELD_DAY+","+FIELD_IMAGE_NUM+","+FIELD_BITMAP+","+FIELD_BYTE_EXPLAIN+","+FIELD_GET_PART_RECORD;
		String sql = null;
		String sql_sentence = "select " +column + " from "+ table_name;
		Cursor m_cursor = null;
		SQLiteDatabase db = this.getReadableDatabase();
		int range = Integer.valueOf(current_time).intValue();
		String range_str = null;
		String range_weekly = null;
		//add 
		String str_getDegree = String.valueOf(degree-1);
//		range_str = String.valueOf(range - THREE_DAY);
		range_str = String.valueOf((range - THREE_DAY)+1);
		range_weekly = String.valueOf((range - WEEKLY_NUM)+1);
		if (id == NUM_ZERO){
			if (str_getDegree.equals("-1")){
				sql = sql_sentence;
			}else{
				sql = sql_sentence +" where " +FIELD_IMAGE_NUM + " = "+ str_getDegree;
			}
		}else if (id == NUM_ONE){
			if (str_getDegree.equals("-1")){
				sql =sql_sentence +" where " + FIELD_DAY + " = "+ current_time + " and "+ FIELD_YEAR + " = "+ year + " and "+ FIELD_MONTH + " = "+ month;;
			}else{
				sql = sql_sentence+" where " + FIELD_DAY + " = "+ current_time + " and "+ FIELD_IMAGE_NUM + " = "+ str_getDegree + " and "+ FIELD_YEAR + " = "+ year + " and "+ FIELD_MONTH + " = "+ month;
			}
		}else if (id == NUM_TWO){
			if (str_getDegree.equals("-1")){
				sql = sql_sentence+" where " + FIELD_DAY + " <= "+ current_time+ " and "+ FIELD_DAY +" >= "+ range_str + " and "+ FIELD_YEAR + " = "+ year + " and "+ FIELD_MONTH + " = "+ month;;
			}else{
				sql = sql_sentence +" where " + FIELD_DAY + " <= "+ current_time+ " and "+ FIELD_DAY +" >= "+ range_str+ " and "+ FIELD_IMAGE_NUM + " = "+ str_getDegree + " and "+ FIELD_YEAR + " = "+ year + " and "+ FIELD_MONTH + " = "+ month;;
			}
		}else if(id == NUM_THREE){
			if (str_getDegree.equals("-1")){
				sql =sql_sentence+" where " + FIELD_DAY + " <= "+ current_time+ " and "+ FIELD_DAY +" >= "+ range_weekly + " and "+ FIELD_YEAR + " = "+ year + " and "+ FIELD_MONTH + " = "+ month;;
			}else{
				sql = sql_sentence+" where " + FIELD_DAY + " <= "+ current_time+ " and "+ FIELD_DAY +" >= "+ range_weekly + " and "+ FIELD_IMAGE_NUM + " = "+ str_getDegree + " and "+ FIELD_YEAR + " = "+ year + " and "+ FIELD_MONTH + " = "+ month;;
			}
		}
		if (sql != null){
			sql = sql +" order by " + FILED_TIME;
			 m_cursor = db.rawQuery(sql, null);
		}
		if (m_cursor !=  null){
			m_cursor.moveToFirst();
		}
		return m_cursor;
	}
	
	//指向某一个表
	public Cursor selectSomeTable(String table){
		SQLiteDatabase db = this.getReadableDatabase();
		String []column = {FILED_ID,FILED_CURSOR_ID,FILED_WORDS,FILED_PHONETICS,FILED_EXPLAIN,FILED_TIME,FIELD_YOUR_ANSWER,FIELD_RESULT,FIELD_RIGHT_ANSWER
				,FIELD_YEAR,FIELD_MONTH,FIELD_DAY,FIELD_IMAGE_NUM,FIELD_BITMAP+","+FIELD_BYTE_EXPLAIN+","+FIELD_GET_PART_RECORD};
		Cursor my_cursor = db.query(table, column, null, null, null, null, FILED_TIME);
		if (my_cursor !=  null){
			my_cursor.moveToFirst();
		}
		return my_cursor;
	}
	
	
	// add new 
	public Cursor query(){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor m_cursor = db.query(TABLE_MANAGE, null, null, null, null, null, null);
		return m_cursor;
	}
	//add new 05-31
	public Cursor cursorTable(String str_name){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor m_cursor = db.query(str_name, null, null, null, null, null, null);
		return m_cursor;
	}
	public Cursor queryDict(){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor m_cursor = db.query(TABLE_DICT, null, null, null, null, null, null);
		return m_cursor;
	}
	//难易程度
	public Cursor getQuery(String table_name,int num,int getTime ,String current_time ,String year, String month){
		String column = FILED_ID+","+FILED_CURSOR_ID+","+FILED_WORDS+","+FILED_PHONETICS+","+FILED_EXPLAIN+","+FILED_TIME +","+
			FIELD_YOUR_ANSWER+","+FIELD_RESULT+","+FIELD_RIGHT_ANSWER
			+","+FIELD_YEAR+","+FIELD_MONTH+","+FIELD_DAY+","+FIELD_IMAGE_NUM+","+FIELD_BITMAP+","+FIELD_BYTE_EXPLAIN+","+FIELD_GET_PART_RECORD;;
		SQLiteDatabase db = this.getReadableDatabase();
		String diff_str = String.valueOf(num);
		String time_str = String.valueOf(getTime -1);
		int getTimeNum = getTime -1;
		String sql = null;
		if (num != -1){
			 if (getTimeNum == NUM_ZERO){
				 sql = "select " +column+" from "+ table_name +" where " + FIELD_IMAGE_NUM + " = "+ diff_str + " and "+ FIELD_DAY + " = "+ current_time + " and "+ FIELD_YEAR + " = "+ year + " and "+ FIELD_MONTH + " = "+ month;;
			}else if (getTimeNum == NUM_ONE){
				sql = "select " +column+" from "+ table_name +" where " + FIELD_IMAGE_NUM + " = "+ diff_str
						+ " and "+ FIELD_DAY + " <= "+ current_time+ " and "+ FIELD_DAY +" >= "+ String.valueOf((Integer.valueOf(current_time).intValue())-2)+
						" and "+ FIELD_YEAR + " = "+ year + " and "+ FIELD_MONTH + " = "+ month;;
			}else if (getTimeNum == NUM_TWO){
				sql = "select " +column+" from "+ table_name +" where " + FIELD_IMAGE_NUM + " = "+ diff_str
				+ " and "+ FIELD_DAY + " <= "+ current_time+ " and "+ FIELD_DAY +" >= "+ String.valueOf((Integer.valueOf(current_time).intValue())-6)
				+ " and "+ FIELD_YEAR + " = "+ year + " and "+ FIELD_MONTH + " = "+ month;
			}else if (getTimeNum == -1){
				sql = "select " +column+" from "+ table_name+" where " + FIELD_IMAGE_NUM+ " = "+ diff_str ;
			}
		}else {
			switch (getTimeNum)
			{
			case -1:
				sql = "select " +column+" from "+ table_name;
				break;
			case 0:
				 sql = "select " +column+" from "+ table_name +" where " + FIELD_DAY + " = "+ current_time + " and "+ FIELD_YEAR + " = "+ year + " and "+ FIELD_MONTH + " = "+ month;
				break;
			case 1:
				sql = "select " +column+" from "+ table_name +" where " +  FIELD_DAY + " <= "+ current_time+ " and "+ FIELD_DAY +" >= "+ String.valueOf((Integer.valueOf(current_time).intValue())-2)
				+ " and "+ FIELD_YEAR + " = "+ year + " and "+ FIELD_MONTH + " = "+ month;
				break;
			case 2:
				sql = "select " +column+" from "+ table_name +" where " +  FIELD_DAY + " <= "+ current_time+ " and "+ FIELD_DAY +" >= "+ String.valueOf((Integer.valueOf(current_time).intValue())-6)
				+ " and "+ FIELD_YEAR + " = "+ year + " and "+ FIELD_MONTH + " = "+ month;
				break;
				default:
					break;
			}
		}
		sql = sql +" order by " + FILED_TIME;
		Cursor m_cursor = db.rawQuery(sql, null);
		if (m_cursor !=  null){
			m_cursor.moveToFirst();
		}
		
		return m_cursor;
	}
	//新建新表名
	public long insertTable(String table_name,String str_table ){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(FILED_GROUP, table_name);
		cv.put(FILED_GET_NEW_TABLE, str_table);
		long row = db.insert(TABLE_MANAGE, null,cv);
		return row;
	}
	// add new
	public long insertTableOnDic(String table_name,String str_table){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(FILED_GROUP, table_name);
		cv.put(FILED_GET_NEW_TABLE, str_table);
		long row = db.insert(TABLE_DICT, null,cv);
		return row;
	}
	public long insertTableCount(String tablename,int table_num){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(FIELD_TABLE_SUM, table_num);
		long row = db.insert(tablename, null,cv);
		return row;
	}
	public void deleteListTable(int id){
		SQLiteDatabase db = this.getWritableDatabase();
		String where = FILED_ID + " = ?";
		String[] whereValue = { Integer.toString(id) };
		db.delete(TABLE_MANAGE, where, whereValue);
	}
	//add new 
	public void delListTableOnDic(int id){
		SQLiteDatabase db = this.getWritableDatabase();
		String where = FILED_ID + " = ?";
		String[] whereValue = { Integer.toString(id) };
		db.delete(TABLE_DICT, where, whereValue);
	}
	public void delete(int id){
		SQLiteDatabase db = this.getWritableDatabase();
		String where = FILED_ID + " = ?";
		String[] whereValue = { Integer.toString(id) };
		db.delete(TABLE_NAME, where, whereValue);
	}
	
	public long insert(String str_nums,String str_words,String str_phonetic, String str_explains,String str_times){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(FILED_CURSOR_ID, str_nums);
		cv.put(FILED_WORDS, str_words);
		cv.put(FILED_PHONETICS, str_phonetic);
		cv.put(FILED_EXPLAIN, str_explains);
		cv.put(FILED_TIME, str_times);
		long row = db.insert(TABLE_NAME, null,cv);
		return row;
	}
	
	// 在某一个表中插入记录
	public long insertOnTable(String tablename,String str_words,byte[] str_phonetic, String str_explains,String str_times,String image_str,String str_part_record){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
//		cv.put(FILED_CURSOR_ID, str_nums);
		cv.put(FILED_WORDS, str_words);
		cv.put(FILED_PHONETICS, str_phonetic);
		cv.put(FILED_EXPLAIN, str_explains);
		cv.put(FILED_TIME, str_times);
		
		
		cv.put(FIELD_YOUR_ANSWER, "");
		cv.put(FIELD_RESULT, "");
		cv.put(FIELD_RIGHT_ANSWER, "");
		cv.put(FIELD_YEAR, data_year);
		cv.put(FIELD_MONTH, data_month);
		cv.put(FIELD_DAY, data_day);
		
		cv.put(FIELD_IMAGE_NUM,image_str);
		//add
		cv.put(FIELD_GET_PART_RECORD, str_part_record);
		
		long row = db.insert(tablename, null,cv);
//		System.out.println(" row = "+row);
		return row;
	}

	public long insertOnDictTable(String tablename,String str_words,byte[] str_phonetic ,byte[]explain ,String str_times,String image_str,String str_part_record){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(FILED_WORDS, str_words);
		cv.put(FIELD_BYTE_EXPLAIN, str_phonetic);//
		cv.put(FILED_TIME, str_times);
		
		cv.put(FIELD_YOUR_ANSWER, "");
		cv.put(FIELD_RESULT, "");
		cv.put(FIELD_RIGHT_ANSWER, "");
		
		cv.put(FIELD_YEAR, data_year);
		cv.put(FIELD_MONTH, data_month);
		cv.put(FIELD_DAY, data_day);
		
		cv.put(FIELD_IMAGE_NUM,image_str);
		//add
		cv.put(FIELD_GET_PART_RECORD, str_part_record);
		
		
//		final ByteArrayOutputStream byte_os = new ByteArrayOutputStream();
//		bmp.compress(Bitmap.CompressFormat.PNG, 100, byte_os);
//		cv.put(FIELD_BITMAP,byte_os.toByteArray());
		
		cv.put(FIELD_BITMAP,explain);
		
		long row = db.insert(tablename, null,cv);
		return row;
	}
	
	public void getString(String year ,String month ,String day){
		data_year = year;
		data_month = month;
		data_day = day;
	}
	
	//对某一表记录进行删除
	public void delOnTable(String tablename ,int id){
		SQLiteDatabase db = this.getWritableDatabase();
		String where = FILED_ID + " = ?";
		String[] whereValue = { Integer.toString(id) };
		db.delete(tablename, where, whereValue);
	}
	
	//数据库中添加表
	public String addTable(String tablename){
		SQLiteDatabase db = this.getWritableDatabase();
		String TABLE_NAME = tablename;
        String str_sql_2 =  "CREATE table " + TABLE_NAME + 
								" (" + FILED_ID+ " INTEGER primary key autoincrement, "+ 
								FILED_CURSOR_ID + " INTEGER, "+
								FILED_WORDS + " text, " + 
								FILED_PHONETICS + " blob, " + 
								FILED_EXPLAIN+" text, "+
								FILED_TIME +" text, " +  
								FIELD_YOUR_ANSWER+" text, "+FIELD_RESULT +" text, "+FIELD_RIGHT_ANSWER+ " text, "+
								FIELD_YEAR+" INTEGER, "+FIELD_MONTH+" INTEGER, "+FIELD_DAY+" INTEGER, "
								+FIELD_IMAGE_NUM+" INTEGER, "+FIELD_BITMAP+" blob, "+FIELD_BYTE_EXPLAIN+" blob, "+FIELD_GET_PART_RECORD+" INTEGER)";
        
        db.execSQL(str_sql_2);
        return tablename;
	}
	//数据库中删除表
	public void deleteTable(String table_str){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE if exists "+table_str);
//		db.close();
//		onCreate(db);
	}
	
	//更新数据
	public void update(int id , String str_1,String str_2,String str_3){
		SQLiteDatabase db = this.getWritableDatabase();
		String where = FILED_ID + " = ? ";
		String[] whereValue = { Integer.toString(id) };
		ContentValues cv = new ContentValues();
		cv.put(FIELD_YOUR_ANSWER, str_1);
		cv.put(FIELD_RESULT, str_2);
		cv.put(FIELD_RIGHT_ANSWER, str_3);
		
		db.update(TABLE_NAME, cv, where, whereValue);
	}
	// 插入某个要查询的记录
	public long insertsomeColumn(String str_table,String part_record){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(FIELD_GET_PART_RECORD, part_record);
		
		long row = db.insert(str_table, null,cv);
		return row;
	}
	
	// 更新词典数据
	public void updateOndict(int id , String str_1,String str_2,byte[] m_byte){
		SQLiteDatabase db = this.getWritableDatabase();
		String where = FILED_ID + " = ? ";
		String[] whereValue = { Integer.toString(id) };
		ContentValues cv = new ContentValues();
		cv.put(FIELD_YOUR_ANSWER, str_1);
		cv.put(FIELD_RESULT, str_2);
		cv.put(FIELD_BITMAP, m_byte);
		
		db.update(WORDS_TABLE, cv, where, whereValue);
	}
	public void updateImage(int id,String str_nums,String table_name){
		SQLiteDatabase db = this.getWritableDatabase();
		String where = FILED_ID + " = ? ";
		String[] whereValue = { Integer.toString(id) };
		ContentValues cv = new ContentValues();
		cv.put(FIELD_IMAGE_NUM, str_nums);
		db.update(table_name, cv, where, whereValue);
	}
}
