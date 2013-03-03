package com.currage.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.currade.objects.Course;


public class DBHandler extends SQLiteOpenHelper{

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "coursesManager";
	
	//Tables
	private static final String TABLE_COURSES = "courses";
	private static final String TABLE_TASKS = "tasks";
	
	//Columns for courses table
	private static final String KEY_COURSE_ID = "id";
	private static final String COURSE_CODE = "course_code";
	private static final String COURSE_NAME = "course_name";
	private static final String COURSE_MARK = "mark";
	
	//Columns for tasks
	private static final String KEY_TASK_ID = "id";
	private static final String TASK_NAME = "task_name";
	private static final String TASK_DUE_DATE = "due_date";
	private static final String TASK_TYPE = "type";
	private static final String TASK_FOR_WHAT_COURSE = "course";
	private static final String TASK_WEIGHT = "weight";
	
	
	public DBHandler(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_COURSE_TABLE = 
				"CREATE TABLE " + TABLE_COURSES + "(" + KEY_COURSE_ID +	" INTEGER PRIMARY KEY," 
		        + COURSE_CODE + " TEXT," + COURSE_NAME + " TEXT,"
				+ COURSE_MARK + " REAL" + ")";
		
		String CREATE_TASK_TABLE = 
				"CREATE TABLE " + TABLE_TASKS + "(" + KEY_TASK_ID +	" INTEGER PRIMARY KEY," 
		        + TASK_NAME + " TEXT," + TASK_DUE_DATE + " TEXT,"	+ TASK_TYPE + " TEXT," +
						TASK_FOR_WHAT_COURSE + " TEXT," + TASK_WEIGHT + " REAL" + ")";
		
		db.execSQL(CREATE_COURSE_TABLE);
		db.execSQL(CREATE_TASK_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
		onCreate(db);
	}
	
	public void addCourse(Course course){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COURSE_CODE, course.getCourseCode());
		values.put(COURSE_NAME, course.getCourseName());
		values.put(COURSE_MARK, course.getCurrentMark());
		
		db.insert(TABLE_COURSES, null, values);
		db.close();
	}
	
	public Course getCourse(int id){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_COURSES, new String[] {KEY_COURSE_ID, COURSE_NAME, COURSE_CODE, COURSE_MARK}, KEY_COURSE_ID + " = ?", new String[] {String.valueOf(id)},
				null, null, null, null);
		if (cursor != null){
			cursor.moveToFirst();
		}
		
		Course course = new Course (Integer.parseInt(cursor.getString(0)), cursor.getString(1), 
				cursor.getString(2), Float.parseFloat(cursor.getString(3)));
		return course;
	}
	
	public List<Course> getAllCourses(){
		List<Course> courseList = new ArrayList<Course>();
		String selectQuery = "SELECT * FROM " + TABLE_COURSES;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if (cursor.moveToFirst()){
			do{
				Course course = new Course();
				course.setId(Integer.parseInt(cursor.getString(0)));
				course.setCourseCode(cursor.getString(1));
				course.setCourseName(cursor.getString(2));
				course.setCurrentMark(Float.parseFloat(cursor.getString(3)));
				courseList.add(course);
			} while (cursor.moveToNext());
		}
		return courseList;
	}
	
	public int getCourseCount(){
		String countQuery = "SELECT * FROM " + TABLE_COURSES;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
		return cursor.getCount();
	}
	
	public int updateCourse(Course course){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(COURSE_CODE, course.getCourseCode());
		values.put(COURSE_NAME, course.getCourseName());
		values.put(COURSE_MARK, course.getCurrentMark());
		
		return db.update(TABLE_COURSES, values, KEY_COURSE_ID + " = ?", 
				new String[] {String.valueOf(course.getId())});
		
	}
	
	public void deleteCourse(Course course){
	    SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(TABLE_COURSES, KEY_COURSE_ID + " = ?",
	            new String[] { String.valueOf(course.getId()) });
	    db.close();
	}
	
	
	// TODO: DO ALL OF THIS FOR TASK TABLE
	
	
}
