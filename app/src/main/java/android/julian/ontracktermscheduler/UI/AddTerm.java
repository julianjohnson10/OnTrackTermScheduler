package android.julian.ontracktermscheduler.UI;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
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

public class AddTerm extends AppCompatActivity {
    EditText addTermName;
    EditText addTermStart;
    EditText addTermEnd;
    int termID;
    Repository repository;
    String termName;
    String termStart;
    String termEnd;
    DatePickerDialog.OnDateSetListener startDate;
    DatePickerDialog.OnDateSetListener endDate;
    final Calendar calendarStart = Calendar.getInstance();
    final Calendar calendarEnd = Calendar.getInstance();
    String format;
    SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);
        addTermName =findViewById(R.id.addTermName);
        addTermStart=findViewById(R.id.addTermStart);
        addTermEnd =findViewById(R.id.addTermEnd);

        termID = getIntent().getIntExtra("term_id", -1);
        termName = getIntent().getStringExtra("term_name");
        termStart = getIntent().getStringExtra("term_start");
        termEnd = getIntent().getStringExtra("term_end");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        repository = new Repository(getApplication());
        ArrayList<Term> terms = repository.getAllTerms();
        final TermAdapter termAdapter=new TermAdapter(this, terms);
        termAdapter.setTerms(terms);


        format = "MM/dd/yy";
        dateFormat = new SimpleDateFormat(format, Locale.US);

        addTermStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date;
                String info = addTermStart.getText().toString();
                if(info.equals(""));
                try{
                    calendarStart.setTime(dateFormat.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                new DatePickerDialog(AddTerm.this, startDate, calendarStart
                        .get(Calendar.YEAR), calendarStart.get(Calendar.MONTH),
                        calendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        addTermEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date;
                String info = addTermEnd.getText().toString();
                if(info.equals(""));
                try{
                    calendarEnd.setTime(dateFormat.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                new DatePickerDialog(AddTerm.this, endDate, calendarEnd
                        .get(Calendar.YEAR), calendarEnd.get(Calendar.MONTH),
                        calendarEnd.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        startDate = (datePicker, year, monthOfYear, dayOfMonth) -> {
            calendarStart.set(Calendar.YEAR,year);
            calendarStart.set(Calendar.MONTH,monthOfYear);
            calendarStart.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            addTermStart.setText(dateFormat.format(calendarStart.getTime()));
        };

        endDate = (datePicker, year, monthOfYear, dayOfMonth) -> {
            calendarEnd.set(Calendar.YEAR,year);
            calendarEnd.set(Calendar.MONTH,monthOfYear);
            calendarEnd.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            addTermEnd.setText(dateFormat.format(calendarEnd.getTime()));
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

    public boolean checkValidTerm(){
        return !(addTermName.getText().toString().isEmpty() | addTermStart.getText().toString().isEmpty() | addTermEnd.getText().toString().isEmpty());
    }

    public void addTerm(View view) {
        Term t;

        if (!checkValidTerm()){
            AlertDialog.Builder builder = new AlertDialog.Builder(AddTerm.this);
            builder.setTitle("Error!");
            builder.setMessage("All fields must be filled out before you can add a term.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            builder.show();
        } else {
            if (termID == -1) {
                int id = repository.getAllTerms().get(repository.getAllTerms().size() -1).getTermID() + 1;
                t = new Term(id, addTermName.getText().toString(), addTermStart.getText().toString(), addTermEnd.getText().toString());
                repository.insertTerm(t);
            } else {
                t = new Term(termID, addTermName.getText().toString(), addTermStart.getText().toString(), addTermEnd.getText().toString());
                repository.updateTerm(t);
            }
            finish();
        }
    }



}