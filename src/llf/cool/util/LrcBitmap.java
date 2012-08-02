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
 * ��Ӧ���ظ���
 * 
 * ���ͼƬ���� ֻҪ��������ļ� �Ϳ��� ������Ӧ��bitmap ���ҿ�����ʱ���� �ػ� ��ʵ�ǰ��һ�б�����
 * 
 */
public class LrcBitmap {
	private Context mContext;
	private List<LrcStr> allLrc;// ���и��
	private float lineHeight;// ÿ�и�ʵ��и� ����Ϊ��λ
	private float textHeight;// �ֵĸ߶� ��С�� �и�
	private float lineHeightTotextHeight = 0.8f;// �и�:����߶�
	private int TEXTSIZE=20;
	private int lrcBitmapHeight;// ������ʻ����ĸ߶�
	private int lrcBitmapWidth;// ������ʻ����Ŀ��

	private LrcStr templrc;
	private Bitmap lrcBitmap;// ���ͼƬ
	private Canvas mCanvas;// ��ʻ���

	private LrcStr tLrc;
	private Bitmap mMagnifierBitmap;// ��Ҫ���Ŵ��ͼƬ
	private Canvas mMagnifierCanvas;//�Ŵ�ר��ͼƬ
	
	private Paint mPaint;//��ʻ���
	private Paint mPlayingPaint;//���ڲ��ŵĸ��ר��
	private Paint mMagnifierPaint;//�Ŵ�ר��
	
	private LrcStr currentLrc;// ��ǰ���ڲ��ŵĸ�ʶ���
	private Rect rect;
//	private Bitmap bottomBitmap;
//	private Typeface type;
//	private Typeface tf;

	public int index = 0;
	private int old_index = -1;
	public static ArrayList<Integer> m_current_time_point; //���ǵĸ��
	public static ArrayList<Integer> m_index;
	public static ArrayList<Integer> m_per_star_time;
	public static ArrayList<LrcStr> m_lines_lyric;

	public void drawStar(Canvas canvas, float y){
		Drawable draw1 = mContext.getResources().getDrawable(
				R.drawable.img_playback_btn_star);
		canvas.drawBitmap(PubFunc.drawableToBitmap(draw1), 0, y - 28 , mPlayingPaint);
	}
	
	public LrcBitmap(final int lrcBitmapWidth, List<LrcStr> allLrc, Context context) {
		
		this.lrcBitmapWidth = lrcBitmapWidth;// һ����Ǹ�������� ����surfaceView�Ŀ��
		this.setIrc(allLrc);// ����Щ���
		mContext = context;
		
		m_current_time_point = new ArrayList<Integer>();
		m_index = new ArrayList<Integer>();
		m_lines_lyric = new ArrayList<LrcStr>();
		m_per_star_time = new ArrayList<Integer>();
		
		//��ʼ��4֧����		
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
		
		/*���� ��������߶� textHeight, ����и�lineHeight, �����߶�lrcBitmapHeight*/
		textHeight = (mPaint.getFontMetrics().bottom - mPaint.getFontMetrics().top) * 3 / 2;
		lineHeight = (int) (textHeight * lineHeightTotextHeight * 0.8);
		lrcBitmapHeight = (int) (allLrc.size() * lineHeight);
		log("����߶�" + textHeight);
		log("��ʸ߶�" + lineHeight); //(bottom-top) * 6 / 5
		log("�����߶�" + lrcBitmapHeight);
				
		/* ��ÿ����ʶ������λ��*/
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
		
		// �������λͼ����
		lrcBitmap = Bitmap.createBitmap(lrcBitmapWidth, lrcBitmapHeight, Config.RGB_565);	
		mCanvas = new Canvas(lrcBitmap);    //��ʻ���
		
		mMagnifierBitmap = lrcBitmap; 
		mMagnifierCanvas = new Canvas(mMagnifierBitmap); //�Ŵ��Ļ���
		
		//��ʼ��������
		//drawAllLine();
	}
	
	public void drawAllLine() {
		for (LrcStr tmp : allLrc) {
			drawLine(tmp);
		}
	}
	
	/** �ڻ����ϻ�һ�и�� */
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
				continue;// ����Ǳ��� ���Ǹ�� ��ô����
			if (s.time < time) {
				if ((i + 1) < allLrc.size()) {// ����������һ�и��
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

	/** �ڻ����ϻ�һ�и��, flag��ʾ�Ƿ����ڲ��ŵĸ�� */
	public void drawLinePlaying(LrcStr lrcStr, boolean flag) {
		// ���û�������
		if (flag) {
			drawNewWork(lrcStr);
		} else {
			drawNewWork2(lrcStr);
		}
	}

	/**�ڸ�ʻ���canvas�ϻ������*/
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
	
	/**�ڷŴ󾵻���c�ϻ������ */
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
	
	/**�������ڲ��ŵĸ��*/
	private void drawNewWork(LrcStr lrcStr) {
		currentLrc = lrcStr;
		if (AudioActivity.is_play_star_lines){//���ڲ��ű��Ǹ��
			log("drawNewWork is play star ="+AudioActivity.is_play_star_lines);
			if (m_index.indexOf(index) != -1 && !LrcSurfaceView.isControl){
				drawStar(mCanvas, lrcStr.y);
			}
		} else { //��������
			if (AudioActivity.m_is_star && !LrcSurfaceView.isControl){//������
				log("���ǣ�"+lrcStr.y);
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
				mPlayingPaint.setColor(Color.MAGENTA);//��ǰ���ڲ��ŵĸ�ʣ����ɫ
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

	/** ���ػ��ø�ʵ�bitmap */
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

	/** �������и�� */
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
	
	
	/**Andorid������8M(�е�˵��4M)��ͼƬ�ڴ棬��������˾ͻ�������Ǿ���Ҫ��ʱ�ͷ��ڴ��е�ͼƬ��Դ��
	 * ��ͼƬ��������null����ʵ�����ǲ��еģ�ͼƬ��Ȼ���ڴ���, ��recycle()
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
