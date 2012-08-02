package llf.cool.model.impl;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import llf.cool.model.RootResource;

/**
 * @author  bxy 
 * @version 创建时间：2012-7-26 上午11:08:02 
 *   
 */
public class RootResourceBuilder {
	
	public RootResource build(JSONObject object) throws JSONException{
		
		RootResource resource = new RootResource();
		resource.setId(object.getInt("id"));		
		resource.setTitle(object.getString("title"));
		resource.setIconName(object.getString("icon_name"));
		resource.setResNum(object.getInt("res_num"));
		
		Log.i("RootResourceBuilder", " ");
		Log.i("RootResourceBuilder", "id="+object.getInt("id"));
		Log.i("RootResourceBuilder", "title="+object.getString("title"));
		Log.i("RootResourceBuilder", "icon_name="+object.getString("icon_name"));
		Log.i("RootResourceBuilder", "res_num="+object.getInt("res_num"));
		Log.i("RootResourceBuilder", " ");
		return resource;
	}
	
}
