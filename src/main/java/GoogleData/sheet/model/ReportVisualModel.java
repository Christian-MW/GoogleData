package GoogleData.sheet.model;

import java.util.List;

public class ReportVisualModel {
	private String title;
	private Integer alcance;
	private Integer mentions;
	private Integer post;
	private List<String> stakeholders;
	private List<String> lines;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getAlcance() {
		return alcance;
	}
	public void setAlcance(Integer alcance) {
		this.alcance = alcance;
	}
	public Integer getMentions() {
		return mentions;
	}
	public void setMentions(Integer mentions) {
		this.mentions = mentions;
	}
	public Integer getPost() {
		return post;
	}
	public void setPost(Integer post) {
		this.post = post;
	}
	public List<String> getStakeholders() {
		return stakeholders;
	}
	public void setStakeholders(List<String> stakeholders) {
		this.stakeholders = stakeholders;
	}
	public List<String> getLines() {
		return lines;
	}
	public void setLines(List<String> lines) {
		this.lines = lines;
	}
}
