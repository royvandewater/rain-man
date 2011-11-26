package com.royvandewater.rainman.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import com.royvandewater.rainman.R;
import com.royvandewater.rainman.RainManApplication;
import com.royvandewater.rainman.WeatherService;
import com.royvandewater.rainman.util.EventBus;
import com.royvandewater.rainman.views.RainmanNotification;

public class RainManPreferenceActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener
{
    private final EventBus eventBus = EventBus.obtain();
    private final RainmanNotification notificationManager = new RainmanNotification(this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        notificationManager.initialize();
        
        addPreferencesFromResource(R.xml.preferences);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(this);
        
        Intent intent = new Intent(this, WeatherService.class);
        startService(intent);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        eventBus.sendMessage(RainManApplication.EventName.PREFERENCES_UPDATE.toString(), new Bundle());
    }
}