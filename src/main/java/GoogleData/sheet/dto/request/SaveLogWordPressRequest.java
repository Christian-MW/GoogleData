package GoogleData.sheet.dto.request;

public class SaveLogWordPressRequest {
	private String spreadsheet_id;
	private String user;
	private String blog;
	private String site;
	private String url_post;
	private String text_chatgpt;
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
	public String getBlog() {
		return blog;
	}
	public void setBlog(String blog) {
		this.blog = blog;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getUrl_post() {
		return url_post;
	}
	public void setUrl_post(String url_post) {
		this.url_post = url_post;
	}
	public String getText_chatgpt() {
		return text_chatgpt;
	}
	public void setText_chatgpt(String text_chatgpt) {
		this.text_chatgpt = text_chatgpt;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}
