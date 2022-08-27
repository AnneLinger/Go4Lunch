package repositories;

import android.content.Context;

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

    void createBooking(String placeId, String placeName, String user, Context context);

    void updateBooking();

    void deleteBooking(Booking booking);
}
