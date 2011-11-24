package com.royvandewater.rainman.views;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.royvandewater.rainman.R;
import com.royvandewater.rainman.activities.RainManPreferenceActivity;

public class MenuView
{

    private Activity activity;

    public MenuView(Activity activity)
    {
        this.activity = activity;
    }
    
    public void showMenu(Menu menu) {
        MenuInflater inflater = activity.getMenuInflater();
        inflater.inflate(R.menu.rain_man_menu, menu);
    }

    public void select(MenuItem item)
    {
        switch(item.getItemId()) {
            case R.id.settings_menu_option:
                Intent intent = new Intent(activity, RainManPreferenceActivity.class);
                activity.startActivity(intent);
                break;
        }
    }

}
