package com.currage.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.currade.objects.Course;
import com.currade.objects.Task;

public class DBHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 4;
	private static final String DATABASE_NAME = "coursesManager";

	// Tables
	private static final String TABLE_COURSES = "courses";
	private static final String TABLE_TASKS = "tasks";

	// Columns for courses table
	private static final String KEY_COURSE_ID = "id";
	private static final String COURSE_CODE = "course_code";
	private static final String COURSE_NAME = "course_name";
	private static final String COURSE_MARK = "mark";
	private static final String COURSE_MAX_MARK = "max_mark";
	private static final String COURSE_MIN_MARK = "min_mark";

	// Columns for tasks
	private static final String KEY_TASK_ID = "id";
	private static final String TASK_NAME = "task_name";
	private static final String TASK_DUE_DATE = "due_date";
	private static final String TASK_FOR_WHAT_COURSE = "course";
	private static final String TASK_WEIGHT = "weight";
	private static final String TASK_GRADE = "grade";
	private static final String TASK_APROX_GRADE = "aprox_grade";

	public DBHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_COURSE_TABLE = "CREATE TABLE " + TABLE_COURSES + "(" + KEY_COURSE_ID + " INTEGER PRIMARY KEY,"
				+ COURSE_CODE + " TEXT," + COURSE_NAME + " TEXT," + COURSE_MARK + " REAL," + COURSE_MAX_MARK + " REAL,"
				+ COURSE_MIN_MARK + " REAL" + ")";

		String CREATE_TASK_TABLE = "CREATE TABLE " + TABLE_TASKS + "(" + KEY_TASK_ID + " INTEGER PRIMARY KEY,"
				+ TASK_NAME + " TEXT," + TASK_DUE_DATE + " TEXT," + TASK_FOR_WHAT_COURSE + " TEXT," + TASK_WEIGHT
				+ " REAL," + TASK_GRADE + " REAL," + TASK_APROX_GRADE + " REAL" + ")";

		db.execSQL(CREATE_COURSE_TABLE);
		db.execSQL(CREATE_TASK_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
		onCreate(db);
	}

	public void addCourse(Course course) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COURSE_CODE, course.getCourseCode());
		values.put(COURSE_NAME, course.getCourseName());
		values.put(COURSE_MARK, course.getCurrentMark());
		values.put(COURSE_MAX_MARK, course.getCurrentMaxMark());
		values.put(COURSE_MIN_MARK, course.getCurrentMinMark());
		db.insert(TABLE_COURSES, null, values);
		db.close();
	}

	public Course getCourse(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_COURSES, new String[] { KEY_COURSE_ID, COURSE_NAME, COURSE_CODE, COURSE_MARK,
				COURSE_MAX_MARK, COURSE_MIN_MARK }, KEY_COURSE_ID + " = ?", new String[] { String.valueOf(id) }, null,
				null, null, null);
		Course course = null;
		if (cursor.moveToFirst()) {
			course = new Course(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getFloat(3),
					cursor.getFloat(4), cursor.getFloat(5));
			cursor.close();
		}
		db.close();
		return course;
	}

	public List<Course> getAllCourses() {
		List<Course> courseList = new ArrayList<Course>();
		String selectQuery = "SELECT * FROM " + TABLE_COURSES;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				Course course = new Course();
				course.setId(cursor.getInt(0));
				course.setCourseCode(cursor.getString(1));
				course.setCourseName(cursor.getString(2));
				course.setCurrentMark(cursor.getFloat(3));
				course.setCurrentMaxMark(cursor.getFloat(4));
				course.setCurrentMinMark(cursor.getFloat(5));
				courseList.add(course);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return courseList;
	}

	public int getCourseCount() {
		String countQuery = "SELECT * FROM " + TABLE_COURSES;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
		db.close();
		return cursor.getCount();
	}

	public int updateCourse(Course course) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(COURSE_CODE, course.getCourseCode());
		values.put(COURSE_NAME, course.getCourseName());
		values.put(COURSE_MARK, course.getCurrentMark());
		values.put(COURSE_MAX_MARK, course.getCurrentMaxMark());
		values.put(COURSE_MIN_MARK, course.getCurrentMinMark());

		int retVal = db.update(TABLE_COURSES, values, KEY_COURSE_ID + " = ?",
				new String[] { String.valueOf(course.getId()) });
		db.close();
		return retVal;
	}

	public void deleteCourse(Course course) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_COURSES, KEY_COURSE_ID + " = ?", new String[] { String.valueOf(course.getId()) });
		db.delete(TABLE_TASKS, TASK_FOR_WHAT_COURSE + " = ?", new String[] { String.valueOf(course.getCourseCode()) });
		db.close();
	}

	public void addTask(Task task) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TASK_NAME, task.getName());
		values.put(TASK_DUE_DATE, task.getDueDate());
		values.put(TASK_FOR_WHAT_COURSE, task.getForWhatCourse());
		values.put(TASK_WEIGHT, task.getWeight());
		values.put(TASK_GRADE, task.getGrade());
		values.put(TASK_APROX_GRADE, task.getApproximatedGrade());
		db.insert(TABLE_TASKS, null, values);
		db.close();
	}

	public Task getTask(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_TASKS, new String[] { KEY_TASK_ID, TASK_NAME, TASK_DUE_DATE,
				TASK_FOR_WHAT_COURSE, TASK_WEIGHT, TASK_GRADE, TASK_APROX_GRADE }, KEY_TASK_ID + " = ?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}

		Task task = new Task();
		task.setId(cursor.getInt(0));
		task.setName(cursor.getString(1));
		task.setDueDate(cursor.getString(2));
		task.setForWhatCourse(cursor.getString(3));
		task.setWeight(cursor.getFloat(4));
		task.setGrade(cursor.getFloat(5));
		task.setApproximatedGrade(cursor.getFloat(6));
		cursor.close();
		db.close();
		return task;
	}

	public List<Task> getAllTasks() {
		List<Task> taskList = new ArrayList<Task>();
		String selectQuery = "SELECT * FROM " + TABLE_TASKS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				Task task = new Task();
				task.setId(cursor.getInt(0));
				task.setName(cursor.getString(1));
				task.setDueDate(cursor.getString(2));
				task.setForWhatCourse(cursor.getString(3));
				task.setWeight(cursor.getFloat(4));
				task.setGrade(cursor.getFloat(5));
				task.setApproximatedGrade(cursor.getFloat(6));
				taskList.add(task);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return taskList;
	}

	public int getTaskCount() {
		String countQuery = "SELECT * FROM " + TABLE_TASKS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
		db.close();
		return cursor.getCount();
	}

	public int updateTask(Task task) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TASK_NAME, task.getName());
		values.put(TASK_DUE_DATE, task.getDueDate());
		values.put(TASK_FOR_WHAT_COURSE, task.getForWhatCourse());
		values.put(TASK_WEIGHT, task.getWeight());
		values.put(TASK_GRADE, task.getGrade());
		values.put(TASK_APROX_GRADE, task.getApproximatedGrade());
		int retVal = db
				.update(TABLE_TASKS, values, KEY_TASK_ID + " = ?", new String[] { String.valueOf(task.getId()) });
		db.close();
		return retVal;

	}

	public void deleteTask(Task task) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_TASKS, KEY_TASK_ID + " = ?", new String[] { String.valueOf(task.getId()) });
		db.close();
	}

	public List<Task> getTasksFromCourse(Course course) {
		List<Task> taskList = new ArrayList<Task>();
		String selectQuery = "SELECT * FROM " + TABLE_TASKS + " WHERE " + TASK_FOR_WHAT_COURSE + " = '"
				+ course.getCourseCode() + "'";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Task task = new Task();
				task.setId(cursor.getInt(0));
				task.setName(cursor.getString(1));
				task.setDueDate(cursor.getString(2));
				task.setForWhatCourse(cursor.getString(3));
				task.setWeight(cursor.getFloat(4));
				task.setGrade(cursor.getFloat(5));
				task.setApproximatedGrade(cursor.getFloat(6));
				taskList.add(task);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return taskList;
	}

}
