/*
 *  TodayActivity
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
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

/**
 * This class creates an activity where the user can view all the habits that
 * should be done today.
 *
 * @author Paige Lekach
 */
public class TodayActivity extends AppCompatActivity {
    private User user;

    /**
     * Sets up UI elements and listeners when activity is created.
     * @param savedInstanceState the current state of the app
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // set up the view
        setContentView(R.layout.today_view);

        // get the current user
        user = CurrentUser.get();

        // set up the today list
        ListView todaysHabitListView = findViewById(R.id.today_habits_list);
        //retrieving user's today habits
        StaticHabitAdapter staticHabitAdapter = new StaticHabitAdapter(this, user.getTodayHabits());
        todaysHabitListView.setAdapter(staticHabitAdapter);

        //creating intents for activities
        Intent intentHabits = new Intent(this, HabitListActivity.class);
        Intent intentEvents = new Intent(this, HabitEventListActivity.class);
        Intent intentProfile = new Intent(this, ProfileActivity.class);
        Intent intentSearch = new Intent(this, SearchActivity.class);

        //initializing navigation
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);

        // listener for navigation buttons
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    /**
                     * Switches to new activities based on which navigation button
                     * was pressed.
                     * @param item the button that was pressed
                     * @return false if the activity fails to start
                     */
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        //switching between activities
                        switch (item.getItemId()) {
                            case R.id.action_habits:
                                // habit button was pressed
                                startActivity(intentHabits);
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
}
