package com.royvandewater.rainman.views;

import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class SeekBarPreference extends DialogPreference implements SeekBar.OnSeekBarChangeListener
{
    private static final String ANDROID_NS = "http://schemas.android.com/apk/res/android";

    private SeekBar seekBar;
    private TextView splashText, valueText;
    private Context context;

    private String dialogMessage, suffix;
    private int defaultValue, maxValue, value = 0;

    public SeekBarPreference(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.context = context;

        this.dialogMessage = attrs.getAttributeValue(ANDROID_NS, "dialogMessage");
        this.suffix = attrs.getAttributeValue(ANDROID_NS, "text");
        this.defaultValue = attrs.getAttributeIntValue(ANDROID_NS, "defaultValue", 0);
        this.maxValue = attrs.getAttributeIntValue(ANDROID_NS, "max", 100);
    }

    @Override
    protected View onCreateDialogView()
    {
        LinearLayout.LayoutParams params;
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(6, 6, 6, 6);

        splashText = new TextView(context);
        if (dialogMessage != null)
            splashText.setText(dialogMessage);
        layout.addView(splashText);

        valueText = new TextView(context);
        valueText.setGravity(Gravity.CENTER_HORIZONTAL);
        valueText.setTextSize(32);
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.addView(valueText, params);

        seekBar = new SeekBar(context);
        seekBar.setOnSeekBarChangeListener(this);
        layout.addView(seekBar, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        if (shouldPersist())
            value = getPersistedInt(defaultValue);

        seekBar.setMax(maxValue);
        seekBar.setProgress(value);
        return layout;
    }

    @Override
    protected void onBindDialogView(View v)
    {
        super.onBindDialogView(v);
        seekBar.setMax(maxValue);
        seekBar.setProgress(value);
    }

    @Override
    protected void onSetInitialValue(boolean restore, Object defaultValue)
    {
        super.onSetInitialValue(restore, defaultValue);
        if (restore)
            value = shouldPersist() ? getPersistedInt(this.defaultValue) : 0;
        else
            value = (Integer)defaultValue;
    }

    public void onProgressChanged(SeekBar seek, int value, boolean fromTouch)
    {
        String t = String.valueOf(value);
        valueText.setText(suffix == null ? t : t.concat(suffix));
    }

    @Override
    public void onClick(DialogInterface dialog, int button)
    {
        if(button == DialogInterface.BUTTON_POSITIVE)
            persistProgress();
    }

    private void persistProgress()
    {
        int progress = getProgress();
        
        if (progress == 0) // Prevent from calling as fast as possible
            progress = 1;
        
        if (shouldPersist())
            persistInt(progress);
        callChangeListener(new Integer(progress));
    }

    public void onStartTrackingTouch(SeekBar seek)
    {
    }

    public void onStopTrackingTouch(SeekBar seek)
    {
    }

    public void setMax(int maxValue)
    {
        this.maxValue = maxValue;
    }
    public int getMax()
    {
        return maxValue;
    }

    public void setProgress(int value)
    {
        this.value = value;
        if (seekBar != null)
            seekBar.setProgress(value);
    }
    public int getProgress()
    {
        if (seekBar != null)
            this.value = seekBar.getProgress();
        
        return value;
    }
}
