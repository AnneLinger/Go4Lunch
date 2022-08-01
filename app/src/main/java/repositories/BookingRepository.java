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

    public void instanceFirestore();

    public LiveData<List<Booking>> getBookingListLiveData();

    public void getBookingListFromFirestore();

    public void createBooking(int bookingId, String placeId, List<FirebaseUser> userList);

    public void updateBooking();

    public void deleteBooking(Booking booking);
}
