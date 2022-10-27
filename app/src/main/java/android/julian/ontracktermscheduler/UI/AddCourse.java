package android.julian.ontracktermscheduler.UI;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.julian.ontracktermscheduler.Database.Repository;
import android.julian.ontracktermscheduler.Entity.Course;
import android.julian.ontracktermscheduler.R;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddCourse extends AppCompatActivity {
    EditText addCourseTitle;
    EditText addCourseStart;
    EditText addCourseEnd;
    Spinner addCourseStatus;
    EditText addInstructorName;
    EditText addInstructorPhone;
    EditText addInstructorEmail;
    EditText addCourseNotes;
    String courseTitle;
    String courseStart;
    String courseEnd;
    String courseNotes;
    String courseStatus;
    String courseInstructor;
    String courseInstructorPhone;
    String courseInstructorEmail;
    int courseID;
    Repository repo;
    DatePickerDialog.OnDateSetListener startDate;
    DatePickerDialog.OnDateSetListener endDate;
    final Calendar calendarStart = Calendar.getInstance();
    final Calendar calendarEnd = Calendar.getInstance();
    String format;
    SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        addCourseTitle =findViewById(R.id.addCourseTitle);
        addCourseStart =findViewById(R.id.addCourseStart);
        addCourseEnd =findViewById(R.id.addCourseEnd);
        addCourseStatus = findViewById(R.id.addCourseStatus);
        addInstructorName = findViewById(R.id.addInstructorName);
        addInstructorPhone = findViewById(R.id.addInstructorPhone);
        addInstructorEmail = findViewById(R.id.addInstructorEmail);
        addCourseNotes = findViewById(R.id.addCourseNotes);
        courseID = getIntent().getIntExtra("course_id", -1);
        courseTitle = getIntent().getStringExtra("course_title");
        courseStart = getIntent().getStringExtra("course_start");
        courseNotes = getIntent().getStringExtra("course_notes");
        courseStatus = getIntent().getStringExtra("course_status");
        courseEnd = getIntent().getStringExtra("course_end");
        courseInstructor = getIntent().getStringExtra("course_instructor_name");
        courseInstructorPhone = getIntent().getStringExtra("course_instructor_phone");
        courseInstructorEmail = getIntent().getStringExtra("course_instructor_email");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        repo = new Repository(getApplication());
        ArrayList<Course> courses;
        courses = repo.getAllCourses();
        final CourseAdapter courseAdapter=new CourseAdapter(this, courses);
        courseAdapter.setCourses(courses);

        Spinner spinner = (Spinner) findViewById(R.id.addCourseStatus);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.course_status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        addCourseStatus.setSelection(adapter.getPosition(courseStatus));


        format = "MM/dd/yy";
        dateFormat = new SimpleDateFormat(format, Locale.US);

        addCourseStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date;
                String info = addCourseStart.getText().toString();
                if(info.equals(""));
                try{
                    calendarStart.setTime(dateFormat.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                new DatePickerDialog(AddCourse.this, startDate, calendarStart
                        .get(Calendar.YEAR), calendarStart.get(Calendar.MONTH),
                        calendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        addCourseEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date;
                String info = addCourseEnd.getText().toString();
                if(info.equals(""));
                try{
                    calendarEnd.setTime(dateFormat.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                new DatePickerDialog(AddCourse.this, endDate, calendarEnd
                        .get(Calendar.YEAR), calendarEnd.get(Calendar.MONTH),
                        calendarEnd.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        startDate = (datePicker, year, monthOfYear, dayOfMonth) -> {
            calendarStart.set(Calendar.YEAR,year);
            calendarStart.set(Calendar.MONTH,monthOfYear);
            calendarStart.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            addCourseStart.setText(dateFormat.format(calendarStart.getTime()));
        };

        endDate = (datePicker, year, monthOfYear, dayOfMonth) -> {
            calendarEnd.set(Calendar.YEAR,year);
            calendarEnd.set(Calendar.MONTH,monthOfYear);
            calendarEnd.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            addCourseEnd.setText(dateFormat.format(calendarEnd.getTime()));
        };
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

    public void addCourse(View view) {

        if(addCourseTitle.getText().toString().isEmpty()|addCourseStart.getText().toString().isEmpty()|addCourseEnd.getText().toString().isEmpty()|addCourseStatus.getSelectedItem().toString().isEmpty()|addInstructorName
                .getText().toString().isEmpty()|addInstructorPhone.getText().toString().isEmpty()|addInstructorEmail
                .getText().toString().isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(AddCourse.this);
            builder.setTitle("Error!");
            builder.setMessage("All fields must be filled out before you can add this course. Optional fields can be left blank.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            builder.show();
        } else {
            Course c;
            if (courseID == -1) {
                int id = repo.getAllCourses().get(repo.getAllCourses().size() -1).getCourseID() + 1;
                c = new Course(id, addCourseTitle.getText().toString(), addCourseStart.getText().toString(), addCourseEnd
                        .getText().toString(), addCourseStatus.getSelectedItem().toString(),addInstructorName
                        .getText().toString(),addInstructorPhone.getText().toString(),addInstructorEmail
                        .getText().toString(),addCourseNotes.getText().toString(),TermDetails.termID);
                repo.insertCourse(c);
            } else {
                c = new Course(courseID,addCourseTitle.getText().toString(), addCourseStart.getText().toString(), addCourseEnd
                        .getText().toString(), addCourseStatus.getSelectedItem().toString(),addInstructorName
                        .getText().toString(),addInstructorPhone.getText().toString(),addInstructorEmail
                        .getText().toString(),addCourseNotes.getText().toString(),TermDetails.termID);
                repo.updateCourse(c);
            }
            finish();
        }
    }
}