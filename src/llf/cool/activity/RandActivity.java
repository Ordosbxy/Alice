package llf.cool.activity;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.Arrays;

import llf.cool.R;
import llf.cool.data.MyDatabase;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class RandActivity extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
	private ImageButton m_button_come;
	private TextView m_textview ;
	private static  int WINDOW_WIDTH =0;
	private static  int WINDOW_HEIGHT = 0;   
	private static final int WORDS_MODEL = 0;
	private static final int DICTIONAY_MODEL = 1;
	private int which;
	private int isSucess;
	NewWordsActivity m_wordsbook;
	MyDatabase m_base;
	Cursor cursor;
	byte[] tran_explain ;
	int length;
//    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Bundle bundle=this.getIntent().getExtras();
        Window window = getWindow();
		WindowManager.LayoutParams layoutParams = window.getAttributes();
		layoutParams.width = 320;
		layoutParams.height = 220;
		setContentView(R.layout.main_new);
        WINDOW_WIDTH = bundle.getInt("top");
        WINDOW_HEIGHT= bundle.getInt("buttom");
        String s = bundle.getString("explain");
        layoutParams.y=WINDOW_WIDTH;
        layoutParams.x=40;
        		
//        m_button_come = (ImageButton)findViewById(R.id.come);
        m_textview = (TextView)findViewById(R.id.addition);
        m_textview.setText(s);
//        m_button_come.setOnClickListener(this);
        
        new Thread()
        {
        	@Override
        	public void run()
        	{
        		try
				{
					sleep(3000);
				}
				catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Message msg = new Message();
				msg.what=0;
				System.out.println("++++++++++++++++");
				handler.sendMessage(msg);
				System.out.println("11111111111111111111111");
        		super.run();
        	}
        }.start();
       
		
    }
	
    Handler handler = new Handler()
    {
    	public void handleMessage(android.os.Message msg) 
    	{
    		switch (msg.what)
    		{
    		case 0:
    			System.out.println("--------------------");
    			RandActivity.this.finish();
    		break;
			}
    		super.handleMessage(msg);
    	};
    };
	protected void onActivityResult(int requestCode ,int resultCode,Intent data){
		switch(resultCode)
		{
			case 1:
				if(m_base != null){
					m_base.close();
					m_base = null;
				}
				finish();
		}
	}
	public void onClick(View v)
	{
		this.finish();
		
	}
}