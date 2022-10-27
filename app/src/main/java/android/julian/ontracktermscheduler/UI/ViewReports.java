package android.julian.ontracktermscheduler.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.julian.ontracktermscheduler.Database.Repository;
import android.julian.ontracktermscheduler.Entity.Course;
import android.julian.ontracktermscheduler.Entity.Term;
import android.os.Bundle;

import android.julian.ontracktermscheduler.R;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;

public class ViewReports extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reports);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void runReport(View view){
        Repository repository=new Repository(getApplication());
        ArrayList<Term> terms= repository.getAllTerms();
        TableLayout tableLayout1 = findViewById(R.id.tableLayout1);
        TableLayout tableLayout2 = findViewById(R.id.tableLayout2);
        TextView reportTime = findViewById(R.id.reportTime);
        TextView reportTitle = findViewById(R.id.reportTitle);

        LocalDateTime ldt = LocalDateTime.now();

        tableLayout1.removeAllViews();
        tableLayout2.removeAllViews();

        TableRow titleRow = new TableRow(this);
        TextView col1 = new TextView(this);
        TextView col2 = new TextView(this);
        TextView col3 = new TextView(this);
        TextView col4 = new TextView(this);
        TextView col5 = new TextView(this);
        col1.setText("Term Name");
        col2.setText("Course Title");
        col3.setText("Instructor Name");
        col4.setText("Instructor Phone");
        col5.setText("Instructor Email");

        titleRow.addView(col1);
        titleRow.addView(col2);
        titleRow.addView(col3);
        titleRow.addView(col4);
        titleRow.addView(col5);
        tableLayout1.addView(titleRow);

        col1.setPadding(10,0,20,0);
        col2.setPadding(10,0,20,0);
        col3.setPadding(10,0,20,0);
        col4.setPadding(10,0,20,0);
        col5.setPadding(10,0,20,0);

        reportTitle.setText("Report Title: Instructors per Course");
        reportTime.setText("Report Time: " + DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).format(ldt));

        for (Term term: terms){
            ArrayList<Course> courses= repository.getCoursesFromTerm(term.getTermID());
            for (Course course: courses){
                TableRow infoRow = new TableRow(this);
                TextView termName = new TextView(this);
                termName.setPadding(10,0,20,0);
                termName.setText(term.getTermName());

                TextView courseName = new TextView(this);
                courseName.setPadding(10,0,20,0);
                courseName.setText(course.getCourseTitle());

                TextView courseInstructor = new TextView(this);
                courseInstructor.setText(course.getInstructorName());

                TextView instructorPhone = new TextView(this);
                instructorPhone.setPadding(10,0,20,0);
                instructorPhone.setText(course.getInstructorPhone());

                TextView instructorEmail = new TextView(this);
                instructorEmail.setPadding(10,0,20,0);
                instructorEmail.setText(course.getInstructorEmail());

                infoRow.addView(termName);
                infoRow.addView(courseName);
                infoRow.addView(courseInstructor);
                infoRow.addView(instructorPhone);
                infoRow.addView(instructorEmail);

                tableLayout1.addView(infoRow);
            }
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_view_reports);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}