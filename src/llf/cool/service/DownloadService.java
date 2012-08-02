package llf.cool.service;

import llf.cool.CoolApplication;
import llf.cool.model.RootResource;
import llf.cool.model.Track;
import llf.cool.util.download.DownloadHelper;
import llf.cool.util.download.DownloadProvider;
import llf.cool.util.download.DownloadWrapper;
import llf.cool.util.download.DownloadWrapperListener;
import llf.cool.util.download.MediaScannerNotifier;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * ��̨���ط���
 * @author Administrator
 *
 */
public class DownloadService extends Service{

	public static final String ACTION_ADD_A_DOWNLOAD = "cool_download";
	public static final String EXTRA_FOR_DOWNLOAD = "resource_info"; 
	
	private DownloadProvider mDownloadProvider;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		logi("onCreate");
		mDownloadProvider = CoolApplication.getInstance().getDownloadManager().getProvider();
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		logi("onStart");		
		if(intent == null) return;
		
		String action = intent.getAction();
		if(action.equals(ACTION_ADD_A_DOWNLOAD)){
			Track track = (Track) intent.getSerializableExtra(EXTRA_FOR_DOWNLOAD);
			addToDownloadQueue(track, startId);
		}		
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		logi("onDestroy");
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @param track     wrap the download info.
	 * @param startId represent this specific request.
	 */
	private void addToDownloadQueue(Track track, int startId) {
		// TODO Auto-generated method stub
		//���ȼ�����ݿ⣬��track�Ƿ��Ѿ����ڣ� �����ظ�����
		String path = DownloadHelper.getDownloadPath();
		String format = DownloadHelper.getDownloadFormat();
		DownloadWrapper process = new DownloadWrapper(track, path, format, startId);
		if(mDownloadProvider.queueDownload(process)){
			process.setListener(mListener);
			//���������ء��첽����
			process.start();
		}
	}

	private DownloadWrapperListener mListener = new DownloadWrapperListener() {
		@Override
		public void downloadStarted() {
			// TODO Auto-generated method stub			
		}

		@Override
		public void downloadEnded(DownloadWrapper process) {
			// TODO Auto-generated method stub
			
			/*������ɺ� ���¸���״̬��֪ͨ��ý��ɨ�������ļ���ϵͳ����*/
			mDownloadProvider.downloadCompleted(process);
			new MediaScannerNotifier(DownloadService.this, process);
		}
	};
	
	public void notifyScanCompleted(){
		if(mDownloadProvider.getQueuedDownloads().size()==0){
			this.stopSelf();
		}
	}
	
	private void logi(String str){
		Log.i("DownloadService", str);
	}
	
	private void loge(String str){
		Log.e("DownloadService", str);
	}
}
