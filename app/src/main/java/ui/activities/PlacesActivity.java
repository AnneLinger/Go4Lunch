package ui.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.anne.linger.go4lunch.databinding.ActivityAuthenticationBinding;
import com.anne.linger.go4lunch.databinding.ActivityPlacesBinding;

/**
*Main activity of the app
*/
public class PlacesActivity extends AppCompatActivity {

    //For UI
    private ActivityPlacesBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
    }

    //Configure the UI
    private void initUi() {
        mBinding = ActivityPlacesBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
    }
}
