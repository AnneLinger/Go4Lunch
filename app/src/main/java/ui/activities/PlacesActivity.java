package ui.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.anne.linger.go4lunch.R;
import com.anne.linger.go4lunch.databinding.ActivityPlacesBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import dagger.hilt.android.AndroidEntryPoint;
import ui.fragments.ListViewFragment;
import ui.fragments.MapViewFragment;
import ui.fragments.WorkmatesFragment;
import viewmodel.UserViewModel;

/**
*Main activity of the app
*/

@AndroidEntryPoint
public class PlacesActivity extends AppCompatActivity {

    //For UI
    private ActivityPlacesBinding mBinding;
    private BottomNavigationView mBottomNavigationView;

    //For data
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private UserViewModel mUserViewModel;
    private final AuthenticationActivity mAuthenticationActivity = new AuthenticationActivity();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        configureViewModel();
        showSnackBar(getString(R.string.successful_auth));
        configureBottomNav();
    }

    //Configure the UI
    private void initUi() {
        mBinding = ActivityPlacesBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
    }

    //Configure bottom nav
    private void configureBottomNav() {
        //TODO complete with method no deprecated
        mBinding.bottomNav.setOnItemSelectedListener(this::select);
        //mBottomNavigationView.setOnNavigationItemSelectedListener(item -> updateFragment(item.getItemId()));
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

    /**public void setOnItemSelectedListener(@Nullable NavigationBarView.OnItemSelectedListener listener) {
        Fragment selectedFragment = null;
        if (R.id.item_map_view == (listener)) {
            selectedFragment = new MapViewFragment();

            selectedFragment = new ListViewFragment();

            selectedFragment = new WorkmatesFragment();
        } else if (R.id.item_list_view.equals(listener)) {
            selectedFragment = new ListViewFragment();

            selectedFragment = new WorkmatesFragment();
        } else if (R.id.item_workmates.equals(listener)) {
            selectedFragment = new WorkmatesFragment();
        }

        assert selectedFragment != null;
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_places_frame_layout, selectedFragment).commit();

        return true
    }

    private Boolean updateFragment(Integer integer) {
        Fragment selectedFragment = null;
        switch (integer) {
            case R.id.item_map_view:
                selectedFragment = new MapViewFragment();
            case R.id.item_list_view:
                selectedFragment = new ListViewFragment();
            case R.id.item_workmates:
                selectedFragment = new WorkmatesFragment();
        }

        assert selectedFragment != null;
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_places_frame_layout, selectedFragment).commit();

        return true;
    }*/

    //Configure data
    private void configureViewModel() {
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    // Show Snack Bar with a message
    private void showSnackBar( String message){
        Snackbar.make(mBinding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

    //TODO : configure in the layout
    //To log out
    private void logOut() {
        mUserViewModel.logOut();
    }
}
