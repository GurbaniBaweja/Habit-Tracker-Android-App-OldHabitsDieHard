/*
 *  HabitEventFragment
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

import static android.app.Activity.RESULT_OK;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

/**
 * A class for the fragment allowing the user to add, edit, view or delete
 * habit events.
 * @author Gurbani Baweja
 * @author Claire Martin
 */
public class HabitEventFragment extends DialogFragment implements View.OnClickListener, OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMapClickListener {
    private User user;
    private UserDatabase db;
    private Spinner habitEventType;
    private EditText habitEventComment;
    private DatePicker habitEventDate;
    private HabitEventFragment.onFragmentInteractionListener listener;
    private Button uploadButton;
    private Button cameraButton;
    private ImageView img;
    private Button addLocationButton;
    private Button removeLocationButton;
    private TextView locationText;
    private MyMapView mapView;
    private Marker chosenLocation;
    private boolean isLocationSaved;
    private GoogleMap myGoogleMap;
    public LatLng markerLatLng;

    // constants
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    static final int REQUEST_IMAGE_GET = 1;
    static final int REQUEST_IMAGE_CAPTURE = 2;

    /**
     * A listener interface for this fragment to interact with the calling activity.
     */
    public interface onFragmentInteractionListener {
        // abstract methods to be implemented in the activity classes
        void addHabitEvent(HabitEvent event);
        void editHabitEvent(HabitEvent event);
        void deleteHabitEvent(HabitEvent event);
    }

    /**
     * Creates an instance of the HabitEventFragment.
     * @param habitEvent the habit event we are looking at
     * @return the fragment
     */
    public static HabitEventFragment newInstance(HabitEvent habitEvent) {
        // create a bundle and add the habit event to it
        Bundle args = new Bundle();
        args.putSerializable("HabitEvent", habitEvent);

        // create the fragment and add the arguments to it
        HabitEventFragment fragment = new HabitEventFragment();
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * When the fragment is attached, the listener is defined and an exception
     * is thrown if this fails.
     * @param context the context of the current activity
     */
    @Override
    public void onAttach(Context context){
        // get the current user and the database
        user = CurrentUser.get();
        db = UserDatabase.getInstance();
        db.updateUser(user);
        super.onAttach(context);
        // check if the context is an instance of this fragment listener
        if(context instanceof HabitEventFragment.onFragmentInteractionListener){
            // set the listener
            listener = (HabitEventFragment.onFragmentInteractionListener) context;
        }else {
            // the listener was not implemented
            throw new RuntimeException(context.toString()+"must implement OnFragmentInteractionListner");
        }
    }

    /**
     * When the fragment is created, the UI elements are defined and action is
     * taken based on whether the user is adding or editing/deleting a habit
     * event.
     * @param savedInstanceState the current state of the app
     * @return the dialog
     */
    @SuppressLint("ClickableViewAccessibility")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // get the current user
        db.updateUser(user);
        // get storage reference to store images in FireBase
        StorageReference storageRef = db.getStorageRef();

        // set the view
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.habit_event_fragment, null);

        // get view for habit event info objects
        habitEventType = view.findViewById(R.id.habitEventType);
        habitEventComment = view.findViewById(R.id.habitEventComment);
        habitEventDate = view.findViewById(R.id.habitEventDate);
        uploadButton = view.findViewById(R.id.UploadBtn);
        cameraButton = view.findViewById(R.id.TakePhotoButton);
        img = view.findViewById(R.id.habitEventImage);
        addLocationButton = view.findViewById(R.id.addLocationButton);
        removeLocationButton = view.findViewById(R.id.removeLocationButton);
        locationText = view.findViewById(R.id.locationText);

        // default not saving location
        isLocationSaved = false;
        markerLatLng = null;

        // set the map view bundle
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            // get the map view bundle if it was saved
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        // get the mapView object
        mapView = view.findViewById(R.id.mapView);
        // create the mapView
        mapView.onCreate(mapViewBundle);
        // synchronize mapView
        mapView.getMapAsync(this);

        // create a list of habits to populate the spinner
        // the user can only select habits which are already part of the current user
        ArrayList <Habit> habits = user.getHabits();
        ArrayList<String> habitNames = new ArrayList<String>();
        for (int i = 0; i < habits.size(); i++) {
            habitNames.add(habits.get(i).getTitle());
        }

        // create an adapter to display the habit names in the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, habitNames);
        habitEventType.setAdapter(adapter);

        // set listeners for the buttons
        uploadButton.setOnClickListener(this);
        cameraButton.setOnClickListener(this);
        addLocationButton.setOnClickListener(this);
        removeLocationButton.setOnClickListener(this);

        // build the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        if (getArguments() != null) {
            // we are editing a habitEvent
            HabitEvent myEvent = (HabitEvent) getArguments().getSerializable("HabitEvent");

            // set the habit comment
            habitEventComment.setText(myEvent.getComment());

            // set which habit type is selected
            String title = myEvent.getHabit();
            int spinnerPos = adapter.getPosition(title);
            habitEventType.setSelection(spinnerPos);

            // get the date info and set the datePicker to show this info
            int year = myEvent.getYear();
            int month = myEvent.getMonth() - 1;
            int day = myEvent.getDay();
            habitEventDate.updateDate(year, month, day);

            if (myEvent.getHasLocation()) {
                // we have a location
                // get lat and lon and convert to LatLng object
                double lat = myEvent.getLat();
                double lon = myEvent.getLon();
                markerLatLng = new LatLng(lat, lon);
                // set the text to show the location is saved
                locationText.setText("Location is saved!");
            }

            // get the image reference string for this event
            String imgString = myEvent.getImage();

            // if the image reference string exists
            if (imgString != null) {
                // we have an image so get its reference
                StorageReference imgRef = storageRef.child(imgString);

                // get image in the form of a bitmap
                final long ONE_MEGABYTE = 1024 * 1024;
                imgRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    /**
                     * If the image is successfully downloaded, we get it as a
                     * Bitmap object.
                     * @param bytes the byte array of the image
                     */
                    @Override
                    public void onSuccess(byte[] bytes) {
                        // get the image as bitmap
                        Bitmap imgBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        img.setImageBitmap(imgBitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    /**
                     * If the image fails to download print an error.
                     * @param e the error
                     */
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Image download failed!", Toast.LENGTH_LONG).show();
                    }
                });
            }

            // return the dialog
            return builder
                    .setView(view)
                    .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                        /**
                         * When the delete button is pressed, we delete this
                         * habit event and return to the habit event list
                         * activity.
                         * @param dialogInterface the dialog interface
                         * @param i an integer
                         */
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // get reference string for the image
                            String refString = myEvent.getImage();
                            if (refString != null) {
                                // if this event had an image, we delete it from firebase storage
                                StorageReference imgRef = storageRef.child(refString);
                                imgRef.delete();
                            }
                            // delete habit event from the list
                            listener.deleteHabitEvent(myEvent);
                        }
                    })
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        /**
                         * When the ok button is pressed, we save any changes
                         * made to the habit event and return to the habit event
                         * list activity.
                         * @param dialogInterface the dialog interface
                         * @param i an integer
                         */
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // get the updated habit name
                            int pos = habitEventType.getSelectedItemPosition();
                            Habit habit = habits.get(pos);
                            String habitName = habit.getTitle();

                            // get the updated comment
                            String comment = habitEventComment.getText().toString();

                            // get the updated date info for the habit event
                            int day = habitEventDate.getDayOfMonth();
                            int month = habitEventDate.getMonth() + 1;
                            int year = habitEventDate.getYear();
                            LocalDate date = LocalDate.of(year, month, day);

                            // get the location if it was saved
                            if (chosenLocation != null && isLocationSaved) {
                                // we have chosen a location and saved it
                                LatLng eventLoc = chosenLocation.getPosition();
                                myEvent.setLocation(eventLoc);
                            } else if (!isLocationSaved) {
                                // user wants to remove location
                                myEvent.setHasLocation(false);
                            }

                            // get updated image if it exists
                            if (img.getDrawable() != null) {
                                // create reference for existing image if there is one
                                String refString = myEvent.getImage();
                                if (refString != null) {
                                    // delete existing image if there is one
                                    StorageReference imgRef = storageRef.child(refString);
                                    imgRef.delete();
                                }

                                // get the image as a bitmap
                                Bitmap imgBitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();

                                // generate random uuid to reference image
                                UUID uuid = UUID.randomUUID();
                                String uuidStr = uuid.toString();

                                // create reference for this image
                                String newString = user.getUsername() + "/" + uuidStr + ".jpg";
                                StorageReference imgRef = storageRef.child(newString);

                                // convert image to byte array
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] data = baos.toByteArray();

                                // upload image to storage
                                UploadTask uploadTask = imgRef.putBytes(data);
                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                    /**
                                     * If the image upload fails, we display an
                                     * error message.
                                     * @param e the exception that occurred
                                     */
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(), "Image upload failed!", Toast.LENGTH_LONG).show();
                                        myEvent.setImage(newString);
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    /**
                                     * If the image upload is successful, we
                                     * add the image to the selected habit event.
                                     * @param taskSnapshot the upload task
                                     */
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        // add the habit event
                                        myEvent.setImage(newString);
                                    }
                                });
                            }

                            // update event with new info
                            myEvent.setHabit(habitName);
                            myEvent.setComment(comment);
                            myEvent.setDate(date);

                            // add the habit event to the listener
                            listener.editHabitEvent(myEvent);
                        }
                    }).create();
        } else {
            // we are adding a habit event
            return builder
                    .setView(view)
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        /**
                         * When the ok button is pressed, we add this habit event
                         * to the list.
                         * @param dialogInterface the dialog interface
                         * @param i an integer
                         */
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // get the selected habit name
                            int pos = habitEventType.getSelectedItemPosition();
                            Habit habit = habits.get(pos);
                            String habitName = habit.getTitle();

                            // get the comment for the habit event
                            String comment = habitEventComment.getText().toString();

                            // get the date info for the habit event
                            int day = habitEventDate.getDayOfMonth();
                            int month = habitEventDate.getMonth() + 1;
                            int year = habitEventDate.getYear();
                            LocalDate date = LocalDate.of(year, month, day);

                            // create a new habit event with the given name, comment and date
                            HabitEvent newEvent = new HabitEvent(habitName, comment, date);

                            // check if a location was saved
                            if (chosenLocation != null && isLocationSaved) {
                                // we have chosen a location and saved it
                                LatLng eventLoc = chosenLocation.getPosition();
                                newEvent.setLocation(eventLoc);
                            } else if (!isLocationSaved) {
                                newEvent.setHasLocation(false);
                            }

                            // check if an image was saved
                            if (img.getDrawable() != null) {
                                // get image as a bitmap if there is one
                                Bitmap imgBitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();

                                // generate random uuid to reference image
                                UUID uuid = UUID.randomUUID();
                                String uuidStr = uuid.toString();

                                // create reference for this image
                                String refString = user.getUsername() + "/" + uuidStr + ".jpg";
                                StorageReference imgRef = storageRef.child(refString);

                                // convert image to byte array
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] data = baos.toByteArray();

                                // upload image to storage
                                UploadTask uploadTask = imgRef.putBytes(data);
                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                    /**
                                     * If the upload fails, print an error
                                     * message.
                                     * @param e the exception that occurred
                                     */
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(), "Image upload failed!", Toast.LENGTH_LONG).show();
                                        newEvent.setImage(refString);
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    /**
                                     * If the upload is successful, add the
                                     * image to the event.
                                     * @param taskSnapshot the upload task
                                     */
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        // add the image to the new event
                                        newEvent.setImage(refString);
                                    }
                                });
                            }
                            // add the new event to the list
                            listener.addHabitEvent(newEvent);
                        }
                    }).create();
        }
    }

    /**
     * When the one of the buttons is clicked, we check which button it was
     * and then perform its action.
     * @param view the button that was clicked
     */
    @Override
    public void onClick(View view) {
        // create an empty intent
        Intent intent;

        if (view.getId() == R.id.UploadBtn) {
            // we are uploading a photo
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        } else if (view.getId() == R.id.TakePhotoButton) {
            // we are taking a photo
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            } catch (ActivityNotFoundException e) {
                // error
            }
        } else if (view.getId() == R.id.addLocationButton) {
            // we want to save our location
            isLocationSaved = true;
            locationText.setText("Location is saved!");
        } else if (view.getId() == R.id.removeLocationButton) {
            // we don't want to save our location
            isLocationSaved = false;
            locationText.setText("Location is not saved!");
        }
    }

    /**
     * When the image activity gets a result, we check to see if it worked; if
     * so, we add the image bitmap to the extras bundle as "data".
     * @param requestCode the code representing whether the image was uploaded
     *                    or taken with the camera
     * @param resultCode the code representing whether the intent was
     *                   successful
     * @param data the intent to add the image to upon success
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            // we were uploading an image and it worked
            // get the image from the data bundle
            Uri imgUri = data.getData();
            try {
                // decode the image and display it
                img.setImageBitmap(ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContext().getContentResolver(), imgUri)));
            } catch (IOException e) {
                // something failed
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // we were capturing an image with the camera and it worked
            // get the image from the data bundle
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            // display the image
            img.setImageBitmap(imageBitmap);
        }
    }

    /**
     * Defines what to do when the instance state is saved.
     * @param outState a bundle representing the new state
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // get the mapview bundle
        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            // we have no mapview bundle so create one and add it to the outState
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        // save the mapview instance state
        mapView.onSaveInstanceState(mapViewBundle);
    }

    /**
     * Defines what the do when the activity is resumed.
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * Defines what to do when the activity is started.
     */
    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    /**
     * Defines what to do when the activity is stopped.
     */
    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    /**
     * When the map is ready, we update its settings and camera view, then save
     * any location that the user has clicked as a marker.
     * @param map the GoogleMap object
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap map) {
        // save the map in a global variable
        myGoogleMap = map;

        // allow the user to zoom in or out
        myGoogleMap.getUiSettings().setZoomControlsEnabled(true);

        // check if we have location permissions for the app
        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            // if we have permissions, we can enable user location
            myGoogleMap.setMyLocationEnabled(true);
            if (markerLatLng != null) {
                // we have clicked a habit event that already has latlng
                // move the camera to the current marker location
                myGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng, 6));

                // reset the chosen location
                if (chosenLocation != null) {
                    chosenLocation.remove();
                    chosenLocation = null;
                }
                // add a marker at the location that the user clicked
                chosenLocation = myGoogleMap.addMarker(
                        new MarkerOptions()
                                .position(markerLatLng)
                                .title("Marker")
                                .draggable(false)
                );
            }
            // add the my location button to zoom into user's current location
            myGoogleMap.setOnMyLocationButtonClickListener(this);
            // allow user to click on the map
            myGoogleMap.setOnMapClickListener(this);
        } else {
            // we do not already have permission so we need to get it
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            // try again
            if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {
                // enable location
                myGoogleMap.setMyLocationEnabled(true);

                if (markerLatLng != null) {
                    // we have clicked a habit event that already has latlng
                    myGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng, 6));

                    // reset the chosen location
                    if (chosenLocation != null) {
                        chosenLocation.remove();
                        chosenLocation = null;
                    }

                    // add a marker at the location the user clicked
                    chosenLocation = myGoogleMap.addMarker(
                            new MarkerOptions()
                                    .position(markerLatLng)
                                    .title("Marker")
                                    .draggable(false)
                    );
                }
                // set my location button
                myGoogleMap.setOnMyLocationButtonClickListener(this);
                // allow user to click on map
                myGoogleMap.setOnMapClickListener(this);
            } else {
                // user denied permission
                Toast.makeText(getActivity(), "Location permissions are required to use map feature", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Defines what to do when the my location button is clicked.
     * @return false
     */
    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    /**
     * When the map is clicked, we add a marker at the location that was clicked.
     * @param point the point that was clicked
     */
    @Override
    public void onMapClick(LatLng point) {
        // reset the chosen location global variable
        if (chosenLocation != null) {
            chosenLocation.remove();
            chosenLocation = null;
        }
        // add a marker at the point that was clicked and save this marker globally
        chosenLocation = myGoogleMap.addMarker(
                new MarkerOptions()
                        .position(point)
                        .title("Marker")
                        .draggable(false)
        );
    }

    /**
     * Defines what to do when the activity is paused.
     */
    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    /**
     * Defines what to do when the activity is destroyed.
     */
    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    /**
     * Defines what to do when the activity is low on memory.
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}