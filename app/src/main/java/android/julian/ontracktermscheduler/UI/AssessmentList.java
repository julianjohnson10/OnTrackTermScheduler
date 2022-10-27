package android.julian.ontracktermscheduler.UI;

import android.content.Intent;
import android.julian.ontracktermscheduler.Database.Repository;
import android.julian.ontracktermscheduler.Entity.Assessment;
import android.julian.ontracktermscheduler.R;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class AssessmentList extends AppCompatActivity {

    int courseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_list);
        RecyclerView recyclerView=findViewById(R.id.assessmentRV);
        TextView emptyView = findViewById(R.id.empty_view_assessment);
        courseID = getIntent().getIntExtra("course_id", -1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Repository repository=new Repository(getApplication());
        ArrayList<Assessment> assessments = repository.getAssessmentsFromCourse(CourseDetail.courseID);
        final AssessmentAdapter assessmentAdapter = new AssessmentAdapter(this, assessments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(assessmentAdapter);
        assessmentAdapter.setAssessments(assessments);


        if (assessments.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        setContentView(R.layout.activity_assessment_list);
        RecyclerView recyclerView=findViewById(R.id.assessmentRV);
        TextView emptyView = findViewById(R.id.empty_view_assessment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        courseID = getIntent().getIntExtra("course_id", -1);
        Repository repository=new Repository(getApplication());
        ArrayList<Assessment> assessments = repository.getAssessmentsFromCourse(CourseDetail.courseID);
        final AssessmentAdapter assessmentAdapter = new AssessmentAdapter(this, assessments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(assessmentAdapter);
        assessmentAdapter.setAssessments(assessments);


        if (assessments.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
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

    public void gotoAddAssessment(View view) {
        Intent i = new Intent(AssessmentList.this, AddAssessment.class);
        startActivity(i);
    }
}