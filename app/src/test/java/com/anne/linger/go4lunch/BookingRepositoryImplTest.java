package com.anne.linger.go4lunch;

import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import model.Booking;
import repositories.BookingRepositoryImpl;

/**
*Unit tests for booking repository
*/

public class BookingRepositoryImplTest {

    //For data
    public List<Booking> mBookingList = new ArrayList<>();
    public List<Booking> mExpectedBookingList = new ArrayList<>();
    public Booking mBooking = mock(Booking.class);

    //For repository
    public BookingRepositoryImpl mBookingRepository = new BookingRepositoryImpl();

}
