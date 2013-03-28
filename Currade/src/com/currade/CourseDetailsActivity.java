package com.currade;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import com.currade.db.DBHandler;
import com.currade.objects.Course;
import com.currade.objects.Task;

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
		setCourse();
		setAllValues();
		setAdapterFillTasks();
		setAdapterListeners();

		Button addTask = (Button) findViewById(R.id.courseDetailsAddNewTaskButton);
		addTask.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addTask(v.getContext());
			}

		});

	}

	private boolean doesNotExceedHundred(float nextTaskWeight) {
		float totalWeight = 0;
		for (Task t : courseTasks) {
			totalWeight += t.getWeight();
		}
		totalWeight += nextTaskWeight;
		return (totalWeight <= 100);
	}

	private void addTask(Context c) {
		final Dialog dialog = new Dialog(c);
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

				boolean someEmpty = taskName.getText().toString().isEmpty() || dueDate.getText().toString().isEmpty()
						|| weight.getText().toString().isEmpty();

				if (!someEmpty) {
					if (doesNotExceedHundred(Float.parseFloat(weight.getText().toString()))) {
						Task task = new Task();
						task.setName(taskName.getText().toString());
						task.setDueDate(dueDate.getText().toString());
						task.setWeight(Float.parseFloat(weight.getText().toString()));
						task.setForWhatCourse(course.getCourseCode());
						task.setGrade(-1);
						task.setApproximatedGrade(-1);
						dbh.addTask(task);
						adapter.notifyDataSetChanged();
						setAdapterFillTasks();
					} else {
						Toast.makeText(v.getContext(), "This task exceeds 100% of the course", Toast.LENGTH_LONG)
								.show();
					}
					dialog.dismiss();
				} else {
					Toast.makeText(v.getContext(), "Please fill in all the fields.", Toast.LENGTH_LONG).show();
				}
			}

		});
		dialog.show();
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
					public void onClick(final View v) {

						DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								switch (which) {
								case DialogInterface.BUTTON_POSITIVE:
									Task t = courseTasks.get(pos);
									courseTasks.remove(pos);
									dbh.deleteTask(t);
									calculateGrades();
									setAllValues();
									adapter.notifyDataSetChanged();
									setAdapterFillTasks();
									Toast.makeText(v.getContext(), "Removed", Toast.LENGTH_LONG).show();
									break;
								case DialogInterface.BUTTON_NEGATIVE:
									break;
								}

							}
						};

						AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
						builder.setMessage("Delete this task?").setPositiveButton("Yes", dialogClickListener)
								.setNegativeButton("No", dialogClickListener).show();

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
						gradeBox.setText("");
						addGradeDone.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								int selected = whatToDo.getCheckedRadioButtonId();
								RadioButton selectedRadioButton = (RadioButton) addGradeDialog.findViewById(selected);

								if (!gradeBox.getText().toString().isEmpty()
										&& (selectedRadioButton.getText().equals("Final") || selectedRadioButton
												.getText().equals("Prediction"))) {
									try {

										float grade = Float.parseFloat(gradeBox.getText().toString());
										if (selectedRadioButton.getText().equals("Final")) {
											courseTasks.get(pos).setGrade(grade);
										} else {
											courseTasks.get(pos).setApproximatedGrade(grade);
										}
										dbh.updateTask(courseTasks.get(pos));
										adapter.notifyDataSetChanged();
										setAdapterFillTasks();
										addGradeDialog.dismiss();
										dialog.dismiss();
										calculateGrades();
										setAllValues();
									} catch (NumberFormatException e) {
										Toast.makeText(v.getContext(), "Please use a number value for your grade",
												Toast.LENGTH_LONG).show();
									}

								} else if (selectedRadioButton.getText().equals("Clear")) {
									if (courseTasks.get(pos).getApproximatedGrade() == -1) {
										courseTasks.get(pos).setGrade(-1);
									} else {
										courseTasks.get(pos).setApproximatedGrade(-1);
									}
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.course_detail_items, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
		case R.id.add_task_item:
			addTask(this);			
			break;

		case R.id.clear_predictions:
			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case DialogInterface.BUTTON_POSITIVE:
						clearPredictedGrades();
						calculateGrades();
						adapter.notifyDataSetChanged();
						setAdapterFillTasks();
						setAllValues();
						break;
					case DialogInterface.BUTTON_NEGATIVE:
						break;
					}
				}
			};
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Remove all predicted grades?").setPositiveButton("Yes", dialogClickListener)
					.setNegativeButton("No", dialogClickListener).show();
			break;

		case R.id.export_course_data:
			Toast.makeText(getBaseContext(), "Export Data", Toast.LENGTH_SHORT).show();
			break;

		case R.id.import_course_data:
			Toast.makeText(getBaseContext(), "Import Data", Toast.LENGTH_SHORT).show();
			break;

		}
		return true;

	}

	private void clearPredictedGrades() {
		if (courseTasks.size() != 0) {
			for (Task t : courseTasks) {
				if (t.getApproximatedGrade() != -1) {
					t.setApproximatedGrade(-1);
					dbh.updateTask(t);
				}
			}
		}
	}

	private void calculateGrades() {
		if (courseTasks.size() != 0) {
			float grade = 0;
			float totalPercentage = 0;
			for (Task t : courseTasks) {
				if (t.getApproximatedGrade() == -1 && t.getGrade() != -1) {
					grade += t.getGrade() * t.getWeight();
					totalPercentage += t.getWeight();
				} else if (t.getApproximatedGrade() != -1) {
					grade += t.getApproximatedGrade() * t.getWeight();
					totalPercentage += t.getWeight();
				}
			}
			float maxMark = (100 - totalPercentage) + (grade / totalPercentage) * (totalPercentage / 100);
			float minMark = (grade / 100);
			course.setCurrentMark(grade / totalPercentage);
			course.setCurrentMaxMark(maxMark);
			course.setCurrentMinMark(minMark);
		} else {
			course.setCurrentMark(0);
			course.setCurrentMaxMark(0);
			course.setCurrentMinMark(0);
		}
		dbh.updateCourse(course);
		return;
	}

	private void setAdapterFillTasks() {
		courseTasks = dbh.getTasksFromCourse(course);
		taskListView = (ListView) findViewById(R.id.courseDetailsTaskListView);
		adapter = new CourseTaskListAdapter(this, R.layout.course_task_row, courseTasks);
		taskListView.setAdapter(adapter);
	}

	private void setCourse() {
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			course = dbh.getCourse(extras.getInt("COURSE_ID"));
		}
	}

	private void setAllValues() {

		TextView courseCodeLabel = (TextView) findViewById(R.id.courseDetailsCourseCode);
		TextView courseNameLabel = (TextView) findViewById(R.id.courseDetailsCourseName);
		TextView courseGradeLabel = (TextView) findViewById(R.id.courseDetailsCurrentGradeValueLabel);
		TextView courseMaxGradeLabel = (TextView) findViewById(R.id.courseDetailsMaxGradeValueLabel);
		TextView courseMinGradeLabel = (TextView) findViewById(R.id.courseDetailsMinGradeValueLabel);

		courseCodeLabel.setText(course.getCourseCode());
		courseNameLabel.setText(course.getCourseName());

		String mark = course.getCurrentMark() == -1 ? "0" : String.valueOf(course.getCurrentMark()).substring(0, 3);
		String maxMark = course.getCurrentMaxMark() == -1 ? "0" : String.valueOf(course.getCurrentMaxMark()).substring(
				0, 3);
		String minMark = course.getCurrentMinMark() == -1 ? "0" : String.valueOf(course.getCurrentMinMark()).substring(
				0, 3);

		courseGradeLabel.setText(mark + "0%");
		courseMaxGradeLabel.setText(maxMark + "0%");
		courseMinGradeLabel.setText(minMark + "0%");

	}
}
