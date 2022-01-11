/*
 *  FollowingAdapter
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

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Custom adapter for the list of users that the current user is following.
 *
 * @author Paige Lekach
 */
public class FollowingAdapter extends ArrayAdapter<String> {
    private Context context;
    private User user;
    UserDatabase db = UserDatabase.getInstance();

    /**
     * Constructor
     * @param context the current context
     * @param user the current user
     */
    public FollowingAdapter(Context context, User user) {
        super(context, 0, user.getFollowing());
        this.user = user;
        this.context = context;
    }

    /**
     * Method to get a view for a list of Following users using this adapter.
     * @param position the position of the user
     * @param convertView the view to be converted, can be null
     * @param parent the parent ViewGroup, cannot be null
     * @return a View that displays the following user at the specified position
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // get the view
        View view = convertView;
        if(view == null){
            // inflate the layout if the view was not given
            view = LayoutInflater.from(context).inflate(R.layout.following_view_content, parent,false);
        }

        // get the username of the user
        String follow = user.getFollowing().get(position);

        // define the textview and set it to show the selected user
        TextView followingUser = view.findViewById(R.id.following_user);
        followingUser.setText(follow);

        // define the unfollow button
        Button unFollowingButton = view.findViewById(R.id.unfollow_button);

        followingUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User secondUser = db.getUser(follow);
                Intent intent = new Intent(view.getContext(), FollowingUserActivity.class);
                intent.putExtra("username",secondUser.getUsername());
                startActivity(view.getContext(),intent, null);
            }
        });
        // listener for the unfollow button
        unFollowingButton.setOnClickListener(new View.OnClickListener() {
            /**
             * If the unfollow button is presed, the selected user is removed
             * from the current user's following list, and the current user is
             * removed from the selected user's followers. The current user no
             * longer follows the selected user.
             * @param view the unfollow button
             */
            @Override
            public void onClick(View view) {
                // unfollow the selected user
                user.getFollowing().remove(follow);
                // get the selected user object
                User secondUser = db.getUser(follow);
                // remove current user from the selected user's follower list
                secondUser.removeFollower(user.getUsername());
                // update database
                db.updateUser(user);
                db.updateUser(secondUser);
                notifyDataSetChanged();
            }
        });

        return view;
    }
}


