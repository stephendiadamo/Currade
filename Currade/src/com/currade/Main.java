package com.currade;

import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
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

		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		setContentView(R.layout.activity_main);

		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				if (position == 0) {
					SetAdapterFillCourses();
					adapter.notifyDataSetChanged();
				} else {
					setContentView(R.layout.tasks_page);
				}
			}
		});

		// Do this on first load
		dbh = new DBHandler(this);
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
			if (position == 0) {
				frag = new CoursesPage();
			} else if (position == 1) {
				frag = new TasksPage();
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

	private void SetAdapterFillCourses() {
		setContentView(R.layout.courses_page);
		allCourses = dbh.getAllCourses();
		courseListView = (ListView) findViewById(R.id.courseListView);
		adapter = new CourseListingAdapter(this, R.layout.course_row, allCourses);
		courseListView.setAdapter(adapter);
		setAdapterListeners();
	}

	private void setAdapterListeners() {
		courseListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> av, View view, final int pos, long id) {

				DialogInterface.OnClickListener diaClickListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case DialogInterface.BUTTON_POSITIVE:
							dbh.deleteCourse(getCourseFromPos(pos));
							removeFromCourses(pos);
							adapter.notifyDataSetChanged();
							break;
						default:
							break;
						}
					}
				};
				AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
				builder.setMessage("Delete " + getCourseFromPos(pos).getCourseCode() + "?");
				builder.setPositiveButton("Yes", diaClickListener);
				builder.setNegativeButton("No", diaClickListener);
				builder.show();
				return true;
			}
		});

		courseListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> av, View view, int pos, long id) {

				Intent intent = new Intent(view.getContext(), CourseDetailsActivity.class);
				Course c = getCourseFromPos(pos);
				intent.putExtra("COURSE_ID", c.getId());
				intent.putExtra("COURSE_CODE", c.getCourseCode());
				intent.putExtra("COURSE_NAME", c.getCourseName());
				startActivity(intent);
			}

		});

	}

	private Course getCourseFromPos(int pos) {
		return allCourses.get(pos);
	}

	private void removeFromCourses(int pos) {
		allCourses.remove(pos);
	}

	// Course fragment actions
	public void onClickCourse(View v) {
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

					if (!courseCode.getText().toString().isEmpty()) {
						Course course = new Course();
						course.setCourseCode(courseCode.getText().toString());
						course.setCourseName(courseName.getText().toString());
						course.setCurrentMark(0);
						dbh.addCourse(course);
						adapter.notifyDataSetChanged();
						SetAdapterFillCourses();
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
