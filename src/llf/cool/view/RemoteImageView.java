package llf.cool.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import llf.cool.CoolApplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;

public class RemoteImageView extends ImageView{

	private final static String TT_CACHE_DIR = "Android/data/com.llf.cool";
	
	private final static int DAYS_OF_CACHE = 30; //每30天删一次缓存
	private final static int MAX_FAILURES = 3;   //最大抓取失败次数	
	private final static int FREE_TO_SAVE_SD = 10;
	private final static int MB = 1*1024*1024;
	
	private long mTimeDiff; //删除缓存的时间
	private String mUrl;    //远程图片地址
	private String mCurrentlyGrabbedUrl; //当前被成功抓取的url
	private int mFailure;   //远程图片抓取失败次数
	private Integer mDefaultImageId = 0; 
	
	//init value diffrent that possible values of mCacheSize
	private static int mPrevCacheSize= 1;
	private static int mCacheSize= 150;
	
	private Context mContext;
	private int mPosition;      //cover 所在的 row position
	private ListView mListView; //cover 所在的 ListView
	
	public void setDefaultImage(Integer resId){
		mDefaultImageId = resId;
	}
	
	public void setImageUrl(String url, String iconName, int position, ListView listView) {
		// TODO Auto-generated method stub
		mPosition = position;
		mListView = listView;
		setImageUrl(url, iconName);
	}
    
    public void setImageUrl(String url, String iconName){
    	
    	log("分类封面图片下载地址 = "+ url);
    	
    	if(mUrl != null && mUrl.equals(url) && (mCurrentlyGrabbedUrl == null ||
    			(mCurrentlyGrabbedUrl != null && mCurrentlyGrabbedUrl.equals(url)))){
    		mFailure++;
    		if(mFailure > MAX_FAILURES){
    			Log.e("RemoteImageView", "下载失败 "+url+", then falling back to default imgage");
    			loadDefaultImage();
    		}
    	} else {
    		mUrl = url;
    		mFailure = 0;
    	}
    	
    	updateCacheSize();
    	
		if (mCacheSize>0) {
			
			String directory = getDirection();
			String fileName = iconName;			
			String dirFileName = directory + "/" +fileName;
			log(">>>dirFileName="+dirFileName);
			Bitmap bitmap  = BitmapFactory.decodeFile(dirFileName);
			if(bitmap == null){ //没有在 缓存目录dirPath 下找到该 fileName(=iconName);
				log(">>>缓存中没找到分类封面图片, so to download");
				new DownLoadCoverTask().execute(url, iconName);
			} else {
				log(">>>缓存中已有封面图片 ，直接设置。");
				RemoteImageView.this.setImageBitmap(bitmap);
			}
		}	
    }
    
    /**
     * 缓存目录
     * @return
     */
    private String getDirection() {
		// TODO Auto-generated method stub
		String extSD = Environment.getExternalStorageDirectory().toString();
		String direction = extSD + "/" + TT_CACHE_DIR;
		File dirFile = new File(direction);
		dirFile.mkdirs();  /* 创建此抽象路径名指定的目录,包括创建必需但不存在的父目录.**/
		
		direction = dirFile + "/files";
		dirFile = new File(direction);
		dirFile.mkdir();   /* 创建此抽象路径名指定的目录, 只能创建最下面的一级目录*/
		return direction;
	}


	private class DownLoadCoverTask extends AsyncTask<String, Void, String>{ //Params, Progress, Result
		String mTaskUrl = null;
		String mIconName = null;
		Bitmap mBitmap = null;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			//下载前先显示默认的icon图片
			loadDefaultImage();
			super.onPreExecute();
		}
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			InputStream steam = null;
			URL taskUrl = null;
			Bitmap bmp = null;
			
			mTaskUrl = params[0];
			mIconName = params[1];	
			
			try {
				taskUrl = new URL(mTaskUrl);
				
				try {
					//下载图片
					steam = taskUrl.openStream();
					bmp = BitmapFactory.decodeStream(steam);
					try {
						if(bmp != null){  //成功
							mBitmap = bmp;
							//将图片保存在缓存中
							loge("缓存图片...");
							loge("mTaskUrl="+mTaskUrl);
							loge("bmp =" + bmp);
							CoolApplication.getInstance().getImageCache().put(mTaskUrl, bmp);
						} else {          //失败
							loge("下载图片失败？");
						}
					} catch (NullPointerException e) {
						// TODO: handle exception
						e.printStackTrace();
						log("缓存失败");
					}
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					loge("---------------DownLoadCoverTask------------");
					loge("无法从给定的地址下载图片："+ mTaskUrl);
				} finally {
					if(steam != null){
						try {
							steam.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return mTaskUrl;
		}
		
		@Override
		protected void onPostExecute(String url) {
			// TODO Auto-generated method stub
			super.onPostExecute(url);
			log("onPostExecute");
			// 如果下载地址变化了， 则将上一次的图片保存在SD卡中
			if(!mTaskUrl.equals(mUrl)){
				saveToSDCachePath(mBitmap, mIconName);
			}
			Bitmap bmp = CoolApplication.getInstance().getImageCache().get(url);
			log("bmp="+bmp);
			if(bmp == null){
				//重新下载
				
				log("bmg=null, 重新下载");
				RemoteImageView.this.setImageUrl(url, mIconName);
			} else{
				//只有当image所属的row是可见的时候， 才更新设置图片
				log("mListView="+mListView);
				
				if(mListView != null)
					if(mPosition > mListView.getLastVisiblePosition() 
							|| mPosition < mListView.getFirstVisiblePosition())
						return;
					
				//设置远程图片
				log("给RemoteImageView设置图片，bmg="+bmp);
				RemoteImageView.this.setImageBitmap(bmp);
				mCurrentlyGrabbedUrl = url;						
				
				//保存到SD卡缓存目录
				saveToSDCachePath(bmp, mIconName);
				
			}
		}		
	}

	private void saveToSDCachePath(Bitmap bmp, String iconName) {
		// TODO Auto-generated method stub
		if(bmp == null) return;
		if(mCacheSize == 0) return;
		if(FREE_TO_SAVE_SD > freeSpaceInSd()) return;
			
		String directory = getDirection();
		String fileName = iconName;			
		File file = new File(directory +"/"+ fileName);
		log("保存为jpg图片  路径："+(directory +"/"+ fileName));
		try {
			//Creates a new, empty file
			file.createNewFile();
			OutputStream outStream = new FileOutputStream(file);
			//true if successfully compressed to the specified stream.
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, outStream); 
			outStream.flush(); //刷新此输出流并强制写出所有缓冲的输出字节
			outStream.close(); //关闭此输出流并释放与此流有关的所有系统资源
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	private int freeSpaceInSd() {
		// TODO Auto-generated method stub
		StatFs fs = new StatFs(Environment.getExternalStorageDirectory().getPath());
		double sdFreeMB = ((double) fs.getAvailableBlocks() 
							* (double) fs.getBlockSize()) 
							 / MB;
		return (int)sdFreeMB;
	}

	private void loadDefaultImage() {
    	// TODO Auto-generated method stub
    	if(mDefaultImageId != null){
    		this.setImageResource(mDefaultImageId);
    	}
    }

	private void updateCacheSize() {
		// TODO Auto-generated method stub
		mPrevCacheSize = mCacheSize;
		//从设置中取得的缓存大小
		mCacheSize = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(mContext)
				.getString("cache_option", "100"));
		//当我们改变缓存的大小为0时
		if(mPrevCacheSize != 0 && mCacheSize == 0){
			clearCache();
		}	
	}

	private void clearCache() {
		// TODO Auto-generated method stub
		String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
		log("extStorageDirectory : "+extStorageDirectory);
		
		String dirPath = extStorageDirectory + "/" + TT_CACHE_DIR + "/files";
		//找点指定目录下的一个文件(夹）
		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		
		//缓存为空， 也可能是SD卡为未挂载状态
		if(files == null){			
			return;
		} 
		
		//清楚缓存文件
		for(File f : files){
			f.delete();
		}
	}

	private void log(String str){
		Log.i("RemoteImageView", str);
	}
	
	private void loge(String str){
		Log.e("RemoteImageView", str);
	}
	
	private void init() {		
		mTimeDiff = DAYS_OF_CACHE * 24 * 60 * 60 * 1000L;
	}
	
	public RemoteImageView(Context context) {
		super(context);
		mContext = context;
		init();
	}
	
	public RemoteImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}
	
	public RemoteImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}
}
