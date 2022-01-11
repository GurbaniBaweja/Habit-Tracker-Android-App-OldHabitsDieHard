/*
 *  CurrentUser
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

/**
 * Class that provides global access to the currently logged in user.
 * Only one User will be logged in at a time.
 *
 * @author Rowan Tilroe
 */
public class CurrentUser {
    private static User currentUser = null;

    /**
     * Private constructor to prevent instantiation.
     */
    private CurrentUser() {}

    /**
     * Get reference to currently logged in user (can be null).
     * @return current user
     */
    public static User get() {
        if (currentUser != null) {
            return UserDatabase.getInstance().getUser(currentUser.getUsername());
        }
        return null;
    }

    /**
     * Set currently logged in user to another user (can be null).
     * @param user user to be set as logged in
     */
    public static void set(User user) {
        currentUser = user;
    }
}
