package uz.courier.view;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import uz.courier.R;
import uz.courier.databinding.ActivityProfileBinding;
import uz.courier.util.FileUtils;
import uz.courier.viewModel.ProfileViewModel;

import java.io.File;


public class ProfileActivity extends AppCompatActivity {

    private ProfileViewModel profileViewModel;
    private TextView rating;
    private RatingBar ratingBar;

    private ActivityProfileBinding binding;

    private Button personalInfoButton;
    private Button transportsButton;
    private Button documentsButton;
    private Button historyButton;
    private ImageButton changeAvatar;

    private Toolbar toolbar;

    private static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

        binding = DataBindingUtil.setContentView(ProfileActivity.this, R.layout.activity_profile);
        binding.setLifecycleOwner(this);

        binding.setProfileViewModel(profileViewModel);

        Log.d("LOGGED_IN_PROF_ACTIVITY", "You are here");

        toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        changeAvatar = findViewById(R.id.profile_change_avatar);

        rating = findViewById(R.id.profile_rating_value);
        ratingBar = findViewById(R.id.user_rating_indicator);

        personalInfoButton = findViewById(R.id.personal_info_button);
        transportsButton = findViewById(R.id.transports_button);
        documentsButton = findViewById(R.id.documents_button);
        historyButton = findViewById(R.id.history_button);

        profileViewModel.getMe();
        profileViewModel.getUser().observe(this, user -> {
            if (user != null) {
                binding.setUser(user);
            }
        });

        personalInfoButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), PersonalInfoActivity.class);
            startActivity(intent);
        });

        transportsButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), TransportsActivity.class);
            startActivity(intent);
        });

        documentsButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), DocumentsActivity.class);
            startActivity(intent);
        });

        changeAvatar.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);

            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);


        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        Uri selectedImageUri;

        if (requestCode == PICK_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {

                if ((data != null) && (data.getData() != null)) {
                    selectedImageUri = data.getData();
                    File file = FileUtils.getFile(this, selectedImageUri);
                    profileViewModel.updatePicture(file, getContentResolver().getType(selectedImageUri));
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
