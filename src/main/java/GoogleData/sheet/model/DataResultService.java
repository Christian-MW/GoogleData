package GoogleData.sheet.model;

import java.util.List;

public class DataResultService {
	private List<ProyectModel> result;
    private int code;
    private String message;
    
	public List<ProyectModel> getResult() {
		return result;
	}
	public void setResult(List<ProyectModel> result) {
		this.result = result;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
