package llf.cool.activity;

import java.util.ArrayList;

import llf.cool.R;
import llf.cool.adapter.GridViewAdapter;
import llf.cool.view.RollBackGridView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView mTv_title = null;
	private RollBackGridView mGridView = null;
	private GridViewAdapter mGridAdapter = null;

	private ArrayList<String> typeEntryList = new ArrayList<String>();

	public static String[] TYPE_ITEMS = null;

	public static final int[] TYPE_ICONS = { R.drawable.world,
			R.drawable.music, R.drawable.school, R.drawable.speech };

	public static final String EXTRA_KEY_GRID_POSITION = "grid_position";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTv_title = (TextView) findViewById(R.id.main_tv_title);
		mGridView = (RollBackGridView) findViewById(R.id.main_gridView_type);

		TYPE_ITEMS = getResources().getStringArray(R.array.type_item_names);
		mTv_title.setText(R.string.app_name);

		mGridAdapter = new GridViewAdapter(this);
		mGridView.setAdapter(mGridAdapter);
		mGridView.setOnItemClickListener(new GridOnItemClickListener());
	}

	public class GridOnItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS,
					HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
			view.playSoundEffect(SoundEffectConstants.CLICK);

			Intent intent = new Intent(MainActivity.this,
					FileListActivity.class);
			intent.putExtra("position", position);
			//startActivity(intent);
		}
	}

	public ArrayList<String> getTypeEntryList() {
		return typeEntryList;
	}

	public void setTypeEntryList(ArrayList<String> typeEntryList) {
		this.typeEntryList = typeEntryList;
	}

}
