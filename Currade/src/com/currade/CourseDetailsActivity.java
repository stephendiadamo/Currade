package com.currade;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.currade.objects.Course;
import com.currage.db.DBHandler;

public class CourseDetailsActivity extends Activity{
	
	Course course;
	private DBHandler dbh;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_details_page);
		dbh = new DBHandler(this);
		setAllValues();
		
		Button addTask = (Button) findViewById(R.id.courseDetailsAddNewTaskButton);
		addTask.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				final Dialog dialog = new Dialog(v.getContext());
				dialog.setContentView(R.layout.add_task_from_course);
				dialog.setTitle("Add Task");
				
				Button cancelCourse = (Button) dialog.findViewById(R.id.addCourseTaskCancelButton);
				cancelCourse.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				
				Button addCourse = (Button) dialog.findViewById(R.id.addCourseTaskAddButton);
				addCourse.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						EditText taskName = (EditText) dialog.findViewById(R.id.addCourseTaskTaskNameTextBox);
						EditText dueDate = (EditText) dialog.findViewById(R.id.addCourseTaskDueDateTextBox);
						EditText weight = (EditText) dialog.findViewById(R.id.addCourseTaskWeightTextBox);
						
						boolean someEmpty = taskName.getText().toString().isEmpty() 
								&& dueDate.getText().toString().isEmpty() 
								&& weight.getText().toString().isEmpty();
						
						if(!someEmpty){
							
							Toast.makeText(v.getContext(), "Added a task...", Toast.LENGTH_LONG).show();
							
					        // TODO: Create new task
							// TODO: Add to list adapter
							// TODO: notify adapter
							
							dialog.dismiss();
						} else {
							Toast.makeText(v.getContext(), "Please fill in the fields.", Toast.LENGTH_LONG).show();
						}
					}
				});
				dialog.show();
			}
		});
		
	}
	
	private void setAllValues(){
		Bundle extras = getIntent().getExtras();
		if (extras != null){
			TextView courseCodeLabel = (TextView) findViewById(R.id.courseDetailsCourseCode);
			TextView courseNameLabel = (TextView) findViewById(R.id.courseDetailsCourseName);
			TextView courseGradeLabel = (TextView) findViewById(R.id.courseDetailsCurrentGradeValueLabel);
			course = dbh.getCourse(extras.getInt("COURSE_ID"));
			courseCodeLabel.setText(course.getCourseCode());
			courseNameLabel.setText(course.getCourseName());
			courseGradeLabel.setText(Integer.toString((int)course.getCurrentMark()) + "%");
		}
	}
}
