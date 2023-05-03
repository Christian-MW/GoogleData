package GoogleData.sheet.model;

import java.util.List;

public class ReachScrapperModel {
	public List<String> account;
	public String spreadsheetId;
	
	
	public List<String> getAccount() {
		return account;
	}
	public void setAccount(List<String> account) {
		this.account = account;
	}
	public String getSpreadsheetId() {
		return spreadsheetId;
	}
	public void setSpreadsheetId(String spreadsheetId) {
		this.spreadsheetId = spreadsheetId;
	}
}
