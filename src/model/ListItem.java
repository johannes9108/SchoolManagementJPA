package model;

public class ListItem {

	private String content;
	private int id;
	
	public ListItem(String content,int id) {
		this.content = content;
		this.id = id;
	}
	
	@Override
	public String toString() {
		return content;
	}


	public String getContent() {
		return content;
	}


	public int getId() {
		return id;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public void setId(int id) {
		this.id = id;
	}
	
	
	
	
}
