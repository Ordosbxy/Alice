package llf.cool.model.impl;

import llf.cool.model.Track;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class TrackBuilder {

	
	public Track build(JSONObject object) throws JSONException{
		
		Track track = new Track();
		track.setId(object.getInt("id"));
		track.setTitle(object.getString("title"));
		track.setIntro(object.getString("intro"));
		track.setCreateTime(object.getString("create_time"));
		track.setScore(object.getInt("score"));
		track.setDownNum(object.getInt("down_num"));
		track.setCoverName(object.getString("fname"));
		track.setDownUrl(object.getString("down_url"));
		
		Log.i("RootResourceBuilder", " ");
		Log.i("RootResourceBuilder", "id="+object.getInt("id"));
		Log.i("RootResourceBuilder", "title="+object.getString("title"));
		Log.i("RootResourceBuilder", "intro="+object.getString("intro"));
		Log.i("RootResourceBuilder", "create_time="+object.getString("create_time"));
		Log.i("RootResourceBuilder", "score="+object.getInt("score"));
		Log.i("RootResourceBuilder", "down_num="+object.getInt("down_num"));
		Log.i("RootResourceBuilder", "fname="+object.getString("fname"));
		Log.i("RootResourceBuilder", "down_url="+object.getString("down_url"));		
		Log.i("RootResourceBuilder", " ");
		
		return track;
	}
}
