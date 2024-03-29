package data;

import model.autocompletepojo.AutocompleteResponse;
import model.nearbysearchpojo.NearbySearchResponse;
import model.placedetailspojo.PlaceDetailsResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interface to get data from Google places API
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

    @GET("autocomplete/json?")
    Call<AutocompleteResponse> getAutocompleteResponse(
            @Query("input") String input,
            @Query("location") String location,
            @Query("type") String type,
            @Query("key") String key);
}
