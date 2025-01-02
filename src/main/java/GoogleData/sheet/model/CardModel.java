package GoogleData.sheet.model;

import java.util.List;

public class CardModel {
	private Long id;
	private String status;
	private String created_at;
	private String updated_at;
	private String name;
	private String description;
	private String comments_url;
	private boolean completedCard;
	private UsersModel userCreator;
	private String urlCard;
	//Si la tarjeta está terminada se llenará este objeto si no será null
	private UsersModel userCompletion;
	private String dateCompletion;
	private List<UsersModel> userAssignees;
	private List<UsersModel> notifiedUsers;
	
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
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	public String getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
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
	public String getComments_url() {
		return comments_url;
	}
	public void setComments_url(String comments_url) {
		this.comments_url = comments_url;
	}
	public boolean isCompletedCard() {
		return completedCard;
	}
	public void setCompletedCard(boolean completedCard) {
		this.completedCard = completedCard;
	}
	public UsersModel getUserCreator() {
		return userCreator;
	}
	public void setUserCreator(UsersModel userCreator) {
		this.userCreator = userCreator;
	}
	public UsersModel getUserCompletion() {
		return userCompletion;
	}
	public void setUserCompletion(UsersModel userCompletion) {
		this.userCompletion = userCompletion;
	}
	public String getDateCompletion() {
		return dateCompletion;
	}
	public void setDateCompletion(String dateCompletion) {
		this.dateCompletion = dateCompletion;
	}
	public List<UsersModel> getUserAssignees() {
		return userAssignees;
	}
	public void setUserAssignees(List<UsersModel> userAssignees) {
		this.userAssignees = userAssignees;
	}
	public List<UsersModel> getNotifiedUsers() {
		return notifiedUsers;
	}
	public void setNotifiedUsers(List<UsersModel> notifiedUsers) {
		this.notifiedUsers = notifiedUsers;
	}
	public String getUrlCard() {
		return urlCard;
	}
	public void setUrlCard(String urlCard) {
		this.urlCard = urlCard;
	}
}
