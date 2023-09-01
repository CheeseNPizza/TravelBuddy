package my.edu.utar.groupassignment.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import my.edu.utar.groupassignment.models.PlaceApi;

import java.net.MalformedURLException;
import java.util.ArrayList;

public class PlaceAutocompleteAdapter extends ArrayAdapter implements Filterable {

    ArrayList<String> results;

    int resource;
    Context context;

    PlaceApi placeApi = new PlaceApi();

    public PlaceAutocompleteAdapter(Context context, int resId){
        super(context,resId);
        this.context = context;
        this.resource = resId;
    }

    @Override
    public int getCount(){
        return results.size();
    }

    @Override
    public String getItem(int pos){
        return results.get(pos);
    }

    public Filter getFilter(){
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                if (charSequence!= null){
                    try {
                        results = placeApi.autoComplete(charSequence.toString());
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }

                    filterResults.values = results;
                    filterResults.count = results.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (filterResults != null && filterResults.count > 0 ){
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
