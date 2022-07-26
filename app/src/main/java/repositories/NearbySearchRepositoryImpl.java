package repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.anne.linger.go4lunch.BuildConfig;

import java.util.List;

import javax.inject.Inject;

import data.PlacesApi;
import model.nearbysearchpojo.NearbySearchResponse;
import model.nearbysearchpojo.Result;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;
import utils.Retrofit;

/**
*Implementation of NearbySearchRepository interface
*/

public class NearbySearchRepositoryImpl implements NearbySearchRepository {

    private static final String GOOGLE_PLACE_API_KEY = BuildConfig.MAPS_API_KEY;
    private String type = "restaurant";
    private MutableLiveData<List<Result>> mNearbySearchResponseLiveData = new MutableLiveData<>();
    private PlacesApi mPlacesApi;
    private utils.Retrofit mRetrofit = new Retrofit();

    @Inject
    public NearbySearchRepositoryImpl() {
    }

    @Override
    public LiveData<List<Result>> getNearbySearchResponseLiveData() {
        Log.d("Anne", "getRepo");
        return mNearbySearchResponseLiveData;
    }

    @Override
    public void fetchNearbySearchPlaces(String location, int radius) {
        Log.d("Anne", "fetchRepo");

        mPlacesApi = mRetrofit.buildRetrofit();
        Call<NearbySearchResponse> call = mPlacesApi.getNearbySearchResponse(location, radius, type, GOOGLE_PLACE_API_KEY);
        call.enqueue(new Callback<NearbySearchResponse>() {
            @Override
            public void onResponse(Call<NearbySearchResponse> call, Response<NearbySearchResponse> response) {
                /**if(response.body().getResults().isEmpty()) {
                    Log.d("Anne", "response=null");
                }
                else {
                    Log.d("Anne", "responseOk");*/
                    mNearbySearchResponseLiveData.setValue(response.body().getResults());

            }

            @Override
            public void onFailure(Call<NearbySearchResponse> call, Throwable t) {

            }
        });
        /**mRetrofit.create(PlacesApi.class).getNearbySearchResponse(location, radius, type, GOOGLE_PLACE_API_KEY)
                .enqueue(new Callback<NearbySearchResponse>() {
                    @Override
                    public void onResponse(Call<NearbySearchResponse> call, Response<NearbySearchResponse> response) {
                        if(response.body()!=null) {
                            Log.d("Anne", "onResponse");
                            mNearbySearchResponseLiveData.setValue(response.body().getResults());
                        }
                        else {
                            Log.d("Anne", "response=null");
                        }
                    }

                    @Override
                    public void onFailure(Call<NearbySearchResponse> call, Throwable t) {

                    }
                });*/
    }
}
