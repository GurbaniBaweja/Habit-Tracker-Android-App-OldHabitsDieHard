/*
 *  SearchActivity
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

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

/**
 * This class defines an activity for the search page, where a user can search
 * for other users in order to request to follow them or view their profile.
 *
 * @author Paige Lekach
 * @author Claire Martin
 */
public class SearchActivity extends AppCompatActivity {
    private User user;
    private User searchUser;
    private UserDatabase db = UserDatabase.getInstance();

    /**
     * Declares action to take when this activity is started.
     * @param savedInstanceState
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set view
        setContentView(R.layout.search_activity);

        // get current user
        user = db.getUser(CurrentUser.get().getUsername());

        // set up UI elements
        SearchView searchView = findViewById(R.id.search_bar);
        TextView searchResultHeader = findViewById(R.id.search_result_header);
        TextView searchUsername = findViewById(R.id.search_username);
        Button requestButton = findViewById(R.id.request_search);
        TextView habitsHeader = findViewById(R.id.habits_header_search);
        ListView userHabitList = findViewById(R.id.user_habits);

        //creating intents for different activities
        Intent intentHabits = new Intent(this, HabitListActivity.class);
        Intent intentEvents = new Intent(this, HabitEventListActivity.class);
        Intent intentToday = new Intent(this, TodayActivity.class);
        Intent intentProfile = new Intent(this, ProfileActivity.class);

        // listener for search box
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            /**
             * Search for a user when text is submitted in the search box.
             * @param s the text that was submitted
             * @return false if the searched user does not exist
             */
            @Override
            public boolean onQueryTextSubmit(String s) {
                // search for user
                db = UserDatabase.getInstance();
                searchUser = db.getUser(s);

                if(searchUser != null){
                    // the user that was searched for exists
                    // get the searched user's public habits
                    ArrayList<Habit> userPublicHabits = searchUser.getPublicHabits();
                    StaticHabitAdapter staticHabitAdapter = new StaticHabitAdapter(getApplicationContext(), userPublicHabits);
                    userHabitList.setAdapter(staticHabitAdapter);
                    userHabitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        /**
                         * Start a FollowingHabitFragment when a habit in the
                         * list is clicked.
                         * @param adapterView the adapter view
                         * @param view the view that was clicked
                         * @param position the position of the clicked habit
                         * @param l
                         */
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            // get clicked habit
                            final Habit habit = userPublicHabits.get(position);
                            // create and show fragment to view habit
                            FollowingHabitFragment fragment = FollowingHabitFragment.newInstance(habit, s);
                            fragment.show(getSupportFragmentManager(), "VIEW_HABIT");
                        }
                    });


                    if(!searchUser.getUsername().equals(user.getUsername())){
                        // user searched for a different user, show result
                        searchResultHeader.setVisibility(View.VISIBLE);
                        searchUsername.setVisibility(View.VISIBLE);
                        searchUsername.setText(s);
                        requestButton.setVisibility(View.VISIBLE);

                        if(user.getFollowing().contains(searchUser.getUsername())){
                            // user is following the searched user
                            habitsHeader.setVisibility(View.VISIBLE);
                            userHabitList.setVisibility(View.VISIBLE);
                            requestButton.setText("Following");
                            requestButton.setTextColor(getResources().getColor(R.color.blue));
                            requestButton.setBackgroundColor(getResources().getColor(R.color.pink));

                        } else if(searchUser.getFollowRequests().contains(new FollowRequest(user.getUsername(), searchUser.getUsername()))){
                            // user has requested to follow the searched user
                            requestButton.setText("Requested");
                            requestButton.setTextColor(getResources().getColor(R.color.blueLight)); //lb
                            requestButton.setBackgroundColor(getResources().getColor(R.color.blue) ); //b
                            habitsHeader.setVisibility(View.INVISIBLE);
                            userHabitList.setVisibility(View.INVISIBLE);
                        } else{
                            // user can request to follow the searched user
                            requestButton.setText("Request");
                            requestButton.setTextColor(getResources().getColor(R.color.blue));//b
                            requestButton.setBackgroundColor(getResources().getColor(R.color.blueLight)); //lb
                            habitsHeader.setVisibility(View.INVISIBLE);
                            userHabitList.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        // user searched for themselves, start profile activity
                        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(intent);
                    }
                } else {
                    // searched user does not exist
                    Toast.makeText(getApplicationContext(), "No user matches search\nTry again!", Toast.LENGTH_LONG).show();
                }
                return false;
            }

            /**
             * Return false when the query text is changed.
             * @param s the query text
             * @return false
             */
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        // listener for request button
        requestButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Request to follow the searched user when the request button is
             * clicked.
             * @param view the request button
             */
            @Override
            public void onClick(View view) {
                // get the searched user
                searchUser = db.getUser(searchUsername.getText().toString());

                if(requestButton.getText().toString().equals("Request")) {
                    // request to follow the searched user
                    searchUser.addFollowRequest(new FollowRequest(CurrentUser.get().getUsername(), searchUsername.getText().toString()));
                    // update database
                    db.updateUser(searchUser);

                    // switch to requested button
                    requestButton.setText("Requested");
                    requestButton.setTextColor(getResources().getColor(R.color.blueLight)); //lb
                    requestButton.setBackgroundColor(getResources().getColor(R.color.blue)); //b

                } else if (requestButton.getText().toString().equals("Requested")) {
                    // user has already requested to follow the searched user
                    // remove follow request from searched user
                    searchUser.removeFollowRequest(new FollowRequest(CurrentUser.get().getUsername(), searchUsername.getText().toString()));
                    db.updateUser(searchUser);

                    // switch to request button
                    requestButton.setText("Request");
                    requestButton.setTextColor(getResources().getColor(R.color.blue));
                    requestButton.setBackgroundColor(getResources().getColor(R.color.blueLight)); //lb

                } else {
                    // user already follows searched user
                    // unfollow searched user
                    searchUser.removeFollower(CurrentUser.get().getUsername());
                    db.updateUser(searchUser);
                    user.removeFollowing(searchUser.getUsername());
                    db.updateUser(user);

                    // switch to request button
                    requestButton.setText("Request");
                    requestButton.setTextColor(getResources().getColor(R.color.blue));
                    requestButton.setBackgroundColor(getResources().getColor(R.color.blueLight)); //lb
                }
            }
        });

        //initializing navigation
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);

        // listener for navigation buttons
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    /**
                     * Switches to new activities based on which navigation
                     * button was pressed.
                     * @param item the item that was selected
                     * @return false if the activity is not started correctly
                     */
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        //switching through actions
                        switch (item.getItemId()) {
                            case R.id.action_today:
                                // today button was pressed
                                startActivity(intentToday);
                                break;
                            case R.id.action_habits:
                                // habits button was pressed
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
                        }
                        return false;
                    }
                });
    }
}
