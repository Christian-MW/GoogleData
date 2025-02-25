package GoogleData.sheet.dto.request;

public class ConfigGoogleRequest {
	private String spreadsheet_id;
	private String spreadsheet_name;
	private String access_token;
	private String dateGenerateToken;
	private String refresh_token;
	private String dateNextGenerateToken;
	private String state;
	
	public String getSpreadsheet_id() {
		return spreadsheet_id;
	}
	public void setSpreadsheet_id(String spreadsheet_id) {
		this.spreadsheet_id = spreadsheet_id;
	}
	public String getSpreadsheet_name() {
		return spreadsheet_name;
	}
	public void setSpreadsheet_name(String spreadsheet_name) {
		this.spreadsheet_name = spreadsheet_name;
	}
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getDateGenerateToken() {
		return dateGenerateToken;
	}
	public void setDateGenerateToken(String dateGenerateToken) {
		this.dateGenerateToken = dateGenerateToken;
	}
	public String getRefresh_token() {
		return refresh_token;
	}
	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}
	public String getDateNextGenerateToken() {
		return dateNextGenerateToken;
	}
	public void setDateNextGenerateToken(String dateNextGenerateToken) {
		this.dateNextGenerateToken = dateNextGenerateToken;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
}
