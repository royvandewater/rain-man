package com.royvandewater.rainman.views;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.royvandewater.rainman.R;
import com.royvandewater.rainman.RainManActivity;

public class WeatherNotification
{
    public final static int ICON = R.drawable.icon;
    private static final int NOTIFICATION_ID = 1;
    public final static String TITLE = "RainMan";
    private NotificationManager notificationManager;
    private Activity activity;

    public WeatherNotification(Activity activity)
    {
        this.activity = activity;
    }

    public void initialize()
    {
        notificationManager = (NotificationManager)activity.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }

    public void displayWeather(String weather)
    {
        long when = System.currentTimeMillis();

        Notification notification = new Notification(ICON, weather, when);

        Intent notificationIntent = new Intent(this.activity, RainManActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this.activity, 0, notificationIntent, 0);

        notification.setLatestEventInfo(this.activity, TITLE, weather, contentIntent);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

}
