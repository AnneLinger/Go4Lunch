package ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.anne.linger.go4lunch.R;
import com.anne.linger.go4lunch.databinding.ActivityPlacesBinding;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import kotlin.contracts.SimpleEffect;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import ui.fragments.ListViewFragment;
import ui.fragments.MapViewFragment;
import ui.fragments.WorkmatesFragment;
import viewmodel.UserViewModel;

/**
*Main activity of the app launched after login
*/

@AndroidEntryPoint
public class PlacesActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    //For UI
    private ActivityPlacesBinding mBinding;
    private ActionBarDrawerToggle mActionBarDrawerToggle;

    //For data
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private UserViewModel mUserViewModel;
    private final AuthenticationActivity mAuthenticationActivity = new AuthenticationActivity();

    //For permission
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 124;
    private boolean permissionDenied = false;
    private GoogleMap mGoogleMap;
    private static final int DEFINE_LOCATION_REQUEST_CODE = 1;
    private String fineLocation = Manifest.permission.ACCESS_FINE_LOCATION;
    private String coarseLocation = Manifest.permission.ACCESS_COARSE_LOCATION;

    //For location
    private Location mUserLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        configureViewModel();
        showSnackBar(getString(R.string.successful_auth));
        configureBottomNav();
        mBinding.bottomNav.setSelectedItemId(R.id.item_map_view);
        checkUserLocation();
        configureDrawer();
    }

    //Configure UI
    private void initUi() {
        mBinding = ActivityPlacesBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
    }

    //Configure data
    private void configureViewModel() {
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    // Show Snack Bar with a message
    private void showSnackBar( String message){
        Snackbar.make(mBinding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

    //Configure bottom nav
    private void configureBottomNav() {
        mBinding.bottomNav.setOnItemSelectedListener(this::select);
    }

    public boolean select(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_map_view:
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_places_frame_layout, new MapViewFragment()).commit();
                return true;
            case R.id.item_list_view:
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_places_frame_layout, new ListViewFragment()).commit();
                return true;
            case R.id.item_workmates:
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_places_frame_layout, new WorkmatesFragment()).commit();
                return true;
        }
        return false;
    }

    private void configureDrawer() {
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mBinding.placesLayout, R.string.open_drawer_menu, R.string.close_drawer_menu);
        mBinding.placesLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();
        drawerListener();
    }

    private void drawerListener() {

        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.placesLayout.open();           }
        });
        mBinding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return false;
            }
        });
    }

    //TODO : configure in the drawer
    //To log out
    private void logOut() {
        mUserViewModel.logOut();
    }

    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(DEFINE_LOCATION_REQUEST_CODE)
    private void checkUserLocation() {
        if (EasyPermissions.hasPermissions(this, fineLocation) || EasyPermissions.hasPermissions(this, coarseLocation)) {
            getUserLocation();
        }
        else {
            requestPermission();
        }
    }

    @SuppressLint("MissingPermission")
    private void getUserLocation() {
        //For location
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                mUserLocation = location;
                double latitude = mUserLocation.getLatitude();
                double longitude = mUserLocation.getLongitude();
            }
        });
        mGoogleMap.setMyLocationEnabled(true);
    }

    private void requestPermission() {
        EasyPermissions.requestPermissions(this, String.valueOf(R.string.rationale_location), LOCATION_PERMISSION_REQUEST_CODE, fineLocation, coarseLocation);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
        else {
            requestPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
