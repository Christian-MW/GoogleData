package GoogleData.sheet.dto.request;

import GoogleData.sheet.model.ReportVisualModel;

public class TestRequest {
	private String spreadsheet_id;
	private String message;
	private String sheet_name;
	private ReportVisualModel item;
	
	public String getSpreadsheet_id() {
		return spreadsheet_id;
	}
	public void setSpreadsheet_id(String spreadsheet_id) {
		this.spreadsheet_id = spreadsheet_id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getSheet_name() {
		return sheet_name;
	}
	public void setSheet_name(String sheet_name) {
		this.sheet_name = sheet_name;
	}
	public ReportVisualModel getItem() {
		return item;
	}
	public void setItem(ReportVisualModel item) {
		this.item = item;
	}
}
