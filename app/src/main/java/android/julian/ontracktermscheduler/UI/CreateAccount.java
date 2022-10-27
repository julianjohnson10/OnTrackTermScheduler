package android.julian.ontracktermscheduler.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.julian.ontracktermscheduler.Database.Repository;
import android.julian.ontracktermscheduler.Entity.User;
import android.julian.ontracktermscheduler.R;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class CreateAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
    }

    public void goBack(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void createnewAcct(View view) throws NoSuchAlgorithmException, InvalidKeySpecException {

        EditText username = findViewById(R.id.createUser);
        EditText password = findViewById(R.id.createPass);
        Repository repo = new Repository(getApplication());
        if (username.getText().toString().isEmpty() | password.getText().toString().isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateAccount.this);
            builder.setTitle("Error!");
            builder.setMessage("Username or password cannot be blank!");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            builder.show();
        } else {
            if (repo.checkUserName(username.getText().toString()) !=  null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateAccount.this);
                builder.setTitle("Error!");
                builder.setMessage("Username " + "'" + username.getText().toString() + "'" + " exists! Please choose a different username.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.show();
            } else {

                if(password.getText().length()<=15){
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateAccount.this);
                    builder.setTitle("Error!");
                    builder.setMessage("Your password must be 16 characters minimum!");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    builder.show();
                } else {
                    SecureRandom rand = new SecureRandom();
                    byte[] salt = new byte[50];
                    rand.nextBytes(salt);

                    KeySpec keySpec = new PBEKeySpec(password.getText().toString().toCharArray(), salt, 65536, 128);
                    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");

                    byte[] hashedPass = factory.generateSecret(keySpec).getEncoded();
                    Base64.Encoder encoder = Base64.getEncoder();

                    User u = new User(username.getText().toString(), encoder.encodeToString(salt), encoder.encodeToString(hashedPass));
                    repo.insertUser(u);

                    Intent i = new Intent(this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
    }
}
}
