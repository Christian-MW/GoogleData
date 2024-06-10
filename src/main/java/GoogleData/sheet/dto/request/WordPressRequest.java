package GoogleData.sheet.dto.request;

public class WordPressRequest {
	private String spreadsheet_id;
	private String site;
	private String userWP;
	private String passWP;
	private String tokenChatGPT;
	private boolean changeTCGPT;
	private String email;
	
	public String getSpreadsheet_id() {
		return spreadsheet_id;
	}
	public void setSpreadsheet_id(String spreadsheet_id) {
		this.spreadsheet_id = spreadsheet_id;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getUserWP() {
		return userWP;
	}
	public void setUserWP(String userWP) {
		this.userWP = userWP;
	}
	public String getPassWP() {
		return passWP;
	}
	public void setPassWP(String passWP) {
		this.passWP = passWP;
	}
	public String getTokenChatGPT() {
		return tokenChatGPT;
	}
	public void setTokenChatGPT(String tokenChatGPT) {
		this.tokenChatGPT = tokenChatGPT;
	}
	public boolean isChangeTCGPT() {
		return changeTCGPT;
	}
	public void setChangeTCGPT(boolean changeTCGPT) {
		this.changeTCGPT = changeTCGPT;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
