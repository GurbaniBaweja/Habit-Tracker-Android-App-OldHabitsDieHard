/*
 *  FollowerAdapter
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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Custom adapter for the list of users that follow the current user.
 *
 * @author Paige Lekach
 */
public class FollowerAdapter extends ArrayAdapter<String> {
    private Context context;
    private User user;
    UserDatabase db = UserDatabase.getInstance();

    /**
     * Constructor
     * @param context the current context
     * @param user the current user
     */
    public FollowerAdapter(Context context, User user) {
        super(context, 0, user.getFollowers());
        this.user = user;
        this.context = context;
    }

    /**
     * Method to get a view for a list of Followers using this adapter.
     * @param position the index of the follower
     * @param convertView the view to be converted, can be null
     * @param parent the parent ViewGroup, cannot be null
     * @return a View that displays the follower at the specified position
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // get the view
        View view = convertView;
        if(view == null){
            // inflate the layout if the view was not given
            view = LayoutInflater.from(context).inflate(R.layout.followers_view_content, parent,false);
        }

        // get the username of the follower
        String follower = user.getFollowers().get(position);

        // define the textview and set it to show the selected follower
        TextView followerUser = view.findViewById(R.id.follower_user);
        followerUser.setText(follower);

        // define the delete button
        Button deleteFollower = view.findViewById(R.id.delete_follower);

        // listener for delete button
        deleteFollower.setOnClickListener(new View.OnClickListener() {
            /**
             * If the delete button is pressed, the follower is deleted from the
             * user's followers list. The follower no longer follows the current
             * user.
             * @param view the delete button
             */
            @Override
            public void onClick(View view) {
                // remove the follower from this user
                user.getFollowers().remove(follower);
                // get the follower user object
                User secondUser = db.getUser(follower);
                // remove this user from the follower
                secondUser.removeFollowing(user.getUsername());
                // update database
                db.updateUser(user);
                db.updateUser(secondUser);
                notifyDataSetChanged();
            }
        });

        return view;
    }
}
