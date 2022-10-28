package GoogleData.sheet.dto.response;

import java.util.ArrayList;

public class GetListSheetsResponse {
	private String message;
	private int code;
	private ArrayList<String> ListSheets;
	
	
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
	public ArrayList<String> getListSheets() {
		return ListSheets;
	}
	public void setListSheets(ArrayList<String> listSheets) {
		ListSheets = listSheets;
	}
}
