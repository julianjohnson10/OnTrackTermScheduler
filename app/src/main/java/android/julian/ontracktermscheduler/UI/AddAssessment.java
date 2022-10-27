package android.julian.ontracktermscheduler.UI;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
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


public class AddAssessment extends AppCompatActivity {
    EditText addAssessmentTitle;
    Spinner addAssessmentType;
    EditText addAssessmentStartDate;
    EditText addAssessmentEndDate;
    String assessmentType;
    int assessmentID;
    Repository repo;

    DatePickerDialog.OnDateSetListener assessmentStartDate;
    DatePickerDialog.OnDateSetListener assessmentEndDate;
    final Calendar calendarEnd = Calendar.getInstance();
    final Calendar calendarStart = Calendar.getInstance();
    String format;
    SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assessment);
        addAssessmentTitle =findViewById(R.id.addAssessmentTitle);
        addAssessmentType = findViewById(R.id.addAssessmentType);
        addAssessmentStartDate =findViewById(R.id.addAssessmentStartDate);
        addAssessmentEndDate =findViewById(R.id.addAssessmentEndDate);
        assessmentID = getIntent().getIntExtra("assessment_id", -1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        repo = new Repository(getApplication());
        ArrayList<Assessment> assessments;
        assessments = repo.getAllAssessments();
        final AssessmentAdapter assessmentAdapter = new AssessmentAdapter(this, assessments);
        assessmentAdapter.setAssessments(assessments);

        Spinner spinner = (Spinner) findViewById(R.id.addAssessmentType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.assessment_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        addAssessmentType.setSelection(adapter.getPosition(assessmentType));

        format = "MM/dd/yy";
        dateFormat = new SimpleDateFormat(format, Locale.US);

        addAssessmentStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date;
                String info = addAssessmentStartDate.getText().toString();
                if(info.equals(""));
                try{
                    calendarStart.setTime(dateFormat.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                new DatePickerDialog(AddAssessment.this, assessmentStartDate, calendarStart
                        .get(Calendar.YEAR), calendarStart.get(Calendar.MONTH),
                        calendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        addAssessmentEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date;
                String info = addAssessmentEndDate.getText().toString();
                if(info.equals(""));
                try{
                    calendarEnd.setTime(dateFormat.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                new DatePickerDialog(AddAssessment.this, assessmentEndDate, calendarEnd
                        .get(Calendar.YEAR), calendarEnd.get(Calendar.MONTH),
                        calendarEnd.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        assessmentStartDate = (datePicker, year, monthOfYear, dayOfMonth) -> {
            calendarEnd.set(Calendar.YEAR,year);
            calendarEnd.set(Calendar.MONTH,monthOfYear);
            calendarEnd.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            addAssessmentStartDate.setText(dateFormat.format(calendarEnd.getTime()));
        };

        assessmentEndDate = (datePicker, year, monthOfYear, dayOfMonth) -> {
            calendarEnd.set(Calendar.YEAR,year);
            calendarEnd.set(Calendar.MONTH,monthOfYear);
            calendarEnd.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            addAssessmentEndDate.setText(dateFormat.format(calendarEnd.getTime()));
        };

    }

    public void addAssessment(View view) {
        if(addAssessmentType.getSelectedItem().toString().isEmpty()|addAssessmentTitle.getText().toString().isEmpty()|addAssessmentStartDate.getText().toString().isEmpty()|addAssessmentEndDate.getText().toString().isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(AddAssessment.this);
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
                int id = repo.getAllAssessments().get(repo.getAllAssessments().size() -1).getAssessmentID() + 1;
                assessment = new Assessment(id, addAssessmentType.getSelectedItem().toString(), addAssessmentTitle.getText().toString(), addAssessmentStartDate.getText().toString(), addAssessmentEndDate.getText().toString(), CourseDetail.courseID);
                repo.insertAssessment(assessment);
            } else {
                assessment = new Assessment(assessmentID, addAssessmentType.getSelectedItem().toString(), addAssessmentTitle.getText().toString(), addAssessmentStartDate.getText().toString(), addAssessmentEndDate.getText().toString(), CourseDetail.courseID);
                repo.updateAssessment(assessment);
            }
            finish();
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
}