package ui.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
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
    private float DEFAULT_RADIUS = 15;
    private boolean DEFAULT_NOTIFICATIONS = true;

    //For data
    private SharedPreferences mSharedPreferences;
    private Location userLocation;
    private SettingsViewModel mSettingsViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        configureViewModel();
        configureActionBar();
        loadUserSettings();
        saveUserSettings();
    }

    //Configure UI
    private void initUi() {
        mBinding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
    }

    //Configure data
    private void configureViewModel() {
        mSettingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
    }

    private void configureActionBar() {
        mBinding.settingsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToPlacesActivity();
            }
        });
    }

    //When click on action bar for back
    private void navigateToPlacesActivity() {
        Intent intent = new Intent(SettingsActivity.this, PlacesActivity.class);
        startActivity(intent);
        finish();
    }

    //If user settings exist
    private void loadUserSettings() {
        Log.d("Anne", "loadUserSettings");
        FirebaseUser currentUser = mSettingsViewModel.getCurrentUser();
        Log.d("Anne", "getCurrentUser");
        if(currentUser!=null) {
            Log.d("Anne", "currentUserOK");
            //mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            mSharedPreferences = this.getPreferences(MODE_PRIVATE);
            if (mSharedPreferences != null) {
                Log.d("Anne", "sharedPrefOK");
                float newRadius = mSharedPreferences.getFloat(getString(R.string.radius), DEFAULT_RADIUS);
                DEFAULT_ZOOM = mSharedPreferences.getFloat(getString(R.string.zoom), DEFAULT_ZOOM);
                DEFAULT_NOTIFICATIONS = mSharedPreferences.getBoolean(getString(R.string.notifications), DEFAULT_NOTIFICATIONS);

                mBinding.sliderRadius.setValue(newRadius);
                mBinding.sliderZoom.setValue(DEFAULT_ZOOM);
                mBinding.switchNotifications.setChecked(DEFAULT_NOTIFICATIONS);
            }
            else {
                Log.d("Anne", "noSharedPref");
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
                //mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
                mSharedPreferences = SettingsActivity.this.getSharedPreferences(getString(R.string.user_settings), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPreferences.edit();

                mBinding.sliderRadius.addOnChangeListener(new Slider.OnChangeListener() {
                    @Override
                    public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                        mSharedPreferences.edit().putFloat(getString(R.string.radius), value).apply();
                        //editor.putFloat(getString(R.string.radius), value);
                        //editor.apply();
                    }
                });

                mBinding.sliderZoom.addOnChangeListener(new Slider.OnChangeListener() {
                    @Override
                    public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                        editor.putFloat(getString(R.string.zoom), value);
                        editor.apply();
                    }
                });

                mBinding.switchNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        editor.putBoolean(getString(R.string.notifications), b);
                        editor.apply();
                    }
                });
                Toast.makeText(SettingsActivity.this, getString(R.string.settings_saved), Toast.LENGTH_SHORT).show();
                navigateToPlacesActivity();
            }
        });
    }
}
