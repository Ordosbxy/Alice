package llf.cool.activity;

import java.io.File;
import java.util.ArrayList;

import llf.cool.R;
import llf.cool.adapter.CurrentAdapter;
import llf.cool.service.AudioService;
import llf.cool.util.PubFunc;
import llf.cool.view.RollBackListView;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CurrentListActivity extends Activity
{

	private RollBackListView mLv_ResourceList = null;
	private CurrentAdapter mResourceListAdapter = null;
	private ProgressDialog mSearchProgressDialog = null;
	private String filePath = null;
	private ImageButton image = null;
	private ArrayList<File> folder = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_file_list);

		filePath = getIntent().getStringExtra("filePath");
//		System.out.println("--filePath = " + filePath);

		mSearchProgressDialog = new ProgressDialog(this);
		mSearchProgressDialog.setTitle(R.string.progress_dialog_title);
		String msg = getString(R.string.progress_dialog_search);
		mSearchProgressDialog.setMessage(msg);
		mSearchProgressDialog.show();
	}

	@Override
	protected void onStart()
	{
		// TODO Auto-generated method stub
		super.onStart();
		new ResourceSearcherThread().start();
	}

	private void initUI()
	{
		mLv_ResourceList = (RollBackListView) findViewById(R.id.lv_englishList);
		image = (ImageButton) findViewById(R.id.addfolder);
		showData();
		mLv_ResourceList
				.setOnItemClickListener(new MusicListOnItemClickListener());
	}

	private void showData()
	{
		mResourceListAdapter = new CurrentAdapter(this, filePath);
		mLv_ResourceList.setAdapter(mResourceListAdapter);
		mResourceListAdapter.notifyDataSetChanged();
	}

	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			mSearchProgressDialog.cancel();
			initUI();
		}
	};

	private class MusicListOnItemClickListener implements OnItemClickListener
	{

		private String path = filePath;

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long id)
		{
//			System.out.println("size = "
//					+ MediaService.getFolder(filePath).size() + " position = "
//					+ position + " filePath " + filePath);
			if (filePath.equals("/sdcard"))
			{
				filePath = AudioService.MUSIC_PATH;
			}
			int fileSize = AudioService.getFolder(filePath).size();
			ArrayList<File> folder = AudioService.getFile(filePath);
			PubFunc.sortFileListInfo(folder);
			File f = folder.get(position);
			if (fileSize > 0)
			{
				view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS,
						HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
				view.playSoundEffect(SoundEffectConstants.CLICK);
				Intent intent = new Intent(CurrentListActivity.this,
						AudioActivity.class);
				intent.putExtra("CurrentListActivity", "CurrentListActivity");
				intent.putExtra("position", position);
				intent.putExtra("filePath", filePath);
				startActivity(intent);

			}
			else
			{
				Toast.makeText(view.getContext(), R.string.nodata,
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	protected void onRestart()
	{
		super.onRestart();
//		System.out.println("---------onRestart()--------------");
//		File f = new File(filePath);
//		filePath = f.getParent();
	}

	class ResourceSearcherThread extends Thread
	{
		@Override
		public void run()
		{
			folder = AudioService.getFile(filePath);
			Message msg = handler.obtainMessage();
			handler.sendMessage(msg);
		}
	}

//	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			Intent intent = new Intent(this,FileListActivity.class);
			startActivity(intent);
			finish();
		}
		return true;
	}

}
