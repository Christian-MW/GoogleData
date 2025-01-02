package GoogleData.sheet.model;

import java.util.List;

public class TodoSetModel {
	private Long id;
	private boolean status;
	private String created_at;
	private String name;
	private String creator_name;
	private String creator_email;
	private String description;
	private String todo_list_url;
	private String completed_ratio;
	private List<CardModel> list_cards;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCreator_name() {
		return creator_name;
	}
	public void setCreator_name(String creator_name) {
		this.creator_name = creator_name;
	}
	public String getCreator_email() {
		return creator_email;
	}
	public void setCreator_email(String creator_email) {
		this.creator_email = creator_email;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTodo_list_url() {
		return todo_list_url;
	}
	public void setTodo_list_url(String todo_list_url) {
		this.todo_list_url = todo_list_url;
	}
	public String getCompleted_ratio() {
		return completed_ratio;
	}
	public void setCompleted_ratio(String completed_ratio) {
		this.completed_ratio = completed_ratio;
	}
	public List<CardModel> getList_cards() {
		return list_cards;
	}
	public void setList_cards(List<CardModel> list_cards) {
		this.list_cards = list_cards;
	}
}
