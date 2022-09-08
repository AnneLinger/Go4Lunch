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
 * Implementation of LocationRepository interface
 */

public class LocationRepositoryImpl implements LocationRepository {

    private static final int LOCATION_REQUEST_INTERVAL_MS = 10_000;
    private static final float SMALLEST_DISPLACEMENT_THRESHOLD_METER = 25;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private final MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();
    private LocationCallback callback;

    @Inject
    public LocationRepositoryImpl() {
    }

    public void instantiateFusedLocationProviderClient(Context context) {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }

    @RequiresPermission(anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"})
    public void startLocationRequest(Context context) {
        instantiateFusedLocationProviderClient(context);

        callback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                locationMutableLiveData.setValue(location);
            }
        };

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

    public LiveData<Location> getLiveDataLocation() {
        return locationMutableLiveData;
    }

    public void stopLocationRequest() {
        mFusedLocationProviderClient.removeLocationUpdates(callback);
    }
}
