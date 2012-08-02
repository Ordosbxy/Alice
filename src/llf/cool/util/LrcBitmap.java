package llf.cool.util;

import java.util.ArrayList;
import java.util.List;

import llf.cool.R;
import llf.cool.activity.AudioActivity;
import llf.cool.service.AudioService;
import llf.cool.view.LrcSurfaceView;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

/**
 * 不应该重复画
 * 
 * 歌词图片对象 只要给定歌词文件 就可以 返回相应的bitmap 而且可以随时控制 重绘 歌词当前哪一行被播放
 * 
 */
public class LrcBitmap {
	private Context mContext;
	private List<LrcStr> allLrc;// 所有歌词
	private float lineHeight;// 每行歌词的行高 像素为单位
	private float textHeight;// 字的高度 略小于 行高
	private float lineHeightTotextHeight = 0.8f;// 行高:字体高度
	private int TEXTSIZE=20;
	private int lrcBitmapHeight;// 整个歌词画布的高度
	private int lrcBitmapWidth;// 整个歌词画布的宽度

	private LrcStr templrc;
	private Bitmap lrcBitmap;// 歌词图片
	private Canvas mCanvas;// 歌词画布

	private LrcStr tLrc;
	private Bitmap mMagnifierBitmap;// 需要给放大的图片
	private Canvas mMagnifierCanvas;//放大镜专用图片
	
	private Paint mPaint;//歌词画笔
	private Paint mPlayingPaint;//正在播放的歌词专用
	private Paint mMagnifierPaint;//放大镜专用
	
	private LrcStr currentLrc;// 当前正在播放的歌词对象
	private Rect rect;
//	private Bitmap bottomBitmap;
//	private Typeface type;
//	private Typeface tf;

	public int index = 0;
	private int old_index = -1;
	public static ArrayList<Integer> m_current_time_point; //标星的歌词
	public static ArrayList<Integer> m_index;
	public static ArrayList<Integer> m_per_star_time;
	public static ArrayList<LrcStr> m_lines_lyric;

	public void drawStar(Canvas canvas, float y){
		Drawable draw1 = mContext.getResources().getDrawable(
				R.drawable.img_playback_btn_star);
		canvas.drawBitmap(PubFunc.drawableToBitmap(draw1), 0, y - 28 , mPlayingPaint);
	}
	
	public LrcBitmap(final int lrcBitmapWidth, List<LrcStr> allLrc, Context context) {
		
		this.lrcBitmapWidth = lrcBitmapWidth;// 一般就是歌词组件宽度 就是surfaceView的宽度
		this.setIrc(allLrc);// 有哪些歌词
		mContext = context;
		
		m_current_time_point = new ArrayList<Integer>();
		m_index = new ArrayList<Integer>();
		m_lines_lyric = new ArrayList<LrcStr>();
		m_per_star_time = new ArrayList<Integer>();
		
		//初始化4支画笔		
		TEXTSIZE = (int)context.getResources().getDimension(R.dimen.lrc_text_size);
		log(">>>TEXTSIZE="+TEXTSIZE);
		
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setTextSize(TEXTSIZE);
		
		mPlayingPaint=new Paint();
		mPlayingPaint.setAntiAlias(true);
		mPlayingPaint.setTextSize(TEXTSIZE);
		
		mMagnifierPaint = new Paint();
		mMagnifierPaint.setAntiAlias(true);
		mMagnifierPaint.setTextSize(TEXTSIZE);
		
		/*计算 画笔字体高度 textHeight, 歌词行高lineHeight, 画布高度lrcBitmapHeight*/
		textHeight = (mPaint.getFontMetrics().bottom - mPaint.getFontMetrics().top) * 3 / 2;
		lineHeight = (int) (textHeight * lineHeightTotextHeight * 0.8);
		lrcBitmapHeight = (int) (allLrc.size() * lineHeight);
		log("字体高度" + textHeight);
		log("歌词高度" + lineHeight); //(bottom-top) * 6 / 5
		log("画布高度" + lrcBitmapHeight);
				
		/* 给每个歌词对象分配位置*/
		int logFlag = 0;
		for (int i = 0; i < allLrc.size(); i++) {
			LrcStr s = allLrc.get(i);
			
			float[] f = new float[200];
			mPaint.getTextWidths(s.lrc, f);
			
			rect = new Rect();
			mPaint.getTextBounds(s.lrc, 0, s.lrc.length(), rect);
			
			float width = 0;
			for (int j = 0; j < f.length; j++) {
				width = width + f[j];
			}
			
			s.textWidth = rect.width();
			s.x = 40;//(lrcBitmapWidth - width) / 2;
			s.y = (i + 1) * lineHeight;
			s.str = s.lrc.split("\\s+");
			s.mFloat=new float[s.str.length];
			float mWidth = s.x;
			for(int k=0;k<s.str.length;k++){
				mPaint.getTextBounds(s.str[k], 0, s.str[k].length(), rect);
				s.mFloat[k]=mWidth;
				mWidth=mWidth+rect.width()+5;
			}	
			if(logFlag<4){
				log(s.lrc + " ("+s.x+", " + s.y+")");				
				logFlag ++;
			}
		}
		
		// 构建歌词位图对象
		lrcBitmap = Bitmap.createBitmap(lrcBitmapWidth, lrcBitmapHeight, Config.RGB_565);	
		mCanvas = new Canvas(lrcBitmap);    //歌词画布
		
		mMagnifierBitmap = lrcBitmap; 
		mMagnifierCanvas = new Canvas(mMagnifierBitmap); //放大层的画布
		
		//开始画所有行
		//drawAllLine();
	}
	
	public void drawAllLine() {
		for (LrcStr tmp : allLrc) {
			drawLine(tmp);
		}
	}
	
	/** 在画布上画一行歌词 */
	public void drawLine(LrcStr lrcStr) {
		drawNewWork2(lrcStr);
		drawNewWork2(lrcStr,true);
	}

	public void setTime(int time) {	
		
		for (int j = 1; j < allLrc.size(); j++){			
			if (time < allLrc.get(j).time){
				if (j == 1 || time >= allLrc.get(j - 1).time){
					if(AudioService.PLAYER_STATUS == AudioService.PlayerStatus.RUN){
						old_index = index;
						index = j - 1;						
					}
				}
			}
			
			if (old_index != index){
				AudioActivity.m_is_star = false;
			}
			
			if (index == -1){
				return;
			}
		}
		
		//log("m_index="+m_index+", index="+index);
		for (int i = 0; i < allLrc.size(); i++) {
			LrcStr s = allLrc.get(i);			
			drawLinePlaying(s, false);			
			if (s.isHeader)
				continue;// 如果是标题 不是歌词 那么继续
			if (s.time < time) {
				if ((i + 1) < allLrc.size()) {// 如果不是最后一行歌词
					LrcStr s2 = allLrc.get(i + 1);
					if (s2.time > time) {
							drawLinePlaying(s, true);
						}
					} else {
					drawLinePlaying(s, true);
				}
			}
		}
	}//end setTime

	/** 在画布上画一行歌词, flag表示是否正在播放的歌词 */
	public void drawLinePlaying(LrcStr lrcStr, boolean flag) {
		// 设置画笔属性
		if (flag) {
			drawNewWork(lrcStr);
		} else {
			drawNewWork2(lrcStr);
		}
	}

	/**在歌词画布canvas上画出歌词*/
	private void drawNewWork2(LrcStr lrcStr) {
		for (int i = 0; i < lrcStr.str.length; i++) {
			if (LrcSurfaceView.listData.indexOf(lrcStr.str[i])== -1) {
				mPaint.setColor(Color.GRAY);
				mCanvas.drawText(lrcStr.str[i], lrcStr.mFloat[i], lrcStr.y, mPaint);
			} else {
				mPaint.setColor(Color.BLUE);
				mCanvas.drawText(lrcStr.str[i], lrcStr.mFloat[i], lrcStr.y, mPaint);
			}
		}
	}
	
	/**在放大镜画布c上画出歌词 */
	private void drawNewWork2(LrcStr lrcStr, boolean b) {
		for (int i = 0; i < lrcStr.str.length; i++) {
			if (LrcSurfaceView.listData.indexOf(lrcStr.str[i])== -1) {
				mMagnifierPaint.setColor(Color.GRAY);
				mMagnifierCanvas.drawText(lrcStr.str[i], lrcStr.mFloat[i], lrcStr.y, mMagnifierPaint);
			} else {
				mMagnifierPaint.setColor(Color.BLUE);
				mMagnifierCanvas.drawText(lrcStr.str[i], lrcStr.mFloat[i], lrcStr.y, mMagnifierPaint);
			}
		}
	}
	
	/**画出正在播放的歌词*/
	private void drawNewWork(LrcStr lrcStr) {
		currentLrc = lrcStr;
		if (AudioActivity.is_play_star_lines){//正在播放标星歌词
			log("drawNewWork is play star ="+AudioActivity.is_play_star_lines);
			if (m_index.indexOf(index) != -1 && !LrcSurfaceView.isControl){
				drawStar(mCanvas, lrcStr.y);
			}
		} else { //正常播放
			if (AudioActivity.m_is_star && !LrcSurfaceView.isControl){//若标星
				log("标星："+lrcStr.y);
				drawStar(mCanvas, lrcStr.y);
				if (m_index.indexOf(index) == -1){
					m_index.add(index);
					m_current_time_point.add(lrcStr.time);
					m_lines_lyric.add(lrcStr);
					m_per_star_time.add(lrcStr.getSleepTime());
					
					log("index="+index);
					log("m_index="+m_index);
				}
			}		
		}		
//		}else{
//			if (m_index.indexOf(index) != -1){
//				m_current_time_point.remove(lrcStr.time);
//				m_index.remove(index);
//				m_lines_lyric.remove(lrcStr);
//				m_per_star_time.remove(lrcStr.getSleepTime());
//			}
//		}

		for (int i = 0; i < lrcStr.str.length; i++) {
			if (LrcSurfaceView.listData.indexOf(lrcStr.str[i])== -1) {
				mPlayingPaint.setColor(Color.MAGENTA);//当前正在播放的歌词，洋红色
				mCanvas.drawText(lrcStr.str[i], lrcStr.mFloat[i], lrcStr.y, mPlayingPaint);
			} else {
				mPlayingPaint.setColor(Color.BLUE);
				mCanvas.drawText(lrcStr.str[i], lrcStr.mFloat[i], lrcStr.y, mPlayingPaint);
			}
		}
	}

	private void drawNewWork3(LrcStr lrcStr, int j) {
		for (int i = 0; i < lrcStr.str.length; i++) {
			if (LrcSurfaceView.listData.indexOf(lrcStr.str[i])== -1) {
				if (i == j) {
					mMagnifierPaint.setColor(Color.GREEN);
				} else {
					mMagnifierPaint.setColor(Color.GRAY);
				}
				mMagnifierCanvas.drawText(lrcStr.str[i], lrcStr.mFloat[i], lrcStr.y, mMagnifierPaint);
			} else {
				if (i == j) {
					mMagnifierPaint.setColor(Color.GREEN);
				} else {
					mMagnifierPaint.setColor(Color.BLUE);
				}
				mMagnifierCanvas.drawText(lrcStr.str[i], lrcStr.mFloat[i], lrcStr.y, mMagnifierPaint);
			}
		}
	}

	/** 返回画好歌词的bitmap */
	public Bitmap getBitmap() {
		return lrcBitmap;
	}

	public long getTimeInLrcBitMap(float lrcBitmapY) {
		int i = (int) (lrcBitmapY / lineHeight);
		if (i < 0)
			return 0;
		if (i > allLrc.size() - 1) {
			LrcStr lrc = allLrc.get(allLrc.size() - 1);
			drawLinePlaying(lrc, true);
			return lrc.time;
		}
		LrcStr lrc = allLrc.get(i);
		if (templrc != null) {
			drawLinePlaying(templrc, false);
		}
		drawLinePlaying(lrc, true);
		templrc = lrc;
		return lrc.time;
	}

	public String getLrcByY(float y, float y2, float x) {
		if (tLrc != null) {
			drawNewWork2(tLrc,true);
		}
		int temp2 = 0;

		for (LrcStr lrc : allLrc) {
			if ((y + y2 > lrc.y - textHeight )
					&& (y + y2) < lrc.y + textHeight) {
				int temp = (int) ((x - lrc.x) * lrc.lrc.length() / lrc.textWidth);
				if(temp<0)
					return null;
//				if(y>lrc.x+lrc.textWidth)
//					return null;
				String[] str = lrc.lrc.split("\\s+");
				for (int i = 0; i < str.length; i++) {
					temp2 = temp2 + str[i].length() + 1;
					if (temp2 >= temp) {
//						log("drawZooword="+str[i]);
						drawZooword(lrc, i, str[i]);
						return str[i];
					}
				}
			}
		}
		return null;
	}

	public LrcStr getCurrentLrc() {
		return currentLrc;
	}

	/** 设置所有歌词 */
	public void setIrc(List<LrcStr> allLrc) {
		this.allLrc = allLrc;
	}

	public Bitmap getBt() {
		return mMagnifierBitmap;
	}

	private void drawZooword(LrcStr lrc2, int i, String str) {
		drawNewWork3(lrc2, i);
		tLrc = lrc2;
	}
	
	
	/**Andorid限制了8M(有的说是4M)的图片内存，如果超过了就会溢出，那就是要及时释放内存中的图片资源。
	 * 对图片对象设置null。其实这样是不行的，图片依然在内存中, 需recycle()
	 */
	public void mRecycled(){
		 if(mMagnifierBitmap.isRecycled()==false && mMagnifierBitmap!=null){
			 mMagnifierBitmap.recycle();
			 mMagnifierBitmap=null;			 
		 }
		 if(lrcBitmap.isRecycled()==false && lrcBitmap!=null){
			 lrcBitmap.recycle();
			 lrcBitmap=null;
		 }
		 System.gc();
	 }
	
	private void log(String log){
		Log.i("LrcBitmap", log+"");
	}
}
