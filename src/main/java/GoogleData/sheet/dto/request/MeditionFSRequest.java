package GoogleData.sheet.dto.request;

import java.util.List;

import GoogleData.sheet.model.ObjMelt;

public class MeditionFSRequest {

	public String spreadsheet_id;
	private String range;
	private String columns;
	private String slide_id;
	private List<ObjMelt> objectResult;

	
	public String getSpreadsheet_id() {
		return spreadsheet_id;
	}
	public void setSpreadsheet_id(String spreadsheet_id) {
		this.spreadsheet_id = spreadsheet_id;
	}
	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
	}
	public String getColumns() {
		return columns;
	}
	public void setColumns(String columns) {
		this.columns = columns;
	}
	public String getSlide_id() {
		return slide_id;
	}
	public void setSlide_id(String slide_id) {
		this.slide_id = slide_id;
	}
	public List<ObjMelt> getObjectResult() {
		return objectResult;
	}
	public void setObjectResult(List<ObjMelt> objectResult) {
		this.objectResult = objectResult;
	}
}
