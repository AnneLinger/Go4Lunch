package repositories;

import android.content.Context;
import android.location.Location;

import androidx.lifecycle.LiveData;

/**
 *Interface repository for the user location
 */
public interface LocationRepository {

    void instantiateFusedLocationProviderClient(Context context);

    void startLocationRequest(Context context);

    LiveData<Location> getLiveDataLocation();

    void stopLocationRequest();

}
