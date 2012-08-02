package llf.cool.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

public class LyricUtil {
	/**根据文件 解析出 所有歌词对象*/
	public static List<LrcStr> parseLrcFile(File path){
		try {						
			List<LrcStr> list=new ArrayList<LrcStr>();
//			List<String> oldlList=new ArrayList<String>();//放每一行原始歌词  [00:00:00]sss
			System.out.println("path="+path);
			BufferedReader in=new BufferedReader(
					new InputStreamReader(new FileInputStream(path), "gbk"));
			String line=null;
			while((line=in.readLine())!=null){
				LrcStr lrc = parseLrcStrLine(line);
				if(lrc!=null && lrc.lrc.trim().length()>1){
					if(lrc.lrc.trim().length()>50){
						int hLength=lrc.lrc.length()/2;
						String tLRC=lrc.lrc;
						lrc.lrc=tLRC.substring(0, hLength);
						list.add(lrc);
						list.add(new LrcStr(lrc.time+1000
											, tLRC.substring(hLength, tLRC.length())
											, lrc.isHeader));
					}else{
						list.add(lrc);
					}
				}
			}
			Collections.sort(list, new Comparator<LrcStr>() {
				@Override
				public int compare(LrcStr object1, LrcStr object2) {				
					return (int)(object1.time-object2.time);
				}
			});
			in.close();
			
			//add for stars
			for (int i = 0; i < list.size(); i++) {
				LrcStr one = list.get(i);
				one.setLineIndex(i);
				
				if (i + 1 < list.size()) {
					LrcStr two = list.get(i + 1);
					one.setSleepTime(two.time
							- one.time);
				}
//				
//				if(i == 0){
//					list.get(i).getWordList().get(2).setNewWord(true);
//				}else if(i == 1){
//					list.get(i).getWordList().get(5).setNewWord(true);
//				}
			}
			
			return list;
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	static Pattern headerInfo=Pattern.compile("^\\[[^0-9]{2}:.*");
	static Pattern lrcInfo=Pattern.compile("^\\[[0-9]{2}:.*");
	/**解析一行歌词   转换成  lrcStr 对象*/
	private static LrcStr parseLrcStrLine(String line) {
		LrcStr lrc=new LrcStr();
		if(headerInfo.matcher(line).matches()){
			lrc.lrc=line.substring(line.indexOf(":")+1, line.length()-1);
			lrc.isHeader=true;
		}else if(lrcInfo.matcher(line).matches()){
			int mi=Integer.parseInt(line.substring(1,3));
			int se=Integer.parseInt(line.substring(4,6));
			int mm=Integer.parseInt(line.substring(7,9));
			lrc.time=mm*10+se*1000+mi*60*1000;
			if(line.length()>10){
				lrc.lrc=line.substring(10,line.length());
			}else{
				lrc.lrc="";
			}
			lrc.isHeader=false;
		}else{
			return null;
		}
		return lrc;
	}
	

	public static Vector<LyricLine> parseLRCFile(File f) {
		try {
			if (f == null || !f.exists()) {
				return null;
			} else {
				Vector<LyricLine> lrclist = new Vector<LyricLine>();
				lrclist.clear();
				InputStream is = new BufferedInputStream(new FileInputStream(f));
				BufferedReader br = new BufferedReader(new InputStreamReader(
						is, GetCharset(f)));
				String strTemp = "";
				while ((strTemp = br.readLine()) != null) {
					// Log.d(TAG,"strTemp = "+strTemp);
					strTemp = AnalyzeLRC(lrclist, strTemp);
				}
				br.close();
				is.close();
				Collections.sort(lrclist, new Sort());

				for (int i = 0; i < lrclist.size(); i++) {
					LyricLine one = lrclist.get(i);
					one.setLineIndex(i);
					if (i + 1 < lrclist.size()) {
						LyricLine two = lrclist.get(i + 1);
						one.setSleepTime(two.getTimePoint()
								- one.getTimePoint());
					}
					/////////////////////////
					if(i == 0){
						lrclist.get(i).getWordList().get(2).setNewWord(true);
					}else if(i == 1){
						lrclist.get(i).getWordList().get(5).setNewWord(true);
					}
				}
				return lrclist;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static String AnalyzeLRC(Vector<LyricLine> lrclist, String LRCText) {
		try {
			int pos1 = LRCText.indexOf("[");
			int pos2 = LRCText.indexOf("]");

			if (pos1 == 0 && pos2 != -1) {
				Long time[] = new Long[GetPossiblyTagCount(LRCText)];
				time[0] = TimeToLong(LRCText.substring(pos1 + 1, pos2));
				if (time[0] == -1) {
					return ""; // LRCText
				}
				String strLineRemaining = LRCText;
				int i = 1;
				while (pos1 == 0 && pos2 != -1) {
					strLineRemaining = strLineRemaining.substring(pos2 + 1);
					pos1 = strLineRemaining.indexOf("[");
					pos2 = strLineRemaining.indexOf("]");
					if (pos2 != -1) {
						time[i] = TimeToLong(strLineRemaining.substring(
								pos1 + 1, pos2));
						if (time[i] == -1)
							return ""; // LRCText
						i++;
					}
				}

				LyricLine linel = new LyricLine();
				for (int j = 0; j < time.length; j++) {
					if (time[j] != null) {
						linel.setTimePoint(time[j].intValue());
						linel.setWordList(strLineRemaining);
						lrclist.add(linel);
						linel = new LyricLine();
					}
				}
				return strLineRemaining;
			} else {
				return "";
			}
		} catch (Exception e) {
			return "";
		}
	}

	private static int GetPossiblyTagCount(String Line) {
		String strCount1[] = Line.split("\\[");
		String strCount2[] = Line.split("\\]");
		if (strCount1.length == 0 && strCount2.length == 0) {
			return 1;
		} else if (strCount1.length > strCount2.length) {
			return strCount1.length;
		} else {
			return strCount2.length;
		}
	}

	private static long TimeToLong(String Time) {
		try {
			String[] s1 = Time.split(":");
			int min = Integer.parseInt(s1[0]);
			String[] s2 = s1[1].split("\\.");
			int sec = Integer.parseInt(s2[0]);
			int mill = 0;
			if (s2.length > 1) {
				mill = Integer.parseInt(s2[1]);
			}
			return min * 60 * 1000 + sec * 1000 + mill * 10;
		} catch (Exception e) {
			return -1;
		}
	}

	private static String GetCharset(File file) {
		String charset = "GBK";
		byte[] first3Bytes = new byte[3];
		try {
			boolean checked = false;
			BufferedInputStream bufferedInputStream = new BufferedInputStream(
					new FileInputStream(file));
			bufferedInputStream.mark(0);
			int read = bufferedInputStream.read(first3Bytes, 0, 3);
			if (read == -1) {
				return charset;
			}
			if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
				charset = "UTF-16LE";
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xFE
					&& first3Bytes[1] == (byte) 0xFF) {
				charset = "UTF-16BE";
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xEF
					&& first3Bytes[1] == (byte) 0xBB
					&& first3Bytes[2] == (byte) 0xBF) {
				charset = "UTF-8";
				checked = true;
			}
			bufferedInputStream.reset();
			if (!checked) {
				while ((read = bufferedInputStream.read()) != -1) {
					if (read >= 0xF0) {
						break;
					}
					if (0x80 <= read && read <= 0xBF) {
						break;
					}
					if (0xC0 <= read && read <= 0xDF) {
						read = bufferedInputStream.read();
						if (0x80 <= read && read <= 0xBF) {
							continue;
						} else {
							break;
						}
					} else if (0xE0 <= read && read <= 0xEF) {
						read = bufferedInputStream.read();
						if (0x80 <= read && read <= 0xBF) {
							read = bufferedInputStream.read();
							if (0x80 <= read && read <= 0xBF) {
								charset = "UTF-8";
								break;
							} else {
								break;
							}
						} else {
							break;
						}
					}
				}
			}
			bufferedInputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return charset;
	}
	public static class Sort implements Comparator<LyricLine> {

		public int compare(LyricLine line1, LyricLine line2) {
			return sortUp(line1, line2);
		}

		private int sortUp(LyricLine line1, LyricLine line2) {
			if (line1.getTimePoint() < line2.getTimePoint()) {
				return -1;
			} else if (line1.getTimePoint() > line2.getTimePoint()) {
				return 1;
			} else {
				return 0;
			}
		}
	}
}
