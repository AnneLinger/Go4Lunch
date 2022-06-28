package ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.anne.linger.go4lunch.R;
import com.anne.linger.go4lunch.databinding.ActivityAuthenticationBinding;
import com.anne.linger.go4lunch.databinding.DialogAuthenticationWithExistingMailBinding;
import com.anne.linger.go4lunch.databinding.DialogAuthenticationWithNewMailBinding;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

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
            showDialogToLoginUserWithMail();
        });
    }

    private void startToNextActivity() {
        Intent intent = new Intent(AuthenticationActivity.this, PlacesActivity.class);
        startActivity(intent);
        finish();
    }

    // Show Snack Bar with a message
    private void showSnackBar( String message){
        Snackbar.make(mBinding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

    //For login with new mail
    public void showDialogToCreateUserWithMail() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DialogAuthenticationWithNewMailBinding mDialogBinding = DialogAuthenticationWithNewMailBinding.inflate(LayoutInflater.from(this));
        builder.setView(mDialogBinding.getRoot());
        builder.setTitle(R.string.authentication_new_mail_title);

        mDialogBinding.buttonRegister.setOnClickListener(view -> {
            String mail = mDialogBinding.inputMail.getText().toString();
            String password = mDialogBinding.inputPassword.getText().toString();
            mAuth.createUserWithEmailAndPassword(mail, password);
            startToNextActivity();
        });

        builder.create().show();
    }

    //For login with existing mail
    public void showDialogToLoginUserWithMail() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DialogAuthenticationWithExistingMailBinding mDialogBinding = DialogAuthenticationWithExistingMailBinding.inflate(LayoutInflater.from(this));
        builder.setView(mDialogBinding.getRoot());
        builder.setTitle(R.string.authentication_mail_title);

        mDialogBinding.buttonLogin.setOnClickListener(view -> {
            String mail = mDialogBinding.inputMail.getText().toString();
            String password = mDialogBinding.inputPassword.getText().toString();
            mAuth.createUserWithEmailAndPassword(mail, password);
            startToNextActivity();
        });

        mDialogBinding.buttonRegisterNewUser.setOnClickListener(view -> {
            showDialogToCreateUserWithMail();
        });

        builder.create().show();
    }

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
        transaction.add(android.R.id.content, authenticationWithMailFragment).disallowAddToBackStack().commit();


    //Enregistre un rappel pour le contrat de résultat d'activité FirebaseUI
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );

    // Choose authentication providers
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build(),
            new AuthUI.IdpConfig.FacebookBuilder().build(),
            new AuthUI.IdpConfig.TwitterBuilder().build());

    // Create and launch sign-in intent
    Intent signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build();
    signInLauncher.launch(signInIntent);

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            // ...
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }*/
}


