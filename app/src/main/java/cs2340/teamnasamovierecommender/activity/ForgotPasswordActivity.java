package cs2340.teamnasamovierecommender.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import javax.mail.MessagingException;

import cs2340.teamnasamovierecommender.R;
import cs2340.teamnasamovierecommender.pojo.DBHandler;
import cs2340.teamnasamovierecommender.pojo.GmailSender;

/**
 * Created by sai on 4/24/16.
 */
public class ForgotPasswordActivity extends AppCompatActivity {

    AutoCompleteTextView recoveryEmail;
    AutoCompleteTextView username;
    DBHandler handler;
    GmailSender sender;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Forgot Password");
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recoveryEmail = (AutoCompleteTextView) findViewById(R.id.email);
        username = (AutoCompleteTextView) findViewById(R.id.username);
        String email = recoveryEmail.getText().toString();
        String user = username.getText().toString();

        sender = new GmailSender();
    }

    private boolean isEmpty(AutoCompleteTextView autoCompleteTextView) {
        boolean empty = autoCompleteTextView.getText().toString().isEmpty();
        if (empty) {
            autoCompleteTextView.setError("Cannot be empty! Try again.");
        }
        return empty;
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public void sendEmail(View view) {
        boolean emailEmpty = isEmpty(recoveryEmail);
        boolean usernameEmpty = isEmpty(username);
        if (!emailEmpty && !usernameEmpty) {

            String usernameText = username.getText().toString();

            String email = recoveryEmail.getText().toString();

            handler = new DBHandler(this, usernameText.toUpperCase() + ".db");

            if (email.equalsIgnoreCase(handler.getEmail())) {
                String pass = generateRandom();

                new MyAsyncClass().execute(usernameText, pass, email);
            }

        } else {
            Log.d("Forgot", "Email was wrong.");
        }
    }

    private String generateRandom() {
        final String alphaNumerics = "qwertyuiopasdfghjklzxcvbnm1234567890";
        String t = "";
        final int eight = 8;
        for (int i = 0; i < eight; i++) {
            t += alphaNumerics.charAt((int) (Math.random() * alphaNumerics.length()));
        }
        return t;
    }

    class MyAsyncClass extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(final String... params) {
            try {
                // Add subject, Body, your mail Id, and receiver mail Id.
                final String user = params[0];
                final String pass = params[1];
                final String email2 = params[2];
                sender.sendMail(
                        "Hi " + user + ",\n\n" +
                                "Your new password is " + pass + ". It is recommended that you change it after you log back in." +
                                ".\n\nTeam NASA ",
                        email2);
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Recovery email sent.", Toast.LENGTH_SHORT).show();
                        handler.updatePassword(pass);
                        startActivity(new Intent(ForgotPasswordActivity.this, MainActivity.class));
                        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
                    }
                });

            } catch (MessagingException e) {
                Log.e("Exc", e.toString());
                Toast.makeText(getApplicationContext(), "Invalid info. Try again.", Toast.LENGTH_SHORT).show();
            }
            return null;
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
