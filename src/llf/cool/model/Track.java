package llf.cool.model;

import java.io.Serializable;

public class Track implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/*from GetResInfo*/
	private int id = 0;
	private String title = "";
	private String intro = "";
	private String create_time = "";
	private int score = 0;
	private int down_num = 0;
	private String fname = ""; /*封面:$fname.jpg 字幕:$fname.lrc 音频:$fname.mp3,*/
	private String down_url = "";

	
	/**
	 * 获取资源id
	 * @return
	 */
	public int getId(){
		return id;
	}
	public void setId(int id){
		this.id = id;
	}
	
	public String getIntro(){
		return intro;
	}
	public void setIntro(String intro){
		this.intro = intro;
	}
	
	public String getTitle(){
		return title;
	}
	public void setTitle(String title){
		this.title = title;
	}
	
	public String getCreateTime(){
		return create_time;
	}
	public void setCreateTime(String createTime){
		this.create_time = createTime;
	}
	
	public int getScore(){
		return score;
	}
	public void setScore(int score){
		this.score = score;
	}
	
	/**
	 * 下载次数
	 * @return
	 */
	public int getDownNum(){ 
		return down_num;
	}
	public void setDownNum(int downNum){
		this.down_num = downNum;
	}
	
	public String getCoverName(){
		return fname;
	}
	public void setCoverName(String fname){
		this.fname = fname;
	}
	
	public String getDownUrl(){
		return down_url;
	}
	public void setDownUrl(String downUrl){
		this.down_url = downUrl;
	}

	
}
