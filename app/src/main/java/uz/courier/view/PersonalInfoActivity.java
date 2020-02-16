package uz.courier.view;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.LinearLayout;

import uz.courier.R;
import uz.courier.databinding.ActivityPersonalInfoBinding;
import uz.courier.viewModel.PersonalInfoViewModel;
import uz.courier.viewModel.ProfileViewModel;

public class PersonalInfoActivity extends AppCompatActivity {

    private ProfileViewModel profileViewModel;
    private PersonalInfoViewModel personalInfoViewModel;

    private ActivityPersonalInfoBinding binding;

    private EditText name;
    private EditText country;
    private EditText email;
    private EditText phone;
    private EditText passport;
    private Toolbar toolbar;
    private LinearLayout editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        personalInfoViewModel = ViewModelProviders.of(this).get(PersonalInfoViewModel.class);

        binding = DataBindingUtil.setContentView(PersonalInfoActivity.this, R.layout.activity_personal_info);
        binding.setLifecycleOwner(this);

        binding.setPersonalInfoViewModel(personalInfoViewModel);

        toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        name = findViewById(R.id.personalInfoLastnameValue);
        country = findViewById(R.id.personalInfoCountryValue);
        email = findViewById(R.id.personalInfoEmailValue);
        phone = findViewById(R.id.personalInfoPhonenumberValue);
        passport = findViewById(R.id.personalInfoPassportValue);
        editButton = findViewById(R.id.personalInfoEditButton);

        profileViewModel.getMe();
        profileViewModel.getUser().observe(this, user -> {
            if (user != null) {
                name.setText(user.getName());
                phone.setText(user.getPhone());
                email.setText(user.getEmail());
            }
        });

        editButton.setOnClickListener(view -> {
            name.setEnabled(true);
            name.setFocusable(true);
            country.setEnabled(true);
            country.setFocusable(true);
            email.setEnabled(true);
            email.setFocusable(true);
            phone.setEnabled(true);
            phone.setFocusable(true);
            passport.setEnabled(true);
            passport.setFocusable(true);
        });

    }
}
