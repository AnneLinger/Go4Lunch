package ui.activities;

import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.anne.linger.go4lunch.databinding.ActivityPlacesBinding;
import com.anne.linger.go4lunch.databinding.ActivitySettingsBinding;

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
    }

    //Configure UI
    private void initUi() {
        mBinding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
    }
}
