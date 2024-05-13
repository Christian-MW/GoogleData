package GoogleData.sheet.dto.response;

public class SendMessageChatGPTResponse {
	private String message;
	private Integer code;
	private String messageProcessed;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMessageProcessed() {
		return messageProcessed;
	}
	public void setMessageProcessed(String messageProcessed) {
		this.messageProcessed = messageProcessed;
	}
	
}
