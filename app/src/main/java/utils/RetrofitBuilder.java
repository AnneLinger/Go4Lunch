package utils;

import data.PlacesApi;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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
                .client(new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)).build())
                .build();
        return mRetrofit.create(PlacesApi.class);
    }
}
