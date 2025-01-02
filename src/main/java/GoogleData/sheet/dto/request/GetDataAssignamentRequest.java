package GoogleData.sheet.dto.request;

public class GetDataAssignamentRequest {
	private String spreadsheet_id;
	private String sheet_name;
	private String date;
	
	public String getSpreadsheet_id() {
		return spreadsheet_id;
	}
	public void setSpreadsheet_id(String spreadsheet_id) {
		this.spreadsheet_id = spreadsheet_id;
	}
	public String getSheet_name() {
		return sheet_name;
	}
	public void setSheet_name(String sheet_name) {
		this.sheet_name = sheet_name;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}
