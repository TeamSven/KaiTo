package io.github.dnivra26.kaito.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import io.github.dnivra26.kaito.R;
import io.github.dnivra26.kaito.models.FoodItem;
import io.github.dnivra26.kaito.models.VandiReview;

/**
 * Created by ganesshkumar on 30/08/15.
 */
public class ReviewAdapter extends ParseQueryAdapter<VandiReview> {
    public ReviewAdapter(Context context, final String vandiId) {
        super(context, new ParseQueryAdapter.QueryFactory<VandiReview>() {
            public ParseQuery<VandiReview> create() {
                ParseQuery query = new ParseQuery("VandiReview");
                query.whereEqualTo("vandiId", vandiId);
                return query;
            }
        });
    }

    @Override
    public View getItemView(VandiReview review, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.vandi_review_row, null);
        }
        super.getItemView(review, v, parent);

        // Setting name
        TextView username = (TextView) v.findViewById(R.id.review_user);
        username.setText(review.getUser().getUsername());

        // Setting review
        TextView reviewText = (TextView) v.findViewById(R.id.review_text);
        reviewText.setText(String.valueOf(review.getReview()));

        return v;
    }
}

