package data;

import model.nearbysearchpojo.NearbySearchResponse;
import model.placedetailspojo.PlaceDetailsResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 *Interface to get data from places API
 */


public interface PlacesApi {

    @GET("nearbysearch/json?")
    Call<NearbySearchResponse> getNearbySearchResponse(
            @Query("location") String location,
            @Query("radius") int radius,
            @Query("type") String type,
            @Query("key") String key);

    @GET("details/json?")
    Call<PlaceDetailsResponse> getPlaceDetailsResponse(
            @Query("fields") String fields,
            @Query("place_id") String placeId,
            @Query("key") String key);
}
