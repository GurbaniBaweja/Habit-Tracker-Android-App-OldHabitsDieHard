/*
 *  MyMapView
 *
 *  Version 1.0
 *
 *  November 28, 2021
 *
 *  Copyright 2021 Rowan Tilroe, Claire Martin, Filippo Ciandy,
 *  Gurbani Baweja, Chanpreet Singh, and Paige Lekach
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.example.oldhabitsdiehard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;

/**
 * Custom MapView class for use in the HabitEventFragment.
 *
 * @author Claire Martin
 */
public class MyMapView extends MapView {
    /**
     * Constructor
     * @param context a context
     */
    public MyMapView(Context context) {
        super(context);
    }

    /**
     * Constructor
     * @param context a context
     * @param attrs attributes to use
     */
    public MyMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Constructor
     * @param context a context
     * @param attrs attributes to use
     * @param defStyle the style to use
     */
    public MyMapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Constructor
     * @param context a context
     * @param options a set of GoogleMapOptions to use
     */
    public MyMapView(Context context, GoogleMapOptions options) {
        super(context, options);
    }

    /**
     * When the map is touched, we relinquish all other touch events (such as
     * those in the parent HabitEventFragment).
     * @param ev the motion event
     * @return the result of the touch event
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // request all parents to relinquish touch events
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }
}
