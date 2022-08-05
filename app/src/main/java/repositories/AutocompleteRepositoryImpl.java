package repositories;

import android.util.Log;

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
*Implementation of AutocompleteRepository interface
*/

public class AutocompleteRepositoryImpl implements AutocompleteRepository {

    //For data
    private final MutableLiveData<List<Prediction>> mAutocompleteLiveData = new MutableLiveData<>();
    private static final String GOOGLE_PLACE_API_KEY = BuildConfig.MAPS_API_KEY;
    private static final String TYPE = "restaurant";
    private final RetrofitBuilder mRetrofitBuilder = new RetrofitBuilder();

    //Constructor
    @Inject
    public AutocompleteRepositoryImpl(){
    }

    @Override
    public LiveData<List<Prediction>> getAutocompleteLiveData() {
        return mAutocompleteLiveData;
    }

    @Override
    public void fetchAutocomplete(String query, String location, int radius) {
        Log.e("Anne", "fetchAuto");
        PlacesApi placesApi = mRetrofitBuilder.buildRetrofit();
        Call<AutocompleteResponse> call = placesApi.getAutocompleteResponse(query, location, TYPE, radius, GOOGLE_PLACE_API_KEY);
        call.enqueue(new Callback<AutocompleteResponse>() {
            @Override
            public void onResponse(Call<AutocompleteResponse> call, Response<AutocompleteResponse> response) {
                Log.e("Anne", "fetchAutoResponse");
                mAutocompleteLiveData.setValue(response.body().getPredictions());
            }

            @Override
            public void onFailure(Call<AutocompleteResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void setAutocompleteToNull() {
        mAutocompleteLiveData.setValue(null);
    }
}
