/*
 *  Login
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

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * This class defines the Login activity which is called when the app starts.
 *
 * @author Rowan Tilroe
 * @author Paige Lekach
 */
public class Login extends AppCompatActivity {
    private User user;

    /**
     * Defines action to take when the activity is created.
     * @param savedInstanceState the current state of the app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set the view
        setContentView(R.layout.activity_login);

        // set up boxes for user to enter login info
        EditText usernameBox = findViewById(R.id.username_login);
        EditText passwordBox = findViewById(R.id.password_login);
        Button createButton = findViewById(R.id.create_button);
        Button loginButton = findViewById(R.id.login_button);

        // get instance of database
        UserDatabase db = UserDatabase.getInstance();

        // define create account button
        createButton.setOnClickListener(new View.OnClickListener() {
            /**
             * When the create button is clicked, the create account activity is
             * started.
             * @param view the create button
             */
            @Override
            public void onClick(View view) {
                // start the create account activity
                Intent intent = new Intent(view.getContext(), CreateAccount.class);
                startActivity(intent);
            }
        });

        // define login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            /**
             * When the login button is clicked, checks to make sure the login
             * is valid. If so, starts the today activity. If not, displays an
             * error message.
             * @param view
             */
            @Override
            public void onClick(View view) {
                // get entered username and password
                String username = usernameBox.getText().toString();
                String password = passwordBox.getText().toString();

                // check for empty boxes
                if (username.length() < 1 || password.length() < 1) {
                    // user must enter username and password
                    Toast.makeText(getApplicationContext(), "Incorrect username and/or password.\nPlease try again!", Toast.LENGTH_LONG).show();
                } else {
                    // check whether the user info is correct
                    user = db.checkLogin(username, password);

                    if (user != null) {
                        // login success, set the current user
                        CurrentUser.set(user);
                        // start today view
                        Intent intent = new Intent(view.getContext(), TodayActivity.class);
                        startActivity(intent);
                    } else {
                        // login failure
                        Toast.makeText(getApplicationContext(), "Incorrect username and/or password.\nPlease try again!", Toast.LENGTH_LONG).show();
                        usernameBox.setText("");
                        passwordBox.setText("");
                    }
                }
            }
        });
    }
}
