package android.julian.ontracktermscheduler.UI;

import android.content.Intent;
import android.julian.ontracktermscheduler.Database.Repository;
import android.julian.ontracktermscheduler.Entity.Course;
import android.julian.ontracktermscheduler.R;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;



public class CourseList extends AppCompatActivity {

    @Override
    protected void onResume() {

        super.onResume();
        setContentView(R.layout.activity_course_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RecyclerView recyclerView = findViewById(R.id.courseRV);
        TextView emptyView = findViewById(R.id.empty_view);

        Repository repository = new Repository(getApplication());
        ArrayList<Course> courses = repository.getCoursesFromTerm(TermDetails.termID);
//        ArrayList<Course> courses = repository.searchCourse(TermDetails.termID,"Test%");
        final CourseAdapter courseAdapter = new CourseAdapter(this, courses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(courseAdapter);
        courseAdapter.setCourses(courses);

        if (courses.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

        EditText searchField = findViewById(R.id.searchField);

        searchField.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN)) {
                    ArrayList<Course> courses = repository.searchCourse(TermDetails.termID,"%"+searchField.getText().toString() + "%");
                    courseAdapter.setCourses(courses);
                    System.out.println(searchField.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.baseline, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void gotoAddCourse(View view) {
        Intent i = new Intent(CourseList.this, AddCourse.class);
        startActivity(i);
    }
}