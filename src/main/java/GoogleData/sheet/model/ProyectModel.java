package GoogleData.sheet.model;

import java.util.List;

public class ProyectModel {
	private Long id;
	private String status;
	private String name;
	private String description;
	private String url_todoset;
	private List<UsersModel> users;
	private List<TodoSetModel> todoSets;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl_todoset() {
		return url_todoset;
	}
	public void setUrl_todoset(String url_todoset) {
		this.url_todoset = url_todoset;
	}
	public List<UsersModel> getUsers() {
		return users;
	}
	public void setUsers(List<UsersModel> users) {
		this.users = users;
	}
	public List<TodoSetModel> getTodoSets() {
		return todoSets;
	}
	public void setTodoSets(List<TodoSetModel> todoSets) {
		this.todoSets = todoSets;
	}
}
