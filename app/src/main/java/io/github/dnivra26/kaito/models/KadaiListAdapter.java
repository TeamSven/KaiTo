package io.github.dnivra26.kaito.models;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import io.github.dnivra26.kaito.R;

/**
 * Created by ganesshkumar on 29/08/15.
 */
public class KadaiListAdapter extends ParseQueryAdapter<Vandi> {

    private ParseGeoPoint currentGeoPoint;
    public KadaiListAdapter(Context context, ParseGeoPoint currentGeoPoint) {

        super(context, new ParseQueryAdapter.QueryFactory<Vandi>() {
            public ParseQuery<Vandi> create() {
                ParseQuery query = new ParseQuery("Vandi");
                return query;
            }
        });
        this.currentGeoPoint = currentGeoPoint;
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
        String[] addressParts = vandi.getAddress().split(",");
        vandiLocation.setText(addressParts[addressParts.length-1]);


        // Distance
        TextView vandiDistance = (TextView) v
                .findViewById(R.id.vandi_distance);
        double distance = vandi.getLocation().distanceInKilometersTo(currentGeoPoint);
        DecimalFormat df = new DecimalFormat("#.##");
        vandiDistance.setText(String.valueOf(df.format(distance)) + " Kms");

        // Star Rating
        //RatingBar vandiRating = (RatingBar) v.findViewById(R.id.vandi_rating);
        //LayerDrawable stars = (LayerDrawable) vandiRating.getProgressDrawable();
        //stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        //vandiRating.setRating((float) vandi.getAvgRating());

        // Number rating
        TextView numberRating = (TextView) v.findViewById(R.id.number_rating);
        numberRating.setText(String.valueOf(vandi.getAvgRating()));
        return v;
    }

}
