package ui.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.anne.linger.go4lunch.R;
import com.anne.linger.go4lunch.databinding.ActivityAuthenticationBinding;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import viewmodel.UserViewModel;

/**
 * Activity for the authentication of the user
 */

@RequiresApi(api = Build.VERSION_CODES.M)
@AndroidEntryPoint
public class AuthenticationActivity extends AppCompatActivity {

    //For UI
    private ActivityAuthenticationBinding mBinding;

    //For data
    private UserViewModel mUserViewModel;

    //To get auth result
    private final ActivityResultLauncher<Intent> mIntentActivityResultLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            this::onSignInResponse
    );

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureViewModel();
        startSignInActivity();
    }

    private void configureViewModel() {
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    private void navigateToPlacesActivity() {
        Intent intent = new Intent(AuthenticationActivity.this, PlacesActivity.class);
        startActivity(intent);
        finish();
    }

    private void showSnackBarMessage(String message) {
        Snackbar.make(mBinding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

    private void startSignInActivity() {
        //Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.TwitterBuilder().build()
        );

        //Launch the activity
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setTheme(R.style.LoginTheme)
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false, true)
                .build();
        mIntentActivityResultLauncher.launch(signInIntent);
    }

    private void onSignInResponse(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse idpResponse = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            createUser();
            Toast.makeText(AuthenticationActivity.this, getString(R.string.successful_auth), Toast.LENGTH_SHORT).show();
            navigateToPlacesActivity();
        } else {
            if (idpResponse == null) {
                showSnackBarMessage(getString(R.string.canceled_authentication));
            } else if (idpResponse.getError() != null) {
                if (idpResponse.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackBarMessage(getString(R.string.no_connection));
                } else if (idpResponse.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackBarMessage(getString(R.string.unknown_error));
                }
            }
        }
    }

    private void createUser() {
        mUserViewModel.createUser();
    }
}


