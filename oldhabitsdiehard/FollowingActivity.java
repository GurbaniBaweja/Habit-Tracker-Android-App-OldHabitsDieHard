/*
 *  FollowingActivity
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

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Class that displays the Following page, which shows a list of users that the
 * current user is following.
 *
 * @author Paige Lekach
 */
public class FollowingActivity extends AppCompatActivity {
    private User user;
    private ArrayList<String> followingValList;

    /**
     * Declares action to take when this activity is started. Upon creation, the
     * activity defines UI elements, instantiates the follower and following
     * lists, and defines listener methods.
     * @param savedInstanceState
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the following view
        setContentView(R.layout.following_view);

        // get the database and current user
        UserDatabase db = UserDatabase.getInstance();
        user = db.getUser(CurrentUser.get().getUsername());
        followingValList = user.getFollowing();

        // define buttons and textviews
        Button backArrow = findViewById(R.id.back_profile_following);
        TextView followingHeader = findViewById(R.id.following_header);
        TextView followersHeader = findViewById(R.id.follower_header);
        @SuppressLint("WrongViewCast") TextView followerLayout = findViewById(R.id.followers_layout);

        // define follower and following lists
        ListView followingList = findViewById(R.id.following_list_2);
        FollowingAdapter followingAdapter = new FollowingAdapter(this, user);
        followingList.setAdapter(followingAdapter);

        // listener for the followers header
        followersHeader.setOnClickListener(new View.OnClickListener() {
            /**
             * If the followers header is clicked, the app switches to the
             * Follower activity.
             * @param view the followers header
             */
            @Override
            public void onClick(View view) {
                // create Follower intent and start it
                Intent intent = new Intent(view.getContext(), FollowerActivity.class);
                startActivity(intent);
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            /**
             * If the back arrow is pressed, the app switches back to the
             * Profile activity.
             * @param view the back arrow button
             */
            @Override
            public void onClick(View view) {
                // create Profile intent and start it
                Intent intent = new Intent(view.getContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        followingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String followingUser = followingValList.get(position);

                Intent intent = new Intent(getApplicationContext(), FollowingUserActivity.class);
                intent.putExtra("username",followingUser);
                startActivity(intent);
            }
        });

    }
}
