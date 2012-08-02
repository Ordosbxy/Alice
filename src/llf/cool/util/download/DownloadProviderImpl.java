package llf.cool.util.download;

import java.util.ArrayList;

import android.database.DatabaseUtils;

import llf.cool.model.RootResource;

public class DownloadProviderImpl implements DownloadProvider{

	private DownloadDatabase mDb;
	private DownloadManager mDownloadManager;
	private ArrayList<DownloadWrapper> mQueuedLists;
	private ArrayList<DownloadWrapper> mCompletedLists;
	
	public DownloadProviderImpl(DownloadManager downloadManager) {
		// TODO Auto-generated constructor stub
		mDb = new DownloadDatabaseImpl(DownloadHelper.getDownloadPath());
		mDownloadManager = downloadManager;
		mQueuedLists = new ArrayList<DownloadWrapper>();
		mCompletedLists = new ArrayList<DownloadWrapper>();
	}
	
	@Override
	public ArrayList<DownloadWrapper> getAllDownloads() {
		// TODO Auto-generated method stub
		ArrayList<DownloadWrapper> allLists = new ArrayList<DownloadWrapper>();
		allLists.addAll(mQueuedLists);
		allLists.addAll(mCompletedLists);
		return allLists;
	}

	@Override
	public ArrayList<DownloadWrapper> getCompletedDownloads() {
		// TODO Auto-generated method stub
		return mCompletedLists;
	}

	@Override
	public ArrayList<DownloadWrapper> getQueuedDownloads() {
		// TODO Auto-generated method stub
		return mQueuedLists;
	}
	
	/**��һ���������е������У�����Ѿ����κ�һ�������У���������ʧ�� false��������Ȼ*/
	@Override
	public boolean queueDownload(DownloadWrapper process) {
		// TODO Auto-generated method stub
		for(DownloadWrapper pro : mCompletedLists){
			if(pro.getDownloadEntry().getId() == process.getDownloadEntry().getId())
				return false;
		}
		
		for(DownloadWrapper pro : mQueuedLists){
			if(pro.getDownloadEntry().getId() == process.getDownloadEntry().getId())
				return false;
		}
		
		if(mDb.recordTrack(process.getDownloadEntry())){
			mQueuedLists.add(process);
			mDownloadManager.notifyObservers();
			return true;
		} else {
			return false;			
		}
		
	}

	/**������ɺ���¸���״̬*/
	@Override
	public void downloadCompleted(DownloadWrapper process) {
		// TODO Auto-generated method stub
		mQueuedLists.remove(process);
		mCompletedLists.add(process);
		mDb.setStatus(process.getDownloadEntry(), true);
		mDownloadManager.notifyObservers();
	}

	@Override
	public void removeDownload(DownloadWrapper process) {
		// TODO Auto-generated method stub
		if(process.getProgress() < 100){
			process.cancel();
			mQueuedLists.remove(process);
		} else {
			mCompletedLists.remove(process);
		}
		mDb.remove(process);
		mDownloadManager.notifyObservers();
	}

	@Override
	public boolean resourceAvailable(RootResource res) {
		// TODO Auto-generated method stub
		for(DownloadWrapper process : mCompletedLists){
			if(res.getId() == process.getDownloadEntry().getId()){
				return true;
			}
		}
		return false;
	}

}
