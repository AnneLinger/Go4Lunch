package ui.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.anne.linger.go4lunch.R;
import com.anne.linger.go4lunch.databinding.ActivitySettingsBinding;
import com.google.firebase.auth.FirebaseUser;

import dagger.hilt.android.AndroidEntryPoint;
import viewmodel.UserViewModel;

/**
 * Activity for the user settings
 */

@RequiresApi(api = Build.VERSION_CODES.M)
@AndroidEntryPoint
public class SettingsActivity extends AppCompatActivity {

    //For ui
    private ActivitySettingsBinding mBinding;
    private final float DEFAULT_ZOOM = 12;
    private final boolean DEFAULT_NOTIFICATIONS = true;

    //For data
    private SharedPreferences mSharedPreferences;
    private UserViewModel mUserViewModel;
    private float zoom;
    private boolean notifications;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        configureViewModel();
        configureActionBar();
        saveZoom();
        saveNotificationsChoice();
        deleteAccount();
        loadUserSettings();
        saveUserSettings();
    }

    private void initUi() {
        mBinding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
    }

    private void configureViewModel() {
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    private void configureActionBar() {
        mBinding.settingsToolbar.setNavigationOnClickListener(view -> navigateToPlacesActivity());
    }

    private void initSharedPreferences() {
        mSharedPreferences = SettingsActivity.this.getSharedPreferences(getString(R.string.user_settings), Context.MODE_PRIVATE);
    }

    private void navigateToPlacesActivity() {
        Intent intent = new Intent(SettingsActivity.this, PlacesActivity.class);
        startActivity(intent);
        finish();
    }

    private void saveZoom() {
        mBinding.sliderZoom.addOnChangeListener((slider, value, fromUser) -> zoom = value);
    }

    private void saveNotificationsChoice() {
        mBinding.switchNotifications.setOnCheckedChangeListener((compoundButton, b) -> notifications = b);
    }

    private void deleteAccount() {
        mBinding.btDeleteAccount.setOnClickListener(v -> showDialogToConfirmDeleteAccount());
    }

    //Dialog to alert about essential permission
    public void showDialogToConfirmDeleteAccount() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.are_you_sure)
                .setCancelable(false)
                .setPositiveButton(R.string.confirm_delete, (dialog, which) -> {
                    mUserViewModel.deleteAccount(SettingsActivity.this);
                    navigateToAuthenticationActivity();
                })
                .setNegativeButton(R.string.cancel_delete, (dialog, which) -> {
                })
                .setCancelable(false)
                .create()
                .show();
    }


    //If user settings exist
    private void loadUserSettings() {
        FirebaseUser currentUser = mUserViewModel.getCurrentUserFromFirebase();
        if (currentUser != null) {
            initSharedPreferences();
            if (mSharedPreferences != null) {
                mBinding.sliderZoom.setValue(mSharedPreferences.getFloat(getString(R.string.zoom), DEFAULT_ZOOM));
                mBinding.switchNotifications.setChecked(mSharedPreferences.getBoolean(getString(R.string.notifications), DEFAULT_NOTIFICATIONS));
            } else {
                mBinding.sliderZoom.setValue(DEFAULT_ZOOM);
                mBinding.switchNotifications.setChecked(DEFAULT_NOTIFICATIONS);
            }
        }
    }

    //When click on button save
    private void saveUserSettings() {
        mBinding.btSaveSettings.setOnClickListener(view -> {
            initSharedPreferences();

            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putFloat(getString(R.string.zoom), zoom);
            editor.putBoolean(getString(R.string.notifications), notifications);
            editor.apply();

            Toast.makeText(SettingsActivity.this, getString(R.string.settings_saved), Toast.LENGTH_SHORT).show();
            navigateToPlacesActivity();
        });
    }

    private void navigateToAuthenticationActivity() {
        Intent intent = new Intent(SettingsActivity.this, AuthenticationActivity.class);
        startActivity(intent);
        finish();
    }
}
