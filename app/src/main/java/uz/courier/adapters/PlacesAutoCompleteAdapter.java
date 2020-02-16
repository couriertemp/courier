package uz.courier.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.maps.PlacesApi;

import java.util.ArrayList;
import java.util.List;

import uz.courier.R;
import uz.courier.models.Place;

public class PlacesAutoCompleteAdapter extends ArrayAdapter {
    public static final String TAG = "PlacesAutoCompAdapter";
    private List<Place> dataList;
    private Context mContext;
    private PlacesClient geoDataClient;

    private PlacesAutoCompleteAdapter.PlacesAutoCompleteFilter listFilter =
            new PlacesAutoCompleteAdapter.PlacesAutoCompleteFilter();

    public PlacesAutoCompleteAdapter(Context context) {
        super(context, android.R.layout.simple_dropdown_item_1line,
                new ArrayList<Place>());
        mContext = context;


        Places.initialize(mContext, mContext.getString(R.string.google_maps_key));

        geoDataClient = Places.createClient(mContext);

    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Place getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_dropdown_item_1line,
                            parent, false);
        }

        TextView textOne = view.findViewById(android.R.id.text1);
        textOne.setText(dataList.get(position).getPlaceText());

        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return listFilter;
    }

    public class PlacesAutoCompleteFilter extends Filter {
        private Object lock = new Object();
        private Object lockTwo = new Object();
        private boolean placeResults = false;


        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            placeResults = false;
            final List<Place> placesList = new ArrayList<>();

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    results.values = new ArrayList<Place>();
                    results.count = 0;
                }
            } else {
                final String searchStrLowerCase = prefix.toString().toLowerCase();

                Task<FindAutocompletePredictionsResponse> task
                        = getAutoCompletePlaces(searchStrLowerCase);

                task.addOnCompleteListener(new OnCompleteListener<FindAutocompletePredictionsResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<FindAutocompletePredictionsResponse> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Auto complete prediction successful");
                            FindAutocompletePredictionsResponse predictions = task.getResult();
                            Place autoPlace;
                            for (AutocompletePrediction prediction : predictions.getAutocompletePredictions()) {
                                autoPlace = new Place();
                                autoPlace.setPlaceId(prediction.getPlaceId());
                                autoPlace.setPlaceText(prediction.getFullText(null).toString());
                                placesList.add(autoPlace);
                            }
                            Log.d(TAG, "Auto complete predictions size " + placesList.size());
                        } else {
                            Log.d(TAG, "Auto complete prediction unsuccessful");
                        }
                        //inform waiting thread about api call completion
                        placeResults = true;
                        synchronized (lockTwo) {
                            lockTwo.notifyAll();
                        }
                    }
                });

                //wait for the results from asynchronous API call
                while (!placeResults) {
                    synchronized (lockTwo) {
                        try {
                            lockTwo.wait();
                        } catch (InterruptedException e) {

                        }
                    }
                }
                results.values = placesList;
                results.count = placesList.size();
                Log.d(TAG, "Autocomplete predictions size after wait" + results.count);
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                dataList = (ArrayList<Place>) results.values;
            } else {
                dataList = null;
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

        private Task<FindAutocompletePredictionsResponse> getAutoCompletePlaces(String query) {
            //create autocomplete filter using data from filter Views

            Task<FindAutocompletePredictionsResponse> results =
                    geoDataClient.findAutocompletePredictions(FindAutocompletePredictionsRequest.builder().setQuery(query).setTypeFilter(TypeFilter.CITIES).build());
            return results;
        }
    }
}