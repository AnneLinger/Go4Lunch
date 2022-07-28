package ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.anne.linger.go4lunch.R;
import com.anne.linger.go4lunch.databinding.ActivityPlacesBinding;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
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
public class PlacesActivity extends AppCompatActivity {

    //For UI
    private ActivityPlacesBinding mBinding;
    private ActionBarDrawerToggle mActionBarDrawerToggle;

    //For data
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private UserViewModel mUserViewModel;
    private final AuthenticationActivity mAuthenticationActivity = new AuthenticationActivity();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        configureViewModel();
        configureBottomNav();
        mBinding.bottomNav.setSelectedItemId(R.id.item_map_view);
        configureDrawer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.search_icon).getActionView();
        searchView.setQueryHint(getResources().getText(R.string.search_hint));
        searchView.setBackgroundColor(getResources().getColor(R.color.white));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //TODO Manage the user query
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //TODO Manage the autocomplete with places API
                return false;
            }
        });

        return true;
    }

    //Configure UI
    private void initUi() {
        mBinding = ActivityPlacesBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        getSupportActionBar().hide();
    }

    //Configure data
    private void configureViewModel() {
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    //Configure bottom nav
    private void configureBottomNav() {
        mBinding.bottomNav.setOnItemSelectedListener(this::selectBottomNavItem);
    }

    public boolean selectBottomNavItem(MenuItem item) {
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

        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.placesLayout.open();           }
        });
        mBinding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.your_lunch:
                        navigateToPlaceDetailsActivity();
                        return true;
                    case R.id.item2:
                        navigateToSettingsActivity();
                        return true;
                    case R.id.item3:
                        mUserViewModel.logOut();
                        navigateToAuthenticationActivity();
                        return true;
                }
                mBinding.placesLayout.close();
                return false;
            }
        });
    }

    private void navigateToSettingsActivity() {
        Intent intent = new Intent(PlacesActivity.this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToPlaceDetailsActivity() {
        Intent intent = new Intent(PlacesActivity.this, PlaceDetailsActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToAuthenticationActivity() {
        Intent intent = new Intent(PlacesActivity.this, AuthenticationActivity.class);
        startActivity(intent);
        finish();
    }
}
