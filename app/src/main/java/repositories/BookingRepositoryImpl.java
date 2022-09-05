package repositories;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.anne.linger.go4lunch.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import model.Booking;
import ui.activities.SettingsActivity;
import utils.NotificationReceiver;

/**
*Implementation of BookingRepository interface
*/

public class BookingRepositoryImpl implements BookingRepository {

    //For data
    private FirebaseFirestore mFirestore;
    private final MutableLiveData<List<Booking>> mBookingList = new MutableLiveData<>();
    private static final String BOOKING_COLLECTION = "Bookings";
    private static final String BOOKING_ID = "bookingId";
    private static final String PLACE_ID = "placeId";
    private static final String PLACE_NAME = "placeName";
    private static final String USER = "user";
    private static final String BOOKING_DATE = "bookingDate";
    public static final int ALARM_TYPE_RTC = 100;

    @Inject
    public BookingRepositoryImpl(){
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
            if(error!=null){
                Log.e("Anne", "collectionError");
                return;
            }
            if (value!=null) {
                Log.e("Anne", "getCollectionOK");
                Log.e("Anne", value.toString());
                List<Booking> bookingList = value.toObjects(Booking.class);
                mBookingList.setValue(bookingList);
                Log.e("Anne", mBookingList.toString());
            }
            else{
                Log.e("Anne", "collectionValueNull");
            }
        });
        /**mFirestore.collection(BOOKING_COLLECTION).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null) {
                    Log.e("Anne", "getCollectionError");
                    return;
                }

                List<Booking> bookings = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : value){
                    Booking booking = new Booking();
                    if(documentSnapshot.get("User") != null) {
                        booking.setUser(documentSnapshot.getString("User"));
                    }
                    if(documentSnapshot.get("BookingId") != null) {
                        booking.setBookingId(documentSnapshot.getString("BookingId"));
                    }
                    if(documentSnapshot.get("PlaceId") != null) {
                        booking.setPlaceId(documentSnapshot.getString("PlaceId"));
                    }
                    bookings.add(booking);
                }
                Log.e("Anne", "queryListenerRepo : bookings : " + bookings.toString());
                mBookingList.setValue(bookings);
            }
        });*/
    }

    @Override
    public void createBooking(String placeId, String placeName, String user, Context context) {

        instanceFirestore();

        //Create a new booking
        Map<String, Object> newBooking = new HashMap<>();
        newBooking.put(BOOKING_ID, null);
        newBooking.put(PLACE_ID, placeId);
        newBooking.put(PLACE_NAME, placeName);
        newBooking.put(USER, user);
        newBooking.put(BOOKING_DATE, FieldValue.serverTimestamp());

        //Create a new document
        CollectionReference bookingCollection = mFirestore.collection(BOOKING_COLLECTION);
        bookingCollection
                .add(newBooking)
                .addOnSuccessListener(documentReference -> mFirestore.collection(BOOKING_COLLECTION)
                        .document(documentReference.getId())
                        .update(BOOKING_ID, documentReference.getId())
                        .addOnSuccessListener(unused -> {
                            setNotificationTimeToSend(context);
                            Log.e("Anne", "setCollectionOnSuccess");
                        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Anne", "setCollectionOnFailure");
                    }
                }));
    }

    @Override
    public void updateBooking() {
    }

    @Override
    public void deleteBooking(Booking booking) {
        Log.e("Anne", booking.getBookingId());
        mFirestore.collection(BOOKING_COLLECTION).document(booking.getBookingId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e("Anne", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Anne", "Error deleting document", e);
                    }
                });
    }

    @SuppressLint("MissingPermission")
    private void setNotificationTimeToSend(Context context) {
        Log.e("Anne", "intentForNotif");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 02);
        calendar.set(Calendar.SECOND, 1);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ALARM_TYPE_RTC, alarmIntent, PendingIntent.FLAG_IMMUTABLE);
        //@SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, alarmIntent, PendingIntent.FLAG_ONE_SHOT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmManager.INTERVAL_DAY, pendingIntent);

        /**Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY,12);

        //Setting intent to class where Alarm broadcast message will be handled
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        //Setting alarm pending intent
        alarmIntentRTC = PendingIntent.getBroadcast(mContext, ALARM_TYPE_RTC, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //getting instance of AlarmManager service
        alarmManagerRTC = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);

        alarmManagerRTC.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntentRTC);*/
    }
}
