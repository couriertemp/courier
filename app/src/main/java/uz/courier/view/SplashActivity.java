package uz.courier.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import uz.courier.R;

import uz.courier.util.SharedPrefs;

public class SplashActivity extends AppCompatActivity {

    String FIRST_OPEN_KEY = "APP_FIRST_OPEN";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        Intent startAction;

        SharedPrefs sharedPrefs = new SharedPrefs(getApplicationContext());

        if (sharedPrefs.getSharedPreferences().contains(FIRST_OPEN_KEY)) {
            if (!sharedPrefs.isLoggedIn()) {
                startAction = new Intent(this, LoginActivity.class);
                startActivity(startAction);
                finish();
            } else {
                startAction = new Intent(this, MainActivity.class);
                startActivity(startAction);
                finish();
            }
        } else {
            startAction = new Intent(this, IntroActivity.class);
            startActivity(startAction);
            finish();
        }

    }
}
