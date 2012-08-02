package llf.cool.util.download;

import java.util.ArrayList;

import llf.cool.model.Track;

/**
 * 
 * 每个SD卡下都有一个 yingyutt/download.db, 包涵一张library表
 * 
 * @author Administrator
 *
 */

public interface DownloadDatabase {

	public boolean recordTrack(Track track);
	
	public boolean setStatus(Track track, boolean downloaded);
	
	public boolean trackAvailabl(Track track);
	
	public ArrayList<DownloadWrapper> getAllDownloadProcess();
	
	public void remove(DownloadWrapper process);
	
	
}
