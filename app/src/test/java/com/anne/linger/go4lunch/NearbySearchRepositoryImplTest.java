package com.anne.linger.go4lunch;

import static org.mockito.Mockito.mock;

import org.junit.Rule;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import model.nearbysearchpojo.NearbySearchResponse;
import model.nearbysearchpojo.Result;

/**
*Unit tests for NearbySearch repository
*/

@RunWith(JUnit4.class)
public class NearbySearchRepositoryImplTest {

    //For data
    public NearbySearchResponse mNearbySearchResponse = mock(NearbySearchResponse.class);
    public List<Result> mResultList;
}
