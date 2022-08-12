package viewmodel;

import androidx.lifecycle.LiveData;
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

    public LiveData<List<Booking>> getBookingListLiveData() {
        mBookingRepositoryImpl.instanceFirestore();
        //mBookingRepositoryImpl.getBookingListFromFirestore();
        return mBookingRepositoryImpl.getBookingListLiveData();
    }

    public void createBooking(int bookingId, String placeId, List<String> userList) {
        mBookingRepositoryImpl.instanceFirestore();
        mBookingRepositoryImpl.createBooking(bookingId, placeId, userList);
    }

    public void updateBooking() {
        mBookingRepositoryImpl.updateBooking();
    }

    public void deleteBooking(Booking booking) {
        mBookingRepositoryImpl.deleteBooking(booking);
    }

}

