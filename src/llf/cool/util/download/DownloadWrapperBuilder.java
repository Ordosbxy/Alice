package llf.cool.util.download;

import llf.cool.data.DataBaseBuilder;
import llf.cool.model.Track;
import android.content.ContentValues;
import android.database.Cursor;

public class DownloadWrapperBuilder extends DataBaseBuilder<DownloadWrapper>{

	@Override
	public DownloadWrapper build(Cursor c) {
		// TODO Auto-generated method stub
		Track track = new Track();
		track.setId(c.getInt(c.getColumnIndex("track_id")));
		track.setTitle(c.getString(c.getColumnIndex("track_title")));
		track.setCoverName(c.getString(c.getColumnIndex("track_cover_name")));
		
		DownloadWrapper process = new DownloadWrapper(track, 
														DownloadHelper.getDownloadPath(), 
														  DownloadHelper.getDownloadFormat(), 
														    0);
		int progress = c.getInt(c.getColumnIndex("downloaded"));
		if(progress == 1){
			process.setProgress(100);
		}
		return process;
	}

	@Override
	public ContentValues deconstruct(DownloadWrapper t) {
		// TODO Auto-generated method stub
		return null;
	}

}
