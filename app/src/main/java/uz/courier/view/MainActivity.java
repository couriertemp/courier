package uz.courier.view;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uz.courier.CourierCore;
import uz.courier.R;

import uz.courier.adapters.PlacesAutoCompleteAdapter;
import uz.courier.api.response.DataResponse;
import uz.courier.dialogs.LogoutDialog;
import uz.courier.models.Package;
import uz.courier.models.Place;
import uz.courier.models.User;
import uz.courier.util.LatLngInterpolator;
import uz.courier.util.MarkerAnimation;
import uz.courier.viewModel.ProfileViewModel;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.Distance;
import com.google.maps.model.DistanceMatrix;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, LocationListener {

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GoogleMap mGoogleMap;
    private LocationCallback locationCallback;

    private ImageButton navClose;
    private DrawerLayout drawer;
    private ConstraintLayout goProfile;

    private ImageView avatar;
    private TextView name;
    private TextView rating;

    private ProfileViewModel profileViewModel;

    private Marker marker;

    private RelativeLayout logout;

    private FrameLayout mainFramelayout;
    private LayoutInflater inflater;
    private View topBarMain;
    private Boolean holderShow = true;

    private AutoCompleteTextView packageFrom;
    private AutoCompleteTextView packageTo;

    private AutoCompleteTextView packageFromInfo;
    private AutoCompleteTextView packageToInfo;

    private Place placeFrom;
    private Place placeTo;
    private Distance distance;

    private String description;
    private int weight;

    private boolean cameraMoved = false;

    private GeoApiContext mGeoApiContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainFramelayout = findViewById(R.id.mainFrameLayout);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        navClose = navigationView.getHeaderView(0).findViewById(R.id.nav_close);
        navClose.setOnClickListener(view -> drawer.closeDrawer(GravityCompat.START));

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    getLastKnownLocation(location);
                }
            }
        };

        avatar = navigationView.getHeaderView(0).findViewById(R.id.avatar);
        name = navigationView.getHeaderView(0).findViewById(R.id.nav_name);
        goProfile = navigationView.getHeaderView(0).findViewById(R.id.go_to_profile);
        ImageButton currentLocationButton = findViewById(R.id.currentLocationButton);

        logout = findViewById(R.id.logout);

        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

        profileViewModel.getMe();
        profileViewModel.getUser().observe(this, user -> {
            if (user != null) {
                name.setText(user.getName());

                if (user.getImage() != null)
                    Glide.with(getApplicationContext())
                            .load(user.getImage().getIcon())
                            .apply(new RequestOptions().circleCrop())
                            .into(avatar);
            }
        });

        goProfile.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(intent);
        });

        logout.setOnClickListener(view -> {
            LogoutDialog logoutDialog = new LogoutDialog(MainActivity.this);
            logoutDialog.show();
            logoutDialog.setOwnerActivity(MainActivity.this);
        });

        currentLocationButton.setOnClickListener(view -> {
            if (marker != null)
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
        });

        if (mGeoApiContext == null) {
            mGeoApiContext = new GeoApiContext.Builder()
                    .apiKey(getString(R.string.google_maps_key))
                    .build();
        }

    }

    @Override
    public void onBackPressed() {
        drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

//        switch (id) {
//            case R.id.nav_camera:
//                // Handle
//                break;
//            default:
//                break;
//        }
//

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getLastKnownLocation(Location location) {
        if (location != null && mGoogleMap != null) {

            if (marker == null) {
                mGoogleMap.clear();

                if (!cameraMoved) {
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                    cameraMoved = true;
                }

                marker = mGoogleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(location.getLatitude(), location.getLongitude()))
                        .title("Marker")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.maps_blue_dot))
                        .anchor(0.5f, 0.5f));

                mGoogleMap.getProjection().toScreenLocation(new LatLng(location.getLatitude(), location.getLongitude()));
            } else {
                MarkerAnimation.animateMarkerToICS(marker, new LatLng(location.getLatitude(), location.getLongitude()), new LatLngInterpolator.Spherical());
            }

        }
    }

    private void startLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(500);
        mFusedLocationProviderClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

            Log.d("LOCATION", "Denied");

            return;
        }

        mGoogleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle_graysacle));

        mGoogleMap.setMyLocationEnabled(false);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
        mGoogleMap.getUiSettings().setCompassEnabled(false);

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.311081, 69.240562), 14));

        startLocationUpdates();

        inflateMainTabBar();

    }

    public void inflateMainTabBar() {

        topBarMain = inflater.inflate(R.layout.app_bar_main, mainFramelayout, false);

        mainFramelayout.removeView(mainFramelayout);
        mainFramelayout.addView(topBarMain);

        Button searchCarsButton = findViewById(R.id.searchCars);
        searchCarsButton.setOnClickListener(view -> {

            DirectionsApiRequest directions = new DirectionsApiRequest(mGeoApiContext);

            directions.alternatives(false);
            directions.originPlaceId(placeFrom.getPlaceId());

            mGoogleMap.clear();

            try {
                com.google.maps.model.LatLng placeFromLatLng = Objects.requireNonNull(PlacesApi.placeDetails(mGeoApiContext, placeFrom.getPlaceId()).await()).geometry.location;
                com.google.maps.model.LatLng placeToLatLng = Objects.requireNonNull(PlacesApi.placeDetails(mGeoApiContext, placeTo.getPlaceId()).await()).geometry.location;

                Marker fromMarker = mGoogleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(
                                placeFromLatLng.lat,
                                placeFromLatLng.lng
                        ))
                        .title("From here")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.package_drop))
                        .anchor(0.5f, 0.5f));

                Marker toMarker = mGoogleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(
                                placeToLatLng.lat,
                                placeToLatLng.lng
                        ))
                        .title("To here")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker))
                        .anchor(0.5f, 0.5f));


                directions.destination(
                        placeToLatLng
                );

                directions.setCallback(new PendingResult.Callback<DirectionsResult>() {
                    @Override
                    public void onResult(DirectionsResult result) {
                        addPolylinesToMap(result);
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        Log.d("ROUTE", "Error! " + e.getMessage());
                    }
                });

            } catch (ApiException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            inflatePackageCreateTabBar();
        });

        mGoogleMap.setOnMapClickListener(latLng -> {
            if (holderShow) {
                mainFramelayout.removeView(topBarMain);
                holderShow = false;
            } else {
                mainFramelayout.addView(topBarMain);
                holderShow = true;
            }
        });

        packageFrom = findViewById(R.id.packageFrom);
        packageTo = findViewById(R.id.packageTo);

        PlacesAutoCompleteAdapter adapter = new PlacesAutoCompleteAdapter(MainActivity.this);
        packageFrom.setAdapter(adapter);
        packageTo.setAdapter(adapter);

        packageFrom.setOnItemClickListener((adapterView, view, position, l) -> {
            placeFrom = (Place) adapterView.getItemAtPosition(position);
        });

        packageTo.setOnItemClickListener((adapterView, view, position, l) -> {
            placeTo = (Place) adapterView.getItemAtPosition(position);

        });


    }

    public void inflatePackageCreateTabBar() {

        mainFramelayout.removeView(topBarMain);
        topBarMain = inflater.inflate(R.layout.app_bar_package_create, mainFramelayout, false);

        mainFramelayout.removeView(mainFramelayout);
        mainFramelayout.addView(topBarMain);

        Button addInfoButton = findViewById(R.id.packageAddInfoButton);
        Button savePackageInfoButton = findViewById(R.id.savePackageButton);

        final ConstraintLayout detailsDecoration = findViewById(R.id.packageDecorateDetails);
        final ConstraintLayout details = findViewById(R.id.packageInfo);
        final ScrollView carTypes = findViewById(R.id.packageCarTypes);
        final EditText descriptionView = findViewById(R.id.packageDescription);
        final EditText weightView = findViewById(R.id.packageWeight);

        addInfoButton.setOnClickListener(view -> {
            detailsDecoration.setVisibility(View.INVISIBLE);
            carTypes.setVisibility(View.VISIBLE);
            details.setVisibility(View.VISIBLE);
            carTypes.setVisibility(View.VISIBLE);
        });

        savePackageInfoButton.setOnClickListener(view -> {

            try {
                DistanceMatrix distanceMatrix = Objects.requireNonNull(DistanceMatrixApi
                        .getDistanceMatrix(
                                mGeoApiContext,
                                new String[]{"place_id:" + placeFrom.getPlaceId()},
                                new String[]{"place_id:" + placeTo.getPlaceId()})
                        .await());

                distance = Objects.requireNonNull(distanceMatrix).rows[0].elements[0].distance;

                com.google.maps.model.LatLng placeFromLatLng = Objects.requireNonNull(PlacesApi.placeDetails(mGeoApiContext, placeFrom.getPlaceId()).await()).geometry.location;
                com.google.maps.model.LatLng placeToLatLng = Objects.requireNonNull(PlacesApi.placeDetails(mGeoApiContext, placeTo.getPlaceId()).await()).geometry.location;

                CourierCore.service.createPkg(
                        placeFrom.getPlaceText(),
                        placeTo.getPlaceText(),
                        1,
                        (int) distance.inMeters,
                        1,
                        (float) placeFromLatLng.lat,
                        (float) placeFromLatLng.lng,
                        (float) placeToLatLng.lat,
                        (float) placeToLatLng.lng,
                        (int) System.currentTimeMillis() / 1000,
                        descriptionView.getText().toString(),
                        Integer.parseInt(weightView.getText().toString())
                ).enqueue(new Callback<DataResponse<Package>>() {
                    @Override
                    public void onResponse(Call<DataResponse<Package>> call, Response<DataResponse<Package>> response) {
                        mainFramelayout.removeView(topBarMain);
                        topBarMain = inflater.inflate(R.layout.app_bar_main, mainFramelayout, false);
                        mGoogleMap.clear();

                    }

                    @Override
                    public void onFailure(Call<DataResponse<Package>> call, Throwable t) {
                        Log.d("CREATE_PACKAGE", "Error! " + t.getMessage());
                    }
                });
            } catch (ApiException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            detailsDecoration.setVisibility(View.VISIBLE);
            details.setVisibility(View.INVISIBLE);
            carTypes.setVisibility(View.INVISIBLE);
        });


        mGoogleMap.setOnMapClickListener(latLng -> {
            if (holderShow) {
                mainFramelayout.removeView(topBarMain);
                holderShow = false;
            } else {
                mainFramelayout.addView(topBarMain);
                holderShow = true;
            }
        });

        packageFromInfo = findViewById(R.id.packageFromInfo);
        packageToInfo = findViewById(R.id.packageToInfo);

        packageFromInfo.setText(placeFrom.getPlaceText());
        packageToInfo.setText(placeTo.getPlaceText());

        PlacesAutoCompleteAdapter adapter = new PlacesAutoCompleteAdapter(MainActivity.this);
        packageFromInfo.setAdapter(adapter);
        packageToInfo.setAdapter(adapter);

        packageFromInfo.setOnItemClickListener((adapterView, view, position, l) -> {
            placeFrom = ((Place) adapterView.getItemAtPosition(position));
        });

        packageToInfo.setOnItemClickListener((adapterView, view, position, l) -> {
            placeTo = ((Place) adapterView.getItemAtPosition(position));

        });

    }

    @Override
    public void onLocationChanged(Location location) {
        getLastKnownLocation(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
//        getLastKnownLocation();
        Log.d("Location", "status changed " + s);
    }

    @Override
    public void onProviderEnabled(String s) {
//        getLastKnownLocation();
        Log.d("Location", "provider enabled " + s);
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.d("LOGGED", "Location provider is disabled");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (mGoogleMap != null) {
            mGoogleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle_graysacle));

            mGoogleMap.setMyLocationEnabled(false);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
            mGoogleMap.getUiSettings().setCompassEnabled(false);
        }

    }

    private void addPolylinesToMap(final DirectionsResult result) {
        new Handler(Looper.getMainLooper()).post(() -> {
            Log.d("ROUTE", "run: result routes: " + result.routes.length);

            for (DirectionsRoute route : result.routes) {
                Log.d("ROUTE", "run: leg: " + route.legs[0].toString());
                List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());

                List<LatLng> newDecodedPath = new ArrayList<>();

                for (com.google.maps.model.LatLng latLng : decodedPath) {

                    newDecodedPath.add(new LatLng(
                            latLng.lat,
                            latLng.lng
                    ));
                }
                Polyline polyline = mGoogleMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                polyline.setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                polyline.setClickable(true);

            }
        });
    }
}
