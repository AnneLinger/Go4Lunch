package repositories;

import android.util.Log;
import android.util.LruCache;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.anne.linger.go4lunch.BuildConfig;

import java.util.List;

import javax.inject.Inject;

import data.PlacesApi;
import model.nearbysearchpojo.NearbySearchResponse;
import model.nearbysearchpojo.Result;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.RetrofitBuilder;

/**
*Implementation of NearbySearchRepository interface
*/

public class NearbySearchRepositoryImpl implements NearbySearchRepository {

    private static final String GOOGLE_PLACE_API_KEY = BuildConfig.MAPS_API_KEY;
    private final int radius = 10000;
    final MutableLiveData<List<Result>> mNearbySearchResponseLiveData = new MutableLiveData<>();
    private final RetrofitBuilder mRetrofitBuilder = new RetrofitBuilder();
    //To limit Google API queries
    private final LruCache<String, NearbySearchResponse> mCache = new LruCache<>(2000);

    @Inject
    public NearbySearchRepositoryImpl() {
    }

    @Override
    public LiveData<List<Result>> getNearbySearchResponseLiveData() {
        //Log.d("Anne", "getRepo");
        return mNearbySearchResponseLiveData;
    }

    @Override
    public void fetchNearbySearchPlaces(String location) {
        Log.e("Anne", "fetchRepo");

        NearbySearchResponse existing = mCache.get("nearbySearch");

        if(existing!=null){
            Log.e("Anne", "ExistingIsNotNull");
            mNearbySearchResponseLiveData.setValue(existing.getResults());
        }
        else {
            Log.e("Anne", "ExistingIsNull");
            PlacesApi placesApi = mRetrofitBuilder.buildRetrofit();
            String type = "restaurant";
            Call<NearbySearchResponse> call = placesApi.getNearbySearchResponse(location, radius, type, GOOGLE_PLACE_API_KEY);
            call.enqueue(new Callback<NearbySearchResponse>() {
                @Override
                public void onResponse(@NonNull Call<NearbySearchResponse> call, @NonNull Response<NearbySearchResponse> response) {
                    /**if(response.body().getResults().isEmpty()) {
                     Log.d("Anne", "response=null");
                     }
                     else {
                     Log.d("Anne", "responseOk");*/
                    assert response.body() != null;
                    Log.e("Anne", response.body().getResults().toString());
                    assert response.body() != null;
                    mCache.put("nearbySearch", response.body());
                    mNearbySearchResponseLiveData.setValue(response.body().getResults());
                }

                @Override
                public void onFailure(@NonNull Call<NearbySearchResponse> call, @NonNull Throwable t) {
                }
            });
        }
    }
}
