package GoogleData.sheet.dto.request;

public class AIRequest {
	private String messageCHATGPT;
	private String searchWikipedia;
	private String spreadsheet_id;
	private String typeMessage;
	private String prompt;
	
	
	public String getMessageCHATGPT() {
		return messageCHATGPT;
	}
	public void setMessageCHATGPT(String messageCHATGPT) {
		this.messageCHATGPT = messageCHATGPT;
	}
	public String getSearchWikipedia() {
		return searchWikipedia;
	}
	public void setSearchWikipedia(String searchWikipedia) {
		this.searchWikipedia = searchWikipedia;
	}
	public String getSpreadsheet_id() {
		return spreadsheet_id;
	}
	public void setSpreadsheet_id(String spreadsheet_id) {
		this.spreadsheet_id = spreadsheet_id;
	}
	public String getTypeMessage() {
		return typeMessage;
	}
	public void setTypeMessage(String typeMessage) {
		this.typeMessage = typeMessage;
	}
	public String getPrompt() {
		return prompt;
	}
	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}
	
}
