package llf.cool.util.download;

import java.util.ArrayList;

import llf.cool.model.RootResource;

public interface DownloadProvider {

	public ArrayList<DownloadWrapper> getAllDownloads();

	public ArrayList<DownloadWrapper> getQueuedDownloads();

	public ArrayList<DownloadWrapper> getCompletedDownloads();

	public boolean queueDownload(DownloadWrapper process);

	public void downloadCompleted(DownloadWrapper process);

	public void removeDownload(DownloadWrapper process);

	public boolean resourceAvailable(RootResource res);
	
}
