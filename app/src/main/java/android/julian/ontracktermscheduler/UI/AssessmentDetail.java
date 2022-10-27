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
import android.julian.ontracktermscheduler.Entity.Assessment;
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

public class AssessmentDetail extends AppCompatActivity {
    EditText editAssessmentTitle;
    Spinner editAssessmentType;
    EditText editAssessmentStartDate;
    EditText editAssessmentEndDate;
    int assessmentID;
    Repository repository;
    String assessmentTitle;
    String assessmentType;
    String assessmentStart;
    String assessmentEnd;
    int courseID;

    DatePickerDialog.OnDateSetListener assessmentStartDate;
    DatePickerDialog.OnDateSetListener assessmentEndDate;
    final Calendar calendarEnd = Calendar.getInstance();
    final Calendar calendarStart = Calendar.getInstance();
    String format;
    SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);
        editAssessmentTitle =findViewById(R.id.editAssessmentTitle);
        editAssessmentType = findViewById(R.id.editAssessmentType);

        editAssessmentEndDate =findViewById(R.id.editAssessmentEndDate);
        editAssessmentStartDate =findViewById(R.id.editAssessmentStartDate);
        assessmentID = getIntent().getIntExtra("assessment_id", -1);
        assessmentTitle = getIntent().getStringExtra("assessment_title");
        assessmentType = getIntent().getStringExtra("assessment_type");
        assessmentEnd = getIntent().getStringExtra("assessment_end");
        assessmentStart = getIntent().getStringExtra("assessment_start");
        courseID = getIntent().getIntExtra("course_id", -1);
        editAssessmentTitle.setText(assessmentTitle);
        editAssessmentEndDate.setText(assessmentEnd);
        editAssessmentStartDate.setText(assessmentStart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        repository = new Repository(getApplication());
        ArrayList<Assessment> assessments = repository.getAllAssessments();
        final AssessmentAdapter assessmentAdapter = new AssessmentAdapter(this,assessments);
        assessmentAdapter.setAssessments(assessments);

        Spinner spinner = (Spinner) findViewById(R.id.editAssessmentType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.assessment_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        editAssessmentType.setSelection(adapter.getPosition(assessmentType));

        format = "MM/dd/yy";
        dateFormat = new SimpleDateFormat(format, Locale.US);

        editAssessmentStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date;
                String info = editAssessmentStartDate.getText().toString();
                if(info.equals(""));
                try{
                    calendarStart.setTime(dateFormat.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                new DatePickerDialog(AssessmentDetail.this, assessmentStartDate, calendarStart
                        .get(Calendar.YEAR), calendarStart.get(Calendar.MONTH),
                        calendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        editAssessmentEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date;
                String info = editAssessmentEndDate.getText().toString();
                if(info.equals(""));
                try{
                    calendarEnd.setTime(dateFormat.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                new DatePickerDialog(AssessmentDetail.this, assessmentEndDate, calendarEnd
                        .get(Calendar.YEAR), calendarEnd.get(Calendar.MONTH),
                        calendarEnd.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        assessmentStartDate = (datePicker, year, monthOfYear, dayOfMonth) -> {
            calendarStart.set(Calendar.YEAR,year);
            calendarStart.set(Calendar.MONTH,monthOfYear);
            calendarStart.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            editAssessmentStartDate.setText(dateFormat.format(calendarStart.getTime()));
        };

        assessmentEndDate = (datePicker, year, monthOfYear, dayOfMonth) -> {
            calendarEnd.set(Calendar.YEAR,year);
            calendarEnd.set(Calendar.MONTH,monthOfYear);
            calendarEnd.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            editAssessmentEndDate.setText(dateFormat.format(calendarEnd.getTime()));
        };

    }

    public void saveAssessment(View view) {

        if(editAssessmentType.getSelectedItem().toString().isEmpty()|editAssessmentTitle.getText().toString().isEmpty()|editAssessmentStartDate.getText().toString().isEmpty()|editAssessmentEndDate.getText().toString().isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(AssessmentDetail.this);
            builder.setTitle("Error!");
            builder.setMessage("All fields must be filled out before you can add this assessment.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            builder.show();
        } else {
            Assessment assessment;
            if (assessmentID == -1) {
                int id = repository.getAllAssessments().get(repository.getAllAssessments().size() -1).getAssessmentID() + 1;
                assessment = new Assessment(id, editAssessmentType.getSelectedItem().toString(), editAssessmentTitle.getText().toString(), editAssessmentStartDate.getText().toString(),editAssessmentEndDate.getText().toString(), CourseDetail.courseID);
                repository.insertAssessment(assessment);
            } else {
                assessment = new Assessment(assessmentID, editAssessmentType.getSelectedItem().toString(), editAssessmentTitle.getText().toString(), editAssessmentStartDate.getText().toString(), editAssessmentEndDate.getText().toString(), CourseDetail.courseID);
                repository.updateAssessment(assessment);
            }
            finish();
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_assessmentdetails, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Assessment assessment;
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.deleteAssessment:
                assessment = new Assessment(assessmentID,editAssessmentType.getSelectedItem().toString() , editAssessmentTitle.getText().toString(), editAssessmentStartDate.getText().toString(), editAssessmentEndDate.getText().toString(),CourseDetail.courseID);
                AlertDialog.Builder builder = new AlertDialog.Builder(AssessmentDetail.this);
                builder.setTitle("Are you sure?");
                builder.setMessage("Are you sure you want to delete " + editAssessmentTitle.getText().toString() +"?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        repository.deleteAssessment(assessment);

                        AlertDialog.Builder builder = new AlertDialog.Builder(AssessmentDetail.this);
                        builder.setTitle("Success!");
                        builder.setMessage(editAssessmentTitle.getText().toString() + " was successfully deleted!");
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
            case R.id.notifyAssessmentStart:
                String datePicked = editAssessmentStartDate.getText().toString();
                Date date = null;
                try {
                    date = dateFormat.parse(datePicked);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Long trigger = date.getTime();


                Intent intent1 = new Intent(AssessmentDetail.this, Receiver.class);
                intent1.putExtra("key","Your " + editAssessmentTitle.getText().toString() + " assessment starts today!");

                PendingIntent sender = PendingIntent.getBroadcast(AssessmentDetail.this,MainActivity.numAlert++, intent1,0);

                AlarmManager manager=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
                manager.set(AlarmManager.RTC_WAKEUP,trigger,sender);

                return true;
            case R.id.notifyAssessmentEnd:
                String datePicked1 = editAssessmentEndDate.getText().toString();
                Date date1 = null;
                try {
                    date1 = dateFormat.parse(datePicked1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Long trigger1 = date1.getTime();


                Intent intent2 = new Intent(AssessmentDetail.this, Receiver.class);
                intent2.putExtra("key","Your " + editAssessmentTitle.getText().toString() + " assessment ends today!");

                PendingIntent sender1 = PendingIntent.getBroadcast(AssessmentDetail.this,MainActivity.numAlert++, intent2,0);

                AlarmManager manager1=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
                manager1.set(AlarmManager.RTC_WAKEUP,trigger1,sender1);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void gotoAssessmentList(View view) {
        Intent intent = new Intent(AssessmentDetail.this, AssessmentList.class);
        startActivity(intent);
    }
}
