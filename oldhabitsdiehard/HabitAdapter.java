/*
 *  HabitAdapter
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Custom adapter for Habits.
 *
 * @author Filippo Ciandy
 */
public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.ViewHolder> {
    private ArrayList<Habit> HabitList;
    private AdapterView.OnItemClickListener ItemClickListener;

    /**
     * Constructor
     * @param HabitList the list of habits to display
     * @param ItemClickListener a listener to see if the habit was clicked
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public HabitAdapter(ArrayList<Habit> HabitList,AdapterView.OnItemClickListener ItemClickListener) {
        this.HabitList = HabitList;
        this.ItemClickListener = ItemClickListener;
    }

    /**
     * Inflates row layout from habit_list_content.
     * @param parent the parent ViewGroup, cannot be null
     * @param viewType the type of view
     * @return a ViewHolder for the list of habits
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate the layout
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.habit_list_content,parent,false);

        // create the viewholder and return
        return new ViewHolder(view);

    }

    /**
     * Binds Habit title in dedicated position to TextView in each row.
     * @param holder a ViewHolder, cannot be null
     * @param _ an integer
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int _) {
        // get the position
        int position = holder.getAdapterPosition();
        // get the habit at this position
        Habit habit = HabitList.get(position);

        // get the habit title and set the textview to display it
        holder.textView.setText(habit.getTitle());

        // get the habit's score
        int score = habit.followScore();

        // set the indicator based on the score
        if (score == 3) {
            holder.textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_score3habit, 0);
        }
        else if (score == 2) {
            holder.textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_score2habit, 0);
        }
        else if (score == 1) {
            holder.textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_score1habit, 0);
        }
        else {
            holder.textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_score0habit, 0);
        }

        // listener for when a habit is clicked
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            /**
             * If a habit is clicked, we use the ItemClickListener defined in
             * this class.
             * @param view the habit that was clicked
             */
            @Override
            public void onClick(View view) {
                ItemClickListener.onItemClick(null,null,position,position);
            }
        });
    }

    /**
     * Get total number of rows
     * @return total number of rows
     */
    @Override
    public int getItemCount() {
        return HabitList.size();
    }

    /**
     * Recycles and stores view when list is scrolled out of the screen
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;

        /**
         * Constructor
         * @param itemView a view that cannot be null
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // get the textview and define its listener
            textView = itemView.findViewById(R.id.habit_title);
            itemView.setOnClickListener(this);
        }

        /**
         * Defines what action to take when a habit is clicked.
         * @param v the habit ciew that was clicked
         */
        @Override
        public void onClick(View v) {

        }
    }
}
