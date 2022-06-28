package ui.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.anne.linger.go4lunch.R;
import com.anne.linger.go4lunch.databinding.ActivityAuthenticationBinding;
import com.anne.linger.go4lunch.databinding.ActivityPlacesBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

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
        showSnackBar(getString(R.string.successful_auth));
    }

    //Configure the UI
    private void initUi() {
        mBinding = ActivityPlacesBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
    }

    // Show Snack Bar with a message
    private void showSnackBar( String message){
        Snackbar.make(mBinding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

    //TODO : configure in the layout
    //To log out
    private void logOut() {
        FirebaseAuth.getInstance().signOut();
    }
}
