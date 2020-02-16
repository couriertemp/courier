package uz.courier.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {

    private String preferencesName = "login";

    private String keyUsername = "login";
    private String keyPassword = "password";
    private String keyLoggedIn = "isLoggedIn";

    private String FIRST_OPEN_KEY = "APP_FIRST_OPEN";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private Context mContext;

    public SharedPrefs(Context context) {
        mContext = context;
        sharedPreferences = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(keyLoggedIn, false);
    }

    public void putUserCredentials(String email, String password, boolean loggedIn) {
        editor = sharedPreferences.edit();
        editor.putString(keyUsername, email);
        editor.putString(keyPassword, password);
        editor.putBoolean(keyLoggedIn, true);
        editor.commit();
    }

    public void putFirstOpen(String not_first) {
        editor = sharedPreferences.edit();
        editor.putString(FIRST_OPEN_KEY, not_first);
        editor.commit();
    }
}
