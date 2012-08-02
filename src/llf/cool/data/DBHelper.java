package llf.cool.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "english_study_database"; // 数据库名�?
	private static final int DB_VERSION = 1; // 版本

	// table 1
	public static final String TABLE_NAME_STAR = "star_table"; // 星级的表�?
	public static final String STAR_TABLE_ID = "_id";
	public static final String STAR_TABLE_LRC_NAME = "lrc_name";
	public static final String STAR_TABLE_TIME_INDEX = "lrc_time_index";
	public static final String STAR_TABLE_LEVEL = "star_level";

	// table 2
	public static final String TABLE_NAME_NEWWORD = "newword_table"; // 生词本菜�?
	public static final String NEWWORD_TABLE_ID = "_id";
	public static final String NEWWORD_TABLE_LRC_NAME = "lrc_name";
	public static final String NEWWORD_TABLE_TIME_INDEX = "lrc_time_index";
	public static final String NEWWORD_TABLE_WORD_INDEX = "word_index";

	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE " + TABLE_NAME_STAR + " (" + STAR_TABLE_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + STAR_TABLE_LRC_NAME
				+ " VARCHAR NOT NULL, " + STAR_TABLE_TIME_INDEX
				+ " INTEGER NOT NULL, " + STAR_TABLE_LEVEL
				+ " INTEGER NOT NULL);";
		db.execSQL(sql); // 创建第一张表
		
		sql = "CREATE TABLE " + TABLE_NAME_NEWWORD + " (" + NEWWORD_TABLE_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + NEWWORD_TABLE_LRC_NAME
				+ " VARCHAR NOT NULL, " + NEWWORD_TABLE_TIME_INDEX
				+ " INTEGER NOT NULL, " + NEWWORD_TABLE_WORD_INDEX
				+ " INTEGER NOT NULL);";
		db.execSQL(sql); // 创建第二张表
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String sql = "DROP TABLE IF EXISTS " + TABLE_NAME_STAR; // 删除第一张表
		db.execSQL(sql);
		sql = "DROP TABLE IF EXISTS " + TABLE_NAME_NEWWORD; // 删除第二张表
		db.execSQL(sql);
		onCreate(db);
	}

}
