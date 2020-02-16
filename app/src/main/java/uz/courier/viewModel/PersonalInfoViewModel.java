package uz.courier.viewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import uz.courier.api.response.DataResponse;
import uz.courier.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalInfoViewModel extends ViewModel implements Callback<DataResponse<User>> {

    public String PageTitle = "Personal Info";
    public String EditButtonLabel = "Edit";

    private MutableLiveData<User> userMutableLiveData;

    @Override
    public void onResponse(Call<DataResponse<User>> call, Response<DataResponse<User>> response) {

    }

    @Override
    public void onFailure(Call<DataResponse<User>> call, Throwable t) {

    }
}
