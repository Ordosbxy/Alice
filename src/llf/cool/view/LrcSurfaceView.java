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
 * �����ʾview
 */
public class LrcSurfaceView extends SurfaceView {
	
	private static final int FACTOR = 1;//�Ŵ���
	private static final int RADIUS = 90;//�Ŵ󾵵İ뾶	
	
	public DrawThread drawThread;//����ͼƬ���߳�
	public static boolean isControl=false;//���ڱ�����  ��ָ�������ͼƬ
	public static ArrayList<String> listData = null;
	public static MyDatabase m_base = null;
	
	private int mZOOx=-80;
	private int mZOOy=-80;
	private float y;//���ͼƬ��y������ �����Ļ����ϵ
	private long currentControlPosition=0;
	private long currentTime;//��ǰ��λ����ʱ��  ��ָ�������ͼƬ �õ��ĸ���Ӧ�õ���ʱ��
	private boolean isInit=false;//�������� ֻ��ʼ��һ�� init����ֻ����һ��
	private boolean isPrepare=false;
	private boolean isSurfaceViewOnCreate=false;//�Ƿ��Ѿ�������surfaceCreated����
	private boolean isRun=false;
	private boolean isLongPress=false;
	private boolean isAnimation=false;//�����Ƿ�����  ���Զ��������ʹ��
	private boolean isPlaying=false; //�жϸ����Ƿ����ڲ���
	private boolean isLrcFileExist=false;
	
	private Context mContext;
	private Paint mPaint=new Paint();
	private Matrix matrix = new Matrix();
	private File LrcFile;//����ļ�
	private Bitmap lrcBitmap;//���ͼƬ
	private LrcBitmap lrcBitmapFactory;//���ͼƬ����
	private SurfaceHolder holder;
	private BitmapShader shader;
	private ShapeDrawable drawable;
	private Handler activiyHandler;//������activity����Ϣ ���߽��� ��ָ�������ͼƬ�� 
	private LrcSurfaceViewOnCreateListener onCreateListenr;//����surfaceCreated����
	
	
	public LrcSurfaceView(Context context1, AttributeSet attrs, int defStyle) {
		super(context1, attrs, defStyle);
		this.mContext=context1;
		init();
	}	
	
    /*
     * �Զ���ؼ�һ��д�������췽�� LrcSurfaceView(Context mContext)����javaӲ���봴���ؼ�
     * �����Ҫ���Լ��Ŀؼ��ܹ�ͨ��xml�������ͱ����е�2�����췽�� LrcSurfaceView(Context context,
     * AttributeSet attrs) ��Ϊ��ܻ��Զ����þ���AttributeSet������������췽���������̳���View�Ŀؼ�
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
	
	/**��ʼ������*/
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
				
					// ������ʱ����	
					case MotionEvent.ACTION_DOWN:
						isControl=true;
						break;
						
					// ��̧��ʱ���� ȡ�ʷ���
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
     * �ؼ��������֮������ʾ֮ǰ������������������ʱ���Ի�ȡ�ؼ��Ĵ�С ���õ����������������Բ�����ڵĵ㡣
     */
    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        log("neww="+w+", oldw="+oldw);
        log("newh="+h+", oldh="+oldh );
        super.onSizeChanged(w, h, oldw, oldh);
    }
	
	/**��������ͼƬ�и���ʾ���ڲ�����һ�и��*/
	public void setTime(int time){
		if(!isPrepare) 
			return;
		if(!isControl &&! isLongPress && lrcBitmapFactory!=null){
			lrcBitmapFactory.setTime(time);
		}
	}

	/**���ø���ļ� ��Acitivity�е��ô˷���,�����л�����ļ�
	 * if lrcFile=null ��ʾû�и���ļ�,����ǰ���ڲ���... ����
	 * */
	public void setLrcFile(File lrcFile) {
		if(lrcFile==null){
			//�޸��/��ǰҪû�в��ŵĸ���
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

		//�������
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
		
		//���ͼƬ��y������ �����Ļ����ϵ
		y = getHeight()/2; 

		//�Ŵ�
		shader=new BitmapShader(lrcBitmapFactory.getBt(), TileMode.CLAMP, TileMode.CLAMP);
		OvalShape shape=new OvalShape();//Բ�ε�drawable
		drawable = new ShapeDrawable(shape);
		drawable.getPaint().setShader(shader);
		drawable.setBounds(0, 0, RADIUS*2, RADIUS*2);
	    
	    drawThread=new DrawThread();
	    drawThread.start();
	    
		isPrepare=true;
	}
	
	/**����surfaceView�߳�*/
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
					
					/**������Ի�õ�ǰ��ʶ���,��ô�Ϳ��Ե�ǰ��ʶ����λ��(�������Ļ)*/
					float currentIrcY=lrcBitmapFactory.getCurrentLrc().y+y;
					
					/**��Ļ�м��yλ��*/
					float centerY=LrcSurfaceView.this.getHeight()/2;
					
					/**�ж�  centerY ��  currentIrcY  
					 * ���currentIrcY > centerY,��ô��ĻҪ�����ƶ�
					 * ʵ�ʾ��� ��bitmap���ͼƬ �����ƶ�(currentIrcY-centerY)
					 */
					
					//log("���ڲ��Ÿ��λ��="+currentIrcY+", ��Ļ���룺"+centerY);
					/**���ͼƬ �����ƶ� ���پ���, �������ͼƬ�� �������ͼƬ��y������*/
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
		
		/**f �Ǹ��ͼƬҪ�����ƶ��ľ��� Ϊ����    ��������  �л�һ�и��*/
		private void startMyAnimation(final float f) {
			if(isAnimation==true)
				return;
			new Thread(){
				public void run() {
				isAnimation=true;
				//log("���ͼƬҪ�����ƶ��ľ��� :"+f);
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

	/**����surfaceview*/
	private void drawSurfaceview() {
		synchronized(holder){
			Canvas canvas=holder.lockCanvas();
			if(canvas==null)			
				return;
			
			//�������
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
			
			//�����ͼƬ��lrcBitmap�����ڻ�����
			mPaint.setColor(Color.BLACK);
		    canvas.drawBitmap(lrcBitmap, 0, y, mPaint);
		    
		    //������������ʾ�Ŵ󾵣���ȡ����
		    if(isLongPress && LrcSurfaceView.this.isLongClickable()){  
			   String getWord = lrcBitmapFactory.getLrcByY(mZOOy+RADIUS, Math.abs(y), mZOOx);
	
			    log(("�Ŵ�  ƽ��  Y="+((int)((RADIUS-mZOOy+y)*FACTOR*1)-15)));
			   	matrix.setTranslate((int)(RADIUS-mZOOx*1), (int)((RADIUS-(mZOOy+RADIUS)+y)*FACTOR*1));
				drawable.getPaint().getShader().setLocalMatrix(matrix);
				//Bounds�������Ǹ�Բ�����о���
				drawable.setBounds(mZOOx-RADIUS, mZOOy-RADIUS-15, mZOOx+RADIUS, mZOOy+RADIUS-15);
				
				mPaint.setColor(Color.LTGRAY);
				canvas.drawRoundRect(new RectF(mZOOx-RADIUS, mZOOy-RADIUS-15, mZOOx+RADIUS, mZOOy+RADIUS-15)
										, RADIUS, RADIUS, mPaint);
				
				mPaint.setColor(Color.parseColor("#20ffffff")); //�Ŵ�͸����ɫ
				canvas.drawRoundRect(new RectF(mZOOx-RADIUS, mZOOy-RADIUS-15, mZOOx+RADIUS, mZOOy+RADIUS-15)
										, RADIUS, RADIUS, mPaint);
				
				drawable.invalidateSelf();
				drawable.draw(canvas);
		    }
		    
			if(isControl){//�����ָ���ڻ������
				mPaint.setColor(Color.GRAY);
	//			canvas.drawLine(0, getHeight()/2, getWidth(), getHeight()/2, mPaint);
				float lrcBitmapY = (getHeight()/2-y);//ȡ���м��������ǵĸ����ͼƬ�е�λ�� y
				log("lrcBitmapY="+lrcBitmapY+", y="+y);
				long time =lrcBitmapFactory.getTimeInLrcBitMap(lrcBitmapY);
				currentControlPosition = time;
				
				//֪ͨ��ʱ���������貥�ŵ�λ��
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

	/**�û��Ƿ����ֶ�������Ļ��ʵ��ƶ�*/
	public boolean isLrcViewControlScroll() {
		return isControl;
	}

	/**
	 * ��Activity���˱�����һ��handler��
	 * ��ָ�������ͼƬ����Activity������Ҫ�������ֵ�ʱ��� 
	 */
	public void setActiviyHandler(Handler activiyHandler) {
		this.activiyHandler = activiyHandler;
	}

	/**���ø��surfaceView surfaceCreatedʱҪ���õķ���  ������ */
	public void setLrcSurfaceViewOnCreateListener(LrcSurfaceViewOnCreateListener onCreateListenr){
		this.onCreateListenr=onCreateListenr;
		if(isSurfaceViewOnCreate==true){//����Ѿ���ʼcreate ����ִ��
			onCreateListenr.onCreate();
		}
	}

	 //not used
	/**���������Ƿ����ڲ���flag*/
	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}
	
	public interface LrcSurfaceViewOnCreateListener {
		/**��LrcSurfaceView onCreateʱ������*/
		void onCreate();
	}
	
	private void log(String log){
		Log.i("LrcSurfaceView", log+"");
	}
}
