package ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.anne.linger.go4lunch.R;
import com.anne.linger.go4lunch.databinding.ActivityPlacesBinding;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;
import model.Booking;
import model.autocompletepojo.AutocompleteResponse;
import model.autocompletepojo.Prediction;
import ui.fragments.ListViewFragment;
import ui.fragments.MapViewFragment;
import ui.fragments.WorkmatesFragment;
import viewmodel.AutocompleteViewModel;
import viewmodel.BookingViewModel;
import viewmodel.UserViewModel;

/**
*Main activity of the app launched after login
*/

@AndroidEntryPoint
public class PlacesActivity extends AppCompatActivity {

    //For UI
    private ActivityPlacesBinding mBinding;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private ListView mSearchListView;
    private ArrayAdapter<String> mAdapter;

    //For data
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser;
    private UserViewModel mUserViewModel;
    private AutocompleteViewModel mAutocompleteViewModel;
    private BookingViewModel mBookingViewModel;
    private final AuthenticationActivity mAuthenticationActivity = new AuthenticationActivity();
    private ArrayList<String> mSearchList = new ArrayList<>();
    private List<Booking> mBookingList = new ArrayList<>();
    private Location mLocation;
    private String mLocationString;
    private MapViewFragment mMapViewFragment;
    private ListViewFragment mListViewFragment;
    private WorkmatesFragment mWorkmatesFragment;
    private String mUserPlaceBooking;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        configureViewModels();
        configureBottomNav();
        configureDrawer();
        getSupportFragmentManager().beginTransaction().add(R.id.activity_places_frame_layout, new MapViewFragment()).commit();
        getUserLocation();
        getCurrentUser();
        observeBookings();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.e("Anne", "onCreateOptionMenu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.search_icon).getActionView();
        //searchView.setBackgroundColor(getResources().getColor(R.color.white));
        searchView.setQueryHint(getResources().getText(R.string.search_hint));
        searchView.setIconifiedByDefault(false);
        searchView.getOverlay();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.e("Anne", "onQueryTextSubmitAutocomplete");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.e("Anne", "onQueryTextChangeAutocomplete");
                //TODO recover default radius or shared pref radius
                int radius = 12000;
                mAutocompleteViewModel.fetchAutocomplete(s, mLocationString, radius);
                return true;
            }
        });

        searchView.setOnCloseListener(() -> {
            Log.e("Anne", "setOnCloseAutocomplete");
            mAutocompleteViewModel.setAutocompleteToNull();
            return true;
        });

        return true;
    }

    //Configure UI
    private void initUi() {
        mBinding = ActivityPlacesBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        setSupportActionBar(mBinding.toolbar);
    }

    //Configure data
    private void configureViewModels() {
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        mAutocompleteViewModel = new ViewModelProvider(this).get(AutocompleteViewModel.class);
        mBookingViewModel = new ViewModelProvider(this).get(BookingViewModel.class);
    }

    //Configure bottom nav
    private void configureBottomNav() {
        mBinding.bottomNav.setOnItemSelectedListener(this::selectBottomNavItem);
    }

    @SuppressLint("NonConstantResourceId")
    public boolean selectBottomNavItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_map_view:
                if(mMapViewFragment==null){
                    this.mMapViewFragment = MapViewFragment.newInstance();
                }
                if(!mMapViewFragment.isVisible()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.activity_places_frame_layout, mMapViewFragment).commit();
                }
                return true;
            case R.id.item_list_view:
                if(mListViewFragment==null) {
                    this.mListViewFragment = ListViewFragment.newInstance();
                }
                if(!mListViewFragment.isVisible()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.activity_places_frame_layout, mListViewFragment).commit();
                }
                return true;
            case R.id.item_workmates:
                if(mWorkmatesFragment==null) {
                    this.mWorkmatesFragment = WorkmatesFragment.newInstance();
                }
                if(!mWorkmatesFragment.isVisible()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.activity_places_frame_layout, mWorkmatesFragment).commit();
                }
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
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.your_lunch:
                        if(mUserPlaceBooking==null) {
                            noBookingToast();
                        }
                        else {
                            navigateToPlaceDetailsActivity();
                        }
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

    private void getCurrentUser() {
        mUser = mUserViewModel.getCurrentUser();
    }

    //Get the user location
    @SuppressLint("MissingPermission")
    private void getUserLocation() {
        Log.e("Anne", "getLoc");
        mUserViewModel.getLivedataLocation().observe(this, this::initLocation);
    }

    private void initLocation(Location location) {
        Log.e("Anne", "initLoc");
        mLocation = location;
        mLocationString = location.getLatitude() + "," + location.getLongitude();
    }

    private void observeBookings() {
        mBookingViewModel.getBookingListLiveData().observe(this, this::getUserBooking);
    }

    private void getUserBooking(List<Booking> bookings) {
        mBookingList = bookings;
        if(!mBookingList.isEmpty()) {
            for(Booking booking : bookings) {
                List<FirebaseUser> userList = booking.getUserList();
                for (FirebaseUser firebaseUser : userList) {
                    if (firebaseUser.getUid().equals(mUser.getUid())) {
                        mUserPlaceBooking = booking.getPlaceId();
                    }
                }
            }
        }
    }

    private void noBookingToast() {
        Toast.makeText(PlacesActivity.this, getString(R.string.no_booking), Toast.LENGTH_SHORT).show();
    }

    private void navigateToSettingsActivity() {
        Intent intent = new Intent(PlacesActivity.this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToPlaceDetailsActivity() {
        Intent intent = new Intent(PlacesActivity.this, PlaceDetailsActivity.class);
        intent.putExtra("place id", mUserPlaceBooking);
        startActivity(intent);
        finish();
    }

    private void navigateToAuthenticationActivity() {
        Intent intent = new Intent(PlacesActivity.this, AuthenticationActivity.class);
        startActivity(intent);
        finish();
    }
}
