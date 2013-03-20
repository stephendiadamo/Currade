package com.currade;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.currade.objects.Task;

public class CourseTaskListAdapter extends ArrayAdapter<Task> {

	private Context context;
	private int layoutResId;
	private List<Task> tasks;

	public CourseTaskListAdapter(Context c, int layoutResId, List<Task> tasks) {
		super(c, layoutResId, tasks);
		this.context = c;
		this.layoutResId = layoutResId;
		this.tasks = tasks;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		View row = convertView;
		TextView taskName;
		TextView taskWeight;
		TextView taskMark;
		CourseTaskHolder holder;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResId, parent, false);
			taskName = (TextView) row.findViewById(R.id.taskRowTaskName);
			taskWeight = (TextView) row.findViewById(R.id.taskRowTaskWeight);
			taskMark = (TextView) row.findViewById(R.id.taskRowTaskMark);
			holder = new CourseTaskHolder(taskName, taskWeight, taskMark);
			row.setTag(holder);
		} else {
			holder = (CourseTaskHolder) row.getTag();
		}

		Task task = tasks.get(pos);
		holder.taskName.setText(task.getName());
		String weight = Float.toString(task.getWeight()).substring(0, 3) + "0%";
		String grade = task.getGrade() == -1 ? task.getDueDate() : Float.toString(task.getGrade()).substring(0, 3)
				+ "0%";

		holder.taskWeight.setText("(" + weight + ")");
		holder.taskMark.setText(grade);
		return row;
	}
}
