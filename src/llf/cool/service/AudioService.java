package llf.cool.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import llf.cool.data.Music;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;

public class AudioService extends Service{
	
	public static final String MODE_PLAY="MODE_RUN";
	public static final String TRICK_NOW="TRICK_NOW";
	public static final String MUSIC_PATH = "/sdcard/yingyutt/";
	public static final String DIC_FILE_PATH = AudioService.MUSIC_PATH
												+ "dic/llf_chn_eng.dic";
	public static final String DIC_CHI_FILE_PATH = AudioService.MUSIC_PATH
												+ "dic/llf_eng_chn.dic";
	public static int PLAYER_STATUS = 0; 
	
	private Control control;
	private MediaPlayer player;
	private boolean isCompleted;
	private int playerStatus;
	private int mCurrentMusic=0;
	private int mCurrentPlayingPosition = 0;
	private File lrcFile=null;//歌词文件

	private static ArrayList<File> mFileList = new ArrayList<File>();
	private static ArrayList<Music> mList = new ArrayList<Music>();
	private static ArrayList<Music> musicList = new ArrayList<Music>();
	@Override
	public void onCreate(){
		super.onCreate();		
		log("Service onCreate");
		playerStatus=PlayerStatus.IDLE;
		PLAYER_STATUS = playerStatus;
		player=new MediaPlayer();
		player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				playerStatus=PlayerStatus.IDLE;
				player.reset();
				isCompleted=true;
				
				if(control==null)
					return;
//				if(mList==null)
//					return;			
				sendModeBroadcast();
			}
		});
	}

	@Override
	public IBinder onBind(Intent intent) {
		log("return control extents Binder");
		control = new Control();
		return control;
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		log("Unbind service");
		return super.onUnbind(intent);
	}

	@Override
	public void onDestroy() {
		System.out.println("Service-onDestroy");
		player.release();
		player=null;
		super.onDestroy();
	}
	
	public class Control extends Binder{
		
		/**获取当前状态  PlayerStatus中定义的状态*/
		public int getPlayerStatus(){
			return playerStatus;
		}		
		public void setmList(ArrayList<Music> list){
			mList=list;
		}
		
		public ArrayList<Music> getmList(ArrayList<Music> list){
			return mList;
		}
		
		public void setPosition(int position){
			mCurrentMusic=position;			
		}
		
		/**得到当前音乐序号*/
		public int  getPosition(){
			return mCurrentMusic;			
		}	
		
		/**是否播放完成*/
		public boolean isPlayerCompleted(){
			return isCompleted;
		}		
		public void setPlayerCompleted(){
			 isCompleted=false;
		}		
		/**得到当前播放进度*/
		public int getProgress(){
			if(player==null) 
				return 0;
			if(playerStatus==PlayerStatus.RUN
					||playerStatus==PlayerStatus.PAUSE){
				return player.getCurrentPosition();
			}else{
				return 0;
			}
		}		
		/**得到歌曲总时长*/
		public int getMaxLength(){
			if(playerStatus==PlayerStatus.RUN
					||playerStatus==PlayerStatus.PAUSE){
				return player.getDuration();
			}else{
				return 1000;
			}
		}		
		/**控制歌曲进度*/
		public void seekTo(int progess){
			if(playerStatus==PlayerStatus.RUN
					||playerStatus==PlayerStatus.PAUSE){
				player.seekTo(progess);
			}
		}		
		public void pre() {
			player.reset();
			playerStatus=PlayerStatus.IDLE;
		}		
		public void nex() {
			player.reset();
			playerStatus=PlayerStatus.IDLE;
		}		
		
		public void pause(){
			if (player != null && player.isPlaying()){
				player.pause();
				playerStatus = PlayerStatus.PAUSE;
			}
		}
		
		public void continuing(){
			if (player != null && !player.isPlaying()){
				player.start();
				playerStatus = PlayerStatus.RUN;
			}
		}
		
		/**点击播放按钮*/
		public void play(String file, int currentPlayingPosition) {
			log("play playStatus="+playerStatus);
			switch (playerStatus) {
				case PlayerStatus.IDLE:
					try {
						lrcFile = new File(file.substring(0, file.lastIndexOf("."))+".lrc");
						if(!lrcFile.exists()){
							log("Lrc File is not exists!");
							lrcFile=null;
						}

						player.setDataSource(file);
						player.prepare();
						player.start();
						
						//add for remember last play position
						mCurrentPlayingPosition = currentPlayingPosition;
						if(mCurrentPlayingPosition>0){
							player.seekTo(mCurrentPlayingPosition);
						}
						
						File music = new File(file);
						sendTrickBroadcast((music.getName().substring(0, 
							music.getName().lastIndexOf("."))));						
						playerStatus=PlayerStatus.RUN;
						
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
		
					break;
	
				case PlayerStatus.PAUSE:
					player.start();
					playerStatus=PlayerStatus.RUN;
					break;
					
				case PlayerStatus.RUN:
					player.pause();
					playerStatus=PlayerStatus.PAUSE;
					break;
					
				case PlayerStatus.STOP:
					try {
						player.prepare();
						playerStatus=PlayerStatus.RUN;
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					player.start();
					break;
				}
			
			PLAYER_STATUS = playerStatus;
		}
		/**如果返回为null表示歌词文件不存在*/
		public File getPlayLrcFile() {
			return lrcFile;
		}
	}

   /**自定义 当前媒体播放器的状态*/
	public class PlayerStatus{
		public static final int IDLE=0;
		public static final int RUN=1;
		public static final int PAUSE=2;
		public static final int STOP=3;
	}

	public static void setMusicList(ArrayList<Music> musicList){
		AudioService.musicList = musicList;
	}

	public static ArrayList<Music> getMusicList(){
		return AudioService.musicList;
	}
	
	public static ArrayList<Music> getMusics(Context context, String filePath){
		log("AudioService getMusics");
		ArrayList<Music> musicList = new ArrayList<Music>();
		Cursor cursor = context.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				new String[] { MediaStore.Audio.Media.DISPLAY_NAME,
						MediaStore.Audio.Media.DURATION,
						MediaStore.Audio.Media.ARTIST,
						MediaStore.Audio.Media._ID,
						MediaStore.Audio.Media.DATA,
						MediaStore.Audio.Media.SIZE }, null, null, null);
		log("count="+cursor.getCount());
		if (cursor != null){
			while (cursor.moveToNext()){
				String path = cursor.getString(4);
				String newPath = path.substring(4, path.length());
				
				if (newPath.startsWith(filePath)){
					String startIndex = newPath.substring(filePath.length(),
							newPath.length());
					if(startIndex.indexOf("/")<0){
						startIndex= startIndex;
					}else{
						startIndex = newPath.substring(filePath.length()+1,
								newPath.length());
					}if(startIndex.indexOf("/")<0
							&&startIndex.endsWith(".mp3")){
						Music music = new Music();
						String name = cursor.getString(0);
						name = name.substring(0,
								name.toLowerCase().indexOf(".mp3"));
						music.setName(name);
						music.setDuration(cursor.getString(1));
						music.setArtist(cursor.getString(2));
						music.setPath(path);
						music.setSize(cursor.getString(5));
						music.setId(String.valueOf(musicList.size())); // index
						musicList.add(music);
					}
				}
			}
		}
		return musicList;
	}
	
	public static ArrayList<File> getFolder(String path){
		ArrayList<File> folder = new ArrayList<File>();
		File file = new File(path);
		if (file.isDirectory()){
			File array[] = file.listFiles();
			for (int i = 0; i < array.length; i++){
				if (array[i].isDirectory()
						|| (array[i].isFile() && array[i].getName().endsWith(
								".mp3"))){
					folder.add(array[i]);
				}
			}
		}
		return folder;
	}
	
	public static ArrayList<File> getFile(String path){
		ArrayList<File> folder = new ArrayList<File>();
		File file = new File(path);
		if (file.isDirectory()){
			File array[] = file.listFiles();
			for (int i = 0; i < array.length; i++){
				if ((array[i].isFile() && array[i].getName().endsWith(
								".mp3"))){
					folder.add(array[i]);
				}
			}
		}
		return folder;
	}
	public static ArrayList<File> getFileList(){
		return mFileList;
	}

	public static void setFileList(ArrayList<File> fileList){
		mFileList = fileList;
	}
	
	public void sendModeBroadcast(){
	   Intent intent = new Intent(MODE_PLAY);
	   sendBroadcast(intent);	   
   }
   
   public void sendTrickBroadcast(String trick){
	   Intent intent=new Intent(TRICK_NOW);
	   intent.putExtra("trick", trick);
	   sendBroadcast(intent);	   
   }
   
	private static void log(Object log){
		Log.i("AudioService", log+"");
	}

}













