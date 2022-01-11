/*
 *  Habit
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

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This class represents a habit.
 *
 * @author Claire Martin
 * @author Filippo Ciandy
 */
public class Habit implements Serializable {
    private String title; // required
    private String reason;
    private int day;
    private int month;
    private int year;
    private List<Boolean> weekdays; // default all false
    private boolean isPublic; // default true
    private ArrayList<HabitEvent> habitEvents; // default empty

    /**
     * Empty habit constructor for Firestore compatibility.
     */
    public Habit() {}

    /**
     * Habit constructor specifying all fields.
     * @param title the habit name
     * @param reason the reason for the habit
     * @param startDate the date to start the habit
     * @param weekdays which days of the week the habit should be performed
     * @param isPublic whether this habit is viewable by other users or not
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    Habit(String title, String reason, LocalDate startDate, List<Boolean> weekdays, boolean isPublic) {
        this.weekdays = new ArrayList<Boolean>();
        setTitle(title);
        setReason(reason);
        setStartDate(startDate);
        setWeekdays(weekdays);
        setPublic(isPublic);
        habitEvents = new ArrayList<HabitEvent>();
    }

    /**
     * Habit constructor specifying all fields except publicity.
     * @param title the habit name
     * @param reason the reason for the habit
     * @param startDate the date to start the habit
     * @param weekdays which days of the week the habit should be performed
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    Habit(String title, String reason, LocalDate startDate, List<Boolean> weekdays) {
        this.weekdays = new ArrayList<Boolean>();
        setTitle(title);
        setReason(reason);
        setStartDate(startDate);
        setWeekdays(weekdays);
        setPublic(true);
        habitEvents = new ArrayList<HabitEvent>();
    }

    /**
     * Habit constructor without a date specification.
     * @param title the habit name
     * @param reason the reason for the habit
     * @param weekdays which days of the week the habit should be performed
     * @param isPublic whether this habit is viewable by other users or not
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    Habit(String title, String reason, List<Boolean> weekdays, boolean isPublic) {
        this.weekdays = new ArrayList<Boolean>();
        LocalDate defDate = LocalDate.now();
        setTitle(title);
        setReason(reason);
        setStartDate(defDate);
        setWeekdays(weekdays);
        setPublic(isPublic);
        habitEvents = new ArrayList<HabitEvent>();
    }

    /**
     * Habit constructor without a date or public specification.
     * @param title the habit name
     * @param reason the reason for the habit
     * @param weekdays which days of the week the habit should be performed
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    Habit(String title, String reason, List<Boolean> weekdays) {
        this.weekdays = new ArrayList<Boolean>();
        LocalDate defDate = LocalDate.now();
        setTitle(title);
        setReason(reason);
        setStartDate(defDate);
        setWeekdays(weekdays);
        setPublic(true);
        habitEvents = new ArrayList<HabitEvent>();
    }

    /**
     * Habit constructor specifying title, reason, and startDate.
     * @param title the name of the habit
     * @param reason the reason for the habit
     * @param startDate the date to start the habit
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    Habit(String title, String reason, LocalDate startDate) {
        // set default weekdays to false
        List<Boolean> defWeekdays = new ArrayList<Boolean>(Arrays.asList(new Boolean[7])); // initialized to false
        Collections.fill(defWeekdays, Boolean.FALSE);
        setTitle(title);
        setReason(reason);
        setStartDate(startDate);
        setWeekdays(defWeekdays);
        setPublic(true);
        habitEvents = new ArrayList<HabitEvent>();
    }

    /**
     * Habit constructor specifying only title and reason.
     * @param title the name of the habit
     * @param reason the reason for the habit
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    Habit(String title, String reason) {
        // set default date to today
        LocalDate defDate = LocalDate.now();
        // set default weekdays to false
        List<Boolean> defWeekdays = new ArrayList<Boolean>(Arrays.asList(new Boolean[7])); // initialized to false
        Collections.fill(defWeekdays, Boolean.FALSE);
        setTitle(title);
        setReason(reason);
        setStartDate(defDate);
        setWeekdays(defWeekdays);
        setPublic(true);
        habitEvents = new ArrayList<HabitEvent>();
    }

    /**
     * Returns the title of a habit.
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the reason for a habit.
     * @return the reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * Returns the day for the start date of this habit.
     * @return the day of the month
     */
    public int getDay() {
        return day;
    }

    /**
     * Returns the month for the start date of this habit.
     * @return the month
     */
    public int getMonth() {
        return month;
    }

    /**
     * Returns the year for the start date of this habit.
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * Returns the weekdays on which a habit should be performed.
     * @return a boolean array of length 7 starting at Sunday, with value true
     * if the habit is performed on that day, false if not
     */
    public List<Boolean> getWeekdays() {
        return weekdays;
    }

    /**
     * Returns a boolean value representing whether this habit is public or not.
     * @return true if public, false if not
     */
    public boolean getPublic() {
        return isPublic;
    }

    /**
     * Return the habitEvents associated with this habit.
     * @return a list of habit habitEvents associated with this habit
     */
    public ArrayList<HabitEvent> getHabitEvents() { return habitEvents; }

    /**
     * Sets the title of a habit.
     * @param title the title
     */
    public void setTitle(String title) {
        // if the title is too long, cut it to the first 20 characters
        if (title.length() > 20) {
            title = title.substring(0,20);
        }
        this.title = title;
    }

    /**
     * Sets the reason for a habit.
     * @param reason the reason
     */
    public void setReason(String reason) {
        // if the reason is too long, cut it to the first 30 characters
        if (reason.length() > 30) {
            reason = reason.substring(0,30);
        }
        this.reason = reason;
    }

    /**
     * Sets the start date of a habit
     * @param startDate the start date
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setStartDate(LocalDate startDate) {
        setDay(startDate.getDayOfMonth());
        setMonth(startDate.getMonthValue());
        setYear(startDate.getYear());
    }

    /**
     * Sets the day of month for the start date of this habit.
     * @param day the day on which to start
     */
    public void setDay(int day) {
        this.day = day;
    }

    /**
     * Sets the month for the start date of this habit.
     * @param month the month in which to start
     */
    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * Sets the year for the start date of this habit.
     * @param year the year in which to start
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Sets the weekdays on which a habit should be performed, starting with
     * weekdays[0] representing Sunday.
     * @param weekdays a boolean array of length 7 with value true if the
     *                 habit should be performed on that weekday, false if not
     */
    public void setWeekdays(List<Boolean> weekdays) {
        this.weekdays = weekdays;
    }

    /**
     * Sets the publicity of the habit. If public, a habit may be viewed by
     * other users who follow this user.
     * @param isPublic true if public, false if not
     */
    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    /**
     * Adds a habitEvent to the habitEvents list.
     * @param habitEvent the event to add
     */
    public void addHabitEvent(HabitEvent habitEvent){
        habitEvents.add(habitEvent);
    }

    /**
     * Removes a habitEvent from the habitEvents list.
     * @param habitEvent the event to remove
     */
    public void removeHabitEvent(HabitEvent habitEvent) {
        habitEvents.remove(habitEvent);
    }

    /**
     *  Returns a score representing how well a user is following this habit.
     *  Score starts at 3 (good) and is subtracted by 1 for every previous habit
     *  event missed, down to 0 (bad). Only check previous 3 scheduled habit
     *  event dates to see if user did the habit.
     *  @return a score from 0 to 3, with 3 being the best
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public int followScore() {
        // initialize score and count
        int score = 3;
        int countChecker = 3;

        // get the current date and day of week
        LocalDate current = LocalDate.now();
        DayOfWeek currentDOW = current.getDayOfWeek();
        // get the start date of the habit
        LocalDate start = LocalDate.of(year, month, day);

        while ((score > 0) && (countChecker > 0) && (current.isAfter(start))) {
            // Habit needs to be done this day
            int getCurrentDOW = currentDOW.getValue();
            // Set value of sunday to be 0, to be consistent with weekdays list
            if (getCurrentDOW == 7) {
                getCurrentDOW = 0;
            }
            if (weekdays.get(getCurrentDOW)) {
                // Habit needed to be done on this day
                countChecker--;
                // Was habit done on this day?
                boolean flag = false;
                for (HabitEvent he : habitEvents) {
                    LocalDate habitEventsDate = LocalDate.of(he.getYear(), he.getMonth(), he.getDay());
                    if (current.equals(habitEventsDate)) {
                        // the habit was done on this day
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    // habit was not done on this day, decrement score
                    score--;
                }
            }

            // Decrement current day
            current = current.minusDays(1);
            currentDOW = current.getDayOfWeek();
        }
        return score;
    }
}