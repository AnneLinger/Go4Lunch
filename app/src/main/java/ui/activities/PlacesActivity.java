package ui.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.anne.linger.go4lunch.R;
import com.anne.linger.go4lunch.databinding.ActivityPlacesBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import dagger.hilt.android.AndroidEntryPoint;
import di.ViewModelFactory;
import viewmodel.UserViewModel;

/**
*Main activity of the app
*/

@AndroidEntryPoint
public class PlacesActivity extends AppCompatActivity {

    //For UI
    private ActivityPlacesBinding mBinding;

    //For data
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private UserViewModel mUserViewModel;
    private final AuthenticationActivity mAuthenticationActivity = new AuthenticationActivity();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        //configureViewModel();
        showSnackBar(getString(R.string.successful_auth));
    }

    //Configure the UI
    private void initUi() {
        mBinding = ActivityPlacesBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
    }

    //Configure bottom nav
    private void configureBottomNav() {
        //TODO complete with method no deprecated
    }

    //Configure data
    private void configureViewModel() {
        mUserViewModel = new ViewModelProvider(mAuthenticationActivity).get(UserViewModel.class);
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
