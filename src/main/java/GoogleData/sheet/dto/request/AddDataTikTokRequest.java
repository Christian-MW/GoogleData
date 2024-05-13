package GoogleData.sheet.dto.request;

import java.util.List;

import GoogleData.sheet.model.ObjTiktok;

public class AddDataTikTokRequest {
	private String spreadsheet_id;
	private Integer type = 0;
	private List<ObjTiktok> objectResult;
	
	public String getSpreadsheet_id() {
		return spreadsheet_id;
	}
	public void setSpreadsheet_id(String spreadsheet_id) {
		this.spreadsheet_id = spreadsheet_id;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public List<ObjTiktok> getObjectResult() {
		return objectResult;
	}
	public void setObjectResult(List<ObjTiktok> objectResult) {
		this.objectResult = objectResult;
	}
}
