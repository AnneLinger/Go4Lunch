package ui.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.anne.linger.go4lunch.R;
import com.anne.linger.go4lunch.databinding.FragmentMapViewBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Map;

import model.nearbysearchpojo.Result;
import pub.devrel.easypermissions.EasyPermissions;
import viewmodel.PlacesViewModel;
import viewmodel.UserViewModel;

/**
*Fragment to display a map for places
*/
public class MapViewFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    //For UI
    private FragmentMapViewBinding mBinding;
    private GoogleMap mGoogleMap;
    private float zoom = 15;
    private int radius = 12000;

    //For data
    private Location mLocation;
    private String mLocationString;
    private SharedPreferences mSharedPreferences;
    private UserViewModel mUserViewModel;
    private PlacesViewModel mPlacesViewModel;
    private List<Result> mPlaceList;
    private Context mContext;

    //For permissions
    private static final String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private static final int PERMISSION = 5678;
    private ActivityResultLauncher<String[]> mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), (Map<String, Boolean> isGranted) -> {
        boolean granted = true;
        for (Map.Entry<String, Boolean> x : isGranted.entrySet())
            if(!x.getValue()) granted = false;
        if (granted) getUserLocation();
        else {
            showDialogToDenyAccessApp();
        }
    });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMapViewBinding.inflate(getLayoutInflater());
        configureViewModel();
        mActivityResultLauncher.launch(perms);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGoogleMap = googleMap;
        checkIfUserIsSignIn();
        requestLocationPermission();
    }

    private void configureViewModel() {
        mUserViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        mPlacesViewModel = new ViewModelProvider(requireActivity()).get(PlacesViewModel.class);
    }

    //Check if user is signed in to load SharedPreferences
    private void checkIfUserIsSignIn() {
        FirebaseUser currentUser = mUserViewModel.getCurrentUser();
        if(currentUser!=null) {
            mSharedPreferences = requireActivity().getSharedPreferences(getString(R.string.user_settings), Context.MODE_PRIVATE);
            if (mSharedPreferences != null) {
                zoom = mSharedPreferences.getFloat(getString(R.string.zoom), zoom);
                radius = (int) mSharedPreferences.getFloat(getString(R.string.radius), radius);
            }
        }
    }

    //Check the location permission
    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d("Anne", "requestLocPerm=>getUserLoc");
            //Permission ok => display map
            getUserLocation();
        } else {
            //Permission not ok => ask permission to the user for location
            Log.d("Anne", "requestLocPerm=>requestPerm");
            ActivityCompat.requestPermissions(requireActivity(), perms, PERMISSION);
        }
    }

    //Get the user location when permission is ok
    @SuppressLint("MissingPermission")
    private void getUserLocation() {
        Log.d("Anne", "getUserLoc");
        mGoogleMap.setMyLocationEnabled(true);
        mUserViewModel.getUserLocation(this.getContext());
        mUserViewModel.getLivedataLocation().observe(requireActivity(), this::updateMapLocation);
    }

    //Update map with user location
    private void updateMapLocation(Location location) {
        Log.d("Anne", "updateMapLoc");
        mLocation = location;
        mLocationString = mLocation.getLatitude() + "," + mLocation.getLongitude();
        Log.d("Anne", mLocationString);
        mPlacesViewModel.fetchNearbySearchPlaces(mLocationString, radius);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        //mGoogleMap.addMarker(new MarkerOptions()
          //      .position(new LatLng(location.getLatitude(), location.getLongitude()))
            //    .title(requireActivity().getString(R.string.marker_title)));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), zoom));
        //updateMapPlaces();
    }

    //Update the map with places
    private void updateMapPlaces() {
        Log.d("Anne", "updateMap");
        mGoogleMap.clear();
        mPlacesViewModel.getNearbySearchResponseLiveData().observe(getViewLifecycleOwner(), new Observer<List<Result>>() {
            @Override
            public void onChanged(List<Result> results) {
                mPlaceList = results;
                if(mPlaceList.isEmpty()) {
                    Log.d("Anne", "=null");
                }
                else {
                    Log.d("Anne", "!=null");
                }
                for (Result mResult : mPlaceList) {
                    LatLng placeLatLng = new LatLng(mResult.getGeometry().getLocation().getLatitude(), mResult.getGeometry().getLocation().getLongitude());
                    mGoogleMap.addMarker(new MarkerOptions()
                            .position(placeLatLng)
                            .title(mResult.getName())
                            .icon(BitmapDescriptorFactory.fromBitmap(setUpMarkerIcon())));
                }
            }
        });
    }

    //Update the map when the user location changes
    @Override
    public void onLocationChanged(@NonNull Location location) {
        //updateMapPlaces();
    }

    //Dialog to alert about essential permission
    public void showDialogToDenyAccessApp() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getContext());
        alertDialogBuilder.setTitle(R.string.access_denied_title)
                .setMessage(R.string.access_denied_text)
                .setCancelable(false)
                .setPositiveButton(R.string.go_to_settings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", "com.anne.linger.go4lunch", null));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.close_go4lunch, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requireActivity().finish();
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

    private Bitmap setUpMarkerIcon() {
        Bitmap markerIcon = BitmapFactory.decodeResource(requireContext().getResources(), R.drawable.ic_baseline_restaurant_24);
        markerIcon = Bitmap.createScaledBitmap(markerIcon, 100, 100, false);
        return markerIcon;
    }

    //Update the map with the user location
    private void updateLocationUI(Location location) {
        mLocation = location;
        mLocationString = mLocation.getLatitude() + "," + mLocation.getLongitude();
        Log.d("Anne", mLocationString);
        mPlacesViewModel.fetchNearbySearchPlaces(mLocationString, radius);
        mPlacesViewModel.getNearbySearchResponseLiveData().observe(requireActivity(), results -> {
            mPlaceList = results;
            if(mPlaceList.isEmpty()) {
                Log.d("Anne", "=null");
            }
            else {
                Log.d("Anne", "!=null");
            }
        });
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                .title(String.valueOf(R.string.marker_title)));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), zoom));
    }

    private void initPlacesList() {
        //mSharedPreferences = requireActivity().getSharedPreferences(getString(R.string.user_settings), Context.MODE_PRIVATE);
        /**if (mSharedPreferences != null) {
         radius = (int) mSharedPreferences.getFloat(getString(R.string.radius), radius);
         }*/
        Log.d("Anne", "initPlaceList");
        mGoogleMap.clear();
        //mPlacesViewModel.getNearbySearchResponseLiveData().observe(requireActivity(), this::updateMap);
        mPlacesViewModel.getNearbySearchResponseLiveData().observe(requireActivity(), results -> {
            mPlaceList = results;
            if(mPlaceList.isEmpty()) {
                Log.d("Anne", "=null");
            }
            else {
                Log.d("Anne", "!=null");
            }
        });
    }
}
