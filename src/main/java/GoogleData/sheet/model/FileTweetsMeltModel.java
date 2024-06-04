package GoogleData.sheet.model;

public class FileTweetsMeltModel {
	private String date;
	private String author;
	private String url;
	private String body;
	private Long reach;
	private Long ave;
	private Long engagement;
	private Long likes;
	private Long rts;
	private Long qts;
	private Long views;
	private String sentiment;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public Long getReach() {
		return reach;
	}
	public void setReach(Long reach) {
		this.reach = reach;
	}
	public Long getAve() {
		return ave;
	}
	public void setAve(Long ave) {
		this.ave = ave;
	}
	public Long getEngagement() {
		return engagement;
	}
	public void setEngagement(Long engagement) {
		this.engagement = engagement;
	}
	public Long getLikes() {
		return likes;
	}
	public void setLikes(Long likes) {
		this.likes = likes;
	}
	public Long getRts() {
		return rts;
	}
	public void setRts(Long rts) {
		this.rts = rts;
	}
	public Long getQts() {
		return qts;
	}
	public void setQts(Long qts) {
		this.qts = qts;
	}
	public Long getViews() {
		return views;
	}
	public void setViews(Long views) {
		this.views = views;
	}
	public String getSentiment() {
		return sentiment;
	}
	public void setSentiment(String sentiment) {
		this.sentiment = sentiment;
	}
}
