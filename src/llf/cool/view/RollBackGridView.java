package llf.cool.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.animation.TranslateAnimation;
import android.widget.GridView;

public class RollBackGridView extends GridView {

	private boolean outBound = false;
	private int distance;
	private int firstOut;

	public RollBackGridView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public RollBackGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	GestureDetector gestureDetector = new GestureDetector(
			new OnGestureListener() {

				// @Override
				public boolean onDown(MotionEvent e) {
					// TODO Auto-generated method stub
					return false;
				}

				// @Override
				public boolean onFling(MotionEvent e1, MotionEvent e2,
						float velocityX, float velocityY) {
					// TODO Auto-generated method stub
					return false;
				}

				// @Override
				public void onLongPress(MotionEvent e) {
					// TODO Auto-generated method stub

				}

				// @Override
				public boolean onScroll(MotionEvent e1, MotionEvent e2,
						float distanceX, float distanceY) {
					int firstPos = getFirstVisiblePosition(); // ?????item??index
					int lastPos = getLastVisiblePosition(); // ??????item??index
					int itemCount = getCount(); // ?????item??

					if (outBound && firstPos != 0 && lastPos != (itemCount - 1)) {
						scrollTo(0, 0);
						return false;
					}

					View firstView = getChildAt(firstPos);
					View lastView = getChildAt(itemCount - 1);

					if (!outBound) {
						firstOut = 35;
					}
					if (firstView != null
							&& (outBound || (firstPos == 0
									&& firstView.getTop() == 0 && distanceY < 0))) {
						distance = (int) (firstOut - e2.getRawY());
						scrollBy(0, distance / 2);
						return true;
					}
					if (lastView == null
							&& (outBound || (lastPos == itemCount - 1 && distanceY > 0))) {
						distance = (int) ((itemCount - 1) / 2 - e2.getRawY());
						scrollBy(0, distance / 2);
						return true;
					}
					return false;
				}

				// @Override
				public void onShowPress(MotionEvent e) {
					// TODO Auto-generated method stub

				}

				// @Override
				public boolean onSingleTapUp(MotionEvent e) {
					// TODO Auto-generated method stub
					return false;
				}
			});

	public boolean onTouchEvent(MotionEvent ev) {
		if (getFirstVisiblePosition() == 0) {
			int act = ev.getAction();
			if ((act == MotionEvent.ACTION_UP || act == MotionEvent.ACTION_UP)
					&& outBound) {
				outBound = false;
			}
			if (!gestureDetector.onTouchEvent(ev)) {
				outBound = false;
			} else {
				outBound = true;
			}

			if (outBound) {
				Rect rect = new Rect();
				getLocalVisibleRect(rect);
				TranslateAnimation am = new TranslateAnimation(0, 0, -rect.top,
						0);
				am.setDuration(300);
				startAnimation(am);
				scrollTo(0, 0);
			}

		}
		if (getLastVisiblePosition() == getCount() - 1) {
			int act = ev.getAction();
			if ((act == MotionEvent.ACTION_DOWN || act == MotionEvent.ACTION_UP)
					&& outBound) {
				outBound = false;
			}
			if (!gestureDetector.onTouchEvent(ev)) {
				outBound = false;
			} else {
				outBound = true;
			}
			if (outBound) {
				Rect rect1 = new Rect();
				getLocalVisibleRect(rect1);
				TranslateAnimation am1 = new TranslateAnimation(0, 0,
						(-rect1.top - rect1.bottom) / 2, getCount() - 1);
				am1.setDuration(300);
				startAnimation(am1);
				scrollTo(0, 0);
			}
		}
		return super.onTouchEvent(ev);
	};
}
