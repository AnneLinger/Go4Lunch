package com.anne.linger.go4lunch;

//import static org.mockito.Mockito.mock;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;

import java.util.List;

import model.nearbysearchpojo.NearbySearchResponse;
import model.nearbysearchpojo.Result;
import repositories.NearbySearchRepositoryImpl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.RetrofitBuilder;

/**
*Unit tests for NearbySearch repository
*/

@RunWith(JUnit4.class)
public class NearbySearchRepositoryImplTest {

    @Rule
    public InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    //For repository
    @Mock
    public NearbySearchRepositoryImpl mNearbySearchRepository = new NearbySearchRepositoryImpl();

    //For data
    private NearbySearchResponse mNearbySearchResponse = mock(NearbySearchResponse.class);
    private Result mResult = mock(Result.class);
    private List<Result> mResultList;
    private String testLocation = "48.4875332,-2.0271007";
    private final MutableLiveData<List<Result>> resultList = new MutableLiveData<>();
    private RetrofitBuilder mRetrofitBuilder = mock(RetrofitBuilder.class);
    private static final String GOOGLE_PLACE_API_KEY = BuildConfig.MAPS_API_KEY;
    private final int radius = 10000;
    private String type = "restaurant";

    @Before
    public void setUp() {
        mNearbySearchRepository.
        /**mRetrofitBuilder = new RetrofitBuilder();
        Call<NearbySearchResponse> call = mRetrofitBuilder.buildRetrofit().getNearbySearchResponse(testLocation, radius, "Restaurant", GOOGLE_PLACE_API_KEY);
        call.enqueue(new Callback<NearbySearchResponse>() {
            @Override
            public void onResponse(Call<NearbySearchResponse> call, Response<NearbySearchResponse> response) {
                resultList.setValue(response.body().getResults());
            }

            @Override
            public void onFailure(Call<NearbySearchResponse> call, Throwable t) {

            }
        });*/
        //mNearbySearchRepository.fetchNearbySearchPlaces(testLocation);
    }

    @Test
    public void getPlaces() {
        /*mNearbySearchRepository.getNearbySearchResponseLiveData().observeForever(new Observer<List<Result>>() {
            @Override
            public void onChanged(List<Result> results) {
                mResultList = results;
            }
        });*/
        //resultList = mNearbySearchRepository.getNearbySearchResponseLiveData();
        //when(mNearbySearchRepository.getNearbySearchResponseLiveData()).thenReturn(resultList);
        //mNearbySearchRepository.fetchNearbySearchPlaces(testLocation);
        mResultList = getValueForTest(mNearbySearchRepository.getNearbySearchResponseLiveData());

        assertFalse(mResultList.isEmpty());
        //assertNotNull(mResultList);
    }

    public static <T> T getValueForTest(final LiveData<T> liveData) {
        liveData.observeForever(ignored -> {
        });
        return liveData.getValue();
    }

}
