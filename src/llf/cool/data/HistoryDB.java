package llf.cool.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HistoryDB
{
	public final static String DATABASE_NAME = "history.db";
	public final static String TABLE_NAME = "history_table";
	public final static int DATABASE_VERSION = 3;
	public final static String WORD_NAME = "word";
	public final static String ID = "id";
	private Context mContext;
	private SQLiteDatabase mSQLiteDatabase = null;
	private DatabaseHelper mDatabaseHelper = null;

	private static class DatabaseHelper extends SQLiteOpenHelper
	{

		public DatabaseHelper(Context context)
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		// @Override
		public void onCreate(SQLiteDatabase db)
		{
			String sql = "CREATE table " + TABLE_NAME + " (" + ID
					+ " INTEGER primary key autoincrement, " + WORD_NAME
					+ " text); ";

			db.execSQL(sql);
		}

		// @Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			String sql = "drop table if exists " + TABLE_NAME;
			db.execSQL(sql);
			onCreate(db);

		}

	}

	public HistoryDB(Context context)
	{
		mContext = context;
	}

	// 打开数据库
	public void open()
	{
		if (mSQLiteDatabase != null)
		{
			mSQLiteDatabase.close();
		}
		mDatabaseHelper = new DatabaseHelper(mContext);
		mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();

	}

	// 关闭数据库
	public void close()
	{
		mSQLiteDatabase.close();
		mDatabaseHelper.close();
	}

	public Cursor select()
	{
		Cursor cursor = mSQLiteDatabase.query(TABLE_NAME, null, null, null,
				null, null, null);
		return cursor;
	}

	public Cursor selectWord(String word)
	{
		String sql = "select word from history_table" + " where word =" + "'"+word+"'";
		Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
		return cursor;
	}

	public long insert(String word)
	{
		ContentValues cv = new ContentValues();
		cv.put(WORD_NAME, word);
		long result = mSQLiteDatabase.insert(TABLE_NAME, null, cv);
		return result;
	}

	public long delete(String word)
	{
		String where = WORD_NAME + " = ?";
		String[] wherevalues =
		{ word };
		int result = mSQLiteDatabase.delete(TABLE_NAME, where, wherevalues);
		return result;
	}

	public int clearAll()
	{
		int result = mSQLiteDatabase.delete(TABLE_NAME, null, null);
		return result;

	}
}
