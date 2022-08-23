package ui.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.anne.linger.go4lunch.R;
import com.anne.linger.go4lunch.databinding.ActivitySettingsBinding;
import com.google.android.material.slider.Slider;
import com.google.firebase.auth.FirebaseUser;

import dagger.hilt.android.AndroidEntryPoint;
import viewmodel.SettingsViewModel;
import viewmodel.UserViewModel;

/**
*Activity for the user settings
*/

@AndroidEntryPoint
public class SettingsActivity extends AppCompatActivity {

    //For ui
    private ActivitySettingsBinding mBinding;
    private float DEFAULT_ZOOM = 15;
    private float DEFAULT_RADIUS = 12000;
    private boolean DEFAULT_NOTIFICATIONS = true;

    //For data
    private SharedPreferences mSharedPreferences;
    private Location userLocation;
    private SettingsViewModel mSettingsViewModel;
    private UserViewModel mUserViewModel;
    private FirebaseUser mUser;
    private float zoom;
    private float radius = 12000;
    private boolean notifications;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        configureViewModels();
        configureActionBar();
        saveRadius();
        saveZoom();
        saveNotificationsChoice();
        deleteAccount();
        loadUserSettings();
        saveUserSettings();
    }

    //Configure UI
    private void initUi() {
        mBinding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
    }

    //Configure data
    private void configureViewModels() {
        mSettingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    private void configureActionBar() {
        mBinding.settingsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToPlacesActivity();
            }
        });
    }

    private void initSharedPreferences() {
        mSharedPreferences = SettingsActivity.this.getSharedPreferences(getString(R.string.user_settings), Context.MODE_PRIVATE);
    }

    private void getCurrentUser() {
        mUser = mUserViewModel.getCurrentUser();
    }

    //When click on action bar for back
    private void navigateToPlacesActivity() {
        Intent intent = new Intent(SettingsActivity.this, PlacesActivity.class);
        startActivity(intent);
        finish();
    }

    private void saveRadius() {
        mBinding.sliderRadius.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                radius = value;

            }
        });
    }

    private void saveZoom() {
        mBinding.sliderZoom.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                zoom = value;
            }
        });
    }

    private void saveNotificationsChoice() {
        mBinding.switchNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               notifications = b;
            }
        });
    }

    private void deleteAccount() {
        mBinding.btDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogToConfirmDeleteAccount();
            }
        });
    }

    //Dialog to alert about essential permission
    public void showDialogToConfirmDeleteAccount() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.are_you_sure)
                .setCancelable(false)
                .setPositiveButton(R.string.confirm_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mUserViewModel.deleteAccount(SettingsActivity.this);
                        navigateToAuthenticationActivity();
                    }
                })
                .setNegativeButton(R.string.cancel_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }


    //If user settings exist
    private void loadUserSettings() {
        FirebaseUser currentUser = mSettingsViewModel.getCurrentUser();
        if(currentUser!=null) {
            initSharedPreferences();
            if (mSharedPreferences != null) {
                mBinding.sliderRadius.setValue(mSharedPreferences.getFloat(getString(R.string.radius), DEFAULT_RADIUS));
                mBinding.sliderZoom.setValue(mSharedPreferences.getFloat(getString(R.string.zoom), DEFAULT_ZOOM));
                mBinding.switchNotifications.setChecked(mSharedPreferences.getBoolean(getString(R.string.notifications), DEFAULT_NOTIFICATIONS));            }
            else {
                mBinding.sliderRadius.setValue(DEFAULT_RADIUS);
                mBinding.sliderZoom.setValue(DEFAULT_ZOOM);
                mBinding.switchNotifications.setChecked(DEFAULT_NOTIFICATIONS);
            }
        }
    }

    //When click on button save
    private void saveUserSettings() {
        mBinding.btSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initSharedPreferences();

                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putFloat(getString(R.string.radius), radius);
                editor.putFloat(getString(R.string.zoom), zoom);
                editor.putBoolean(getString(R.string.notifications), notifications);
                editor.apply();

                Toast.makeText(SettingsActivity.this, getString(R.string.settings_saved), Toast.LENGTH_SHORT).show();
                navigateToPlacesActivity();
            }
        });
    }

    private void navigateToAuthenticationActivity() {
        Intent intent = new Intent(SettingsActivity.this, AuthenticationActivity.class);
        startActivity(intent);
        finish();
    }
}
