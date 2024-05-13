package GoogleData.sheet.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchV2DW {
	private List<ObjDownV2> mentions;
	private List<ObjDownV2> impressions;
	private List<ObjDownV2> authors;
	private List<ObjDownV2> views;
	
	public List<ObjDownV2> getMentions() {
		return mentions;
	}
	public void setMentions(List<ObjDownV2> mentions) {
		this.mentions = mentions;
	}
	public List<ObjDownV2> getImpressions() {
		return impressions;
	}
	public void setImpressions(List<ObjDownV2> impressions) {
		this.impressions = impressions;
	}
	public List<ObjDownV2> getAuthors() {
		return authors;
	}
	public void setAuthors(List<ObjDownV2> authors) {
		this.authors = authors;
	}
	public List<ObjDownV2> getViews() {
		return views;
	}
	public void setViews(List<ObjDownV2> views) {
		this.views = views;
	}
}
