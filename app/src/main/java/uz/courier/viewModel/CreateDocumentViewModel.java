package uz.courier.viewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import uz.courier.CourierCore;
import uz.courier.api.response.DataResponse;
import uz.courier.models.Document;

import java.io.File;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateDocumentViewModel extends ViewModel implements Callback<DataResponse<Document>> {

    public String PageTitle = "Create Document";
    public MutableLiveData<String> Name = new MutableLiveData<>();

    private MutableLiveData<Document> documentsMutableLiveData;

    public MutableLiveData<Document> getMutableDocument() {
        if (documentsMutableLiveData == null) {
            documentsMutableLiveData = new MutableLiveData<>();
        }

        return documentsMutableLiveData;
    }

    public void createDocument(File file, String type) {


        RequestBody requestFile = RequestBody.create(
                MediaType.parse(type),
                file
        );

        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        CourierCore.service.createDocument(Name.getValue(), 1, body).enqueue(this);
    }

    @Override
    public void onResponse(Call<DataResponse<Document>> call, Response<DataResponse<Document>> response) {
        if (response.isSuccessful()) {
            documentsMutableLiveData.postValue(Objects.requireNonNull(response.body()).getData());
        }
    }

    @Override
    public void onFailure(Call<DataResponse<Document>> call, Throwable t) {

    }
}
