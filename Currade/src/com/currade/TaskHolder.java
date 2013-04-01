package com.currade;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class TaskHolder {

	protected TextView courseCode;
	protected TextView taskName;
	protected TextView weight;
	protected TextView date;
	// protected TextView daysLeft;
	protected CheckBox isDone;
	protected View strikeThrough;

	public TaskHolder() {
		super();
	}

	public TaskHolder(TextView taskName, TextView courseCode, TextView weight, TextView date, CheckBox isDone, View strike) {
		this.taskName = taskName;
		this.courseCode = courseCode;
		this.weight = weight;
		this.date = date;
		// this.daysLeft = daysLeft;
		this.isDone = isDone;
		this.strikeThrough = strike;
	}

}
