package GoogleData.sheet.dto.request;

public class UpdateTweetRequest {
	private String user;
	private String account;
	private String tweet;
	private String urlPost;
	private String status;
	private String spreadsheet_id;
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getTweet() {
		return tweet;
	}
	public void setTweet(String tweet) {
		this.tweet = tweet;
	}
	public String getUrlPost() {
		return urlPost;
	}
	public void setUrlPost(String urlPost) {
		this.urlPost = urlPost;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSpreadsheet_id() {
		return spreadsheet_id;
	}
	public void setSpreadsheet_id(String spreadsheet_id) {
		this.spreadsheet_id = spreadsheet_id;
	}
}
