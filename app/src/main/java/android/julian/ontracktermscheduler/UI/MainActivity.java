package android.julian.ontracktermscheduler.UI;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.julian.ontracktermscheduler.Database.Repository;
import android.julian.ontracktermscheduler.Entity.Assessment;
import android.julian.ontracktermscheduler.Entity.Course;
import android.julian.ontracktermscheduler.Entity.Term;
import android.julian.ontracktermscheduler.Entity.User;
import android.os.Bundle;
import android.view.View;

import android.julian.ontracktermscheduler.R;
import android.widget.EditText;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class MainActivity extends AppCompatActivity {

    public static int numAlert;
    EditText enterPassword;
    EditText enterUsername;
    Repository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void createAcct(View view) {
        Intent intent = new Intent(MainActivity.this, CreateAccount.class);
        startActivity(intent);
    }


    public void enterApp(View view) throws NoSuchAlgorithmException, InvalidKeySpecException {
        repo = new Repository(getApplication());
        enterUsername=findViewById(R.id.enterUsername);
        enterPassword=findViewById(R.id.enterPassword);

        String userName = enterUsername.getText().toString();
        String passwordField = enterPassword.getText().toString();

        if (repo.checkUserName(userName) ==  null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Error!");
            builder.setMessage("Username is invalid!");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            builder.show();
        } else {
            User u = repo.getUser(userName);
            String userHash = u.getHash();
            String salt = u.getSalt();
            Base64.Decoder decoder = Base64.getDecoder();
            Base64.Encoder encoder = Base64.getEncoder();

            KeySpec keySpec = new PBEKeySpec(passwordField.toCharArray(), decoder.decode(salt), 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");

            byte[] hashedPass = factory.generateSecret(keySpec).getEncoded();

            String hashed = encoder.encodeToString(hashedPass);


            if (hashed.equals(userHash)){
                Intent intent = new Intent(MainActivity.this, TermList.class);
                startActivity(intent);
                Repository repo = new Repository(getApplication());
                Term term = new Term(1, "Fall Term 2022", "08/10/22", "02/20/23");
                repo.insertTerm(term);
                Course c = new Course(1, "Intro to IT", "08/10/22", "09/20/22", "In Progress", "Prof. Garcia", "(844)-100-2222", "jgarcia@school.edu", "Here are my class notes.", 1);
                repo.insertCourse(c);
                Course d = new Course(2, "Computer Science", "09/21/22", "10/31/22", "In Progress", "Prof. Wilson", "(829)-232-1231", "twilson@school.edu", "I love this class!", 1);
                repo.insertCourse(d);
                Assessment a = new Assessment(1, "Performance Assessment", "IT Fundamentals Test", "09/01/22", "09/20/22", 1);
                repo.insertAssessment(a);

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Error!");
                builder.setMessage("Invalid password!");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.show();
            }
        }
    }
}