package GoogleData.sheet.dto.request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class UpdateSheetRequest {
	private String spreadsheet_id;
	private String range;
	private String columns;
	public List<TreeMap<String, Object>> objectResult;
	
	
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
	public List<TreeMap<String, Object>> getObjectResult() {
		return objectResult;
	}
	public void setObjectResult(List<TreeMap<String, Object>> objectResult) {
		this.objectResult = objectResult;
	}
	
	
}
