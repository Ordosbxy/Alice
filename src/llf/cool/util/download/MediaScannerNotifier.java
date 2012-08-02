package llf.cool.util.download;

import llf.cool.service.DownloadService;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;

public class MediaScannerNotifier implements MediaScannerConnectionClient{

	private DownloadService mService;
	private DownloadWrapper mProcess;
	private MediaScannerConnection mConnection;
	public static int mScannedFilesInProgress = 0;
	
	public MediaScannerNotifier(DownloadService service, DownloadWrapper process) {
		// TODO Auto-generated constructor stub
		this.mService = service;
		this.mProcess = process;
		this.mConnection = new MediaScannerConnection(service, this);
		
		//Initiates a connection to the media scanner service.
		mConnection.connect();
	}
	
	@Override
	public void onMediaScannerConnected() {
		// TODO Auto-generated method stub
		String filePath = DownloadHelper.getDownloadPath();
		String fileName = mProcess.getDownloadEntry().getTitle();
		
		if(mConnection.isConnected()){
			mScannedFilesInProgress++;
			mConnection.scanFile(filePath + "/" + fileName + ".mp3", null);
		}
	}

	@Override
	public void onScanCompleted(String path, Uri uri) {
		// TODO Auto-generated method stub
		mScannedFilesInProgress--;
		mConnection.disconnect();
		
		if(mScannedFilesInProgress == 0){
			mService.notifyScanCompleted();
		}
		
	}

}
