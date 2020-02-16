package uz.courier.interceptors;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import uz.courier.CourierCore;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import uz.courier.util.SharedPrefs;

public class LoginInterceptor implements Interceptor {

    public static boolean isLoggedIn;

    private String preferencesName = "login";

    private String keyUsername = "login";
    private String keyPassword = "password";
    private String keyLoggedIn = "isLoggedIn";
    private SharedPrefs sharedPrefs;

    public LoginInterceptor(Context context) {

        sharedPrefs = new SharedPrefs(context);
        isLoggedIn = sharedPrefs.isLoggedIn();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String httpUrl = request.url().encodedPath();

        if (isLoggedIn) {

            if (!CourierCore.token.isEmpty()) {
                Log.d("LOGGEDIN_TOKEN", CourierCore.token);

                request = request.newBuilder()
                        .removeHeader("Authorization")
                        .addHeader("Authorization", "Bearer " + CourierCore.token)
                        .build();

                Log.d("LOGGEDIN_TOKEN_USED", request.headers().get("Authorization") + " is token");
            } else {
                Log.d("LOGGED_IN", request.url().toString());
                Log.d("LOGGED_IN_PASS", sharedPrefs.getSharedPreferences().getString(keyUsername, "") + " " + sharedPrefs.getSharedPreferences().getString(keyPassword, ""));
                if (!request.url().encodedPath().endsWith("login"))
                    loginIfNeeded();
            }

        }

        return chain.proceed(request);
    }

    public void loginIfNeeded() throws IOException {
        CourierCore.setToken(Objects.requireNonNull(CourierCore.service.loginUser(
                sharedPrefs.getSharedPreferences().getString(keyUsername, ""),
                sharedPrefs.getSharedPreferences().getString(keyPassword, "")
        ).execute().body()).getData().getToken());
    }
}
