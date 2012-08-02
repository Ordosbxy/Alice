package llf.cool.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import llf.cool.activity.AudioActivity;
import llf.cool.data.MyDatabase;
import llf.cool.service.AudioService;
import llf.cool.util.LrcBitmap;
import llf.cool.util.LrcStr;
import llf.cool.util.LyricUtil;
import llf.dictionary.engine.LLFDic;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/***
 * 歌词显示view
 */
public class LrcSurfaceView extends SurfaceView {
	
	private static final int FACTOR = 1;//放大倍数
	private static final int RADIUS = 90;//放大镜的半径	
	
	public DrawThread drawThread;//绘制图片的线程
	public static boolean isControl=false;//正在被控制  手指滑动歌词图片
	public static ArrayList<String> listData = null;
	public static MyDatabase m_base = null;
	
	private int mZOOx=-80;
	private int mZOOy=-80;
	private float y;//歌词图片的y轴坐标 相对屏幕坐标系
	private long currentControlPosition=0;
	private long currentTime;//当前定位到的时间  手指滑动歌词图片 得到的歌曲应该到的时间
	private boolean isInit=false;//用来限制 只初始化一次 init方法只运行一次
	private boolean isPrepare=false;
	private boolean isSurfaceViewOnCreate=false;//是否已经调用了surfaceCreated方法
	private boolean isRun=false;
	private boolean isLongPress=false;
	private boolean isAnimation=false;//动画是否启动  在自动滚动歌词使用
	private boolean isPlaying=false; //判断歌曲是否正在播放
	private boolean isLrcFileExist=false;
	
	private Context mContext;
	private Paint mPaint=new Paint();
	private Matrix matrix = new Matrix();
	private File LrcFile;//歌词文件
	private Bitmap lrcBitmap;//歌词图片
	private LrcBitmap lrcBitmapFactory;//歌词图片工具
	private SurfaceHolder holder;
	private BitmapShader shader;
	private ShapeDrawable drawable;
	private Handler activiyHandler;//用来给activity发消息 告诉进度 手指滑动歌词图片用 
	private LrcSurfaceViewOnCreateListener onCreateListenr;//监听surfaceCreated方法
	
	
	public LrcSurfaceView(Context context1, AttributeSet attrs, int defStyle) {
		super(context1, attrs, defStyle);
		this.mContext=context1;
		init();
	}	
	
    /*
     * 自定义控件一般写两个构造方法 LrcSurfaceView(Context mContext)用于java硬编码创建控件
     * 如果想要让自己的控件能够通过xml来产生就必须有第2个构造方法 LrcSurfaceView(Context context,
     * AttributeSet attrs) 因为框架会自动调用具有AttributeSet参数的这个构造方法来创建继承自View的控件
     */
	public LrcSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		init();
	}
	
	public LrcSurfaceView(Context context) {
		super(context);
		mContext=context;
		init();
	}	
	
	/**初始化工作*/
	private void init(){
		if(isInit)
			return;		
		isInit=true;
		
		holder= this.getHolder();
		holder.addCallback(new SurfaceHolder.Callback() {			
			@Override
			public void surfaceCreated(final SurfaceHolder holder) {
				log("surfaceCreated");
				if(onCreateListenr!=null){
					onCreateListenr.onCreate();					
				}
				isSurfaceViewOnCreate=true;	
			}
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
			}			
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				//if(isRun)drawThread.die();
			}
		});
		
		final GestureDetector gestureDetector = new GestureDetector(new SimpleOnGestureListener(){
			@Override
			public void onLongPress(MotionEvent e) {
				isLongPress=true;
				super.onLongPress(e);
			}
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				return super.onFling(e1, e2, velocityX, velocityY);
			}
			@Override
			public boolean onScroll(MotionEvent e1, final MotionEvent e2,
					float distanceX, float distanceY) {
				y=y-distanceY;
				//mZOOx=(int)(e2.getX());mZOOy=(int)e2.getY();
				return super.onScroll(e1, e2, distanceX, distanceY);
			}
		});
		
		this.setLongClickable(true);		
		this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mZOOx=(int)(event.getX());
				mZOOy=(int)event.getY()-RADIUS;				
				switch(event.getAction()){
				
					// 当按下时发生	
					case MotionEvent.ACTION_DOWN:
						isControl=true;
						break;
						
					// 当抬起时发生 取词翻译
					case MotionEvent.ACTION_UP:
						Message msg=new Message();
						msg.what=1;
						msg.arg1=(int)currentControlPosition;
						activiyHandler.sendMessage(msg);
						isControl=false;

						new Timer().schedule(new TimerTask() {						
							@Override
							public void run() {
								if(isLongPress){
									isLongPress=false;
									String word=lrcBitmapFactory.getLrcByY(mZOOy+RADIUS, Math.abs(y), mZOOx);
									log("word="+word);
									if(word!=null){
										char first = word.charAt(0);
										if ((first > 'a' && first < 'z')
												|| (first > 'A' && first < 'Z')){
											LLFDic.init(AudioService.DIC_CHI_FILE_PATH);
										}else{
											LLFDic.init(AudioService.DIC_FILE_PATH);
										}
									}
									Intent intent = new Intent();
									intent.setAction(AudioActivity.BOARDCAST_ACTION_GET_WORD);
									intent.putExtra("word", word);
									mContext.sendBroadcast(intent);										
									
								}
							}
						}, 200);
						break;

					default:
						break;
				}
				//invalidate();
				return gestureDetector.onTouchEvent(event);
			}
		});
	}

	
    /*
     * 控件创建完成之后，在显示之前都会调用这个方法，此时可以获取控件的大小 并得到中心坐标和坐标轴圆心所在的点。
     */
    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        log("neww="+w+", oldw="+oldw);
        log("newh="+h+", oldh="+oldh );
        super.onSizeChanged(w, h, oldw, oldh);
    }
	
	/**用来调节图片中该显示正在播放哪一行歌词*/
	public void setTime(int time){
		if(!isPrepare) 
			return;
		if(!isControl &&! isLongPress && lrcBitmapFactory!=null){
			lrcBitmapFactory.setTime(time);
		}
	}

	/**设置歌词文件 从Acitivity中调用此方法,用来切换歌词文件
	 * if lrcFile=null 显示没有歌词文件,您当前正在播放... 歌曲
	 * */
	public void setLrcFile(File lrcFile) {
		if(lrcFile==null){
			//无歌词/当前要没有播放的歌曲
			if(drawThread!=null){
				drawThread.die();
			}
			isLrcFileExist=false;
			setLongClickable(false);
			setClickable(false);
			drawNoLrcFile("Lrc Not Found!");
			return;
		}else{
			isLrcFileExist=true;
		}
		if(this.LrcFile==lrcFile){
			return;
		}
		this.LrcFile=lrcFile;

		//解析歌词
		List<LrcStr> allLrc = LyricUtil.parseLrcFile(lrcFile);
		if (allLrc != null && allLrc.size() > 0){
			if (m_base == null){
				m_base = new MyDatabase(mContext);
			}
			Cursor words_cursor = m_base.selectSomeTable("newwords_table");
			listData = new ArrayList<String>();
			for (int i = 0; i < words_cursor.getCount(); i++){
				words_cursor.moveToPosition(i);
				String s = words_cursor.getString(2).toLowerCase();
				listData.add(s);
			}
			if(words_cursor!=null)
			words_cursor.close();
			if (m_base != null){
				m_base.close();
				m_base = null;
			}
		}else{
			listData = null;			
		} 
		
		try {
			if(lrcBitmapFactory!=null){
				lrcBitmapFactory.mRecycled();
				lrcBitmapFactory=null;
			}
			lrcBitmap=null;
			System.gc();
			
			lrcBitmapFactory = new LrcBitmap(LrcSurfaceView.this.getWidth(), allLrc, mContext);
			lrcBitmap = lrcBitmapFactory.getBitmap();
			
		} catch (OutOfMemoryError e) {
			if(lrcBitmapFactory!=null)
				lrcBitmapFactory.mRecycled();
			lrcBitmapFactory=null;
			System.gc();
			
			lrcBitmapFactory = new LrcBitmap(LrcSurfaceView.this.getWidth(), allLrc, mContext);
			lrcBitmap = lrcBitmapFactory.getBitmap();
			
		}
		
		//歌词图片的y轴坐标 相对屏幕坐标系
		y = getHeight()/2; 

		//放大镜
		shader=new BitmapShader(lrcBitmapFactory.getBt(), TileMode.CLAMP, TileMode.CLAMP);
		OvalShape shape=new OvalShape();//圆形的drawable
		drawable = new ShapeDrawable(shape);
		drawable.getPaint().setShader(shader);
		drawable.setBounds(0, 0, RADIUS*2, RADIUS*2);
	    
	    drawThread=new DrawThread();
	    drawThread.start();
	    
		isPrepare=true;
	}
	
	/**绘制surfaceView线程*/
	public class DrawThread extends Thread {
		
		public void die() {
			isRun=false;
			Log.e("DrawThread","die");
		}
		public void run() {
			isRun=true;
			while(isRun){
				if(lrcBitmapFactory==null)
					continue;
				if(lrcBitmapFactory.getCurrentLrc()!=null 
						&& !isControl){
					
					/**如果可以获得当前歌词对象,那么就可以当前歌词对象的位置(相对于屏幕)*/
					float currentIrcY=lrcBitmapFactory.getCurrentLrc().y+y;
					
					/**屏幕中间的y位置*/
					float centerY=LrcSurfaceView.this.getHeight()/2;
					
					/**判断  centerY 和  currentIrcY  
					 * 如果currentIrcY > centerY,那么屏幕要向上移动
					 * 实际就是 让bitmap歌词图片 向上移动(currentIrcY-centerY)
					 */
					
					//log("正在播放歌词位置="+currentIrcY+", 屏幕中央："+centerY);
					/**歌词图片 向上移动 多少距离, 滑动歌词图片， 调整歌词图片的y轴坐标*/
					startMyAnimation(currentIrcY-centerY);
				}
				
				drawSurfaceview();
				
				try {
					Thread.sleep(40);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		/**f 是歌词图片要向上移动的距离 为正数    启动动画  切换一行歌词*/
		private void startMyAnimation(final float f) {
			if(isAnimation==true)
				return;
			new Thread(){
				public void run() {
				isAnimation=true;
				//log("歌词图片要向上移动的距离 :"+f);
				for(int i=0;i<10;i++){
					y=y-f/10;
					try {
						Thread.sleep(40);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				isAnimation=false;
			}}.start();
		}//end startMyAnimation		
	}//end DrawThread

	/**绘制surfaceview*/
	private void drawSurfaceview() {
		synchronized(holder){
			Canvas canvas=holder.lockCanvas();
			if(canvas==null)			
				return;
			
			//清除垃圾
			canvas.drawColor(Color.BLACK);
			
			if(lrcBitmap==null){
				log("!!! attention please, lrcBitmap == null");
				holder.unlockCanvasAndPost(canvas);
				return;
			}
			
			if(lrcBitmap.isRecycled()){
				log("!!! attention please, lrcBitmap is Recycled");
				holder.unlockCanvasAndPost(canvas);  
				return;
			}
			
			//将歌词图片（lrcBitmap）画在画布上
			mPaint.setColor(Color.BLACK);
		    canvas.drawBitmap(lrcBitmap, 0, y, mPaint);
		    
		    //若长按，则显示放大镜，获取单词
		    if(isLongPress && LrcSurfaceView.this.isLongClickable()){  
			   String getWord = lrcBitmapFactory.getLrcByY(mZOOy+RADIUS, Math.abs(y), mZOOx);
	
			    log(("放大镜  平移  Y="+((int)((RADIUS-mZOOy+y)*FACTOR*1)-15)));
			   	matrix.setTranslate((int)(RADIUS-mZOOx*1), (int)((RADIUS-(mZOOy+RADIUS)+y)*FACTOR*1));
				drawable.getPaint().getShader().setLocalMatrix(matrix);
				//Bounds，就是那个圆的外切矩形
				drawable.setBounds(mZOOx-RADIUS, mZOOy-RADIUS-15, mZOOx+RADIUS, mZOOy+RADIUS-15);
				
				mPaint.setColor(Color.LTGRAY);
				canvas.drawRoundRect(new RectF(mZOOx-RADIUS, mZOOy-RADIUS-15, mZOOx+RADIUS, mZOOy+RADIUS-15)
										, RADIUS, RADIUS, mPaint);
				
				mPaint.setColor(Color.parseColor("#20ffffff")); //放大镜透明颜色
				canvas.drawRoundRect(new RectF(mZOOx-RADIUS, mZOOy-RADIUS-15, mZOOx+RADIUS, mZOOy+RADIUS-15)
										, RADIUS, RADIUS, mPaint);
				
				drawable.invalidateSelf();
				drawable.draw(canvas);
		    }
		    
			if(isControl){//如果手指正在滑动歌词
				mPaint.setColor(Color.GRAY);
	//			canvas.drawLine(0, getHeight()/2, getWidth(), getHeight()/2, mPaint);
				float lrcBitmapY = (getHeight()/2-y);//取得中间线所覆盖的歌词在图片中的位置 y
				log("lrcBitmapY="+lrcBitmapY+", y="+y);
				long time =lrcBitmapFactory.getTimeInLrcBitMap(lrcBitmapY);
				currentControlPosition = time;
				
				//通知歌词被滑动后的需播放的位置
				Message msg=new Message();
				msg.what=0;
				msg.arg1=(int)time;
				activiyHandler.sendMessage(msg);
						
				currentTime = time;
				
				time /= 1000;
				long minute = time / 60;
				//long hour = minute / 60;
				long second = time % 60;
				minute %= 60;
				String smallProgressHint = String.format("%02d:%02d", minute, second);
				mPaint.setTextSize(21);
				canvas.drawText(smallProgressHint, getWidth()-55, getHeight()/2, mPaint);
			}
			holder.unlockCanvasAndPost(canvas);		
		}//end synchronized
	}

	private void drawNoLrcFile(String string) {
		synchronized (holder) {
			Canvas canvas=holder.lockCanvas();
			canvas.drawColor(Color.BLACK);
			Paint paint = new Paint();
			paint.setColor(Color.BLACK);
			canvas.drawRect(new Rect(0, 0, getWidth(), getHeight()), paint);
//		canvas.drawBitmap(((BitmapDrawable)(mContext.getResources()
//							.getDrawable(R.drawable.bottom)))
//							.getBitmap(), 0, 0, paint);
			
			paint.setColor(Color.YELLOW);
			paint.setTextSize(24);
			canvas.drawText(string, getWidth()/2, getHeight()/2, paint);
			
			holder.unlockCanvasAndPost(canvas);			
		}
	}

	/**用户是否在手动控制屏幕歌词的移动*/
	public boolean isLrcViewControlScroll() {
		return isControl;
	}

	/**
	 * 从Activity给了本界面一个handler，
	 * 手指滑动歌词图片，给Activity发送需要播放音乐的时间戳 
	 */
	public void setActiviyHandler(Handler activiyHandler) {
		this.activiyHandler = activiyHandler;
	}

	/**设置歌词surfaceView surfaceCreated时要调用的方法  监听器 */
	public void setLrcSurfaceViewOnCreateListener(LrcSurfaceViewOnCreateListener onCreateListenr){
		this.onCreateListenr=onCreateListenr;
		if(isSurfaceViewOnCreate==true){//如果已经开始create 马上执行
			onCreateListenr.onCreate();
		}
	}

	 //not used
	/**用来设置是否正在播放flag*/
	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}
	
	public interface LrcSurfaceViewOnCreateListener {
		/**当LrcSurfaceView onCreate时被调用*/
		void onCreate();
	}
	
	private void log(String log){
		Log.i("LrcSurfaceView", log+"");
	}
}
