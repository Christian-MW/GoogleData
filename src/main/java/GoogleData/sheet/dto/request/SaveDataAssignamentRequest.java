package GoogleData.sheet.dto.request;

import java.util.List;

import GoogleData.sheet.model.SaveAssignModel;

public class SaveDataAssignamentRequest {
	private String spreadsheet_id;
	private Integer max_assign;
	private String date;
	private List<SaveAssignModel> items;
	
	public String getSpreadsheet_id() {
		return spreadsheet_id;
	}
	public void setSpreadsheet_id(String spreadsheet_id) {
		this.spreadsheet_id = spreadsheet_id;
	}
	public Integer getMax_assign() {
		return max_assign;
	}
	public void setMax_assign(Integer max_assign) {
		this.max_assign = max_assign;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public List<SaveAssignModel> getItems() {
		return items;
	}
	public void setItems(List<SaveAssignModel> items) {
		this.items = items;
	}
}
