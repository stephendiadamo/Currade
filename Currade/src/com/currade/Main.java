package com.currade;

import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.currade.objects.Course;
import com.currage.db.DBHandler;

public class Main extends FragmentActivity implements ActionBar.TabListener {

	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	private DBHandler dbh;
	private CourseListingAdapter adapter;
	private ListView courseListView;
	List<Course> allCourses;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
			
			// TODO: Remove swiping
		});
		dbh = new DBHandler(this);
		allCourses = dbh.getAllCourses();
		SetAdapterFillCourses();
		
		ActionBar.Tab coursesTab = actionBar.newTab().setText("Courses");
		ActionBar.Tab tasksTab = actionBar.newTab().setText("Tasks");
		
		coursesTab.setTabListener(this);
		tasksTab.setTabListener(this);
		
		actionBar.addTab(coursesTab);
		actionBar.addTab(tasksTab);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.

			Fragment frag = new Fragment();
			if (position == 0){
				frag = new CoursesPage();
			} else if (position == 1){
				frag =  new TasksPage();
			}
			return frag;
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}
	
	private void SetAdapterFillCourses(){
		setContentView(R.layout.courses_page);
		courseListView = (ListView)findViewById(R.id.courseListView);
		adapter = new CourseListingAdapter(this, R.layout.course_row, allCourses);	
		courseListView.setAdapter(adapter);
		
	}
	
	// Course fragment actions
	public void onClickCourse(View v){
		switch (v.getId()) {
		case R.id.addNewCourse:
			final Dialog dialog = new Dialog(this);
			dialog.setContentView(R.layout.add_course);
			dialog.setTitle("Add Course");
			
			Button cancelCourse = (Button) dialog.findViewById(R.id.cancelAddCourseButton);
			cancelCourse.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			
			Button addCourse = (Button) dialog.findViewById(R.id.addCourseButton);
			addCourse.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					EditText courseCode = (EditText) dialog.findViewById(R.id.courseCodeInputText);
					EditText courseName = (EditText) dialog.findViewById(R.id.courseNameInputText);
					
					if(!courseCode.getText().toString().isEmpty()){
						Course course = new Course();
						course.setCourseCode(courseCode.getText().toString());
						course.setCourseName(courseName.getText().toString());
						course.setCurrentMark(0);					
						dbh.addCourse(course);
						allCourses.add(course);
				        adapter.notifyDataSetChanged();
						dialog.dismiss();
					} else {
						Toast.makeText(v.getContext(), "Please add a course code.", Toast.LENGTH_LONG).show();
					}
				}
			});
			dialog.show();
		}
	}
	
}
