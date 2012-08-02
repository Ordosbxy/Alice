package llf.cool.util;

import android.graphics.Paint;

public class LrcStr{
	public LrcStr(){}
	
	public LrcStr(int time, String lrc) {
		super();
		this.time = time;
		this.lrc = lrc;
	}	
	
	public LrcStr(int time, String lrc, boolean isHeader) {
		super();
		this.time = time;
		this.lrc = lrc;
		this.isHeader = isHeader;
	}

	public int time;
	public String lrc;
	public String str[]; //°´¿Õ¸ñ
	public float x;
	public float y;
	public float mFloat[];
	public float textWidth;
	public int width;
	public boolean isCurrent;
	public boolean isHeader;
	Paint paint;
	
	private int lineIndex;
	private int sleepTime;
	public int getLineIndex() {
		return lineIndex;
	}

	public void setLineIndex(int lineIndex) {
		this.lineIndex = lineIndex;
	}
	
	public int getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
	}
	
	@Override
	public String toString() {
		return "LrcStr [time=" + time + ", lrc=" + lrc + ", x=" + x + ", y="
				+ y + ", width=" + width + ", isCurrent=" + isCurrent
				+ ", isHeader=" + isHeader + ", textWidth=" + textWidth
				+ ", paint=" + paint + "]";
	}
	
	
}