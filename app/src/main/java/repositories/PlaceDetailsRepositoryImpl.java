package repositories;

import android.util.LruCache;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.anne.linger.go4lunch.BuildConfig;

import javax.inject.Inject;

import data.PlacesApi;
import model.placedetailspojo.PlaceDetailsResponse;
import model.placedetailspojo.Result;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.RetrofitBuilder;

/**
 * Implementation of PlaceDetailsRepository interface
 */

public class PlaceDetailsRepositoryImpl implements PlaceDetailsRepository {

    //For data
    private static final String GOOGLE_PLACE_API_KEY = BuildConfig.MAPS_API_KEY;
    private static final String FIELDS = "name,formatted_address,geometry/location,vicinity,place_id,photo,opening_hours/open_now,rating,website,international_phone_number,formatted_phone_number";
    private final MutableLiveData<Result> mPlaceDetailsLiveData = new MutableLiveData<>();
    private final RetrofitBuilder mRetrofitBuilder = new RetrofitBuilder();

    //To limit Google API queries
    private final LruCache<String, PlaceDetailsResponse> mCache = new LruCache<>(2000);

    @Inject
    public PlaceDetailsRepositoryImpl() {
    }

    @Override
    public LiveData<Result> getPlaceDetailsLiveData() {
        return mPlaceDetailsLiveData;
    }

    @Override
    public void fetchPlaceDetails(String placeId) {
        PlaceDetailsResponse existing = mCache.get("placeDetails");

        if (existing != null) {
            mPlaceDetailsLiveData.setValue(existing.getResult());
        } else {
            PlacesApi placesApi = mRetrofitBuilder.buildRetrofit();
            Call<PlaceDetailsResponse> call = placesApi.getPlaceDetailsResponse(FIELDS, placeId, GOOGLE_PLACE_API_KEY);
            call.enqueue(new Callback<PlaceDetailsResponse>() {
                @Override
                public void onResponse(@NonNull Call<PlaceDetailsResponse> call, @NonNull Response<PlaceDetailsResponse> response) {
                    assert response.body() != null;
                    mCache.put("placeDetails", response.body());
                    mPlaceDetailsLiveData.setValue(response.body().getResult());
                }

                @Override
                public void onFailure(@NonNull Call<PlaceDetailsResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }
}
