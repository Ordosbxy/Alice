package llf.cool.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import llf.cool.R;
import llf.cool.data.Music;
import llf.cool.data.MyDatabase;
import llf.cool.service.AudioService;
import llf.cool.service.AudioService.PlayerStatus;
import llf.cool.util.LrcBitmap;
import llf.cool.util.PubFunc;
import llf.cool.view.LrcSurfaceView;
import llf.cool.view.LrcSurfaceView.LrcSurfaceViewOnCreateListener;
import llf.dictionary.engine.LLFDic;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class AudioActivity extends Activity{
		
	private ProgressDialog mProgressDialog = null;	
	private TextView mTv_currentMusicTitle = null;
	private ImageButton mBtn_playMode = null;
	private ImageButton mBtn_Playselect = null;
	private ImageButton mBtn_Playback = null;
	private ImageButton mBtn_PlayNext = null;
	private ImageButton mBtn_musicList = null;
	
	private LinearLayout mRepeateLayout;
	private TextView mTv_playorder = null;
	private TextView mTv_repeateone = null;
	private TextView mTv_repeateall = null;
	private TextView mTv_playone = null;
	
	private LinearLayout mDisplaymenutable = null;
	private RadioGroup mControllfont;
	private RadioButton mBig, mMiddle, mSmall;
	private TextView mMoreSource;
	private TextView mSearchWord;
	private TextView mNotebook;
	private TextView mCountmanager;
	private TextView mExit;

	private LrcSurfaceView lrcShow;//歌词view
	
	private TextView mTv_searchedWord;//显示单词的翻译
	private ImageButton mTv_btn_addNewWord = null;
	private ImageButton mBtn_star = null;
	private ImageButton mBtn_musicPrev = null;
	public static ImageButton mBtn_musicPlay = null;
	private ImageButton mBtn_musicNext = null;
	private ImageButton mBtn_starCheckState = null;	
	
	private static SeekBar mSeekBar;
	private static TextView mTv_musicCurrentTime;
	private static TextView mTv_musicDurationTime;
	
	private static final String SHARED_NAME = "yingyutt_shares";
	private static final String LASTPATH = "lastPath";
	private static final String PLAYMODE = "playMode";
	private static final String SHARED ="yingyutt_position";
	private static final String LASTPOSITION = "lastposition";
	private static final String LASTPLAYPOSITION ="lastplayposition";
	private static final String LASTPLAY ="lastplay";
	public static final String BOARDCAST_ACTION_GET_WORD = "brordcast_get_word";
	
	private String word;//装载单词
	private String explain;//与之相对应的翻译
	private String filePath;
	private String mCurrentMusicTitlePre = null;
	
	private int mCurrentMusicPosition = 0;
	private static int mCurrentPlayingPosition = 0;
	private static boolean isScrolling = false;
	
	public static boolean is_play_star_lines = false;
	public static boolean m_is_star = false;
	public static int mStarLevel = 0;
	public static int m_play_star_index;
	private long m_per_star_dunringTime;
	private int m_pre_play_star_timepoint;
	
	// playMode
	public int mCurrentPlayMode = 0;
	public static final int MODE_SINGE = 0; // 单曲播放
	public static final int MODE_SINGE_R = 1;// 单曲循环
	public static final int MODE_REPEAT = 2;// 全部循环
	public static final int MODE_ORDER = 3;// 顺序播放
	public static final String PLAY_MODE = "playMode";	
	
	
	private ArrayList<Music> mList;
	private NewWordsActivity m_wordsbook = null;
	private Timer timer;//用来从service中获取进度
	private Timer timerPlayStar;
	private ServiceConnection mServiceConnection;//与serivce的连接
	private AudioService.Control control;//service中的控制器
	private boolean isSeekBarMove=false;//是否正在控制进度条
		
	//监听歌词SurfaceView surfaceCreated完成
	private LrcSurfaceViewOnCreateListener lrcSurfaceViewOnCreateListener;
	
	private boolean first = true;
	private Handler durationHandler = new Handler(new Handler.Callback(){	
		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			mTv_musicDurationTime.setText(toTime(control.getMaxLength()));
			return false;
		}
	});
	
	/**歌曲播放结束 收到来自主界面线程的消息  设置play按钮按钮的显示背景图片*/   
	private Handler completedHander = new Handler(new Handler.Callback() {
		public boolean handleMessage(Message msg) {
			mBtn_musicPlay.setBackgroundResource(R.drawable.xml_playback_btn_play);
			control.setPlayerCompleted();
			lrcShow.setLongClickable(false);
			return true;
		}
	});
	
	/**
	 * LrcView用来控制歌曲进度
	 * 在歌词界面中  如果用户控制歌词的上下滚动时  同步更新当前界面中的seekbar
	 */
	private Handler handlerForLrc = new Handler(new Handler.Callback(){		
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
				case 0:
					mSeekBar.setProgress(msg.arg1);
					break;
				case 1:
					mSeekBar.setProgress(msg.arg1);
					control.seekTo(msg.arg1);
					break;
				default:
					break;
			}
			return true;
		}
	});	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.framelayout); 
		initViews();
		log("onCreate");
        
    }
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		log("onResume");
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setTitle(R.string.loadMusicProgressTitle);
		String msg = getString(R.string.loadMusicProgressMsg);
		mProgressDialog.setMessage(msg);
		mProgressDialog.show();
		new InitDataTask().execute((Void[]) null);			
	}
	
	@Override
	protected void onStart() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(BOARDCAST_ACTION_GET_WORD);
		registerReceiver(mWordReceiver, filter);		
	    
	    IntentFilter intentFilter=new IntentFilter();
	    intentFilter.addAction(AudioService.MODE_PLAY);
	    intentFilter.addAction(AudioService.TRICK_NOW);
	    registerReceiver(mModeBroadcastReceiver, intentFilter);
		super.onStart();
	}

	private class InitDataTask extends AsyncTask<Void, Void, Void>{
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... params){
			       
//			initPlayMode();			
			isScrolling = false;
			mCurrentMusicTitlePre = getString(R.string.current_title_pre);
	        mCurrentMusicPosition = getIntent().getIntExtra("position", 0);
	        filePath = getIntent().getStringExtra("filePath");

	        log("------onCreate-------");
	        log(" filePath="+filePath);  
	        log(" mCurrentMusicPosition="+mCurrentMusicPosition);  
			
			if(filePath==null || filePath.equals("")){
				filePath = getLastPath();
				filePath = filePath.substring(0, filePath.lastIndexOf("/")+1);
				mCurrentMusicPosition = getLastPosition();	
				
			}	
			
			int size = AudioService.getFolder(filePath).size();
			String str = getIntent().getStringExtra("CurrentListActivity");

			
			if (mCurrentMusicPosition < size){
				for (int i = 0; i < size; i++){
					if (AudioService.getFolder(filePath).get(i).isDirectory()){
						if (str == null || str.equals("")){
							mCurrentMusicPosition--;							
						}else{
							//do noting?
						}
					}
				}
			}
			log(" filePath="+filePath);
			log(" mCurrentMusicPosition="+mCurrentMusicPosition);  
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);			
	        startThread();
	        toBindService();
		}
	}
	
	private void play(){		
		
		m_is_star = false;
		is_play_star_lines = false;
		
//		m_play_star_index = 0;
//		m_per_star_dunringTime = 0;
//		m_pre_play_star_timepoint = 0;
		
//		if(LrcBitmap.m_current_time_point != null){
//			LrcBitmap.m_current_time_point.clear();
//			LrcBitmap.m_index.clear();
//			LrcBitmap.m_lines_lyric.clear();
//			LrcBitmap.m_per_star_time.clear();			
//		}
		
		ArrayList<File> fileList = getData();
		if (mCurrentMusicPosition >= fileList.size())
			mCurrentMusicPosition = mCurrentMusicPosition - 1;
		File file = fileList.get(mCurrentMusicPosition);
		control.setPosition(mCurrentMusicPosition);
		
		if(file.getPath().equals(getLastPath())){
			mCurrentPlayingPosition = getLastPlayPosition();
		}else{
			mCurrentPlayingPosition = 0;
		}
		control.play(file.getPath(), mCurrentPlayingPosition);
		lrcShow.setLongClickable(true);
		lrcShow.setClickable(true);
		setLrcFile();
	}
	
	private class PlayBtnOnClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			if(control==null)
				return;		
			if(!startFunction())
				return;
			ArrayList<File> musicList = getData();
			switch (v.getId()){			
				case R.id.btnPlay:
					if(!is_play_star_lines){
						if(control.getPlayerStatus() == PlayerStatus.RUN){
							control.pause();
						} else {
							control.continuing();
						}											
					} else {
						play();
					}
					break;
			
				case R.id.btnPrev:
					control.pre();				
					if (musicList.size() > 0){
						if (mCurrentMusicPosition > 0){
							mCurrentMusicPosition--;
						}else{
							mCurrentMusicPosition = musicList.size() - 1;
						}
						play();
					}
					break;
				case R.id.btnNext:		
					control.nex();
					if (musicList.size() > 0){
						if (mCurrentMusicPosition < musicList.size() - 1){
							mCurrentMusicPosition++;
						}else{
							mCurrentMusicPosition = 0;
						}
						play();
					}
					break;
			}
			
			mTv_musicDurationTime.setText(toTime(control.getMaxLength()));
			System.gc();
			
			if(control.getPlayerStatus()==AudioService.PlayerStatus.RUN){
				mBtn_musicPlay.setBackgroundResource(R.drawable.xml_playback_btn_pause);
			}else{
				mBtn_musicPlay.setBackgroundResource(R.drawable.xml_playback_btn_play);
			}
		}
		
	}
	
	private class MySeekBarChangeListener implements OnSeekBarChangeListener {				
			
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			mTv_musicCurrentTime.setText(toTime(progress));
		}
		
		public void onStartTrackingTouch(SeekBar seekBar) {
			isSeekBarMove=true;
		}
		
		public void onStopTrackingTouch(SeekBar seekBar) {
			isSeekBarMove=false;
			control.seekTo(seekBar.getProgress());
		}
	}

	private class BtnOnClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
				case R.id.btnPlayMode:
					changePlayMode();
					log("PlayMode"+getPlayMode());
					break;
				case R.id.btnMusicList:
					break;
				case R.id.btn_addNewWord:/*点击添加生词*/
					if(word!=null && word!=""){
						addNewWord(word);
					}
					break;
				case R.id.btnStar:
					if (m_is_star == false){
						m_is_star = true;
					}else{
						m_is_star = false;
					}
					break;
					
				case R.id.btnCheckState:
					if(!is_play_star_lines){
						if((LrcBitmap.m_current_time_point).size()>0){
							is_play_star_lines = true; //通知LrcBitmap，当前播放的是标星歌词
							m_play_star_index = 0; //标星歌词的索引
							m_pre_play_star_timepoint = LrcBitmap.m_current_time_point
							.get(m_play_star_index);
							m_per_star_dunringTime = LrcBitmap.m_per_star_time
							.get(m_play_star_index);
							// 跳到第一个星号的播放点
							control.seekTo(m_pre_play_star_timepoint);
							
							timerPlayStar = new Timer();
							timerPlayStar.schedule(new TimerTask(){
								// @Override
								public void run(){
									
									mCurrentPlayingPosition = control.getProgress();
									
									if ((mCurrentPlayingPosition - m_pre_play_star_timepoint) 
											>= m_per_star_dunringTime){
										
										log("m_play_star_index="+m_play_star_index);
										log("LrcBitmap.m_index="+LrcBitmap.m_index);
										
										if (m_play_star_index >= LrcBitmap.m_index
												.size() - 1){
											
											//暂停
											setLastPlayPosition(m_pre_play_star_timepoint);
											control.pause();
											timerPlayStar.cancel();
											
										}else{
											m_play_star_index++;
											m_pre_play_star_timepoint = LrcBitmap.m_current_time_point
											.get(m_play_star_index);
											m_per_star_dunringTime = LrcBitmap.m_per_star_time
											.get(m_play_star_index);
											
											control.seekTo(m_pre_play_star_timepoint);
										}
									}
								}
							}, 500, 600);							
						}						
					}else{
						if(control.getPlayerStatus() == PlayerStatus.RUN){
							control.pause();
						} else {
							control.continuing();
						}	
					}
					break;
			}
		}		
	}

	/**初始化所有组件*/
	private  void initViews(){
		
		mTv_currentMusicTitle=(TextView)findViewById(R.id.study_tv_title);		
		mBtn_playMode = (ImageButton) findViewById(R.id.btnPlayMode);
		mBtn_Playback = (ImageButton) findViewById(R.id.btnPlayback);
		mBtn_Playselect = (ImageButton) findViewById(R.id.btnPlayselect);
		mBtn_PlayNext = (ImageButton) findViewById(R.id.btnPlayNext);
		mBtn_musicList = (ImageButton) findViewById(R.id.btnMusicList);
		
		mRepeateLayout = (LinearLayout) findViewById(R.id.playmodetable);
		mTv_playorder = (TextView) findViewById(R.id.playorder);
		mTv_repeateone = (TextView) findViewById(R.id.repeateone);
		mTv_repeateall = (TextView) findViewById(R.id.repeateall);
		mTv_playone = (TextView) findViewById(R.id.playone);
		
		lrcShow=(LrcSurfaceView)findViewById(R.id.LyricShow);
		mTv_searchedWord = (TextView) findViewById(R.id.tv_searched_word);
		mTv_btn_addNewWord = (ImageButton) findViewById(R.id.btn_addNewWord);
	
		mBtn_star = (ImageButton) findViewById(R.id.btnStar);
		mBtn_musicPrev = (ImageButton) findViewById(R.id.btnPrev);
		mBtn_musicPlay = (ImageButton) findViewById(R.id.btnPlay);
		mBtn_musicNext = (ImageButton) findViewById(R.id.btnNext);
		mBtn_starCheckState = (ImageButton) findViewById(R.id.btnCheckState);
		
		mControllfont = (RadioGroup) findViewById(R.id.controllfont);
		mBig = (RadioButton) findViewById(R.id.big);
		mMiddle = (RadioButton) findViewById(R.id.middle);
		mSmall = (RadioButton) findViewById(R.id.small);
		mSearchWord = (TextView) findViewById(R.id.searchword);
		mNotebook = (TextView) findViewById(R.id.notebook);
		
		mSeekBar=(SeekBar)findViewById(R.id.seekBar_musicProgress);
	
		mTv_musicCurrentTime = (TextView) findViewById(R.id.tv_musicCurrentTime);
		mTv_musicDurationTime = (TextView) findViewById(R.id.tv_musicDurationTime);
		
		
		BtnOnClickListener listener = new BtnOnClickListener();
		//mControllfont.setOnCheckedChangeListener(this);
		mBtn_playMode.setOnClickListener(listener);
		mBtn_Playback.setOnClickListener(listener);
		mBtn_Playselect.setOnClickListener(listener);
		mBtn_PlayNext.setOnClickListener(listener);
		mBtn_musicList.setOnClickListener(listener);
		
		mTv_playorder.setOnClickListener(listener);
		mTv_repeateone.setOnClickListener(listener);
		mTv_repeateall.setOnClickListener(listener);
		mTv_playone.setOnClickListener(listener);
		
		mTv_btn_addNewWord.setOnClickListener(listener);
		mSearchWord.setOnClickListener(listener);
		mNotebook.setOnClickListener(listener);
		
		PlayBtnOnClickListener playBtnListener = new PlayBtnOnClickListener();
		mBtn_musicPrev.setOnClickListener(playBtnListener);
		mBtn_musicPlay.setOnClickListener(playBtnListener);
		mBtn_musicNext.setOnClickListener(playBtnListener);
		
		mBtn_star.setOnClickListener(listener);
		mBtn_starCheckState.setOnClickListener(listener);
		
		MySeekBarChangeListener seekBarChangeListener = new MySeekBarChangeListener();
		mSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
		lrcShow.setActiviyHandler(handlerForLrc);
	}

	/**1.绑定服务  2.给lrcShow设置监听*/
	private void toBindService(){		
        mServiceConnection=new ServiceConnection() {
			@Override
			public void onServiceConnected(ComponentName name, IBinder binder) {
				log("serviceConnected success");
				control=(AudioService.Control)binder;
				//control.setmList(mList);
			    control.setPosition(mCurrentMusicPosition);
		        lrcSurfaceViewOnCreateListener=new LrcSurfaceViewOnCreateListener() {
					@Override
					public void onCreate() {
						log("========bind Success， setLrcFile");
						//setLrcFile();
					}
				};
				lrcShow.setLrcSurfaceViewOnCreateListener(lrcSurfaceViewOnCreateListener);
				mProgressDialog.dismiss();
				log("start play");
				play();
			}			
			@Override
			public void onServiceDisconnected(ComponentName name) {
			}		
		};
		
		//绑定服务
		log("binding AudioService...");
        Intent intent=new Intent(this, AudioService.class);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
	}	
	
	private void setLrcFile() {
		 //从Service获得当前状态,是否播放,有没有歌曲, 如果有正在播放的歌曲,得到歌词
        int status = control.getPlayerStatus(); 
        File lrcFile = null;
    	//只要不是空闲状态,说明已经有了要播放的歌曲   那么就获得歌词  默认为歌曲路径下的同名歌词
    	//从service中获得歌词文件
        lrcFile = control.getPlayLrcFile();
        //让LrcSurfaceView显示歌词
        lrcShow.setLrcFile(lrcFile); 
	}
	
	/**主界面一直运行的线程  用来维护主界面中的进度*/
	private void startThread(){
		timer=new Timer();
        timer.schedule(new TimerTask() {
			@Override
			public void run() {
				
				//当滑动歌词的时候，不更新界面
				if(lrcShow.isLrcViewControlScroll())
					return;
				
				if(control!=null && isSeekBarMove!=true){					
					lrcShow.setTime(control.getProgress());
					
					if(first){
						durationHandler.sendEmptyMessage(0);					
						first = false;
					}
					
					try {
						mSeekBar.setMax(control.getMaxLength());
						mSeekBar.setProgress(control.getProgress());
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(control.isPlayerCompleted()){
						completedHander.sendEmptyMessage(0);
					}
				}
			}
		}, 0, 80);
	}
	
	@Override
    protected void onDestroy(){
		ArrayList<File> musicList = getData();
		if (musicList.size() > 0){
			setLastPath(musicList.get(mCurrentMusicPosition).getPath());
			setLastPosition(mCurrentMusicPosition);
			setLastPlayPosition(mCurrentPlayingPosition);
		}
		LLFDic.free();
    	timer.cancel();//停止一直运行线程 维护进度的线程
    	if(lrcShow.drawThread!=null)
    		lrcShow.drawThread.die();
    	unbindService(mServiceConnection);
    	if(mModeBroadcastReceiver!=null)
    		unregisterReceiver(mModeBroadcastReceiver);
    	if(mWordReceiver!=null)
    		unregisterReceiver(mWordReceiver);
    	super.onDestroy();
    }

    private BroadcastReceiver mWordReceiver =  new BroadcastReceiver(){
		public void onReceive(Context context, Intent intent){
			// TODO Auto-generated method stub
			if (intent.getAction().equals(BOARDCAST_ACTION_GET_WORD)){
				word = intent.getStringExtra("word");
				log("word="+word);
				if(word==null){
					mTv_searchedWord.setText(getString(R.string.no));
					return;
				}
				long lIndex = LLFDic.getIndexByInput(word, LLFDic.FIND_EXACT_MATCH);
				explain = new String(LLFDic.getExplainByIndex(lIndex)); 
				
				if (explain.equals("") || explain == null){
					if (word.endsWith("ing")){
						word = word.substring(0, word.length() - 3);
					}else if(word.endsWith("ies")){
						word = word.substring(0, word.length()-3).concat("y");
					}else if(word.startsWith(".")||word.startsWith("?")
							||word.startsWith("|")||word.startsWith("-")){
						word = word.substring(1, word.length());
					}else if (word.endsWith("d") || word.endsWith("s")
							||word.endsWith(".")||word.endsWith("!")
							||word.endsWith("-")||word.endsWith("?")){
						word = word.substring(0, word.length() - 1);
					}else if (word.endsWith("ed") || word.endsWith("er")
							|| word.endsWith("'s") || word.endsWith("es")){
						word = word.substring(0, word.length() - 2);
					}
					
					long mIndex = LLFDic.getIndexByInput(word,
							LLFDic.FIND_EXACT_MATCH);
					explain = new String(LLFDic.getExplainByIndex(mIndex));
				}
				
				if (LrcSurfaceView.listData==null){
					mTv_searchedWord.setText("");
				}else{
					if(!TextUtils.isEmpty(explain)){
						explain = PubFunc.filterEnglish(word, explain);
						mTv_searchedWord.setText(explain);						
					} else {
						mTv_searchedWord.setText(getString(R.string.not_find_tip1)
								+word+getString(R.string.not_find_tip2));
					}
				}
			}
		}
	};

	public String toTime(int time) {
		time /= 1000;
		int minute = time / 60;
		int hour = minute / 60;
		int second = time % 60;
		minute %= 60;
		return String.format("%02d:%02d", minute, second);
	}
	
	private void addNewWord(String word){
		
		if (word.toString().trim() != null && !word.toString().trim().equals("")){
			if (m_wordsbook == null){
				m_wordsbook = new NewWordsActivity();
			}
			if (LrcSurfaceView.m_base == null){
				LrcSurfaceView.m_base = new MyDatabase(this);
			}
			String str = "";
			byte[] words_phonetic = str.getBytes();
			int isSucess = m_wordsbook.getTransferData(LrcSurfaceView.m_base, 0, word,
					words_phonetic, explain);
			if (isSucess == 0){
				Toast.makeText(this, 
								getResources().getString(R.string.added),
								Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(this,
								getResources().getString(R.string.addsuccess),
								Toast.LENGTH_SHORT).show();
				LrcSurfaceView.listData.add(word);

			}
			if (LrcSurfaceView.m_base != null)
			{
				LrcSurfaceView.m_base.close();
				LrcSurfaceView.m_base = null;
			}
		}
	}
	
	private void changePlayMode(){
		int current = getPlayMode();
		if (current == MODE_SINGE){
			setPlayMode(MODE_SINGE_R);
			mBtn_playMode.setBackgroundResource(R.drawable.icon_playmode_repeat_single);
			Toast.makeText(this,getResources().getString(R.string.repeateone),
					Toast.LENGTH_SHORT).show();
		}else if (current == MODE_SINGE_R){
			setPlayMode(MODE_REPEAT);
			mBtn_playMode.setBackgroundResource(R.drawable.icon_playmode_repeat);
			Toast.makeText(this,
					getResources().getString(R.string.repeateall),
					Toast.LENGTH_SHORT).show();
		}else if (current == MODE_REPEAT){
			setPlayMode(MODE_ORDER);
			mBtn_playMode.setBackgroundResource(R.drawable.icon_playmode_normal);
			Toast.makeText(this,
					getResources().getString(R.string.playorder),
					Toast.LENGTH_SHORT).show();
		}else if (current == MODE_ORDER){
			setPlayMode(MODE_SINGE);
			mBtn_playMode.setBackgroundResource(R.drawable.icon_playmode_shuffle);
			Toast.makeText(this,
					getResources().getString(R.string.playone),
					Toast.LENGTH_SHORT).show();
		}
	}
	
	private void setPlayMode(int mode){
		SharedPreferences preference = getSharedPreferences(SHARED_NAME,
				Context.MODE_PRIVATE);
		Editor editor = preference.edit();
		editor.putInt(PLAY_MODE, mode);
		editor.commit();
	}
	private int getPlayMode(){
		SharedPreferences preference = getSharedPreferences(SHARED_NAME,
				Context.MODE_PRIVATE);
		int mode = preference.getInt(PLAY_MODE, MODE_ORDER);
		return mode;
	}
	
	private BroadcastReceiver mModeBroadcastReceiver =new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			
			if(AudioService.TRICK_NOW.equals(intent.getAction())){
				mTv_currentMusicTitle.setText(intent.getStringExtra("trick"));
				return;
			}
			ArrayList<File> musicList = getData();
			switch (getPlayMode()) {
			case AudioActivity.MODE_ORDER://顺序播放
				control.nex();
				if (musicList.size() > 0){
					if (mCurrentMusicPosition < musicList.size() - 1){
						mCurrentMusicPosition++;
						File file = musicList.get(mCurrentMusicPosition);
						control.setPosition(mCurrentMusicPosition);
						
						if(file.getPath().equals(getLastPath())){
							mCurrentPlayingPosition = getLastPlayPosition();
						}else{
							mCurrentPlayingPosition = 0;
						}
						control.play(file.getPath(), mCurrentPlayingPosition);
						lrcShow.setLongClickable(true);
						lrcShow.setClickable(true);
						setLrcFile();
					}
				}
				break;
	
			case AudioActivity.MODE_REPEAT://全部循环
				control.nex();
				if (musicList.size() > 0){
					if (mCurrentMusicPosition < musicList.size() - 1){
						mCurrentMusicPosition++;
					}else{
						mCurrentMusicPosition = 0;
					}
					play();
				}
				break;
			case AudioActivity.MODE_SINGE://单曲播放
				
				break;
			case AudioActivity.MODE_SINGE_R://单曲循环
				control.nex();
				if (musicList.size() > 0){
					File file = musicList.get(mCurrentMusicPosition);
					control.setPosition(mCurrentMusicPosition);
					
					if(file.getPath().equals(getLastPath())){
						mCurrentPlayingPosition = getLastPlayPosition();
					}else{
						mCurrentPlayingPosition = 0;
					}
					control.play(file.getPath(), mCurrentPlayingPosition);
					lrcShow.setLongClickable(true);
					lrcShow.setClickable(true);
					setLrcFile();
				}
				break;
			}
			
			mTv_musicDurationTime.setText(toTime(control.getMaxLength()));
			if(control.getPlayerStatus()==AudioService.PlayerStatus.RUN){
				mBtn_musicPlay.setBackgroundResource(R.drawable.xml_playback_btn_pause);
			}else{
				mBtn_musicPlay.setBackgroundResource(R.drawable.xml_playback_btn_play);
			}
		}
	};
	
	private int timesTouch;
	private long currentTime1 = 0;
	private long currentTime2 = 0;
	private boolean isFirstTime = true;
	/**???*/
	private boolean startFunction(){
		timesTouch++;
		if (1 == timesTouch){
			currentTime1 = System.currentTimeMillis();
			if (isFirstTime){
				isFirstTime = false;
				return true;
			}else{
				if (Math.abs(currentTime2 - currentTime1) > 800){
					return true;
				}
			}
		}else if (2 == timesTouch){
			currentTime2 = System.currentTimeMillis();
			timesTouch = 0;
			if (Math.abs(currentTime2 - currentTime1) > 800){
				return true;
			}
		}
		return false;
	}	
	
	public String getLastPath(){
		SharedPreferences preference = getSharedPreferences(SHARED_NAME,
				Context.MODE_PRIVATE);
		String path = preference.getString(LASTPATH, "");
		return path;
	}

	private void setLastPath(String path){
		SharedPreferences preference = getSharedPreferences(SHARED_NAME,
				Context.MODE_PRIVATE);
		Editor editor = preference.edit();
		editor.putString(LASTPATH, path);
		editor.commit();
	}
	
	/**最后一次播放的歌曲*/
	public int getLastPosition(){
		SharedPreferences preferences = getSharedPreferences(SHARED, Context.MODE_PRIVATE);
		int position = preferences.getInt(LASTPOSITION, 0);
		return position;
	}

	/**最后一次播放的歌曲*/
	private void setLastPosition(int position){
		SharedPreferences preference = getSharedPreferences(SHARED, Context.MODE_PRIVATE);
		Editor editor = preference.edit();
		editor.putInt(LASTPOSITION, mCurrentMusicPosition);
		editor.commit();
	}
	
	public int getLastPlayPosition(){
		SharedPreferences preferences = getSharedPreferences(LASTPLAYPOSITION, Context.MODE_PRIVATE);
		int position = preferences.getInt(LASTPLAY, 0);
		return position;
	}
	
	private void setLastPlayPosition(int lastplayposition){
		SharedPreferences preference = getSharedPreferences(LASTPLAYPOSITION,
				Context.MODE_PRIVATE);
		Editor editor = preference.edit();
		editor.putInt(LASTPLAY, mCurrentPlayingPosition);
		editor.commit();
	}
	
	private ArrayList<File> getData(){
		ArrayList<File> fileList = AudioService.getFolder(filePath);
		for (int i = 0; i < fileList.size(); i++){
			if (fileList.get(i).isDirectory()){
				fileList.remove(fileList.get(i));
				i = i - 1;
			}
		}
		PubFunc.sortFileListInfo(fileList);
		return fileList;
	}
	
	private void log(String log){
		Log.i("AudioActivity", log+"");
	}
}




