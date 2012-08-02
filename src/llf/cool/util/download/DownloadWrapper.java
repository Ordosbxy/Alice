package llf.cool.util.download;

import llf.cool.CoolApplication;
import llf.cool.model.Track;

public class DownloadWrapper{
	
	private Track mTrack;
	private String mPath;
	private String mFormat;
	private int mStartId;

	private int mProgress;
	private int mTotalSize;
	private int mDownloadedSize;
	
	private DownloadManager mManager;	
	private DownloadTask mDownloadTask;
	private DownloadWrapperListener mListener;
	
	public DownloadWrapper(Track track, String path, String format,  int startId) {
		// TODO Auto-generated constructor stub
		this.mTrack  = track;
		this.mPath = path;
		this.mFormat = format;
		this.mStartId = startId;
		
		this.mProgress = 0;
		this.mManager = CoolApplication.getInstance().getDownloadManager();
	}
	

	public void start(){
		mDownloadTask = new DownloadTask(DownloadWrapper.this);
		mDownloadTask.execute();
	}

	public void pause(){
		
	}
	
	public void resume(){
		
	}
	
	public void cancel(){
		if(mDownloadTask != null){
			/*
			 * true if the thread executing this task should be interrupted; 
			 * otherwise, in-progress tasks are allowed to complete.
			 */
			mDownloadTask.cancel(true);
		}
	}

	public void notifyDownloadStarted(){
		if(mListener != null){
			mListener.downloadStarted();
		}
		mProgress = 0;
	}
	
	public void notifyDownloadEnded() {
		// TODO Auto-generated method stub
		if(!mDownloadTask.isCancelled()){
			if(mListener != null){
				mListener.downloadEnded(DownloadWrapper.this);
			}			
			mProgress = 100;
		}
	}
	
	public void setListener(DownloadWrapperListener listener){
		mListener = listener;
	}
	
	public Track getDownloadEntry(){
		return mTrack;
	}	
	public void setDownloadEntry(Track track){
		this.mTrack = track;
	}
	
	public String getDownloadPath(){
		return mPath;
	}	
	public void setDownloadPath(String path){
		this.mPath = path;
	}
	
	public String getDownloadFormat(){
		return mFormat;
	}	
	public void setDownloadFormat(String format){
		this.mFormat = format;
	}
	
	public int getStartId() {
		return mStartId;
	}
	public void setStartId(int mStartId) {
		this.mStartId = mStartId;
	}

	public int getProgress(){
		return mProgress;
	}
	public void setProgress(int progress){
		mProgress = progress;
	}

	public int getTotalSize() {
		return mTotalSize;
	}
	public void setTotalSize(int totalSize) {
		this.mTotalSize = totalSize;
	}
	
	public int getDownloadedSize(){
		return mDownloadedSize;
	}
	public void setDownloadedSize(int loadedSize){
		this.mDownloadedSize = loadedSize;
		int oldProgress = mProgress;
		mProgress = (loadedSize*100)/mTotalSize;
		if(mProgress != oldProgress){
			mManager.notifyObservers();
		}
	}
}
