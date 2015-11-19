package com.bison.app.ui.refresh;

/**
 * Created by oliviergoutay on 1/23/15.
 */
public enum SwipeMode {

    TOP(0),
    BOTTOM(1),
    BOTH(2);

    private int mValue;

    SwipeMode(int value) {
        this.mValue = value;
    }

    public static SwipeMode getFromInt(int value) {
        for (SwipeMode mode : SwipeMode.values()) {
            if (mode.mValue == value) {
                return mode;
            }
        }
        return BOTH;
    }

}
