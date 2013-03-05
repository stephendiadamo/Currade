package com.currade;

import android.widget.TextView;

public class CourseTaskHolder {

	protected TextView taskName;
	protected TextView taskWeight;
	protected TextView taskMark;

	public CourseTaskHolder(TextView taskName, TextView taskWeight, TextView taskMark) {
		this.taskName = taskName;
		this.taskWeight = taskWeight;
		this.taskMark = taskMark;
	}
}
