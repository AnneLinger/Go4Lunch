package viewmodel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;

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
    private final MediatorLiveData<String> locationMessageLiveData = new MediatorLiveData<>();
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

        locationMessageLiveData.addSource(locationLiveData, location ->
                combine(location, hasLocationPermissionLiveData.getValue())
        );

        locationMessageLiveData.addSource(hasLocationPermissionLiveData, hasLocationPermission ->
                combine(locationLiveData.getValue(), hasLocationPermission));
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

    private void combine(@Nullable Location location, @Nullable Boolean hasTheLocationPermission) {
        if (location == null) {
            if (hasTheLocationPermission == null || !hasTheLocationPermission) {
                // Never hardcode translatable Strings, always use Context.getString(R.string.my_string) instead !
                locationMessageLiveData.setValue("I am lost... Should I click the permission button ?!");
            } else {
                locationMessageLiveData.setValue("Querying location, please wait for a few seconds...");
            }
        } else {
            locationMessageLiveData.setValue("I am at coordinates (lat:" + location.getLatitude() + ", long:" + location.getLongitude() + ")");
        }
    }

    @SuppressLint("MissingPermission")
    public void refresh(Context context) {
        boolean hasTheLocationPermission = mPermissionChecker.hasLocationPermission();
        hasLocationPermissionLiveData.setValue(hasTheLocationPermission);

        if (hasTheLocationPermission) {
            mLocationRepositoryImpl.startLocationRequest(context);
        } else {
            mLocationRepositoryImpl.stopLocationRequest(context);
        }
    }

    public LiveData<String> getGpsMessageLiveData() {
        return locationMessageLiveData;
    }

    /**public LiveData<Location> getCurrentLocation() {
        return mLocationRepositoryImpl.getCurrentLocation();
    }

    public void instantiateFusedProviderLocationClient(Context context) {
        mLocationRepositoryImpl.instantiateFusedProviderLocationClient(context);
    }

    public void updateLocation(Context context) {
        mLocationRepositoryImpl.updateLocation(context);
    }*/
}


