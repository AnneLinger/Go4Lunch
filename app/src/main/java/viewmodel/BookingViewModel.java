package viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import javax.inject.Inject;

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
    @Inject
    public BookingViewModel(BookingRepositoryImpl bookingRepositoryImpl) {
        mBookingRepositoryImpl = bookingRepositoryImpl;
    }

    public void fetchBookingList() {
        mBookingRepositoryImpl.instanceFirestore();
        mBookingRepositoryImpl.getBookingListFromFirestore();
    }

    public LiveData<List<Booking>> getBookingListLiveData() {
        return mBookingRepositoryImpl.getBookingListLiveData();
    }

    public void createBooking(String bookingId, String placeId, String user) {
        mBookingRepositoryImpl.instanceFirestore();
        mBookingRepositoryImpl.createBooking(bookingId, placeId, user);
    }

    public void updateBooking() {
        mBookingRepositoryImpl.updateBooking();
    }

    public void deleteBooking(Booking booking) {
        mBookingRepositoryImpl.deleteBooking(booking);
    }

}

