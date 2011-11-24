package com.royvandewater.rainman.tasks;

import android.os.AsyncTask;
import com.royvandewater.rainman.util.EventBus;

public class PollTask extends AsyncTask<String, Void, Void>
{
    
    private static EventBus eventBus = EventBus.obtain();
    private long interval;
    private String eventName;
    
    /**
     * @param interval in milliseconds
     * @param event to fire
     */
    public PollTask(long interval, String eventName) {
        this.interval = interval;
        this.eventName = eventName;
    }

    @Override
    protected Void doInBackground(String... params)
    {
        
        while(!isCancelled())
        {
            publishProgress();
            try {
                Thread.sleep(1000 * 60 * interval);
            } catch (InterruptedException e) {
                return null;
            }
        }
        return null;
    }
    
    @Override
    protected void onProgressUpdate(Void... values)
    {
        eventBus.sendMessage(eventName, eventName);
    }
}