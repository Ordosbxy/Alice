package llf.cool.model;
/**
 * @author  bxy 
 * @version ����ʱ�䣺2012-7-26 ����10:37:53 
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
