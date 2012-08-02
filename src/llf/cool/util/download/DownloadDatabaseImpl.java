package llf.cool.util.download;

import java.util.ArrayList;

import llf.cool.model.RootResource;
import llf.cool.model.Track;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class DownloadDatabaseImpl implements DownloadDatabase {

	private String mPath;
	
	private static final String TABLE_NAME = "library";
	
	private static final int DB_VERSION = 1;
	
	private SQLiteDatabase mDb;
	
	public DownloadDatabaseImpl(String path) {
		// TODO Auto-generated constructor stub
		mPath = path;
		
		mDb = getDb();
		if(mDb == null) 
			return;
		if(mDb.getVersion()<DB_VERSION)
			new UpdaterBuilder().getUpdater(DB_VERSION).update();
	}
	
	private SQLiteDatabase getDb() {
		// TODO Auto-generated method stub
		try {
			return SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
			
		} catch (SQLiteException e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.e("DownloadDatabaseImpl", "创建数据库失败");
			return null;
		}
	}

	@Override
	public boolean recordTrack(Track track) {
		// TODO Auto-generated method stub
		if(mDb == null) return false; //数据库还未建立
		
		ContentValues values = new ContentValues();
		values.put("track_id", track.getId());
		values.put("track_title", track.getTitle());
		values.put("track_intro", track.getIntro());
		values.put("downloaded", 0);
		values.put("create_time", track.getCreateTime());
		values.put("score", track.getScore());
		values.put("down_num", track.getDownNum()); 
		values.put("down_url", track.getDownUrl());
		values.put("track_cover_name", track.getCoverName());		
		
		String[] whereArgs = { "" + track.getId()};
		long row_count = mDb.update(TABLE_NAME, values, "track_id=?", whereArgs);

		if (row_count == 0) {
			mDb.insert(TABLE_NAME, null, values);
		}
		Log.i("DownloadDatabaseImpl", "addToLibrary row_count="+row_count);
		return row_count != -1l;
	}

	@Override
	public boolean setStatus(Track track, boolean downloaded) {
		// TODO Auto-generated method stub
		if(mDb == null) return false; //数据库还未建立
		
		ContentValues values = new ContentValues();
		values.put("downloaded", downloaded ? 1 : 0);
		
		String[] whereArgs = {"" + track.getId()};
		long row_count = mDb.update(TABLE_NAME, values, "track_id=?", whereArgs);
		if(row_count<=0){
			Log.i("DownloadDatabaseImpl", "setStatus 失败");
		}
		return false;
	}

	@Override
	public boolean trackAvailabl(Track track) {
		// TODO Auto-generated method stub
		if(mDb == null) return false; //数据库还未建立
		String[] whereArgs = {"" + track.getId()};
		Cursor cursor = mDb.query(TABLE_NAME, null, "track_id=? and downloaded>0", whereArgs, null, null, null);
		
		boolean value = cursor.getCount() > 0;
		cursor.close();
		
		return value;
	}

	@Override
	public ArrayList<DownloadWrapper> getAllDownloadProcess() {
		// TODO Auto-generated method stub
		ArrayList<DownloadWrapper> processes = new ArrayList<DownloadWrapper>();
		if(mDb == null) return processes;
		
		Cursor cursor = mDb.query(TABLE_NAME, null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			while(!cursor.isAfterLast()){
				processes.add(new DownloadWrapperBuilder().build(cursor));
				cursor.moveToNext();
			}
		}
		cursor.close();
		return processes;
	}

	@Override
	public void remove(DownloadWrapper process) {
		// TODO Auto-generated method stub
		if(mDb == null) return; //数据库还未建立
		
		String[] whereArgs = {"" + process.getDownloadEntry().getId()};
		mDb.delete(TABLE_NAME, "track_id", whereArgs);
	}

	private class DatabaseUpdaterV1 extends DatabaseUpdater {

		//TODO Add copying and updating data from previous db version

		private static final int VERSION = 1;

		@Override
		public void update() {
			if (getUpdater() != null) {
				getUpdater().update();
			}

			try{
				mDb.execSQL("DROP TABLE " + TABLE_NAME + ";");
			} catch (SQLiteException e) {
				Log.i("DownloadDatabaseImpl", "library table not existing");
			}
			createTables();
			mDb.setVersion(VERSION);
		}

		private void createTables(){
			mDb.execSQL("CREATE TABLE IF NOT EXISTS "
					+ TABLE_NAME
					+ "(track_id INTEGER UNIQUE, " 
					+ " track_title VARCHAR, " 
					+ " track_intro VARCHAR, " 
					
					+ " downloaded INTEGER, " 
					+ " create_time VARCHAR, " 
					+ " score INTEGER, " 
					+ " down_num INTEGER, "
					+ " down_url VARCHAR, "					
					+ " track_cover_name VARCHAR, " 
					
					+ " track_cover_img VARCHAR, " 					
					+ " track_duration INTEGER, " 
					+ " track_url VARCHAR, "
					
					+ " album_id INTEGER, " 
					+ " album_name VARCHAR, " 
					+ " album_image VARCHAR, "
					+ " artist_name VARCHAR, "
					+ " album_track_num INTEGER);");
		}

	}

	/**
	 * 
	 */
	private class UpdaterBuilder {

		public DatabaseUpdater getUpdater(int version){
			DatabaseUpdater updater = null;

			switch(version){
			case 1: updater = new DatabaseUpdaterV1();
				break;
			case 0: updater = null;
				break;
			}

			if(version > mDb.getVersion() + 1){
				updater.setUpdater(getUpdater(version - 1));
			}
			return updater;
		}
	}
	
}

/**
 * Database updater. Build around Decorator pattern.
 */
abstract class DatabaseUpdater {
	private DatabaseUpdater mUpdater;

	abstract void update();

	public void setUpdater(DatabaseUpdater mUpdater) {
		this.mUpdater = mUpdater;
	}

	public DatabaseUpdater getUpdater() {
		return mUpdater;
	}
}
