package com.anne.linger.go4lunch;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import model.Booking;

/**
 * Unit tests for bookings
 */

@RunWith(JUnit4.class)
public class BookingTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final Booking BOOKING_TEST = new Booking("UuA0Up0mmyKxxI0wdIKD", "ChIJLZFf6iOKDkgR1zucO9jHrP0", "La Fontaine du Jerzual", "Anne Linger", 250);
    private final String BOOKING_TEST_ID = "UuA0Up0mmyKxxI0wdIKD";
    private final String BOOKING_TEST_PLACE_ID = "ChIJLZFf6iOKDkgR1zucO9jHrP0";
    private final String BOOKING_TEST_PLACE_NAME = "La Fontaine du Jerzual";
    private final String BOOKING_TEST_USER = "Anne Linger";
    private final int BOOKING_TEST_BOOKING_DAY = 250;

    //------------------------------------For getters-----------------------------------------------
    @Test
    public void getBookingId() {
        String bookingTestId = BOOKING_TEST.getBookingId();
        assertSame(BOOKING_TEST_ID, bookingTestId);
    }

    @Test
    public void getPlaceId() {
        String bookingTestPlaceId = BOOKING_TEST.getPlaceId();
        assertSame(BOOKING_TEST_PLACE_ID, bookingTestPlaceId);
    }

    @Test
    public void getPlaceName() {
        String bookingTestPlaceName = BOOKING_TEST.getPlaceName();
        assertSame(BOOKING_TEST_PLACE_NAME, bookingTestPlaceName);
    }

    @Test
    public void getUser() {
        String bookingTestUser = BOOKING_TEST.getUser();
        assertSame(BOOKING_TEST_USER, bookingTestUser);
    }

    @Test
    public void getBookingDay() {
        int bookingTestDay = BOOKING_TEST.getBookingDay();
        assertEquals(BOOKING_TEST_BOOKING_DAY, bookingTestDay);
    }

    //------------------------------------For setters-----------------------------------------------
    @Test
    public void setBookingId() {
        String newBookingId = "1";
        BOOKING_TEST.setBookingId(newBookingId);
        assertSame(BOOKING_TEST.getBookingId(), newBookingId);
    }

    @Test
    public void setBookingPlaceId() {
        String newBookingPlaceId = "1";
        BOOKING_TEST.setPlaceId(newBookingPlaceId);
        assertSame(BOOKING_TEST.getPlaceId(), newBookingPlaceId);
    }

    @Test
    public void setBookingPlaceName() {
        String newBookingPlaceName = "New Place";
        BOOKING_TEST.setPlaceName(newBookingPlaceName);
        assertSame(BOOKING_TEST.getPlaceName(), newBookingPlaceName);
    }

    @Test
    public void setBookingUser() {
        String newBookingUser = "New User";
        BOOKING_TEST.setUser(newBookingUser);
        assertSame(BOOKING_TEST.getUser(), newBookingUser);
    }

    @Test
    public void setBookingDay() {
        int newBookingDay = 1;
        BOOKING_TEST.setBookingDay(newBookingDay);
        assertEquals(BOOKING_TEST.getBookingDay(), newBookingDay);
    }
}
