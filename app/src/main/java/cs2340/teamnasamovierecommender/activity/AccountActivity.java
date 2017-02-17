package cs2340.teamnasamovierecommender.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import cs2340.teamnasamovierecommender.R;
import cs2340.teamnasamovierecommender.pojo.DBHandler;

/**
 * Created by sai on 4/23/16.
 */
public class AccountActivity extends AppCompatActivity {

    private DBHandler handler;
    private AutoCompleteTextView nameTextView;
    private AutoCompleteTextView emailTextView;
    private Spinner majorSpinner;

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Account");
        }
        setSupportActionBar(toolbar);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String username = sharedPreferences.getString("Username", "userDefault");

        handler = new DBHandler(this, username.toUpperCase() + ".db");

        String name = handler.getName();
        String major = handler.getMajor();
        String email = handler.getEmail();


        majorSpinner = (Spinner) findViewById(R.id.majorSpinner);
        nameTextView = (AutoCompleteTextView) findViewById(R.id.name);
        emailTextView = (AutoCompleteTextView) findViewById(R.id.email);
        nameTextView.setText(name);
        emailTextView.setText(email);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.majors_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        majorSpinner.setAdapter(adapter);

        majorSpinner.setSelection(getMajorIndex(majorSpinner, major));


    }

    private int getMajorIndex(Spinner spinner, String toGet) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(toGet)) {
                index = i;
                break;
            }
        }

        return index;
    }


    public void updateInfo(View view) {
        String name = nameTextView.getText().toString();
        String email = emailTextView.getText().toString();
        String major = majorSpinner.getSelectedItem().toString();

        handler.updateInfo(name, major, email);

        DBHandler adminDb = new DBHandler(this, "admin.db");
        adminDb.updateInfo(name, major, email);

        Toast.makeText(getApplicationContext(), "Saved changes.", Toast.LENGTH_SHORT).show();
    }

    public void toChangePassword(View view) {
        startActivity(new Intent(this, ChangePasswordActivity.class));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
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
