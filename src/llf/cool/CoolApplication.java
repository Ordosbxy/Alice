package llf.cool;

import llf.cool.util.ImageCache;
import llf.cool.util.download.DownloadManager;
import llf.cool.util.download.DownloadManagerImpl;
import android.app.Application;
import android.os.Environment;
import android.util.Log;

/**
 * 
 * @author bxy
 *
 */
public class CoolApplication extends Application{
	
	/**
	 * Singleton pattern
	 */
	private static CoolApplication instance;
	private ImageCache mImageCache;
	private DownloadManager mDownloadManager;

	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i("CoolApplication", "CoolApplication onCreated");
		instance = this;
		mImageCache = new ImageCache();
		mDownloadManager = new DownloadManagerImpl(this);
	}
	
	public static CoolApplication getInstance(){
		return instance;
	}
	
	public ImageCache getImageCache(){
		return mImageCache;
	}
	
	public DownloadManager getDownloadManager(){
		return mDownloadManager;
	}	
	
}
