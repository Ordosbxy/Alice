package llf.cool.activity;

import java.util.ArrayList;

import llf.cool.R;
import llf.cool.adapter.RootResourceAdapter;
import llf.cool.model.RootResource;
import llf.cool.model.Track;
import llf.cool.model.WebService;
import llf.cool.model.WebServiceError;
import llf.cool.model.impl.WebServiceImpl;
import llf.cool.view.RollBackListView;

import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.markupartist.android.widget.ActionBar;

public class HomeActivity extends Activity{
	
	private ActionBar mActionBar;
	//private ProgressDialog mRootResourceDialog;
	private ProgressDialog mSubResourceDialog;
	private RollBackListView mRootResourceList;
	private RootResourceAdapter mRootResourceAdapter;
	
	public static String[] TYPE_ITMES = null;
	public static int[] TYPE_ICONS = new int[]{
		R.drawable.world, 
		R.drawable.music,
		R.drawable.school,
		R.drawable.speech,
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		mActionBar = (ActionBar)findViewById(R.id.actionbar_of_home);
		mActionBar.setTitle(R.string.app_name);
		mActionBar.setHomeLogo(R.drawable.icon);
		//mActionBar.addAction(new IntentAction(this, ));
		
		mRootResourceList = (RollBackListView)findViewById(R.id.list_of_net_resource);
		mRootResourceList.setOnItemClickListener(new ListOnItemClickListener());		
		TYPE_ITMES = getResources().getStringArray(R.array.type_item_names);
			
		new ResourceSearchTask().execute((Void[])null);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}
	
	private class ResourceSearchTask extends AsyncTask<Void, Void, RootResource[]> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			log("onPreExecute");
			mActionBar.setProgressBarVisibility(View.VISIBLE);
		}
		@Override
		protected RootResource[] doInBackground(Void... params) {
			// TODO Auto-generated method stub
			log("doInBackground");
			WebService webService = new WebServiceImpl();
			RootResource[] rootResources = null;
			try {
				rootResources = webService.getSubResClass(0);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (WebServiceError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return rootResources;
		}
		@Override
		protected void onPostExecute(RootResource[] rootResources) {
			// TODO Auto-generated method stub
			log("onPostExecute");
			log("rootResources="+rootResources);
			mActionBar.setProgressBarVisibility(View.GONE);
			if(rootResources != null) {
				mRootResourceAdapter = new RootResourceAdapter(HomeActivity.this);
				mRootResourceAdapter.setList(rootResources);
				mRootResourceAdapter.setListView(mRootResourceList);
				mRootResourceList.setAdapter(mRootResourceAdapter);						
			} else {
				//
			}
			super.onPostExecute(rootResources);
		}		
	}
	
	private class ListOnItemClickListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			int rootId = mRootResourceAdapter.getList().get(position).getId();
			int resNum = mRootResourceAdapter.getList().get(position).getResNum();
			new SubResourceSearchTask().execute(rootId, resNum);
		}
	}

	private class SubResourceSearchTask extends AsyncTask<Integer, Void, ArrayList<Track>>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mSubResourceDialog = new ProgressDialog(HomeActivity.this);
			mSubResourceDialog.setTitle(R.string.progress_dialog_title);
			mSubResourceDialog.setMessage(getString(R.string.progress_dialog_search));
			mSubResourceDialog.show();
		}
		
		@Override
		protected ArrayList<Track> doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			WebService webService = new WebServiceImpl();
			
			int rootId = params[0];
			int resNum = params[1];
			log(">>>doInBackground getResInfo "+rootId+", resNum="+resNum);
			Track[] tracks = null;				
			try {
				tracks = webService.getResInfo(rootId, 0, resNum, null); //"time>20110101000000"
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (WebServiceError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
						
//			Hashtable<Integer, PlaylistEntry> hashtable = new Hashtable<Integer, PlaylistEntry>();
//			hashtable.put(tracks[i].getId(), playlistEntry);
//			Playlist playlist = new Playlist();
//			for(int i=0; i<tracks.length; i++){
//				PlaylistEntry playlistEntry = new PlaylistEntry();
//				playlistEntry.setTrack(tracks[i]);
//				playlist.addPlaylistEntry(playlistEntry);
//			}
			ArrayList<Track> tracklist = new ArrayList<Track>();
			for(Track track : tracks){
				tracklist.add(track);
			}
			return tracklist;
		}
		
		@Override
		protected void onPostExecute(ArrayList<Track> tracklist) {
			// TODO Auto-generated method stub
			super.onPostExecute(tracklist);
			if(mSubResourceDialog!=null && mSubResourceDialog.isShowing()){
				mSubResourceDialog.dismiss();
			}
			
			if(tracklist != null) {
				log("tracklist="+tracklist);
				Intent intent = new Intent(HomeActivity.this, SubResourceActivity.class);
				intent.putExtra("trackslist", tracklist);
				startActivity(intent);
			}
		}
		
	}
	
	private void log(String str) {
		// TODO Auto-generated method stub
		Log.i("HomeActivity", str);
	}
}
