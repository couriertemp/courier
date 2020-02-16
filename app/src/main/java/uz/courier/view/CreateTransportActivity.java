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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import uz.courier.CourierCore;
import uz.courier.R;
import uz.courier.api.response.DataResponse;
import uz.courier.databinding.ActivityCreateTransportBinding;

import uz.courier.models.Image;
import uz.courier.models.Region;
import uz.courier.models.Transport;
import uz.courier.models.TransportType;
import uz.courier.util.Constants;
import uz.courier.util.FileUtils;
import uz.courier.viewModel.CreateTransportViewModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Response;

public class CreateTransportActivity extends AppCompatActivity {

    private CreateTransportViewModel createTransportViewModel;

    private ActivityCreateTransportBinding binding;

    private ImageView addPhotoButton;
    private FrameLayout addPhotoContainer;
    private Spinner typeSpinner;
    private Spinner markSpinner;
    private Button cancelButton;
    private Button saveButton;

    private ArrayList<TransportType> transportTypes;

    private ArrayAdapter<String> typesAdapter;

    private Uri imageUri;

    private boolean caputred = false;

    private Context mContext;

    private Toolbar toolbar;

    ArrayList<String> transportTypeNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_transport);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},  1);

        binding = DataBindingUtil.setContentView(CreateTransportActivity.this, R.layout.activity_create_transport);
        binding.setLifecycleOwner(this);
        binding.setCreateTransportViewModel(createTransportViewModel);

        createTransportViewModel = ViewModelProviders.of(this).get(CreateTransportViewModel.class);

        mContext = getApplicationContext();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        typeSpinner = (Spinner) findViewById(R.id.transportType);
        markSpinner = (Spinner) findViewById(R.id.transportMark);

        try {
            transportTypes =  Objects.requireNonNull(CourierCore.service.getTransportTypes().execute().body()).getData();
            transportTypeNames = new ArrayList<>();

            for (TransportType type: transportTypes) {
                transportTypeNames.add(type.getName());
                Log.d("TRANSPORT", type.getName());
            }

            typesAdapter = new ArrayAdapter<String>(CreateTransportActivity.this, R.layout.spinner_item, R.id.spinnerName, transportTypeNames);

        } catch (IOException e) {
            e.printStackTrace();
        }

        typeSpinner.setAdapter(typesAdapter);

//        typeSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                int clickedCountryId = transportTypes.get(transportTypeNames.indexOf(adapterView.getAdapter().getItem(position))).getId();
//                createTransportViewModel.setTypeId(clickedCountryId);
//            }
//        });

        addPhotoButton = (ImageView) findViewById(R.id.addTransportPhoto);
        addPhotoButton.setOnClickListener(view -> {

            if (!caputred) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File photo = new File(Environment.getExternalStorageDirectory(), "transport.jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
                imageUri = Uri.fromFile(photo);
                startActivityForResult(intent, Constants.TAKE_PICTURE);
                caputred = true;
            } else {
                File file = FileUtils.getFile(mContext, imageUri);
                Log.d("IMAGE_URI", imageUri.toString());
                createTransportViewModel.createTransport(file, "image/*");
            }
        });

        createTransportViewModel.getMutableTransport().observe(this, transport -> finish());

        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(view -> {
            File file = FileUtils.getFile(mContext, imageUri);
            Log.d("IMAGE_URI", imageUri.toString());
            createTransportViewModel.createTransport(file, "image/*");
        });

        addPhotoContainer = findViewById(R.id.transportImagesContainer);

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

                        addPhotoButton.setImageBitmap(bitmap);
                        addPhotoButton.setBackground(null);
                        addPhotoContainer.setBackgroundResource(R.drawable.primary_border);
                        addPhotoContainer.setPaddingRelative(0, 0, 0, 0);
//                        imageView.setVisibility(View.VISIBLE);
                        Toast.makeText(this, selectedImage.toString(),
                                Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
                                .show();
                        Log.e("Camera", e.toString());
                    }
                }
        }
    }
}
