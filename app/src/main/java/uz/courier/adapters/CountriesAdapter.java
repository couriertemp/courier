package uz.courier.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import uz.courier.R;

import java.util.ArrayList;

import uz.courier.models.Country;
import uz.courier.models.Region;

public class CountriesAdapter extends ArrayAdapter<Country> {

    private ArrayList<Country> arrayList;
    private Context context;
    private LayoutInflater layoutInflater;

    public CountriesAdapter(@NonNull Context context, ArrayList<Country> list) {
        super(context, R.layout.spinner_item ,list);
        this.arrayList = list;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.spinner_item,parent,false);

        Country item = arrayList.get(position);

        TextView name = (TextView) listItem.findViewById(R.id.spinnerName);
        name.setText(item.getName());

        return listItem;
    }

}
