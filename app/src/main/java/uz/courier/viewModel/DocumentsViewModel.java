package uz.courier.viewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import uz.courier.CourierCore;
import uz.courier.api.response.DataResponse;
import uz.courier.models.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uz.courier.view.CreateDocumentActivity;

public class DocumentsViewModel extends ViewModel implements Callback<DataResponse<ArrayList<Document>>> {

    public String PageTitle = "Documents";

    private MutableLiveData<ArrayList<Document>> documentsMutableLiveData;

    private ArrayList<Document> documents;

    public MutableLiveData<ArrayList<Document>> getMutableDocuments() {
        if (documentsMutableLiveData == null) {
            documentsMutableLiveData = new MutableLiveData<>();
            this.getDocuments();
        }

        return documentsMutableLiveData;
    }

    public void getDocuments() {
        CourierCore.service.getDocuments().enqueue(this);
    }

    @BindingAdapter({"imageUrl"})
    public static void loadimage(ImageView imageView, String imageUrl){
        Glide.with(imageView.getContext()).load(imageUrl).apply(RequestOptions.circleCropTransform()).into(imageView);
        //Picasso.with(imageView.getContext()).load(imageUrl).into(imageView);
    }

    public void deleteDocument(ConstraintLayout layout, int id) {
        try {
            if (CourierCore.service.deleteDocument(id).execute().isSuccessful()) {
                this.getDocuments();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(Call<DataResponse<ArrayList<Document>>> call, Response<DataResponse<ArrayList<Document>>> response) {
        documentsMutableLiveData.postValue(Objects.requireNonNull(response.body()).getData());
    }

    @Override
    public void onFailure(Call<DataResponse<ArrayList<Document>>> call, Throwable t) {

    }

    public void onClickCreateDocument(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, CreateDocumentActivity.class);
        context.startActivity(intent);
    }


}
