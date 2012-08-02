package llf.cool.util;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LyricLine {

	public static final String STAR_TAG = "";
	private static final String WORD_EXPR = "[A-Za-z'-]+|.";
	private static Pattern sPattern = Pattern.compile(WORD_EXPR);

	private int starLevel = 0;
	private int sleepTime = 0;
	private int timePoint = 0;
	private int lineIndex = 0;
	private String lineText = null;
	private ArrayList<LyricWord> wordList = new ArrayList<LyricWord>();

	public ArrayList<LyricWord> getWordList() {
		return wordList;
	}

	public void setWordList(String text) {
		Matcher matcher = sPattern.matcher(text);
		int index = 0;
		this.lineText = text;
		while (matcher.find()) {
			LyricWord lyricWord = new LyricWord(matcher.group());
			lyricWord.setWordIndex(index++);
			this.wordList.add(lyricWord);
		}
	}

	public int getStarLevel() {
		return starLevel;
	}

	public void setStarLevel(int starLevel) {
		this.starLevel = starLevel;
	}

	public int getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
	}

	public int getTimePoint() {
		return timePoint;
	}

	public void setTimePoint(int timePoint) {
		this.timePoint = timePoint;
	}

	public int getLineIndex() {
		return lineIndex;
	}

	public void setLineIndex(int lineIndex) {
		this.lineIndex = lineIndex;
	}

	public String getLineText() {
		return lineText;
	}

	/**
	 * add word
	 * 
	 * @param word
	 */
	public void addWord(LyricWord word) {
		this.wordList.add(word);
	}
}
