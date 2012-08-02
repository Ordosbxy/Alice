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
	 * 添加 “一个下载观察者对象” 到下载观察者列表中
	 */
	public void registerDownloadObserver(DownloadObserver observer);

	/*
	 * 删除 “一个下载观察者对象” 从下载观察者列表中
	 */
	public void deregisterDownloadObserver(DownloadObserver observer);

	public void notifyObservers();
}
