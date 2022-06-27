package ui.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.anne.linger.go4lunch.databinding.ActivityAuthenticationBinding;
import com.google.firebase.auth.FirebaseAuth;

import injections.ViewModelFactory;
import viewmodel.UserViewModel;

/**
*Activity for the authentication of the user
*/
public class AuthenticationActivity extends AppCompatActivity {

    //For UI
    private ActivityAuthenticationBinding mBinding;

    //For data
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private UserViewModel mUserViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        configureViewModel();
        setupListeners();
    }

    //Configure the UI
    private void initUi() {
        mBinding = ActivityAuthenticationBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
    }

    //Configure data
    private void configureViewModel() {
        mUserViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(UserViewModel.class);
    }

    //Configure the listener on login buttons
    private void setupListeners() {
        //To login with email
        mBinding.btMailLogin.setOnClickListener(view -> {
            showDialogToCreateUserWithMail();
        });
    }

    //For login with email
    private void showDialogToCreateUserWithMail() {
        //mUserViewModel.createUserWithMail();
    }
}


