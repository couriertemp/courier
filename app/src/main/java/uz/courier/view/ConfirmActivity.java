package uz.courier.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import uz.courier.CourierCore;
import uz.courier.R;
import uz.courier.databinding.ActivityConfirmBinding;
import com.poovam.pinedittextfield.PinField;

import org.jetbrains.annotations.NotNull;

import uz.courier.interceptors.LoginInterceptor;
import uz.courier.models.User;
import uz.courier.util.Constants;
import uz.courier.util.SharedPrefs;
import uz.courier.viewModel.ConfirmViewModel;

public class ConfirmActivity extends AppCompatActivity {

    private ConfirmViewModel confirmViewModel;

    private ActivityConfirmBinding binding;

    private String phone;

    private PinField confirmCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        phone = getIntent().getStringExtra("phone");

        confirmViewModel = ViewModelProviders.of(this).get(ConfirmViewModel.class);

        binding = DataBindingUtil.setContentView(ConfirmActivity.this, R.layout.activity_confirm);

        binding.setLifecycleOwner(this);

        binding.setConfirmViewModel(confirmViewModel);

        confirmViewModel.setPhone(phone);

        confirmCode = findViewById(R.id.confirmCode);

        confirmCode.setOnTextCompleteListener(s -> {
            confirmViewModel.Code = s;
            return true;
        });

        confirmViewModel.getUserMutableLiveData().observe(this, user -> {

            if (user != null) {
                SharedPrefs sharedPrefs = new SharedPrefs(getApplicationContext());

                CourierCore.setToken(user.getToken());
                LoginInterceptor.isLoggedIn = true;
                Log.d("LOGGED_IN", "SET TOKEN " + user.getToken());

                sharedPrefs.putUserCredentials(user.getEmail(), sharedPrefs.getSharedPreferences().getString(Constants.keyPassword, ""), true);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }

        });

    }
}
