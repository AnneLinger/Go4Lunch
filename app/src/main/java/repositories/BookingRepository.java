package repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

import model.Booking;

/**
 * Interface repository for bookings
 */

public interface BookingRepository {

    void instanceFirestore();

    LiveData<List<Booking>> getBookingListLiveData();

    void getBookingListFromFirestore();

    void createBooking(String placeId, String placeName, String user, Context context);

    void deleteBooking(Booking booking);

    void deletePreviousBookings();
}
