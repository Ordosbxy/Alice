package llf.cool.activity;

import llf.cool.R;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

public class AnIntroductionActivity extends Activity{

	private Animation mEndAnimation;
	private Handler mEndAnimationHandler;
	private Runnable mEndAnimationRunnable;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		System.out.println(">>>AnIntroductionActivity onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_introduction);
		final LinearLayout cover = (LinearLayout)findViewById(R.id.cover);
		
		mEndAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
		mEndAnimation.setFillAfter(true);/*动画结束时，停留在最后一帧*/  
		
		mEndAnimation.setAnimationListener(new AnimationListener() {			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub				
			}			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub				
			}			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				startActivity(new Intent(AnIntroductionActivity.this, 
											AllTabActivity.class));
				AnIntroductionActivity.this.finish();
			}
		});
		
		mEndAnimationHandler = new Handler();
		mEndAnimationRunnable = new Runnable() {			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				cover.startAnimation(mEndAnimation);
			}
		};
		
		mEndAnimationHandler.removeCallbacks(mEndAnimationRunnable);
		mEndAnimationHandler.postDelayed(mEndAnimationRunnable, 50);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		//do nothing
	}
}
