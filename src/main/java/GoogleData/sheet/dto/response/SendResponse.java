package GoogleData.sheet.dto.response;

public class SendResponse {

	private String code;
	private String message;
	private boolean sended;
	private String uid;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isSended() {
		return sended;
	}
	public void setSended(boolean sended) {
		this.sended = sended;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public SendResponse(String code, String message, boolean sended, String uid) {
		super();
		this.code = code;
		this.message = message;
		this.sended = sended;
		this.uid = uid;
	}
}
