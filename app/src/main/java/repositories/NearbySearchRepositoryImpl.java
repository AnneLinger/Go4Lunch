package repositories;

import com.anne.linger.go4lunch.BuildConfig;

import javax.inject.Inject;

import data.PlacesApi;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
*Implementation of NearbySearchRepository interface
*/

public class NearbySearchRepositoryImpl implements NearbySearchRepository {

    public  static final String GOOGLE_PLACE_API_KEY = BuildConfig.MAPS_API_KEY;
    public static String base_url = "https://maps.googleapis.com/maps/api/";



    @Inject
    public NearbySearchRepositoryImpl() {
    }

    final Retrofit mRetrofit = new Retrofit.Builder()
            .baseUrl(base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    final PlacesApi mPlacesApi = mRetrofit.create(PlacesApi.class);
}
