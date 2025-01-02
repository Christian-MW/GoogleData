package GoogleData.sheet.model;

public class AssignamentModel {
	private String link;
	private String dateAssign;
	private Integer max_assign;
	private Integer current_assign;
	
	
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getDateAssign() {
		return dateAssign;
	}
	public void setDateAssign(String dateAssign) {
		this.dateAssign = dateAssign;
	}
	public Integer getMax_assign() {
		return max_assign;
	}
	public void setMax_assign(Integer max_assign) {
		this.max_assign = max_assign;
	}
	public Integer getCurrent_assign() {
		return current_assign;
	}
	public void setCurrent_assign(Integer current_assign) {
		this.current_assign = current_assign;
	}
}
