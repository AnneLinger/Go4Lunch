package ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;

import com.anne.linger.go4lunch.R;
import com.anne.linger.go4lunch.databinding.ActivityPlacesBinding;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import model.Booking;
import ui.fragments.ListViewFragment;
import ui.fragments.MapViewFragment;
import ui.fragments.WorkmatesFragment;
import viewmodel.AutocompleteViewModel;
import viewmodel.BookingViewModel;
import viewmodel.UserViewModel;

/**
 * Main activity of the app launched after login
 */

@RequiresApi(api = Build.VERSION_CODES.M)
@AndroidEntryPoint
public class PlacesActivity extends AppCompatActivity {

    //For UI
    private ActivityPlacesBinding mBinding;

    //For data
    private FirebaseUser mUser;
    private UserViewModel mUserViewModel;
    private AutocompleteViewModel mAutocompleteViewModel;
    private BookingViewModel mBookingViewModel;
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
        getCurrentUser();
        configureDrawer();
        getSupportFragmentManager().beginTransaction().add(R.id.activity_places_frame_layout, new MapViewFragment()).commit();
        this.mMapViewFragment = MapViewFragment.newInstance();
        getUserLocation();
        observeBookings();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.search_icon).getActionView();
        searchView.setQueryHint(getResources().getText(R.string.search_hint));
        searchView.setIconifiedByDefault(false);
        searchView.getOverlay();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mAutocompleteViewModel.fetchAutocomplete(s, mLocationString);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        searchView.setOnCloseListener(() -> {
            mAutocompleteViewModel.setAutocompleteToNull();
            return true;
        });

        return true;
    }

    private void initUi() {
        mBinding = ActivityPlacesBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        setSupportActionBar(mBinding.toolbar);
    }

    private void configureViewModels() {
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        mAutocompleteViewModel = new ViewModelProvider(this).get(AutocompleteViewModel.class);
        mBookingViewModel = new ViewModelProvider(this).get(BookingViewModel.class);
    }

    private void configureBottomNav() {
        mBinding.bottomNav.setOnItemSelectedListener(this::selectBottomNavItem);
    }

    @SuppressLint("NonConstantResourceId")
    public boolean selectBottomNavItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_map_view:
                if (mMapViewFragment == null) {
                    this.mMapViewFragment = MapViewFragment.newInstance();
                }
                if (!mMapViewFragment.isVisible()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.activity_places_frame_layout, mMapViewFragment).commit();
                }
                mBinding.toolbar.setTitle(this.getString(R.string.toolbar_title));
                return true;
            case R.id.item_list_view:
                if (mListViewFragment == null) {
                    this.mListViewFragment = ListViewFragment.newInstance();
                }
                if (!mListViewFragment.isVisible()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.activity_places_frame_layout, mListViewFragment).commit();
                }
                mBinding.toolbar.setTitle(this.getString(R.string.toolbar_title));
                return true;
            case R.id.item_workmates:
                if (mWorkmatesFragment == null) {
                    this.mWorkmatesFragment = WorkmatesFragment.newInstance();
                }
                if (!mWorkmatesFragment.isVisible()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.activity_places_frame_layout, mWorkmatesFragment).commit();
                }
                mBinding.toolbar.setTitle(this.getString(R.string.available_workmates));
                return true;
        }
        return false;
    }

    private void configureDrawer() {
        //To open drawer
        mBinding.toolbar.setNavigationOnClickListener(view -> mBinding.placesLayout.open());

        //To manage header drawer data
        NavigationView navigationView = findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);
        ImageView userAvatar = headerView.findViewById(R.id.iv_header_avatar);
        TextView userName = headerView.findViewById(R.id.tv_header_name);
        TextView userMail = headerView.findViewById(R.id.tv_header_mail);

        if (mUser.getPhotoUrl() != null) {
            Glide.with(this)
                    .load(mUser.getPhotoUrl())
                    .circleCrop()
                    .into(userAvatar);
        }
        userName.setText(mUser.getDisplayName());
        userMail.setText(mUser.getEmail());

        //Listener on drawer menu
        mBinding.navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.your_lunch:
                    if (mUserPlaceBooking == null) {
                        noBookingToast();
                    } else {
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
        });
    }

    private void getCurrentUser() {
        mUser = mUserViewModel.getCurrentUserFromFirebase();
    }

    //Get the user location
    @SuppressLint("MissingPermission")
    private void getUserLocation() {
        mUserViewModel.getLivedataLocation().observe(this, this::initLocation);
    }

    private void initLocation(Location location) {
        mLocation = location;
        mLocationString = location.getLatitude() + "," + location.getLongitude();
    }

    private void observeBookings() {
        mBookingViewModel.fetchBookingList();
        mBookingViewModel.getBookingListLiveData().observe(this, this::getUserBooking);
    }

    private void getUserBooking(List<Booking> bookings) {
        mBookingViewModel.deletePreviousBookings();
        mBookingList = bookings;
        if (!mBookingList.isEmpty()) {
            for (Booking booking : mBookingList) {
                if (!(booking.getUser() == null)) {
                    if (booking.getUser().equalsIgnoreCase(mUser.getDisplayName())) {
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

    @Override
    protected void onStop() {
        super.onStop();
        mUserViewModel.stopLocationRequest();
        mBookingViewModel.getBookingListLiveData().removeObservers(this);
    }
}
