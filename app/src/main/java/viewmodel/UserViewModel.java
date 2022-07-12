package viewmodel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import di.PermissionChecker;
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

    private final PermissionChecker mPermissionChecker;
    private final MutableLiveData<Boolean> hasLocationPermissionLiveData = new MutableLiveData<>();

    //For threads
    //private final Executor mExecutor;

    //Constructor
    @Inject
    public UserViewModel(UserRepositoryImpl userRepositoryImpl, LocationRepositoryImpl locationRepositoryImpl, PermissionChecker permissionChecker) {
        mUserRepositoryImpl = userRepositoryImpl;
        //mExecutor = executor;
        mLocationRepositoryImpl = locationRepositoryImpl;
        mPermissionChecker = permissionChecker;

        LiveData<Location> locationLiveData = mLocationRepositoryImpl.getLocationLiveData();
    }

    //..........................For authentication...............................................

    public FirebaseUser getCurrentUser() {
        return mUserRepositoryImpl.getCurrentUser();
    }

    public void createUser() {
        mUserRepositoryImpl.createUser();
    }

    public void logOut() {
        mUserRepositoryImpl.logOut();
    }

    //..........................For location.....................................................

    @SuppressLint("MissingPermission")
    public void refresh(Context context) {
       // boolean hasTheLocationPermission = mPermissionChecker.hasLocationPermission();
        //hasLocationPermissionLiveData.setValue(hasTheLocationPermission);

        //if (hasTheLocationPermission) {
            Log.d("Anne", "refreshOK");
            mLocationRepositoryImpl.startLocationRequest(context);
        //} else {
          //  mLocationRepositoryImpl.stopLocationRequest(context);
        //}
    }

    public LiveData<Location> getCurrentLocation() {
        return mLocationRepositoryImpl.getLocationLiveData();
    }

}


