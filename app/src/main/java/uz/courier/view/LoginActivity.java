package uz.courier.view;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.util.Objects;

import uz.courier.CourierCore;
import uz.courier.interceptors.LoginInterceptor;
import uz.courier.models.User;
import uz.courier.R;
import uz.courier.util.AsteriskTransformationMethod;
import uz.courier.util.SharedPrefs;
import uz.courier.viewModel.LoginViewModel;
import uz.courier.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        binding = DataBindingUtil.setContentView(LoginActivity.this, R.layout.activity_login);

        binding.setLifecycleOwner(this);

        binding.setLoginViewModel(loginViewModel);

        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 1);

        loginViewModel.getUser().observe(this, user -> {

            if (!loginViewModel.ErrorMessage.equals("")) {
                binding.errorContainer.setVisibility(View.VISIBLE);
                binding.errorMessage.setText(loginViewModel.ErrorMessage);
                binding.password.requestFocus();
            } else if (TextUtils.isEmpty(Objects.requireNonNull(user).getEmail())) {
                binding.emailAddress.setError("Enter an E-Mail Address");
                binding.emailAddress.requestFocus();
            } else if (!user.isEmailValid()) {
                binding.emailAddress.setError("Enter a Valid E-mail Address");
                binding.emailAddress.requestFocus();
            } else {

                SharedPrefs sharedPrefs = new SharedPrefs(getApplicationContext());

                CourierCore.setToken(user.getToken());
                LoginInterceptor.isLoggedIn = true;
                Log.d("LOGGED_IN", "SET TOKEN " + user.getToken());

                sharedPrefs.putUserCredentials(user.getEmail(), loginViewModel.Password.getValue(), true);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }

        });

        binding.password.setTransformationMethod(new AsteriskTransformationMethod());

        binding.signinButton.setOnClickListener(view -> loginViewModel.login());

        binding.registerLink.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
            startActivity(intent);
        });

    }

}
