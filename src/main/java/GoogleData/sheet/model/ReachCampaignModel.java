package GoogleData.sheet.model;

import java.util.List;

public class ReachCampaignModel {
	public List<AccountsAverageModel> accounts;
	public float max;
	public float min;
	public float average;
	
	
	public List<AccountsAverageModel> getAccounts() {
		return accounts;
	}
	public void setAccounts(List<AccountsAverageModel> accounts) {
		this.accounts = accounts;
	}
	public float getMax() {
		return max;
	}
	public void setMax(float max) {
		this.max = max;
	}
	public float getMin() {
		return min;
	}
	public void setMin(float min) {
		this.min = min;
	}
	public float getAverage() {
		return average;
	}
	public void setAverage(float average) {
		this.average = average;
	}
	
}
