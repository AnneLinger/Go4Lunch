package repositories;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import model.Booking;
import utils.NotificationReceiver;

/**
 * Implementation of BookingRepository interface
 */

@RequiresApi(api = Build.VERSION_CODES.M)
public class BookingRepositoryImpl implements BookingRepository {

    private FirebaseFirestore mFirestore;
    private final MutableLiveData<List<Booking>> mBookingList = new MutableLiveData<>();
    private static final String BOOKING_COLLECTION = "Bookings";
    private static final String BOOKING_ID = "bookingId";
    private static final String PLACE_ID = "placeId";
    private static final String PLACE_NAME = "placeName";
    private static final String USER = "user";
    private static final String BOOKING_DAY = "bookingDay";
    public static final int ALARM_TYPE_RTC = 100;

    @Inject
    public BookingRepositoryImpl() {
    }

    @Override
    public void instanceFirestore() {
        mFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public LiveData<List<Booking>> getBookingListLiveData() {
        return mBookingList;
    }

    @Override
    public void getBookingListFromFirestore() {
        mFirestore.collection(BOOKING_COLLECTION).addSnapshotListener((value, error) -> {
            if (error != null) {
                return;
            }
            if (value != null) {
                List<Booking> bookingList = value.toObjects(Booking.class);
                mBookingList.setValue(bookingList);
            } else {
                Log.e("Anne", "getBookingListFromFirestoreOnFailure");
            }
        });
    }

    @Override
    public void createBooking(String placeId, String placeName, String user, Context context) {
        instanceFirestore();

        //Create a new booking
        Calendar calendar = Calendar.getInstance();
        Map<String, Object> newBooking = new HashMap<>();
        newBooking.put(BOOKING_ID, null);
        newBooking.put(PLACE_ID, placeId);
        newBooking.put(PLACE_NAME, placeName);
        newBooking.put(USER, user);
        newBooking.put(BOOKING_DAY, calendar.get(Calendar.DAY_OF_YEAR));

        //Create a new document
        CollectionReference bookingCollection = mFirestore.collection(BOOKING_COLLECTION);
        bookingCollection
                .add(newBooking)
                .addOnSuccessListener(documentReference -> mFirestore.collection(BOOKING_COLLECTION)
                        .document(documentReference.getId())
                        .update(BOOKING_ID, documentReference.getId())
                        .addOnSuccessListener(unused -> setNotificationTimeToSend(context))
                        .addOnFailureListener(e -> Log.e("Anne", "setCollectionOnFailure")));
    }

    @Override
    public void deleteBooking(Booking booking) {
        mFirestore.collection(BOOKING_COLLECTION).document(booking.getBookingId())
                .delete()
                .addOnSuccessListener(aVoid -> Log.e("Anne", "DocumentSnapshot successfully deleted!"))
                .addOnFailureListener(e -> Log.e("Anne", "Error deleting document", e));
    }

    //To keep only bookings of the current day
    @Override
    public void deletePreviousBookings() {
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_YEAR);
        for (Booking booking : Objects.requireNonNull(mBookingList.getValue())) {
            if (booking.getBookingDay() != currentDay) {
                deleteBooking(booking);
            }
        }
    }

    //To send notification at 12 o'clock
    @SuppressLint("MissingPermission")
    private void setNotificationTimeToSend(Context context) {
        //Determine calendar time
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 47);

        //Manage intent to send notification
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ALARM_TYPE_RTC, alarmIntent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}
