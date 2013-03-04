package com.currade;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.currade.objects.Course;

public class CourseListingAdapter extends ArrayAdapter<Course> {

	private Context context;
	private int layoutResId;
	private List<Course> courses;

	public CourseListingAdapter(Context context, int layoutResId, List<Course> courses) {
		super(context, layoutResId, courses);
		this.context = context;
		this.layoutResId = layoutResId;
		this.courses = courses;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		View row = convertView;
		TextView courseCode;
		TextView courseName;
		TextView mark;
		CourseHolder holder;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResId, parent, false);
			courseCode = (TextView) row.findViewById(R.id.courseRowCourseCode);
			courseName = (TextView) row.findViewById(R.id.courseRowCourseName);
			mark = (TextView) row.findViewById(R.id.courseRowCourseMark);
			holder = new CourseHolder(courseCode, courseName, mark);
			row.setTag(holder);
		} else {
			holder = (CourseHolder) row.getTag();
		}
		Course course = courses.get(pos);
		holder.courseCode.setText(course.getCourseCode());
		holder.courseName.setText(course.getCourseName());
		holder.courseMark.setText(Integer.toString((int) course.getCurrentMark()) + "%");
		return row;
	}

}
