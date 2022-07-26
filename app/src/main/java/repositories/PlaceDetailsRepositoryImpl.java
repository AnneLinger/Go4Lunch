package repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.anne.linger.go4lunch.BuildConfig;

import java.util.List;

import javax.inject.Inject;

import data.PlacesApi;
import model.placedetailspojo.PlaceDetailsResponse;
import model.placedetailspojo.Result;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Retrofit;

/**
*Implementation of NearbySearchRepository interface
*/

public class PlaceDetailsRepositoryImpl implements PlaceDetailsRepository {

    private static final String GOOGLE_PLACE_API_KEY = BuildConfig.MAPS_API_KEY;
    private static final String FIELDS = "name,formatted_address,geometry,place_id,photo,opening_hours,rating,website,international_phone_number,type";
    private final MutableLiveData<Result> mPlaceDetailsLiveData = new MutableLiveData<>();
    private final utils.Retrofit mRetrofit = new Retrofit();

    @Inject
    public PlaceDetailsRepositoryImpl(){
    }

    @Override
    public void fetchPlaceDetails(String placeId) {
        PlacesApi placesApi = mRetrofit.buildRetrofit();
        Call<PlaceDetailsResponse> call = placesApi.getPlaceDetailsResponse(FIELDS, placeId, GOOGLE_PLACE_API_KEY);
        call.enqueue(new Callback<PlaceDetailsResponse>() {
            @Override
            public void onResponse(@NonNull Call<PlaceDetailsResponse> call, @NonNull Response<PlaceDetailsResponse> response) {
                assert response.body() != null;
                mPlaceDetailsLiveData.setValue(response.body().getResult());
            }

            @Override
            public void onFailure(@NonNull Call<PlaceDetailsResponse> call, @NonNull Throwable t) {
            }
        });
    }

    @Override
    public LiveData<Result> getPlaceDetailsLiveData() {
        return mPlaceDetailsLiveData;
    }
}
