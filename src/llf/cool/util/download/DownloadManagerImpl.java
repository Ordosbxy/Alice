package llf.cool.util.download;

import java.util.ArrayList;

import llf.cool.model.Track;
import llf.cool.service.DownloadService;
import android.content.Context;
import android.content.Intent;

public class DownloadManagerImpl implements DownloadManager{

	private Context mContext;
	private DownloadProvider mProvider;
	private ArrayList<DownloadObserver> mObservers;
	
	public DownloadManagerImpl(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
		mProvider = new DownloadProviderImpl(this);
		mObservers = new ArrayList<DownloadObserver>();
	}
	
	/**
	 * 启动下载服务
	 */
	@Override
	public void download(Track track) {
		// TODO Auto-generated method stub
		Intent intent =  new Intent(mContext, DownloadService.class);
		intent.setAction(DownloadService.ACTION_ADD_A_DOWNLOAD);
		intent.putExtra(DownloadService.EXTRA_FOR_DOWNLOAD, track);
		mContext.startService(intent);
	}
	
	@Override
	public ArrayList<DownloadWrapper> getAllDownloads() {
		// TODO Auto-generated method stub
		return mProvider.getAllDownloads();
	}
	
	@Override
	public ArrayList<DownloadWrapper> getCompletedDownloads() {
		// TODO Auto-generated method stub
		return mProvider.getCompletedDownloads();
	}
	
	@Override
	public ArrayList<DownloadWrapper> getQueuedDownloads() {
		// TODO Auto-generated method stub
		return mProvider.getQueuedDownloads();
	}

	@Override
	public void deletedownload(DownloadWrapper process) {
		// TODO Auto-generated method stub
		mProvider.removeDownload(process);
		removeDownloadFromSD(process);
	}	
	
	private void removeDownloadFromSD(DownloadWrapper process) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getTrackPath() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public DownloadProvider getProvider() {
		// TODO Auto-generated method stub
		return mProvider;
	}

	@Override
	public synchronized void registerDownloadObserver(DownloadObserver observer) {
		// TODO Auto-generated method stub
		mObservers.add(observer);
	}

	@Override
	public synchronized void deregisterDownloadObserver(DownloadObserver observer) {
		// TODO Auto-generated method stub
		mObservers.remove(observer);
	}
	
	@Override
	public synchronized void notifyObservers() {
		// TODO Auto-generated method stub
		for(DownloadObserver observer : mObservers){
			observer.onDownloadChanged(this);
		}
	}

}
