package GoogleData.sheet.model;

import java.util.List;

public class CampaignStreamModel {
	private String channel;
	private String search;
	private String sheet;
	private String theme;
	private String update;
	private String type;
	public List<String> users;
	private CampaignRulesModel rules;
	
	
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	public String getSheet() {
		return sheet;
	}
	public void setSheet(String sheet) {
		this.sheet = sheet;
	}
	public String getTheme() {
		return theme;
	}
	public void setTheme(String theme) {
		this.theme = theme;
	}
	public String getUpdate() {
		return update;
	}
	public void setUpdate(String update) {
		this.update = update;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public CampaignRulesModel getRules() {
		return rules;
	}
	public void setRules(CampaignRulesModel rules) {
		this.rules = rules;
	}
	public List<String> getUsers() {
		return users;
	}
	public void setUsers(List<String> users) {
		this.users = users;
	}
	
}
