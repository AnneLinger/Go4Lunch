package repositories;

import androidx.lifecycle.LiveData;

import java.util.List;

import model.nearbysearchpojo.Result;

/**
 * Interface repository for the nearby search places
 */

public interface NearbySearchRepository {

    void fetchNearbySearchPlaces(String location);

    LiveData<List<Result>> getNearbySearchResponseLiveData();
}
