package GoogleData.sheet.model;

import java.util.List;

public class DiscursiveLinesM {
	private String account;
	private List<DiscursiveLinesModel> lines;
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public List<DiscursiveLinesModel> getLines() {
		return lines;
	}
	public void setLines(List<DiscursiveLinesModel> lines) {
		this.lines = lines;
	}
}
