package cs2340.teamnasamovierecommender.pojo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import cs2340.teamnasamovierecommender.R;

/**
 * Created by sai on 4/22/16.
 */
public class CastAdapter extends ArrayAdapter<HashMap<String, String>> {
    public CastAdapter(Context context, ArrayList<HashMap<String, String>> cast) {
        super(context, R.layout.cast_list, cast);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.cast_list, parent, false);

        TextView character = (TextView) customView.findViewById(R.id.character);
        TextView actor = (TextView) customView.findViewById(R.id.actor);

        try {
            HashMap cast = getItem(position);

            Object[] characters = cast.keySet().toArray();

            if (characters[0].toString() != null && characters[0].toString().length() > 0) {
                character.setText(characters[0].toString());
            } else {
                character.setText("???");
                character.setVisibility(View.GONE);
            }

            if (cast.get(characters[0]) != null && ((String) cast.get(characters[0])).length() > 0) {
                actor.setText((String) cast.get(characters[0]));
            } else {
                actor.setText("???");
            }
            actor.setText((String) cast.get(characters[0]));
        } catch (IndexOutOfBoundsException e) {
            character.setText("???");
            actor.setText("???");
        }

        return customView;
    }

    public int getCount() {
        return 3;
    }
}
