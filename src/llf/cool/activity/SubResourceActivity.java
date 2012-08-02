package llf.cool.activity;

import java.util.ArrayList;

import llf.cool.CoolApplication;
import llf.cool.R;
import llf.cool.adapter.SubResourceAdapter;
import llf.cool.model.Track;
import llf.cool.view.RollBackListView;
import llf.cool.view.AlertDialog;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

import com.markupartist.android.widget.ActionBar;

public class SubResourceActivity extends Activity implements OnItemClickListener{
	
	ActionBar mActionBar;
	RollBackListView mSubResourceList;
	SubResourceAdapter mSubResourceAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_subresource);
		
		mActionBar = (ActionBar)findViewById(R.id.actionbar_of_subresource);
		mSubResourceList = (RollBackListView)findViewById(R.id.list_of_sub_resource);
		mSubResourceList.setOnItemClickListener(this);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Intent intent = getIntent();

		@SuppressWarnings("unchecked")
		ArrayList<Track> tracklist = (ArrayList<Track>)(intent.getSerializableExtra("trackslist"));;
		if(tracklist != null){
			mSubResourceAdapter = new SubResourceAdapter(this);
			mSubResourceAdapter.setList(tracklist);
			mSubResourceList.setAdapter(mSubResourceAdapter);			
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		final Track track = mSubResourceAdapter.getList().get(position);
		
//		final AlertDialog dialog = new AlertDialog.Builder(SubResourceActivity.this).create();
//		dialog.show();
//		Window window = dialog.getWindow();
//		window.setGravity(Gravity.BOTTOM);
//		window.setContentView(R.layout.my_dialog);
//		window.setWindowAnimations(R.style.my_style);
//		window.setBackgroundDrawableResource(R.drawable.dialog_bg);
		
		
		final AlertDialog alertDialog = new AlertDialog(SubResourceActivity.this);
		alertDialog.setPositive1Button(getString(R.string.favorites), new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// ’≤ÿ
			}
		});
		alertDialog.setPositive2Button(getString(R.string.download), new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//œ¬‘ÿ
				CoolApplication.getInstance().getDownloadManager().download(track);
			}
		});
		alertDialog.setNegativeButton(getString(R.string.cancel), new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alertDialog.dismiss();
			}
		});
		
	}

	private void log(String str) {
		// TODO Auto-generated method stub
		Log.i("SubResourceActivity", str);
	}
}
