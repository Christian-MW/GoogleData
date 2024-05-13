package GoogleData.sheet.dto.response;

public class AIResponse {

	private Object messageWikipedia;
	private String messageChatGTP;
	private int code;
	
	
	public Object getMessageWikipedia() {
		return messageWikipedia;
	}
	public void setMessageWikipedia(Object messageWikipedia) {
		this.messageWikipedia = messageWikipedia;
	}
	public String getMessageChatGTP() {
		return messageChatGTP;
	}
	public void setMessageChatGTP(String messageChatGTP) {
		this.messageChatGTP = messageChatGTP;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
}
