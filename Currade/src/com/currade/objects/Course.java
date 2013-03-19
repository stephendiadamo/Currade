package com.currade.objects;

import java.util.ArrayList;

public class Course {

	private String courseName;
	private String courseCode;
	private ArrayList<Task> tasks;
	private float currentMark;
	private float currentMaxMark;
	private float currentMinMark;
	private int id;

	public Course() {
		super();
	}

	public Course(int id, String courseName, String courseCode, float mark, float maxMark, float minMark) {
		this.id = id;
		this.courseName = courseName;
		this.courseCode = courseCode;
		this.currentMark = mark;
		this.currentMaxMark = maxMark;
		this.currentMinMark = minMark;
	}

	public float getCurrentMaxMark() {
		return currentMaxMark;
	}

	public void setCurrentMaxMark(float currentMaxMark) {
		this.currentMaxMark = currentMaxMark;
	}

	public float getCurrentMinMark() {
		return currentMinMark;
	}

	public void setCurrentMinMark(float currentMinMark) {
		this.currentMinMark = currentMinMark;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public float getCurrentMark() {
		return currentMark;
	}

	public void setCurrentMark(float currentMark) {
		this.currentMark = currentMark;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void addTask(Task task) {
		tasks.add(task);
	}

	public ArrayList<Task> getTasks() {
		return tasks;
	}

	public void setTasks(ArrayList<Task> tasks) {
		this.tasks = tasks;
	}

}
