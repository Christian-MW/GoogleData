package GoogleData.sheet.model;

public class ViralLinkModel {
	private String link;
	private boolean status;
	private String dateDisplay;
	
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getDateDisplay() {
		return dateDisplay;
	}
	public void setDateDisplay(String dateDisplay) {
		this.dateDisplay = dateDisplay;
	}
}
