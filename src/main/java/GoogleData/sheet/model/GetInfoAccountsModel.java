package GoogleData.sheet.model;

import java.util.List;

public class GetInfoAccountsModel {
	private String type;
	private List<String> users;
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<String> getUsers() {
		return users;
	}
	public void setUsers(List<String> users) {
		this.users = users;
	}
}
