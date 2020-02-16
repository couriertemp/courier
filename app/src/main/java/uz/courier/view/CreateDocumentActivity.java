package uz.courier.view;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import uz.courier.R;
import uz.courier.databinding.ActivityCreateDocumentBinding;
import uz.courier.models.Document;
import uz.courier.util.Constants;
import uz.courier.util.FileUtils;
import uz.courier.viewModel.CreateDocumentViewModel;

import java.io.File;

public class CreateDocumentActivity extends AppCompatActivity {

    private CreateDocumentViewModel createDocumentViewModel;

    private ActivityCreateDocumentBinding binding;

    private Button launchCameraButton;

    private ImageView imageView;

    private FrameLayout cameraIconLayout;

    private Uri imageUri;

    private boolean caputred = false;

    private Context mContext;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_document);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},  1);

        createDocumentViewModel = ViewModelProviders.of(this).get(CreateDocumentViewModel.class);

        mContext = getApplicationContext();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());


        binding = DataBindingUtil.setContentView(CreateDocumentActivity.this, R.layout.activity_create_document);
        binding.setLifecycleOwner(this);
        binding.setCreateDocumentViewModel(createDocumentViewModel);

        launchCameraButton = findViewById(R.id.createDocumentLaunchCamera);
        launchCameraButton.setOnClickListener(view -> {

            if (!caputred) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File photo = new File(Environment.getExternalStorageDirectory(), "document.jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
                imageUri = Uri.fromFile(photo);
                startActivityForResult(intent, Constants.TAKE_PICTURE);
                caputred = true;
            } else {
                File file = FileUtils.getFile(mContext, imageUri);
                Log.d("IMAGE_URI", imageUri.toString());
                createDocumentViewModel.createDocument(file, "image/*");
            }
        });

        imageView = findViewById(R.id.createDocumentImage);
        cameraIconLayout = findViewById(R.id.createDocumentCamera);

        createDocumentViewModel.getMutableDocument().observe(this, document -> finish());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.TAKE_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = imageUri;
                    ContentResolver cr = getContentResolver();
                    Bitmap bitmap;
                    try {
                        bitmap = android.provider.MediaStore.Images.Media
                                .getBitmap(cr, selectedImage);

                        imageView.setImageBitmap(bitmap);
//                        imageView.setVisibility(View.VISIBLE);
                        cameraIconLayout.setBackgroundResource(R.drawable.white_border);
                        cameraIconLayout.setPaddingRelative(0, 0, 0, 0);
                    } catch (Exception e) {
                        Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
                                .show();
                        Log.e("Camera", e.toString());
                    }
                }
        }
    }
}
