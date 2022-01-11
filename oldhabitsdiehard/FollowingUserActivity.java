/*
 *  FollowingUserActivity
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * This activity allows the user to view another user that they follow.
 *
 * @author Paige Lekach
 */
public class FollowingUserActivity extends AppCompatActivity {
    private User user;
    private UserDatabase db;

    /**
     * Defines UI elements and listeners when the activity is started.
     * @param savedInstanceState the state of the app
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set view
        setContentView(R.layout.following_user_view);

        // get instance of database
        db = UserDatabase.getInstance();

        // get the intent and set the username
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");

        // get the user
        user = db.getUser(username);

        // define back button
        Button backButton = findViewById(R.id.back_to_following);

        // get list of public habits belonging to the user
        ArrayList<Habit> userPublicHabits = user.getPublicHabits();

        // define UI elements
        ListView userHabitList = findViewById(R.id.following_habits_list);
        TextView usernameHeader = findViewById(R.id.following_username);
        usernameHeader.setText(username);

        // user static habit adapter to display habits
        StaticHabitAdapter staticHabitAdapter = new StaticHabitAdapter(getApplicationContext(), user.getPublicHabits());
        userHabitList.setAdapter(staticHabitAdapter);

        // listener for habit list
        userHabitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * Gets the user's public habit and creates the habit view fragment
             * when a habit is clicked.
             * @param adapterView the parent view of the habit list
             * @param view the view that was clicked
             * @param position the position of the habit that was clicked
             * @param l
             */
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // get clicked habit
                final Habit habit = userPublicHabits.get(position);
                // create fragment to view habit
                FollowingHabitFragment fragment = FollowingHabitFragment.newInstance(habit, username);
                fragment.show(getSupportFragmentManager(), "VIEW_HABIT");
            }
        });

        // listener for the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Starts the following activity when the back button is clicked.
             * @param view the back button
             */
            @Override
            public void onClick(View view) {
                // create intent for following activity and start it
                Intent intent = new Intent(view.getContext(), FollowingActivity.class);
                startActivity(intent);
            }
        });
    }
}
