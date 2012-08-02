package llf.cool.util;

import java.util.WeakHashMap;

import android.graphics.Bitmap;

public class ImageCache extends WeakHashMap<String, Bitmap>{ //key, value
	
	public boolean isCached(String url){
		return (containsKey(url) && (get(url) != null));
	}
}
