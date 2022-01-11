/*
 *  FollowingHabitFragment
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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.List;

/**
 * Fragment to display a habit of a user that the current user follows.
 *
 * @author Claire Martin
 */
public class FollowingHabitFragment extends DialogFragment {
    private TextView habitTitle;
    private TextView habitReason;
    private TextView habitDate;
    private TextView habitWeekdays;
    private TextView habitHeader;

    /**
     * Creates an instance of the FollowingHabitFragment.
     * @param habit the habit we are viewing
     * @param user the username of the user that the habit belongs t\o
     * @return the fragment
     */
    public static FollowingHabitFragment newInstance(Habit habit, String user) {
        // create a bundle of arguments to be passed into the fragment
        Bundle args = new Bundle();
        // add the habit and username to the bundle
        args.putSerializable("Habit", habit);
        args.putSerializable("User", user);
        // create the fragment and add the arguments to it
        FollowingHabitFragment fragment = new FollowingHabitFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Defines action to take when this fragment is attached.
     * @param context the current context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    /**
     * When the fragment is created, the information about the selected habit is
     * retrieved and displayed in the TextView boxes.
     * @param savedInstanceState the state of the activity
     * @return the Dialog
     */
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // get habit fragment view
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.following_habit_fragment, null);

        // get textview objects
        habitTitle = view.findViewById(R.id.following_habit_title);
        habitReason = view.findViewById(R.id.following_habit_reason);
        habitDate = view.findViewById(R.id.following_habit_date);
        habitWeekdays = view.findViewById(R.id.following_habit_weekdays);
        habitHeader = view.findViewById(R.id.following_habit_header);

        // build alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        if (getArguments() != null) {
            // we have clicked a habit
            // get the habit and user from the bundle of arguments
            Habit habit = (Habit) getArguments().getSerializable("Habit");
            String user = (String) getArguments().getSerializable("User");
            // set TextView boxes for the title and reason
            habitTitle.setText("Title: " + habit.getTitle());
            habitReason.setText("Reason: " + habit.getReason());

            // convert day to string
            String day = Integer.toString(habit.getDay());
            // convert the month to a string
            int monthVal = habit.getMonth();
            String month = "";
            // month is defined as integer from 1 to 12, with 1 being January
            switch (monthVal) {
                case 1:
                    month = "Jan";
                    break;
                case 2:
                    month = "Feb";
                    break;
                case 3:
                    month = "Mar";
                    break;
                case 4:
                    month = "Apr";
                    break;
                case 5:
                    month = "May";
                    break;
                case 6:
                    month = "Jun";
                    break;
                case 7:
                    month = "Jul";
                    break;
                case 8:
                    month = "Aug";
                    break;
                case 9:
                    month = "Sept";
                    break;
                case 10:
                    month = "Oct";
                    break;
                case 11:
                    month = "Nov";
                    break;
                case 12:
                    month = "Dec";
                    break;
            }
            // get the year as a string
            String year = Integer.toString(habit.getYear());

            // set the date textview
            habitDate.setText("Start Date: " + month + ". " + day + ", " + year);

            // get the weekdays list and convert to string
            List<Boolean> weekdaysList = habit.getWeekdays();
            String weekdays = "Days of Week: ";
            // weekdays is a list of size 7 with position 0 representing Sunday
            if (weekdaysList.get(0)) {
                weekdays += "Sun, ";
            }
            if (weekdaysList.get(1)) {
                weekdays += "Mon, ";
            }
            if (weekdaysList.get(2)) {
                weekdays += "Tues, ";
            }
            if (weekdaysList.get(3)) {
                weekdays += "Wed, ";
            }
            if (weekdaysList.get(4)) {
                weekdays += "Thurs, ";
            }
            if (weekdaysList.get(5)) {
                weekdays += "Fri, ";
            }
            if (weekdaysList.get(6)) {
                weekdays += "Sat";
            }

            // remove the comma from the end if there is one
            if (weekdays.substring(weekdays.length()-2).equals(", ")) {
                weekdays = weekdays.substring(0, weekdays.length()-2);
            }
            // set the text of the weekdays and header boxes
            habitWeekdays.setText(weekdays);
            habitHeader.setText(user + "'s Habit");
            // create the dialog
            return builder
                    .setView(view)
                    .setNeutralButton("Cancel", null)
                    .create();
        }
        // if we get here, we did not click a habit
        return null;
    }
}
