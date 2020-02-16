package uz.courier.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import uz.courier.R;

import uz.courier.CourierCore;
import uz.courier.models.Transport;

import java.io.IOException;
import java.util.ArrayList;

public class TransportsAdapter extends RecyclerView.Adapter<TransportsAdapter.ViewHolder> {

    private ArrayList<Transport> arrayList;
    private LayoutInflater layoutInflater;
    private Context context;


    public TransportsAdapter(ArrayList<Transport> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater==null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        View view = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.document_item, parent, false);

        context = parent.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transport transport = arrayList.get(position);
        holder.bind(transport);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView documentName;
        private ConstraintLayout listItemMenu;
        private LinearLayout documentActions;
        private TextView deleteDocument;
        private TextView editDocument;

        private boolean actionsVisible = false;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.documentImage);
            this.documentName = itemView.findViewById(R.id.documentItemName);
            this.listItemMenu = itemView.findViewById(R.id.listItemMenu);
            this.documentActions = itemView.findViewById(R.id.documentActions);
            this.deleteDocument = itemView.findViewById(R.id.deleteDocument);
        }

        public void bind(final Transport transport) {
            documentName.setText(transport.getName());
            Glide.with(context)
                    .load(transport.getImage().getLow())
//                    .apply(RequestOptions.circleCropTransform())
                    .into(imageView);
            Log.d("TRANSPORT_IMAGE", transport.getImage().getLow());

            listItemMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    documentActions.setVisibility(actionsVisible ? View.VISIBLE : View.GONE);
                    actionsVisible = !actionsVisible;
                }
            });

            deleteDocument.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        CourierCore.service.deleteTransport(transport.getId()).execute();
                        itemView.setVisibility(View.GONE);
                        arrayList.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


}
