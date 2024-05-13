package GoogleData.sheet.dto.request;

import java.util.List;

import GoogleData.sheet.model.ObjMelt;
import GoogleData.sheet.model.ObjMeltSearch;

public class MeditionFSV2Request {
	public String spreadsheet_id;
	private String range;
	private String columns;
	private String slide_id;
	private Integer numberSlide;
	private List<ObjMelt> objectResult;
	private List<ObjMeltSearch> objectSearch;
	
	
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
	public Integer getNumberSlide() {
		return numberSlide;
	}
	public void setNumberSlide(Integer numberSlide) {
		this.numberSlide = numberSlide;
	}
	public List<ObjMelt> getObjectResult() {
		return objectResult;
	}
	public void setObjectResult(List<ObjMelt> objectResult) {
		this.objectResult = objectResult;
	}
	public List<ObjMeltSearch> getObjectSearch() {
		return objectSearch;
	}
	public void setObjectSearch(List<ObjMeltSearch> objectSearch) {
		this.objectSearch = objectSearch;
	}
}
