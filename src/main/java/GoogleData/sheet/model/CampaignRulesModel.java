package GoogleData.sheet.model;

import java.util.List;

public class CampaignRulesModel {
	private String endDate;
	private List<String> language;
	private List<String> location;
	private Integer maxFollowers;
	private Integer maxNumReactions;
	private Integer minFollowers;
	private Integer minNumReactions;
	private String startDate;
	private List<String> typeTweet;
	private String verifiedUser;
	
	
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public List<String> getLanguage() {
		return language;
	}
	public void setLanguage(List<String> language) {
		this.language = language;
	}
	public List<String> getLocation() {
		return location;
	}
	public void setLocation(List<String> location) {
		this.location = location;
	}
	public Integer getMaxFollowers() {
		return maxFollowers;
	}
	public void setMaxFollowers(Integer maxFollowers) {
		this.maxFollowers = maxFollowers;
	}
	public Integer getMaxNumReactions() {
		return maxNumReactions;
	}
	public void setMaxNumReactions(Integer maxNumReactions) {
		this.maxNumReactions = maxNumReactions;
	}
	public Integer getMinFollowers() {
		return minFollowers;
	}
	public void setMinFollowers(Integer minFollowers) {
		this.minFollowers = minFollowers;
	}
	public Integer getMinNumReactions() {
		return minNumReactions;
	}
	public void setMinNumReactions(Integer minNumReactions) {
		this.minNumReactions = minNumReactions;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public List<String> getTypeTweet() {
		return typeTweet;
	}
	public void setTypeTweet(List<String> typeTweet) {
		this.typeTweet = typeTweet;
	}
	public String getVerifiedUser() {
		return verifiedUser;
	}
	public void setVerifiedUser(String verifiedUser) {
		this.verifiedUser = verifiedUser;
	}
}
