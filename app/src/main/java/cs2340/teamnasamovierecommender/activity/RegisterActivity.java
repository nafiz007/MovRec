package cs2340.teamnasamovierecommender.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import cs2340.teamnasamovierecommender.R;
import cs2340.teamnasamovierecommender.pojo.DBHandler;
import cs2340.teamnasamovierecommender.pojo.User;

/**
 * <b>!!! May need to be debugged, run the app and read the log for errors to fix them. !!!</b>
 * <p/>
 * This class handles the registration process. User gets here by tapping the "Register" button on
 * the landing page.
 *
 * @author Anas Tahir Khan
 * @author Anuragsharma Venukadasula
 * @author Sai Srivatsav Muppiri
 * @author Sayed Nafiz Imtiaz Ali
 * @version 3.0.2
 * @since 4/18/2016
 */
public class RegisterActivity extends AppCompatActivity {

    private AutoCompleteTextView username;
    private AutoCompleteTextView email;
    private AutoCompleteTextView password;
    private AutoCompleteTextView confirm;

    private Button register;
    private Button cancel;

    /**
     * Perform initialization of all fragments and loaders.
     *
     * @param savedInstanceState Bundle: If the activity is being re-initialized after previously
     *                           being shut down then this Bundle contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle).
     *                           <b>Note: Otherwise it is null.</b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.register_toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Register");
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        getViews();
        setupViews();
    }

    /**
     * Getter for the input fields for username, email, password, and confirm password.
     */
    private void getViews() {
        username = (AutoCompleteTextView) findViewById(R.id.username);
        email = (AutoCompleteTextView) findViewById(R.id.email);
        password = (AutoCompleteTextView) findViewById(R.id.password);
        confirm = (AutoCompleteTextView) findViewById(R.id.confirmPassword);

        register = (Button) findViewById(R.id.register);
        cancel = (Button) findViewById(R.id.cancel);
    }

    /**
     * Sets up the input fields on the registration screen and shows errors if input fields are left
     * empty.
     */
    private void setupViews() {
        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (checkDataBase(username.getText().toString()))
                        username.setError("Username taken. Try again.");
                } else {
                    if (checkDataBase(username.getText().toString()))
                        username.setError("Username taken. Try again.");
                    if (username.getText().toString().isEmpty())
                        username.setError("Username cannot be empty. Try again.");
                }
            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (!checkEmail(email.getText().toString())
                            && !email.getText().toString().isEmpty())
                        email.setError("E-Mail is invalid. Try again.");
                } else {
                    if (!checkEmail(email.getText().toString()))
                        email.setError("E-Mail is invalid. Try again.");
                }
            }
        });

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

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    /**
     * Checks if the given field is empty and sets an error if it is.
     *
     * @param autoCompleteTextView AutoCompleteTextView: the field that needs to be checked.
     * @return boolean: whether the given field is empty or not.
     */
    private boolean isEmpty(AutoCompleteTextView autoCompleteTextView) {
        boolean empty = autoCompleteTextView.getText().toString().isEmpty();
        if (empty) {
            autoCompleteTextView.setError("Cannot be empty! Try again.");
        }
        return empty;
    }

    /**
     * Checks if the email matches the pattern Patterns.EMAIL_ADDRESS.
     *
     * @param email String: the email string that needs to be checked.
     * @return boolean: whether the email string matches the pattern Patterns.EMAIL_ADDRESS.
     */
    private boolean checkEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Checks if a database with the name given exists or not.
     *
     * @param dbName String: name of database to check.
     * @return boolean: whether database with the given name exists or not.
     */
    private boolean checkDataBase(String dbName) {
        if (dbName.equalsIgnoreCase("admin")) {
            return true;
        }
        dbName = dbName.replaceAll("\\s", "");
        dbName = dbName.toUpperCase();
        SQLiteDatabase checkDB = null;
        String dbPath = "//data/data/cs2340.teamnasamovierecommender/databases/" + dbName + ".db";
        try {
            checkDB = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException e) {
            Log.d("SQL", "User DNE");
        }
        return checkDB != null;
    }


    /**
     * Checks if all of the fields required to register are valid. Then, if they are, creates a
     * database for the user, logs them in and takes to the home screen of the app.
     */
    private void register() {
        boolean validUsername = (username.getError() == null && !isEmpty(username));
        boolean validEmail = (email.getError() == null && !isEmpty(email));
        boolean validPassword = (password.getError() == null && !isEmpty(password));
        boolean validVerify = (confirm.getError() == null && !isEmpty(confirm));

        if (validUsername && validEmail && validPassword && validVerify) {
            String userDB = username.getText().toString();
            userDB = userDB.replaceAll("\\s", "");
            userDB = userDB.toUpperCase();

            DBHandler handler = new DBHandler(this, userDB + ".db");
            User user = new User(username.getText().toString(),
                    password.getText().toString(), email.getText().toString(), "Computer Science");
            handler.addUser(user);
            DBHandler adminDb = new DBHandler(this, "admin.db");
            adminDb.addUser(user);

            Toast.makeText(getApplicationContext(), "Successfully registed as " + username.getText(), Toast.LENGTH_SHORT).show();

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("Username", username.getText().toString());
            editor.apply();
            startActivity(new Intent(this, HomeActivity.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        } else Log.d("Register", "Something went wrong.");
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
