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
	
	private final static int DAYS_OF_CACHE = 30; //ÿ30��ɾһ�λ���
	private final static int MAX_FAILURES = 3;   //���ץȡʧ�ܴ���	
	private final static int FREE_TO_SAVE_SD = 10;
	private final static int MB = 1*1024*1024;
	
	private long mTimeDiff; //ɾ�������ʱ��
	private String mUrl;    //Զ��ͼƬ��ַ
	private String mCurrentlyGrabbedUrl; //��ǰ���ɹ�ץȡ��url
	private int mFailure;   //Զ��ͼƬץȡʧ�ܴ���
	private Integer mDefaultImageId = 0; 
	
	//init value diffrent that possible values of mCacheSize
	private static int mPrevCacheSize= 1;
	private static int mCacheSize= 150;
	
	private Context mContext;
	private int mPosition;      //cover ���ڵ� row position
	private ListView mListView; //cover ���ڵ� ListView
	
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
    	
    	log("�������ͼƬ���ص�ַ = "+ url);
    	
    	if(mUrl != null && mUrl.equals(url) && (mCurrentlyGrabbedUrl == null ||
    			(mCurrentlyGrabbedUrl != null && mCurrentlyGrabbedUrl.equals(url)))){
    		mFailure++;
    		if(mFailure > MAX_FAILURES){
    			Log.e("RemoteImageView", "����ʧ�� "+url+", then falling back to default imgage");
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
			if(bitmap == null){ //û���� ����Ŀ¼dirPath ���ҵ��� fileName(=iconName);
				log(">>>������û�ҵ��������ͼƬ, so to download");
				new DownLoadCoverTask().execute(url, iconName);
			} else {
				log(">>>���������з���ͼƬ ��ֱ�����á�");
				RemoteImageView.this.setImageBitmap(bitmap);
			}
		}	
    }
    
    /**
     * ����Ŀ¼
     * @return
     */
    private String getDirection() {
		// TODO Auto-generated method stub
		String extSD = Environment.getExternalStorageDirectory().toString();
		String direction = extSD + "/" + TT_CACHE_DIR;
		File dirFile = new File(direction);
		dirFile.mkdirs();  /* �����˳���·����ָ����Ŀ¼,�����������赫�����ڵĸ�Ŀ¼.**/
		
		direction = dirFile + "/files";
		dirFile = new File(direction);
		dirFile.mkdir();   /* �����˳���·����ָ����Ŀ¼, ֻ�ܴ����������һ��Ŀ¼*/
		return direction;
	}


	private class DownLoadCoverTask extends AsyncTask<String, Void, String>{ //Params, Progress, Result
		String mTaskUrl = null;
		String mIconName = null;
		Bitmap mBitmap = null;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			//����ǰ����ʾĬ�ϵ�iconͼƬ
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
					//����ͼƬ
					steam = taskUrl.openStream();
					bmp = BitmapFactory.decodeStream(steam);
					try {
						if(bmp != null){  //�ɹ�
							mBitmap = bmp;
							//��ͼƬ�����ڻ�����
							loge("����ͼƬ...");
							loge("mTaskUrl="+mTaskUrl);
							loge("bmp =" + bmp);
							CoolApplication.getInstance().getImageCache().put(mTaskUrl, bmp);
						} else {          //ʧ��
							loge("����ͼƬʧ�ܣ�");
						}
					} catch (NullPointerException e) {
						// TODO: handle exception
						e.printStackTrace();
						log("����ʧ��");
					}
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					loge("---------------DownLoadCoverTask------------");
					loge("�޷��Ӹ����ĵ�ַ����ͼƬ��"+ mTaskUrl);
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
			// ������ص�ַ�仯�ˣ� ����һ�ε�ͼƬ������SD����
			if(!mTaskUrl.equals(mUrl)){
				saveToSDCachePath(mBitmap, mIconName);
			}
			Bitmap bmp = CoolApplication.getInstance().getImageCache().get(url);
			log("bmp="+bmp);
			if(bmp == null){
				//��������
				
				log("bmg=null, ��������");
				RemoteImageView.this.setImageUrl(url, mIconName);
			} else{
				//ֻ�е�image������row�ǿɼ���ʱ�� �Ÿ�������ͼƬ
				log("mListView="+mListView);
				
				if(mListView != null)
					if(mPosition > mListView.getLastVisiblePosition() 
							|| mPosition < mListView.getFirstVisiblePosition())
						return;
					
				//����Զ��ͼƬ
				log("��RemoteImageView����ͼƬ��bmg="+bmp);
				RemoteImageView.this.setImageBitmap(bmp);
				mCurrentlyGrabbedUrl = url;						
				
				//���浽SD������Ŀ¼
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
		log("����ΪjpgͼƬ  ·����"+(directory +"/"+ fileName));
		try {
			//Creates a new, empty file
			file.createNewFile();
			OutputStream outStream = new FileOutputStream(file);
			//true if successfully compressed to the specified stream.
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, outStream); 
			outStream.flush(); //ˢ�´��������ǿ��д�����л��������ֽ�
			outStream.close(); //�رմ���������ͷ�������йص�����ϵͳ��Դ
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
		//��������ȡ�õĻ����С
		mCacheSize = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(mContext)
				.getString("cache_option", "100"));
		//�����Ǹı仺��Ĵ�СΪ0ʱ
		if(mPrevCacheSize != 0 && mCacheSize == 0){
			clearCache();
		}	
	}

	private void clearCache() {
		// TODO Auto-generated method stub
		String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
		log("extStorageDirectory : "+extStorageDirectory);
		
		String dirPath = extStorageDirectory + "/" + TT_CACHE_DIR + "/files";
		//�ҵ�ָ��Ŀ¼�µ�һ���ļ�(�У�
		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		
		//����Ϊ�գ� Ҳ������SD��Ϊδ����״̬
		if(files == null){			
			return;
		} 
		
		//��������ļ�
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
