package GoogleData.sheet.dto.response;

import java.util.List;

public class GetUsersCampaignResponse {
	private String message;
	private int code;
	List<String> users;
	
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public List<String> getUsers() {
		return users;
	}
	public void setUsers(List<String> users) {
		this.users = users;
	}
}
