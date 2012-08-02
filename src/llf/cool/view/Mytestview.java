package llf.cool.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import llf.cool.R;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class Mytestview extends View {
	private Bitmap m_bitmap;
	private Bitmap m_bitmap_back;
	private Bitmap m_bitmap_change;
	private Canvas canvas;
	private Paint m_paint;
	private Bitmap[] bitmap_begin = new Bitmap[2];
	private Bitmap[] bitmap_button = new Bitmap[3];
	private Rect src_rt = new Rect(0,0,800,480);
	private Rect dst_rt = new Rect(0,0,800,480);
	
	private Rect[] selectRt = new Rect[4];
	private Rect[] beginRect = new Rect[2];
	private Rect[] buttonRt = new Rect[3];
	private Bitmap[] bitmap_in = new Bitmap[5];
	//词典
	private Bitmap[] m_bit = new Bitmap[4];
	private Rect[] dic_rect = new Rect[4];
	private Rect[] inRect = new Rect[5];
	public Mytestview(Context context) {
		super(context);
		m_paint = new Paint();
		m_paint.setTextSize(20);
		m_paint.setAntiAlias(true);
		// TODO Auto-generated constructor stub
	}

	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		canvas.drawBitmap(m_bitmap, src_rt, dst_rt, m_paint);
	}	
	
	public void loadBack (int resId){
		m_bitmap_back = BitmapFactory.decodeResource(getResources(), resId);
		m_bitmap = Bitmap.createBitmap(dst_rt.right,dst_rt.bottom, m_bitmap_back.getConfig());
		canvas = new Canvas(m_bitmap);
	}
	public void showBack(){
		canvas.drawBitmap(m_bitmap_back, 0, 0, m_paint);
	}
	//进入测试界面
	public void loadRect(int resId,Rect rect,int index ){
		bitmap_in[index] = BitmapFactory.decodeResource(getResources(), resId);
		this.inRect[index] =rect;
	}
	public void showTest(int index){
		canvas.drawBitmap(bitmap_in[index],this.inRect[index].left, this.inRect[index].top, m_paint);
	}
	
	//开始测试
	public void loadImage(int resId ,Rect rect,int index){
		bitmap_begin[index] = BitmapFactory.decodeResource(getResources(), resId);
		this.beginRect[index] = rect;
	}
	public void showImage(int index){
		canvas.drawBitmap(bitmap_begin[index],this.beginRect[index].left, this.beginRect[index].top, m_paint);
	}
	//上面的按钮
	public void loadButton(int resId ,Rect rect,int index){
		bitmap_button[index] = BitmapFactory.decodeResource(getResources(), resId);
		buttonRt[index] =rect;
	}
	public void showImageButton(int index){
		canvas.drawBitmap(bitmap_button[index],this.buttonRt[index].left, this.buttonRt[index].top, m_paint);
	}
	
	public void showChageBack(){
		m_bitmap_change = BitmapFactory.decodeResource(getResources(), R.drawable.background_1);
		canvas.drawBitmap(m_bitmap_change, 0, 0, m_paint);
		invalidate();
	}
	public int getInt(int x ,int y){
		for (int i = 0; i < 2; i++){
			if (this.beginRect[i].contains(x,y)){
				return i;
			}
		}
		return -1;
	}
	//显示词典选项
	public void drawImage(byte[] in ,Rect rect,int index){
		m_bit[index] = BitmapFactory.decodeByteArray(in, 0, in.length);
		dic_rect[index]= rect;
		canvas.drawBitmap(m_bit[index], dic_rect[index].left,dic_rect[index].top, m_paint);
	}
	public int selectedItem(int x ,int y){
		for (int i = 0; i< 4;i++ ){
			if (dic_rect[i].contains(x, y)){
				return i;
			}
		}
		return -1;
	}
	
	
	public void showText(int id ,String words){
		canvas.drawText(id+"."+words, 100,100, m_paint);
	}
	public void showTextOne(String str_a,Rect rect){
		canvas.drawText("A:"+ str_a, rect.left, rect.top, m_paint);
		selectRt[0] =rect;
	}
	public void showTextTwo(String str_a,Rect rect){
		canvas.drawText("B:"+ str_a,rect.left, rect.top, m_paint);
		selectRt[1] =rect;
	}
	public void showTextThree(String str_a,Rect rect){
		canvas.drawText("C:"+ str_a, rect.left, rect.top, m_paint);
		selectRt[2] =rect;
	}
	public void showTextFour(String str_a,Rect rect){
		canvas.drawText( "D:"+ str_a, rect.left, rect.top, m_paint);
		selectRt[3] =rect;
	}
	public void selectAnswer(int index,int id){
		Paint sel_paint = new Paint();
		sel_paint.setColor(Color.BLUE);
		sel_paint.setTextSize(25);
		if (id == 0){
			canvas.drawText( "√", selectRt[index].left,selectRt[index].top, sel_paint);
		}else if(id == 1){
			canvas.drawText( "√", dic_rect[index].left,dic_rect[index].top, sel_paint);
		}
		invalidate();
	}
	
//	public void showAllItem(String item){
//		canvas.drawText( "D:"+ str_a, rect.left, rect.top, m_paint);
//	}
	
	public int getchoice(int x ,int y){
		for (int i = 0 ;i <4 ; i++){
			selectRt[i] = new Rect (selectRt[i].left,(selectRt[i].top)-15,selectRt[i].right,selectRt[i].bottom);
			if (selectRt[i].contains(x, y)){
				return i;
			}
		}
		return -1;
	}
}
