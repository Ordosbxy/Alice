package llf.cool.util.download;

import java.util.ArrayList;

import llf.cool.model.Track;

public interface DownloadManager {

	public void download(Track track);
	
	public ArrayList<DownloadWrapper> getAllDownloads();

	public ArrayList<DownloadWrapper> getCompletedDownloads();

	public ArrayList<DownloadWrapper> getQueuedDownloads();
	
	public void deletedownload(DownloadWrapper process);
	
	public void getTrackPath();
	
	public DownloadProvider getProvider();
		
	/*
	 * ��� ��һ�����ع۲��߶��� �����ع۲����б���
	 */
	public void registerDownloadObserver(DownloadObserver observer);

	/*
	 * ɾ�� ��һ�����ع۲��߶��� �����ع۲����б���
	 */
	public void deregisterDownloadObserver(DownloadObserver observer);

	public void notifyObservers();
}
