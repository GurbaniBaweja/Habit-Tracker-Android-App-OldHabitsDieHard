/*
 *  FollowRequest
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

/**
 * Class that holds follow requests between users. Follow requests are held by
 * the followee (user that is to be followed).
 *
 * @author Rowan Tilroe
 */
public class FollowRequest {
    private String follower; // User that wants to follow
    private String followee; // User that is to be followed

    // Empty constructor for Firestore compatibility
    public FollowRequest() {}

    /**
     * Constructor
     * @param follower the user that wants to follow
     * @param followee the user that is to be followed
     */
    public FollowRequest(String follower, String followee) {
        this.follower = follower;
        this.followee = followee;
    }

    /**
     * Followee getter
     * @return the user that is to be followed)
     */
    public String getFollowee() { return followee; }

    /**
     * Follower getter
     * @return the user that wants to follow
     */
    public String getFollower() { return follower; }

    /**
     * Remove the follow request
     */
    private void delete() {
        // get the database
        UserDatabase db = UserDatabase.getInstance();
        // remove this request from the followee
        User followeeUser = db.getUser(followee);
        followeeUser.getFollowRequests().remove(this);
        // update database
        db.updateUser(followeeUser);
    }

    /**
     * Accept the follow request
     */
    public void accept() {
        // Get the users
        UserDatabase db = UserDatabase.getInstance();
        User followerUser = db.getUser(follower);
        User followeeUser = db.getUser(followee);

        // Add the follower to the followee's followers list
        followeeUser.getFollowers().add(follower);

        // Add the followee to the follower's following list
        followerUser.getFollowing().add(followee);

        // Update database with changes
        db.updateUser(followerUser);
        db.updateUser(followeeUser);

        // Remove follow request
        delete();
    }

    /**
     * Deny the follow request
     */
    public void deny() {
        // Do nothing and delete the request
        delete();
    }

    /**
     * Overriding equals method
     * @param o object to compare to
     * @return true if equals, false if not
     */
    @Override
    public boolean equals(Object o) {
        // Object is itself
        if (o == this) {
            return true;
        }

        // Object is not instance of followRequest
        if (!(o instanceof FollowRequest)) {
            return false;
        }

        // Cast object
        FollowRequest fr = (FollowRequest) o;

        // Compare follower & followee
        return fr.getFollowee().equals(followee) && fr.getFollower().equals(follower);
    }
}
