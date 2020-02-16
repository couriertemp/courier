package uz.courier;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

public class CourierApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        String userAgent = String.format("Courier Android Client %s %s API:%d", Build.BRAND, Build.MODEL, Build.VERSION.SDK_INT);

        CourierCore.initialize(getApplicationContext(), userAgent);
    }
}
