package llf.cool.model;

import java.io.Serializable;

public class RootResource implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/*from GetSubResClass*/
	private int id = 0;
	private String title = "";
	private String icon_name = "";
	private int res_num = 0;

	public int getId(){
		return id;
	}
	public void setId(int id){
		this.id = id;
	}
	
	public String getTitle(){
		return title;
	}
	public void setTitle(String title){
		this.title = title;
	}
	
	public String getIconName(){
		return icon_name;
	}
	public void setIconName(String iconName){
		this.icon_name = iconName;
	}
	
	public int getResNum(){
		return res_num;
	}
	public void setResNum(int resNum){
		this.res_num = resNum;
	}
}
