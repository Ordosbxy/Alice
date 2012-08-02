package llf.cool.activity;

import com.markupartist.android.widget.ActionBar;

import llf.cool.R;
import llf.cool.adapter.DownloadListAdapter;
import llf.cool.util.download.DownloadManager;
import llf.cool.util.download.DownloadObserver;
import llf.cool.view.RollBackListView;
import android.app.Activity;
import android.os.Bundle;

public class DownloadActivity extends Activity implements DownloadObserver{
	
	private ActionBar actionBar;
	private RollBackListView mDownloadList;
	private DownloadListAdapter mDownloadAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download);
		
		mDownloadList = (RollBackListView)findViewById(R.id.list_of_download);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onDownloadChanged(DownloadManager manager) {
		// TODO Auto-generated method stub
		
	}

}
