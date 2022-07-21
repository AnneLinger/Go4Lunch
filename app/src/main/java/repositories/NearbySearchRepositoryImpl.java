package repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.anne.linger.go4lunch.BuildConfig;

import java.lang.reflect.Type;
import java.security.Key;
import java.util.List;

import javax.inject.Inject;

import data.PlacesApi;
import model.nearbysearchpojo.NearbySearchResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import utils.PlacesApiCalls;

/**
*Implementation of NearbySearchRepository interface
*/

public class NearbySearchRepositoryImpl implements NearbySearchRepository {

    private static final String GOOGLE_PLACE_API_KEY = BuildConfig.MAPS_API_KEY;
    private String type = "restaurant";
    public static String base_url = "https://maps.googleapis.com/maps/api/";
    private MutableLiveData<NearbySearchResponse> mNearbySearchResponseLiveData = new MutableLiveData<>();

    @Inject
    public NearbySearchRepositoryImpl() {
    }

    public LiveData<NearbySearchResponse> getNearbySearchResponseLiveData() {
        return mNearbySearchResponseLiveData;
    }

    public void fetchNearbySearchPlaces(String location, float radius) {
        //PlacesApiCalls.fetchNearbySearchPlaces(callbacks, location, radius, type, key);

        PlacesApi mPlacesApi = PlacesApi.mRetrofit.create(PlacesApi.class);

        //Create the call
        Call<NearbySearchResponse> call = mPlacesApi.getNearbySearchResponse(location, radius, type, GOOGLE_PLACE_API_KEY);

        //Start the call
        call.enqueue(new Callback<NearbySearchResponse>() {
            @Override
            public void onResponse(Call<NearbySearchResponse> call, Response<NearbySearchResponse> response) {
                mNearbySearchResponseLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<NearbySearchResponse> call, Throwable t) {
                mNearbySearchResponseLiveData.postValue(null);
            }
        });
    }
}
