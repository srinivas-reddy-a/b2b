package com.arraykart.b2b.Home;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

import android.app.PendingIntent;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

import com.arraykart.b2b.Home.TechnicalName.TechnicalNameActivity;
import com.arraykart.b2b.Order.OrderActivity;
import com.arraykart.b2b.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class NotificationService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        notify(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
    }

    public void notify(String title, String message) {
        //add this line instead of 0(zero) as request code
        int requestID = (int) System.currentTimeMillis();
        // After tapping notification navigate to cart fragment
        // Create an Intent for the activity you want to start
        Intent intent = new Intent(this, TechnicalNameActivity.class);
//        intent.putExtra("fragment", "cart");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);
        // Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(requestID,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notification_channel")
                .setContentIntent(resultPendingIntent)
                .setSmallIcon(R.drawable.company_icon_green)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true);


        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(123, builder.build());
    }
}