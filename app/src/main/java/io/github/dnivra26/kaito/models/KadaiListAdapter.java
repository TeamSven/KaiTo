package io.github.dnivra26.kaito.models;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import io.github.dnivra26.kaito.R;

/**
 * Created by ganesshkumar on 29/08/15.
 */
public class KadaiListAdapter extends ParseQueryAdapter<Vandi> {

    public KadaiListAdapter(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<Vandi>() {
            public ParseQuery<Vandi> create() {
                ParseQuery query = new ParseQuery("Vandi");
                return query;
            }
        });
    }

    @Override
    public View getItemView(Vandi vandi, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.vandi_row, null);
        }
        super.getItemView(vandi, v, parent);

        // Setting Image
        ParseImageView vandiImage = (ParseImageView) v.findViewById(R.id.vandi_image);
        ParseFile photoFile = vandi.getParseFile("photo");
        if (photoFile != null) {
            vandiImage.setParseFile(photoFile);
            vandiImage.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    // nothing to do
                }
            });
        }

        // Setting name
        TextView vandiName = (TextView) v.findViewById(R.id.vandi_name);
        vandiName.setText(vandi.getName());

        // Setting location
        TextView vandiLocation = (TextView) v
                .findViewById(R.id.vandi_location);
        vandiLocation.setText(vandi.getLocation().toString());

        // Star Rating
        RatingBar vandiRating = (RatingBar) v.findViewById(R.id.vandi_rating);
        vandiRating.setRating((float)vandi.getAvgRating());
        
        return v;
    }
}
