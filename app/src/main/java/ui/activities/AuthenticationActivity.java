package ui.activities;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.anne.linger.go4lunch.databinding.ActivityAuthenticationBinding;

/**
*Activity for the authentication of the user
*/
public class AuthenticationActivity extends AppCompatActivity {

    //For UI
    private ActivityAuthenticationBinding mBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        initData();
    }

    //Configure the UI
    private void initUi() {
        mBinding = ActivityAuthenticationBinding.inflate(getLayoutInflater());
    }

    //Configure data
    private void initData() {

    }
}


