package repositories;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationRequest;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;

/**
*Implementation of LocationRepository interface
*/
public class LocationRepositoryImpl implements LocationRepository {

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private final MutableLiveData<Location> mLocationMutableLiveData = new MutableLiveData<>();
    private LocationCallback mLocationCallback;
    private LocationRequest mLocationRequest;
    private int mLocationRequestCode = 1000;
    private double mLatitude = 0.0;
    private double mLongitude = 0.0;

    @Inject
    public LocationRepositoryImpl() {
    }

    @Override
    public LiveData<Location> getCurrentLocation() {
        return mLocationMutableLiveData;
    }

    @Override
    public void instantiateFusedProviderLocationClient(Context context) {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }

    @Override
    public void configureLocationRequest() {
    }

    @Override
    public void configureLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                for (Location location : locationResult.getLocations()) {
                    mLocationMutableLiveData.setValue(location);
                }
            }
        };
    }

    @SuppressLint("MissingPermission")
    @Override
    public void updateLocation(Context context) {
        mFusedLocationProviderClient.getLastLocation().addOnSuccessListener((Activity) context, location -> {
            mLocationMutableLiveData.setValue(location);
            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();
        });
    }

    @Override
    public void stopLocationUpdates() {

    }
}
