package GoogleData.sheet.dto.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import GoogleData.sheet.model.ObjMelt;

public class RequestProcessLinesChatGPT {
	private Map<String, List<Map<String, Object>>> accounts;
	private String prompt;
	
	
	public Map<String, List<Map<String, Object>>> getAccounts() {
		return accounts;
	}
	public void setAccounts(Map<String, List<Map<String, Object>>> accounts) {
		this.accounts = accounts;
	}
	public String getPrompt() {
		return prompt;
	}
	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}
}
