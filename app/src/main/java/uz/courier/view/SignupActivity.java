package uz.courier.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import uz.courier.R;
import uz.courier.databinding.ActivitySignupBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import retrofit2.Response;
import uz.courier.CourierCore;
import uz.courier.models.Confirmation;
import uz.courier.models.Country;
import uz.courier.models.Region;
import uz.courier.util.SharedPrefs;
import uz.courier.viewModel.SignupViewModel;

public class SignupActivity extends AppCompatActivity {

    private SignupViewModel signupViewModel;

    private ActivitySignupBinding binding;

    private TextView loginLink;

    private AutoCompleteTextView regionsSpinner;
    private AutoCompleteTextView countiesSpinner;

    private ArrayList<Region> regions;
    private ArrayList<Country> countries;

    ArrayAdapter<String> regionsAdapter;
    ArrayAdapter<String> countriesAdapter;

    ArrayList<String> countryNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        signupViewModel = ViewModelProviders.of(this).get(SignupViewModel.class);

        binding = DataBindingUtil.setContentView(SignupActivity.this, R.layout.activity_signup);

        binding.setLifecycleOwner(this);

        binding.setSignupViewModel(signupViewModel);

        signupViewModel.getConfirmation().observe(this, confirmation -> {
            SharedPrefs sharedPrefs = new SharedPrefs(getApplicationContext());
            sharedPrefs.getSharedPreferences().edit().putString("password", signupViewModel.Password.getValue()).commit();

            Intent intent = new Intent(getApplicationContext(), ConfirmActivity.class);
            intent.putExtra("phone", signupViewModel.Email.getValue());
            startActivity(intent);
        });

        loginLink = findViewById(R.id.loginLink);
        loginLink.setOnClickListener(view -> onBackPressed());

        regionsSpinner = findViewById(R.id.region);
        countiesSpinner = findViewById(R.id.country);

        try {
            countries = Objects.requireNonNull(CourierCore.service.getCountries(500).execute().body()).getData();

            countryNames = new ArrayList<>();

            for (Country country : countries) {
                countryNames.add(country.getName());
            }
            countriesAdapter = new ArrayAdapter<>(SignupActivity.this, R.layout.spinner_item, R.id.spinnerName, countryNames);

        } catch (IOException e) {
            e.printStackTrace();
        }

        countiesSpinner.setOnItemClickListener((adapterView, view, position, l) -> {
            try {
                int clickedCountryId = countries.get(countryNames.indexOf(adapterView.getAdapter().getItem(position))).getId();
                signupViewModel.setCountryId(clickedCountryId);

                regions = Objects.requireNonNull(CourierCore.service.getRegions(
                        clickedCountryId,
                        500
                ).execute().body()).getData();

            } catch (IOException e) {
                e.printStackTrace();
            }

            final ArrayList<String> regionNames = new ArrayList<>();

            for (Region region : regions) {
                regionNames.add(region.getName());
            }

            regionsAdapter = new ArrayAdapter<>(SignupActivity.this, R.layout.spinner_item, R.id.spinnerName, regionNames);
            regionsSpinner.setOnItemClickListener((adapterViewRegions, viewRegions, positionRegions, lRegions) -> {
                int clickedRegionId = regions.get(regionNames.indexOf(adapterViewRegions.getAdapter().getItem(positionRegions))).getId();
                signupViewModel.setRegionId(clickedRegionId);
            });
            regionsSpinner.setAdapter(regionsAdapter);
        });

        countiesSpinner.setAdapter(countriesAdapter);

    }
}
