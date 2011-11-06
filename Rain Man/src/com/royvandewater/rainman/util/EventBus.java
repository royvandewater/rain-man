package com.royvandewater.rainman.util;

import java.io.Serializable;
import java.util.ArrayList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;

public class EventBus
{
    public static final String EVENT_DATA = "data";
    public static final String EVENT_NAME = "name";
    
    private static final EventBus eventBus = new EventBus();
    
    private final ArrayList<Handler> handlers = new ArrayList<Handler>();
    
    private EventBus() {} // declare the constructor private to force using obtain

    public static EventBus obtain() {
        return eventBus;
    }
    
    public void registerHandler(Handler handler)
    {
        handlers.add(handler);
    }
    
    
    public void sendMessage(String eventName, Serializable serializable)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EVENT_DATA, serializable);

        sendMessage(eventName, bundle);
    }

    public void sendMessage(String eventName, Parcelable parcelable)
    {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EVENT_DATA, parcelable);

        sendMessage(eventName, bundle);
    }

    public void sendMessage(String eventName, String string)
    {
        Bundle bundle = new Bundle();
        bundle.putString(EVENT_DATA, string);

        sendMessage(eventName, bundle);
    }
    
    public void sendMessage(String eventName, Bundle bundle)
    {
        bundle.putString(EVENT_NAME, eventName);

        for (Handler handler : handlers) {
            Message message = Message.obtain();
            message.setData(bundle);

            handler.sendMessage(message);
        }

    }
}