package com.currade;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.currade.objects.Task;

public class TaskListingAdapter extends ArrayAdapter<Task> {
	private Context context;
	private int layoutResId;
	private List<Task> tasks;

	public TaskListingAdapter(Context context, int layoutResId, List<Task> tasks) {
		super(context, layoutResId, tasks);
		this.context = context;
		this.layoutResId = layoutResId;
		this.tasks = tasks;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		View row = convertView;
		TextView courseCode;
		TextView taskName;
		TextView weight;
		TextView date;
		// TextView daysLeft;
		CheckBox isDone;
		TaskHolder holder;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResId, parent, false);

			courseCode = (TextView) row.findViewById(R.id.taskPageRowCourseCode);
			taskName = (TextView) row.findViewById(R.id.taskPageRowTaskName);
			weight = (TextView) row.findViewById(R.id.taskPageRowWeight);
			date = (TextView) row.findViewById(R.id.taskPageRowDueDate);
			isDone = (CheckBox) row.findViewById(R.id.taskPageRowCheckBox);

			holder = new TaskHolder(taskName, courseCode, weight, date, isDone);
			row.setTag(holder);
		} else {
			holder = (TaskHolder) row.getTag();
		}

		Task task = tasks.get(pos);
		holder.taskName.setText(task.getName());
		holder.courseCode.setText(task.getForWhatCourse());
		String setWeight = task.getWeight() == -1 ? "---" : String.valueOf(task.getWeight()) + "%";
		holder.weight.setText(setWeight);
		holder.date.setText(task.getDueDate());
		// TODO: Implement days left
		// holder.daysLeft.setText("5");
		return row;
	}

}
