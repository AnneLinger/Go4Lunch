package utils;

import data.PlacesApi;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
*Utils class to manages Retrofit builder
 **/

public class RetrofitBuilder {

    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/";
    private Retrofit mRetrofit;

    public PlacesApi buildRetrofit() {
        mRetrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return mRetrofit.create(PlacesApi.class);
    }
}
