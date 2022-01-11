/*
 *  FollowerActivity
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Class that displays the Following page, which shows a list of users that the
 * current user is following.
 *
 * @author Paige Lekach
 */
public class FollowerActivity extends AppCompatActivity {
    private User user;

    /**
     * Declares action to take when this activity is started. Upon creation,
     * the current user is retrieved, the UI elements are defined, and adapters
     * are created for the follower and following lists. Then, several listeners
     * are defined for various user actions.
     * @param savedInstanceState
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the view to the followers page
        setContentView(R.layout.followers_view);

        // get the user database and the current user
        UserDatabase db = UserDatabase.getInstance();
        user = db.getUser(CurrentUser.get().getUsername());

        // define the UI elements
        Button backArrow = findViewById(R.id.back_profile_following);
        TextView followingHeader = findViewById(R.id.following_header);
        @SuppressLint("WrongViewCast") TextView followerLayout = findViewById(R.id.followers_layout);

        // define the lists
        ListView followingList = findViewById(R.id.following_list_1);
        ListView followersList = findViewById(R.id.follower_list_1);

        // create follower and following adapters
        FollowingAdapter followingAdapter = new FollowingAdapter(this, user);
        FollowerAdapter followerAdapter = new FollowerAdapter(this, user);

        // set the adapters
        followingList.setAdapter(followingAdapter);
        followersList.setAdapter(followerAdapter);

        // listener for the following header
        followingHeader.setOnClickListener(new View.OnClickListener() {
            /**
             * When the following header is pressed, the app switches to the
             * Following activity.
             * @param view the following header
             */
            @Override
            public void onClick(View view) {
                // create a new FollowingActivity intent
                Intent intent = new Intent(view.getContext(), FollowingActivity.class);
                // start the intent
                startActivity(intent);
            }
        });

        // listener for the back arrow
        backArrow.setOnClickListener(new View.OnClickListener() {
            /**
             * When the back arrow is pressed, the app switches to the Profile
             * activity.
             * @param view the back arrow
             */
            @Override
            public void onClick(View view) {
                // create Profile intent
                Intent intent = new Intent(view.getContext(), ProfileActivity.class);
                // start intent
                startActivity(intent);
            }
        });
    }
}
