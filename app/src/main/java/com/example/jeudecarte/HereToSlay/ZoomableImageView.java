package com.example.jeudecarte.HereToSlay;

import static com.example.jeudecarte.HereToSlay.view.HereToSlay.GENERIC;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * A class that inherit the image view one to permit the user to click on images
 * so the text inside the image appear bigger
 */
public class ZoomableImageView extends AppCompatImageView {
    //Attributes

    /**
     * The tag showed in the logcat console
     */
    private static final String TAG = GENERIC + "ZOOMABLE";

    /**
     * The text view that should contains the description
     */
    private TextView zoomText;

    /**
     * The image's description
     */
    private String description;


    //Setters

    /**
     * Set the description to the specified string
     *
     * @param description the image's description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Set the zoomText text view to the specified element
     *
     * @param zoomText the text view that should contains the image's description
     */
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

    /**
     * Called when the image is pressed or released
     * Make the zoomText text view appear and disappear
     *
     * @param event The motion event.
     */
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

    /**
     *  Because we call this from onTouchEvent, this code will be executed for both
     *  normal touch events and for when the system calls this using Accessibility
     */
    @Override
    public boolean performClick() {
        super.performClick();
        return false;
    }
}
