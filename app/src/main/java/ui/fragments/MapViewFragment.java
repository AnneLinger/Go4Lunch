package ui.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.anne.linger.go4lunch.R;
import com.anne.linger.go4lunch.databinding.FragmentMapViewBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import model.Booking;
import model.autocompletepojo.Prediction;
import model.nearbysearchpojo.Result;
import ui.activities.AuthenticationActivity;
import ui.activities.PlaceDetailsActivity;
import viewmodel.AutocompleteViewModel;
import viewmodel.BookingViewModel;
import viewmodel.PlacesViewModel;
import viewmodel.UserViewModel;

/**
 * Fragment to display a map for places
 */

@RequiresApi(api = Build.VERSION_CODES.M)
public class MapViewFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    //For UI
    private FragmentMapViewBinding mBinding;
    private GoogleMap mGoogleMap;
    private float zoom = 12;

    //For data
    private Location mLocation;
    private String mLocationString;
    private SharedPreferences mSharedPreferences;
    private UserViewModel mUserViewModel;
    private PlacesViewModel mPlacesViewModel;
    private BookingViewModel mBookingViewModel;
    private AutocompleteViewModel mAutocompleteViewModel;
    private List<Result> mPlaceList;
    private List<Booking> mBookingList = new ArrayList<>();
    private final List<Result> mPlaceListAutocomplete = new ArrayList<>();
    private final MapViewFragment mMapViewFragment = this;
    private Context mContext;
    private ProgressBar mProgressBar;

    //For location permission result
    private static final String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private final ActivityResultLauncher<String[]> mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), (Map<String, Boolean> isGranted) -> {
        boolean granted = true;
        for (Map.Entry<String, Boolean> x : isGranted.entrySet())
            if (!x.getValue()) granted = false;
        if (granted) getUserLocation();
        else {
            showDialogToDenyAccessApp();
        }
    });

    public static MapViewFragment newInstance() {
        return new MapViewFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMapViewBinding.inflate(getLayoutInflater());
        configureViewModels();
        this.onAttach(requireContext());
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(this);
        mProgressBar = mBinding.progressCircular;
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGoogleMap = googleMap;
        getDataIfUserIsSignIn();
        observeBookings();
        observeNearbyPlaces();
        observeAutocomplete();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void configureViewModels() {
        mUserViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        mPlacesViewModel = new ViewModelProvider(requireActivity()).get(PlacesViewModel.class);
        mBookingViewModel = new ViewModelProvider(requireActivity()).get(BookingViewModel.class);
        mAutocompleteViewModel = new ViewModelProvider(requireActivity()).get(AutocompleteViewModel.class);
    }

    //Check if user is signed in to load SharedPreferences
    private void getDataIfUserIsSignIn() {
        FirebaseUser currentUser = mUserViewModel.getCurrentUserFromFirebase();
        if (currentUser != null) {
            mSharedPreferences = requireActivity().getSharedPreferences(getString(R.string.user_settings), Context.MODE_PRIVATE);
            if (mSharedPreferences != null) {
                zoom = mSharedPreferences.getFloat(getString(R.string.zoom), zoom);
            }
            requestLocationPermission();
        } else {
            navigateToAuthenticationActivity();
        }
    }

    //Check the location permission
    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //Permission ok => display map
            getUserLocation();
        } else {
            //Permission not ok => ask permission to the user for location
            mActivityResultLauncher.launch(perms);
        }
    }

    //Get the user location when permission is ok
    @SuppressLint("MissingPermission")
    private void getUserLocation() {
        mUserViewModel.getUserLocation(this.getContext());
        mGoogleMap.setMyLocationEnabled(true);
        mUserViewModel.getLivedataLocation().observe(requireActivity(), this::updateMapLocation);
    }

    //Update map with user location
    private void updateMapLocation(Location location) {
        mLocation = location;
        mLocationString = mLocation.getLatitude() + "," + mLocation.getLongitude();
        mPlacesViewModel.fetchNearbySearchPlaces(mLocationString);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        Activity activity = getActivity();
        if (isAdded() && activity != null) {
            mGoogleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
                    .title(requireActivity().getString(R.string.marker_title)));
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), zoom));
        }
    }

    private void observeNearbyPlaces() {
        mPlacesViewModel.getNearbySearchResponseLiveData().observe(getViewLifecycleOwner(), this::updateMapWithNearbyPlaces);
    }

    private void updateMapWithNearbyPlaces(List<Result> results) {
        mPlaceList = results;
        updateMapWithData(mPlaceList);
    }

    private void updateMapWithData(List<Result> results) {
        mGoogleMap.clear();
        for (Result mResult : results) {
            LatLng placeLatLng = new LatLng(mResult.getGeometry().getLocation().getLat(), mResult.getGeometry().getLocation().getLng());
            addMarker(R.drawable.icon2073973_1920restaurant, placeLatLng, mResult);
            for (Booking booking : mBookingList) {
                if (booking.getPlaceId().equalsIgnoreCase(mResult.getPlaceId())) {
                    addMarker(R.drawable.icon_restaurant_green, placeLatLng, mResult);
                }
            }
        }
        mProgressBar.setVisibility(View.INVISIBLE);
        mGoogleMap.setOnInfoWindowClickListener(marker -> navigateToPlaceDetailsActivity(results, marker));
    }

    private void addMarker(int drawable, LatLng placeLatLng, Result result) {
        mGoogleMap.addMarker(new MarkerOptions()
                .position(placeLatLng)
                .title(result.getName())
                .icon(BitmapDescriptorFactory.fromBitmap(setUpMarkerIcon(drawable))));
    }

    private void observeBookings() {
        mBookingViewModel.fetchBookingList();
        mBookingViewModel.getBookingListLiveData().observe(getViewLifecycleOwner(), this::updateBookingList);
    }

    private void updateBookingList(List<Booking> bookings) {
        mBookingList = bookings;
    }

    private void observeAutocomplete() {
        mAutocompleteViewModel.getAutocompleteLiveData().observe(requireActivity(), this::updateMapWithAutocompletePlaces);
    }

    private void updateMapWithAutocompletePlaces(List<Prediction> predictions) {
        //Clear previous predictions
        mPlaceListAutocomplete.clear();

        if (mMapViewFragment.isVisible()) {
            //If no search is done
            if (predictions.isEmpty()) {
                updateMapWithNearbyPlaces(mPlaceList);
            }
            //If search occurs
            else {
                for (Result result : mPlaceList) {
                    for (Prediction prediction : predictions) {
                        if (prediction.getStructuredFormatting().getMainText().contains(result.getName())) {
                            mPlaceListAutocomplete.add(result);
                        }
                    }
                }
                //If search returns no result
                if (mPlaceListAutocomplete.isEmpty()) {
                    Toast.makeText(requireActivity(), getString(R.string.no_search_result), Toast.LENGTH_LONG).show();
                    final Handler handler = new Handler();
                    handler.postDelayed(() -> updateMapWithNearbyPlaces(mPlaceList), 2000);
                }
                //If there are search results to display
                else {
                    updateMapWithData(mPlaceListAutocomplete);
                }
            }
        }
    }

    //Update the map when the user location changes
    @Override
    public void onLocationChanged(@NonNull Location location) {
        observeNearbyPlaces();
    }

    private Bitmap setUpMarkerIcon(int drawable) {
        Bitmap markerBitmap = BitmapFactory.decodeResource(requireContext().getResources(), drawable);
        return Bitmap.createScaledBitmap(markerBitmap, 80, 120, false);
    }

    //Dialog to alert about essential permission
    public void showDialogToDenyAccessApp() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getContext());
        alertDialogBuilder.setTitle(R.string.access_denied_title)
                .setMessage(R.string.access_denied_text)
                .setCancelable(false)
                .setPositiveButton(R.string.go_to_settings, (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", "com.anne.linger.go4lunch", null));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                })
                .setNegativeButton(R.string.close_go4lunch, (dialog, which) -> requireActivity().finish())
                .setCancelable(false)
                .create()
                .show();
    }

    private void navigateToPlaceDetailsActivity(List<Result> results, Marker marker) {
        Intent intent = new Intent(requireActivity(), PlaceDetailsActivity.class);
        for (Result place : results) {
            if (Objects.requireNonNull(marker.getTitle()).equalsIgnoreCase(place.getName())) {
                intent.putExtra("place id", place.getPlaceId());
            }
        }
        startActivity(intent);
    }

    private void navigateToAuthenticationActivity() {
        Intent intent = new Intent(requireActivity(), AuthenticationActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mPlacesViewModel.getNearbySearchResponseLiveData().removeObservers(this);
            mAutocompleteViewModel.getAutocompleteLiveData().removeObservers(this);
            mBookingViewModel.getBookingListLiveData().removeObservers(this);
            mUserViewModel.stopLocationRequest();
        }
    }
}
