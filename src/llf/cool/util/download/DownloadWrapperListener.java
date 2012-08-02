package llf.cool.util.download;

public interface DownloadWrapperListener {

	public void downloadStarted();
	
	public void downloadEnded(DownloadWrapper process);
	
}
