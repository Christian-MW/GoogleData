package GoogleData.sheet.dto.response;

public class TwitterXItemsRequest {
	private String account;
	private String tweet;
	private String hour;
	private String status;
	private String url_Post;
	
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
	public String getHour() {
		return hour;
	}
	public void setHour(String hour) {
		this.hour = hour;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUrl_Post() {
		return url_Post;
	}
	public void setUrl_Post(String url_Post) {
		this.url_Post = url_Post;
	}
}
