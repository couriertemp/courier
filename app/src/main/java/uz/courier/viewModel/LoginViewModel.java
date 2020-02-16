package uz.courier.viewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import uz.courier.CourierCore;
import uz.courier.api.response.DataResponse;
import uz.courier.models.User;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel implements Callback<DataResponse<User>> {

    public MutableLiveData<String> Email = new MutableLiveData<>();
    public MutableLiveData<String> Password = new MutableLiveData<>();

    public String WelcomeText = "Welcome";
    public String WelcomeSnippetText = "Sign in to continue";
    public String EmailLabel = "Email";
    public String PasswordLabel = "Password";
    public String ForgotPasswordText = "Forgot Password?";
    public String RegisterLabelText = "New user?";
    public String RegisterLinkText = "Sign up";
    public String SocialSignText = "Sign in with";
    public String ErrorMessage = "";

    private MutableLiveData<User> userMutableLiveData;

    public MutableLiveData<User> getUser() {
        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();
        }

        return userMutableLiveData;
    }

    public void login() {
        CourierCore.service.loginUser(Email.getValue(), Password.getValue()).enqueue(this);
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
            userMutableLiveData.postValue(null);
        }
    }

    @Override
    public void onFailure(Call<DataResponse<User>> call, Throwable t) {
        Log.d("LOGGEDIN", t.getMessage());
    }
}
