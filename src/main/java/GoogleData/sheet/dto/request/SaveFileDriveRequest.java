package GoogleData.sheet.dto.request;

import java.util.List;

import GoogleData.sheet.model.FileTweetsMeltModel;

public class SaveFileDriveRequest {
	private String user;
	private String spreadsheet_id;
	private List<FileTweetsMeltModel> items;
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getSpreadsheet_id() {
		return spreadsheet_id;
	}
	public void setSpreadsheet_id(String spreadsheet_id) {
		this.spreadsheet_id = spreadsheet_id;
	}
	public List<FileTweetsMeltModel> getItems() {
		return items;
	}
	public void setItems(List<FileTweetsMeltModel> items) {
		this.items = items;
	}
}
