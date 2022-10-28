package GoogleData.sheet.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ObjMelt {
	private String search;
	private String totalmentions;
	private String mentionsdayaverage;
	private String totalengagement;
	private HashMap<String, List<String>> valuesFile;
	
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	public String getTotalmentions() {
		return totalmentions;
	}
	public void setTotalmentions(String totalmentions) {
		this.totalmentions = totalmentions;
	}
	public String getMentionsdayaverage() {
		return mentionsdayaverage;
	}
	public void setMentionsdayaverage(String mentionsdayaverage) {
		this.mentionsdayaverage = mentionsdayaverage;
	}
	public String getTotalengagement() {
		return totalengagement;
	}
	public void setTotalengagement(String totalengagement) {
		this.totalengagement = totalengagement;
	}
	public HashMap<String, List<String>> getValuesFile() {
		return valuesFile;
	}
	public void setValuesFile(HashMap<String, List<String>> valuesFile) {
		this.valuesFile = valuesFile;
	}
	
	
}
