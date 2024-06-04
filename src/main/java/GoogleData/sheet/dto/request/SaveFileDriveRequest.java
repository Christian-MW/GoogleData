package GoogleData.sheet.dto.request;

import java.util.List;

import GoogleData.sheet.model.FileTweetsMeltModel;

public class SaveFileDriveRequest {
	private String user;
	private List<FileTweetsMeltModel> items;
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public List<FileTweetsMeltModel> getItems() {
		return items;
	}
	public void setItems(List<FileTweetsMeltModel> items) {
		this.items = items;
	}
}
