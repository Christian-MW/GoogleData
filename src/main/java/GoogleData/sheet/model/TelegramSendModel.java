package GoogleData.sheet.model;

public class TelegramSendModel {

	private String from;
	private String to;
	private String token;
	private String url;
	private String caption;
	private String description;
	private String url_thumb;
	private String text;
	private int replica;
	
	
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl_thumb() {
		return url_thumb;
	}
	public void setUrl_thumb(String url_thumb) {
		this.url_thumb = url_thumb;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getReplica() {
		return replica;
	}
	public void setReplica(int replica) {
		this.replica = replica;
	}
}
