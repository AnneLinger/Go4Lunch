package repositories;

import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import model.Booking;
import model.placedetailspojo.Result;

/**
 *Interface repository for bookings
 */

public interface BookingRepository {

    void instanceFirestore();

    LiveData<List<Booking>> getBookingListLiveData();

    void getBookingListFromFirestore();

    void createBooking(int bookingId, String placeId, List<FirebaseUser> userList);

    void updateBooking();

    void deleteBooking(Booking booking);
}
