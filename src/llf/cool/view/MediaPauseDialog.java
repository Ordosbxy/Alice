package llf.cool.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

public class MediaPauseDialog extends AlertDialog {

	private Activity mContext = null;
	private ImageView mImageView = null;

	private static final float RATE_X = 0.6F;
	private static final float RATE_Y = 0.6F;
	private static final float RATE_ALPHA = 0.9F;

	private static final int IAMGE_ID = 0;

	protected MediaPauseDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mContext = (Activity) context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		Display display = mContext.getWindowManager().getDefaultDisplay();
		int iWidth = (int) (RATE_X * display.getWidth());
		int iHeight = (int) (RATE_Y * display.getHeight());
		System.out.println("-------w = " + iWidth + ", h = " + iHeight);

		Window window = this.getWindow();
		LayoutParams layoutParams = window.getAttributes();
		layoutParams.width = iWidth;
		layoutParams.height = iHeight;
		layoutParams.alpha = RATE_ALPHA;
		window.setAttributes(layoutParams);

		mImageView = new ImageView(mContext);
		mImageView.setId(IAMGE_ID);
		mImageView.setImageResource(android.R.drawable.ic_media_play);
		mImageView.setOnClickListener(new ImageOnClickListener());
		ViewGroup.LayoutParams imagelayoutParams = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		setContentView(mImageView, imagelayoutParams);
	}

	class ImageOnClickListener implements android.view.View.OnClickListener {
		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			switch (view.getId()) {
			case IAMGE_ID:
				MediaPauseDialog.this.cancel();
				System.out.println("-------cancel");
				break;
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			MediaPauseDialog.this.cancel();
			System.out.println("-------down");
			break;
		}
		return super.onTouchEvent(event);
	}
}
