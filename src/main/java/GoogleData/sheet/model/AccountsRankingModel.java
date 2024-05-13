package GoogleData.sheet.model;

import java.util.List;

public class AccountsRankingModel {
	private List<UsersRankingModel> accounts;
	private String prompt;
	
	public List<UsersRankingModel> getAccounts() {
		return accounts;
	}
	public void setAccounts(List<UsersRankingModel> accounts) {
		this.accounts = accounts;
	}
	public String getPrompt() {
		return prompt;
	}
	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}
}
