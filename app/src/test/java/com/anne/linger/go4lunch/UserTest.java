package com.anne.linger.go4lunch;

import static org.junit.Assert.assertSame;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;

import model.User;

/**
 * Unit tests for users
 */

@RunWith(JUnit4.class)
public class UserTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final List<String> LIKED_PLACES_TEST = Arrays.asList("1", "2", "3");
    private final User USER_TEST = new User("tWRsYoSKUBdPanTqErteMY81rOm2", "Stuart bob", "https://lh3.googleusercontent.com/a-/AFdZucpn7ss-Xn4cmBi9b2TYsC4k9LGXN8HFNXQO_6OmwA=s96-c", LIKED_PLACES_TEST);
    private final String USER_TEST_ID = "tWRsYoSKUBdPanTqErteMY81rOm2";
    private final String USER_TEST_NAME = "Stuart bob";
    private final String USER_TEST_PICTURE_URL = "https://lh3.googleusercontent.com/a-/AFdZucpn7ss-Xn4cmBi9b2TYsC4k9LGXN8HFNXQO_6OmwA=s96-c";
    private final List<String> USER_TEST_LIKED_PLACES = LIKED_PLACES_TEST;

    //------------------------------------For getters-----------------------------------------------
    @Test
    public void getUserId() {
        String userTestId = USER_TEST.getUserId();
        assertSame(USER_TEST_ID, userTestId);
    }

    @Test
    public void getUserName() {
        String userTestName = USER_TEST.getName();
        assertSame(USER_TEST_NAME, userTestName);
    }

    @Test
    public void getUserPictureUrl() {
        String userTestPictureUrl = USER_TEST.getPictureUrl();
        assertSame(USER_TEST_PICTURE_URL, userTestPictureUrl);
    }

    @Test
    public void getUserLikedPlaces() {
        List<String> userTestLikedPlaces = USER_TEST.getLikedPlaces();
        assertSame(USER_TEST_LIKED_PLACES, userTestLikedPlaces);
    }

    //------------------------------------For setters-----------------------------------------------
    @Test
    public void setUserId() {
        String newUserId = "1";
        USER_TEST.setUserId(newUserId);
        assertSame(USER_TEST.getUserId(), newUserId);
    }

    @Test
    public void setUserName() {
        String newUserName = "New Name";
        USER_TEST.setName(newUserName);
        assertSame(USER_TEST.getName(), newUserName);
    }

    @Test
    public void setUserPictureUrl() {
        String newUserPictureUrl = "https://newurl.com";
        USER_TEST.setPictureUrl(newUserPictureUrl);
        assertSame(USER_TEST.getPictureUrl(), newUserPictureUrl);
    }

    @Test
    public void setUserLikedPlaces() {
        List<String> newLikedPlaces = Arrays.asList("4", "5");
        USER_TEST.setLikedPlaces(newLikedPlaces);
        assertSame(USER_TEST.getLikedPlaces(), newLikedPlaces);
    }
}
