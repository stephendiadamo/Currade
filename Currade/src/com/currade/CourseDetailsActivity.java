package com.currade;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.currade.objects.Course;
import com.currade.objects.Task;
import com.currage.db.DBHandler;

public class CourseDetailsActivity extends Activity {

	private Course course;
	private DBHandler dbh;
	private List<Task> courseTasks;
	private ListView taskListView;
	private CourseTaskListAdapter adapter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_details_page);
		dbh = new DBHandler(this);
		setAllValues();
		setAdapterFillTasks();
		setAdapterListeners();

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
								|| dueDate.getText().toString().isEmpty() || weight.getText().toString().isEmpty();

						if (!someEmpty) {
							Task task = new Task();
							task.setName(taskName.getText().toString());
							task.setDueDate(dueDate.getText().toString());
							task.setWeight(Float.parseFloat(weight.getText().toString()));
							task.setForWhatCourse(course.getCourseCode());
							task.setGrade(0);
							dbh.addTask(task);
							adapter.notifyDataSetChanged();
							setAdapterFillTasks();
							dialog.dismiss();
						} else {
							Toast.makeText(v.getContext(), "Please fill in all the fields.", Toast.LENGTH_LONG).show();
						}
					}
				});
				dialog.show();
			}
		});

	}

	private void setAdapterListeners() {
		taskListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> av, View view, final int pos, long id) {

				final Dialog dialog = new Dialog(view.getContext());
				dialog.setContentView(R.layout.course_task_options);
				dialog.setTitle("Options");
				TableRow removeTask = (TableRow) dialog.findViewById(R.id.courseRemoveTask);
				TableRow editTask = (TableRow) dialog.findViewById(R.id.courseEditTask);
				TableRow addGrade = (TableRow) dialog.findViewById(R.id.courseAddGrade);
				TableRow cancel = (TableRow) dialog.findViewById(R.id.courseCancel);

				removeTask.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Toast.makeText(v.getContext(), "Remove", Toast.LENGTH_LONG).show();
						dialog.dismiss();
					}
				});

				editTask.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Toast.makeText(v.getContext(), "Edit", Toast.LENGTH_LONG).show();
						dialog.dismiss();
					}
				});

				addGrade.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

						final Dialog addGradeDialog = new Dialog(v.getContext());
						addGradeDialog.setContentView(R.layout.add_grade);
						addGradeDialog.setTitle("Add Grade");

						Button addGradeDone = (Button) addGradeDialog.findViewById(R.id.addGradeDone);
						Button addGradeCancel = (Button) addGradeDialog.findViewById(R.id.addGradeCancel);
						final EditText gradeBox = (EditText) addGradeDialog.findViewById(R.id.addGradeGradeBox);
						final RadioGroup whatToDo = (RadioGroup) addGradeDialog.findViewById(R.id.addGradeRadioGroup);
						whatToDo.check(R.id.addGradeFinalRadio);
						gradeBox.setText(String.valueOf(course.getCurrentMark()));
						addGradeDone.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								int selected = whatToDo.getCheckedRadioButtonId();
								RadioButton selectedRadioButton = (RadioButton) addGradeDialog.findViewById(selected);

								if (!gradeBox.getText().toString().isEmpty()
										&& (selectedRadioButton.getText().equals("Final") || selectedRadioButton
												.getText().equals("Predict"))) {
									try {

										float grade = Float.parseFloat(gradeBox.getText().toString());
										if (selectedRadioButton.getText().equals("Final")) {
											courseTasks.get(pos).setGrade(grade);
											dbh.updateTask(courseTasks.get(pos));
											adapter.notifyDataSetChanged();
											setAdapterFillTasks();
										} else {
											// TODO: Implement grade prediction
										}
										addGradeDialog.dismiss();
										dialog.dismiss();
										calculateGrades();
										setAllValues();
									} catch (NumberFormatException e) {
										Toast.makeText(v.getContext(), "Please use a number value for your grade",
												Toast.LENGTH_LONG).show();
									}

								} else if (selectedRadioButton.getText().equals("Clear")) {
									// TODO: Use a better value than 0 for
									// default
									courseTasks.get(pos).setGrade(0);
									dbh.updateTask(courseTasks.get(pos));
									adapter.notifyDataSetChanged();
									setAdapterFillTasks();
									addGradeDialog.dismiss();
									dialog.dismiss();
									calculateGrades();
									setAllValues();
								}
							}
						});

						addGradeCancel.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								addGradeDialog.dismiss();
							}
						});
						addGradeDialog.show();
					}
				});

				cancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				dialog.show();
			}
		});
	}

	private void calculateGrades() {
		float grade = 0;
		float totalPercentage = 0;
		for (Task t : courseTasks) {
			grade += t.getGrade() * t.getWeight();
			totalPercentage += t.getWeight();
		}
		grade = grade / totalPercentage;
		course.setCurrentMark(grade);
		dbh.updateCourse(course);
		return;
	}

	private void setAdapterFillTasks() {
		courseTasks = dbh.getTasksFromCourse(course);
		taskListView = (ListView) findViewById(R.id.courseDetailsTaskListView);
		adapter = new CourseTaskListAdapter(this, R.layout.course_task_row, courseTasks);
		taskListView.setAdapter(adapter);
	}

	private void setAllValues() {
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			TextView courseCodeLabel = (TextView) findViewById(R.id.courseDetailsCourseCode);
			TextView courseNameLabel = (TextView) findViewById(R.id.courseDetailsCourseName);
			TextView courseGradeLabel = (TextView) findViewById(R.id.courseDetailsCurrentGradeValueLabel);
			course = dbh.getCourse(extras.getInt("COURSE_ID"));
			courseCodeLabel.setText(course.getCourseCode());
			courseNameLabel.setText(course.getCourseName());
			courseGradeLabel.setText(Integer.toString((int) course.getCurrentMark()) + "%");
		}
	}
}
