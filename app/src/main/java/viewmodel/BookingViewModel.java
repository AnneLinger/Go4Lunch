package viewmodel;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import model.Booking;
import repositories.BookingRepositoryImpl;

/**
 * ViewModel for bookings
 */

@RequiresApi(api = Build.VERSION_CODES.M)
@HiltViewModel
public class BookingViewModel extends ViewModel {

    //For data
    private final BookingRepositoryImpl mBookingRepositoryImpl;

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

    public void createBooking(String placeId, String placeName, String user, Context context) {
        mBookingRepositoryImpl.instanceFirestore();
        mBookingRepositoryImpl.createBooking(placeId, placeName, user, context);
    }

    public void deleteBooking(Booking booking) {
        mBookingRepositoryImpl.deleteBooking(booking);
    }

    public void deletePreviousBookings() {
        mBookingRepositoryImpl.deletePreviousBookings();
    }

}

