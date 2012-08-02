package llf.cool.util.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import llf.cool.model.Track;
import android.os.AsyncTask;
import android.util.Log;

public class DownloadTask extends AsyncTask<Void, Integer, Boolean>{

	DownloadWrapper mProcess;
	
	public DownloadTask(DownloadWrapper process) {
		// TODO Auto-generated method stub
		mProcess = process;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		mProcess.notifyDownloadStarted();
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO Auto-generated method stub		
		
		try {
			return downloadFile(mProcess);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}	

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

	/*下载+*/
	private Boolean downloadFile(DownloadWrapper process) throws IOException {
		// TODO Auto-generated method stub
		Track track = process.getDownloadEntry();
		int resId = track.getId();
		String path = process.getDownloadPath();
		String str = DownloadHelper.DOWNLOAD_URL 
							   + "?i=" + resId + "z" 
								 + DownloadHelper.USER_NAME ;
		
		URL downloadURL  = new URL(str);
		//使用 HttpURLConnection 打开连接
		HttpURLConnection con = (HttpURLConnection) downloadURL.openConnection();   /*URL.openStream();*/
//		//获取读到的内容（流）InputStreamReader: A class for turning a byte stream into a character stream.
//		InputStreamReader in = new InputStreamReader(con.getInputStream()); 
		con.setRequestMethod("GET");
		con.setDoOutput(true);
		con.connect();
		process.setTotalSize(con.getContentLength()); //设置大小
		
		String fileName = track.getTitle() + process.getDownloadFormat();
		
		try {
			boolean success = (new File(path)).mkdirs();
			if(success) logi("下载目录：" + path + "创建成功.");
		} catch (Exception e) {
			// TODO: handle exception
			loge("创建下载目录失败.");
			return false;
		}
		
		//创建写文件流
		FileOutputStream fout = new FileOutputStream(new File(path, fileName));
		InputStream in = con.getInputStream();
		if(in == null) return false;
		
		byte[] buffer = new byte[1024];  //控制一次读多少
		int len = 0;
		while((len=in.read(buffer)) > 0){
			fout.write(buffer, 0, len);
			process.setDownloadedSize(len);
		}
		fout.close();
		logi("下载完成.");
		return false;
	}
	
	private void logi(String str){
		Log.i("DownloadTask", str);
	}
	
	private void loge(String str){
		Log.e("DownloadTask", str);
	}
}
