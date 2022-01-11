/*
 *  HabitEventListActivity
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

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

/**
 * This class shows Habit event list activity where users can see all their habit events.
 *
 * @author Filippo Ciandy
 * @author Claire Martin
 */
public class HabitEventListActivity extends AppCompatActivity implements HabitEventFragment.onFragmentInteractionListener {
    private ListView habitEventListView;
    private HabitEventAdapter habitEventAdapter;
    private ArrayList<HabitEvent> habitEventList;
    private User user;
    private UserDatabase db;

    /**
     * Defines action to take when the activity is created.
     * @param savedInstanceState
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.habitevent_list);

        // get the current logged in user
        user = CurrentUser.get();
        db = UserDatabase.getInstance();
        db.updateUser(user);

        // initialize views and get the list of habit events
        habitEventListView = findViewById(R.id.habitevent_list);
        habitEventList = user.getHabitEvents();

        // set adapter
        habitEventAdapter = new HabitEventAdapter(this, user);
        habitEventListView.setAdapter(habitEventAdapter);

        // define the add button

        final FloatingActionButton addHabitEventButton = findViewById(R.id.add_habit_event_button);
        if (user.getHabits().size() == 0) {
            // we have no habits, so user is not allowed to add a habit event
            addHabitEventButton.setVisibility(View.INVISIBLE);
        } else {
            addHabitEventButton.setVisibility(View.VISIBLE);
        }
   
        addHabitEventButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Defines action to take when add button is clicked.
             * @param view
             */
            @Override
            public void onClick(View view) {
                // create new fragment to add the habit
                new HabitEventFragment().show(getSupportFragmentManager(), "ADD_HABIT_EVENT");
            }
        });

        // when a habit event is clicked
        habitEventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * Defines action to take when a habit event in the list is clicked.
             * @param parent
             * @param view
             * @param position the position in the list of the event which was
             *                 clicked
             * @param id
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // get the clicked habit event
                final HabitEvent habitEvent = habitEventList.get(position);
                // create a new fragment to edit the habit
                HabitEventFragment newFragment = HabitEventFragment.newInstance(habitEvent);
                newFragment.show(getSupportFragmentManager(), "EDIT_HABIT_EVENT");
            }
        });

        //creating intents for the different activities
        Intent intentToday = new Intent(this, TodayActivity.class);
        Intent intentProfile = new Intent(this, ProfileActivity.class);
        Intent intentHabits = new Intent(this, HabitListActivity.class);
        Intent intentSearch = new Intent(this, SearchActivity.class);

        //setting up the navigation bar in the activity
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);

        // listener for navigation buttons
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    /**
                     * Switches to a new activity based on which navigation
                     * button was pressed.
                     * @param item the button that was pressed
                     * @return false if the activity fails to start
                     */
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        //switching between the different actions
                        switch (item.getItemId()) {
                            case R.id.action_today:
                                // today button was pressed
                                startActivity(intentToday);
                                break;
                            case R.id.action_habits:
                                // habits button was pressed
                                startActivity(intentHabits);
                                break;
                            case R.id.action_profile:
                                // profile button was pressed
                                startActivity(intentProfile);
                                break;
                            case R.id.action_search:
                                // search button was pressed
                                startActivity(intentSearch);
                                break;
                        }
                        return false;
                    }
                });
    }

    /**
     * Method to add a habit event.
     * @param newEvent the event to add
     */
    @Override
    public void addHabitEvent(HabitEvent newEvent) {
        // add the habit event to the user
        user.addHabitEvent(newEvent);
        // notify the adapter of changes
        habitEventAdapter.notifyDataSetChanged();
        // update user in firestore
        db.updateUser(user);
    }

    /**
     * **NOT IMPLEMENTED**
     * Method to edit a habit event.
     * @param event the event to edit
     */
    @Override
    public void editHabitEvent(HabitEvent event) {
        // notify the adapter of changes
        habitEventAdapter.notifyDataSetChanged();
        // update the user in firestore
        db.updateUser(user);
    }

    /**
     * **NOT IMPLEMENTED**
     * Method to delete a habit event.
     * @param event the event to delete
     */
    @Override
    public void deleteHabitEvent(HabitEvent event) {
        user.deleteHabitEvent(event);
        // remove event from adapter
        habitEventAdapter.notifyDataSetChanged();
        // udpate the user in firestore
        db.updateUser(user);
    }
}
