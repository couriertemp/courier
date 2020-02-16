package uz.courier.viewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import uz.courier.CourierCore;
import uz.courier.api.response.DataResponse;
import uz.courier.models.Transport;

import java.io.File;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateTransportViewModel extends ViewModel implements Callback<DataResponse<Transport>> {

    public String PageTitle = "Create Transport";
    public MutableLiveData<String> Name = new MutableLiveData<>();
    public MutableLiveData<String> Number = new MutableLiveData<>();
    public MutableLiveData<String> WeightMin = new MutableLiveData<>();
    public MutableLiveData<String> WeightMax = new MutableLiveData<>();

    private MutableLiveData<Transport> transportMutableLiveData;

    private int TypeId = 1;

    public MutableLiveData<Transport> getMutableTransport() {
        if (transportMutableLiveData == null) {
            transportMutableLiveData = new MutableLiveData<>();
        }

        return transportMutableLiveData;
    }

    public void createTransport(File file, String type) {


        RequestBody requestFile = RequestBody.create(
                MediaType.parse(type),
                file
        );

        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        CourierCore.service.createTransport(
                TypeId,
                1,
                convertStringToRequestBody(Name.getValue()),
                convertStringToRequestBody(Number.getValue()),
                convertStringToRequestBody(WeightMin.getValue()),
                convertStringToRequestBody(WeightMax.getValue()),
                body
        ).enqueue(this);
    }

    @Override
    public void onResponse(Call<DataResponse<Transport>> call, Response<DataResponse<Transport>> response) {
        if (response.isSuccessful()) {
            transportMutableLiveData.postValue(Objects.requireNonNull(response.body()).getData());
        } else {
            Log.d("LOGGED_IN_ERROR", response.message());
        }
    }

    @Override
    public void onFailure(Call<DataResponse<Transport>> call, Throwable t) {
        Log.d("LOGGED_IN_ERROR", t.getMessage());
    }

    public RequestBody convertStringToRequestBody(String plain) {
        return RequestBody.create(MediaType.parse("text/plain"), plain);
    }

    public void setTypeId(int type_id) {
        this.TypeId = type_id;
    }

}
