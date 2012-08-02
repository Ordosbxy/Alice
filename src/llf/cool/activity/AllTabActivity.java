package llf.cool.activity;

import llf.cool.R;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

public class AllTabActivity extends TabActivity implements OnCheckedChangeListener{

	private TabHost mTabHost;
	private RadioGroup mTabGroup;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_tab);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		mTabHost = this.getTabHost();
		mTabGroup = (RadioGroup)findViewById(R.id.tab_group);
		mTabGroup.setOnCheckedChangeListener(this);
		
		
		mTabHost.addTab(mTabHost.newTabSpec("Main")
				.setIndicator("") //TabWidget, we do noting.
				.setContent(new Intent(this, HomeActivity.class)));
		mTabHost.addTab(mTabHost.newTabSpec("File")
				.setIndicator("")
				.setContent(new Intent(this, FileListActivity.class)));
		mTabHost.addTab(mTabHost.newTabSpec("Dictionary")
				.setIndicator("")
				.setContent(new Intent(this, DictionaryActivity.class)));
		
		Intent intent = new Intent();
		intent.setClass(this, NewWordsActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("Key_sometable", 0);
		intent.putExtras(bundle);
		mTabHost.addTab(mTabHost.newTabSpec("NewWords")
				.setIndicator("")
				.setContent(intent));
		
		mTabHost.addTab(mTabHost.newTabSpec("Setting")
				.setIndicator("")
				.setContent(new Intent(this, SettingActivity.class)));	
	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch(checkedId){
			case R.id.tab1:
				mTabHost.setCurrentTabByTag("Main");
				break;
			case R.id.tab2:
				mTabHost.setCurrentTabByTag("File");
				break;
			case R.id.tab3:
				mTabHost.setCurrentTabByTag("Dictionary");
				break;
			case R.id.tab4:
				mTabHost.setCurrentTabByTag("NewWords");
				break;
			case R.id.tab5:
				mTabHost.setCurrentTabByTag("Setting");
				break;
			default:
				break;
		}
	}
	
	private void log(String str) {
		// TODO Auto-generated method stub
		Log.i("AllTabActivity", str);
	}
}
