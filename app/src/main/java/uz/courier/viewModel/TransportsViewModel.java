package uz.courier.viewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import uz.courier.CourierCore;
import uz.courier.api.response.DataResponse;
import uz.courier.models.Transport;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uz.courier.view.CreateTransportActivity;

public class TransportsViewModel extends ViewModel implements Callback<DataResponse<ArrayList<Transport>>> {

    public String PageTitle = "Transports";

    private MutableLiveData<ArrayList<Transport>> transportsMutableLiveData;

    private ArrayList<Transport> transports;

    public MutableLiveData<ArrayList<Transport>> getMutableTransports() {
        if (transportsMutableLiveData == null) {
            transportsMutableLiveData = new MutableLiveData<>();
            this.getTransports();
        }

        return transportsMutableLiveData;
    }

    public void getTransports() {
        CourierCore.service.getTransports().enqueue(this);
    }

    @BindingAdapter({"imageUrl"})
    public static void loadimage(ImageView imageView, String imageUrl){
        Glide.with(imageView.getContext()).load(imageUrl).apply(RequestOptions.circleCropTransform()).into(imageView);
        //Picasso.with(imageView.getContext()).load(imageUrl).into(imageView);
    }

    @Override
    public void onResponse(Call<DataResponse<ArrayList<Transport>>> call, Response<DataResponse<ArrayList<Transport>>> response) {

        transportsMutableLiveData.postValue(Objects.requireNonNull(response.body()).getData());

    }

    @Override
    public void onFailure(Call<DataResponse<ArrayList<Transport>>> call, Throwable t) {

    }

    @BindingAdapter("transportImage")
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl).apply(new RequestOptions().circleCrop())
                .into(view);
    }

    public void onClickCreateTransport(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, CreateTransportActivity.class);
        context.startActivity(intent);
    }

}
