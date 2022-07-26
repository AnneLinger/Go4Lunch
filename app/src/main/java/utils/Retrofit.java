package utils;

import android.location.Location;
import android.util.Log;

import com.anne.linger.go4lunch.BuildConfig;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.annotation.Nullable;

import data.PlacesApi;
import model.Place;
import model.nearbysearchpojo.NearbySearchResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

/**
*Utils class to manages Retrofit builder
 **/

public class Retrofit {

    public static String base_url = "https://maps.googleapis.com/maps/api/place/";
    private retrofit2.Retrofit mRetrofit;

    public PlacesApi buildRetrofit() {
        mRetrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return mRetrofit.create(PlacesApi.class);
    }
}
