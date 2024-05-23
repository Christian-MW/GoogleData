package GoogleData.sheet.model;

import java.util.ArrayList;
import java.util.List;

public class SearchV2Model {
	private String name;
	private String search;
	private long mentions;
	private long authors;
	private long views;
	private long impressions;
	private long reach;
	private SearchV2DW downloads;
	private ArrayList<Object> dataAlcance;
	//private List<DataAlcanceModel> dataAlcance;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	public long getMentions() {
		return mentions;
	}
	public void setMentions(long mentions) {
		this.mentions = mentions;
	}
	public long getAuthors() {
		return authors;
	}
	public void setAuthors(long authors) {
		this.authors = authors;
	}
	public long getViews() {
		return views;
	}
	public void setViews(long views) {
		this.views = views;
	}
	public long getImpressions() {
		return impressions;
	}
	public void setImpressions(long impressions) {
		this.impressions = impressions;
	}
	public long getReach() {
		return reach;
	}
	public void setReach(long reach) {
		this.reach = reach;
	}
	public SearchV2DW getDownloads() {
		return downloads;
	}
	public void setDownloads(SearchV2DW downloads) {
		this.downloads = downloads;
	}
	public ArrayList<Object> getDataAlcance() {
		return dataAlcance;
	}
	public void setDataAlcance(ArrayList<Object> dataAlcance) {
		this.dataAlcance = dataAlcance;
	}
	
}
