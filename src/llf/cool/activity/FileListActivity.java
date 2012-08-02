package llf.cool.activity;

import java.io.File;
import java.util.ArrayList;

import llf.cool.R;
import llf.cool.adapter.FileListAdapter;
import llf.cool.data.Music;
import llf.cool.service.AudioService;
import llf.cool.util.PubFunc;
import llf.cool.view.RollBackListView;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class FileListActivity extends Activity{
	private RollBackListView mLv_ResourceList = null;
	private FileListAdapter mResourceListAdapter = null;
	private ProgressDialog mSearchProgressDialog = null;
	private ImageButton image = null;
	
	private int mExtraPostion = 0;
	private String filePath = AudioService.MUSIC_PATH;
	
	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_list);
		initUI();
	}

	@Override
	protected void onStart(){
		super.onStart();
		mExtraPostion = getIntent().getIntExtra("position", 0);
		//Show a progressDialog
		mSearchProgressDialog = new ProgressDialog(this);
		mSearchProgressDialog.setTitle(R.string.progress_dialog_title);
		mSearchProgressDialog.setMessage(getString(R.string.progress_dialog_search));
		mSearchProgressDialog.show();
		new ResourceSearcherThread().start();
	}

	@Override
	protected void onRestart(){
		super.onRestart();
		File f = new File(filePath);
		log("onRestart"+filePath);
		filePath = f.getParent();
		log("onRestart"+filePath);
	}

	private void initUI(){		
		image = (ImageButton) findViewById(R.id.addfolder);
		mLv_ResourceList = (RollBackListView)findViewById(R.id.lv_englishList);
		image.setOnClickListener(new AddFolderOnClickListener());
		mLv_ResourceList.setOnItemClickListener(new MusicListOnItemClickListener());
	}

	
	private void showData(){
		mResourceListAdapter = new FileListAdapter(this,
				AudioService.MUSIC_PATH);
		mLv_ResourceList.setAdapter(mResourceListAdapter);
		mResourceListAdapter.notifyDataSetChanged();
	}

	private class ResourceSearcherThread extends Thread{
		@Override
		public void run(){			
			log("startSearchThread");
			// search musics in sdcard
			String searchedPath = filePath;
			if (searchedPath.equals("/sdcard")){
				searchedPath = AudioService.MUSIC_PATH;
			}
			ArrayList<Music> musicList = AudioService
					.getMusics(FileListActivity.this, searchedPath);
			AudioService.setMusicList(musicList);			
			log("musicList="+musicList);
			
			// notify to init UI
			Message msg = handler.obtainMessage();
			handler.sendMessage(msg);
		}
	}

	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			mSearchProgressDialog.cancel();
			showData();
		}
	};

	private class MusicListOnItemClickListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position, long id){
			
			if (filePath.equals("/sdcard")){
				filePath = AudioService.MUSIC_PATH;
			}
			int fileSize = AudioService.getFolder(filePath).size();
			ArrayList<File> folder = AudioService.getFolder(filePath);
			
			log("MusicList clicked. size = "
					+ AudioService.getFolder(filePath).size() + " position = "
					+ position );

			for (int i = 0; i < fileSize; i++){
				log(i+"   "+AudioService.getFolder(filePath).get(i).getName());
			}
			
			//排序
			PubFunc.sortFileListInfo(folder);			
			
			File f = folder.get(position);
			log("f.getPath="+f.getPath());
			if (fileSize > 0){
				if (f.isFile()){
					view.performHapticFeedback(
							HapticFeedbackConstants.LONG_PRESS,
							HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
					view.playSoundEffect(SoundEffectConstants.CLICK);
					
					 /* 
					  *Go to next step
					  */
					Intent intent = new Intent(FileListActivity.this, AudioActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("position", position);
					intent.putExtra("filePath", filePath);
					startActivity(intent);
				}else{
					filePath = f.getPath();
					ArrayList<File> folder1 = AudioService.getFolder(filePath);
					PubFunc.sortFileListInfo(folder1);
					if (folder1.size() <= 0){
						Toast.makeText(view.getContext(), R.string.nodata,
								Toast.LENGTH_SHORT).show();
						filePath = f.getParent();
					}else{
						mResourceListAdapter = new FileListAdapter(
								FileListActivity.this, f.getPath());
						mLv_ResourceList.setAdapter(mResourceListAdapter);
						mResourceListAdapter.notifyDataSetChanged();
					}
				}
			}else{ //file.size<0
				Toast.makeText(view.getContext(), 
						R.string.nodata,
						Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private class AddFolderOnClickListener implements View.OnClickListener{
		@Override
		public void onClick(View v){
			final EditText edit = new EditText(v.getContext());
			edit.setHint("新建列表1");
			image.setBackgroundResource(R.drawable.addpress);
			new AlertDialog.Builder(v.getContext())
					.setTitle("新建列表")
					.setView(edit)
					.setPositiveButton(R.string.confirm,
							new DialogInterface.OnClickListener(){
								public void onClick(DialogInterface dialog, int which){										
									String filePath = "";
									if (edit.getText().toString().equals("")){
										filePath = edit.getHint()
												.toString();
									}else{
										filePath = edit.getText()
												.toString();
									}
									File f = new File(
											AudioService.MUSIC_PATH
													+ filePath);
									if (f.exists()){
										return;
									}else{
										f.mkdir();
									}
									showData();
								}
							})
					.setNegativeButton("取消", null).show();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if (keyCode == KeyEvent.KEYCODE_BACK){
			File f = new File(filePath);
			filePath = f.getParent();
			String name = filePath.concat("/");

			log(name.equals(AudioService.MUSIC_PATH)+" "+AudioService.MUSIC_PATH+"---onKeyDown---"+filePath);
			
			if(name.equals(AudioService.MUSIC_PATH)){
				mResourceListAdapter = new FileListAdapter(
						FileListActivity.this, filePath);
				mLv_ResourceList.setAdapter(mResourceListAdapter);
				mResourceListAdapter.notifyDataSetChanged();
			}else{
			    Intent startMain = new Intent(Intent.ACTION_MAIN);
	            startMain.addCategory(Intent.CATEGORY_HOME);
	            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	            startActivity(startMain);
	            //?
	            System.exit(0);
			    return false;
			}
			return false;
		}
		return true;
	}
	
	private void log(String log){
		Log.i("FileListActivity", log+"");
	}
}