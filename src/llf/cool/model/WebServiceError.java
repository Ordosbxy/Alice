package llf.cool.model;
/**
 * @author  bxy 
 * @version 创建时间：2012-7-26 上午10:37:53 
 *   
 */
public class WebServiceError extends Throwable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;

	public WebServiceError() {
	}
	
	public WebServiceError(String message) {
		this.message = message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
