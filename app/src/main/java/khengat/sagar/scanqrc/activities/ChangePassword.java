package khengat.sagar.scanqrc.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import khengat.sagar.scanqrc.R;
import khengat.sagar.scanqrc.Register;
import khengat.sagar.scanqrc.util.DatabaseHandler;

public class ChangePassword extends AppCompatActivity {
    private EditText etxPassword;
    private EditText etxConfirmPassword;
    private EditText etxCurrentPassword;
    private EditText etxCurrentEmail;
    private Button btnSubmit;
    private final AppCompatActivity activity = ChangePassword.this;
    private DatabaseHandler databaseHelper;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        etxPassword =(EditText)findViewById(R.id.password);
        etxConfirmPassword = (EditText) findViewById(R.id.confirmpassword);
        etxCurrentPassword = (EditText) findViewById(R.id.current_password);
        etxCurrentEmail = (EditText) findViewById(R.id.current_email);
        btnSubmit = (Button) findViewById(R.id.submit);
        databaseHelper = new DatabaseHandler(activity);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Change Password");


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etxCurrentEmail.getText().toString().trim().length()==0)
                {
                    etxCurrentEmail.setError("Email field can not be blank");
                    etxCurrentEmail.requestFocus();
                    return;
                }

                if(etxCurrentPassword.getText().toString().trim().length()==0)
                {
                    etxCurrentPassword.setError("Password field can not be blank");
                    etxCurrentPassword.requestFocus();
                    return;
                }

                if(etxPassword.getText().toString().trim().length()==0)
                {
                    etxPassword.setError("Password field can not be blank");
                    etxPassword.requestFocus();
                    return;
                }
                if(etxConfirmPassword.getText().toString().trim().length()==0)
                {
                    etxConfirmPassword.setError("Please Confirm your Password");
                    etxConfirmPassword.requestFocus();
                    return;

                }

                if (!(etxPassword.getText().toString().trim().equals(etxConfirmPassword.getText().toString().trim())))
                {
                    etxPassword.setError("Password does not Match please enter again");
                    etxPassword.requestFocus();
                    return;
                }
                else
                {
                    password = etxPassword.getText().toString();
                }
                fnSubmit();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void fnSubmit()
    {

        if (databaseHelper.checkUser(etxCurrentEmail.getText().toString().trim())) {
            if (databaseHelper.checkUser(etxCurrentEmail.getText().toString().trim(),etxCurrentPassword.getText().toString().trim())) {
            databaseHelper.updateUserPassword(etxCurrentEmail.getText().toString(),password);




            } else {
                // Snack Bar to show error message that record already exists
                Toast.makeText(activity, getString(R.string.error_password_not_exists), Toast.LENGTH_LONG).show();
            }

        } else {
            // Snack Bar to show error message that record already exists
            Toast.makeText(activity, getString(R.string.error_email_not_exists), Toast.LENGTH_LONG).show();
        }
    }

}