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
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.anne.linger.go4lunch.R;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import ui.activities.AuthenticationActivity;

/**
 * Util class to manage notifications
 */

@RequiresApi(api = Build.VERSION_CODES.M)
@AndroidEntryPoint
public class NotificationReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 7;
    private static final String NOTIFICATION_TAG = "Go4Lunch";
    private Context context;
    private SharedPreferences mSharedPreferencesBooking;
    private SharedPreferences mSharedPreferencesUser;
    private String placeName;
    private String placeAddress;
    private String joiningWorkmates;
    private String notificationText;
    private boolean notificationChoice;

    @Inject
    public NotificationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        getUserBookingFromSharedPreferences();
        updateNotificationText();
        getNotificationChoiceFromSharedPreferences();
        if (notificationChoice) {
            sendVisualNotification();
        }
    }

    private void getUserBookingFromSharedPreferences() {
        mSharedPreferencesBooking = context.getSharedPreferences(context.getString(R.string.user_booking), Context.MODE_PRIVATE);
        placeName = mSharedPreferencesBooking.getString(context.getString(R.string.notification_place_name_key), null);
        placeAddress = mSharedPreferencesBooking.getString(context.getString(R.string.notification_place_address_key), null);
        joiningWorkmates = mSharedPreferencesBooking.getString(context.getString(R.string.notification_joining_workmates_key), null);
    }

    private void updateNotificationText() {
        String notificationTextStart = context.getString(R.string.notification_first_start) + placeName + context.getString(R.string.notification_second_start) + placeAddress;
        if (joiningWorkmates.length() > 3) {
            notificationText = notificationTextStart + context.getString(R.string.notification_with) + joiningWorkmates;
        } else {
            notificationText = notificationTextStart + context.getString(R.string.notification_alone);
        }
    }

    private void getNotificationChoiceFromSharedPreferences() {
        mSharedPreferencesUser = context.getSharedPreferences(context.getString(R.string.user_settings), Context.MODE_PRIVATE);
        notificationChoice = mSharedPreferencesUser.getBoolean(context.getString(R.string.notifications), true);
    }


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
