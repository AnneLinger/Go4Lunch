package repositories;

import androidx.lifecycle.LiveData;

import java.util.List;

import model.nearbysearchpojo.Result;

/**
 *Interface repository for the place details
 */
public interface PlaceDetailsRepository {

    void fetchPlaceDetails(String placeId);

    LiveData<List<Result>> getPlaceDetailsLiveData();
}
