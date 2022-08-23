package viewmodel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import model.User;
import repositories.LocationRepositoryImpl;
import repositories.UserRepositoryImpl;

/**
*ViewModel for users
*/

@HiltViewModel
public class UserViewModel extends ViewModel {
    //For data
    private final UserRepositoryImpl mUserRepositoryImpl;
    private final LocationRepositoryImpl mLocationRepositoryImpl;

    //Constructor
    @Inject
    public UserViewModel(UserRepositoryImpl userRepositoryImpl, LocationRepositoryImpl locationRepositoryImpl) {
        mUserRepositoryImpl = userRepositoryImpl;
        mLocationRepositoryImpl = locationRepositoryImpl;

    }

    //..........................For authentication...............................................

    public FirebaseUser getCurrentUser() {
        return mUserRepositoryImpl.getCurrentFirebaseUser();
    }

    public void createUser() {
        mUserRepositoryImpl.instanceFirestore();
        mUserRepositoryImpl.createUser();
    }

    public void logOut() {
        mUserRepositoryImpl.logOut();
    }

    public void deleteAccount(Context context) {
        mUserRepositoryImpl.deleteAccount(context);
    }

    //..........................For location.....................................................

    @SuppressLint("MissingPermission")
    public void getUserLocation(Context context) {
            mLocationRepositoryImpl.startLocationRequest(context);
    }

    public LiveData<Location> getLivedataLocation() {
        return mLocationRepositoryImpl.getLiveDataLocation();
    }

    public void stopLocationRequest() {
        mLocationRepositoryImpl.stopLocationRequest();
    }

}


