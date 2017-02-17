package cs2340.teamnasamovierecommender.pojo;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import cs2340.teamnasamovierecommender.R;

/**
 * Created by sai on 4/25/16.
 */
public class UserAdapter extends CursorAdapter {

    public UserAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.user_list, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView username = (TextView) view.findViewById(R.id.username);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView banned = (TextView) view.findViewById(R.id.banned);

        username.setText(cursor.getString(cursor.getColumnIndex(DBHandler.COLUMN_USERNAME)));
        name.setText(cursor.getString(cursor.getColumnIndex(DBHandler.COLUMN_NAME)));
        banned.setText("Banned: " + cursor.getString(cursor.getColumnIndex(DBHandler.COLUMN_BANNED)));
    }
}