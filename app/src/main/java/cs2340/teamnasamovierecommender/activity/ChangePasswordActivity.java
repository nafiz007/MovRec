package cs2340.teamnasamovierecommender.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import cs2340.teamnasamovierecommender.R;
import cs2340.teamnasamovierecommender.pojo.DBHandler;

/**
 * Created by sai on 4/24/16.
 */
public class ChangePasswordActivity extends AppCompatActivity {

    AutoCompleteTextView password;
    AutoCompleteTextView confirm;
    DBHandler handler;


    SharedPreferences sharedPreferences;
    String username;

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        username = sharedPreferences.getString("Username", "userDefault");

        final Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Change Password");
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        password = (AutoCompleteTextView) findViewById(R.id.password);
        confirm = (AutoCompleteTextView) findViewById(R.id.confirmPassword);

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (password.getText().toString().isEmpty()
                            && !password.getText().toString().isEmpty())
                        password.setError("Password cannot be empty. Try again.");
                } else {
                    if (password.getText().toString().isEmpty())
                        password.setError("Password cannot be empty. Try again.");
                }
            }
        });

        confirm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (!confirm.getText().toString().equals(password.getText().toString())
                            && !confirm.getText().toString().isEmpty())
                        confirm.setError("Does not match password. Try again.");
                } else {
                    if (!confirm.getText().toString().equals(password.getText().toString()))
                        confirm.setError("Does not match password. Try again.");
                }
            }
        });
    }

    public void exit(View view) {
        password.setText(null);
        confirm.setText(null);
        password.requestFocus();
        finish();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            password.setText(null);
            confirm.setText(null);
            password.requestFocus();
            finish();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void apply(View view) {
        String passwordText = password.getText().toString();
        String confirmText = confirm.getText().toString();

        boolean validPassword = (password.getError() == null && !isEmpty(password));
        boolean validVerify = (confirm.getError() == null && !isEmpty(confirm));

        if (validPassword && validVerify && (passwordText.equals(confirmText))) {
            handler = new DBHandler(this, username.toUpperCase() + ".db");
            handler.updatePassword(passwordText);

            DBHandler adminDb = new DBHandler(this, "admin.db");

            Toast.makeText(getApplicationContext(), "Password changed.", Toast.LENGTH_SHORT).show();

            password.setText(null);
            confirm.setText(null);
            password.requestFocus();
            finish();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        } else {
            confirm.setError("Does not match password. Try again.");
        }
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
        password.setText(null);
        confirm.setText(null);
        password.requestFocus();
        finish();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }
}