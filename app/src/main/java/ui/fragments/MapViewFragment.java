package ui.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.anne.linger.go4lunch.R;
import com.anne.linger.go4lunch.databinding.FragmentMapViewBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import pub.devrel.easypermissions.EasyPermissions;
import ui.activities.PlacesActivity;
import viewmodel.UserViewModel;

/**
*Fragment to display a map for places
*/
public class MapViewFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    //For UI
    private FragmentMapViewBinding mBinding;
    private GoogleMap mGoogleMap;
    private static final int DEFAULT_ZOOM = 15;

    //For data
    private Location mUserLocation;
    private UserViewModel mUserViewModel;
    private boolean locationPermissionGranted;
    private static final String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMapViewBinding.inflate(getLayoutInflater());
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(this);
        configureViewModel();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGoogleMap = googleMap;
        requestLocationPermission();
        Log.d("Anne", "requestPermissionOK");
        if (checkPermission()) {
            Log.d("Anne", "checkPermissionOK");
            mGoogleMap.setMyLocationEnabled(true);
            Log.d("Anne", "setMyLocOK");
            getUserLocation();
            Log.d("Anne", "getLocOK");
        }
    }

    private void configureViewModel() {
        mUserViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
    }

    private boolean checkPermission() {
        return EasyPermissions.hasPermissions(requireContext(), perms);
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(requireActivity(), perms, 0);
    }

    private void getUserLocation() {
        Context context = this.getContext();
        mUserViewModel.refresh(context);
        mUserViewModel.getCurrentLocation().observe(requireActivity(), this::updateLocationUI);
        Log.d("Anne", "observeOK");
    }

    private void updateLocationUI(Location location) {
        mUserLocation = location;
        Log.d("Anne", "locationOK");
        //Double latitude = mUserLocation.getLatitude();
        Log.d("Anne", "latitude OK");
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        Log.d("Anne", "getUiSettingsOK");
        //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), DEFAULT_ZOOM));
        mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                .title("Marker"));
        Log.d("Anne", "AddMarkerOK");
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12));
        Log.d("Anne", "moveCameraOK");
        //mGoogleMap.setMyLocationEnabled(true);

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        //updateLocationUI(location);
    }
}
