package repositories;

import com.anne.linger.go4lunch.BuildConfig;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
*Implementation of ApiRepository interface
*/

public class ApiRepositoryImpl implements ApiRepository {

    private static Retrofit sRetrofit;
    public  static final String GOOGLE_PLACE_API_KEY = BuildConfig.MAPS_API_KEY;
    public static String base_url = "https://maps.googleapis.com/maps/api/";

    @Inject
    public ApiRepositoryImpl() {
    }

}
