package ui.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.anne.linger.go4lunch.R;
import com.anne.linger.go4lunch.databinding.FragmentMapViewBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import pub.devrel.easypermissions.EasyPermissions;
import viewmodel.UserViewModel;

/**
*Fragment to display a map for places
*/
public class MapViewFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    //For UI
    private FragmentMapViewBinding mBinding;
    private GoogleMap mGoogleMap;
    private static final int DEFAULT_ZOOM = 12;

    //For data
    private Location mUserLocation;
    private UserViewModel mUserViewModel;

    //For permission
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
        if (checkPermission()) {
            mGoogleMap.setMyLocationEnabled(true);
            getUserLocation();
        }
    }

    private void configureViewModel() {
        mUserViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
    }

    //Check the user answer about permission to get his location
    private boolean checkPermission() {
        return EasyPermissions.hasPermissions(requireContext(), perms);
    }

    //Ask permission to the user for location
    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(requireActivity(), perms, 0);
    }

    //Get the user location
    private void getUserLocation() {
        mUserViewModel.getUserLocation(this.getContext());
        mUserViewModel.getLivedataLocation().observe(requireActivity(), this::updateLocationUI);
    }

    //Update the map with the user location
    private void updateLocationUI(Location location) {
        mUserLocation = location;
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                .title(String.valueOf(R.string.marker_title)));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), DEFAULT_ZOOM));
    }

    //Update the map when the user location changes
    @Override
    public void onLocationChanged(@NonNull Location location) {
        updateLocationUI(location);
    }
}
