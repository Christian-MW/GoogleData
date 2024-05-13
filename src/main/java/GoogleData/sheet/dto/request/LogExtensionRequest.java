package GoogleData.sheet.dto.request;

public class LogExtensionRequest {
	private String email;
	private String date;
	private String module;
	private String control;
	private String spreadsheet_id;
	
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getControl() {
		return control;
	}
	public void setControl(String control) {
		this.control = control;
	}
	public String getSpreadsheet_id() {
		return spreadsheet_id;
	}
	public void setSpreadsheet_id(String spreadsheet_id) {
		this.spreadsheet_id = spreadsheet_id;
	}
}
