package GoogleData.sheet.dto.request;

import java.util.ArrayList;
import java.util.List;

public class AddImgSlideRequest {
	private String slide_id;
	private String presentation_id;
	private List<Object> authors;
	
	
	public String getSlide_id() {
		return slide_id;
	}
	public void setSlide_id(String slide_id) {
		this.slide_id = slide_id;
	}
	public String getPresentation_id() {
		return presentation_id;
	}
	public void setPresentation_id(String presentation_id) {
		this.presentation_id = presentation_id;
	}
	public List<Object> getAuthors() {
		return authors;
	}
	public void setAuthors(List<Object> authors) {
		this.authors = authors;
	}
}
