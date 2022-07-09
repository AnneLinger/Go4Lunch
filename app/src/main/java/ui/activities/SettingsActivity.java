package ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.anne.linger.go4lunch.databinding.ActivityPlacesBinding;
import com.anne.linger.go4lunch.databinding.ActivitySettingsBinding;

import java.util.Objects;

/**
*Activity for the user settings
*/
public class SettingsActivity extends AppCompatActivity {
    //For ui
    private ActivitySettingsBinding mBinding;

    private SharedPreferences mSharedPreferences;
    private Location userLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        configureActionBar();
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

    private void navigateToPlacesActivity() {
        Intent intent = new Intent(SettingsActivity.this, PlacesActivity.class);
        startActivity(intent);
        finish();
    }
}
