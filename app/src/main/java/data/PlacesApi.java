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

    String base_url = "https://maps.googleapis.com/maps/api/";

    //TODO à mettre ailleurs dans le repo ?
    Retrofit mRetrofit = new Retrofit.Builder()
            .baseUrl(base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("nearbysearch/json?")
    Call<NearbySearchResponse> getNearbySearchResponse(
            @Query("location") String location,
            @Query("radius") float radius,
            @Query("type") String type,
            @Query("key") String key);
}
