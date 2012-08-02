package llf.cool.util;

import android.graphics.Rect;

public class LyricWord {

	private boolean isNewWord = false;
	private int wordIndex = 0;
	private String text = null;
	private Rect dispRect = null;

	public LyricWord(String text) {
		super();
		// TODO Auto-generated constructor stub
		setText(text);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isNewWord() {
		return isNewWord;
	}

	public void setNewWord(boolean isNewWord) {
		this.isNewWord = isNewWord;
	}

	public int getWordIndex() {
		return wordIndex;
	}

	public void setWordIndex(int wordIndex) {
		this.wordIndex = wordIndex;
	}

	public Rect getDispRect() {
		return dispRect;
	}

	public void setDispRect(Rect dispRect) {
		this.dispRect = dispRect;
	}

}
