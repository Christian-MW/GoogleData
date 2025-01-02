package GoogleData.sheet.model;

import java.util.List;

public class UsersViralModel {
	private String user;
	private List<ViralLinkModel> items;
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public List<ViralLinkModel> getItems() {
		return items;
	}
	public void setItems(List<ViralLinkModel> items) {
		this.items = items;
	}
}
