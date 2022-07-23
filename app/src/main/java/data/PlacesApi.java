package data;

import android.location.Location;

import java.util.List;

import model.Place;
import model.nearbysearchpojo.NearbySearchResponse;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 *Interface to get data from nearby search API
 */


public interface PlacesApi {

    @GET("nearbysearch/json?")
    Call<NearbySearchResponse> getNearbySearchResponse(
            @Query("location") String location,
            @Query("rankby") String rankBy,
            //@Query("radius") int radius,
            @Query("type") String type,
            @Query("key") String key);
}
