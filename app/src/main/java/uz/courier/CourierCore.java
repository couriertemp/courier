package uz.courier;

import android.content.Context;

import uz.courier.interceptors.LoginInterceptor;
import uz.courier.interceptors.UserAgentInterceptor;
import uz.courier.services.ApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CourierCore {

    public static ApiService service;

    public static LoginInterceptor loginInterceptor;

    public static boolean isLoggedIn;

    public static String token = "";

    static void initialize(Context context, String userAgent) {

        Gson gson = new GsonBuilder()
//                .registerTypeAdapter()
                .create();

        loginInterceptor = new LoginInterceptor(context);

        isLoggedIn = LoginInterceptor.isLoggedIn;

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loginInterceptor)
                .addInterceptor(new UserAgentInterceptor(userAgent))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl("https://api.courier.imac.uz/v1/")
                .build();

        service = retrofit.create(ApiService.class);
    }

    public static void setToken(String _token) {
        token = _token;
    }
}
