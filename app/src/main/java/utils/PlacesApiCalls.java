package utils;

import android.location.Location;
import android.util.Log;

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

    public static void fetchNearbySearchPlaces(Callbacks callbacks, String location, float radius, String type, String key) {
        /**Log.d("Anne", "fetchNearbyInAPI");


        //Weak reference to avoid memory leaks
        final WeakReference<Callbacks> mCallbacksWeakReference = new WeakReference<Callbacks>(callbacks);

        //Instantiate Retrofit
        PlacesApi mPlacesApi = PlacesApi.mRetrofit.create(PlacesApi.class);

        //Create the call
        Call<NearbySearchResponse> call = mPlacesApi.getNearbySearchResponse(location, radius, type, key);

        //Start the call
        call.enqueue(new Callback<NearbySearchResponse>() {
            @Override
            public void onResponse(Call<NearbySearchResponse> call, Response<NearbySearchResponse> response) {
                if(mCallbacksWeakReference.get() != null) {
                    //mCallbacksWeakReference.get().onResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<NearbySearchResponse> call, Throwable t) {
                if(mCallbacksWeakReference.get() != null) {
                    mCallbacksWeakReference.get().onFailure();
                }
            }
        });*/
    }

}
