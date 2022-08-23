package utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.anne.linger.go4lunch.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import ui.activities.AuthenticationActivity;

/**
*Util class to manage notifications
*/

@AndroidEntryPoint
public class NotificationReceiver extends BroadcastReceiver {

    private final int NOTIFICATION_ID = 007;
    private final String NOTIFICATION_TAG = "Go4Lunch";
    private Context context;


    @Inject
    public NotificationReceiver(){
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Anne", "Notif");
        this.context = context;
        //TODO implement this method
        //getUserPreferences();
        sendVisualNotification();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void sendVisualNotification() {
        // Create an Intent that will be shown when user will click on the Notification
        Intent intent = new Intent(context, AuthenticationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Create a Channel (Android 8)
        String channelId = context.getString(R.string.default_notification_channel_id);

        // Build notification
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.drawable.ic_baseline_dinner_dining_24)
                        .setContentTitle(context.getString(R.string.notification_title))
                        //TODO manage content text
                        .setContentText(context.getString(R.string.notification_title))
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
