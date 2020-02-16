package uz.courier.viewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.BindingAdapter;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import uz.courier.CourierCore;
import uz.courier.api.response.DataResponse;
import uz.courier.models.User;

import java.io.File;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewModel extends ViewModel implements Callback<DataResponse<User>> {

    public String PageTitle = "Profile";
    public String ButtonPersonalInfoText = "Personal info";
    public String ButtonTransportsText = "Transports";
    public String ButtonDocumentsText = "Documents";
    public String ButtonHistoryText = "History";

    private MutableLiveData<User> userMutableLiveData;

    public MutableLiveData<User> getUser() {
        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();
        }

        return userMutableLiveData;
    }

    public void getMe() {
        CourierCore.service.getMe().enqueue(this);
    }

    public void updatePicture(File file, String type) {


        RequestBody requestFile = RequestBody.create(
                MediaType.parse(type),
                file
        );

        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        CourierCore.service.uploadImage(body).enqueue(this);
    }


    @Override
    public void onResponse(Call<DataResponse<User>> call, Response<DataResponse<User>> response) {
        Log.d("LOGGEDIN_IS", String.valueOf(response.isSuccessful()));
        Log.d("LOGGEDIN_IS", String.valueOf(response.raw()));
        if (response.isSuccessful()) {

            DataResponse<User> dataResponse = Objects.requireNonNull(response.body());

            if (dataResponse.isSuccess()) {
                Log.d("LOGGEDIN_USER", dataResponse.getData().getName());

                if (dataResponse.getData().getEmail() != null) {
                    userMutableLiveData.postValue(dataResponse.getData());
                    return;
                }

                CourierCore.service.getMe().enqueue(this);

            }

        } else {
            Log.d("LOGGEDIN_ERROR_MESSAGE", response.message());
            userMutableLiveData.postValue(null);
        }

    }

    @Override
    public void onFailure(Call<DataResponse<User>> call, Throwable t) {

    }

    @BindingAdapter("profileImage")
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl).apply(new RequestOptions().circleCrop())
                .into(view);
    }}
