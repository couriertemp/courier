package uz.courier.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import uz.courier.R;

import java.util.ArrayList;

import uz.courier.models.Document;
import uz.courier.models.Region;

public class RegionsAdapter extends ArrayAdapter<Region> {

    private ArrayList<Region> arrayList;
    private Context context;
    private LayoutInflater layoutInflater;

    public RegionsAdapter(@NonNull Context context, ArrayList<Region> list) {
        super(context, R.layout.spinner_item ,list);
        this.arrayList = list;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent,false);

        Region item = arrayList.get(position);

        TextView name = (TextView) listItem.findViewById(R.id.spinnerName);
        name.setText(item.getName());

        return listItem;
    }

}
