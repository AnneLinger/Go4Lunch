package repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.anne.linger.go4lunch.BuildConfig;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import javax.inject.Inject;

import data.PlacesApi;
import model.Booking;
import model.placedetailspojo.PlaceDetailsResponse;
import model.placedetailspojo.Result;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.RetrofitBuilder;

/**
*Implementation of PlaceDetailsRepository interface
*/

public class PlaceDetailsRepositoryImpl implements PlaceDetailsRepository {

    //For data
    private static final String GOOGLE_PLACE_API_KEY = BuildConfig.MAPS_API_KEY;
    private static final String FIELDS = "name,formatted_address,geometry/location,vicinity,place_id,photo,opening_hours/open_now,rating,website,international_phone_number,formatted_phone_number";
    private final MutableLiveData<Result> mPlaceDetailsLiveData = new MutableLiveData<>();
    private final RetrofitBuilder mRetrofitBuilder = new RetrofitBuilder();

    //Constructor
    @Inject
    public PlaceDetailsRepositoryImpl(){
    }

    @Override
    public LiveData<Result> getPlaceDetailsLiveData() {
        Log.e("Anne", "getPDRepo");
        return mPlaceDetailsLiveData;
    }

    @Override
    public void fetchPlaceDetails(String placeId) {
        Log.e("Anne", "fetchPDRepo");
        PlacesApi placesApi = mRetrofitBuilder.buildRetrofit();
        Call<PlaceDetailsResponse> call = placesApi.getPlaceDetailsResponse(FIELDS, placeId, GOOGLE_PLACE_API_KEY);
        call.enqueue(new Callback<PlaceDetailsResponse>() {
            @Override
            public void onResponse(@NonNull Call<PlaceDetailsResponse> call, @NonNull Response<PlaceDetailsResponse> response) {
                Log.e("Anne", "fetchPDRepoResponse");
                Log.e("Anne", response.body().getResult().getPlaceId());
                if(response.body() !=null){
                    Log.e("Anne", "fetchPDRepoResponseok");
                }
                else{
                    Log.e("Anne", "fetchPDRepoResponsenull");
                }
                assert response.body() != null;
                mPlaceDetailsLiveData.setValue(response.body().getResult());
                if(mPlaceDetailsLiveData!=null) {
                    Log.e("Anne", "responsePDLDok");
                }
                else {
                    Log.e("Anne", "responsePDLDnull");
                }
            }

            @Override
            public void onFailure(@NonNull Call<PlaceDetailsResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
