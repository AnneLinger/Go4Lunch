package utils;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.anne.linger.go4lunch.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.FileReader;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import model.Booking;
import model.User;
import ui.activities.AuthenticationActivity;
import viewmodel.AutocompleteViewModel;
import viewmodel.BookingViewModel;
import viewmodel.PlacesViewModel;
import viewmodel.UserViewModel;

/**
*Util class to manage notifications
*/

@AndroidEntryPoint
public class NotificationReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 007;
    private static final String NOTIFICATION_TAG = "Go4Lunch";
    private static final String USER_COLLECTION = "Users";
    private static final String BOOKING_COLLECTION = "Bookings";
    private List<User> mUserList;
    private List<Booking> mBookingList;
    private User mUser;
    private Booking mUserBooking;
    private Context context;
    private BookingViewModel mBookingViewModel;
    private UserViewModel mUserViewModel;
    private SharedPreferences mSharedPreferences;
    private String placeName;
    private String placeAddress;
    private String joiningWorkmates;
    private String notificationText;

    @Inject
    public NotificationReceiver(){
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Anne", "Notif");
        this.context = context;
        getUserBookingFromSharedPreferences();
        //getUserListFromFirestore();
        //getBookingListFromFirestore();
        //getUserBooking();
        updateNotificationText();
        sendVisualNotification();
    }

    private void getUserBookingFromSharedPreferences() {
        mSharedPreferences = context.getSharedPreferences(context.getString(R.string.user_booking), Context.MODE_PRIVATE);
        placeName = mSharedPreferences.getString("PlaceName", null);
        placeAddress = mSharedPreferences.getString("PlaceAddress", null);
        joiningWorkmates = mSharedPreferences.getString("JoiningWorkmates", null);
    }

    private void updateNotificationText() {
        String notificationTextStart = "Your lunch will be in the place " + placeName + " at this address : " + placeAddress;
        if(joiningWorkmates!=null){
            notificationText = notificationTextStart + " with " + joiningWorkmates;
        }
        else {
            notificationText = notificationTextStart + " alone.";
        }
    }

   /**private void configureViewModels() {
        mUserViewModel = new ViewModelProvider(this.get(UserViewModel.class);
        mBookingViewModel = new ViewModelProvider(this.get(BookingViewModel.class);
    }*/

    private void getUserListFromFirestore() {
        Log.e("Anne", "Notif : getUserList");
        FirebaseFirestore.getInstance()
                .collection(USER_COLLECTION)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    mUserList.addAll(queryDocumentSnapshots.toObjects(User.class));
                })
                .addOnFailureListener(e -> Log.e("Anne", "getUserListFailedInNotifReceiver"));
    }

    private void getBookingListFromFirestore() {
        Log.e("Anne", "Notif : getBookingList");

        FirebaseFirestore.getInstance().collection(BOOKING_COLLECTION).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    Log.e("Anne", "Notif : taskIsSuccessful");
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        Log.e("Anne", "Notif : getBookingListSucceed " + documentSnapshot.getData());
                        mBookingList.add(documentSnapshot.toObject(Booking.class));
                    }
                }
                else {
                    Log.e("Anne", "Notif : getBookingListFailed " + task.getException());

                }
            }
        });
        /**FirebaseFirestore.getInstance().collection(BOOKING_COLLECTION).addSnapshotListener((value, error) -> {
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

        FirebaseFirestore.getInstance()
                .collection(BOOKING_COLLECTION)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    mBookingList.addAll(queryDocumentSnapshots.toObjects(Booking.class));
                    Log.e("Anne", "getBookingsInNotif : " + mBookingList.toString());
                })
                .addOnFailureListener(e -> Log.e("Anne", "getBookingListFailedInNotifReceiver"));*/
    }

    private void getUserBooking(){
        Log.e("Anne", "Notif : getUserBooking");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(mBookingList.isEmpty()){
            Log.e("Anne", "Notif : mBookingListIsEmpty");

        }
        else {
            for(Booking booking : mBookingList){
                if(booking.getUser().equalsIgnoreCase(currentUser.getDisplayName())){
                    mUserBooking = booking;
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void sendVisualNotification() {
        // Create an Intent that will be shown when user will click on the Notification
        Intent intent = new Intent(context, AuthenticationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Create a Channel (Android 8)
        String channelId = context.getString(R.string.default_notification_channel_id);

        // Build notification
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.drawable.ic_baseline_dinner_dining_24)
                        .setContentTitle(context.getString(R.string.notification_title))
                        .setContentText(notificationText)
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Support Version >= Android 8
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Firebase Messages";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        // Show notification
        notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build());
    }
}
