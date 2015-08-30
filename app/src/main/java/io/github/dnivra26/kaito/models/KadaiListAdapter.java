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

import java.io.IOException;
import java.util.List;

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
        String address = convertToAddress(vandi.getLocation());
        TextView vandiLocation = (TextView) v
                .findViewById(R.id.vandi_location);
        //vandiLocation.setText(vandi.getLocation().toString());
        vandiLocation.setText(address);

        // Star Rating
        RatingBar vandiRating = (RatingBar) v.findViewById(R.id.vandi_rating);
        LayerDrawable stars = (LayerDrawable) vandiRating.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        vandiRating.setRating((float)vandi.getAvgRating());
        
        return v;
    }
    public String convertToAddress(ParseGeoPoint geoPoint) {
        String address = null;
        Geocoder geocoder;
        geocoder = new Geocoder(getContext());
        String city = null;
        try {
            List<Address> fromLocation = geocoder.getFromLocation(geoPoint.getLatitude(), geoPoint.getLongitude(), 1);
            address = fromLocation.get(0).getAddressLine(0);
            city = fromLocation.get(0).getAddressLine(1);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return address + "," + city;
    }
}
