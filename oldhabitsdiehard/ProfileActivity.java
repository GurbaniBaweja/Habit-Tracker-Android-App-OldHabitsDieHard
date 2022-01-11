/*
 *  ProfileActivity
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

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

/**
 * This class creates an activity for the user to view their profile.
 *
 * @author Paige Lekach
 */
public class ProfileActivity extends AppCompatActivity {
    private User user;
    UserDatabase db = UserDatabase.getInstance();

    /**
     * Defines action to take when activity is created.
     * @param savedInstanceState the state of the app
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set the view
        setContentView(R.layout.profile_view);

        // get the current user
        user = db.getUser(CurrentUser.get().getUsername());

        // set up UI elements
        TextView username = findViewById(R.id.profile_username);
        TextView bio = findViewById(R.id.bio_profile);
        TextView followingTitle = findViewById(R.id.following_title);
        TextView noFollowingTitle = findViewById(R.id.no_following_header);
        TextView followersCount = findViewById(R.id.profile_followers_count);
        TextView followingCount = findViewById(R.id.profile_following_count);
        LinearLayout followerLayout = findViewById(R.id.followers_layout);
        LinearLayout followingLayout = findViewById(R.id.following_layout);
        Button logoutButton = findViewById(R.id.logout_button);
        Button editProfileButton = findViewById(R.id.edit_profile);

        // set the follower and following counts
        followersCount.setText(String.valueOf(user.getFollowers().size()));
        followingCount.setText(String.valueOf(user.getFollowing().size()));

        // if there are follow requests, show the following title
        if(user.getFollowRequests().size() > 0 ){
            followingTitle.setVisibility(View.VISIBLE);
            noFollowingTitle.setVisibility(View.INVISIBLE);
        }

        // define listview and adapter for follow requests
        ListView followRequestsView = findViewById(R.id.follow_request_list);
        FollowRequestAdapter followRequestAdapter = new FollowRequestAdapter(this, user);
        followRequestsView.setAdapter(followRequestAdapter);

        // set the username and bio textviews
        username.setText(CurrentUser.get().getUsername());
        bio.setText(CurrentUser.get().getBio());

        // creating intents for different activities
        Intent intentToday = new Intent(this, TodayActivity.class);
        Intent intentHabits = new Intent(this, HabitListActivity.class);
        Intent intentEvents = new Intent(this, HabitEventListActivity.class);
        Intent intentSearch = new Intent(this, SearchActivity.class);

        // listener for edit profile button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Defines action to take when the create button is clicked
             *
             * @param view
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Login.class);
                startActivity(intent);
            }
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Start edit profile activity when edit profile button is clicked.
             * @param view edit profile button
             */
            @Override
            public void onClick(View view) {
                // create edit profile activity and start it
                Intent intent = new Intent(view.getContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        // follower layout listener
        followerLayout.setOnClickListener(new View.OnClickListener() {
            /**
             * Start follower activity when follower layout is clicked.
             * @param view the follower layout
             */
            @Override
            public void onClick(View view) {
                // create follower activity and start it
                Intent intent = new Intent(view.getContext(), FollowerActivity.class);
                startActivity(intent);
            }
        });

        // following layout listener
        followingLayout.setOnClickListener(new View.OnClickListener() {
            /**
             * Start following activity when following layout is clicked.
             * @param view the following layout
             */
            @Override
            public void onClick(View view) {
                // create following activity and start it
                Intent intent = new Intent(view.getContext(), FollowingActivity.class);
                startActivity(intent);
            }
        });

        // manage follow requests
        followRequestAdapter.registerDataSetObserver(new DataSetObserver() {
            /**
             * Start profile activity when follow request adapter is changed.
             */
            @Override
            public void onChanged() {
                // create profile activity and start it
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
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
                     * Switches to a new activity based on which navigation
                     * button was pressed.
                     * @param item the button that was pressed
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
                            case R.id.action_habits:
                                // habits button was pressed
                                startActivity(intentHabits);
                                break;
                            case R.id.action_events:
                                // events button was pressed
                                startActivity(intentEvents);
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
