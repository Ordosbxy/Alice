package llf.cool.util.download;

import java.util.ArrayList;

import llf.cool.model.Track;

/**
 * 
 * ÿ��SD���¶���һ�� yingyutt/download.db, ����һ��library��
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
