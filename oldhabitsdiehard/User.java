/*
 *  User
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

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * This class represents a user.
 *
 * @author Rowan Tilroe
 * @author Claire Martin
 */
public class User {
    private String username;
    private String password;
    private String bio;
    private ArrayList<Habit> habits;
    private ArrayList<HabitEvent> habitEvents;
    private ArrayList<String> following;
    private ArrayList<String> followers;
    private ArrayList<FollowRequest> followRequests;

    /**
     * User constructor
     * @param username User's username (for login)
     * @param password User's password (for login)
     */
    public User(String username, String password) throws IllegalArgumentException {
        setUsername(username);
        setPassword(password);
        setBio("");
        habits = new ArrayList<Habit>();
        habitEvents = new ArrayList<HabitEvent>();
        following = new ArrayList<String>();
        followers = new ArrayList<String>();
        followRequests = new ArrayList<FollowRequest>();
    }

    /**
     * User constructor
     * @param username User's username (for login)
     * @param password User's password (for login)
     * @param bio User's bio (for profile)
     */
    public User(String username, String password, String bio) throws IllegalArgumentException {
        setUsername(username);
        setPassword(password);
        setBio(bio);
        habits = new ArrayList<Habit>();
        habitEvents = new ArrayList<HabitEvent>();
        following = new ArrayList<String>();
        followers = new ArrayList<String>();
        followRequests = new ArrayList<FollowRequest>();
    }

    /**
     * Empty constructor for firestore compatibility
     */
    public User() {}

    /**
     * Username getter
     * @return User's username
     */
    public String getUsername() { return username; }

    /**
     * Password getter
     * @return User's password
     */
    public String getPassword() { return password; }

    /**
     * Habits getter
     * @return the list of habits belonging to this user
     */
    public ArrayList<Habit> getHabits() { return habits; }

    /**
     * HabitEvents getter
     * @return the list of habits events belonging to this user
     */
    public ArrayList<HabitEvent> getHabitEvents() {
        return habitEvents;
    }

    /**
     * Bio getter
     * @return User's bio
     */
    public String getBio() { return bio; }

    /**
     * Following getter
     * @return Users that this user is following
     */
    public ArrayList<String> getFollowing() { return following; }

    /**
     * Followers getter
     * @return Users that are following this user
     */
    public ArrayList<String> getFollowers() { return followers; }

    /**
     * FollowRequests getter
     * @return Incoming follow requests
     */
    public ArrayList<FollowRequest> getFollowRequests() { return followRequests; }

    /**
     * Username setter
     * @param username the user's username
     * @throws IllegalArgumentException
     */
    public void setUsername(String username) throws IllegalArgumentException {
        // make sure length is at least 1
        if (username.length() < 1) {
            throw new IllegalArgumentException();
        }
        else {
            this.username = username;
        }
    }

    /**
     * Password setter
     * @param password the user's password
     * @throws IllegalArgumentException
     */
    public void setPassword(String password) throws IllegalArgumentException {
        // make sure length is at least 1
        if (password.length() < 1) {
            throw new IllegalArgumentException();
        }
        else {
            this.password = password;
        }
    }

    /**
     * Bio setter
     * @param bio the user's bio
     */
    public void setBio(String bio) {
        this.bio = bio;
    }

    /**
     * Add a habit to this user's habit list.
     * @param habit the habit to be added
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addHabit(Habit habit) {
        habits.add(habit);
    }

    /**
     * Add a habit event to this user's habit event list
     * @param event the event to be added
     */
    public void addHabitEvent(HabitEvent event) {
        // find the name of the habit this event belongs to
        String habitName = event.getHabit();
        for (int i = 0; i < habits.size(); i++) {
            Habit curr = habits.get(i);
            if (curr.getTitle().equals(habitName)) {
                // found the habit
                // add this event to the habit object
                curr.addHabitEvent(event);
                // add this event to the list of habit events for this user
                habitEvents.add(event);
            }
        }
    }

    /**
     * Add a FollowRequest
     * @param request the request to add
     */
    public void addFollowRequest(FollowRequest request){
      this.followRequests.add(request);
    }

    /**
     * Remove a FollowRequest
     * @param request the request to remove
     */
    public void removeFollowRequest(FollowRequest request){
        this.followRequests.remove(request);
    }

    /**
     * Remove a Following user
     * @param s the name of the user to remove
     */
    public void removeFollowing(String s){
        this.following.remove(s);
    }

    /**
     * Remove a Follower
     * @param s the name of the user to remove
     */
    public void removeFollower(String s){
        this.followers.remove(s);
    }

    /**
     * Delete a habit from this user.
     * @param habit the habit to be deleted
     */
    public void deleteHabit(Habit habit) {
        // remove the habit from the list
        habits.remove(habit);
        for (int i = 0; i < habitEvents.size(); i++) {
            if (habitEvents.get(i).getHabit().equals(habit.getTitle())) {
                // remove any events associated with it
                habitEvents.remove(i);
            }
        }
    }

    /**
     * Delete a habit event from this user.
     * @param event the event to be deleted
     */
    public void deleteHabitEvent(HabitEvent event) {
        // get the habit this event belongs to
        String habitName = event.getHabit();
        for (int i = 0; i < habits.size(); i++) {
            if (habits.get(i).getTitle().equals(habitName)) {
                // remove the event from the habit too
                habits.get(i).removeHabitEvent(event);
            }
        }
        // remove habit event from list
        habitEvents.remove(event);
    }

    /**
     * Get the habits to be done today from this user's habit list.
     * The list is recalculated every time this method is called so that it is
     * always updated with the correct date. Use this method to create the
     * Today page.
     * @return an ArrayList of the habits to be done today
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<Habit> getTodayHabits() {
        ArrayList<Habit> todayHabits = new ArrayList<Habit>();
        // 1 is Monday, 7 is Sunday
        int today = LocalDate.now().getDayOfWeek().getValue();
        if (today == 7) {
            // it's sunday, switch to 0 to match our notation
            today = 0;
        }
        for (int i = 0; i < habits.size(); i++) {
            // iterate through habits in list for this user
            if (habits.get(i).getWeekdays().get(today)) {
                // habit i is performed on this day
                todayHabits.add(habits.get(i));
            }
        }
        return todayHabits;
    }

    /**
     * Generates a follow request to follow another user
     * @param user user to follow
     */
    public void requestToFollow(User user) {
        // Not allowed to follow self
        if (user == this) {
            return;
        }

        // Generate follow request
        FollowRequest followRequest = new FollowRequest(this.getUsername(), user.getUsername());

        // Check if either already following, or request already created
        ArrayList<FollowRequest> theirFollowRequests = user.getFollowRequests();
        if (theirFollowRequests.contains(followRequest) || following.contains(user.getUsername())) {
            return;
        }

        // Add follow request
        theirFollowRequests.add(new FollowRequest(this.getUsername(), user.getUsername()));

        // Update database with new follow request
        UserDatabase db = UserDatabase.getInstance();
        db.updateUser(user);
        db.updateUser(this);
    }

    /**
     * Unfollow the given user
     * @param user user to unfollow
     */
    public void unfollow(User user) {
        // Remove given user from this user's following array
        following.remove(user.getUsername());

        // Remove this user from given user's followers array
        user.getFollowers().remove(this.username);

        // Update both users in database
        UserDatabase db = UserDatabase.getInstance();
        db.updateUser(user);
        db.updateUser(this);
    }

    /**
     * Gets all this user's public habits
     * @return ArrayList of public habits for this user
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<Habit> getPublicHabits() {
        return (ArrayList<Habit>) habits.stream().filter(habit -> habit.getPublic()).collect(Collectors.toList());
    }
}
