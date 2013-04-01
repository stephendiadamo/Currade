package com.currade.objects;

public class Task {

	private int id;
	private String name;
	private String dueDate;
	private String forWhatCourse;
	private float weight;
	private float grade;
	private float approximatedGrade;
	public boolean isSelected;

	public Task() {
		super();
	}

	public int getId() {
		return id;
	}

	public float getApproximatedGrade() {
		return approximatedGrade;
	}

	public void setApproximatedGrade(float approximatedGrade) {
		this.approximatedGrade = approximatedGrade;
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

	public float getGrade() {
		return grade;
	}

	public void setGrade(float grade) {
		this.grade = grade;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
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
