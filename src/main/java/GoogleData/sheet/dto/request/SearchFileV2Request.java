package GoogleData.sheet.dto.request;

import java.util.List;

import GoogleData.sheet.model.SearchV2Model;

public class SearchFileV2Request {
	private String spreadsheet_id;
	private List<SearchV2Model> objectResult;
	
	
	public String getSpreadsheet_id() {
		return spreadsheet_id;
	}
	public void setSpreadsheet_id(String spreadsheet_id) {
		this.spreadsheet_id = spreadsheet_id;
	}
	public List<SearchV2Model> getObjectResult() {
		return objectResult;
	}
	public void setObjectResult(List<SearchV2Model> objectResult) {
		this.objectResult = objectResult;
	}
}
