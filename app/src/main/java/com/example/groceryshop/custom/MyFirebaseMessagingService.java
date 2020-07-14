package com.example.groceryshop.custom;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.groceryshop.Activities.MainActivity;
import com.example.groceryshop.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String s) {
        Log.d("onNewToken"," "+s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        if(remoteMessage.getData().size()>0)
        {
            Log.d("remoteMessage"," "+remoteMessage.getData());
            showNotification(remoteMessage.getData().get("title"),remoteMessage.getData().get("Message"));
            Log.d("notification:"," title="+remoteMessage.getData().get("title")+" message="+
                    remoteMessage.getData().get("Message"));
        }
        if(remoteMessage.getNotification()!=null)
        {
            Log.d("remoteMessage notif"," "+remoteMessage.getNotification());
            showNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
        }
    }
    private RemoteViews getCustomDesign(String title,String Message)
    {
        RemoteViews remoteViews=new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification);
        remoteViews.setTextViewText(R.id.title,title);
        remoteViews.setTextViewText(R.id.message,Message);

        return remoteViews;


    }
    public void showNotification(String title,String Message)
    {

        Intent intent=new Intent(this, MainActivity.class);
        String channel_id="web_app_channel";
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(getApplicationContext(),channel_id)
                .setSmallIcon(R.drawable.groceries)

                .setSound(uri)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000,1000,1000,1000,1000})
                 .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN)
        {
            Log.d("versionmorebelly"," ");
            builder= builder.setContent(getCustomDesign(title,Message));
        }else
        {
            Log.d("versionbelly"," ");
            builder=builder.setContentTitle(title)
                    .setContentText(Message)
                    .setSmallIcon(R.drawable.groceries);
        }

        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel=new NotificationChannel(channel_id,"web_app",NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setSound(uri,null);
            notificationManager.createNotificationChannel(notificationChannel);

        }
        notificationManager.notify(0,builder.build());
    }
}

