package android.julian.ontracktermscheduler.UI;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class CourseDetail extends AppCompatActivity {
    EditText editCourseTitle;
    EditText editCourseStart;
    EditText editCourseEnd;
    Spinner editCourseStatus;
    EditText editInstructorName;
    EditText editInstructorPhone;
    EditText editInstructorEmail;
    EditText editCourseNotes;
    public static int courseID;
    Repository repository;
    String courseTitle;
    String courseStart;
    String courseEnd;
    int termID;
    DatePickerDialog.OnDateSetListener startDate;
    DatePickerDialog.OnDateSetListener endDate;
    final Calendar calendarStart = Calendar.getInstance();
    final Calendar calendarEnd = Calendar.getInstance();
    String format;
    SimpleDateFormat dateFormat;
    String courseStatus;
    String courseInstructor;
    String courseInstructorPhone;
    String courseInstructorEmail;

    String courseNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        editCourseTitle=findViewById(R.id.editCourseTitle);
        editCourseStart=findViewById(R.id.editCourseStart);
        editCourseNotes = findViewById(R.id.editCourseNotes);
        editCourseEnd=findViewById(R.id.editCourseEnd);
        editCourseStatus=findViewById(R.id.editCourseStatus);
        editInstructorName=findViewById(R.id.editInstructorName);
        editInstructorPhone=findViewById(R.id.editInstructorPhone);
        editInstructorEmail=findViewById(R.id.editInstructorEmail);
        courseID = getIntent().getIntExtra("course_id", -1);
        courseTitle = getIntent().getStringExtra("course_title");
        courseStart = getIntent().getStringExtra("course_start");
        courseNotes = getIntent().getStringExtra("course_notes");
        courseStatus = getIntent().getStringExtra("course_status");
        courseEnd = getIntent().getStringExtra("course_end");
        courseInstructor = getIntent().getStringExtra("course_instructor_name");
        courseInstructorPhone = getIntent().getStringExtra("course_instructor_phone");
        courseInstructorEmail = getIntent().getStringExtra("course_instructor_email");
        editCourseTitle.setText(courseTitle);
        editCourseNotes.setText(courseNotes);

        editCourseStart.setText(courseStart);
        editCourseEnd.setText(courseEnd);
        editInstructorName.setText(courseInstructor);
        editInstructorPhone.setText(courseInstructorPhone);
        editInstructorEmail.setText(courseInstructorEmail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        repository = new Repository(getApplication());
        ArrayList<Course> courses;
        courses = repository.getAllCourses();
        final CourseAdapter courseAdapter = new CourseAdapter(this,courses);
        courseAdapter.setCourses(courses);

        Spinner spinner = (Spinner) findViewById(R.id.editCourseStatus);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.course_status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        editCourseStatus.setSelection(adapter.getPosition(courseStatus));

        format = "MM/dd/yy";
        dateFormat = new SimpleDateFormat(format, Locale.US);

        editCourseStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date;
                String info = editCourseStart.getText().toString();
                if(info.equals(""));
                try{
                    calendarStart.setTime(dateFormat.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                new DatePickerDialog(CourseDetail.this, startDate, calendarStart
                        .get(Calendar.YEAR), calendarStart.get(Calendar.MONTH),
                        calendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        editCourseEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date;
                String info = editCourseEnd.getText().toString();
                if(info.equals(""));
                try{
                    calendarEnd.setTime(dateFormat.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                new DatePickerDialog(CourseDetail.this, endDate, calendarEnd
                        .get(Calendar.YEAR), calendarEnd.get(Calendar.MONTH),
                        calendarEnd.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        startDate = (datePicker, year, monthOfYear, dayOfMonth) -> {
            calendarStart.set(Calendar.YEAR,year);
            calendarStart.set(Calendar.MONTH,monthOfYear);
            calendarStart.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            editCourseStart.setText(dateFormat.format(calendarStart.getTime()));
        };

        endDate = (datePicker, year, monthOfYear, dayOfMonth) -> {
            calendarEnd.set(Calendar.YEAR,year);
            calendarEnd.set(Calendar.MONTH,monthOfYear);
            calendarEnd.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            editCourseEnd.setText(dateFormat.format(calendarEnd.getTime()));
        };
    }

    public void saveCourse(View view) {

        if(editCourseTitle.getText().toString().isEmpty()|editCourseStart.getText().toString().isEmpty()|editCourseEnd.getText().toString().isEmpty()|editCourseStatus.getSelectedItem().toString().isEmpty()|editInstructorName
                .getText().toString().isEmpty()|editInstructorPhone.getText().toString().isEmpty()|editInstructorEmail
                .getText().toString().isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(CourseDetail.this);
            builder.setTitle("Error!");
            builder.setMessage("All fields must be filled out before you can update this course. Optional fields can be left blank.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            builder.show();
        } else {
            Course course;

            if (courseID == -1) {
                int id = repository.getAllTerms().get(repository.getAllTerms().size() -1).getTermID() + 1;
                course = new Course(id, editCourseTitle.getText().toString(), editCourseStart.getText().toString(), editCourseEnd.getText().toString(),editCourseStatus.getSelectedItem().toString(), editInstructorName.getText().toString(),editInstructorPhone.getText().toString(),editInstructorEmail.getText().toString(), editCourseNotes.getText().toString(),TermDetails.termID);
                repository.insertCourse(course);
            } else {
                course = new Course(courseID, editCourseTitle.getText().toString(), editCourseStart.getText().toString(), editCourseEnd.getText()
                        .toString(),editCourseStatus.getSelectedItem()
                        .toString(), editInstructorName.getText()
                        .toString(),editInstructorPhone.getText().toString(),editInstructorEmail.getText()
                        .toString(), editCourseNotes.getText().toString(),TermDetails.termID);
                repository.updateCourse(course);
            }
            finish();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_coursedetail, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Course course;
        switch (item.getItemId()) {

            case android.R.id.home:
                this.finish();
                return true;
            case R.id.shareNotes:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, this.editCourseNotes.getText().toString());
                intent.putExtra(Intent.EXTRA_TITLE, "Title");
                intent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(intent, null);
                startActivity(shareIntent);
                return true;
            case R.id.deleteCourse:
                course = new Course(courseID, editCourseTitle.getText().toString(), editCourseStart.getText().toString(), editCourseEnd.getText()
                        .toString(),editCourseStatus.getSelectedItem()
                        .toString(), editInstructorName.getText()
                        .toString(),editInstructorPhone.getText().toString(),editInstructorEmail.getText()
                        .toString(), editCourseNotes.getText().toString(),TermDetails.termID);
                AlertDialog.Builder builder = new AlertDialog.Builder(CourseDetail.this);
                builder.setTitle("Are you sure?");
                builder.setMessage("Are you sure you want to delete " + editCourseTitle.getText().toString() +"?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        repository.deleteCourse(course);

                        AlertDialog.Builder builder = new AlertDialog.Builder(CourseDetail.this);
                        builder.setTitle("Success!");
                        builder.setMessage(editCourseTitle.getText().toString() + " was successfully deleted!");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builder.show();

                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
                return true;
            case R.id.notifyCourseStart:
                String datePicked = editCourseStart.getText().toString();

                Date date = null;

                try {
                    date = dateFormat.parse(datePicked);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Long trigger = date.getTime();

                Intent intent1 = new Intent(CourseDetail.this, Receiver.class);
                intent1.putExtra("key","Your " + editCourseTitle.getText().toString() + " course starts today!");

                PendingIntent sender = PendingIntent.getBroadcast(CourseDetail.this,MainActivity.numAlert++, intent1,0);
                AlarmManager manager=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
                manager.set(AlarmManager.RTC_WAKEUP,trigger,sender);

                return true;
            case R.id.notifyCourseEnd:
                String datePicked2 = editCourseEnd.getText().toString();
                Date date2 = null;
                try {
                    date2 = dateFormat.parse(datePicked2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Long trigger2 = date2.getTime();

                Intent intent2 = new Intent(CourseDetail.this, Receiver.class);
                intent2.putExtra("key","Your " + editCourseTitle.getText().toString() + " course ends today!");

                PendingIntent sender2 = PendingIntent.getBroadcast(CourseDetail.this,MainActivity.numAlert++, intent2,0);

                AlarmManager manager2=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
                manager2.set(AlarmManager.RTC_WAKEUP,trigger2,sender2);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void gotoAssessmentList(View view) {
        Intent intent = new Intent(CourseDetail.this, AssessmentList.class);
        startActivity(intent);
    }
}
