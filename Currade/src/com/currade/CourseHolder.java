package com.currade;

import android.widget.TextView;

public class CourseHolder {
	protected TextView courseName;
	protected TextView courseCode;
	protected TextView courseMark;

	public CourseHolder() {
		super();
	}
	
	public CourseHolder(TextView courseCode, TextView courseName, TextView courseMark) {
		this.courseCode = courseCode;
		this.courseName = courseName;
		this.courseMark = courseMark;
	}
}

