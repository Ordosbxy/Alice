package llf.cool.util.download;

import android.os.Environment;

public class DownloadHelper {
	
	public final static String DOWNLOAD_URL = "http://www.wqd56.com/yytt/download.php";
	public final static String USER_NAME = "93bf759f723fd83d56ed381ea40139122c60273e1660689a9d7e21f0cd69ec3d@94df9d0d35cd3388";
	
	
	public static String getDownloadPath(){
		return Environment.getExternalStorageDirectory().getAbsolutePath() 
				+ "/yingyutt";
	}
	
	public static String getDownloadFormat(){
		return ".zip";
	}
}
