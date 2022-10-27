package android.julian.ontracktermscheduler.UI;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.julian.ontracktermscheduler.R;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class Receiver extends BroadcastReceiver {

    String channel = "test";
    static int notificationID;

    private void createNotificationChannel(Context context, String CHANNEL_ID){
        CharSequence name = context.getResources().getString(R.string.channelName);
        String desc = context.getString(R.string.channelDesc);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(desc);

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, intent.getStringExtra("key"),Toast.LENGTH_LONG).show();
        createNotificationChannel(context, channel);

        Notification notification = new NotificationCompat.Builder(context,channel)
                .setSmallIcon((R.drawable.icon))
                .setContentText(intent.getStringExtra("key"))
                .setContentTitle("Test").build();
        NotificationManager notificationManager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID++,notification);

        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

    }
}