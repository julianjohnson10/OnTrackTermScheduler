package android.julian.ontracktermscheduler.UI;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.julian.ontracktermscheduler.Database.Repository;
import android.julian.ontracktermscheduler.Entity.Term;
import android.julian.ontracktermscheduler.R;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TermDetails extends AppCompatActivity {
    EditText editTermName;
    EditText editStartDate;
    EditText editEndDate;
    String termName;
    public static int termID;
    Repository repository;

    DatePickerDialog.OnDateSetListener startDate;
    DatePickerDialog.OnDateSetListener endDate;
    final Calendar calendarStart = Calendar.getInstance();
    final Calendar calendarEnd = Calendar.getInstance();
    String format;
    SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);
        editTermName=findViewById(R.id.editTermName);
        editStartDate=findViewById(R.id.editStartDate);
        editEndDate=findViewById(R.id.editEndDate);
        termID = getIntent().getIntExtra("term_id", -1);
        termName = getIntent().getStringExtra("term_name");
        String termStart = getIntent().getStringExtra("term_start");
        String termEnd = getIntent().getStringExtra("term_end");
        editTermName.setText(termName);
        editStartDate.setText(termStart);
        editEndDate.setText(termEnd);
        repository = new Repository(getApplication());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ArrayList<Term> terms = repository.getAllTerms();
        final TermAdapter termAdapter=new TermAdapter(this, terms);
        termAdapter.setTerms(terms);

        format = "MM/dd/yy";
        dateFormat = new SimpleDateFormat(format, Locale.US);

        editStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date;
                String info = editStartDate.getText().toString();
                if(info.equals(""));
                try{
                    calendarStart.setTime(dateFormat.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                new DatePickerDialog(TermDetails.this, startDate, calendarStart
                        .get(Calendar.YEAR), calendarStart.get(Calendar.MONTH),
                        calendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        editEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date;
                String info = editEndDate.getText().toString();
                if(info.equals(""));
                try{
                    calendarEnd.setTime(dateFormat.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                new DatePickerDialog(TermDetails.this, endDate, calendarEnd
                        .get(Calendar.YEAR), calendarEnd.get(Calendar.MONTH),
                        calendarEnd.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        startDate = (datePicker, year, monthOfYear, dayOfMonth) -> {
            calendarStart.set(Calendar.YEAR,year);
            calendarStart.set(Calendar.MONTH,monthOfYear);
            calendarStart.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            editStartDate.setText(dateFormat.format(calendarStart.getTime()));
        };

        endDate = (datePicker, year, monthOfYear, dayOfMonth) -> {
            calendarEnd.set(Calendar.YEAR,year);
            calendarEnd.set(Calendar.MONTH,monthOfYear);
            calendarEnd.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            editEndDate.setText(dateFormat.format(calendarEnd.getTime()));
        };

    }

    public void saveTerm(View view) {

        if(editTermName.getText().toString().isEmpty()|editStartDate.getText().toString().isEmpty()|editEndDate.getText().toString().isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(TermDetails.this);
            builder.setTitle("Error!");
            builder.setMessage("All fields must be filled out before you can update this term.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            builder.show();
        } else {
            Term t;
            if (termID == -1) {
                int id = repository.getAllTerms().get(repository.getAllTerms().size() -1).getTermID() + 1;
                t = new Term(id, editTermName.getText().toString(), editStartDate.getText().toString(), editEndDate.getText().toString());
                repository.insertTerm(t);
            } else {
                t = new Term(termID, editTermName.getText().toString(), editStartDate.getText().toString(), editEndDate.getText().toString());
                repository.updateTerm(t);
            }
            finish();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_termdetails, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Term term;
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.deleteTerm:
                if(repository.getCoursesFromTerm(termID).isEmpty()){
                    term = new Term(termID, editTermName.getText().toString(), editStartDate.getText().toString(), editEndDate.getText().toString());
                    repository.deleteTerm(term);

                    AlertDialog.Builder builder = new AlertDialog.Builder(TermDetails.this);
                    builder.setTitle("Success!");
                    builder.setMessage(editTermName.getText().toString() + " was successfully deleted!");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    builder.show();

                    this.finish();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(TermDetails.this);
                    builder.setTitle("Error!");
                    builder.setMessage(editTermName.getText().toString() + " cannot be deleted because it has " +
                            "courses associated with it.\n\nPlease delete the associated courses first.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    builder.show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void gotoCourseList(View view) {
        Intent intent = new Intent(TermDetails.this, CourseList.class);
        startActivity(intent);
    }
}