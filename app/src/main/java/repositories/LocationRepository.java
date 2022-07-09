package repositories;

import android.content.Context;
import android.location.Location;

import androidx.lifecycle.LiveData;

/**
 *Interface repository for the user location
 */
public interface LocationRepository {

    LiveData<Location> getCurrentLocation();

    void instantiateFusedProviderLocationClient (Context context);

    void configureLocationRequest();

    void configureLocationCallback();

    void updateLocation(Context context);

    void stopLocationUpdates();
}
