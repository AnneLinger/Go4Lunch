package ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.anne.linger.go4lunch.R;
import com.anne.linger.go4lunch.databinding.ActivityAuthenticationBinding;
import com.anne.linger.go4lunch.databinding.DialogAuthenticationWithMailBinding;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import injections.ViewModelFactory;
import ui.fragments.AuthenticationWithMailFragment;
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

    private static final int RC_SIGN_IN = 123;

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

    private void startToNextActivity() {
        Intent intent = new Intent(AuthenticationActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    public void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null && mAuth.getCurrentUser().getUid().length() > 2) {
            startToNextActivity();
        }
    }

    //For login with email
    public void showDialogToCreateUserWithMail() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DialogAuthenticationWithMailBinding mDialogBinding = DialogAuthenticationWithMailBinding.inflate(LayoutInflater.from(this));
        builder.setView(mDialogBinding.getRoot());
        builder.setTitle(R.string.authentication_mail_title);

        mDialogBinding.buttonRegister.setOnClickListener(view -> {
            String mail = mDialogBinding.inputMail.getText().toString();
            String password = mDialogBinding.inputPassword.getText().toString();
            mAuth.createUserWithEmailAndPassword(mail, password);
        });


        builder.create().show();

        /**List<AuthUI.IdpConfig> providers = Collections.singletonList(new AuthUI.IdpConfig.EmailBuilder().build());
        startActivityForResult(AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false, true)
                        //.setLogo(R.drawable.ic_logo_auth)
                        .build(),
                RC_SIGN_IN);

        FragmentManager fragmentManager = getSupportFragmentManager();
        AuthenticationWithMailFragment authenticationWithMailFragment = new AuthenticationWithMailFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, authenticationWithMailFragment).disallowAddToBackStack().commit();*/
    }
}


