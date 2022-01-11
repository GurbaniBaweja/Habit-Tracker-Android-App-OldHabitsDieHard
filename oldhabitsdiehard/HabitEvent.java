/*
 *  HabitEvent
 *
 *  Version 2.0
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

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * This is a class representing a Habit Event.
 *
 * @author Filippo Ciandy
 * @author Claire Martin
 */
public class HabitEvent implements Serializable {
    private String habit; //required
    private String comment;
    private String image;
    private int day;
    private int month;
    private int year;
    private double lat;
    private double lon;
    private boolean hasLocation;

    /**
     * Empty constructor for Firestore Compatibility.
     */
    public HabitEvent() {}

    /**
     * Constructor having all attributes specified.
     * @param habit the name of the habit that this event belongs to
     * @param comment a description of the event
     * @param image an optional image of the event
     * @param date the date the event occurred
     * @param location the location where the event occurred
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    HabitEvent(String habit, String comment, String image, LocalDate date, LatLng location){
        setHabit(habit);
        setComment(comment);
        setImage(image);
        setDate(date);
        setLocation(location);
    }

    /**
     * Constructor without comment specified
     * @param habit the name of the habit that this event belongs to
     * @param image an optional image of the event
     * @param date the date the event occurred
     * @param location the location where the event occurred
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    HabitEvent(String habit, String image, LocalDate date, LatLng location){
        setHabit(habit);
        setImage(image);
        setDate(date);
        setLocation(location);
    }


    /**
     * Constructor without image specified
     * @param habit the name of the habit that this event belongs to
     * @param comment a description of the event
     * @param img a string that references the image in Firebase Storage
     * //@param location of the Habit event
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    HabitEvent(String habit, String comment, LocalDate date, String img){
        setHabit(habit);
        setComment(comment);
        setDate(date);
        setImage(img);
        hasLocation = false;
    }

    /**
     * Constructor without comment and image specified
     * @param habit the name of the habit that this event belongs to
     * @param date the date the event occurred
     * @param location the location where the event occurred
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    HabitEvent(String habit, LocalDate date, LatLng location){
        setHabit(habit);
        setDate(date);
        setLocation(location);
    }

    /**
     * Constructor with only habit name, comment, and date specified.
     * @param habit the name of the habit that this event belongs to
     * @param comment a description of the event
     * @param date the date the event occurred
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    HabitEvent(String habit, String comment, LocalDate date) {
        setHabit(habit);
        setDate(date);
        setComment(comment);
        hasLocation = false;
    }

    /**
     * Getter for habit name.
     * @return the name of the habit this event belongs to
     */
    public String getHabit(){
        return habit;
    }

    /**
     * Getter for comment.
     * @return comment of the Habit event
     */
    public String getComment(){
        return comment;
    }

    /**
     * Getter for image.
     * @return image of the Habit event
     */
    public String getImage(){
        return image;
    }

    /**
     * Gets the day of month for this event.
     * @return the day
     */
    public int getDay() {
        return day;
    }

    /**
     * Gets the month for this event.
     * @return the month
     */
    public int getMonth() {
        return month;
    }

    /**
     * Gets the year for this event.
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * Getter for latitude.
     * @return latitude of Habit event
     */
    public double getLat() {
        return lat;
    }

    /**
     * Getter for longitude.
     * @return longitude of Habit event
     */
    public double getLon() {
        return lon;
    }

    /**
     * Getter for whether the habit event has a location.
     * @return true if the habit event has a location, false if not
     */
    public boolean getHasLocation() {
        return hasLocation;
    }

    /**
     * Setter for habit.
     * @param habit the title of the habit this event belongs to
     */
    public void setHabit(String habit) {
        this.habit = habit;
    }

    /**
     * Setter for comment.
     * @param comment for Habit event
     */
    public void setComment(String comment){
        if (comment.length()>20){
            comment = comment.substring(0,20);
        }
        this.comment = comment;
    }

    /**
     * Setter for image.
     * @param img the reference string used to get the image from firebase
     *            storage
     */
    public void setImage(String img) {
        image = img;
    }

    /**
     * Setter for date.
     * @param date for Habit event
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setDate(LocalDate date){
        if (date == null){
            setDay(LocalDate.now().getDayOfMonth());
            setMonth(LocalDate.now().getMonthValue());
            setYear(LocalDate.now().getYear());
        }
        else {
            setDay(date.getDayOfMonth());
            setMonth(date.getMonthValue());
            setYear(date.getYear());
        }
    }

    /**
     * Sets the day for this event.
     * @param day the day of month
     */
    public void setDay(int day) {
        this.day = day;
    }

    /**
     * Sets the month for this event.
     * @param month the month
     */
    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * Sets the year for this event.
     * @param year the year
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Setter for location
     * @param location where this habit event occurred
     */
    public void setLocation(LatLng location) {
        this.lat = location.latitude;
        this.lon = location.longitude;
        hasLocation = true;
    }

    /**
     * Sets whether this habit event has a location.
     * @param b true if the habit event has a location, false if not
     */
    public void setHasLocation(boolean b) {
        hasLocation = b;
    }

}
