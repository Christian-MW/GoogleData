package GoogleData.sheet.dto.response;

import java.util.List;

public class SheetResponse {

	public List<List<Object>> objectResult;
	public String message;
	public int code;
	
	
	public List<List<Object>> getObjectResult() {
		return objectResult;
	}
	public void setObjectResult(List<List<Object>> objectResult) {
		this.objectResult = objectResult;
	}
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
}
