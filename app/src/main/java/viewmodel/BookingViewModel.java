package viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import dagger.hilt.android.lifecycle.HiltViewModel;
import model.Booking;
import repositories.BookingRepositoryImpl;

/**
*ViewModel for bookings
*/

@HiltViewModel
public class BookingViewModel extends ViewModel {

    //For data
    private final BookingRepositoryImpl mBookingRepositoryImpl;

    //Constructor
    public BookingViewModel(BookingRepositoryImpl bookingRepositoryImpl) {
        mBookingRepositoryImpl = bookingRepositoryImpl;
    }

    public LiveData<List<Booking>> getBookingListLiveData() {
        return mBookingRepositoryImpl.getBookingListLiveData();
    }

    public void getBookingListFromFirestore() {
        mBookingRepositoryImpl.getBookingListFromFirestore();
    }

    public void createBooking(int bookingId, String placeId, List<FirebaseUser> userList) {
        mBookingRepositoryImpl.createBooking(bookingId, placeId, userList);
    }

    public void updateBooking() {
        mBookingRepositoryImpl.updateBooking();
    }

    public void deleteBooking(Booking booking) {
        mBookingRepositoryImpl.deleteBooking(booking);
    }

}

