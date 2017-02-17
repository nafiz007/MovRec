package cs2340.teamnasamovierecommender.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

import cs2340.teamnasamovierecommender.R;
import cs2340.teamnasamovierecommender.pojo.DBHandler;
import cs2340.teamnasamovierecommender.pojo.UserAdapter;

/**
 * Created by sai on 4/25/16.
 */
public class AdminActivity extends AppCompatActivity {
    static DBHandler adminHandler;

    public static ArrayList<String> getUsers() {
        Cursor c = adminHandler.rawQuery("select * from info");

        ArrayList<String> users = new ArrayList<String>();

        if (c.moveToFirst()) {
            while (c.isAfterLast() == false) {
                String name = c.getString(c.getColumnIndex(DBHandler.COLUMN_USERNAME));
                users.add(name);
                Log.d("User", name);
                c.moveToNext();
            }
        }

        return users;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Admin - Users");
        }
        setSupportActionBar(toolbar);

        adminHandler = new DBHandler(this, "admin.db");

        Cursor c = adminHandler.rawQuery("select rowid _id, * from info");

        UserAdapter userAdapter = new UserAdapter(this, c);

        ListView userListView = (ListView) findViewById(R.id.usersList);
        userListView.setAdapter(userAdapter);
    }

    public void onBackPressed() {

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();
        if (id == R.id.action_logout) {
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        }

        if (id == R.id.action_account) {
            startActivity(new Intent(this, AccountActivity.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }
        return id == R.id.action_account || super.onOptionsItemSelected(item);
    }
}
