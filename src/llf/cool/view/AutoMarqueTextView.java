package llf.cool.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class AutoMarqueTextView extends TextView {

	public AutoMarqueTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isFocused() {
		return true;
	}
}
