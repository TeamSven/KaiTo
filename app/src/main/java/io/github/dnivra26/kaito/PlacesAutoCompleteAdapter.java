package io.github.dnivra26.kaito;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;

/**
 * Created by balasubn on 8/30/15.
 */
public class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {


    ArrayList<String> resultList;

    Context mContext;
    int mResource;
    Placesearch placeApi=new Placesearch();
    public PlacesAutoCompleteAdapter(Context context, int resource) {
        super(context, resource);
        mContext = context;
        mResource = resource;
    }



    @Override
    public int getCount() {
        // Last item will be the footer
        return resultList.size();
    }

    @Override
    public String getItem(int position) {
        return resultList.get(position);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    resultList = placeApi.autocomplete(constraint.toString());

                    filterResults.values = resultList;
                    filterResults.count = resultList.size();
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }
        };

        return filter;
    }



}



