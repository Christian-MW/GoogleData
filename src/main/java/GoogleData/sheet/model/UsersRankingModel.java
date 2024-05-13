package GoogleData.sheet.model;

import java.util.List;

public class UsersRankingModel {
	
	private String user;
	private String ranking;
	private String description;
	private Integer averageViews;
	private Integer averageEngagement;
	private Integer impactFactor;
	private String rankings;
	private List<DiscursiveLinesModel> discursiveLines;
	
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getRanking() {
		return ranking;
	}
	public void setRanking(String ranking) {
		this.ranking = ranking;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getAverageViews() {
		return averageViews;
	}
	public void setAverageViews(Integer averageViews) {
		this.averageViews = averageViews;
	}
	public Integer getAverageEngagement() {
		return averageEngagement;
	}
	public void setAverageEngagement(Integer averageEngagement) {
		this.averageEngagement = averageEngagement;
	}
	public Integer getImpactFactor() {
		return impactFactor;
	}
	public void setImpactFactor(Integer impactFactor) {
		this.impactFactor = impactFactor;
	}
	public String getRankings() {
		return rankings;
	}
	public void setRankings(String rankings) {
		this.rankings = rankings;
	}
	public List<DiscursiveLinesModel> getDiscursiveLines() {
		return discursiveLines;
	}
	public void setDiscursiveLines(List<DiscursiveLinesModel> discursiveLines) {
		this.discursiveLines = discursiveLines;
	}
	
}
