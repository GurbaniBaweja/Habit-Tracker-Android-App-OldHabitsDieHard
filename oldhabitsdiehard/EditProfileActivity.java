/*
 *  EditProfileActivity
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

/**
 * This class displays the Edit Profile activity, which allows a user to edit
 * their password and bio. The user is not allowed to edit their username.
 *
 * @author Paige Lekach
 */
public class EditProfileActivity extends AppCompatActivity {
    private User user;
    private final Integer minPasswordLength = 2;

    /**
     * Upon creation, the activity defines the UI elements and sets listeners
     * for several buttons.
     * @param savedInstanceState
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        // get the current user
        user = CurrentUser.get();

        // set up edit texts and buttons
        EditText usernameBox = findViewById(R.id.username_edit);
        EditText bioBox = findViewById(R.id.bio_edit);
        EditText currentPassword = findViewById(R.id.current_password);
        EditText newPassword = findViewById(R.id.password_new_edit);
        EditText confirmPassword = findViewById(R.id.password_confirm_edit);
        Button saveProfileButton = findViewById(R.id.save_profile_edits);
        Button backProfileButton = findViewById(R.id.back_profile);
        Button savePasswordButton = findViewById(R.id.save_password_edit);


        // set the text of the boxes to the current values
        usernameBox.setText(user.getUsername());
        bioBox.setText(user.getBio());

        // listener for the back button
        backProfileButton.setOnClickListener(new View.OnClickListener() {
            /**
             * When the back button is pressed, the app switches back to the
             * Profile activity.
             * @param view the back button
             */
            @Override
            public void onClick(View view) {
                // create profile intent and start it
                Intent intent = new Intent(view.getContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        // listener for the save button
        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            /**
             * When the save button is pressed, the app updates the user bio and
             * username and prints a success message.
             * @param view the save button
             */
            @Override
            public void onClick(View view) {
                // update the bio and username
                user.setBio(bioBox.getText().toString());
                user.setUsername(usernameBox.getText().toString());
                // print profile updated message
                Toast.makeText(getApplicationContext(), "Profile updated!", Toast.LENGTH_LONG).show();

            }
        });


        // listener for the save password button
        savePasswordButton.setOnClickListener(new View.OnClickListener() {
            /**
             * If the save password button is pressed, the app checks that the
             * current password is valid and that the password was confirmed
             * properly, and if these checks are passed then the password is
             * updated. If not, an error message is displayed.
             * @param view the save password button
             */
            @Override
            public void onClick(View view) {
                // get the current password
                String currentPasswordString = currentPassword.getText().toString();
                // get the new password
                String newPasswordString = newPassword.getText().toString();
                // get the confirmed password
                String confirmPasswordString = confirmPassword.getText().toString();
                // check if the current password is correct
                if (currentPasswordString.equals(user.getPassword())) {
                    // check if the new and confirmed passwords are equal
                    if (newPasswordString.equals(confirmPasswordString)) {
                        // check if the current and new passwords are equal
                        if (currentPasswordString.equals(newPasswordString)) {
                            // user entered the same password
                            Toast.makeText(getApplicationContext(), "You cannot set new password to current password.", Toast.LENGTH_LONG).show();
                        // check password length
                        } else if (newPasswordString.length() < minPasswordLength) {
                            // user entered a password that is too short
                            Toast.makeText(getApplicationContext(), "New password not valid", Toast.LENGTH_LONG).show();
                        } else {
                            // update the password and print success message
                            user.setPassword(newPasswordString);
                            Toast.makeText(getApplicationContext(), "Password successfully changed!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // the new and confirmed passwords don't match
                        Toast.makeText(getApplicationContext(), "New password not confirmed", Toast.LENGTH_LONG).show();
                    }
                } else{
                    // user entered wrong password
                    Toast.makeText(getApplicationContext(), "Invalid current password", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
