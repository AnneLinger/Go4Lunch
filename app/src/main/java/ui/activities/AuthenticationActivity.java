package ui.activities;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.anne.linger.go4lunch.R;
import com.anne.linger.go4lunch.databinding.ActivityAuthenticationBinding;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;
import model.User;
import viewmodel.UserViewModel;

/**
*Activity for the authentication of the user
*/

@AndroidEntryPoint
public class AuthenticationActivity extends AppCompatActivity {
    //For UI
    private ActivityAuthenticationBinding mBinding;

    //For data
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private UserViewModel mUserViewModel;

    private static final int RC_SIGN_IN = 123;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        configureViewModel();
        startSignInActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.responseAfterSignIn(requestCode, resultCode, data);
    }

    //Check if user is signed in
     @Override
     public void onStart() {
         super.onStart();
         FirebaseUser currentUser = mUserViewModel.getCurrentUser();
         if(currentUser!=null) {
             navigateToPlacesActivity();
         }
     }

    //Configure the UI
    private void initUi() {
        mBinding = ActivityAuthenticationBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        //Objects.requireNonNull(getSupportActionBar()).hide();
    }

    //Configure data
    private void configureViewModel() {
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    private void navigateToPlacesActivity() {
        Intent intent = new Intent(AuthenticationActivity.this, PlacesActivity.class);
        startActivity(intent);
        finish();
    }

    // Show Snack Bar with a message
    private void showSnackBar(String message) {
        Snackbar.make(mBinding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

    private void startSignInActivity() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build()
        );

        // Launch the activity
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);
    }

    private void responseAfterSignIn(int requestCode, int resultCode, Intent data) {
        IdpResponse idpResponse = IdpResponse.fromResultIntent(data);
        if (requestCode == RC_SIGN_IN) {
            // SUCCESS
            if (resultCode == RESULT_OK) {
                createUser();
                Toast.makeText(AuthenticationActivity.this, getString(R.string.successful_auth), Toast.LENGTH_SHORT).show();
                navigateToPlacesActivity();
            } else {
                // ERRORS
                if (idpResponse == null) {
                    showSnackBar(getString(R.string.canceled_authentication));
                } else if (idpResponse.getError() != null) {
                    if (idpResponse.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                        showSnackBar(getString(R.string.no_connection));
                    } else if (idpResponse.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                        showSnackBar(getString(R.string.unknown_error));
                    }
                }
            }
        }
    }

    private void createUser() {
        mUserViewModel.createUser();
    }

    private FirebaseUser getCurrentUser() {
        return mUserViewModel.getCurrentUser();
    }

}


