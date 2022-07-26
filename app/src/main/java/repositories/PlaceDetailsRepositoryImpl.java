package repositories;

import androidx.lifecycle.LiveData;

import com.anne.linger.go4lunch.BuildConfig;

import java.util.List;

import javax.inject.Inject;

import model.nearbysearchpojo.Result;

/**
*Implementation of NearbySearchRepository interface
*/

public class PlaceDetailsRepositoryImpl implements PlaceDetailsRepository {

    private static final String GOOGLE_PLACE_API_KEY = BuildConfig.MAPS_API_KEY;


    @Inject
    public PlaceDetailsRepositoryImpl(){
    }

    @Override
    public void fetchPlaceDetails(String placeId) {

    }

    @Override
    public LiveData<List<Result>> getPlaceDetailsLiveData() {
        return null;
    }
}
