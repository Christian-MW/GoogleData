package GoogleData.sheet.dto.request;

public class SendMessageRequest {
	
	private String text;
	private String from;
	private String to;
	private String token;
	private String type;
	private String channel;
	private String url;
	private String caption;
	private String description;
	private String urlThumb;
	private int replica; 
	
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
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
	public String getUrlThumb() {
		return urlThumb;
	}
	public void setUrlThumb(String urlThumb) {
		this.urlThumb = urlThumb;
	}
	public int getReplica() {
		return replica;
	}
	public void setReplica(int replica) {
		this.replica = replica;
	}
	
}
