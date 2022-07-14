package ui.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.anne.linger.go4lunch.R;
import com.anne.linger.go4lunch.databinding.ActivitySettingsBinding;
import com.google.android.material.slider.Slider;

/**
*Activity for the user settings
*/
public class SettingsActivity extends AppCompatActivity {
    //For ui
    private ActivitySettingsBinding mBinding;

    //For data
    private SharedPreferences mSharedPreferences;
    private Location userLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        configureActionBar();
        saveUserSettings();
    }

    //Configure UI
    private void initUi() {
        mBinding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
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

    //When click on button save
    private void saveUserSettings() {
        mBinding.btSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSharedPreferences = SettingsActivity.this.getSharedPreferences(getString(R.string.user_settings), Context.MODE_PRIVATE);
                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = mSharedPreferences.edit();

                mBinding.sliderRadius.addOnChangeListener(new Slider.OnChangeListener() {
                    @Override
                    public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                        editor.putFloat(getString(R.string.radius), value);
                    }
                });

                mBinding.sliderZoom.addOnChangeListener(new Slider.OnChangeListener() {
                    @Override
                    public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                        editor.putFloat(getString(R.string.zoom), value);
                    }
                });

                editor.apply();
                Toast.makeText(SettingsActivity.this, getString(R.string.settings_saved), Toast.LENGTH_SHORT).show();
                navigateToPlacesActivity();
            }
        });
    }
}
