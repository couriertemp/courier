package uz.courier.viewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.BindingAdapter;
import android.util.Log;

import com.poovam.pinedittextfield.LinePinField;
import com.poovam.pinedittextfield.PinField;

import org.jetbrains.annotations.NotNull;

import uz.courier.CourierCore;
import uz.courier.api.response.DataResponse;
import uz.courier.models.User;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmViewModel extends ViewModel implements Callback<DataResponse<User>> {

    public String Code = "";

    public String ErrorMessage = "";

    public String Phone = "";

    public String ConfirmSnippet = "Enter the SMS code sent to your phone number to confirm";

    private MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<User> getUserMutableLiveData() {
        if (userMutableLiveData == null) {
            return new MutableLiveData<>();
        }

        return userMutableLiveData;
    }

    public void confirm() {

        Log.d("LOGGED_CONFIRM", Phone + " " + Code);

        CourierCore.service.confirmUser(Phone, Code).enqueue(ConfirmViewModel.this);

    }

    public void setPhone(String phone) {
        this.Phone = phone;
    }

    @Override
    public void onResponse(Call<DataResponse<User>> call, Response<DataResponse<User>> response) {
        Log.d("LOGGEDIN_IS", String.valueOf(response.isSuccessful()));
        if (response.isSuccessful()) {

            DataResponse<User> dataResponse = Objects.requireNonNull(response.body());

            if (dataResponse.isSuccess()) {
                Log.d("LOGGEDIN_USER", dataResponse.getData().getName());

                ErrorMessage = "";
                userMutableLiveData.postValue(dataResponse.getData());
            }

        } else {
            Log.d("LOGGEDIN_ERROR_MESSAGE", response.message());
            ErrorMessage = "Login or password incorrect!";
        }
    }

    @Override
    public void onFailure(Call<DataResponse<User>> call, Throwable t) {
        Log.d("LOGGEDIN", t.getMessage());
    }

    @BindingAdapter("pinfield")
    public void completeListener(LinePinField pinField, Boolean value){
        pinField.setOnTextCompleteListener(s -> {
            Code = s;
            return true;
        });
    }}
