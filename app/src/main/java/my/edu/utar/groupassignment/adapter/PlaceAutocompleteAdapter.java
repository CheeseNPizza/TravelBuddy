/**
 * PlaceAutocompleteAdapter is an ArrayAdapter used for auto-suggesting place names
 * based on user input. It utilizes Google Places AutoComplete API to fetch place suggestions.
 *
 * @author Lee Teck Junn
 * @version 1.0
 */
package my.edu.utar.groupassignment.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import my.edu.utar.groupassignment.models.PlaceApi;

import java.net.MalformedURLException;
import java.util.ArrayList;

public class PlaceAutocompleteAdapter extends ArrayAdapter implements Filterable {

    // ArrayList to store the auto-suggested place names
    ArrayList<String> results;

    // Resource ID for the adapter
    int resource;
    Context context;

    // Instance of the PlaceApi class for fetching place suggestions
    PlaceApi placeApi = new PlaceApi();

    // Constructor for PlaceAutocompleteAdapter.
    public PlaceAutocompleteAdapter(Context context, int resId){
        super(context,resId);
        this.context = context;
        this.resource = resId;
    }

    // Get the count of auto-suggested place names.
    @Override
    public int getCount(){
        return results.size();
    }

    // Get a specific auto-suggested place name at the given position.
    @Override
    public String getItem(int pos){
        return results.get(pos);
    }

    // Get a Filter object that handles auto-suggestions based on user input.
    public Filter getFilter(){
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                if (charSequence!= null){
                    try {
                        // Use the PlaceApi to fetch auto-suggestions based on user input
                        results = placeApi.autoComplete(charSequence.toString());
                    } catch (MalformedURLException e) {
                        // Handle any URL-related exceptions by throwing a runtime exception
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
                    // Notify the adapter that data has changed, and auto-suggestions should be displayed
                    notifyDataSetChanged();
                }
                else {
                    // Notify the adapter that the data set is invalid, and no suggestions should be displayed
                    notifyDataSetInvalidated();
                }
            }
        };

        return filter;
    }

}
