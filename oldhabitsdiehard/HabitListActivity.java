/*
 *  HabitListActivity
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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This class represents the Habit List Activity in which the user can view
 * a list of all their habits.
 *
 * @author Claire Martin
 * @author Filippo Ciandy
 */
public class HabitListActivity extends AppCompatActivity implements HabitFragment.onFragmentInteractionListener {
    private ArrayList<Habit> habitList;
    private User user;
    private UserDatabase db;
    private HabitAdapter recyclerAdapter;
    private RecyclerView recyclerView;

    /**
     * When the activity is created, all UI elements are defined and the
     * listeners and created for the habit list and the buttons.
     * @param savedInstanceState the saved state
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // set the view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.habit_list);

        // fetch current user from firebase
        user = CurrentUser.get();
        db = UserDatabase.getInstance();
        db.updateUser(user);

        // create the habit list and set its view
        habitList = user.getHabits();
        recyclerView = findViewById(R.id.habit_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // create the adapter
        recyclerAdapter = new HabitAdapter(habitList, new AdapterView.OnItemClickListener() {
            /**
             * When a habit in the list is clicked, the habit fragment is started
             * in edit mode.
             * @param parent the parent adapterview
             * @param view the view that was clicked
             * @param position the position of the habit that was clicked
             * @param id the id
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // get the habit that was clicked
                final Habit habit = habitList.get(position);
                // create the fragment and open it with the chosen habit
                HabitFragment newFragment = HabitFragment.newInstance(habit);
                newFragment.show(getSupportFragmentManager(), "EDIT_HABIT");
            }
        });

        // set the adapter and itemTouchHelper
        recyclerView.setAdapter(recyclerAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        // define habit add button
        final FloatingActionButton addHabitButton = findViewById(R.id.add_habit_button);
        addHabitButton.setOnClickListener(new View.OnClickListener() {
            /**
             * When the add habit button is clicked, the habit fragment is
             * started in add mode.
             * @param view the add button
             */
            @Override
            public void onClick(View view) {
                // start a new fragment with no habit
                new HabitFragment().show(getSupportFragmentManager(), "ADD_HABIT");
            }
        });

        // creating intents for the different activities
        Intent intentToday = new Intent(this, TodayActivity.class);
        Intent intentProfile = new Intent(this, ProfileActivity.class);
        Intent intentEvents = new Intent(this, HabitEventListActivity.class);
        Intent intentSearch = new Intent(this, SearchActivity.class);

        // initializing navigation in the activity
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);

        // listener for the navigation buttons
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    /**
                     * Switches to a new activity based on which navigation
                     * button was pressed.
                     * @param item the item that was chosen
                     * @return false if the activity fails to start
                     */
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        //switching through activities
                        switch (item.getItemId()) {
                            case R.id.action_today:
                                // today button was pressed
                                startActivity(intentToday);
                                break;
                            case R.id.action_events:
                                // events button was pressed
                                startActivity(intentEvents);
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

    // handles callbacks when a drag or swipe action is done.
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        /**
         * Rearranges the habit list when an item is moved.
         * @param recyclerView the recyclerview, cannot be null
         * @param viewHolder the initial viewholder, cannot be null
         * @param target the target viewholder, cannot be null
         * @return false
         */
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            // initial position of the habit
            int fromPosition = viewHolder.getAdapterPosition();
            // target position of the habit
            int toPosition = target.getAdapterPosition();
            // switch the habits in the list
            Collections.swap(habitList, fromPosition, toPosition);
            // update the user and recyclerview
            db.updateUser(user);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            return false;
        }

        /**
         * Scroll the list when the user swipes.
         * @param viewHolder the viewholder that was swiped
         * @param direction the direction swiped
         */
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            // doesn't need to do anything
        }
    };

    /**
     * Method to add a habit to the list using the fragment.
     * @param newHabit the habit to add
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void addHabit(Habit newHabit) {
        habitList.add(newHabit);
        recyclerAdapter.notifyItemInserted(habitList.size()-1);
        db.updateUser(user);
    }

    /**
     * Method to edit a habit in the list.
     * @param habit the habit to edit
     */
    @Override
    public void editHabit(Habit habit) {
        recyclerAdapter.notifyDataSetChanged();
        db.updateUser(user);
    }

    /**
     * Method to delete a habit from the list.
     * @param habit the habit to delete
     */
    @Override
    public void deleteHabit(Habit habit) {
        // delete the habit from the user
        // did not delete from adapter in order to ensure events are deleted too
        user.deleteHabit(habit);
        recyclerAdapter.notifyDataSetChanged();
        db.updateUser(user);
    }
}
