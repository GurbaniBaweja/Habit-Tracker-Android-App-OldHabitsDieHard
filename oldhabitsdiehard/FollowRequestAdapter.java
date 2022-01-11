/*
 *  FollowRequestAdapter
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
 * Custom adapter for the FollowRequest class.
 *
 * @author Rowan Tilroe
 */
public class FollowRequestAdapter extends ArrayAdapter<FollowRequest> {
    private Context context;
    private User user;

    /**
     * Constructor
     * @param context the current context
     * @param user the current user
     */
    public FollowRequestAdapter(Context context, User user) {
        super(context, 0, user.getFollowRequests());
        this.user = user;
        this.context = context;
    }

    /**
     * Gets a view for the list of follow requests for the current user.
     * @param position the index of the follow request
     * @param convertView the view to be converted, can be null
     * @param parent the parent ViewGroup, cannot be null
     * @return a View that displays the request at the specified position
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // get the view
        View view = convertView;
        if(view == null){
            // inflate the layout if the view was not given
            view = LayoutInflater.from(context).inflate(R.layout.followrequest_content, parent,false);
        }
        // Retrieve associated FollowRequest object
        FollowRequest followRequest = user.getFollowRequests().get(position);

        // set the text of the textview
        TextView requestText = view.findViewById(R.id.requested_user);
        requestText.setText(followRequest.getFollower());

        // Button functionality
        Button confirmButton = view.findViewById(R.id.confirm_request);
        Button deleteButton = view.findViewById(R.id.delete_follower);

        // listener for the confirm button
        confirmButton.setOnClickListener(new View.OnClickListener() {
            /**
             * If the confirm button is clicked, the follow request is accepted
             * and the users are added to each other's follower/following lists.
             * @param view the confirm button
             */
            @Override
            public void onClick(View view) {
                // accept request
                followRequest.accept();
                // remove the request from this user
                user.getFollowRequests().remove(followRequest);
                notifyDataSetChanged();
            }
        });

        // listener for the delete button
        deleteButton.setOnClickListener(new View.OnClickListener() {
            /**
             * If the delete button is clicked, the request is denied and the
             * request is removed from this user's follow requests list.
             * @param view the delete button
             */
            @Override
            public void onClick(View view) {
                // deny the request
                followRequest.deny();
                // remove the request from this user
                user.getFollowRequests().remove(followRequest);
                notifyDataSetChanged();
            }
        });

        return view;
    }
}
