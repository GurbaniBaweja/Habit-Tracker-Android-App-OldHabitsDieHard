/*
 *  HabitFragment
 *
 *  Version 1.0
 *
 *  November 4, 2021
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
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

/**
 * A class for the fragment allowing the user to add, edit, view or delete
 * habits.
 * @author Chanpreet Singh
 * @author Claire Martin
 */
public class HabitFragment extends DialogFragment {
    private EditText habitTitle;
    private EditText habitReason;
    private DatePicker habitDate;
    private RadioButton radioSelectedButton;
    private CheckBox sunday,monday,tuesday,wednesday,thursday,friday,saturday;
    private HabitFragment.onFragmentInteractionListener listener;
    private User user;
    private UserDatabase db;

    /**
     * A listener interface for this fragment to interact with the calling activity.
     */
    public interface onFragmentInteractionListener {
        // abstract methods to be implemented in the activity classes
        void addHabit(Habit habit);
        void editHabit(Habit habit);
        void deleteHabit(Habit habit);
    }

    /**
     * Creates an instance of the HabitFragment.
     * @param habit the habit we are looking at
     * @return the fragment
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static HabitFragment newInstance(Habit habit) {
        Bundle args = new Bundle();
        args.putSerializable("Habit", habit);

        HabitFragment fragment = new HabitFragment();
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * Defines action to take when fragment is attached to the activity.
     * @param context the current context
     */
    @Override
    public void onAttach(Context context){
        user = CurrentUser.get();
        db = UserDatabase.getInstance();
        db.updateUser(user);
        super.onAttach(context);
        if(context instanceof HabitFragment.onFragmentInteractionListener){
            listener = (HabitFragment.onFragmentInteractionListener) context;
        }else {
            throw new RuntimeException(context.toString()+"must implement OnFragmentInteractionListner");
        }
    }

    /**
     * Defines action to take when the fragment is created.
     * @param savedInstanceState
     * @return the dialog
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        db.updateUser(user);
        // get the habit fragment view
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.habit_fragment, null);
        // get info for the habit display items
        habitTitle = view.findViewById(R.id.habitTitle);
        habitReason = view.findViewById(R.id.habitReason);
        habitDate = view.findViewById(R.id.startDate);
        final RadioGroup group = view.findViewById(R.id.radio_group);
        sunday = view.findViewById(R.id.sundayCheckBox);
        monday = view.findViewById(R.id.mondayCheckBox);
        tuesday = view.findViewById(R.id.tuesdayCheckBox);
        wednesday = view.findViewById(R.id.wednesdayCheckBox);
        thursday = view.findViewById(R.id.thursdayCheckBox);
        friday = view.findViewById(R.id.fridayCheckBox);
        saturday = view.findViewById(R.id.saturdayCheckBox);

        // public is default
        group.check(R.id.publicHabit);

        // build the alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        if (getArguments() != null) {
            // we are editing a habit
            Habit myHabit = (Habit) getArguments().getSerializable("Habit");

            // set the habit info boxes to show this habit
            habitTitle.setText(myHabit.getTitle());
            habitReason.setText(myHabit.getReason());
            boolean currPublic = myHabit.getPublic();
            if (currPublic) {
                group.check(R.id.publicHabit);
            } else {
                group.check(R.id.privateHabit);
            }
            // start with sunday
            sunday.setChecked(myHabit.getWeekdays().get(0));
            monday.setChecked(myHabit.getWeekdays().get(1));
            tuesday.setChecked(myHabit.getWeekdays().get(2));
            wednesday.setChecked(myHabit.getWeekdays().get(3));
            thursday.setChecked(myHabit.getWeekdays().get(4));
            friday.setChecked(myHabit.getWeekdays().get(5));
            saturday.setChecked(myHabit.getWeekdays().get(6));

            // get the date info
            int year = myHabit.getYear();
            int month = myHabit.getMonth() - 1;
            int day = myHabit.getDay();
            habitDate.updateDate(year, month, day);

            // create builder
            return builder
                    .setView(view)
                    .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                        /**
                         * Defines action to take when delete button is pressed
                         * @param dialogInterface the dialog interface
                         * @param i
                         */
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            listener.deleteHabit(myHabit);
                        }
                    })
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        /**
                         * Defines action to take when the ok button is pressed
                         * @param dialogInterface the dialog interface
                         * @param i
                         */
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // determine which privacy setting was selected
                            int selectedPrivacy = group.getCheckedRadioButtonId();
                            radioSelectedButton = view.findViewById(selectedPrivacy);
                            String privacy = String.valueOf(radioSelectedButton.getText());
                            // set isPublic depending on privacy setting
                            boolean isPublic;
                            if (privacy.equals("Public")) {
                                isPublic = true;
                            } else {
                                isPublic = false;
                            }

                            // get habit title, reason and date info
                            String title = habitTitle.getText().toString();
                            String reason = habitReason.getText().toString();
                            int day = habitDate.getDayOfMonth();
                            int month = habitDate.getMonth() + 1;
                            int year = habitDate.getYear();
                            LocalDate date = LocalDate.of(year, month, day);

                            // get weekdays info
                            List<Boolean> weekdays = new ArrayList<Boolean>();
                            weekdays.add(sunday.isChecked());
                            weekdays.add(monday.isChecked());
                            weekdays.add(tuesday.isChecked());
                            weekdays.add(wednesday.isChecked());
                            weekdays.add(thursday.isChecked());
                            weekdays.add(friday.isChecked());
                            weekdays.add(saturday.isChecked());

                            // get habits that already exist
                            ArrayList<Habit> currentHabits = user.getHabits();
                            boolean habitExists = false;
                            for (int j = 0; j < currentHabits.size(); j++) {
                                // check if the habit name already exists
                                if (title.equals(currentHabits.get(j).getTitle())) {
                                    // user isn't allowed to use this name
                                    habitExists = true;
                                }
                            }
                            if (habitExists && !myHabit.getTitle().equals(title)) {
                                Toast.makeText(getActivity(), "Title already in use\nTry again!", Toast.LENGTH_LONG).show();
                            } else if (title == null){
                                // we require a habit title
                                Toast.makeText(getActivity(), "Please enter a habit title", Toast.LENGTH_LONG).show();
                            } else {
                                // update the habit with new info
                                myHabit.setTitle(title);
                                myHabit.setReason(reason);
                                myHabit.setPublic(isPublic);
                                myHabit.setWeekdays(weekdays);
                                myHabit.setStartDate(date);

                                // update habit in listener
                                listener.editHabit(myHabit);
                            }
                        }
                    }).create();
        } else {
            // we are adding a habit
            return builder
                    .setView(view)
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        /**
                         * Defines action to take when OK button is pressed.
                         * @param dialogInterface the dialog interface
                         * @param i
                         */
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // get info that the user entered
                            String title = habitTitle.getText().toString();
                            String reason = habitReason.getText().toString();

                            // determine which privacy button was selected
                            int selectedPrivacy = group.getCheckedRadioButtonId();
                            radioSelectedButton = view.findViewById(selectedPrivacy);
                            String privacy = String.valueOf(radioSelectedButton.getText());
                            // set isPublic based on privacy setting
                            boolean isPublic;
                            if (privacy.equals("Public")) {
                                isPublic = true;
                            } else {
                                isPublic = false;
                            }

                            // get date info
                            int day = habitDate.getDayOfMonth();
                            int month = habitDate.getMonth() + 1;
                            int year = habitDate.getYear();
                            LocalDate date = LocalDate.of(year, month, day);

                            // get weekday info
                            List<Boolean> weekdays = new ArrayList<Boolean>();
                            weekdays.add(sunday.isChecked());
                            weekdays.add(monday.isChecked());
                            weekdays.add(tuesday.isChecked());
                            weekdays.add(wednesday.isChecked());
                            weekdays.add(thursday.isChecked());
                            weekdays.add(friday.isChecked());
                            weekdays.add(saturday.isChecked());

                            // get habits that already exist
                            ArrayList<Habit> currentHabits = user.getHabits();
                            boolean habitExists = false;
                            for (int j = 0; j < currentHabits.size(); j++) {
                                // check if the habit name already exists
                                if (title.equals(currentHabits.get(j).getTitle())) {
                                    // user isn't allowed to use this name
                                    habitExists = true;
                                }
                            }
                            if (habitExists) {
                                Toast.makeText(getActivity().getApplicationContext(), "This habit already exists\nTry again!", Toast.LENGTH_LONG).show();
                            } else {
                                // add habit to listener
                                listener.addHabit(new Habit(title, reason, date, weekdays, isPublic));
                            }
                        }
                    }).create();
        }
    }
}
