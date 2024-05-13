package GoogleData.sheet.model;

import java.util.List;

public class ObjTiktok {
	private String search;
	private String name;
	private String post;
	private String date;
	private String text;
	private Long likes;
	private Long comments;
	private Long favorites;
	private Long shareds;
	private List<CommentsModel> commentsList;
	
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Long getLikes() {
		return likes;
	}
	public void setLikes(Long likes) {
		this.likes = likes;
	}
	public Long getComments() {
		return comments;
	}
	public void setComments(Long comments) {
		this.comments = comments;
	}
	public Long getFavorites() {
		return favorites;
	}
	public void setFavorites(Long favorites) {
		this.favorites = favorites;
	}
	public Long getShareds() {
		return shareds;
	}
	public void setShareds(Long shareds) {
		this.shareds = shareds;
	}
	public List<CommentsModel> getCommentsList() {
		return commentsList;
	}
	public void setCommentsList(List<CommentsModel> commentsList) {
		this.commentsList = commentsList;
	}
}
