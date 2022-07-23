package repositories;

import androidx.lifecycle.LiveData;

import java.util.List;

import model.nearbysearchpojo.NearbySearchResponse;
import model.nearbysearchpojo.Result;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *Interface repository for the nearby search places
 */

public interface NearbySearchRepository {
    void buildRetrofit();

    void fetchNearbySearchPlaces(String location);

    LiveData<List<Result>> getNearbySearchResponseLiveData();
}
