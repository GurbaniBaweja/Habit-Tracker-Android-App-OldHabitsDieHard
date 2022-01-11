/*
 *  StaticHabitAdapter
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
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;

/**
 * Adapter class for the habits.
 * Different from HabitAdapter, does not support clicking habits or reordering.
 *
 * @author Claire Martin
 * @author Rowan Tilroe
 */
public class StaticHabitAdapter extends ArrayAdapter<Habit> {
    private Context context;
    private ArrayList<Habit> habits;

    /**
     * Constructor
     * @param context the current context
     * @param habits an ArrayList of Habits
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public StaticHabitAdapter(Context context, ArrayList<Habit> habits) {
        super(context, 0, habits);
        this.habits = habits;
        this.context = context;
    }

    /**
     * Gets a view for the list of today's habits so that it can be displayed.
     * @param position the position in the list
     * @param convertView the selected view
     * @param parent the parent view, cannot be null
     * @return the view for the list of today's habits
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // get the convertView
        View view = convertView;
        if (view == null) {
            // inflate layout if view was null
            view = LayoutInflater.from(context).inflate(R.layout.habit_list_content, parent, false);
        }

        // set up textView
        TextView habitTitle = view.findViewById(R.id.habit_title);
        Habit habit = habits.get(position);

        // set the habit title box
        habitTitle.setText(habit.getTitle());

        // show habit score indicator
        int score = habit.followScore();
        if (score == 3) {
            habitTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_score3habit, 0);
        }
        else if (score == 2) {
            habitTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_score2habit, 0);
        }
        else if (score == 1) {
            habitTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_score1habit, 0);
        }
        else {
            habitTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_score0habit, 0);
        }

        return view;
    }
}
