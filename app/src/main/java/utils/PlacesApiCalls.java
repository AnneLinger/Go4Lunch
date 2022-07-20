package utils;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.annotation.Nullable;

import data.PlacesApi;
import model.Place;
import model.nearbysearchpojo.NearbySearchResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
*Utils class to manages calls for Retrofit
 **/

public class PlacesApiCalls {

    public interface Callbacks {
        void onResponse(@Nullable List<NearbySearchResponse> nearbySearchResponseList);
        void onFailure();
    }

    public static void fetchNearbySearchPlaces(Callbacks callbacks, String location, int radius, String type, String key) {

        //Weak reference to avoid memory leaks
        final WeakReference<Callbacks> mCallbacksWeakReference = new WeakReference<Callbacks>(callbacks);

        //Instantiate Retrofit
        PlacesApi mPlacesApi = PlacesApi.mRetrofit.create(PlacesApi.class);

        //Create the call
        Call<List<NearbySearchResponse>> call = mPlacesApi.getNearbySearchResponse(location, radius, type, key);

        //Start the call
        call.enqueue(new Callback<List<NearbySearchResponse>>() {
            @Override
            public void onResponse(Call<List<NearbySearchResponse>> call, Response<List<NearbySearchResponse>> response) {
                if(mCallbacksWeakReference.get() != null) {
                    mCallbacksWeakReference.get().onResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<NearbySearchResponse>> call, Throwable t) {
                if(mCallbacksWeakReference.get() != null) {
                    mCallbacksWeakReference.get().onFailure();
                }
            }
        });
    }

}
