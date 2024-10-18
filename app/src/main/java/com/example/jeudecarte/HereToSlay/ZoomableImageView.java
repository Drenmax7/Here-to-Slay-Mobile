package com.example.jeudecarte.HereToSlay;

import static com.example.jeudecarte.HereToSlay.view.HereToSlay.GENERIC;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

public class ZoomableImageView extends AppCompatImageView {
    //Attributes

    /**
     * The tag showed in the logcat console
     */
    private static final String TAG = GENERIC + "ZOOMABLE";

    private TextView zoomText;

    private String description;


    //Setters

    public void setDescription(String description) {
        this.description = description;
    }

    public void setZoomText(TextView zoomText) {
        this.zoomText = zoomText;
    }


    //Constructors

    public ZoomableImageView(Context context) {
        super(context);
    }

    public ZoomableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    //Methods

    //Methods

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                zoomText.setText(description);
                zoomText.setVisibility(VISIBLE);
                performClick();
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                zoomText.setVisibility(INVISIBLE);
                return true;
        }
        return false;
    }

    // Because we call this from onTouchEvent, this code will be executed for both
    // normal touch events and for when the system calls this using Accessibility
    @Override
    public boolean performClick() {
        super.performClick();
        return false;
    }

}
