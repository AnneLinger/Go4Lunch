package repositories;

import android.util.LruCache;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.anne.linger.go4lunch.BuildConfig;

import java.util.List;

import javax.inject.Inject;

import data.PlacesApi;
import model.autocompletepojo.AutocompleteResponse;
import model.autocompletepojo.Prediction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.RetrofitBuilder;

/**
 * Implementation of AutocompleteRepository interface
 */

public class AutocompleteRepositoryImpl implements AutocompleteRepository {

    //For data
    final MutableLiveData<List<Prediction>> mAutocompleteLiveData = new MutableLiveData<>();
    private static final String GOOGLE_PLACE_API_KEY = BuildConfig.MAPS_API_KEY;
    private static final String TYPE = "restaurant";
    private final RetrofitBuilder mRetrofitBuilder = new RetrofitBuilder();

    //To limit Google API queries
    private final LruCache<String, AutocompleteResponse> mCache = new LruCache<>(2000);

    //Constructor
    @Inject
    public AutocompleteRepositoryImpl() {
    }

    @Override
    public LiveData<List<Prediction>> getAutocompleteLiveData() {
        return mAutocompleteLiveData;
    }

    @Override
    public void fetchAutocomplete(String query, String location) {
        AutocompleteResponse existing = mCache.get("autocomplete");

        if (existing != null) {
            mAutocompleteLiveData.setValue(existing.getPredictions());
        } else {
            PlacesApi placesApi = mRetrofitBuilder.buildRetrofit();
            Call<AutocompleteResponse> call = placesApi.getAutocompleteResponse(query, location, TYPE, GOOGLE_PLACE_API_KEY);
            call.enqueue(new Callback<AutocompleteResponse>() {
                @Override
                public void onResponse(@NonNull Call<AutocompleteResponse> call, @NonNull Response<AutocompleteResponse> response) {
                    mCache.put("autocomplete", response.body());
                    assert response.body() != null;
                    mAutocompleteLiveData.setValue(response.body().getPredictions());
                }

                @Override
                public void onFailure(@NonNull Call<AutocompleteResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    //To cancel search when user wants to stop it
    @Override
    public void setAutocompleteToNull() {
        mAutocompleteLiveData.setValue(null);
    }
}
