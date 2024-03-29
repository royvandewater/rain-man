package com.royvandewater.rainman.views;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.royvandewater.rainman.R;
import com.royvandewater.rainman.activities.RainManPreferenceActivity;

public class RainmanNotification
{
    public final static int ICON = R.drawable.icon;
    private static final int NOTIFICATION_ID = 1;
    public final static String TITLE = "RainMan";
    private NotificationManager notificationManager;
    private Context context;

    public RainmanNotification(Context context)
    {
        this.context = context;
    }

    public void initialize()
    {
        notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }
    
    public void displayWeather(String message)
    {
        long when = System.currentTimeMillis();

        Notification notification = new Notification(ICON, message, when);
        notification.defaults = Notification.DEFAULT_ALL;

        Intent notificationIntent = new Intent(this.context, RainManPreferenceActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this.context, 0, notificationIntent, 0);

        notification.setLatestEventInfo(this.context, TITLE, message, contentIntent);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}
