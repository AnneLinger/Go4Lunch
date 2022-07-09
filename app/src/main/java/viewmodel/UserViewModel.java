package viewmodel;

import android.content.Context;
import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
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

    //For threads
    //private final Executor mExecutor;

    //Constructor
    @Inject
    public UserViewModel(UserRepositoryImpl userRepositoryImpl, LocationRepositoryImpl locationRepositoryImpl) {
        mUserRepositoryImpl = userRepositoryImpl;
        //mExecutor = executor;
        mLocationRepositoryImpl = locationRepositoryImpl;
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

    public LiveData<Location> getCurrentLocation() {
        return mLocationRepositoryImpl.getCurrentLocation();
    }

    public void instantiateFusedProviderLocationClient(Context context) {
        mLocationRepositoryImpl.instantiateFusedProviderLocationClient(context);
    }

    public void updateLocation(Context context) {
        mLocationRepositoryImpl.updateLocation(context);
    }
}


