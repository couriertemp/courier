package uz.courier.viewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import uz.courier.CourierCore;
import uz.courier.api.response.DataResponse;
import uz.courier.models.Confirmation;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uz.courier.models.Country;
import uz.courier.models.Region;

public class SignupViewModel extends ViewModel implements Callback<DataResponse<Confirmation>> {

    public MutableLiveData<String> Email = new MutableLiveData<>();
    public MutableLiveData<String> Password = new MutableLiveData<>();
    public MutableLiveData<String> Firstname = new MutableLiveData<>();
    public MutableLiveData<String> Lastname = new MutableLiveData<>();
    public MutableLiveData<String> Phonenumber = new MutableLiveData<>();
    public MutableLiveData<uz.courier.models.Region> Region = new MutableLiveData<>();
    public MutableLiveData<uz.courier.models.Country> Country = new MutableLiveData<>();

    public String WelcomeText = "Welcome";
    public String WelcomeSnippetText = "Sign in to continue";
    public String EmailLabel = "Email";
    public String PasswordLabel = "Password";
    public String FirstnameLabel = "Firstname";
    public String LastnameLabel = "Lastname";
    public String PhonenumberLabel = "Phone number";
    public String SmsInformationText = "Enter the phone number you are using and we will send you an SMS code";
    public String RegionLabel = "Region";
    public String CountryLabel = "Country";
    public String LoginLabelText = "Have an account?";
    public String LoginLinkText = "Sign in";
    public String SocialSignText = "Sign in with";

    private MutableLiveData<Confirmation> confirmMutableLiveData;

    private int CountryId;
    private int RegionId;

    public MutableLiveData<Confirmation> getConfirmation() {
        if (confirmMutableLiveData == null) {
            confirmMutableLiveData = new MutableLiveData<>();
        }

        return confirmMutableLiveData;
    }

    public void signup() {
        CourierCore.service.createUser(
                Email.getValue(),
                Phonenumber.getValue(),
                Password.getValue(),
                Password.getValue(),
                Firstname.getValue(),
                Lastname.getValue(),
                CountryId,
                RegionId
//                Country.getValue().getId(),
//                Region.getValue().getId()
        ).enqueue(this);
    }

    @Override
    public void onResponse(Call<DataResponse<Confirmation>> call, Response<DataResponse<Confirmation>> response) {
        Log.d("LOGGEDIN", response.message());

        if (response.isSuccessful()) {
            confirmMutableLiveData.setValue(Objects.requireNonNull(response.body()).getData());
        }

    }

    @Override
    public void onFailure(Call<DataResponse<Confirmation>> call, Throwable t) {
        Log.d("LOGGEDIN", t.getMessage());
    }

    private void setEmail(String email) {
        this.Email.setValue(email);
    }

    public void setCountryId(int country_id) {
        this.CountryId = country_id;
    }

    public void setRegionId(int region_id) {
        this.RegionId = region_id;
    }
}
