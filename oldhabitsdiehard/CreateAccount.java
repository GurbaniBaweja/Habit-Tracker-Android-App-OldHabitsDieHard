/*
 *  CreateAccount
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

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Class that displays the Create Account activity, which allows the user
 * to create an account with a unique username, a password and a bio.
 *
 * @author Paige Lekach
 */
public class CreateAccount extends AppCompatActivity{
    private User user;
    private final Integer minPasswordLength = 2;

    /**
     * Defines action to take when the Create Account activity is created. Upon
     * creation, the activity defines the UI elements, instantiates the user
     * database, and defines the listener methods.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        //set up edit texts and buttons
        EditText usernameBox = findViewById(R.id.username_create);
        EditText passwordBox = findViewById(R.id.password_create);
        EditText passwordConfirmBox = findViewById(R.id.confirm_password);
        EditText bioBox = findViewById(R.id.bio_create);
        Button createButton = findViewById(R.id.create_account_button);
        Button backLoginButton = findViewById(R.id.back_login);

        // get instance of database
        UserDatabase db = UserDatabase.getInstance();

        // define create account button
        createButton.setOnClickListener(new View.OnClickListener() {
            /**
             * When the create profile button is pressed, the username and
             * password are checked to ensure they are valid. If so, the user is
             * created and the app switches to the Today activity. If not, the
             * app displays an error message.
             * @param view the create account button
             */
            @Override
            public void onClick(View view) {

                // get entered username and password
                String username = usernameBox.getText().toString();
                String password = passwordBox.getText().toString();
                String passwordConfirm = passwordConfirmBox.getText().toString();
                String bio = bioBox.getText().toString();

                if (username.length() < 1) {
                    // blank username make alert
                    Toast.makeText(getApplicationContext(), "Please enter a valid username", Toast.LENGTH_LONG).show();
                } else if (!password.equals(passwordConfirm)) {
                    // not matching password alert
                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();

                } else if (password.length() < minPasswordLength) {
                    // too short password alert
                    Toast.makeText(getApplicationContext(), "Your password is too short and invalid", Toast.LENGTH_LONG).show();

                } else {

                    // create new user
                    user = new User(username, password, bio);

                    // check if we can add it to the database
                    boolean success = db.addUser(user);

                    if (success) {
                        // account was created successfully
                        CurrentUser.set(user);
                        // switch to today view
                        Intent intent = new Intent(view.getContext(), TodayActivity.class);
                        startActivity(intent);
                    } else {
                        // account already exists alert
                        Toast.makeText(getApplicationContext(), "Username already exists\nPlease try a different option!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        // set listener for back button
        backLoginButton.setOnClickListener(new View.OnClickListener() {
            /**
             * If the back button is clicked, the app switches back to the Login
             * activity.
             * @param view the back button
             */
            @Override
            public void onClick(View view) {
                // create intent for login page
                Intent intent = new Intent(view.getContext(), Login.class);
                // start intent
                startActivity(intent);
            }
        });

    }
}
