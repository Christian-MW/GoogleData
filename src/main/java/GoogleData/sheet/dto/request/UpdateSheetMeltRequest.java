package GoogleData.sheet.dto.request;

import java.util.List;
import java.util.TreeMap;

import GoogleData.sheet.model.*;

public class UpdateSheetMeltRequest {
	private String spreadsheet_id;
	private String range;
	private String columns;
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
	public List<ObjMelt> getObjectResult() {
		return objectResult;
	}
	public void setObjectResult(List<ObjMelt> objectResult) {
		this.objectResult = objectResult;
	}
}
