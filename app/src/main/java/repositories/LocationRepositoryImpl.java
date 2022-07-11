package repositories;

import android.content.Context;
import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import javax.inject.Inject;

/**
*Implementation of LocationRepository interface
*/
public class LocationRepositoryImpl implements LocationRepository {

    private static final int LOCATION_REQUEST_INTERVAL_MS = 10_000;
    private static final float SMALLEST_DISPLACEMENT_THRESHOLD_METER = 25;

    @NonNull
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @NonNull
    private final MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>(null);

    private LocationCallback callback;

    @Inject
    public LocationRepositoryImpl() {
    }

    public void instantiateFusedLocationProviderClient(Context context) {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public LiveData<Location> getLocationLiveData() {
        return locationMutableLiveData;
    }

    @RequiresPermission(anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"})
    public void startLocationRequest(Context context) {
        instantiateFusedLocationProviderClient(context);
        if (callback == null) {
            callback = new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    Location location = locationResult.getLastLocation();

                    locationMutableLiveData.setValue(location);
                }
            };
        }

        mFusedLocationProviderClient.removeLocationUpdates(callback);

        mFusedLocationProviderClient.requestLocationUpdates(
                LocationRequest.create()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setSmallestDisplacement(SMALLEST_DISPLACEMENT_THRESHOLD_METER)
                        .setInterval(LOCATION_REQUEST_INTERVAL_MS),
                callback,
                Looper.getMainLooper()
        );
    }

    public void stopLocationRequest(Context context) {
        instantiateFusedLocationProviderClient(context);
        if (callback != null) {
            mFusedLocationProviderClient.removeLocationUpdates(callback);
        }
    }

    /**private FusedLocationProviderClient mFusedLocationProviderClient;
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

    }*/
}
