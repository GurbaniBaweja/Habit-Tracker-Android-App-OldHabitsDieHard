/*
 *  UserDatabase
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

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Database for user data. Connects to Firestore.
 *
 * (Collection) Users
 *      (Document) username1 -> (User) obj
 *      (Document) username2 -> (User) obj
 *
 * @author Rowan Tilroe
 */
public class UserDatabase {
    private static final UserDatabase instance = new UserDatabase();
    private FirebaseFirestore database;
    private FirebaseStorage storage;
    private CollectionReference userCollection;

    /**
     * Private constructor
     */
    private UserDatabase() {
        database = FirebaseFirestore.getInstance();
        userCollection = database.collection("Users");
        // create storage instance for storing images
        storage = FirebaseStorage.getInstance();
    }

    /**
     * Get an instance of the UserDatabase
     * @return handle to UserDatabase
     */
    public static UserDatabase getInstance() { return instance; }

    /**
     * Gets a reference to the Firebase Storage bucket.
     * @return storage reference
     */
    public StorageReference getStorageRef() {
        // get reference to storage bucket
        return storage.getReference();
    }

    /**
     * Attempt to add user to database
     * @param user User to add
     * @return true if added, false if not added (i.e. user with username already exists)
     */
    public boolean addUser(User user) {
        // check if user is in the database
        User check = getUser(user.getUsername());
        if (check != null) {
            // user already exists
            return false;
        }
        else {
            // add user to database
            userCollection.document(user.getUsername()).set(user);
            return true;
        }
    }

    /**
     * Attempt to get User with given username
     * @param username Username of user to find
     * @return user found (NULL if no user found)
     */
    public User getUser(String username) {
        User result = null;

        DocumentReference userDocRef = userCollection.document(username);
        Task<DocumentSnapshot> task = userDocRef.get();
        while (!task.isComplete()) {}
        result = task.getResult().toObject(User.class);

        return result;
    }

    /**
     * Updates the user's state in the database
     * @param user user to update
     * @return true if update successful, false if unsuccessful (i.e. User does not exist in
     */
    public boolean updateUser(User user) {
        // check if user is in database already
        User check = getUser(user.getUsername());
        if (check == null) {
            // user does not exist
            return false;
        }
        else {
            // update user in database
            userCollection.document(user.getUsername()).set(user);
            return true;
        }
    }

    /**
     * Attempt to delete user from database
     * @param user User to delete
     * @return true if deletion successful, false if unsuccessful (i.e. User doesn't exist)
     */
    public boolean deleteUser(User user) {
        // check if user is in database
        User check = getUser(user.getUsername());
        if (check == null) {
            // user does not exist
            return false;
        }
        else {
            // delete user from database
            userCollection.document(user.getUsername()).delete();
            return true;
        }
    }

    /**
     * Attempt to verify login information
     * @param username the entered username
     * @param password the entered password
     * @return NULL if login information fails, User object if login information correct
     */
    public User checkLogin(String username, String password) {
        // try to get the user from the database
        User user = getUser(username);
        if (user == null) {
            // user does not exist
            return null;
        } else {
            // check password
            if (password.equals(user.getPassword())) {
                // login successful
                return user;
            } else {
                // login failed
                return null;
            }
        }
    }
}
