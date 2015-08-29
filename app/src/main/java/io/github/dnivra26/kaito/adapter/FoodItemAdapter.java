package io.github.dnivra26.kaito.adapter;

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

import io.github.dnivra26.kaito.R;
import io.github.dnivra26.kaito.models.FoodItem;
import io.github.dnivra26.kaito.models.Vandi;

/**
 * Created by ganesshkumar on 30/08/15.
 */
public class FoodItemAdapter extends ParseQueryAdapter<FoodItem> {
    public FoodItemAdapter(Context context, final String vandiId) {
        super(context, new ParseQueryAdapter.QueryFactory<FoodItem>() {
            public ParseQuery<FoodItem> create() {
                ParseQuery query = new ParseQuery("FoodItem");
                query.whereEqualTo("vandiId", vandiId);
                return query;
            }
        });
    }

    @Override
    public View getItemView(FoodItem item, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.kadai_food_row, null);
        }
        super.getItemView(item, v, parent);

        // Setting name
        TextView itemName = (TextView) v.findViewById(R.id.item_name);
        itemName.setText(item.getName());

        // Setting price
        TextView itemPrice = (TextView) v.findViewById(R.id.item_price);
        itemName.setText(String.valueOf(item.getPrice()));

        // Star Rating
        RatingBar itemRating = (RatingBar) v.findViewById(R.id.item_rating);
        itemRating.setRating((float)item.getTotalRating()/item.getNumberOfRating());

        return v;
    }
}
