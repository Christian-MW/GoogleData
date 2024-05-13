package GoogleData.sheet.dto.response;

import java.util.List;

import GoogleData.sheet.model.DiscursiveLinesM;

public class ResponseProcessLinesChatGPT {
	
	private Integer code;
	private String message;
	private List<DiscursiveLinesM> lines;
	
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<DiscursiveLinesM> getLines() {
		return lines;
	}
	public void setLines(List<DiscursiveLinesM> lines) {
		this.lines = lines;
	}
	
}
