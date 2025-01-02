package GoogleData.sheet.dto.request;

import java.sql.Date;
import java.util.List;

import GoogleData.sheet.model.UsersViralModel;

public class ViralizationRequest {
	private List<UsersViralModel> items;
	private String spreadsheet_id;
	private String date;
	
	public List<UsersViralModel> getItems() {
		return items;
	}
	public void setItems(List<UsersViralModel> items) {
		this.items = items;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getSpreadsheet_id() {
		return spreadsheet_id;
	}
	public void setSpreadsheet_id(String spreadsheet_id) {
		this.spreadsheet_id = spreadsheet_id;
	}
}
