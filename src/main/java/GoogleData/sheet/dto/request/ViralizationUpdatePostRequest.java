package GoogleData.sheet.dto.request;

public class ViralizationUpdatePostRequest {
	private String spreadsheet_id;
	private String user;
	private String post;
	private String date;
	
	public String getSpreadsheet_id() {
		return spreadsheet_id;
	}
	public void setSpreadsheet_id(String spreadsheet_id) {
		this.spreadsheet_id = spreadsheet_id;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}
