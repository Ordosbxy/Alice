package llf.cool.view;

import llf.cool.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class AlertDialog {
	
	private Context mContext;	
	private android.app.AlertDialog mAlertDialog;
	private TextView mTitleView;
	private TextView mMessageView;
	private LinearLayout mButtonLayout;
	
	private int mMargins = 0;
	
	public AlertDialog(Context context) {
		// TODO Auto-generated constructor stub
		mContext=context;
		mAlertDialog=new android.app.AlertDialog.Builder(context).create();
		mAlertDialog.show();
		
		//使用window.setContentView,替换整个对话框窗口的布局
		Window window = mAlertDialog.getWindow();
		window.setGravity(Gravity.BOTTOM);
		window.setContentView(R.layout.alert_dialog);
		window.setWindowAnimations(R.style.my_style);
		window.setBackgroundDrawableResource(R.drawable.dialog_bg);
		mMargins = formatDipToPx(mContext, 10);
		
		mTitleView=(TextView)window.findViewById(R.id.title);
		mMessageView=(TextView)window.findViewById(R.id.message);
		mButtonLayout=(LinearLayout)window.findViewById(R.id.buttonLayout);
	}
	public void setTitle(int resId){
		mTitleView.setText(resId);
		mTitleView.setVisibility(View.VISIBLE);
	}
	
	public void setTitle(String title) {
		mTitleView.setText(title);
		mTitleView.setVisibility(View.VISIBLE);
	}
	
	public void setMessage(int resId) {
		mMessageView.setText(resId);
		mMessageView.setVisibility(View.VISIBLE);
	}
 
	public void setMessage(String message){
		mMessageView.setText(message);
		mMessageView.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 设置按钮
	 * @param text
	 * @param listener
	 */
	public void setPositive1Button(String text, final View.OnClickListener listener)
	{
		Button button=new Button(mContext);
		LinearLayout.LayoutParams params=new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(mMargins, 0, mMargins, 0);
		button.setLayoutParams(params);
		//button.setBackgroundResource(R.drawable.alertdialog_button);
		button.setText(text);
		button.setTextColor(Color.BLACK); //0xff4d4d4d
		button.setTextSize(20);
		button.setOnClickListener(listener);
		mButtonLayout.addView(button);
	}
	
	/**
	 * 设置按钮
	 * @param text
	 * @param listener
	 */
	public void setPositive2Button(String text, final View.OnClickListener listener)
	{
		Button button=new Button(mContext);
		LinearLayout.LayoutParams params=new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(mMargins, formatDipToPx(mContext, 10), mMargins, 0);
		button.setLayoutParams(params);
		//button.setBackgroundResource(R.drawable.alertdialog_button);
		button.setText(text);
		button.setTextColor(Color.BLACK);
		button.setTextSize(20);
		button.setOnClickListener(listener);
		mButtonLayout.addView(button, 1);
	}
 
	/**
	 * 设置按钮
	 * @param text
	 * @param listener
	 */
	public void setNegativeButton(String text,final View.OnClickListener listener)
	{
		Button button=new Button(mContext);
		LinearLayout.LayoutParams params=new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		button.setLayoutParams(params);
		//button.setBackgroundResource(R.drawable.alertdialog_button);
		button.setText(text);
		button.setTextColor(Color.BLACK);
		button.setTextSize(20);
		button.setOnClickListener(listener);

		if(mButtonLayout.getChildCount()>0)
		{
			params.setMargins(mMargins, formatDipToPx(mContext, 20), mMargins, 0);
			button.setLayoutParams(params);
			mButtonLayout.addView(button, 2);
		}else{
			button.setLayoutParams(params);
			mButtonLayout.addView(button);
		}
 
	}
	/**
	 * 关闭对话框
	 */
	public void dismiss() {
		mAlertDialog.dismiss();
	}
	
	public int formatDipToPx(Context context, int dip) {
		DisplayMetrics dm = new DisplayMetrics();   
		((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(dm); 
		return (int) FloatMath.ceil( dip * dm.density);
	}
 
}
