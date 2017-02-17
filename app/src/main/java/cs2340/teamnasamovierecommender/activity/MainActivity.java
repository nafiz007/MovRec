package cs2340.teamnasamovierecommender.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import cs2340.teamnasamovierecommender.R;
import cs2340.teamnasamovierecommender.pojo.DBHandler;

/**
 * This class handles what the user first sees when the app launches (the landing page). Display a
 * "Login" button and a "Register" button which when tapped will take the user to the login page and
 * the registration page, respectively.
 *
 * @author Anas Tahir Khan
 * @author Anuragsharma Venukadasula
 * @author Sai Srivatsav Muppiri
 * @author Sayed Nafiz Imtiaz Ali
 * @version 3.0.2
 * @since 4/18/2016
 */
public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView username;
    private AutoCompleteTextView password;

    private Button login;
    private Button cancel;
    private Button register;
    private DBHandler handler;

    /**
     * Perform initialization of all fragments and loaders.
     *
     * @param savedInstanceState Bundle: If the activity is being re-initialized after previously
     *                           being shut down then this Bundle contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Movie Recommender");
        }
        setSupportActionBar(toolbar);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        password = (AutoCompleteTextView) findViewById(R.id.password);
        username = (AutoCompleteTextView) findViewById(R.id.username);

        // initialize the "Login" button
        login = (Button) findViewById(R.id.loginButton);

        // initialize the "Cancel" button
        cancel = (Button) findViewById(R.id.cancelButton);

        // initialize the "Register" button
        register = (Button) findViewById(R.id.registerButton);
        // the if statement is a safety measure just in case the register button happens to be null
        // could have just asserted that the register button is not null
        if (register != null) {
            register.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), RegisterActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            });
        } else {
            Log.d("Register", "Registration could not be started");
        }
    }

    public void cancel(View v) {
        username.setText(null);
        password.setText(null);
        username.requestFocus();
    }

    public void login(View v) {
        String userText = username.getText().toString();
        String passText = password.getText().toString();

        if (!userText.isEmpty()) {
            if (!passText.isEmpty()) {
                String userDB = userText.replaceAll("\\s", "");
                userDB = userDB.toUpperCase();
                Boolean notInDB = !checkDataBase(userDB);
                if (!notInDB || userText.equals("admin")) {
                    handler = new DBHandler(this, userDB + ".db");
                    String pass = handler.login(userText);
                    if (passText.equals(pass) || userText.equals("admin")) {

                        if (userText.equals("admin") && passText.equals("nasa1234")) {
                            startActivity(new Intent(this, AdminActivity.class));
                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        } else if (passText.equals(pass)) {
                            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("Username", userText);
                            editor.commit();
                            startActivity(new Intent(this, HomeActivity.class));
                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        } else {
                            Snackbar.make(findViewById(android.R.id.content), "Invalid username or password, try again.", Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "Invalid username or password, try again.", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Invalid username or password, try again.", Snackbar.LENGTH_LONG).show();
                }
            } else {
                password.setError("Password cannot be empty. Try again.");
            }
        } else {
            username.setError("Username cannot be empty. Try again.");
        }
    }

    private boolean checkDataBase(String dbName) {
        SQLiteDatabase checkDB = null;
        String dbPath = "//data/data/cs2340.teamnasamovierecommender/databases/" + dbName + ".db";
        try {
            checkDB = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException e) {

        }
        return checkDB != null;
    }

    protected void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void onBackPressed() {
        Log.d("Back", "Can't press back on main activity");
    }


    public void forgotPassword(View view) {
        startActivity(new Intent(this, ForgotPasswordActivity.class));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}
