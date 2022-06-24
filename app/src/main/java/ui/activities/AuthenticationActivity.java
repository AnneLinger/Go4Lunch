package ui.activities;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.anne.linger.go4lunch.databinding.ActivityAuthenticationBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
*Activity for the authentication of the user
*/
public class AuthenticationActivity extends AppCompatActivity {

    //For UI
    private ActivityAuthenticationBinding mBinding;

    private FirebaseAuth mAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        initData();
        mAuth = FirebaseAuth.getInstance();
        setupListeners();
    }

    //Configure the UI
    private void initUi() {
        mBinding = ActivityAuthenticationBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
    }

    //Configure data
    private void initData() {
    }

    //Configure the listener on login buttons
    private void setupListeners() {
        //To login with email
        mBinding.btMailLogin.setOnClickListener(view -> {
            startSignInActivity();
        });
    }

    //Start a new activity for email login
    private void startSignInActivity() {
    }
}


