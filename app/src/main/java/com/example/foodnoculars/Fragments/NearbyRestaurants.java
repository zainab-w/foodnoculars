package com.example.foodnoculars.Fragments;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.foodnoculars.Adapter.GooglePlaceAdapter;
import com.example.foodnoculars.Adapter.PlaceInfoWindowAdapter;
import com.example.foodnoculars.AllConstant;
import com.example.foodnoculars.AppPermissions;
import com.example.foodnoculars.Directions;
import com.example.foodnoculars.GooglePlaceModel;
import com.example.foodnoculars.Model.GoogleResponseModel;
import com.example.foodnoculars.Model.PlaceModel;
import com.example.foodnoculars.SavedRestaurantModel;
import com.example.foodnoculars.NearbyPlaceInterface;
import com.example.foodnoculars.R;
import com.example.foodnoculars.Webservice.RetrofitAPI;
import com.example.foodnoculars.Webservice.RetrofitClient;
import com.example.foodnoculars.databinding.FragmentNearbyRestaurantsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NearbyRestaurants extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, NearbyPlaceInterface {

    private View view;
    private FragmentNearbyRestaurantsBinding binding;

    private GoogleMap mGoogleMap;
    private Location currentLocation;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Marker currentMarker;
    private int radius = 50000;

    private boolean isLocationPermissionOk, isTrafficEnable;
    private AppPermissions appPermissions;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference locationReference;
    private DatabaseReference userLocationReference;

    private PlaceModel selectedPlaceModel;
    private SavedRestaurantModel savedRestaurantModel;
    private GooglePlaceModel googlePlaceModel;
    private GooglePlaceAdapter googlePlaceAdapter;
    private RetrofitAPI retrofitAPI;
    private PlaceInfoWindowAdapter placeInfoWindowAdapter;

    private ArrayList<String> userSavedLocationId;
    private List<GooglePlaceModel> googlePlaceModelList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNearbyRestaurantsBinding.inflate(inflater, container, false);

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        locationReference = FirebaseDatabase.getInstance().getReference("Places");
        userLocationReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(firebaseAuth.getUid())
                .child("Saved Restaurants");


        appPermissions = new AppPermissions();
        retrofitAPI = RetrofitClient.getRetrofitClient().create(RetrofitAPI.class);

        //Array Lists
        googlePlaceModelList = new ArrayList<>();
        userSavedLocationId = new ArrayList<>();

        //get user location
        binding.currentLocation.setOnClickListener(currentLocation -> getCurrentLocation());

        //Display traffic on map
        binding.enableTraffic.setOnClickListener(view -> {

            if (isTrafficEnable) {
                if (mGoogleMap != null) {
                    mGoogleMap.setTrafficEnabled(false);
                    isTrafficEnable = false;
                }
            } else {
                if (mGoogleMap != null) {
                    mGoogleMap.setTrafficEnabled(true);
                    isTrafficEnable = true;
                }
            }

        });


        //Change map type
        binding.btnMapType.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(requireContext(), view);
            popupMenu.getMenuInflater().inflate(R.menu.map_type_menu_items,
                    popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.btnNormal:
//                        String mapType = "GoogleMap.MAP_TYPE_NORMAL";
//                        saveMapDisplay(mapType);
                        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;

                    case R.id.btnSatellite:

                        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;

                    case R.id.btnTerrain:

                        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        break;
                }
                return true;
            });

            popupMenu.show();
        });


        binding.placesGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {

                if (checkedId != -1) {
                    PlaceModel placeModel = AllConstant.placesName.get(checkedId - 1);
                    binding.edtPlaceName.setText(placeModel.getName());
                    selectedPlaceModel = placeModel;
                    getPlaces(placeModel.getPlaceType());
                }
            }
        });

        return binding.getRoot();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        for (PlaceModel placeModel : AllConstant.placesName) {
            Chip chip = new Chip(requireContext());
            chip.setText(placeModel.getName());
            chip.setId(placeModel.getId());
            chip.setPadding(8, 8, 8, 8);
            chip.setTextColor(getResources().getColor(R.color.white, null));
            chip.setChipBackgroundColor(getResources().getColorStateList(R.color.gold, null));
            chip.setChipIcon(ResourcesCompat.getDrawable(getResources(), placeModel.getDrawableId(), null));
            chip.setCheckable(true);
            chip.setCheckedIconVisible(false);
            binding.placesGroup.addView(chip);
        }
        // saveUserLocationToFirebase();
        setUpRecyclerView();
        getUserSavedLocations();

    }
//    private void saveUserLocationToFirebase() {
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
//                .child(firebaseAuth.getUid()).child("User location");
//
//    }


    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        int markerTag = (int) marker.getTag();
        //Log.d("TAG", "onMarkerClick: " + markerTag);

        binding.placesRecyclerView.scrollToPosition(markerTag);
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        googleMap.getUiSettings().setMyLocationButtonEnabled(false);

        //binding.currentLocation.setOnClickListener(currentLocation -> getCurrentLocation());
        if (appPermissions.isLocationOk(requireContext())) {
            isLocationPermissionOk = true;
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.setOnMarkerClickListener(this);
            mGoogleMap.getUiSettings().setTiltGesturesEnabled(true);
            setUpLocationUpdate();
        }
        else if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Location Permission")
                    .setMessage("Allow Foodnoculars to access your location while you are using this app?")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            requestLocation();
                        }
                    })
                    .create().show();
        } else {
            requestLocation();
        }

    }


    private void setUpLocationUpdate() {

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(9000);
        locationRequest.setFastestInterval(5000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    for (Location location : locationResult.getLocations()) {
                    }
                }
                super.onLocationResult(locationResult);
            }
        };
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());

        startLocationUpdates();
    }


    private void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            isLocationPermissionOk = false;
            return;
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Toast.makeText(requireContext(), "Location updated started", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

        getCurrentLocation();
    }

    private void getCurrentLocation() {

        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            isLocationPermissionOk = false;
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                currentLocation = location;
                placeInfoWindowAdapter = null;
                placeInfoWindowAdapter = new PlaceInfoWindowAdapter(currentLocation, requireContext());
                mGoogleMap.setInfoWindowAdapter(placeInfoWindowAdapter);
                moveCameraToLocation(location);
            }
        });


    }

    private void moveCameraToLocation(Location location) {

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new
                LatLng(location.getLatitude(), location.getLongitude()), 17);

        MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                .title("Current Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail());

        if (currentMarker != null) {
            currentMarker.remove();
        }

        currentMarker = mGoogleMap.addMarker(markerOptions);
        currentMarker.setTag(703);
        mGoogleMap.animateCamera(cameraUpdate);

    }

    private void stopLocationUpdate() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @SuppressLint("InlinedApi")
    private void requestLocation() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                , Manifest.permission.ACCESS_BACKGROUND_LOCATION}, AllConstant.LOCATION_REQUEST_CODE);

    }

    private void getPlaces(String placeName) {

        if (isLocationPermissionOk) {

            //loadingDialog.startLoading();
            String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                    + currentLocation.getLatitude() + "," + currentLocation.getLongitude()
                    + "&radius=" + radius + "&type=" + placeName + "&key=" +
                    getResources().getString(R.string.google_maps_key);

            if (currentLocation != null) {
                retrofitAPI.getNearByPlaces(url).enqueue(new Callback<GoogleResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<GoogleResponseModel> call, @NonNull Response<GoogleResponseModel> response) {
//                        Gson gson = new Gson();
//                        String res = gson.toJson(response.body());
//                        Log.d("TAG", "onResponse: " + res);
                        if (response.errorBody() == null) {
                            if (response.body() != null) {
                                if (response.body().getGooglePlaceModelList() != null && response.body().getGooglePlaceModelList().size() > 0)
                                {

                                    googlePlaceModelList.clear();
                                    mGoogleMap.clear();
                                    for (int i = 0; i < response.body().getGooglePlaceModelList().size(); i++)
                                    {

                                        if (userSavedLocationId.contains(response.body().getGooglePlaceModelList().get(i).getPlaceId()))
                                        {
                                            response.body().getGooglePlaceModelList().get(i).setSaved(true);
                                        }

                                        googlePlaceModelList.add(response.body().getGooglePlaceModelList().get(i));
                                        addMapMarker(response.body().getGooglePlaceModelList().get(i), i);

                                    }

                                    googlePlaceAdapter.setGooglePlaceModels(googlePlaceModelList);

                                }
                                else if (response.body().getError() != null)
                                {

                                    Snackbar.make(binding.getRoot(),
                                            response.body().getError(),
                                            Snackbar.LENGTH_LONG).show();

                                }
                                else
                                {

                                    mGoogleMap.clear();
                                    googlePlaceModelList.clear();
                                    googlePlaceAdapter.setGooglePlaceModels(googlePlaceModelList);
                                    radius += 1000;
                                    getPlaces(placeName);

                                }
                            }
                        }
                        else
                        {

                            Toast.makeText(requireContext(), "Error : " + response.errorBody(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<GoogleResponseModel> call, Throwable t) {
                        Log.d("TAG", "onFailure: " + t);
                    }
                });
            }
        }
    }

    private void addMapMarker(GooglePlaceModel googlePlaceModel, int position) {

        MarkerOptions markerOptions = new MarkerOptions().position
                (new LatLng(googlePlaceModel.getGeometry().getLocation().getLat(),
                googlePlaceModel.getGeometry().getLocation().getLng()))
                .title(googlePlaceModel.getName())
                .snippet(googlePlaceModel.getVicinity());

        markerOptions.icon(MakeCustomIcon());
        mGoogleMap.addMarker(markerOptions).setTag(position);

    }
    private BitmapDescriptor MakeCustomIcon() {

        Drawable background = ContextCompat.getDrawable(requireContext(), R.drawable.ic_location);
        background.setTint(getResources().getColor(R.color.quantum_googred900, null));
        background.setBounds(0, 0, 70, 70);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void setUpRecyclerView() {

        binding.placesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.placesRecyclerView.setHasFixedSize(false);
        googlePlaceAdapter = new GooglePlaceAdapter(this);
        binding.placesRecyclerView.setAdapter(googlePlaceAdapter);

        SnapHelper snapHelper = new PagerSnapHelper();

        snapHelper.attachToRecyclerView(binding.placesRecyclerView);

        binding.placesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                int position = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                if (position > -1) {
                    GooglePlaceModel googlePlaceModel = googlePlaceModelList.get(position);
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(googlePlaceModel.getGeometry().getLocation().getLat(),
                            googlePlaceModel.getGeometry().getLocation().getLng()), 20));
                }
            }
        });

    }

    @Override
    public void onSaveClick(GooglePlaceModel googlePlaceModel) {

        if (userSavedLocationId.contains(googlePlaceModel.getPlaceId())) {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Remove location")
                    .setMessage("Are you sure to you want to remove this location?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            removePlace(googlePlaceModel);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create().show();
        } else {


            locationReference.child(googlePlaceModel.getPlaceId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.exists()) {

                        SavedRestaurantModel savedRestaurantModel = new SavedRestaurantModel(googlePlaceModel.getName(), googlePlaceModel.getVicinity(),
                                googlePlaceModel.getPlaceId(), googlePlaceModel.getRating(),
                                googlePlaceModel.getUserRatingsTotal(),
                                googlePlaceModel.getGeometry().getLocation().getLat(),
                                googlePlaceModel.getGeometry().getLocation().getLng());

                        saveLocation(savedRestaurantModel);
                    }

                    saveUserLocation(googlePlaceModel.getPlaceId());

                    int index = googlePlaceModelList.indexOf(googlePlaceModel);
                    googlePlaceModelList.get(index).setSaved(true);
                    googlePlaceAdapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }

    private void saveUserLocation(String placeId) {

        userSavedLocationId.add(placeId);
        userLocationReference.setValue(userSavedLocationId);
        Snackbar.make(binding.getRoot(), "Location Saved", Snackbar.LENGTH_LONG).show();
    }

    private void saveLocation(SavedRestaurantModel savedRestaurantModel) {
        locationReference.child(savedRestaurantModel.getPlaceId()).setValue(savedRestaurantModel);
    }

    @Override
    public void onDirectionClick(GooglePlaceModel googlePlaceModel) {
        String placeId = googlePlaceModel.getPlaceId();
        Double lat = googlePlaceModel.getGeometry().getLocation().getLat();
        Double lng = googlePlaceModel.getGeometry().getLocation().getLng();

        Intent intent = new Intent(requireContext(), Directions.class);
        intent.putExtra("placeId", placeId);
        intent.putExtra("lat", lat);
        intent.putExtra("lng", lng);

        startActivity(intent);

    }


    private void getUserSavedLocations() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(firebaseAuth.getUid()).child("Saved Restaurants");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String placeId = ds.getValue(String.class);
                        userSavedLocationId.add(placeId);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void removePlace(GooglePlaceModel googlePlaceModel) {

        userSavedLocationId.remove(googlePlaceModel.getPlaceId());
        int index = googlePlaceModelList.indexOf(googlePlaceModel);
        googlePlaceModelList.get(index).setSaved(false);
        googlePlaceAdapter.notifyDataSetChanged();

        Snackbar.make(binding.getRoot(), "Place removed", Snackbar.LENGTH_LONG)
                .setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        userSavedLocationId.add(googlePlaceModel.getPlaceId());
                        googlePlaceModelList.get(index).setSaved(true);
                        googlePlaceAdapter.notifyDataSetChanged();

                    }
                })
                .addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);

                        userLocationReference.setValue(userSavedLocationId);
                    }
                }).show();

    }
    private String saveMapDisplay(String mapType) {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
            .child(firebaseAuth.getUid()).child("Map type");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String mapType = ds.getValue(String.class);
                    }
                }
                else if (!snapshot.exists())
                {
                    Task<Void> databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                            .child(firebaseAuth.getUid()).child("Map type").setValue(mapType);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, error.getMessage());
            }
        });

        return mapType;
    }


    @Override
    public void onPause() {
        super.onPause();

        if (fusedLocationProviderClient != null)
            stopLocationUpdate();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (fusedLocationProviderClient != null) {

            startLocationUpdates();
            if (currentMarker != null) {
                currentMarker.remove();
            }
        }
    }
}