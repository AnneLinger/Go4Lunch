package repositories;

import android.content.Context;
import android.location.Location;

import androidx.lifecycle.LiveData;

/**
 *Interface repository for the user location
 */
public interface LocationRepository {

    LiveData<Location> getLocationLiveData();

    void instantiateFusedLocationProviderClient(Context context);

    void startLocationRequest(Context context);

    void stopLocationRequest(Context context);




        /**LiveData<Location> getCurrentLocation();

        void instantiateFusedProviderLocationClient (Context context);

        void configureLocationRequest();

        void configureLocationCallback();

        void updateLocation(Context context);

        void stopLocationUpdates();*/
}
