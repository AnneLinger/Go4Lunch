package viewmodel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;

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

    //..........................For user account.................................................

    public FirebaseUser getCurrentUserFromFirebase() {
        return mUserRepositoryImpl.getCurrentUserFromFirebase();
    }

    public void getCurrentUserFromFirestore(String userId) {
        mUserRepositoryImpl.getCurrentUserFromFirestore(userId);
    }

    public LiveData<User> getUserLiveData() {
        return mUserRepositoryImpl.getUserLiveData();
    }

    public void getUserListFromFirestore() {
        mUserRepositoryImpl.getUserListFromFirestore();
    }

    public LiveData<List<User>> getUserListLiveData() {
        return mUserRepositoryImpl.getUserListLiveData();
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


