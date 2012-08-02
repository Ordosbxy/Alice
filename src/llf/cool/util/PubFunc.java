package llf.cool.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class PubFunc
{

	/**
	 * if a char is letter, return true; otherwise return false
	 * 
	 * @param c
	 * @return
	 */

	public static boolean isLetter(char c)
	{
		if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
		{
			return true;
		}
		return false;
	}

	public static boolean isEnglishWord(String word)
	{
		if (word != null && word.length() > 0)
		{
			char c = word.charAt(0);
			if (isLetter(c))
			{
				return true;
			}
		}
		return false;
	}

	public static String parseMilliseconds(long milliseconds)
	{
		Date date = new Date(milliseconds);
		date.setHours(date.getHours() - 8);

		final String template = "HH:mm:ss";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(template);
		String text = simpleDateFormat.format(date);
		return text;
	}

	public static String stringForTime(long timeMs)
	{
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		String str = formatter.format(timeMs);
		return str;
	}

	public static int parseInt(int a)
	{
		byte[] byBuf = new byte[4];

		byBuf[0] = (byte) ((a & 0xff000000) >> 24);
		byBuf[1] = (byte) ((a & 0x00ff0000) >> 16);
		byBuf[2] = (byte) ((a & 0x0000ff00) >> 8);
		byBuf[3] = (byte) (a & 0x000000ff);

		return ((byBuf[3] << 24) & 0xff000000)
				| ((byBuf[2] << 16) & 0x00ff0000)
				| ((byBuf[1] << 8) & 0x0000ff00) | (byBuf[0] & 0x000000ff);
	}

	public static void sortFileListInfo(ArrayList<File> aFileListFile)
	{
		Collections.sort(aFileListFile, new Comparator<File>()
		{
			public int compare(File file1, File file2)
			{
				if ((file1.isDirectory() && file2.isDirectory())
						|| (file1.isFile() && file2.isFile()))
				{
					return file1.getName().compareTo(file2.getName());
				}
				else if (file1.isDirectory() && file2.isFile())
				{
					return -1;
				}
				else if (file1.isFile() && file2.isDirectory())
				{
					return 1;
				}
				return 0;
			}
		});
	}

	/*
	 * ��textview�л�ͼ��ʵ��
	 */
	public static Drawable zoomDrawable(Drawable drawable, int w, int h)
	{
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap oldbmp = drawableToBitmap(drawable); // drawableת����bitmap
		Matrix matrix = new Matrix(); // ��������ͼƬ�õ�Matrix����
		float scaleWidth = ((float) w / width); // �������ű���
		float scaleHeight = ((float) h / height);
		matrix.postScale(scaleWidth, scaleHeight); // �������ű���
		Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
				matrix, true); // �����µ�bitmap���������Ƕ�ԭbitmap�����ź��ͼ
		return new BitmapDrawable(newbmp); // ��bitmapת����drawable������
	}

	public static Bitmap drawableToBitmap(Drawable drawable) // drawable
																// ת����bitmap
	{
		int width = drawable.getIntrinsicWidth(); // ȡdrawable�ĳ���
		int height = drawable.getIntrinsicHeight();
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565; // ȡdrawable����ɫ��ʽ
		Bitmap bitmap = Bitmap.createBitmap(width, height, config); // ������Ӧbitmap
		Canvas canvas = new Canvas(bitmap); // ������Ӧbitmap�Ļ���
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas); // ��drawable���ݻ���������
		return bitmap;
	}

	public static byte[] bitmaptobyte(Bitmap map)
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		map.compress(Bitmap.CompressFormat.PNG, 100, bos);
		return bos.toByteArray();
	}

	public static String isChinese(String str)
	{
		String ucode = "";
		String strChinese = "";
		String strASC = "";
		int clen;
		clen = str.length();
		String utemp = "";
		char[] strBuffer = str.toCharArray();
		int l, s;
		for (int i = 0; i < clen; i++)
		{
			s = strBuffer[i];
			utemp = Integer.toHexString(s).toLowerCase();
			l = utemp.length();
			if (l <= 2)
			{
				utemp = "00" + utemp;
				strASC += str.substring(i, i + 1);
			}
			else
			{
				strChinese += str.substring(i, i + 1);
			}
			ucode = ucode + utemp;
		}
		return strChinese;
	}

	public static String parseChar(String s)
	{
		if (s != null && s.trim().length() > 0)
		{
			String result = new String();
			int index = -1;// ���庺�ֳ��ֵ�λ�ã������-1�Ǽ���û�к���
			for (int i = s.length() - 1; i >= 0; i--)
			{

				String temp = Character.toString(s.charAt(i));// �Ӻ���ǰ��ȡÿһ��Ԫ��
				byte[] array = temp.getBytes();// ÿ��Ԫ�ص��ֽ���
				if (array.length > 1)
				{
					index = i;// ���ֳ��ֵ�λ��
					break;
				}
			}
			result = s.substring(index + 1).trim();
			return result;
		}
		return null;
	}
	public static String parseCharEng(String s)
	{
		if (s != null && s.trim().length() > 0)
		{
			String result = new String();
			int index = -1;
			for (int i = 0; i< s.length(); i++)
			{

				char c = s.toLowerCase().charAt(i);				
				if (c>'a'&&c<'z')
				{
					index = i;// ���ĳ��ֵ�λ��
				}
			}
			
			result = s.substring(index + 1,s.length()).trim();
			return result;
		}
		return null;
	}

	public static String filterEnglish(String word, String explain)
	{
		int index = 0;
		String str = "";
//		System.out.println(" word = "+word);
//		System.out.println("  explain = " + explain);
		if (!explain.equals("") && explain != null)
		{
			index = explain.indexOf("\n");
			// String ec =explain.substring(0, index);
			if (!word.equals("") && word != null)
			{
				char ch = word.toLowerCase().charAt(0);
				if (ch > 'a' && ch < 'z')
				{
					
					str = explain.substring(3, index - 3)
							+ explain.substring(index, explain.length());

				} else
				{
					str = explain.substring(2, index - 2)
							+ explain.substring(index, explain.length());

				}
			}
		}
		return str;
	}
	
	public static String filter(String explain)
	{
		String str = new String();
		for (int i = 0; i < explain.length(); i++)
		{
			System.out.println(explain.charAt(i)+" explain.charAt(i)"+explain.codePointAt(i));
			if(explain.codePointAt(i)!=02&&explain.codePointAt(i)!=15)
			{
				str+=explain.charAt(i);
			}
				
		}
		return str;
	}

	public static String Hex2GBKString(String str){
		int len = str.length();
		return str;
	}
	
}
