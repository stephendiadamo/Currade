package com.currade.objects;

public class Task {

	private int id;
	private String name;
	private String dueDate;
	private String type;
	private String forWhatCourse;
	private float weight;
	
	public Task(int id, String name, String dueDate, String type, String forWhatCourse){
		this.id = id;
		this.name = name;
		this.dueDate = dueDate;
		this.type = type;
		this.forWhatCourse = forWhatCourse;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getForWhatCourse() {
		return forWhatCourse;
	}

	public void setForWhatCourse(String forWhatCourse) {
		this.forWhatCourse = forWhatCourse;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}
	
}
