package GoogleData.sheet.dto.request;

import java.util.List;

import GoogleData.sheet.model.PostVirModel;

public class UpdatePostMessageRequest {
	private String spreadsheet_id;
	private List<PostVirModel> post;

	public String getSpreadsheet_id() {
		return spreadsheet_id;
	}
	public void setSpreadsheet_id(String spreadsheet_id) {
		this.spreadsheet_id = spreadsheet_id;
	}
	public List<PostVirModel> getPost() {
		return post;
	}
	public void setPost(List<PostVirModel> post) {
		this.post = post;
	}
}
